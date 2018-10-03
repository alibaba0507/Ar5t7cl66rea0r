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
public class SpinTemplate implements Serializable{
    private String keyWord,link,spinTemplate;

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
     * @return the spinTemplate
     */
    public String getSpinTemplate() {
        return spinTemplate;
    }

    /**
     * @param spinTemplate the spinTemplate to set
     */
    public void setSpinTemplate(String spinTemplate) {
        this.spinTemplate = spinTemplate;
    }
}
