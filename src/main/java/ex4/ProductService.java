package ex4;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Метод с оптимистичной блокировкой
    @Transactional
    public void updateStockOptimistic(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow();
        product.decreaseStock(quantity);
        // При конфликте версий — OptimisticLockException
    }

    // Метод с пессимистичной блокировкой
    @Transactional
    public void updateStockPessimistic(Long productId, int quantity) {
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow();
        try {
            Thread.sleep(1000); // Имитация долгой операции
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        product.decreaseStock(quantity);
        // Блокировка до конца транзакции
    }
}