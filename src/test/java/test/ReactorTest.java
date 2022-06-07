package test;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

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


        Mono<Tuple2<Integer, Integer>> zip = Mono.zip(Mono.just(1), Mono.just(2));
        Mono<Integer> mono1 = zip.flatMap(i -> {
            Integer t1 = i.getT1();
            Integer t2 = i.getT2();
            return Mono.just(t1 + t2);
        });
        mono1.doOnNext( i -> System.out.println(i)).subscribe();


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
    public void splitTest() {
        Flux<Integer> flux = Flux.range(1, 39);

        flux.window(4).map(i -> {
            i.map(j -> {
                System.out.print(j + " ");
                return j;
            }).subscribe();
            System.out.println();
            return i;
        }).subscribe();

        flux.buffer(4).map(i -> {
            i.forEach(System.out::print);

            System.out.println();
            return i;
        }).subscribe();

    }

}
