/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components.ui;

import articlecreator.gui.components.SearchObject;
import articlecreator.gui.run.ArticleManagmentMain;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JTextArea;

/**
 *
 * @author alibaba0507
 */
public class PropertiesUI {

    private static PropertiesUI instance;
    private Hashtable defaultProps = null;

    public PropertiesUI() {
        super();
        //getInstance();
        loadProperties();
        if (instance == null) {
            instance = this;
        }
    }

    public void initProperties() {

        File file = new File("defaultProperties");
        if (!file.exists()) {

            JTextArea defaultTextArea = new JTextArea(); // Used for both default editor and console setting
            JTextArea customEditor = new JTextArea();
            JTextArea customConsole = new JTextArea();

            Font font = new Font("Courier New", Font.PLAIN, 13);

            defaultTextArea.setFont(font);
            defaultTextArea.setTabSize(3); // Modified latter
            defaultTextArea.setCaretColor(Color.red);

            customEditor.setFont(font);
            customEditor.setTabSize(3);
            customEditor.setCaretColor(Color.red);

            customConsole.setFont(font);
            customConsole.setTabSize(3);
            customConsole.setCaretColor(Color.red);

            defaultProps = new Hashtable();
            ArrayList serchList = new ArrayList();
            SearchObject o = new SearchObject();
            o.setSearchEngine("http://www.google.com/search?q=");
            o.setLinksRegex("h3.r a");
            serchList.add(o);

            o = new SearchObject();
            o.setSearchEngine("https://duckduckgo.com/html/?q=");
            o.setLinksRegex("#links .results_links");
            serchList.add(o);

            o = new SearchObject();
            o.setSearchEngine("https://www.bing.com/search?q=");
            o.setLinksRegex("#b_results .b_algo h2 a");
            serchList.add(o);

            defaultProps.put("USER_SEARCH", serchList);

            defaultProps.put("DEFAULT_TEXTAREA", defaultTextArea);
            defaultProps.put("CUSTOM_EDITOR", customEditor);
            defaultProps.put("CUSTOM_CONSOLE", customConsole);

            defaultProps.put("TAB", "HARD");  // Alternative value is "SOFT"
            defaultProps.put("DEFAULT_TAB_SIZE", new Integer(3));
            defaultProps.put("LOOK&FEEL", "java");
            defaultProps.put("SETTING", "DEFAULT");  // Alternative value is "CUSTOM"

            defaultProps.put("DEFAULT_EDITOR_HIGHLIGHTED_LINE", new Color(254, 204, 255));
            defaultProps.put("DEFAULT_CONSOLE_HIGHLIGHTED_LINE", new Color(51, 255, 255));
            defaultProps.put("CUSTOM_EDITOR_HIGHLIGHTED_LINE", new Color(254, 204, 255));
            defaultProps.put("CUSTOM_CONSOLE_HIGHLIGHTED_LINE", new Color(51, 255, 255));

        } else {
            loadProperties();
        }

    }

    public static PropertiesUI getInstance() {
        if (instance == null) {
            instance = new PropertiesUI();
            // instance.loadProperties();
        }
        return instance;
    }
    
    public Hashtable initProjectProperties(String dir) {
        Hashtable defaultPropsForProjectLinks = new Hashtable();
        File file = new File(dir + ArticleManagmentMain.FILE_SEPARATOR + "defaultProperties");
        if (!file.exists()) {

            saveProjectPoerties(defaultPropsForProjectLinks, dir);

        } else {
            try {
                FileInputStream fis = new FileInputStream(dir + ArticleManagmentMain.FILE_SEPARATOR + "defaultProperties");
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
            FileOutputStream fos = new FileOutputStream(dir + ArticleManagmentMain.FILE_SEPARATOR + "defaultProperties");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(prop);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Hashtable getDefaultProps() {
        return this.defaultProps;
    }

    public void loadProperties() {
        defaultProps = null;
        try {
            String propFile = ArticleManagmentMain.CURRENT_DIR + ArticleManagmentMain.FILE_SEPARATOR + "defaultProperties";
            File f = new File(propFile);//.createNewFile();
            if (!f.exists()) {
                initProperties();
                saveProperties();
                return;
            }
            FileInputStream fis = new FileInputStream("defaultProperties");
            /*if (fis.available() == 0)
            {
               //  defaultProps = new Hashtable();
               // initProperties();
                fis.close();
                return;
            }*/
            ObjectInputStream ois = new ObjectInputStream(fis);
            defaultProps = (Hashtable) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveProperties() {
        try {
            FileOutputStream fos = new FileOutputStream("defaultProperties");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(defaultProps);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
