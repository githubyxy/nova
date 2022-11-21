package com.yxy.nova.mwh.kafka.util;

import org.apache.kafka.common.errors.*;

/**
 * shit code as you can see
 */
public class ErrorHelper {

    public static boolean isRecoverable(final Exception e) {
        if (e == null) return true;
        if (e instanceof CorruptRecordException) {
            return true;
        }
        if (e instanceof InvalidMetadataException) {
            return true;
        }
        if (e instanceof NotEnoughReplicasAfterAppendException) {
            return true;
        }
        if (e instanceof NotEnoughReplicasException) {
            return true;
        }
        if (e instanceof OffsetOutOfRangeException) {
            return true;
        }
        if (e instanceof TimeoutException) {
            return true;
        }
        if (e instanceof UnknownTopicOrPartitionException) {
            return true;
        }
        return false;
    }
}
