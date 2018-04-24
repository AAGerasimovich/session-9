package ru.sbt.jschool.session9.test;

import org.junit.Test;
import ru.sbt.jschool.session9.*;

import static org.junit.Assert.assertEquals;

/**
 */
public class AccountSQLGeneratorTest {

    public  Runnable[] getTasks(int n){
        Runnable[] tt = new TestTask[n];

        for (int i = 0; i <n ; i++) {
            tt[i] = new TestTask();
        }
        return tt;
    }
    public  Runnable getCallback(){

        return new Runnable () {
            @Override
            public void run() {
                System.out.println("im callback");
            }
        };
    }


    @Test public void testComplet() {

        ExecutionManager em = new ExecutionManagerImpl();
        Context c = em.execute(getCallback(), getTasks(10));
        try {
            Thread.sleep(3000);
        }catch (Exception e){}

        System.out.println("Completed   - " + c.getCompletedTaskCount());
        System.out.println("Failed      - " + c.getFailedTaskCount());
        System.out.println("Interrupted - "+ c.getInterruptedTaskCount());
        System.out.println("Finished    - "+ c.isFinished()+ "\n\n\n");


        assertEquals(true, c.isFinished());
    }
    @Test public void testInterrupt() {

        ExecutionManager em = new ExecutionManagerImpl();
        Context c = em.execute(getCallback(), getTasks(15));
        try {
            Thread.sleep(100);
        }catch (Exception e){}
        c.interrupt();
        try {
            Thread.sleep(2000);
        }catch (Exception e){}

        System.out.println("Completed   - " + c.getCompletedTaskCount());
        System.out.println("Failed      - " + c.getFailedTaskCount());
        System.out.println("Interrupted - "+ c.getInterruptedTaskCount());
        System.out.println("Finished    - "+ c.isFinished()+ "\n\n\n");


        assertEquals(false, c.isFinished());
    }
    @Test public void testException() {

        ExecutionManager em = new ExecutionManagerImpl();
        Context c = em.execute(getCallback(), new TestTask(), new TestTaskException(), new TestTask());

        try {
            Thread.sleep(1500);
        }catch (Exception e){}

        System.out.println("Completed   - " + c.getCompletedTaskCount());
        System.out.println("Failed      - " + c.getFailedTaskCount());
        System.out.println("Interrupted - "+ c.getInterruptedTaskCount());
        System.out.println("Finished    - "+ c.isFinished()+ "\n\n\n");


        assertEquals(true, c.isFinished());
    }

}
