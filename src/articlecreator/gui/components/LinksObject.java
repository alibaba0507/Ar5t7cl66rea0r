/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components;

import java.io.Serializable;

/**
 *
 * @author alibaba0507
 */
public class LinksObject implements Serializable{
    private String keyWord;
    private String title;
    private String link;
    private String wordCount;
    
    private String localHTMLFile;
    private String localTXTFile;
    private String localSpinFile;
    
    
    /**
     * @return the keyWord
     */
    public String getKeyWord() {
        return keyWord;
    }

    /**
     * @param keyWord the keyWord to set
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the wordCount
     */
    public String getWordCount() {
        return wordCount;
    }

    /**
     * @param wordCount the wordCount to set
     */
    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }

    @Override
    public String toString() {
        return this.getLink(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        return this.getLink().equals(obj.toString()); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the localHTMLFile
     */
    public String getLocalHTMLFile() {
        return localHTMLFile;
    }

    /**
     * @param localHTMLFile the localHTMLFile to set
     */
    public void setLocalHTMLFile(String localHTMLFile) {
        this.localHTMLFile = localHTMLFile;
    }

    /**
     * @return the localTXTFile
     */
    public String getLocalTXTFile() {
        return localTXTFile;
    }

    /**
     * @param localTXTFile the localTXTFile to set
     */
    public void setLocalTXTFile(String localTXTFile) {
        this.localTXTFile = localTXTFile;
    }

    /**
     * @return the localSpinFile
     */
    public String getLocalSpinFile() {
        return localSpinFile;
    }

    /**
     * @param localSpinFile the localSpinFile to set
     */
    public void setLocalSpinFile(String localSpinFile) {
        this.localSpinFile = localSpinFile;
    }
    
    
    
    

  
}
