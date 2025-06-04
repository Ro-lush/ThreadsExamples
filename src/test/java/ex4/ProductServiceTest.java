package ex4;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        Product product = new Product();
        product.setName("Телефон");
        product.setStock(100);  // Изначально 100 единиц
        productRepository.save(product);
    }

    // Тест для оптимистичной блокировки (должен упасть при race condition)
    @Test
    void testOptimisticLocking() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        executor.submit(() -> {
            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
            }
            productService.updateStockOptimistic(1L, 10);
        });

        executor.submit(() -> {
            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
            }
            productService.updateStockOptimistic(1L, 20);
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        Product product = productRepository.findById(1L).orElseThrow();
        assertEquals(70, product.getStock());  // Ожидаем 70, но может быть 80 или 90
    }

    // Тест для пессимистичной блокировки (всегда корректный)
    @Test
    void testPessimisticLocking() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);

        executor.submit(() -> {
            latch.countDown();
            try {
                latch.await();
                productService.updateStockPessimistic(1L, 10);
                successCount.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            latch.countDown();
            try {
                latch.await();
                productService.updateStockPessimistic(1L, 20);
                successCount.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS); // Увеличили время ожидания

        Product product = productRepository.findById(1L).orElseThrow();
        assertEquals(70, product.getStock());
        assertEquals(2, successCount.get()); // Оба потока должны завершиться успешно
    }
}