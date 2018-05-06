package ru.sbt.jschool.session9;

import java.util.concurrent.Executor;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadPool implements Executor {
    private final Queue<Runnable> workQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean isOpen = true;
    private PoolContext context;
    private int queueCount;
    private Runnable callback;

    public FixedThreadPool(int nThreads) {

        context = new PoolContext();
        for (int i = 0; i < nThreads; i++) {
            new Thread(new TaskWorker()).start();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (command==null){
            throw new NullPointerException();
        }
        if (isOpen) {
            workQueue.add(command);
            queueCount++;
        } else {
            throw new  RejectedExecutionException();

        }
    }

    public void shutdown() {
        isOpen = false;
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (true) {
                Runnable nextTask = workQueue.poll();
                if (nextTask != null) {
                    try {
                        nextTask.run();
                        context.completedTaskCount.incrementAndGet();
                    } catch (Exception e) {
                        context.failedTaskCount.incrementAndGet();
                    }
                }
                if(!isOpen && nextTask!=null )


                    if (context.isFinished()) {
                        if (callback != null) {
                            callback.run();
                        }
                    }


            }
        }
    }

    public void addCallback(Runnable callback) {
        this.callback = callback;
    }

    public Context getContext() {
        return context;
    }

    private class PoolContext implements Context {

        private volatile AtomicInteger completedTaskCount = new AtomicInteger();
        private volatile AtomicInteger failedTaskCount = new AtomicInteger();
        private volatile AtomicInteger interruptedTaskCount = new AtomicInteger();

        public int getCompletedTaskCount() {
            return completedTaskCount.get();
        }

        public int getFailedTaskCount() {
            return failedTaskCount.get();
        }

        public int getInterruptedTaskCount() {
            return interruptedTaskCount.get();
        }

        public void interrupt() {
            if (!workQueue.isEmpty()) {
                interruptedTaskCount.getAndSet(workQueue.size());
                workQueue.clear();
            }
        }

        public boolean isFinished() {
            return queueCount == failedTaskCount.get() + completedTaskCount.get();
        }
    }
}