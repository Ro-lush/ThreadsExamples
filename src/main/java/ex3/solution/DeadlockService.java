package ex3.solution;

//@Service
public class DeadlockService {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void method1() {
        synchronized (lock1) {
            System.out.println("Поток 1: Захватил lock1, жду lock2...");
            // Добавляем задержку для гарантии дедлока
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            synchronized (lock2) {
                System.out.println("Поток 1: Захватил lock2 (этого не будет)");
            }
        }
    }

    public void method2() {
        synchronized (lock2) {
            System.out.println("Поток 2: Захватил lock2, жду lock1...");
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            synchronized (lock1) {
                System.out.println("Поток 2: Захватил lock1 (этого не будет)");
            }
        }
    }
}
