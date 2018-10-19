/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components.ui;

import articlecreator.gui.MyInternalFrame;
import articlecreator.gui.components.OpenPoject;
import articlecreator.net.BrowserControl;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import za.co.utils.AWTUtils;

/**
 *
 * @author alibaba0507
 */
public class InnerFramesUI {

    private static InnerFramesUI interFamesInstance;
    private JDesktopPane desktop;

    public InnerFramesUI(JDesktopPane desktop) {
        super();
        this.desktop = desktop;
        if (interFamesInstance == null) {
            interFamesInstance = this;
        }
    }

    public static InnerFramesUI getInstance() {
        return interFamesInstance;
    }

    public void moveHelpFiles() {
        try {
            String jarPath = new File(InnerFramesUI.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            jarPath = new File(jarPath).getParent();
            ProjectsUI.console.append("OPEN HELP [" + (jarPath) + "] >>>\r\n");

            File f = new File(jarPath + "/help/images");
            if (!f.exists()) {
                f.mkdirs();
            }

            f = new File(jarPath + "/help/IntroducingArticleCreator.html");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/IntroducingArticleCreator.html");
                new File(path).renameTo(f);

            }

            f = new File(jarPath + "/help/images/image1.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image1.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image2.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image2.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image3.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image3.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image4.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image4.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image5.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image5.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image6.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image6.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image7.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image7.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image8.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image8.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image9.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image9.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image10.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image10.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image11.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image11.png");
                new File(path).renameTo(f);
            }

            f = new File(jarPath + "/help/images/image12.png");
            if (f.exists() == false) { // copy file out of jar if not there

                String path = AWTUtils.exportResource("/help/images/image12.png");
                new File(path).renameTo(f);
            }
        } catch (Exception e) {
            ProjectsUI.console.append(e.toString() + "\r\n");
        }
    }

    public void displayHelp(String helpLink) {
        try {
            
            String jarPath = new File(InnerFramesUI.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            jarPath = new File(jarPath).getParent();
            jarPath = jarPath.replaceAll("\\\\", "/");
            
             if (Desktop.isDesktopSupported()) {
                 URI helpURL = null;
                 if (helpLink != null && !helpLink.equals(""))
                    helpURL = URI.create("file:///" + URLDecoder.decode( jarPath+ "/help/IntroducingArticleCreator.html#" + helpLink,"UTF-8"));//.toURI();
                 else
                     helpURL = new File(jarPath + "/help/IntroducingArticleCreator.html").toURI();
                ProjectsUI.console.append("\r\n" +helpURL.toASCIIString() + "\r\n");
                 //BareBonesBrowserLaunch.openURL(helpURL.toASCIIString());
                 BrowserControl.displayURL(helpURL.toASCIIString());
                 //Desktop.getDesktop().browse(helpURL);
            }
             
           /*
            final JEditorPane txt = new JTextPane();
            //HTMLEditorKit kit = HTMLEditorKit.
            String type = ("text/html");
            final EditorKit kit = txt.getEditorKitForContentType(type);

            //  SwingUtilities.invokeLater(new Runnable() {
            //    public void run() {
            txt.setEditorKit(kit);
            javax.swing.text.Document d = kit.createDefaultDocument();

            txt.setDocument(d);
            URL url = InnerFramesUI.class.getResource("/help/IntroducingArticleCreator.html");

            //Document doc = Jsoup.parse(new File(url.toURI()), "UTF-8");
            txt.setPage(url);
            //  String s = doc.toString();
            // txt.setText(s);
            txt.setEditable(false);
            txt.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        // Do something with e.getURL() here
                        try {
                            txt.setPage(e.getURL());
                            ProjectsUI.console.append("\r\n" + e.getURL().toString() + "\r\n");
                        } catch (Exception ex) {

                        }
                    }
                }
            });
            JScrollPane scrollPane = new JScrollPane(txt);
            scrollPane.setPreferredSize(new Dimension(780, 550));
            int result = JOptionPane.showConfirmDialog(InnerFramesUI.getInstance().desktop, scrollPane, "Help File", JOptionPane.PLAIN_MESSAGE);
           */
        } catch (Exception e) {
            ProjectsUI.console.append("\r\n" + e.getLocalizedMessage() + "\r\n");
            e.printStackTrace();
        }

    }

    public void refreshLinkByFrameName(String title) {
        JInternalFrame[] frames = desktop.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (title != null && frames[i].getTitle().equals(title)) {
                MyInternalFrame frm = (MyInternalFrame) frames[i];
                if (frm.getProjectWindows() != null) {
                    frm.getProjectWindows().refreshLinksTable();
                }

            }
        }
    }

    public void closeFrame(String title) {
        JInternalFrame[] frames = desktop.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (title != null && frames[i].getTitle().equals(title)) {
                desktop.remove(frames[i]);
                frames[i].dispose();
                desktop.repaint();
                break;
            }
        }
    }

    public JInternalFrame ceateProjectFrame(File file, String title) {

        if (title == null && ProjectsUI.selectedProjectItem == null) {
            title = "New Project";
        } else if (ProjectsUI.selectedProjectItem != null && title == null) {
            title = ProjectsUI.selectedProjectItem.toString();
        }

        //     else if (title != null)
        JInternalFrame[] frames = desktop.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (title != null && frames[i].getTitle().equals(title)) {
                return frames[i];
            }
        }

        MyInternalFrame jif = new MyInternalFrame(title, true, true, true, true);
        OpenPoject proj = new OpenPoject();
        jif.setProjectWindows(proj);
        JScrollPane scroller = new JScrollPane(proj, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Decorate jif 		
        //jif.setScrollPane(scroller);
        jif.setContentPane(scroller);
        jif.setSize(400, 250);	// A necessary statement	
        jif.setVisible(true);
        jif.addVetoableChangeListener(new ActionsUI().new CloseListener(jif, proj));
        jif.moveToFront();
        try {
            jif.setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        desktop.add(jif);
        return jif;
    }

    public MyInternalFrame getSelectedFrame() {
        return (MyInternalFrame) desktop.getSelectedFrame();
    }

}
 class BareBonesBrowserLaunch {

   static final String[] browsers = { "x-www-browser", "google-chrome",
      "firefox", "opera", "epiphany", "konqueror", "conkeror", "midori",
      "kazehakase", "mozilla" };
   static final String errMsg = "Error attempting to launch web browser";

   public static void openURL(String url) {
      try {  //attempt to use Desktop library from JDK 1.6+
         Class<?> d = Class.forName("java.awt.Desktop");
         d.getDeclaredMethod("browse", new Class[] {java.net.URI.class}).invoke(
            d.getDeclaredMethod("getDesktop").invoke(null),
            new Object[] {java.net.URI.create(url)});
         //above code mimicks:  java.awt.Desktop.getDesktop().browse()
         }
      catch (Exception ignore) {  //library not available or failed
         String osName = System.getProperty("os.name");
         try {
            if (osName.startsWith("Mac OS")) {
               Class.forName("com.apple.eio.FileManager").getDeclaredMethod(
                  "openURL", new Class[] {String.class}).invoke(null,
                  new Object[] {url});
               }
            else if (osName.startsWith("Windows"))
               Runtime.getRuntime().exec(
                  "rundll32 url.dll,FileProtocolHandler " + url);
            else { //assume Unix or Linux
               String browser = null;
               for (String b : browsers)
                  if (browser == null && Runtime.getRuntime().exec(new String[]
                        {"which", b}).getInputStream().read() != -1)
                     Runtime.getRuntime().exec(new String[] {browser = b, url});
               if (browser == null)
                  throw new Exception(Arrays.toString(browsers));
               }
            }
         catch (Exception e) {
            JOptionPane.showMessageDialog(null, errMsg + "\n" + e.toString());
            }
         }
      }

   }