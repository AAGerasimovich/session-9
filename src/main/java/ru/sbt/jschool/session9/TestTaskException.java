package ru.sbt.jschool.session9;

public class TestTaskException implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " - Exception");
        throw new NullPointerException("Exception");
    }
}
