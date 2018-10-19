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
import articlecreator.gui.run.ArticleManagmentMain;
import articlecreator.net.ConnectionManagerUI;
import articlecreator.net.MailService;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;
import sun.security.ssl.SSLSocketImpl;
import za.co.utils.AWTUtils;

/**
 *
 * @author alibaba0507
 */
public class ActionsUI {

    static ScheduledFuture t;
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

    public class CleanSelectedArticles extends AbstractAction {

        private OpenPoject comp;

        public CleanSelectedArticles(OpenPoject comp) {
            super("Clean Articles", new ImageIcon(AWTUtils.getIcon(null, "/images/Delete24.gif")));
            // new Throwable().printStackTrace();
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JTable tbl = (JTable) ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getInvoker();
                    int[] selectedRows = tbl.getSelectedRows();
                    ConnectionManagerUI con = new ConnectionManagerUI();
                    Cursor cursor = CleanSelectedArticles.this.comp.getParent().getCursor();
                    CleanSelectedArticles.this.comp.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    for (int i = 0; i < selectedRows.length; i++) {
                        String keyWord = (String) ((DefaultTableModel) tbl.getModel()).getValueAt(selectedRows[i], 0);
                        String url = (String) ((DefaultTableModel) tbl.getModel()).getValueAt(selectedRows[i], 2);
                        String projDir = CleanSelectedArticles.this.comp.getProjectDir();
                        Hashtable prop = PropertiesUI.getInstance().initProjectProperties(projDir);//editor.initProjectProperties((String) p.get("dir" ));
                        ArrayList links = (ArrayList) prop.get(keyWord);
                        Iterator jsonIt = links.iterator();
                        while (jsonIt.hasNext()) {
                            LinksObject obj = (LinksObject) jsonIt.next();
                            if (obj.equals(url) && obj.getWordCount() != null
                                    && Integer.parseInt(obj.getWordCount()) > 40/* Has some words to check*/) {
                                ProjectsUI.console.append(" >>>> Cleaning [" + obj.getLocalHTMLFile() + "] >>>>\r\n");
                                ProjectsUI.console.setCaretPosition(ProjectsUI.console.getText().length() - 2);
                                ProjectsUI.console.getCaret().setVisible(true);
                                con.cleanFile(obj.getLocalHTMLFile());
                            }
                        }

                    }// end for
                    CleanSelectedArticles.this.comp.getParent().setCursor(cursor);
                }
            });

        }

    }

    public void spinArticle(final String articlePath, final OpenPoject comp) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = comp.getParent().getCursor();
                comp.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                ConnectionManagerUI con = new ConnectionManagerUI();
                ProjectsUI.console.append(" >>>> Spining [" + articlePath + "] >>>>\r\n");
                ProjectsUI.console.setCaretPosition(ProjectsUI.console.getText().length() - 2);
                ProjectsUI.console.getCaret().setVisible(true);
                Document doc = con.spinFile(articlePath);
                comp.getSpinTextCompoent().setText(doc.outerHtml());
                comp.getParent().setCursor(cursor);
            }
        });
    }

    public class EmailSelectedArticles extends AbstractAction {

        private OpenPoject comp;

        public EmailSelectedArticles(OpenPoject comp) {
            super("Email Articles", new ImageIcon(AWTUtils.getIcon(null, "/images/mail.png")));
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JList emailList = new JList();
            emailList.setModel(new DefaultListModel());
            ArrayList blogEmails = (ArrayList) PropertiesUI.getInstance().getDefaultProps().get("BLOGGER_MAILS");

            JScrollPane scrollPane = new JScrollPane(emailList);
            scrollPane.setPreferredSize(new Dimension(250, 200));
            if (blogEmails != null) {
                Iterator it = blogEmails.iterator();
                while (it.hasNext()) {
                    ((DefaultListModel) emailList.getModel()).addElement(it.next());
                }
                int result = JOptionPane.showConfirmDialog(null, scrollPane, "Select Emails", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    int[] indx = emailList.getSelectedIndices();
                    ArrayList emails = new ArrayList();
                    for (int i = 0; i < indx.length; i++) {
                        emails.add(((DefaultListModel) emailList.getModel()).getElementAt(i));
                        //  System.out.println("You entered "
                        //                        +  ((DefaultListModel)emailList.getModel()).getElementAt(i));      
                    }// end for
                    sendEmails(emails);
                } else {
                    System.out.println("User canceled / closed the dialog, result = " + result);
                }
            } else {
                JOptionPane.showMessageDialog(comp, "Blogger Email List is Empty ...");
            }
            /*   JPasswordField password = new JPasswordField();
            final JComponent[] inputs = new JComponent[]{
                new JLabel("First"),
                firstName,
                new JLabel("Last"),
                lastName,
                new JLabel("Password"),
                password
            };
             */

        }

        private void sendEmails(ArrayList blogEmails) {
            int[] indx = this.comp.getArticleTable().getSelectedRows();
            ConnectionManagerUI con = new ConnectionManagerUI();
            String[] bEmails = new String[blogEmails.size()];
            for (int i = 0; i < blogEmails.size(); i++) {
                bEmails[i] = (String) blogEmails.get(i);
            }
            for (int i = 0; i < indx.length; i++) {
                LinksObject l = this.comp.getObjectFromTableIndex(indx[i]);

                File f = new File(l.getLocalHTMLFile());
                String fileToSend = f.getParent() + ArticleManagmentMain.FILE_SEPARATOR
                        + "spin" + ArticleManagmentMain.FILE_SEPARATOR
                        + f.getName();
                Document doc = con.openFile(fileToSend);
                if (doc != null) {
                    MailService mail = new MailService();
                    mail.constractMessade(bEmails, l.getTitle(), doc.toString());
                }
            }
        }

    }

    public class SpinSelectedArticles extends AbstractAction {

        private OpenPoject comp;

        public SpinSelectedArticles(OpenPoject comp) {
            super("Spin Articles", new ImageIcon(AWTUtils.getIcon(null, "/images/articleSpin24.png")));
            // new Throwable().printStackTrace();
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JTable tbl = (JTable) ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getInvoker();
                    int[] selectedRows = tbl.getSelectedRows();
                    ConnectionManagerUI con = new ConnectionManagerUI();
                    Cursor cursor = SpinSelectedArticles.this.comp.getParent().getCursor();
                    SpinSelectedArticles.this.comp.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    for (int i = 0; i < selectedRows.length; i++) {
                        String keyWord = (String) ((DefaultTableModel) tbl.getModel()).getValueAt(selectedRows[i], 0);
                        String url = (String) ((DefaultTableModel) tbl.getModel()).getValueAt(selectedRows[i], 2);
                        String projDir = SpinSelectedArticles.this.comp.getProjectDir();
                        Hashtable prop = PropertiesUI.getInstance().initProjectProperties(projDir);//editor.initProjectProperties((String) p.get("dir" ));
                        ArrayList links = (ArrayList) prop.get(keyWord);
                        Iterator jsonIt = links.iterator();
                        while (jsonIt.hasNext()) {
                            LinksObject obj = (LinksObject) jsonIt.next();
                            if (obj.equals(url) && obj.getWordCount() != null
                                    && Integer.parseInt(obj.getWordCount()) > 40/* Has some words to check*/) {
                                ProjectsUI.console.append(" >>>> Spining [" + obj.getLocalHTMLFile() + "] >>>>\r\n");
                                ProjectsUI.console.setCaretPosition(ProjectsUI.console.getText().length() - 2);
                                ProjectsUI.console.getCaret().setVisible(true);
                                Document doc = con.spinFile(obj.getLocalHTMLFile());
                                SpinSelectedArticles.this.comp.getSpinTextCompoent().setText(doc.outerHtml());
                                //spinArticle(obj.getLocalHTMLFile(), SpinSelectedArticles.this.comp);
                            }
                        }

                    }// end for
                    SpinSelectedArticles.this.comp.getParent().setCursor(cursor);
                }
            });

        }

    }

    public class NewAction extends AbstractAction {

        //private static ActionsUI.NewAction instance;
        public NewAction() {
            super("New Project", new ImageIcon(AWTUtils.getIcon(null, "/images/newProject.jpg")));
            // new Throwable().printStackTrace();
        }

        public void actionPerformed(ActionEvent e) {
            ProjectsUI.selectedProjectItem = null;
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
            //  JOptionPane.showConfirmDialog(null, "Open File Not Implemented yet ...");

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
            // JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");

            System.exit(0);//openFile(null);

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

    public class AddClassAction extends AbstractAction {

        public AddClassAction() {
            super("Add Class", new ImageIcon(AWTUtils.getIcon(null, "/images/class24.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            JTextField txt = (JTextField) ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += ".";
            txt.setText(s);
        }

    }

    public class AddIDAction extends AbstractAction {

        public AddIDAction() {
            super("Add ID", new ImageIcon(AWTUtils.getIcon(null, "/images/id24.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField txt = (JTextField) ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += "#";
            txt.setText(s);
        }
    }

    public class AddTAgAction extends AbstractAction {

        public AddTAgAction() {
            super("Add TAG", new ImageIcon(AWTUtils.getIcon(null, "/images/tag24.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public class AddH1Action extends AbstractAction {

        public AddH1Action() {
            super("Add  H1...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            JTextField txt = (JTextField) ((JPopupMenu) ((Component) ((JPopupMenu) ((Component) ((Component) e.getSource()).getParent())).getInvoker()).getParent()).getInvoker();
            //JTextField txt = (JTextField)((JPopupMenu)((JMenu)((JMenuItem)e.getSource()).getParent()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += "H1";
            txt.setText(s);
        }

    }

    public class AddH2Action extends AbstractAction {

        public AddH2Action() {
            super("Add H2 ...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField txt = (JTextField) ((JPopupMenu) ((Component) ((JPopupMenu) ((Component) ((Component) e.getSource()).getParent())).getInvoker()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += "H2";
            txt.setText(s);
        }

    }

    public class AddSpanAction extends AbstractAction {

        public AddSpanAction() {
            super("Add SPAN ...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField txt = (JTextField) ((JPopupMenu) ((Component) ((JPopupMenu) ((Component) ((Component) e.getSource()).getParent())).getInvoker()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += "span";
            txt.setText(s);
        }

    }

    public class AddDIVAction extends AbstractAction {

        public AddDIVAction() {
            super("Add DIV ...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField txt = (JTextField) ((JPopupMenu) ((Component) ((JPopupMenu) ((Component) ((Component) e.getSource()).getParent())).getInvoker()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += "div";
            txt.setText(s);
        }

    }

    public class AddMAINAction extends AbstractAction {

        public AddMAINAction() {
            super("Add MAIN ...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField txt = (JTextField) ((JPopupMenu) ((Component) ((JPopupMenu) ((Component) ((Component) e.getSource()).getParent())).getInvoker()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += "main";
            txt.setText(s);
        }

    }

    public class AddSECTIONction extends AbstractAction {

        public AddSECTIONction() {
            super("Add SECTION ...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField txt = (JTextField) ((JPopupMenu) ((Component) ((JPopupMenu) ((Component) ((Component) e.getSource()).getParent())).getInvoker()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += "secion";
            txt.setText(s);
        }

    }

    public class AddARTICELction extends AbstractAction {

        public AddARTICELction() {
            super("Add ARTICLE ...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField txt = (JTextField) ((JPopupMenu) ((Component) ((JPopupMenu) ((Component) ((Component) e.getSource()).getParent())).getInvoker()).getParent()).getInvoker();
            String s = txt.getText();
            if (s.length() > 0) {
                s += " ";
            }
            s += "article";
            txt.setText(s);
        }

    }

    public class PasteAction extends AbstractAction {

        public PasteAction() {
            super("Paste", new ImageIcon(AWTUtils.getIcon(null, "/images/Paste24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            Component cmp = ((JMenuItem) e.getSource()).getParent();
            if (cmp instanceof JPopupMenu) {
                cmp = ((JPopupMenu) cmp).getInvoker();
            } else {
                return;
            }
            try {
                // Create a Clipboard object using getSystemClipboard() method
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

                // Get data stored in the clipboard that is in the form of a string (text)
                if (cmp instanceof JTextComponent) {
                    String keyWords = c.getData(DataFlavor.stringFlavor).toString();
                    keyWords = keyWords.replaceAll("(\r\n|\n)", ",");
                    ((JTextComponent) cmp).setText(keyWords);
                }
                //ProjectsUI.console.append(c.getData(DataFlavor.stringFlavor).toString() + "\r\n");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
            if (ProjectsUI.selectedProjectItem != null) {

                int input = JOptionPane.showConfirmDialog(null, "Do you want to delete selected project ...");
                if (input == 0) {
                    int delDirectory = JOptionPane.showConfirmDialog(null, "Do you want to delete All Articles and Directory created by this Project ...");
                    if (ProjectsUI.selectedProjectItem instanceof ProjectItem) {
                        ProjectItem prj = (ProjectItem) ProjectsUI.selectedProjectItem;
                        try {
                            String projProperties = (String) PropertiesUI.getInstance().getDefaultProps().get("PROJECTS");
                            JSONObject savedProjJSON = new JSONObject();
                            JSONParser parser = new JSONParser();
                            JSONObject toBeDeletedProjJSON = (JSONObject) parser.parse(prj.getJSONObject());
                            String deleteName = (String) toBeDeletedProjJSON.get("name");
                            String deleteDir = (String) toBeDeletedProjJSON.get("dir");
                            savedProjJSON = (JSONObject) parser.parse(projProperties);
                            JSONArray projectsJSON = (JSONArray) savedProjJSON.get("prj");
                            Iterator<JSONObject> objs = projectsJSON.iterator();

                            while (objs.hasNext()) {
                                final JSONObject p = (JSONObject) objs.next();
                                String dir = (String) p.get("dir");
                                String name = (String) p.get("name");
                                if (deleteName.equals(name) && deleteDir.equals(dir)) {
                                    projectsJSON.remove(p);
                                    if (delDirectory == 0) {
                                        FileChooserUI.delete(new File(dir));
                                    }
                                    break;
                                }
                            } // end while 
                            savedProjJSON.put("prj", projectsJSON);
                            PropertiesUI.getInstance().getDefaultProps().put("PROJECTS", savedProjJSON.toJSONString());
                            PropertiesUI.getInstance().saveProperties();
                            InnerFramesUI.getInstance().closeFrame(deleteName);
                            ProjectsUI.reloadProjectTree(null);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

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

        public boolean hasStarted = false;
        private java.util.Timer timer;
        private TimerTask task;

        public StopAction() {
            super("Fetch Articles ...", new ImageIcon(AWTUtils.getIcon(null, "/images/go.png")));
            //executor = new ScheduledThreadPoolExecutor(5);
            timer = new java.util.Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    extractArticles(StopAction.this);
                }
            };
        }

        public void actionPerformed(ActionEvent e) {
            // openProjectFile(null, "New Project");
            //JOptionPane.showConfirmDialog(null, "Exit Not Implemented yet ...");
            if (hasStarted) {
                hasStarted = false;
                putValue(Action.SMALL_ICON, new ImageIcon(AWTUtils.getIcon(null, "/images/go.png")));
                stopProces();
            } else {
                hasStarted = true;
                putValue(Action.SMALL_ICON, new ImageIcon(AWTUtils.getIcon(null, "/images/stop.png")));

                startPocess();
            }
            //openFile(null);

        }

        private void newSchedule() {
            timer = new java.util.Timer();
            timer.schedule(task, 1000);

        }

        private void stopProces() {
            task.cancel();
            timer.cancel();

        }

        private void startPocess() {

            newSchedule();

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

    public class ExtractArticlesAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            ScheduledThreadPoolExecutor t = new ScheduledThreadPoolExecutor(5);
            // t.scheduleAtFixedRate(
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    extractArticles(null);
                }
            });
            //, 10, 0, TimeUnit.SECONDS);

        }
    }

    private void extractArticles(StopAction stopAction) {

        String projProperties = (String) PropertiesUI.getInstance().getDefaultProps().get("PROJECTS");
        if (projProperties == null || projProperties.isEmpty()) {
            return; // nothing to do
        }
        JSONObject savedProjJSON = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            savedProjJSON = (JSONObject) parser.parse(projProperties);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JSONArray projectsJSON = (JSONArray) savedProjJSON.get("prj");
        Iterator<JSONObject> objs = projectsJSON.iterator();
        int selectedIndex = 0;
        int cnt = 0;
        JSONObject selectedObject = null;
        if (ProjectsUI.selectedProjectItem instanceof ProjectItem) {
            try {
                selectedObject = (JSONObject) parser.parse(((ProjectItem) ProjectsUI.selectedProjectItem).getJSONObject());
            } catch (ParseException ex) {
                Logger.getLogger(ActionsUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        while (objs.hasNext()) {
            if (stopAction != null && stopAction.hasStarted == false) {
                break;
            }
            final JSONObject p = (JSONObject) objs.next();
            String dir = (String) p.get("dir");
            String name = (String) p.get("name");
            if (selectedObject == null || !selectedObject.get("name").equals(name)
                    || !selectedObject.get("dir").equals(dir)) // key = keyWord , value = array of LinkObject
            {
                continue;
            }

            Hashtable dirProp = PropertiesUI.getInstance().initProjectProperties(dir);
            Iterator it = dirProp.keySet().iterator();
            URL url = null;
            while (it.hasNext()) {
                if (stopAction != null && stopAction.hasStarted == false) {
                    break;
                }
                ArrayList l = (ArrayList) dirProp.get(it.next());
                for (int i = 0; i < l.size(); i++) {
                    if (stopAction != null && stopAction.hasStarted == false) {
                        break;
                    }
                    LinksObject link = (LinksObject) l.get(i);
                    if (link.getWordCount() == null || link.getWordCount().equals("") /*|| Integer.parseInt(link.getWordCount()) < 50*/) {
                        try {
                            processArticle(link, dir);
                            if (link.getWordCount() == null || link.getWordCount().equals("")
                                    || Integer.parseInt(link.getWordCount()) < 150) {
                                //link.setWordCount("1");
                                l.remove(link);
                            }
                            ProjectsUI.console.append(">>>>> Parsing aricle ... [" + link.getTitle() + "] >>>>\r\n");
                            ProjectsUI.console.setCaretPosition(ProjectsUI.console.getText().length() - 2);
                            ProjectsUI.console.getCaret().setVisible(true);

                            url = new URL(link.getLink());
                        } catch (Exception io) {
                            // io.printStackTrace();
                            if (io.getMessage().contains("unrecognized_name")) {

                                dissableSSL(url);
                                link.setWordCount("1");
                            }
                            ProjectsUI.console.append(" >>>> CONNECTION ERROR [" + io.getMessage() + "]\r\n");
                        }
                    }

                    PropertiesUI.getInstance().saveProjectPoerties(dirProp, dir);
                    InnerFramesUI.getInstance().refreshLinkByFrameName(name);
                }// end for
            }// end while
        }
        //}catch (Exception e)
        //{
        //   e.printStackTrace();
        //  ProjectsUI.console.append(" >>>> CONNECTION ERROR [" + e.getMessage() + "]\r\n");
        //  ProjectsUI.console.append(" >>>> Will Resume after [" + (200/60) + "]Minutes\r\n");
        //}
    }

    private void dissableSSL(URL url) {
        try {
            String baseUri = (new StringBuilder()).append(url.getProtocol()).append("://")
                    .append(url.getPath())
                    .toString();
            SSLSocketFactory factory
                    = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket
                    = (SSLSocket) factory.createSocket(baseUri, 80);
            socket.startHandshake();

        } catch (Exception e) {

        }
    }

    private void processArticle(LinksObject link, String dir) throws Exception {
        ConnectionManagerUI con = new ConnectionManagerUI();
        con.processArticle(link, dir);
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
            Hashtable prop = PropertiesUI.getInstance().initProjectProperties(dir);// new Hashtable();//PropertiesUI.getInstance().initProjectProperties(dir);
            // PropertiesUI.getInstance().saveProjectPoerties(prop, dir);
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
                    ArrayList res = con.searchForLinks(keyWord[i], 0);
                    ArrayList oldList = (ArrayList) prop.get(keyWord[i]);
                    if (oldList != null) {
                        Iterator it = oldList.iterator();
                        while (it.hasNext()) {
                            Object old = it.next();
                            if (!res.contains(old)) {
                                res.add(old);
                            }
                        }
                    }
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
            if (cutAction == null) {
                return;
            }
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
            /*  int i = consolesList.getSelectedIndex();
            ArrayList al = (ArrayList) outputList.get(i);
            JTextArea console = (JTextArea) al.get(1);
            MyBasicTextAreaUI myUI = (MyBasicTextAreaUI) console.getUI();
            myUI.setPositions(new ArrayList()); // Clear highlighted lines 
            console.setText("");
             */
            ProjectsUI.console.setText("");
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

    public class Help extends AbstractAction {
        String helpLink;
        public Help(String helpLink) {
            super("Run Project", new ImageIcon(AWTUtils.getIcon(null, "/images/help24.png")));
            this.helpLink = helpLink;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         InnerFramesUI.getInstance().displayHelp(helpLink);
        }

    }
}
