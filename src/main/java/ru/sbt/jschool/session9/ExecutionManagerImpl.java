package ru.sbt.jschool.session9;


public class ExecutionManagerImpl implements ExecutionManager {

    private final static int POOL_SIZE = 10;

    public Context execute(Runnable callback, Runnable... tasks) {
        if (callback == null || tasks.length == 0) {
            return null;
        }

        FixedThreadPool threadPool = new FixedThreadPool(POOL_SIZE);

        threadPool.addCallback(callback);
        for (Runnable task :
                tasks) {
            threadPool.execute(task);
        }

        Context context = threadPool.getContext();
        threadPool.shutdown();

        return context;
    }
}
