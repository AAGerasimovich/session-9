package ru.sbt.jschool.session9;


public class ExecutionManagerImpl implements ExecutionManager {

    private final static int POOL_SIZE = 10;

    public Context execute(Runnable callback, Runnable... tasks){

        FixedThreadPool threadPool = new FixedThreadPool(POOL_SIZE);

        for (Runnable task:
             tasks) {
            threadPool.execute(task);
        }

        Context context = threadPool.getContext();
        Thread callbackThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while (!context.isFinished() && context.getInterruptedTaskCount()==0){}
                if (context.getInterruptedTaskCount()!=0){  return;   }
                callback.run();
            }
        });

        threadPool.shutdown();
        callbackThread.start();
        return context;
    }



}
