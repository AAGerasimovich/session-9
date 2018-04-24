package ru.sbt.jschool.session9;

public class TestTask implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new IllegalStateException("task interrupted", e);
        }
    }


}
