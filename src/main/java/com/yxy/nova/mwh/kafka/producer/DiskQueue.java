package com.yxy.nova.mwh.kafka.producer;

import com.yxy.nova.mwh.kafka.object.ProducerException;
import com.yxy.nova.mwh.kafka.object.QueueJob;
import com.yxy.nova.mwh.kafka.tape2.QueueFile;
import com.yxy.nova.mwh.kafka.util.ErrorHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

//not thread-safe, only one thread is allowed here !!!
public class DiskQueue extends Thread implements Closeable {

    protected static final Logger log = LoggerFactory.getLogger("module-kafka");
    boolean closed = false;

    final IProducer producer;
    final String file;
    final QueueFile queueFile;

    public DiskQueue(final IProducer producer, final String file) throws IOException {
        this.producer = producer;
        this.file = file;
        this.queueFile = new QueueFile.Builder(new File(file)).build();
        log.info("DiskQueue initiated@{} and size is {}", file, this.queueFile.size());
        this.setName("KafkaDiskQueue-"+file);
    }

    public int append(final QueueJob job) {
        try {
            queueFile.add(job.serialize());
        } catch (IOException e) {
            log.error("unable to append to disk queue", e);
        }
        return queueFile.size();
    }

    @Override
    public void run() {
        try {
            while (!closed) {
                try {
                    final QueueJob job = QueueJob.deserialize(queueFile.peek());
                    if (job == null) continue;
                    if (job.getAge() > 3) {
                        log.error("fail to resend msg:{},base64-data:{}",
                                job.getMessageKey(),
                                Base64.encodeBase64String(job.getMessage())
                        );
                        removeHead();
                        continue;
                    }
                    if (job.isOldEnough()) {
                        producer.produce(job.getTopic(), job.getMessageKey(), job.getMessage(), new Callback() {
                            @Override
                            public void onCompletion(final RecordMetadata metadata, final Exception exception) {
                                if (exception != null) {
                                    if (ErrorHelper.isRecoverable(exception)) {
                                        log.error("attempt {} for msg:{} failed again but recoverable", job.getAge(), job.getMessageKey());
                                        job.renew();
                                        DiskQueue.this.append(job);
                                    }
                                } else {
                                    log.warn("resend completed for msg:{}", job.getMessageKey());
                                }
                                removeHead();
                            }
                        });
                    }
                } catch (ProducerException e) {
                    log.error("failed to resend.this should never happen", e);
                    removeHead();//remove it anyway
                } finally {
                    Thread.sleep(200);
                }
            }
        } catch (InterruptedException | IOException e) {
            log.error("failed to peek from queue due to fatal error:" + file, e);
        }
    }

    private void removeHead() {
        try {
            queueFile.remove();
        } catch (IOException e) {
            log.error("failed to remove head", e);
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
        queueFile.close();
        log.info("DiskQueue closed {}", file);
    }
}
