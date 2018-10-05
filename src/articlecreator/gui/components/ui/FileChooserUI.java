/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components.ui;


import static articlecreator.gui.run.ArticleManagmentMain.CURRENT_DIR;

import java.io.File;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;

/**
 *
 * @author alibaba0507
 */
public class FileChooserUI {

    public static final int DIR_ONLY = 0;
    public static final int FILE_ONLY = 1;

    public static JFileChooser directoryChooser;
    public static JFileChooser fileChooser;

    /**
     *
     * @param type One of DIR_ONLY or FILE_ONLY
     */
    public  JFileChooser createFileChooser(int type) {
        if (type == DIR_ONLY) {
            if (directoryChooser != null) {
                return directoryChooser;
            }
            directoryChooser = new JFileChooser(CURRENT_DIR);
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            directoryChooser.setAcceptAllFileFilterUsed(false);
            return directoryChooser;
        } else {
            if (fileChooser != null) {
                return fileChooser;
            }
            fileChooser = new JFileChooser(CURRENT_DIR);
            fileChooser.setFileView(new CustomFileView());
            fileChooser.addChoosableFileFilter(new TextFilter());
            fileChooser.addChoosableFileFilter(new JavaCodeFilter());
            fileChooser.addChoosableFileFilter(new HtmlFilter());
            fileChooser.addChoosableFileFilter(new CSVFilter());
            return fileChooser;
        }

    }
class TextFilter extends SuffixAwareFilter {

    public boolean accept(File f) {
        String suffix = getSuffix(f);
        if (suffix != null) {
            return super.accept(f) || suffix.equals("txt");
        }
        return false;
    }

    public String getDescription() {
        return "Text Files(*.txt)";
    }
}

class HtmlFilter extends SuffixAwareFilter {

    public boolean accept(File f) {
        String suffix = getSuffix(f);
        if (suffix != null) {
            return super.accept(f) || suffix.equals("html");
        }
        return false;
    }

    public String getDescription() {
        return "HTML Files(*.html)";
    }
}


class JavaCodeFilter extends SuffixAwareFilter {

    public boolean accept(File f) {
        boolean accept = super.accept(f);
        if (!accept) {
            String suffix = getSuffix(f);
            if (suffix != null) {
                accept = super.accept(f) || suffix.equals("java");
            }
        }
        return accept;
    }

    public String getDescription() {
        return "Java Source Code Files(*.java)";
    }
}

class CSVFilter extends SuffixAwareFilter {

    public boolean accept(File f) {
        boolean accept = super.accept(f);
        if (!accept) {
            String suffix = getSuffix(f);
            if (suffix != null) {
                accept = super.accept(f) || suffix.equals("csv");
            }
        }
        return accept;
    }

    public String getDescription() {
        return "CSV Files(*.csv)";
    }
}


    class CustomFileView extends FileView {
        
        URL u = FileChooserUI.class.getResource("/images/FILE.gif");
        private Icon fileIcon = new ImageIcon(u);
        
        URL uJava = FileChooserUI.class.getResource("/images/java.png");
        private Icon javaIcon = new ImageIcon(uJava);
        
        URL uHTML = FileChooserUI.class.getResource("/images/html.png");
        private Icon htmlIcon = new ImageIcon(uHTML);
        
          URL uTXT = FileChooserUI.class.getResource("/images/txt.png");
          
          URL uCSV = FileChooserUI.class.getResource("/images/txt.png");
          
        private Icon txtIcon = new ImageIcon(uTXT);
        public Icon getIcon(File f) {
            Icon icon;
            String suffix = getSuffix(f);
            if (suffix == null) {
                return null;
            }

            if (suffix.equals("java")) {
                icon = javaIcon;
            } 
            else if (suffix.equals("html")) {
                icon = htmlIcon;
            }
            else if (suffix.equals("txt") || suffix.equals("csv")) {
                icon = txtIcon;
            }
            else {
                icon = super.getIcon(f);
            }
            return icon;
        }

        private String getSuffix(File file) {
            String filestr = file.getPath(), suffix = null;
            int i = filestr.lastIndexOf('.');
            if (i > 0 && i < filestr.length()) {
                suffix = filestr.substring(i + 1).toLowerCase();
            }
            return suffix;
        }
    }
    
    
    
    /**
 ** SuffixAwareFilter, JavaCodeFilter and TextFilter classes are copied and
 * pasted * here from example 16-5 of 'Graphic Java 2, Mastering the JFC' by
 * David M. Geary.
 */
abstract class SuffixAwareFilter extends javax.swing.filechooser.FileFilter {

    public String getSuffix(File f) {
        String s = f.getPath(), suffix = null;
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            suffix = s.substring(i + 1).toLowerCase();
        }
        return suffix;
    }

    public boolean accept(File f) {
        return f.isDirectory();
    }
}

}
