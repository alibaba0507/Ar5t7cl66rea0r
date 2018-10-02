/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.run;



import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import za.co.utils.AWTUtils;

/**
 *
 * @author alibaba0507
 */
public class Main {
  
    
     // ----- Main method of MyEditor.java -------
    public static void main(String[] args) {
        //URL url = Main.class.getResource("/images/maxresdefault.jpg");
       final SplashScreen splash = new SplashScreen("/images/article_spinning_service.jpg");
        splash.setVisible(true);
       final ArticleManagmentMain frame = new ArticleManagmentMain();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
              //  while (true) {                    
                   Thread.currentThread().yield();
                   splash.close();
                   frame.setVisible(true);
               // }
            }
        }, 3000);
        
        
        //splash.close();
        

    }
}
