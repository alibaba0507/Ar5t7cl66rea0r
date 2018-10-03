/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components.ui;

import articlecreator.gui.MyBasicTextAreaUI;
import articlecreator.gui.MyInternalFrame;
import articlecreator.gui.components.LinksObject;
import articlecreator.gui.components.OpenPoject;
import articlecreator.net.ConnectionManagerUI;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.InternalFrameUI;
import org.json.simple.JSONArray;
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
            selectAll, stopAction, tipsAction, aboutAction, readMeAction, runProjectAction;

    public ActionsUI() {
        initActions();
    }

    private void initActions() {
        newProject = new NewAction();
    }

    public void openProjectFile(final File file, String title) {
        JInternalFrame frame = null;
        try {
            frame = InnerFramesUI.getInstance().ceateProjectFrame(file, title);
            frame.moveToFront();
            frame.setMaximum(true);
            frame.setSelected(true);
        } catch (Exception pve) {
            pve.printStackTrace();
        }
    }

    // Show a confirmation dialog before closing a file
    public int closingCheck(MyInternalFrame frame) {
        if (frame == null || frame.getFile() == null) {
            return -1;
        }
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

        //private static ActionsUI.NewAction instance;
        public NewAction() {
            super("New Project", new ImageIcon(AWTUtils.getIcon(null, "/images/newProject.jpg")));
            // new Throwable().printStackTrace();
        }

        public void actionPerformed(ActionEvent e) {
            openProjectFile(null, "New Project");
            //openFile(null);

        }
    } // End NewAction

    public class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open File", new ImageIcon(AWTUtils.getIcon(null, "/images/openFile24.png")));
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

        public RedoAction() {
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
            super("Tips", new ImageIcon(AWTUtils.getIcon(null, "/images/TipOfTheDay24.gif")));
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
    
    public class ExtractArticlesAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
         ScheduledThreadPoolExecutor t = new ScheduledThreadPoolExecutor(5);
         t.scheduleAtFixedRate(new Runnable() {
             @Override
             public void run() {
               extractArticles();
             }
         }, 1, 1, TimeUnit.SECONDS);
        }
    }
    
    private void extractArticles()
    {
         String projProperties = (String) PropertiesUI.getInstance().getDefaultProps().get("PROJECTS");
        if (projProperties == null || projProperties.isEmpty()) {
            return; // nothing to do
        }
         JSONObject savedProjJSON = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            savedProjJSON = (JSONObject) parser.parse(projProperties);
            JSONArray projectsJSON = (JSONArray) savedProjJSON.get("prj");
            Iterator<JSONObject> objs = projectsJSON.iterator();
            int selectedIndex = 0;
            int cnt = 0;
            while (objs.hasNext()) {
                final JSONObject p = (JSONObject) objs.next();
                String dir = (String) p.get("dir");
                
                // key = keyWord , value = array of LinkObject
                Hashtable dirProp = PropertiesUI.getInstance().initProjectProperties(dir);
                Iterator it = dirProp.keySet().iterator();
                while (it.hasNext())
                {
                 ArrayList l = (ArrayList)dirProp.get(it.next());
                 for (int i = 0;i < l.size();i++)
                 {
                     LinksObject link = (LinksObject)l.get(i);
                     if (link.getWordCount() == null || link.getWordCount().equals(""))
                     {
                         processArticle(link);
                     }
                 }// end for
                }// end while
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void  processArticle(LinksObject link) throws Exception
    {
        ConnectionManagerUI con = new ConnectionManagerUI();
        con.processArticle(link);
    }
    public class RunProjectAction extends AbstractAction {

        public RunProjectAction() {
            super("Run Project", new ImageIcon(AWTUtils.getIcon(null, "/images/Compile24.gif")));

        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (ProjectsUI.projectList.getSelectedValue() != null) {
                final Object selectedProject = ProjectsUI.projectList.getSelectedValue();
                ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(5);
                stpe.schedule(new Runnable() {
                    @Override
                    public void run() {
                        runProject(selectedProject);
                    }
                }, 1, TimeUnit.SECONDS);

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
            String name = (String) savedProjJSON.get("name");
            dir = (String) savedProjJSON.get("dir");
            keyWords = (String) savedProjJSON.get("keyWords");
            Hashtable prop = new Hashtable();//PropertiesUI.getInstance().initProjectProperties(dir);
            PropertiesUI.getInstance().saveProjectPoerties(prop, dir);
            ProjectsUI.console.append(" Starting Extraction of Links for " + name + "\r\n");
            saveLinksForKeyWords(dir, prop, keyWords);
            InnerFramesUI.getInstance().refreshLinkByFrameName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void saveLinksForKeyWords(String dir, Hashtable prop, String keyWords) {
        String[] keyWord = keyWords.split(",");
        ConnectionManagerUI con = new ConnectionManagerUI();
        for (int i = 0; i < keyWord.length; i++) {
            try {
                synchronized (this) {
                    ProjectsUI.console.append(" -- Extractiong Links for --> " + keyWord[i] + " ....\r\n");
                    ArrayList res = con.searchForLinks( keyWord[i], null);
                    prop.put(keyWord[i], res);
                    wait(800);
                }
//saveProjectPoerties(prop, dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PropertiesUI.getInstance().saveProjectPoerties(prop, dir);
        ProjectsUI.console.append(" >>>>> Finish extracting links");
    }

    // Double click on 'Marked Lines:' label clears all marked lines
    // of current text area.
    public class LinesMouseListener extends MouseAdapter {

        private JList markedLinesList;

        public LinesMouseListener(JList markedLinesList) {
            super();
            this.markedLinesList = markedLinesList;
        }

        public void mousePressed(MouseEvent e) {
            int click = e.getClickCount();
            if (click == 2) {
                MyInternalFrame frame = InnerFramesUI.getInstance().getSelectedFrame();
                if (frame == null) {
                    return;
                }
                JTextArea textArea = frame.getJTextArea();
                MyBasicTextAreaUI myUI = (MyBasicTextAreaUI) textArea.getUI();
                myUI.setPositions(new ArrayList());
                DefaultListModel model = (DefaultListModel) markedLinesList.getModel();
                model.clear();
                textArea.repaint(); // Reflect changes
            }
        }
    }

    // Listener for JList markedLinesList
    //
    public class MarkedLinesListener implements ListSelectionListener {

        private JList markedLinesList;

        public MarkedLinesListener(JList markedLinesList) {
            super();
            this.markedLinesList = markedLinesList;
        }

        public void valueChanged(ListSelectionEvent e) {
            int i = markedLinesList.getSelectedIndex();
            if (i != -1) {
                MyInternalFrame frame = InnerFramesUI.getInstance().getSelectedFrame();
                if (frame != null) {
                    JScrollPane jsp = frame.getScrollPane();
                    JViewport vp = jsp.getViewport();
                    JTextArea textArea = frame.getJTextArea();
                    int h = textArea.getFontMetrics(textArea.getFont()).getHeight();
                    String item = (String) markedLinesList.getSelectedValue();
                    item = item.substring(item.lastIndexOf(" ") + 1);
                    Integer Int = new Integer(item);
                    int n = Int.intValue();
                    Point p = new Point(0, h * (n - 1));
                    vp.setViewPosition(p);
                    frame.show();
                }
            }
        }
    }

    // Enable copy action when console has selected text.
    public class ConsoleCaretListener implements CaretListener {

        JTextArea textArea;

        public ConsoleCaretListener(JTextArea textArea) {
            this.textArea = textArea;
            //  ProjectsUI.console = this.textArea;
        }

        public void caretUpdate(CaretEvent e) {
            cutAndCopy(textArea);
        }
    }

    public void cutAndCopy(JTextArea textArea) {
        try {
            String s = textArea.getSelectedText();
            if (s == null) {
                cutAction.setEnabled(false);
                copyAction.setEnabled(false);
            } else {
                cutAction.setEnabled(true);
                copyAction.setEnabled(true);
            }
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    public class ConsoleListener implements ListSelectionListener {

        private JList consolesList;
        private ArrayList outputList;
        private JViewport viewport;

        public ConsoleListener(JList consolesList, ArrayList outputList, JViewport viewport) {
            super();
            this.consolesList = consolesList;
            this.outputList = outputList;
            this.viewport = viewport;
        }

        public void valueChanged(ListSelectionEvent e) {
            int i = consolesList.getSelectedIndex();
            if (i == -1) // Returns if list empty
            {
                return;
            }
            ArrayList al = (ArrayList) outputList.get(i);
            JTextArea console = (JTextArea) al.get(1);
            console.setCaretPosition(console.getText().length());
            updateStopAction(this.consolesList, outputList, viewport);

            // Update buttons of font style if radioButton is selected.
            /* if (radioButton.isSelected()) {

                Font font = console.getFont();
                String fontName = font.getName();
                int fontSize = font.getSize();
                if (font.isBold()) {
                    boldButton.setSelected(true);
                } else {
                    boldButton.setSelected(false);
                }
                if (font.isItalic()) {
                    italicButton.setSelected(true);
                } else {
                    italicButton.setSelected(false);
                }
                cbFonts.setSelectedItem(fontName);
                cbSizes.setSelectedItem(Integer.toString(fontSize));

            }  // if
             */
        }  // valueChanged
    }

    public void updateStopAction(JList consolesList, ArrayList outputList, JViewport viewport) {
        int i = consolesList.getSelectedIndex();
        if (i != -1) {
            ArrayList list = (ArrayList) outputList.get(i);
            JTextArea console = (JTextArea) list.get(1);
            viewport.setView(console);
            if (list.get(2) == null) {
                stopAction.setEnabled(false);
            } else {
                stopAction.setEnabled(true);
            }
        }
    }

    // Listener for JList consolesList. Each item of the consolesList is an
    // ArrayList such that:
    //	 item.get(0) == Boolean
    //	 item.get(1) == JTextArea
    //	 item.get(2) == Process/null
    //    item.get(3) == Run/Build thread
    public class ProjectListener implements ListSelectionListener {

        private JList projectList;

        //private Object selectedProjectItem;
        public ProjectListener(JList projectList) {
            super();
            this.projectList = projectList;
            // this.selectedProjectItem = selectedProjectItem;

        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int i = projectList.getSelectedIndex();
            if (i == -1) // Returns if list empty
            {
                return;
            }
            ProjectsUI.selectedProjectItem = projectList.getSelectedValue();

        }

    }

    public class ClearAction extends AbstractAction {

        private JList consolesList;
        private ArrayList outputList;

        public ClearAction(JList consolesList, ArrayList outputList) {
            super();
            this.consolesList = consolesList;
            this.outputList = outputList;
        }

        public void actionPerformed(ActionEvent e) {
            int i = consolesList.getSelectedIndex();
            ArrayList al = (ArrayList) outputList.get(i);
            JTextArea console = (JTextArea) al.get(1);
            MyBasicTextAreaUI myUI = (MyBasicTextAreaUI) console.getUI();
            myUI.setPositions(new ArrayList()); // Clear highlighted lines 
            console.setText("");
        }
    }

    // Caret listener that updates status labels of each file
    //
    class GoAction extends AbstractAction {

        private JTextField goToLine;

        GoAction(JTextField goToLine) {
            setEnabled(false);
            this.goToLine = goToLine;
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = InnerFramesUI.getInstance().getSelectedFrame();
            if (frame == null) {
                setEnabled(false);
                return;
            }
            JTextArea jta = frame.getJTextArea();
            JScrollPane jsp = frame.getScrollPane();
            JViewport vp = jsp.getViewport();
            int h = jta.getFontMetrics(jta.getFont()).getHeight();
            Point p = new Point();
            p.x = 0;
            int line = 1;
            try {
                line = Integer.parseInt(goToLine.getText());
            } catch (NumberFormatException nfe) {
                // invalidInput();
                //    transferFocusToTextArea();
                return;
            }
            int totalLines = jta.getLineCount();
            if (totalLines == 0 || line < 1) {
                p.y = 0;
            } else if (line > totalLines) {
                p.y = h * (totalLines - 1);
            } else {
                p.y = h * (line - 1);
            }
            vp.setViewPosition(p);
            jta.setCaretPosition(jta.viewToModel(p));
            // revalidate() does the trick of clearing abnormal drawing if
            // p is in the last line
            jta.revalidate();
            //transferFocusToTextArea();
        }
    }

    class TabAction extends AbstractAction {

        private JTextField tabField;

        TabAction(JTextField tabField) {
            setEnabled(false);
            this.tabField = tabField;
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = InnerFramesUI.getInstance().getSelectedFrame();
            if (frame == null) {
                setEnabled(false);
                return;
            }
            int tabSize = 0;
            try {
                tabSize = Integer.parseInt(tabField.getText());
                if (tabSize < 1) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                // invalidInput();
                //  transferFocusToTextArea();
                return;
            }
            JTextArea jta = frame.getJTextArea();
            jta.setTabSize(tabSize);
            jta.setUI(jta.getUI()); // Fix JTextArea.setTabSize() bug
            //transferFocusToTextArea();
        }
    }

    public class MyWindowListener extends WindowAdapter {

        private JDesktopPane desktop;
        private JFrame frm;

        public MyWindowListener(JDesktopPane desktop, JFrame frm) {
            super();
            this.desktop = desktop;
            this.frm = frm;
        }

        public void windowClosing(WindowEvent e) {
            closeEditor(desktop, frm);
        }
    }   // End MyWindowListener
// Close all frames displayed in desktop. Showed a nice trick of using
    // PropertyVetoException to check each file before closing. Check 
    // CloseListener for how.
    //

    public void closeEditor(JDesktopPane desktop, JFrame frm) {

        JInternalFrame jif[] = desktop.getAllFrames();
        for (int i = 0; i < jif.length; i++) {
            try {
                jif[i].setClosed(true);
            } catch (PropertyVetoException pve) {
                // Exception caught when closing event vetoed.
            }
        }
        int size = desktop.getAllFrames().length;
        if (size == 0) {
            exit(frm);
        }

    }   // End closeEditor

    private void exit(JFrame frm) {
        frm.setVisible(false);
        // Clean up unwanted resource
        frm.dispose();
        // findDialog.dispose();
        //commandDialog.dispose();
        //optionDialog.dispose();

        //fileHistory.saveHistoryEntries(); // save entries for next session
        //saveProperties();
        Toolkit.getDefaultToolkit().beep();
        System.exit(0);
    }
}
