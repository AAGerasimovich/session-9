package ru.sbt.jschool.session9;

import java.util.concurrent.Executor;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

public class ThreadPool implements Executor {
    private final Queue<Runnable> workQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean isRunning = true;
    private volatile boolean isOpen = true;
    private PoolContext context;
    private int queueCount;

    public ThreadPool(int nThreads) {
        context = new PoolContext();
        for (int i = 0; i < nThreads; i++) {
            new Thread(new TaskWorker()).start();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (isOpen) {
            workQueue.offer(command);
            queueCount++;
        }
    }

    public void shutdown() {
        isOpen = false;
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                Runnable nextTask = workQueue.poll();
                if (nextTask != null) {
                    try {
                        nextTask.run();
                        context.completedTaskCount++;
                    } catch (Exception e){
                        context.failedTaskCount++;
                    }
                }
                if (!isOpen && nextTask == null){
                    return;
                }
            }
        }
    }

    public Context getContext(){
        return context;
    }

    private class PoolContext implements Context {

        private volatile int  completedTaskCount;
        private volatile int  failedTaskCount;
        private volatile int  interruptedTaskCount;



        public int getCompletedTaskCount() {
            return completedTaskCount;

        }

        public int getFailedTaskCount() {
            return failedTaskCount;

        }

        public int getInterruptedTaskCount() {
            return interruptedTaskCount;

        }

        public void interrupt() {
            if(!workQueue.isEmpty()) {
                interruptedTaskCount = workQueue.size();
                workQueue.clear();
            }
        }

        public boolean isFinished() {
            return queueCount == failedTaskCount + completedTaskCount;

        }
    }
}