package ex1;

public class Main {
    public static void main(String[] args) throws InterruptedException {


        Thread st1 = new Thread(new Student("Иван"));
        st1.start();

        Thread st2 = new Thread(new Student("Дима"));
        st2.start();

        Thread st3 = new Thread(new Student("Коля"));
        st3.start();

        st1.join();
        st2.join();
        st3.join();


    }
}
