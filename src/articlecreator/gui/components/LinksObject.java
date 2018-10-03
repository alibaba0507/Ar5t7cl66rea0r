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

  
}
