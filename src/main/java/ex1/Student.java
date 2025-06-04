package ex1;

public class Student implements Runnable {

    private  final String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public synchronized void run() {
        System.out.println(name + " хочет взять книгу");

        synchronized (Main.book) {
            System.out.println(name + " взял книгу");
            try {
                Thread.sleep((long) (Math.random() * 5000));
                System.out.println(name + " отдал книгу");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
