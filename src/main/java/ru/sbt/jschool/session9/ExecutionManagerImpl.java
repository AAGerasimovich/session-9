package ru.sbt.jschool.session9;


public class ExecutionManagerImpl implements ExecutionManager {

    private final static int POOL_SIZE = 10;

    public Context execute(Runnable callback, Runnable... tasks){

        FixedThreadPool threadPool = new FixedThreadPool(POOL_SIZE);

        for (Runnable task:
             tasks) {
            threadPool.execute(task);
        }

        threadPool.addCallback(callback);
        Context context = threadPool.getContext();
        threadPool.shutdown();

        return context;
    }



}
