/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.run;

import articlecreator.net.SSLCertificates;
import articlecreator.spin.syntax.SynonymFinder;
import java.io.IOException;
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
        // System.setProperty("jsse.enableSNIExtension", "true");
        //  System.setProperty("https.protocols", "SSLv3,TLSv1.2");

        // JDK 8
        //java.lang.System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        java.lang.System.setProperty("https.protocols", "TLSv1");
        SSLCertificates.ignoreCertificates();
        System.out.println(System.getProperty("https.protocols"));
        final SplashScreen splash = new SplashScreen("/images/article-spinning.jpg");
        splash.setVisible(true);
        final ArticleManagmentMain frame = new ArticleManagmentMain();
        Timer t = new Timer();
        try {
            SynonymFinder s = new SynonymFinder(false);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                //  while (true) {                    
                Thread.currentThread().yield();
                splash.close();
                frame.setVisible(true);
                // }
            }
        }, 1000);

        //splash.close();
    }
}
