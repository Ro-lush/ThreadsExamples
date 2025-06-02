package ex3;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class DeadLockTest {
    @Test
    void testDeadlock() throws InterruptedException {
        DeadlockService service = new DeadlockService();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(service::method1);
        executor.submit(service::method2);

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);  // Деадлок через ~100 мс!
    }
}
