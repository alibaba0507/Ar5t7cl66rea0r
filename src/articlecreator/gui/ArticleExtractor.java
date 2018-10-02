/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui;

import articlecreator.ParseHTMLArticles;
import static articlecreator.gui.MyEditor.FILE_SEPARATOR;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimerTask;
import javax.swing.JTextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Timer Task running on background to 
 * parse articles and extract the content
 * @author alibaba0507
 */
public class ArticleExtractor extends TimerTask {

    private Hashtable defaultProps;
    private JTextArea console;
    ArticleExtractor(Hashtable defaultProps,JTextArea console) {
        super();
        this.defaultProps = defaultProps;
        this.console = console;
    }

    @Override
    public void run() {
        try {
            startProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startProcess() throws Exception {

        String projProperties = (String) defaultProps.get("PROJECTS");
        if (projProperties == null || projProperties.isEmpty()) {
            return;
        }
        JSONParser parser = new JSONParser();
        JSONArray projectsJSON = null;
        JSONObject savedProjJSON = null;

        savedProjJSON = (JSONObject) parser.parse(projProperties);
        projectsJSON = (JSONArray) savedProjJSON.get("prj");
        Iterator<JSONObject> objs = projectsJSON.iterator();

        while (objs.hasNext()) {
            JSONObject p = (JSONObject) objs.next();
            String dir = ((String) p.get("dir"));
            Hashtable prop = initProjectProperties(dir);
            loadArticles(prop, dir);
        }// end while
        Thread.currentThread().yield();
        //Thread.currentThread().sleep(500);

    }

    public Hashtable initProjectProperties(String dir) {
        Hashtable defaultPropsForProjectLinks = new Hashtable();
        File file = new File(dir + FILE_SEPARATOR + "defaultProperties");
        if (!file.exists()) {

            saveProjectPoerties(defaultPropsForProjectLinks, dir);

        } else {
            try {
                FileInputStream fis = new FileInputStream(dir + FILE_SEPARATOR + "defaultProperties");
                ObjectInputStream ois = new ObjectInputStream(fis);
                defaultPropsForProjectLinks = (Hashtable) ois.readObject();
                ois.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultPropsForProjectLinks;
    }

    public void saveProjectPoerties(Hashtable prop, String dir) {
        try {
            FileOutputStream fos = new FileOutputStream(dir + FILE_SEPARATOR + "defaultProperties");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prop);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadArticles(Hashtable prop, String dir) throws Exception {
        Iterator it = prop.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = (String) prop.get(key);
            JSONParser p = new JSONParser();
            JSONArray arr = (JSONArray) p.parse(value);
            Iterator valueIt = arr.iterator();
            int cnt = 1;
            while (valueIt.hasNext()) {
                JSONObject o = (JSONObject) valueIt.next();
                String wordCnt = (String) o.get("wordCnt");
                if (wordCnt == null) {
                    extractTextFromArticle((String) o.get("URL"), cnt, dir);
                }
                cnt++;
            }
            console.append("\r\n>>>> KEY[" + key + "] VAL[" + value + "] >>>>");
        }
    }
    
      private void extractTextFromArticle(String url, int cnt, String dir) {
       // String charset = "UTF-8";
       // String userAgent = USER_AGENT; // Change this to your company's name and bot homepage!
        try {
            URL u = new URL(url);
            
            String baseUri = (new StringBuilder())
            .append(u.getProtocol())
            .append("://")
            .append(u.getHost())
            .toString();
            /*
             org.jsoup.nodes.Document doc = ParseHTMLArticles.connectAsBrowser(url);
             ParseHTMLArticles.parse(doc, dir, cnt,baseUri);
        */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
