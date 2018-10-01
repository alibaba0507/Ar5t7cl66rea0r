/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package articlecreator.threads;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author alibaba0507
 */
public class ThreadShedule {
public static void main(String args[]) {
    ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(5);
    
    stpe.scheduleAtFixedRate(new Job1(), 0, 5, TimeUnit.SECONDS);
    stpe.scheduleAtFixedRate(new Job2(), 1, 2, TimeUnit.SECONDS);
  }
}
class Job1 implements Runnable {
  public void run() {
    System.out.println("Job 1");
  }
}

class Job2 implements Runnable {
  public void run() {
      for(int i=-99;i<99;i++){
        System.out.println(i);
      }
  }
}
