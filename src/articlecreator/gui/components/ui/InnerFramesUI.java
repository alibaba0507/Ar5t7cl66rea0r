/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components.ui;

import articlecreator.gui.MyInternalFrame;
import articlecreator.gui.components.OpenPoject;
import java.awt.Component;
import java.io.File;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
    
    public void closeFrame(String title)
    {
      JInternalFrame[] frames = desktop.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (title != null && frames[i].getTitle().equals(title)) {
                desktop.remove(frames[i]);
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
