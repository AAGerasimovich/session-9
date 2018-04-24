package ru.sbt.jschool.session9;


public class ExecutionManagerImpl implements ExecutionManager {

    private final static int POOL_SIZE = 10;

    public Context execute(Runnable callback, Runnable... tasks){

        ThreadPool threadPool = new ThreadPool(POOL_SIZE);

        for (Runnable task:
             tasks) {
            threadPool.execute(task);
        }

        Context c = threadPool.getContext();
        Thread callbackThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while (!c.isFinished() && c.getInterruptedTaskCount()==0){}
                if (c.getInterruptedTaskCount()!=0){  return;   }
                callback.run();
            }
        });

        threadPool.shutdown();
        callbackThread.start();
        return c;
    }



}
