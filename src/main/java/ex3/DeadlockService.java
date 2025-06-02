package ex3;

//@Service
public class DeadlockService {

    public void method1() {

        // блок критичного кода 1

        // Добавляем задержку для гарантии дедлока
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }


        // блок критичного кода 2


    }

    public void method2() {

        // блок критичного кода 2

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }


        // блок критичного кода 1


    }
}
