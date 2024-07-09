package test;

import com.yxy.nova.mwh.utils.concurrent.BoundedExecutor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    @SneakyThrows
    @Test
    public void test() {
        System.out.println(0 % 1);
    }

    @SneakyThrows
    private int execCost(int cost) {
        Thread.sleep(1000);
        cost += 1;
        return cost;
    }

}
