/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components.ui;

import articlecreator.gui.MyInternalFrame;
import articlecreator.gui.components.OpenPoject;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.InternalFrameUI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import za.co.utils.AWTUtils;

/**
 *
 * @author alibaba0507
 */
public class ActionsUI {

    private String message[] = {
        " ",
        " ",
        "Save before closing?",
        " "
    };

    public javax.swing.Action newProject, openProject,
            openFile, saveFile, saveFileAs, exitAction, 
            cutAction, copyAction, pasteAction, 
            findAndReplaceAction, deletAection, undoAction, redoAction, 
            selectAll, stopAction, tipsAction, aboutAction,readMeAction
            ,runProjectAction;

    public ActionsUI() {
        initActions();
    }

    private void initActions() {
        newProject = new NewAction();
    }

    public void openProjectFile(final File file, String title) {
        JInternalFrame frame = null;
        try {
            frame = InterFamesUI.getInstance().ceateProjectFrame(file, title);
            frame.moveToFront();
            frame.setMaximum(true);
            frame.setSelected(true);
        } catch (Exception pve) {
            pve.printStackTrace();
        }
    }

    // Show a confirmation dialog before closing a file
    public int closingCheck(MyInternalFrame frame) {
        String s = frame.getFile().getPath();
        message[0] = "Project " + s + " has changed";
        return JOptionPane.showConfirmDialog(
                frame.getParent(), // parentComponent
                message, // message
                "Article Creator", // title
                JOptionPane.YES_NO_CANCEL_OPTION, // optionType
                JOptionPane.QUESTION_MESSAGE // messageType
        );
    }
    // Listener that handles closing event of a file.
    //

    public class CloseListener implements VetoableChangeListener {

        MyInternalFrame frame;
        OpenPoject proj;
        //UndoManager undo;

        CloseListener(MyInternalFrame frame, OpenPoject proj) {
            this.frame = frame;
            this.proj = proj;
            //this.undo = undo;
        }

        public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
            boolean isClosed = true;
            String name = e.getPropertyName();
            if (name.equals(JInternalFrame.IS_CLOSED_PROPERTY)) {
                Boolean oldValue = (Boolean) e.getOldValue(),
                        newValue = (Boolean) e.getNewValue();
                if (oldValue == Boolean.FALSE && newValue == Boolean.TRUE) {
                    //  if (undo.canUndo()) {
                    int answer = closingCheck(frame);
                    if (answer == JOptionPane.YES_OPTION) {
                        //String fileName = frame.getFile().getName();
                        proj.save();
                        isClosed = true;

                    } else if (answer == JOptionPane.CANCEL_OPTION) {
                        isClosed = false;
                    }
                    if (!isClosed) {
                        throw new PropertyVetoException(" ", e);
                    }
                    // }
                }

            }

        }
    } // End CloseListener

    public class NewAction extends AbstractAction {

        public NewAction() {
            super("New Project", new ImageIcon(AWTUtils.getIcon(null, "/images/newProject.jpg")));
        }

        public void actionPerformed(ActionEvent e) {
            openProjectFile(null, "New Project");
            //openFile(null);

        }
    } // End NewAction

    public class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open File", new ImageIcon(AWTUtils.getIcon(null, "/images/openFile24.jpg")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Open File Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class SavedAction extends AbstractAction {

        public SavedAction() {
            super("Save File", new ImageIcon(AWTUtils.getIcon(null, "/images/Save24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Save File Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class SaveFileAsAction extends AbstractAction {

        public SaveFileAsAction() {
            super("Save File As", new ImageIcon(AWTUtils.getIcon(null, "/images/SaveAs24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Save File As Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class ExitAction extends AbstractAction {

        public ExitAction() {
            super("Exit", new ImageIcon(AWTUtils.getIcon(null, "/images/exit.png")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class CutAction extends AbstractAction {

        public CutAction() {
            super("Cut", new ImageIcon(AWTUtils.getIcon(null, "/images/Cut24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class CopyAction extends AbstractAction {

        public CopyAction() {
            super("Copy", new ImageIcon(AWTUtils.getIcon(null, "/images/Copy24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class PasteAction extends AbstractAction {

        public PasteAction() {
            super("Paste", new ImageIcon(AWTUtils.getIcon(null, "/images/Paste24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class FindAndReplaceAction extends AbstractAction {

        public FindAndReplaceAction() {
            super("Exit", new ImageIcon(AWTUtils.getIcon(null, "/images/Find24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class DeleteAction extends AbstractAction {

        public DeleteAction() {
            super("Delete ", new ImageIcon(AWTUtils.getIcon(null, "/images/Delete24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class UndoAction extends AbstractAction {

        public UndoAction() {
            super("Undo", new ImageIcon(AWTUtils.getIcon(null, "/images/Undo24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class RedoAction extends AbstractAction {

        public RedoAction () {
            super("Redo", new ImageIcon(AWTUtils.getIcon(null, "/images/Redo24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class SelectAllAction extends AbstractAction {

        public SelectAllAction() {
            super("Select All");
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    public class StopAction extends AbstractAction {

        public StopAction() {
            super("Stop", new ImageIcon(AWTUtils.getIcon(null, "/images/Stop24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction
    
    
    public class TipsAction extends AbstractAction {

        public TipsAction() {
            super("Tips", new ImageIcon(AWTUtils.getIcon(null, "/images/TipsOfTheDay24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    
    
    public class ReadMeAction extends AbstractAction {

        public ReadMeAction() {
            super("Read Me");
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            //openFile(null);

        }
    } // End NewAction

    class RunProjectAction extends AbstractAction {

        RunProjectAction() {
            super("Run Project", new ImageIcon(AWTUtils.getIcon(null, "/images/Compile24.gif")));

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (ProjectsUI.projectList.getSelectedValue() != null) {
                final Object selectedProject = ProjectsUI.projectList.getSelectedValue();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        runProject(selectedProject);
                    }
                });

            }
        }

    }

     private void runProject(Object selectedProject) {
        ProjectItem item = (ProjectItem) selectedProject;

        JSONParser parser = new JSONParser();

        String dir = "";
        String keyWords = "";
        try {
            JSONObject savedProjJSON = (JSONObject) parser.parse(item.getJSONObject());
            dir = (String) savedProjJSON.get("dir");
            keyWords = (String) savedProjJSON.get("keyWords");
            Hashtable prop = PropertiesUI.getInstance().initProjectProperties(dir);
            saveLinksForKeyWords(dir, prop, keyWords);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
     
      private void saveLinksForKeyWords(String dir, Hashtable prop, String keyWords) {
        String[] keyWord = keyWords.split(",");
        for (int i = 0; i < keyWord.length; i++) {
            extractLinksForKeyWord(prop, keyWord[i]);
            saveProjectPoerties(prop, dir);
        }
    }
  


}
