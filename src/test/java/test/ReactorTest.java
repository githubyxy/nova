package test;

import com.alibaba.fastjson.JSON;
import com.yxy.nova.mwh.utils.text.TextUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author yuxiaoyu
 * @date 2022/5/24 上午8:16
 * @Description
 */
public class ReactorTest {

    @Test
    public void test() {
        Flux<String> flux = Flux.just("1", "2", "3");
        Flux<Integer> generate = Flux.generate(() -> 0, (v, sink) -> {
            if (v == 5) {
                sink.complete();
            }
            sink.next(v++);
            return v;
        });
//        generate.subscribe(i -> System.out::print);

//        flux.doOnNext(i -> System.out.println(i));
//        flux.doOnNext(i -> System.out.println(i)).subscribe();

//        Flux<Long> interval = Flux.interval(Duration.of(10, ChronoUnit.SECONDS));
//        interval.subscribe(l -> System.out.println(l + ""));

        Flux.generate(()->0, (i, sink) ->{
            if (i >= 10) {
                sink.complete();
            }
            sink.next(i++);
            return i;
        }).subscribe(System.out::println);

//        Flux.create(sink -> {
//            for (int i = 0; i < 10; i++) {
//                sink.next(i);
//            }
//
//            sink.complete();
//
//        }).subscribe(System.out::println);

    }


    @Test
    public void bufferTest() {
//        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
//
//        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);

        Flux.range(1, 10)
                .buffer(5, 3)
                .subscribe(System.out::println);
    }

    @Test
    public void MonoTest() {


//        Mono<Tuple2<Integer, Integer>> zip = Mono.zip(Mono.just(1), Mono.just(2));
//        Mono<Integer> mono1 = zip.flatMap(i -> {
//            Integer t1 = i.getT1();
//            Integer t2 = i.getT2();
//            return Mono.just(t1 + t2);
//        });
//        mono1.doOnNext( i -> System.out.println(i)).subscribe();


        List<A> list = new ArrayList<>();
        list.add(new A("y",1));
        list.add(new A("x",1));
        list.add(new A("x",1));
        Flux.fromIterable(list).collectList()
                .flatMap(alist -> {
            Map<String, List<ReactorTest.A>> map = new HashMap<>();
                    alist.forEach(e -> {
                        String key = e.getName();
                        List<A> innerList = map.getOrDefault(key, new ArrayList<>());
                        innerList.add(e);
                        map.put(key, innerList);
                    });
                    return Mono.just(map);
                }).flatMap(map ->{
                    return Flux.fromIterable(map.entrySet())
                            .flatMap(entry -> {
                                System.out.println(entry.getKey());
                                System.out.println(entry.getValue());
                                return Mono.just(entry);
                            }).collectList();
                    }).subscribe();

    }

    public class A {
        private A(String name, int value) {
            this.name = name;
            this.value = value;

        }
        private String name;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    @Test
    public void mapTest() {
        Flux<Integer> flux = Flux.range(1, 4);
        flux.map( i -> {
            System.out.println(i*2);
            return i*2;
        }).subscribe();


        flux.flatMap(i -> {
            return Flux.just(i*2).doOnNext(System.out::println);
        }).subscribe();

        flux.count().doOnNext(System.out::println).subscribe();

    }


    @Test
    public void splitTest() throws DecoderException {
//        byte[] a= new byte[]{76, 32, 57, -64, 3, -14, -69, 69};
        char[] a= new char[]{89, 111, 159, 94, 29, 32, 206, 173};
        byte[] bytes = Hex.decodeHex(a);
        System.out.println(bytes);
        //                  4c2039c03f2b b45

    }



    private static String bytesToHexStr(byte[] b) {
        if (b == null) return "";
        StringBuffer strBuffer = new StringBuffer(b.length * 3);
        for (int i = 0; i < b.length; i++) {
            strBuffer.append(Integer.toHexString(b[i] & 0xff));
        }
        return strBuffer.toString();
    }


    @Test
    public void test4() throws UnsupportedEncodingException {
        String meterId = "37646";
        while (meterId.length() < 6) {
            meterId = "0" + meterId;
        }
        System.out.println(meterId);
    }


    @Test
    public void testR() {
//         String[] regex = {
//                ".*\\d+.*", //数字
//                ".*[A-Z]+.*", //大写字母
//                ".*[a-z]+.*", //小写字母
//                ".*[^x00-xff]+.*", //2位字符(中文?)
//                ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*", //特殊符号
//        };

         String regex = "^[0-9a-zA-Z_\\-]+$";

        System.out.println("12314234".matches(regex));

    }

    @Test
    public void sorttest() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");

         Flux.fromIterable(list)
                .flatMap((item) -> {
                    return Mono.just(item);
                }).collectList()
                .flatMap((list1) -> {
                    return Mono.just(list1);
                }).flatMap(obj -> {
                    System.out.println(JSON.toJSONString(obj));
                    return Mono.just(obj);
                }).subscribe();
    }

       EmitterProcessor<String> myexchangeProcessor = EmitterProcessor.create(false);
        FluxSink<String> mysink = myexchangeProcessor.sink();
    @Test
    public void test1() {
        Disposable subscribe = myexchangeProcessor.subscribe(s -> {
            System.out.println(s + ":");
        });
        mysink.next("1");
        mysink.next("2");
        mysink.next("3");

    }

    @Test
    public void test2() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("d");
        list.add("e");
        list.add("e");
        list.add("");
        list.add("g");

//        List<String> collect = list.stream().distinct().collect(Collectors.toList());
        list = list.stream().filter(l -> {
            return !l.equals("a");
        }).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(list));
    }

}
