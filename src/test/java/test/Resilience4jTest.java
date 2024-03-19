package test;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class Resilience4jTest {

    @SneakyThrows
    @Test
    public void circuitbreaker() {

        // Create a custom configuration for a CircuitBreaker
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
//                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(4000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowSize(1)
                .recordExceptions(Exception.class, TimeoutException.class)
//                .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

        // Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("yxy的熔断器");

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Supplier<String> decoratedSupplier = CircuitBreaker
                    .decorateSupplier(circuitBreaker, () -> {
                       return MyService.doSomething(finalI);
                    });

            String result = Try.ofSupplier(decoratedSupplier)
                    .recover(throwable -> "Hello from Recovery").get();
            System.out.println(result);

            circuitBreaker.getMetrics();
            CircuitBreaker.State state = circuitBreaker.getState();
            System.out.println("state:" + state.name());

            Thread.sleep(2000);
        }


    }

    static class MyService {
        public static String doSomething(int j){
            int i = 10/j;
            return "Hello from MyService";
        }
    }

//    @Test
//    public void test() {
//        // Create a CircuitBreaker with default configuration
//        CircuitBreaker circuitBreaker = CircuitBreaker
//                .ofDefaults("backendService");
//
//// Create a Retry with default configuration
//// 3 retry attempts and a fixed time interval between retries of 500ms
//        Retry retry = Retry
//                .ofDefaults("backendService");
//
//// Create a Bulkhead with default configuration
//        Bulkhead bulkhead = Bulkhead
//                .ofDefaults("backendService");
//
//        Supplier<String> supplier = () -> backendService
//                .doSomething(param1, param2)
//
//// Decorate your call to backendService.doSomething()
//// with a Bulkhead, CircuitBreaker and Retry
//// **note: you will need the resilience4j-all dependency for this
//        Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
//                .withCircuitBreaker(circuitBreaker)
//                .withBulkhead(bulkhead)
//                .withRetry(retry)
//                .decorate();
//
//// Execute the decorated supplier and recover from any exception
//        String result = Try.ofSupplier(decoratedSupplier)
//                .recover(throwable -> "Hello from Recovery").get();
//
//// When you don't want to decorate your lambda expression,
//// but just execute it and protect the call by a CircuitBreaker.
//        String result = circuitBreaker
//                .executeSupplier(backendService::doSomething);
//
//// You can also run the supplier asynchronously in a ThreadPoolBulkhead
//        ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkhead
//                .ofDefaults("backendService");
//
//// The Scheduler is needed to schedule a timeout
//// on a non-blocking CompletableFuture
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
//        TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));
//
//        CompletableFuture<String> future = Decorators.ofSupplier(supplier)
//                .withThreadPoolBulkhead(threadPoolBulkhead)
//                .withTimeLimiter(timeLimiter, scheduledExecutorService)
//                .withCircuitBreaker(circuitBreaker)
//                .withFallback(asList(TimeoutException.class,
//                                CallNotPermittedException.class,
//                                BulkheadFullException.class),
//                        throwable -> "Hello from Recovery")
//                .get().toCompletableFuture();
//    }
}
