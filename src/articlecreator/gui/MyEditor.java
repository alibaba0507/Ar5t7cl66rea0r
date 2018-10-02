package articlecreator.gui;

/**
 * Copyright 1999-2002 Matthew Robinson and Pavel Vorobiev. All Rights Reserved.
 *
 * =================================================== This program contains
 * code from the book "Swing" 1st Edition by Matthew Robinson and Pavel Vorobiev
 * http://www.spindoczine.com/sbe
 * ===================================================
 *
 * The above paragraph must be included in full, unmodified and completely
 * intact in the beginning of any source code file that references, copies or
 * uses (in any way, shape or form) code contained in this file.
 */
import articlecreator.HttpUrlConnectionExample;
import articlecreator.ParseHTMLArticles;
import articlecreator.gui.dl.*;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.undo.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.lang.reflect.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import javax.swing.filechooser.FileView;
// import gui.*; // For wheelmouse support
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import za.co.utils.AWTUtils;

// MyEditor 1.04. Last updated: 3/07/2002
// Written and tested on J2SDK1.4.0, Win98
// MyEditor 1.0 released on 15/01/2002.
// Author: Samuel Huang 
//public class MyEditor extends JMouseWheelFrame implements FileHistory.IFileHistory {
// Uncomment the above line and delete the 1st line below for wheelmouse support on 
// win32 platform by downloading the gui package (see Thanks.txt for site) and put
// it in the same directory where MyEditor class is.
public class MyEditor extends JFrame implements FileHistory.IFileHistory {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String JAVA_HOME = System.getProperty("java.home");
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    private final String USER_AGENT = "Mozilla/5.0";
    private JDesktopPane desktop;
    private JLabel statusLabel1, statusLabel2, statusLabel3;
    private JSplitPane vSplit, hSplit, listSplit;
    private JFileChooser chooser;
    private JComboBox cbFonts, cbSizes, cbFiles;
    private JViewport viewport;
    private ArrayList outputList = new ArrayList();
    private DefaultListModel consolesListModel, projectListModel;
    private JList consolesList, markedLinesList, projectList;
    private SmallToggleButton italicButton, boldButton;
    private JButton clearButton, goButton, tabButton;
    private InternalFrameListener internalFrameListener;
    private UndoHandler undoHandler;
    private JTextField goToLine, tabField;
    private JRadioButton radioButton;
    // This is 
    private Action openAction, newAction, findAndReplaceAction, saveAction, saveAsAction, commandAction, stopAction, cutAction, copyAction, pasteAction, compileAction, runAction, appletAction, exitAction, clearAction, selectAll, deleteAction, goAction, tabAction, buildAction, tipsAction, aboutAction, readMeAction;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private FindDialog findDialog;  // Dialog for finding and replacing word
    private CommandDialog commandDialog;
    private OptionDialog optionDialog;
    private String message[] = {
        " ",
        " ",
        "Save before closing?",
        " "
    };
    private int m_count = 0, console_count = 1;
    private JToolBar toolBar;
    private JMenuBar menuBar;
    private ColorMenu foregroundMenu1, backgroundMenu1, foregroundMenu2, backgroundMenu2;
    private JPopupMenu popup;
    private JPopupMenu popupProj;
    private MouseListener popupListener;
    private RadioButtonAction radioButtonAction;
    private FileHistory fileHistory;
    private Hashtable actionTable = new Hashtable(), defaultProps = null;
    private Object selectedProjectItem;
    private JTextArea console;
    private boolean hasStartProcess;

    public MyEditor() {

        super("MyEditor");

        // Set up the GUI.
        desktop = new JDesktopPane();
        toolBar = new JToolBar();
        menuBar = new JMenuBar();

        loadActionTable();
        initListeners();
        initActions();
        initProperties();

        // Set up menu bar
        setJMenuBar(createMenuBar());

        createPopup();
        createPopupProject();
        fileHistory = new FileHistory(this); // init FileHistory
        fileHistory.initFileMenuHistory();

        // Make dragging faster:
        desktop.putClientProperty("JDesktopPane.dragMode", "outline");

        consolesList = new JList();
        consolesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        consolesListModel = new DefaultListModel();

        projectList = new JList();
        projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectListModel = new DefaultListModel();
        projectList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JInternalFrame frame = createProjectFrame(null, null, null);
                    try {
                        frame.moveToFront();
                        frame.setMaximum(true);
                        frame.setSelected(true);
                    } catch (Exception ez) {
                        ez.printStackTrace();
                    }
                }
            }

            public void mousePressed(MouseEvent e) {
                ShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                ShowPopup(e);
            }

            private void ShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupProj.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        try {
            consolesList.setModel(consolesListModel);
            projectList.setModel(projectListModel);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        JScrollPane commandScrollPane = new JScrollPane(consolesList);
        JScrollPane projectsScrollPane = new JScrollPane(projectList);

        JPanel commandPanel = new JPanel(new BorderLayout());
        JLabel commandLabel = new JLabel("Consoles:");
        commandLabel.setIcon(new ImageIcon(AWTUtils.getIcon(desktop, "/images/History24.gif")));
        commandLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        commandPanel.add(commandLabel, BorderLayout.NORTH);
        commandPanel.add(commandScrollPane, BorderLayout.CENTER);

        JPanel projectPanel = new JPanel(new BorderLayout());
        JLabel projectLabel = new JLabel("Projects:");
        projectLabel.setIcon(new ImageIcon(AWTUtils.getIcon(desktop, "/images/Open24.gif")));

        projectLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        projectPanel.add(projectLabel, BorderLayout.NORTH);
        projectPanel.add(projectsScrollPane, BorderLayout.CENTER);

        MnemonicTabbedPane tabbedPane = new MnemonicTabbedPane();
        String[] tabs = {"Projects", "Console"};
        char[] ms = {'P', 'C',};
        int[] keys = {KeyEvent.VK_0, KeyEvent.VK_1};
        tabbedPane.addTab(tabs[0], projectPanel);
        tabbedPane.setMnemonicAt(0, ms[0]);
        tabbedPane.addTab(tabs[1], commandPanel);
        tabbedPane.setMnemonicAt(1, ms[1]);
        //tabbedPane.setMnemonicAt(i, keys[i]);

        markedLinesList = new JList();
        markedLinesList.setModel(new DefaultListModel());
        markedLinesList.addListSelectionListener(new markedLinesListener());
        markedLinesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Dimension dim = new Dimension(0, 0);

        JScrollPane lineScrollPane = new JScrollPane(markedLinesList);
        JPanel linePanel = new JPanel(new BorderLayout());
        JLabel lineLabel = new JLabel("Marked Lines:");
        lineLabel.addMouseListener(new LinesMouseListener());
        lineLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        linePanel.add(lineLabel, BorderLayout.NORTH);
        linePanel.add(lineScrollPane, BorderLayout.CENTER);

        listSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane/*commandPanel*/, linePanel);
        listSplit.setDividerLocation(140);

        hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listSplit, desktop);
        hSplit.setOneTouchExpandable(true);
        hSplit.setDividerLocation(100);
        hSplit.setMinimumSize(dim);

        // Initialize default console window for displaying program output
        console = new JTextArea();
        String setting = (String) defaultProps.get("SETTING");
        if (setting.equals("CUSTOM")) {
            customizeTextArea(console, "CUSTOM_CONSOLE");
        } else {
            customizeTextArea(console, "DEFAULT_TEXTAREA");
        }
        //
        console.setWrapStyleWord(true);
        console.setEditable(false);
        console.setDoubleBuffered(true);
        console.addCaretListener(new ConsoleCaretListener(console));
        ArrayList al = new ArrayList(4);
        al.add(new Boolean(true));
        al.add(console);
        al.add(null);
        al.add(null);
        outputList.add(al);  // Reserve place for Process object
        consolesListModel.addElement("Console " + console_count);
        console_count++;

        // Creating view port for console window
        JScrollPane jsp = new JScrollPane(console,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        viewport = jsp.getViewport();
        MyBasicTextAreaUI myUID = new MyBasicTextAreaUI(console, null, true);
        myUID.setColor(getLineColor((String) defaultProps.get("SETTING"), "CONSOLE"));
        console.setUI(myUID);
        console.addMouseListener(new ConsoleMouseListener(console, myUID, this));
        console.setText("JUST TO TEST >>>>");
        consolesList.setSelectedIndex(0);
        consolesList.addListSelectionListener(new ConsoleListener());
        projectList.setSelectedIndex(0);
        projectList.addListSelectionListener(new ProjectListener());
        // GUI layout

        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.add(jsp, BorderLayout.CENTER);
        consolePanel.setMinimumSize(dim);

        vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, hSplit, consolePanel);
        vSplit.setOneTouchExpandable(true);

        try {
            listSplit.setResizeWeight(0.5);
            vSplit.setResizeWeight(1);
        } catch (IllegalArgumentException iae) {
        }
        vSplit.setDividerLocation(300);

        cbFiles = new JComboBox();
        cbFiles.addActionListener(cbFilesListener);
        JPanel northPanel = new JPanel();

        northPanel.setLayout(new BorderLayout());
        northPanel.add(toolBar, BorderLayout.NORTH);
        northPanel.add(cbFiles, BorderLayout.SOUTH);
        Container contentPane = getContentPane();
        contentPane.add(northPanel, BorderLayout.NORTH);
        contentPane.add(vSplit, BorderLayout.CENTER);
        contentPane.add(createStatusPanel(), BorderLayout.SOUTH);

        addWindowListener(new MyWindowListener());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height - 50);
        reloadProjectTree();

        hasStartProcess = true;
        java.util.Timer t = new java.util.Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    while (hasStartProcess) {
                        startProcess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 200, 500);

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

    private void runProject(Object selectedProject) {
        ProjectItem item = (ProjectItem) selectedProject;

        JSONParser parser = new JSONParser();

        String dir = "";
        String keyWords = "";
        try {
            JSONObject savedProjJSON = (JSONObject) parser.parse(item.getJSONObject());
            dir = (String) savedProjJSON.get("dir");
            keyWords = (String) savedProjJSON.get("keyWords");
            Hashtable prop = initProjectProperties(dir);
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

    private void extractTextFromArticle(String url, int cnt, String dir) {
        String charset = "UTF-8";
        String userAgent = USER_AGENT; // Change this to your company's name and bot homepage!
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

    private void extractLinksForKeyWord(Hashtable prop, String keyWord) {
        try {
            String urlSearch = (String) defaultProps.get("SEARCH_ENGINE");
            if (urlSearch == null || urlSearch == "") {
                urlSearch = "http://www.google.com/search?q=";
            }
            String search = keyWord;//"stackoverflow";
            String charset = "UTF-8";
            String userAgent = USER_AGENT; // Change this to your company's name and bot homepage!
            Elements links = Jsoup.connect(urlSearch + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");
            JSONObject mainObj = new JSONObject();
            JSONArray arrJSON = new JSONArray();
            for (org.jsoup.nodes.Element link : links) {
                final String title = link.text();
                //  final String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                final String url = URLDecoder.decode(link.absUrl("href").substring(link.absUrl("href").indexOf('=') + 1, link.absUrl("href").indexOf('&')), "UTF-8");

                if (!url.startsWith("http")) {
                    continue; // Ads/news/etc.
                }
                JSONObject o = new JSONObject();
                o.put("title", title);
                o.put("URL", url);
                arrJSON.add(o);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        console.append("Title: " + title + "\r\n");
                        console.append("URL: " + url + "\r\n");
                        console.update(console.getGraphics());
                    }
                });

                System.out.println("Title: " + title);
                System.out.println("URL: " + url);
            }
            mainObj.put(keyWord, arrJSON);
            // JSONParser parser  =new JSONParser();
            prop.put(keyWord, arrJSON.toJSONString());
            //.    get().select(".g>.r>a");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedProject() {
        String projProperties = (String) defaultProps.get("PROJECTS");
        if (projProperties == null || projProperties.isEmpty()) {
            return;
        }

        Object selectedProject = projectList.getSelectedValue();
        if (selectedProject == null) {
            return;
        }

        JSONParser parser = new JSONParser();
        JSONArray projectsJSON = null;
        JSONObject savedProjJSON = null;
        try {
            savedProjJSON = (JSONObject) parser.parse(projProperties);
            projectsJSON = (JSONArray) savedProjJSON.get("prj");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Iterator<JSONObject> objs = projectsJSON.iterator();

        while (objs.hasNext()) {
            JSONObject p = (JSONObject) objs.next();
            if (((String) p.get("name")).equals(selectedProject.toString())) {
                projectsJSON.remove(p);
                break;
            }
        }// end while

        savedProjJSON.put("prj", projectsJSON);
        defaultProps.put("PROJECTS", savedProjJSON.toJSONString());
        saveProperties();
        reloadProjectTree();
    }

    private void initListeners() {
        internalFrameListener = new MyInternalFrameAdapter();
        radioButtonAction = new RadioButtonAction();
        popupListener = new PopupListener();
        undoHandler = new UndoHandler();
    }

    private void loadActionTable() {
        Action[] actions = (new JTextArea()).getActions();
        for (int i = 0; i < actions.length; ++i) {
            actionTable.put(actions[i].getValue(Action.NAME),
                    actions[i]);
        }
    }

    public Action getAction(String name) {
        return (Action) actionTable.get(name);
    }
    // *********************************************
    //   All Listener classes assemble here
    // *********************************************
    // Listener for combo box 'cbFiles'.
    //
    ActionListener cbFilesListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Return")) {
                return;
            }
            Object object = cbFiles.getSelectedItem();
            String pathName;
            if (object != null) // To stop NullPointerException.
            {
                pathName = object.toString();
            } else {
                return;
            }
            JInternalFrame frame = getFrame(pathName);
            if (frame == null) {
                return;
            }
            try {
                frame.moveToFront();
                frame.setMaximum(true);
                frame.setSelected(true);
            } catch (PropertyVetoException e1) {
                e1.printStackTrace();
            }
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    transferFocusToTextArea();
                }
            });
        }
    };  // End cbFilesListener
    // Listener for combo box 'cbFonts'
    //
    ActionListener cbFontsListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {

            MyInternalFrame frame = getSelectedFrame();
            if (!radioButton.isSelected()) {

                if (frame != null) {

                    String fontName = cbFonts.getSelectedItem().toString();
                    int fontSize = 0;
                    try {
                        fontSize = Integer.parseInt(cbSizes.getSelectedItem().toString());
                    } catch (NumberFormatException ex) {
                        return;
                    }

                    JTextArea textArea = frame.getJTextArea();
                    Font font = textArea.getFont();
                    int style = font.getStyle();
                    textArea.setFont(new Font(fontName, style, fontSize));
                    JTextArea lineArea = frame.getLineArea();
                    lineArea.setFont(new Font(fontName, style, fontSize));

                    // Necessary here to transfer focus back to textArea or the user
                    // can't keep on typing in textArea after selection in cbFonts.
                    textArea.requestFocus();
                }

            } else {

                int i = consolesList.getSelectedIndex();
                ArrayList al = (ArrayList) outputList.get(i);
                JTextArea console = (JTextArea) al.get(1);
                Font font = console.getFont();
                int style = font.getStyle();
                String fontName = cbFonts.getSelectedItem().toString();
                int fontSize = 0;
                try {
                    fontSize = Integer.parseInt(cbSizes.getSelectedItem().toString());
                } catch (NumberFormatException ex) {
                    return;
                }
                console.setFont(new Font(fontName, style, fontSize));
                if (frame != null) {
                    JTextArea textArea = frame.getJTextArea();
                    textArea.requestFocus();
                }
            } // End if

        }  // End actionPerformed
    }; // End cbFontsListener
    // Listener for combo box 'cbSizes'.
    //
    ActionListener cbSizesListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {

            MyInternalFrame frame = getSelectedFrame();
            if (!radioButton.isSelected()) {

                if (frame != null) {	// Apply font size setting to selected editor

                    int fontSize = 0;
                    try {
                        fontSize = Integer.parseInt(cbSizes.getSelectedItem().toString());
                    } catch (NumberFormatException ex) {
                        return;
                    }
                    String fontName = cbFonts.getSelectedItem().toString();
                    JTextArea textArea = frame.getJTextArea();
                    Font font = textArea.getFont();
                    int style = font.getStyle();
                    textArea.setFont(new Font(fontName, style, fontSize));
                    JTextArea lineArea = frame.getLineArea();
                    lineArea.setFont(new Font(fontName, style, fontSize));

                    // Necessary here to transfer focus back to textArea or the user
                    // can't keep on typing in textArea after selection in cbSizes.
                    textArea.requestFocus();

                }

            } else {  // Apply font size setting to selected console

                int i = consolesList.getSelectedIndex();
                ArrayList al = (ArrayList) outputList.get(i);
                JTextArea console = (JTextArea) al.get(1);
                Font font = console.getFont();
                int style = font.getStyle();
                int fontSize = 0;
                try {
                    fontSize = Integer.parseInt(cbSizes.getSelectedItem().toString());
                } catch (NumberFormatException ex) {
                    return;
                }
                String fontName = cbFonts.getSelectedItem().toString();
                console.setFont(new Font(fontName, style, fontSize));
                if (frame != null) {
                    JTextArea textArea = frame.getJTextArea();
                    textArea.requestFocus();
                }
            }

        } // End actionPerformed
    }; // End cbSizesListener

    // Listener that handles closing event of a file.
    //
    class CloseListener implements VetoableChangeListener {

        MyInternalFrame frame;
        UndoManager undo;

        CloseListener(MyInternalFrame frame, UndoManager undo) {
            this.frame = frame;
            this.undo = undo;
        }

        public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
            boolean isClosed = true;
            String name = e.getPropertyName();
            if (name.equals(JInternalFrame.IS_CLOSED_PROPERTY)) {
                Boolean oldValue = (Boolean) e.getOldValue(),
                        newValue = (Boolean) e.getNewValue();
                if (oldValue == Boolean.FALSE && newValue == Boolean.TRUE) {
                    if (undo.canUndo()) {
                        int answer = closingCheck(frame);
                        if (answer == JOptionPane.YES_OPTION) {
                            String fileName = frame.getFile().getName();
                            if (fileName.startsWith("Untitled")) {
                                isClosed = save(frame, true);
                            } else {
                                isClosed = save(frame, false);
                            }
                        } else if (answer == JOptionPane.CANCEL_OPTION) {
                            isClosed = false;
                        }
                        if (!isClosed) {
                            throw new PropertyVetoException(" ", e);
                        }
                    }
                }

            }

        }
    } // End CloseListener

    // Listener for undoable edit events.
    //
    class UndoHandler implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent e) {
            UndoManager undo = getSelectedFrame().getUndoManager();
            UndoableEdit edit = e.getEdit();
            undo.addEdit(edit);
            undoAction.update(undo);
            redoAction.update(undo);
        }
    } // End UndoHandler

    // Listener for JList consolesList. Each item of the consolesList is an
    // ArrayList such that:
    //	 item.get(0) == Boolean
    //	 item.get(1) == JTextArea
    //	 item.get(2) == Process/null
    //    item.get(3) == Run/Build thread
    class ProjectListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int i = projectList.getSelectedIndex();
            if (i == -1) // Returns if list empty
            {
                return;
            }
            selectedProjectItem = projectList.getSelectedValue();

        }

    }

    class ConsoleListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            int i = consolesList.getSelectedIndex();
            if (i == -1) // Returns if list empty
            {
                return;
            }
            ArrayList al = (ArrayList) outputList.get(i);
            JTextArea console = (JTextArea) al.get(1);
            console.setCaretPosition(console.getText().length());
            updateStopAction();

            // Update buttons of font style if radioButton is selected.
            if (radioButton.isSelected()) {

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

        }  // valueChanged
    }

    // Listener for JList markedLinesList
    //
    class markedLinesListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            int i = markedLinesList.getSelectedIndex();
            if (i != -1) {
                MyInternalFrame frame = getSelectedFrame();
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

    // Caret listener that updates status labels of each file
    //
    class MyCaretListener implements CaretListener {

        JTextArea textArea;

        public MyCaretListener(JTextArea textArea) {
            this.textArea = textArea;
        }

        public void caretUpdate(CaretEvent e) {
            int dot = e.getDot();
            int mark = e.getMark();
            if (dot != mark) // Test selection
            {
                deleteAction.setEnabled(true);
            } else {
                deleteAction.setEnabled(false);
            }
            updateStatusPanel();
            cutAndCopy(textArea);
            textArea.requestFocus();
        }
    }

    // Enable copy action when console has selected text.
    class ConsoleCaretListener implements CaretListener {

        JTextArea textArea;

        public ConsoleCaretListener(JTextArea textArea) {
            this.textArea = textArea;
        }

        public void caretUpdate(CaretEvent e) {
            cutAndCopy(textArea);
        }
    }

    // Listener that updates foreground color of selected text area
    //
    class ForegroundListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (e.getActionCommand().equals("File")) {
                if (frame != null) {
                    ColorMenu m = (ColorMenu) e.getSource();
                    JTextArea textArea = frame.getJTextArea();
                    textArea.setForeground(m.getColor());
                    textArea.requestFocus();
                }
            } else {
                int i = consolesList.getSelectedIndex();
                ArrayList al = (ArrayList) outputList.get(i);
                JTextArea console = (JTextArea) al.get(1);
                ColorMenu m = (ColorMenu) e.getSource();
                console.setForeground(m.getColor());
                if (frame != null) {
                    JTextArea textArea = frame.getJTextArea();
                    textArea.requestFocus();
                }
            }
        }
    }

    // Listener that updates background color of selected text area
    class BackgroundListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (e.getActionCommand().equals("File")) {
                if (frame != null) {
                    ColorMenu m = (ColorMenu) e.getSource();
                    JTextArea textArea = frame.getJTextArea();
                    textArea.setBackground(m.getColor());
                    textArea.requestFocus();
                }
            } else {
                int i = consolesList.getSelectedIndex();
                ArrayList al = (ArrayList) outputList.get(i);
                JTextArea console = (JTextArea) al.get(1);
                ColorMenu m = (ColorMenu) e.getSource();
                console.setBackground(m.getColor());
                if (frame != null) {
                    JTextArea textArea = frame.getJTextArea();
                    textArea.requestFocus();
                }
            }
        }
    }

    class BoldListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            JTextArea textArea = null;
            if (!radioButton.isSelected()) {
                if (frame != null) {
                    textArea = frame.getJTextArea();
                }
            } else {
                int i = consolesList.getSelectedIndex();
                ArrayList al = (ArrayList) outputList.get(i);
                textArea = (JTextArea) al.get(1);
            }
            if (textArea != null) {
                int state = e.getStateChange();
                Font font = textArea.getFont();
                String name = font.getName();
                int style = font.getStyle();
                int size = font.getSize();
                if (state == ItemEvent.SELECTED) {
                    style |= Font.BOLD;
                } else {
                    style &= Font.ITALIC;
                }
                Font newFont = new Font(name, style, size);
                textArea.setFont(newFont);
                if (!radioButton.isSelected()) {
                    frame.getLineArea().setFont(newFont);
                }
            }
        }
    }  // End BoldListener

    class ItalicListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            JTextArea textArea = null;
            if (!radioButton.isSelected()) {
                if (frame != null) {
                    textArea = frame.getJTextArea();
                }
            } else {
                int i = consolesList.getSelectedIndex();
                ArrayList al = (ArrayList) outputList.get(i);
                textArea = (JTextArea) al.get(1);
            }
            if (textArea != null) {
                int state = e.getStateChange();
                Font font = textArea.getFont();
                String name = font.getName();
                int style = font.getStyle();
                int size = font.getSize();
                if (state == ItemEvent.SELECTED) {
                    style |= Font.ITALIC;
                } else {
                    style &= Font.BOLD;
                }
                Font newFont = new Font(name, style, size);
                textArea.setFont(newFont);
                if (!radioButton.isSelected()) {
                    frame.getLineArea().setFont(newFont);
                }
            }
        }
    }  // End ItalicListener

    class MyDocumentListener implements DocumentListener {

        DefaultListModel listModel;
        MyBasicTextAreaUI myUID;
        JTextArea textArea;

        MyDocumentListener(JTextArea textArea, JList list) {
            this.listModel = (DefaultListModel) list.getModel();
            this.textArea = textArea;
            this.myUID = (MyBasicTextAreaUI) textArea.getUI();
        }

        public void insertUpdate(DocumentEvent e) {
            saveAction.setEnabled(true);
            updateMarkedLinesList();
        }

        public void removeUpdate(DocumentEvent e) {
            saveAction.setEnabled(true);
            updateMarkedLinesList();
        }

        public void changedUpdate(DocumentEvent e) {
        }

        public void updateMarkedLinesList() {
            listModel.clear();
            ArrayList al = myUID.getPositions();
            Collections.sort(al, MyBasicTextAreaUI.positionsComparator);
            for (int i = 0; i < al.size(); i++) {
                Position p = (Position) al.get(i);
                int offset = p.getOffset();
                String item = "Line: " + Integer.toString(getLine(offset) + 1);
                if (listModel.contains(item)) {
                    al.remove(i);
                    i--;
                    continue;
                }
                listModel.addElement(item);
            }
        }

        public int getLine(int offset) {
            int line = 0;
            try {
                line = textArea.getLineOfOffset(offset);
            } catch (BadLocationException ble) {
                return -1;
            }
            return line;
        }
    } // End MyDocumentListener

    class PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            ShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            ShowPopup(e);
        }

        private void ShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    // Double click on 'Marked Lines:' label clears all marked lines
    // of current text area.
    class LinesMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            int click = e.getClickCount();
            if (click == 2) {
                MyInternalFrame frame = getSelectedFrame();
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

    class MyWindowListener extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            closeEditor();
        }
    }   // End MyWindowListener

    // -------------------------------------------
    //   End of code for Listener classes.
    // -------------------------------------------
    // ***************************************
    //   All Action classes assemble here.
    // ***************************************
    // Action for creating a new empty file.
    //
    class NewAction extends AbstractAction {

        NewAction() {
            super("New Project", new ImageIcon(AWTUtils.getIcon(desktop, "/images/New24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            openProjectFile(null, "New Project");
            //openFile(null);

        }
    } // End NewAction

    // Action for opening a file
    //
    class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open Project", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Open24.gif")));
        }

        public void actionPerformed(ActionEvent e) {

            while (true) { // To keep chooser open if open a directory, then press open button
                chooser.rescanCurrentDirectory();
                int returnVal = chooser.showOpenDialog(MyEditor.this);
                File file = chooser.getSelectedFile();
                if (returnVal == JFileChooser.CANCEL_OPTION || returnVal == -1) {
                    break;
                }
                // Necessary checking for 3 conditions to avoid exceptions
                if (returnVal == JFileChooser.APPROVE_OPTION && file != null && file.isFile()) {
                    openFile(file);
                    break;
                }
            }  // End while

        }	 // End actionPerformed
    }   // End OpenAction

    // Action for showing find and replace dialog.
    //
    class FindAndReplaceAction extends AbstractAction {

        FindAndReplaceAction() {
            super("Find...", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Find24.gif")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {

            MyInternalFrame frame = getSelectedFrame();

            if (frame == null) {
                return;
            }

            placeDialog(findDialog);
            JTextArea jta = frame.getJTextArea();

            String s = null;
            try {
                s = jta.getSelectedText();
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }

            String str = "";

            // Concatenate selection of multiple lines into one line representation.
            // Without concatenation, findDialog drawn abnormally with multiple lines.
            if (s != null) {
                StringTokenizer st = new StringTokenizer(s, "\n");
                if (st.hasMoreTokens()) {
                    str = str + st.nextToken();
                }
                while (st.hasMoreTokens()) {
                    str = str + "\\n" + st.nextToken();
                }
            }
            findDialog.setEditItem(str);
            findDialog.show();

        }
    }  // End FindAndReplaceAction

    // Action for undo file edits
    //
    class UndoAction extends AbstractAction {

        public UndoAction() {
            super("Undo", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Undo24.gif")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (frame != null) {
                UndoManager undo = frame.getUndoManager();
                try {
                    undo.undo();
                } catch (CannotUndoException ex) {
                    System.out.println("Unable to undo: " + ex);
                }
                update(undo);
                redoAction.update(undo);
            }
        }

        private void update(UndoManager undo) {
            if (undo.canUndo()) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    } 	// End UndoAction

    // Action for redo file edits
    //
    class RedoAction extends AbstractAction {

        public RedoAction() {
            super("Redo", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Redo24.gif")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (frame != null) {
                UndoManager undo = frame.getUndoManager();
                try {
                    undo.redo();
                } catch (CannotRedoException ex) {
                    System.out.println("Unable to redo: " + ex);
                }
                update(undo);
                undoAction.update(undo);
            }
        }

        private void update(UndoManager undo) {
            if (undo.canRedo()) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }  // End redo action

    // Action for compiling java source code
    //
    class CompileAction extends AbstractAction {

        public CompileAction() {
            super("Compile Java", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Compile24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (frame != null) {
                File file = frame.getFile();
                String pathName = file.getPath(),
                        fileName = file.getName();
                if (!fileName.endsWith(".java")) {
                    return;
                }
                String dirPath = file.getParent();
                ArrayList list = outputArrayList();
                int index = consolesList.getSelectedIndex();
                String command = "javac " + fileName;
                Run run = new Run(command, dirPath, fileName, list, 1, index, MyEditor.this);
                list.set(3, run);
                run.start();
                stopAction.setEnabled(true);
            }
        }
    } // End CompileAction

    // Action for compiling all java files within the same directory of the 
    // currently selected java file.
    class BuildAction extends AbstractAction {

        public BuildAction() {
            super("Build:", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Import24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (frame != null) {
                File file = frame.getFile(),
                        project = null;
                if (file == null) {
                    return;
                }
                String fileName = file.getName();
                if (!fileName.endsWith(".java")) {
                    return;
                }

                project = file.getParentFile();
                String dirPath = file.getParent();
                ArrayList list = outputArrayList();
                int index = consolesList.getSelectedIndex();
                Build build = new Build(dirPath, project, list, index, MyEditor.this);
                list.set(3, build);
                build.start();
                stopAction.setEnabled(true);

            }
        }
    } // End CompileAction

    // Action for running application
    //
    class RunAction extends AbstractAction {

        public RunAction() {
            super("Run Application", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Application24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (frame != null) {
                File file = frame.getFile();
                String pathName = file.getPath(),
                        fileName = file.getName();
                if (!fileName.endsWith(".java")) {
                    return;
                }
                String dirPath = file.getParent();
                String name = fileName.substring(0, fileName.length() - 5);
                String command = "java " + name;
                ArrayList list = outputArrayList();
                int index = consolesList.getSelectedIndex();
                Run run = new Run(command, dirPath, fileName, list, 2, index, MyEditor.this);
                list.set(3, run);
                run.start();
                stopAction.setEnabled(true);
            }
        }
    }  // End RunAction

    // Action for running applet
    //
    class AppletAction extends AbstractAction {

        public AppletAction() {
            super("Run Applet", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Applet24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (frame != null) {
                File file = frame.getFile();
                String pathName = file.getPath(),
                        fileName = file.getName();
                if (!fileName.endsWith(".java")) {
                    return;
                }
                String dirPath = file.getParent();
                String name = fileName.substring(0, fileName.length() - 5);
                String command = "appletviewer " + name + ".html";
                ArrayList list = outputArrayList();
                int index = consolesList.getSelectedIndex();
                Run run = new Run(command, dirPath, fileName, list, 3, index, MyEditor.this);
                list.set(3, run);
                run.start();
                stopAction.setEnabled(true);
            }
        }
    } // End AppletAction

    // Action for saving a file under a new name.
    //
    class SaveAsAction extends AbstractAction {

        public SaveAsAction() {
            super("Save As", new ImageIcon(AWTUtils.getIcon(desktop, "/images/SaveAs24.gif")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            if (frame != null) {
                save(frame, true);
                UndoManager undo = frame.getUndoManager();
                undo.discardAllEdits();
                saveAction.setEnabled(false);
            }
        }
    }  // End SaveAsAction

    // Action for saving a file under the current file name.
    //
    class SaveAction extends AbstractAction {

        public SaveAction() {
            super("Save...", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Save24.gif")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
            boolean saved = false;
            if (frame != null) {
                JTextArea textArea = frame.getJTextArea();
                String fileName = frame.getFile().getName();
                if (fileName.startsWith("Untitled")) {
                    saved = save(frame, true);
                } else {
                    saved = save(frame, false);
                }
                textArea.requestFocus();
            }
            if (saved) {
                UndoManager undo = frame.getUndoManager();
                undo.discardAllEdits();
                saveAction.setEnabled(false);
                undoAction.setEnabled(false);
                redoAction.setEnabled(false);
            }
        }
    }  // End SaveAction

    class StopAction extends AbstractAction {

        public StopAction() {
            super("Stop Process", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Stop24.gif")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {

            int i = consolesList.getSelectedIndex();
            ArrayList list = (ArrayList) outputList.get(i);
            Process p = (Process) list.get(2);
            if (p == null) {
                return;
            }
            p.destroy();
            Object obj = list.get(3);
            if (obj instanceof Run) {
                Run run = (Run) obj;
                run.terminate();
            } else {
                Build build = (Build) obj;
                build.terminate();
            }
            setEnabled(false);

        }
    }  	 // End StopAction

    // Action associated with commandDialog. 
    //
    class CommandAction extends AbstractAction {

        public CommandAction() {
            super("Run Command");
        }

        public void actionPerformed(ActionEvent e) {
            placeDialog(commandDialog);
            MyInternalFrame frame = getSelectedFrame();
            JTextField commandField = commandDialog.getCommandField();
            JTextField directorydField = commandDialog.getDirectoryField();
            if (frame != null) {
                File file = frame.getFile();
                String fileName = file.getName();
                if (fileName.endsWith(".java")) {
                    commandField.setText("java " + fileName.substring(0, fileName.length() - 5) + " ");
                    directorydField.setText(file.getParent());
                    commandDialog.setFileName(fileName);
                } else {
                    commandField.setText("");
                    directorydField.setText("");
                }

            } else {
                commandField.setText("");
                directorydField.setText("");
            }
            commandDialog.show();
            transferFocusToTextArea();
        }
    }

    class ExitAction extends AbstractAction {

        ExitAction() {
            super("Exit");
        }

        public void actionPerformed(ActionEvent e) {
            closeEditor();
        }
    }

    class ClearAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            int i = consolesList.getSelectedIndex();
            ArrayList al = (ArrayList) outputList.get(i);
            JTextArea console = (JTextArea) al.get(1);
            MyBasicTextAreaUI myUI = (MyBasicTextAreaUI) console.getUI();
            myUI.setPositions(new ArrayList()); // Clear highlighted lines 
            console.setText("");
        }
    }

    class RadioButtonAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (radioButton.isSelected()) {

                int index = consolesList.getSelectedIndex();

                // Remove then add back because we want to use consolesList's listener
                // to update font and color settings of selected console and the 
                // listener does nothing if consolesList selects an already selected item.
                String item = (String) consolesList.getSelectedValue();
                consolesListModel.remove(index);
                consolesListModel.add(index, item);
                consolesList.setSelectedIndex(index);

            } else {

                MyInternalFrame frame = getSelectedFrame();
                // Use MyInternalFrameAdapter to update settings of selected frame
                // on the tool bar
                if (frame != null) {
                    try {
                        Rectangle rec = frame.getBounds();
                        frame.setIcon(true);
                        frame.setIcon(false);
                        frame.setBounds(rec.x, rec.y, rec.width, rec.height);
                    } catch (PropertyVetoException pve) {
                        pve.printStackTrace();
                    }
                }

            }
        }
    }

    class RunProjectAction extends AbstractAction {

        RunProjectAction() {
            super("Run Project", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Compile24.gif")));

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (projectList.getSelectedValue() != null) {
                final Object selectedProject = projectList.getSelectedValue();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        runProject(selectedProject);
                    }
                });

            }
        }

    }

    class DeleteAction extends AbstractAction {

        JTextField jtf;

        DeleteAction() {
            super("Delete", new ImageIcon(AWTUtils.getIcon(desktop, "/images/Delete24.gif")));
            // setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            Object itemSelected = projectList.getSelectedValue();
            if (itemSelected != null) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Do you wnat me to Delete " + itemSelected.toString(), "Warning", dialogButton);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    // Saving code here
                    deleteSelectedProject();
                }
            }
            /*MyInternalFrame frame = getSelectedFrame();
            if (frame == null) {
                setEnabled(false);
                return;
            }
            JTextArea jta = frame.getJTextArea();
            jta.replaceSelection("");
             */

        }
    }

    class GoAction extends AbstractAction {

        GoAction() {
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
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
                invalidInput();
                transferFocusToTextArea();
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
            transferFocusToTextArea();
        }
    }

    class TabAction extends AbstractAction {

        TabAction() {
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            MyInternalFrame frame = getSelectedFrame();
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
                invalidInput();
                transferFocusToTextArea();
                return;
            }
            JTextArea jta = frame.getJTextArea();
            jta.setTabSize(tabSize);
            jta.setUI(jta.getUI()); // Fix JTextArea.setTabSize() bug
            transferFocusToTextArea();
        }
    }

    class TipsAction extends AbstractAction {

        TipsAction() {
            super("Tips", new ImageIcon(AWTUtils.getIcon(desktop, "/images/TipOftheDay24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            openFile(new File(CURRENT_DIR + "\\Tips.txt"));
        }
    }

    class ReadMeAction extends AbstractAction {

        ReadMeAction() {
            super("ReadMe", new ImageIcon(AWTUtils.getIcon(desktop, "/images/History24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            openFile(new File(CURRENT_DIR + "\\Readme.txt"));
        }
    }

    class AboutAction extends AbstractAction {

        AboutAction() {
            super("About MyEditor...", new ImageIcon(AWTUtils.getIcon(desktop, "/images/About24.gif")));
        }

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(
                    MyEditor.this, // parentComponent
                    "MyEditor Version: 1.04 \n"
                    + // Message
                    "Tested on J2SDK1.4.0 \n\n"
                    + "Previous version of JDK \n"
                    + "will not work properly. \n\n"
                    + "Author: Samuel Huang",
                    "MyEditor", // Title
                    JOptionPane.INFORMATION_MESSAGE, // messageType
                    new ImageIcon(AWTUtils.getIcon(desktop, "/images/dining.gif")) // icon 
            );
        }
    }

    // -------------------------------------------
    //   End of code for Action classes.
    // -------------------------------------------
    public void invalidInput() {
        JOptionPane.showMessageDialog(
                MyEditor.this, // parentComponent
                "Invalid Input in Text Field !",
                "Warning", // title
                JOptionPane.ERROR_MESSAGE // optionType
        );
    }

    public void transferFocusToTextArea() {

        MyInternalFrame frame = getSelectedFrame();
        if (frame == null) {
            return;
        }

        // frame.setSelected( true );	doesn't work if 
        // transferFocusToTextArea() is called within
        // catch clause of exception handling
        JTextArea jta = frame.getJTextArea();
        jta.requestFocus();

    }

    public void openProjectFile(final File file, String title) {
        JInternalFrame frame = null;
        try {
            frame = createProjectFrame(null, null, title);
            frame.moveToFront();
            frame.setMaximum(true);
            frame.setSelected(true);
        } catch (Exception pve) {
            pve.printStackTrace();
        }
    }

    public void openFile(final File file) {
        JInternalFrame frame = null;
        if (file == null) {
            frame = createFrame(null, null);
        } else if (file != null) {
            String path = file.getPath();
            if (!checkFileAlreadyOpen(path)) { // If File hasn't opened
                JTextArea textArea = new JTextArea();
                try {
                    FileReader fr = new FileReader(path);
                    textArea.read(fr, null);
                    fr.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                frame = createFrame(textArea, file);
            } else { // If File already opened 
                frame = getFrame(path);
            }
        }
        try {
            frame.moveToFront();
            frame.setMaximum(true);
            frame.setSelected(true);
        } catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                transferFocusToTextArea();
            }
        });
    }

    // Returning the first available console from outputList. If no
    // available console list is found, create a new console entry
    // into outputList.
    //
    public ArrayList outputArrayList() {

        for (int i = 0; i < outputList.size(); i++) {
            ArrayList list = (ArrayList) outputList.get(i);
            Boolean consoleAvailable = (Boolean) list.get(0);
            if (consoleAvailable.equals(Boolean.TRUE)) { 	// Console available
                consolesList.setSelectedIndex(i);
                return list;
            }
        }

        JTextArea console = new JTextArea();
        String setting = (String) defaultProps.get("SETTING");
        if (setting.equals("DEFAULT")) {
            customizeTextArea(console, "DEFAULT_TEXTAREA");
        } else {
            customizeTextArea(console, "CUSTOM_CONSOLE");
        }

        MyBasicTextAreaUI myUID = new MyBasicTextAreaUI(console, null, true);
        myUID.setColor(getLineColor((String) defaultProps.get("SETTING"), "CONSOLE"));
        console.setUI(myUID);
        console.addMouseListener(new ConsoleMouseListener(console, myUID, this));
        console.addCaretListener(new ConsoleCaretListener(console));
        console.setWrapStyleWord(true);
        console.setDoubleBuffered(true);
        console.setEditable(false);
        ArrayList al = new ArrayList(4);
        al.add(new Boolean(true));
        al.add(console);
        al.add(null);
        al.add(null);
        outputList.add(al);
        consolesListModel.addElement("Console " + console_count);
        consolesList.setSelectedIndex(console_count - 1);
        console_count++;

        return al;
    }

    private JMenuBar createMenuBar() {

        JButton button = null;
        JMenuItem menuItem = null;
        JMenu editMenu = new JMenu("Edit"),
                fileMenu = new JMenu("File"),
                executeMenu = new JMenu("Execute"),
                optionMenu = new JMenu("Option"),
                fileWindowMenu = new JMenu("Editor"),
                consoleMenu = new JMenu("Console"),
                helpMenu = new JMenu("Help");

        fileMenu.setMnemonic('f');
        editMenu.setMnemonic('e');
        optionMenu.setMnemonic('o');
        executeMenu.setMnemonic('x');
        helpMenu.setMnemonic('h');

        chooser = new JFileChooser(CURRENT_DIR);
        chooser.setFileView(new CustomFileView());
        chooser.addChoosableFileFilter(new TextFilter());
        chooser.addChoosableFileFilter(new JavaCodeFilter());

        findDialog = new FindDialog(MyEditor.this);
        findDialog.pack();

        commandDialog = new CommandDialog(MyEditor.this);
        commandDialog.pack();

        optionDialog = new OptionDialog(MyEditor.this);
        optionDialog.pack();

        // Opening a new file
        button = new JButton(newAction);
        button.setText(null);
        button.setToolTipText("New Project");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = fileMenu.add(newAction);
        menuItem.setMnemonic('n');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                KeyEvent.CTRL_MASK));

        // Opening an existing file
        button = new JButton(openAction);
        button.setText(null);
        button.setToolTipText("Open File");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = fileMenu.add(openAction);
        menuItem.setMnemonic('o');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                KeyEvent.CTRL_MASK));

        // Add Save button function to toolbar 
        button = new JButton(saveAction);
        button.setText(null);
        button.setToolTipText("Save File");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);

        // Menu items for "Save..." and "Save As" in fileMenu
        menuItem = fileMenu.add(saveAction);
        menuItem.setMnemonic('s');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_MASK));
        menuItem = fileMenu.add(saveAsAction);
        menuItem.setMnemonic('a');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                InputEvent.SHIFT_MASK));

        fileMenu.addSeparator();

        // Exit menu item in file menu
        menuItem = fileMenu.add(exitAction);
        menuItem.setMnemonic('e');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                KeyEvent.CTRL_MASK));

        // Code for setting colors of file window and console
        foregroundMenu1 = new ColorMenu("Foreground Color");
        backgroundMenu1 = new ColorMenu("Background Color");
        foregroundMenu2 = new ColorMenu("Foreground Color");
        backgroundMenu2 = new ColorMenu("Background Color");

        foregroundMenu1.setActionCommand("File");
        backgroundMenu1.setActionCommand("File");
        foregroundMenu2.setActionCommand("Console");
        backgroundMenu2.setActionCommand("Console");

        ForegroundListener foregroundListener = new ForegroundListener();
        BackgroundListener backgroundListener = new BackgroundListener();

        foregroundMenu1.addActionListener(foregroundListener);
        backgroundMenu1.addActionListener(backgroundListener);
        foregroundMenu2.addActionListener(foregroundListener);
        backgroundMenu2.addActionListener(backgroundListener);

        fileWindowMenu.add(foregroundMenu1);
        fileWindowMenu.add(backgroundMenu1);
        optionMenu.add(fileWindowMenu);

        consoleMenu.add(foregroundMenu2);
        consoleMenu.add(backgroundMenu2);
        optionMenu.add(consoleMenu);

        // Menu item for preferences
        menuItem = optionMenu.add(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                optionDialog.pack();
                optionDialog.show();
                optionDialog.Layout();
                transferFocusToTextArea();
            }
        });
        menuItem.setText("Preferences");
        menuItem.setMnemonic('p');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                KeyEvent.CTRL_MASK));

        // Cut button and menu item
        ImageIcon cutIcon = new ImageIcon(AWTUtils.getIcon(desktop, "/images/Cut24.gif"));
        button = new JButton(cutAction);
        button.setText(null);
        button.setIcon(cutIcon);
        button.setToolTipText("Cut Text");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = editMenu.add(cutAction);
        menuItem.setText("Cut");
        menuItem.setMnemonic('x');
        menuItem.setIcon(cutIcon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                KeyEvent.CTRL_MASK));

        // Copy button and menu item
        ImageIcon copyIcon = new ImageIcon(AWTUtils.getIcon(desktop, "/images/Copy24.gif"));
        button = new JButton(copyAction);
        button.setText(null);
        button.setIcon(copyIcon);
        button.setToolTipText("Copy Text");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = editMenu.add(copyAction);
        menuItem.setMnemonic('c');
        menuItem.setText("Copy");
        menuItem.setIcon(copyIcon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                KeyEvent.CTRL_MASK));

        // Paste button and menu item
        ImageIcon pasteIcon = new ImageIcon("/images/Paste24.gif");
        button = new JButton(pasteAction);
        button.setText(null);
        button.setIcon(pasteIcon);
        button.setToolTipText("Paste Text");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = editMenu.add(pasteAction);
        menuItem.setMnemonic('v');
        menuItem.setText("Paste");
        menuItem.setIcon(pasteIcon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                KeyEvent.CTRL_MASK));

        editMenu.addSeparator();

        // Find-&-Replace button and menu item
        button = new JButton(findAndReplaceAction);
        button.setText(null);
        button.setRequestFocusEnabled(false);
        button.setToolTipText("Find a string");
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = editMenu.add(findAndReplaceAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                KeyEvent.CTRL_MASK));
        menuItem.setMnemonic('f');

        // Delete menu item
        menuItem = editMenu.add(deleteAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                KeyEvent.CTRL_MASK));
        menuItem.setMnemonic('d');
        menuBar.add(editMenu);

        editMenu.addSeparator();

        // Undo button for tool bar
        button = new JButton(undoAction);
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("Undo last edit action");
        button.setText(null);
        toolBar.add(button);
        menuItem = editMenu.add(undoAction);

        // Redo button for toolbar
        button = new JButton(redoAction);
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("Redo last edit action");
        button.setText(null);
        toolBar.add(button);
        menuItem = editMenu.add(redoAction);

        editMenu.add(new JSeparator());

        // 'Select All' menu item in edit menu
        menuItem = editMenu.add(selectAll);
        menuItem.setText("Select All");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                KeyEvent.CTRL_MASK));

        // Command menu item in execute menu
        menuItem = executeMenu.add(commandAction);
        menuItem.setMnemonic('r');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                KeyEvent.CTRL_MASK));

        executeMenu.addSeparator();

        // Add Compile function to toolbar and menu item
        button = new JButton(compileAction);
        button.setText(null);
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("Compile Java Source Code");
        toolBar.add(button);
        menuItem = executeMenu.add(compileAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
                KeyEvent.CTRL_MASK));

        // Add Build function to toolbar and menu item
        button = new JButton(buildAction);
        button.setText(null);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("<html>Build Project: Compile all Java files within<br>"
                + "the same directory of opened java file.</html>");
        toolBar.add(button);
        menuItem = executeMenu.add(buildAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
                KeyEvent.CTRL_MASK));

        // Add 'Run Application' function to toolbar and menu item
        button = new JButton(runAction);
        button.setText(null);
        button.setRequestFocusEnabled(false);
        button.setToolTipText("Run Application");
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = executeMenu.add(runAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
                KeyEvent.CTRL_MASK));

        // Add 'Run Applet' function  to toolbar and menu item
        button = new JButton(appletAction);
        button.setText(null);
        button.setToolTipText("Run Applet");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = executeMenu.add(appletAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
                KeyEvent.CTRL_MASK));

        executeMenu.addSeparator();

        // Add Stop function  to toolbar and menu item
        button = new JButton(stopAction);
        button.setText(null);
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("Terminate Current Process");
        toolBar.add(button);
        menuItem = executeMenu.add(stopAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
                KeyEvent.CTRL_MASK));

        menuItem = helpMenu.add(tipsAction);  // Menu item for Tips
        menuItem.setMnemonic('t');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                KeyEvent.CTRL_MASK));
        menuItem = helpMenu.add(readMeAction);  // Menu item for ReadMe
        menuItem.setMnemonic('m');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
                KeyEvent.CTRL_MASK));
        menuItem = helpMenu.add(aboutAction);  // Menu item for About MyEditor
        menuItem.setMnemonic('b');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
                KeyEvent.CTRL_MASK));

        // Add all JMenu instances to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(executeMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Radio button for tool bar
        radioButton = new JRadioButton();
        radioButton.setRequestFocusEnabled(false);
        radioButton.setToolTipText("Apply Text Font Setting to Console if Selected");
        radioButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        radioButton.addActionListener(radioButtonAction);
        toolBar.add(radioButton);

        // Get font names available in system
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();

        // Combo box for selecting font in tool bar
        cbFonts = new JComboBox(fontNames);
        cbFonts.setMaximumSize(cbFonts.getPreferredSize());
        cbFonts.setEditable(false);
        cbFonts.setSelectedItem("Courier New");
        cbFonts.setRequestFocusEnabled(false);
        cbFonts.setBackground(Color.white);
        cbFonts.addActionListener(cbFontsListener);

        // Add font sizes to fontSizes
        String fontSizes[] = new String[64];
        for (int i = 0; i < fontSizes.length; i++) {
            fontSizes[i] = Integer.toString(i + 7);
        }

        // Initialize combo box 'cbSizes'
        cbSizes = new JComboBox(fontSizes);
        cbSizes.setMaximumSize(cbSizes.getPreferredSize());
        cbSizes.setEditable(false);
        cbSizes.setSelectedItem(Integer.toString(13));
        cbSizes.setRequestFocusEnabled(false);
        cbSizes.setBackground(Color.white);
        cbSizes.addActionListener(cbSizesListener);

        toolBar.add(cbFonts);
        toolBar.add(cbSizes);

        // Bold button in tool bar
        ImageIcon icon = new ImageIcon("/images/Bold24.gif");
        boldButton = new SmallToggleButton(false, icon, icon, "Bold Font");
        toolBar.add(boldButton);
        boldButton.addItemListener(new BoldListener());
        boldButton.setMargin(new Insets(0, 0, 0, 0));
        boldButton.initBorder();

        // Italic button in tool bar
        icon = new ImageIcon("/images/Italic24.gif");
        italicButton = new SmallToggleButton(false, icon, icon, "Italic Font");
        toolBar.add(italicButton);
        italicButton.addItemListener(new ItalicListener());
        italicButton.setMargin(new Insets(0, 0, 0, 0));
        italicButton.initBorder();

        return menuBar;

    }  // End of createMenuBar()

    public JPanel createStatusPanel() {

        statusLabel1 = new JLabel("    ");  // Display caret position
        statusLabel2 = new JLabel("    ");  // Display total number of lines in file
        statusLabel3 = new JLabel("    ");  // Display tab size
        // Create lowered labels
        Border loweredBorder = new SoftBevelBorder(SoftBevelBorder.LOWERED);
        Border emptyBorder = new EmptyBorder(-3, 0, -3, 0);

        clearButton = new JButton("    Clear Console    ");
        clearButton.addActionListener(clearAction);

        JPanel panel = new JPanel(new BorderLayout());

        // Nice trick of drawing a thin white line around the panel.
        // Setting panel.setBorder( new EtchedBorder(EtchedBorder.RAISED) );
        // doesn't look as good.
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(4, 1, 1, 1),
                new EtchedBorder(EtchedBorder.RAISED)));

        statusLabel1.setBorder(BorderFactory.createCompoundBorder(
                loweredBorder, emptyBorder));
        statusLabel2.setBorder(BorderFactory.createCompoundBorder(
                loweredBorder, emptyBorder));
        statusLabel3.setBorder(BorderFactory.createCompoundBorder(
                loweredBorder, emptyBorder));

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.add(Box.createRigidArea(new Dimension(1, 0)));
        panel2.add(statusLabel1);
        panel2.add(Box.createRigidArea(new Dimension(4, 0)));
        panel2.add(statusLabel2);
        panel2.add(Box.createRigidArea(new Dimension(4, 0)));
        panel2.add(statusLabel3);
        panel2.add(Box.createRigidArea(new Dimension(4, 0)));

        JLabel label = new JLabel("Go to Line: ");
        label.setBorder(emptyBorder);

        goToLine = new JTextField();
        goToLine.setEnabled(false);
        goToLine.setBorder(new CompoundBorder(new LineBorder(Color.black),
                new EmptyBorder(-2, 0, -2, 0)));
        try {
            goToLine.setColumns(5);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        goButton = new JButton("   Go   ");
        goButton.addActionListener(goAction);
        goButton.setBorder(BorderFactory.createCompoundBorder(
                new EtchedBorder(EtchedBorder.LOWERED), emptyBorder));
        goToLine.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    goButton.doClick();
                }
            }
        });

        JPanel panel3 = new JPanel();
        panel3.add(label);
        panel3.add(goToLine);
        panel3.add(goButton);

        panel2.add(panel3);

        JPanel panel4 = new JPanel();
        panel4.add(clearButton);
        clearButton.setBorder(BorderFactory.createCompoundBorder(
                new EtchedBorder(EtchedBorder.LOWERED), emptyBorder));

        JPanel panel5 = new JPanel();
        label = new JLabel("Tab Size: ");
        label.setBorder(emptyBorder);
        tabField = new JTextField();
        tabField.setEnabled(false);
        tabField.setBorder(new CompoundBorder(new LineBorder(Color.black),
                new EmptyBorder(-2, 0, -2, 0)));

        try {
            tabField.setColumns(3);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        tabButton = new JButton("  Change  ");
        tabButton.addActionListener(tabAction);
        tabButton.setBorder(BorderFactory.createCompoundBorder(
                new EtchedBorder(EtchedBorder.LOWERED), emptyBorder));
        tabField.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    tabButton.doClick();
                }
            }
        });

        panel5.add(label);
        panel5.add(tabField);
        panel5.add(tabButton);

        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(panel2, BorderLayout.WEST);
        panel1.add(new JSeparator(), BorderLayout.CENTER);
        panel1.add(panel5, BorderLayout.EAST);

        panel.add(panel1, BorderLayout.WEST);
        panel.add(panel4, BorderLayout.EAST);

        return panel;

    }

    public void initActions() {

        openAction = new OpenAction();
        newAction = new NewAction();
        saveAsAction = new SaveAsAction();
        saveAction = new SaveAction();
        exitAction = new ExitAction();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
        commandAction = new CommandAction();
        buildAction = new BuildAction();
        compileAction = new CompileAction();
        runAction = new RunAction();
        appletAction = new AppletAction();
        stopAction = new StopAction();
        clearAction = new ClearAction();
        deleteAction = new DeleteAction();
        goAction = new GoAction();
        tabAction = new TabAction();
        tipsAction = new TipsAction();
        readMeAction = new ReadMeAction();
        aboutAction = new AboutAction();

        cutAction = getAction(DefaultEditorKit.cutAction);
        copyAction = getAction(DefaultEditorKit.copyAction);
        pasteAction = getAction(DefaultEditorKit.pasteAction);
        findAndReplaceAction = new FindAndReplaceAction();
        selectAll = getAction(DefaultEditorKit.selectAllAction);

        cutAction.setEnabled(false);
        copyAction.setEnabled(false);
        pasteAction.setEnabled(false);
        selectAll.setEnabled(false);
        activateOmega(false);

    }

    private JInternalFrame createProjectFrame(JTextArea jta, File file, String title) {

        if (title == null && selectedProjectItem == null) {
            title = "New Project";
        } else if (selectedProjectItem != null && title == null) {
            title = selectedProjectItem.toString();
        }
        //     else if (title != null)

        JInternalFrame[] frames = desktop.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (title != null && frames[i].getTitle().equals(title)) {
                return frames[i];
            }
        }

        MyInternalFrame jif = new MyInternalFrame(title, true, true, true, true);
        OpenPoject proj = new OpenPoject(projectListModel, projectList, selectedProjectItem, this);

        proj.setDefaultProperties(defaultProps);
        // Create undo manager for textArea
        UndoManager undo = new UndoManager();
        jif.setUndoManager(undo);
        JScrollPane scroller = new JScrollPane(proj, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Decorate jif 		
        //jif.setScrollPane(scroller);
        jif.setContentPane(scroller);
        jif.setSize(400, 250);	// A necessary statement	
        jif.setVisible(true);
        jif.addVetoableChangeListener(new CloseListener(jif, undo));
        jif.moveToFront();
        try {
            jif.setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        desktop.add(jif);
        return jif;
    }

    // Create a new MyInternalFrame and add it to desktop.
    //
    private JInternalFrame createFrame(JTextArea jta, File file) {

        String fileName = null;
        String pathName = null;

        if (file == null) {
            fileName = "Untitled " + m_count;
            pathName = fileName;
            cbFiles.addItem(fileName);
            file = new File(fileName);  // => file.getName() == fileName 
            m_count++;	// Increment counter for number of Untitled files 
        } else {
            pathName = file.getPath();
            fileName = file.getName();
            cbFiles.addItem(pathName);
            insertPathName(pathName);
        }

        // Set title of internal frame
        String title;
        boolean repeatFileName = checkRepeatFileName(fileName);
        if (repeatFileName) {
            title = pathName;
        } else {
            title = fileName;
        }

        JTextArea textArea;
        if (jta == null) {
            textArea = new JTextArea();
        } else {
            textArea = jta;
        }

        // Create undo manager for textArea
        UndoManager undo = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoHandler);

        MyInternalFrame jif = new MyInternalFrame(title, true, true, true, true);
        jif.addInternalFrameListener(internalFrameListener);
        jif.setJTextArea(textArea);
        jif.setUndoManager(undo);
        jif.setFile(file);

        MyBasicTextAreaUI myUID = new MyBasicTextAreaUI(textArea, markedLinesList, false);
        myUID.setColor(getLineColor((String) defaultProps.get("SETTING"), "EDITOR"));
        textArea.setUI(myUID);
        textArea.addMouseListener(new MyMouseListener(textArea, myUID));
        textArea.getDocument().addDocumentListener(new MyDocumentListener(textArea, markedLinesList));
        textArea.addCaretListener(new MyCaretListener(textArea));
        textArea.addMouseListener(popupListener);

        // Decorate textArea with custom caret. See MyCaret for why. 
        Caret caret = new MyCaret();
        caret.setBlinkRate(500);
        textArea.setCaret(caret);

        String setting = (String) defaultProps.get("SETTING");
        if (setting.equals("DEFAULT")) {
            customizeTextArea(textArea, "DEFAULT_TEXTAREA");
        } else {
            customizeTextArea(textArea, "CUSTOM_EDITOR");
        }

        //	Setting soft tab
        String tab = (String) defaultProps.get("TAB");
        if (tab.equals("SOFT")) {
            KeyListener tabListener = new MyTabKey();
            jif.setTabKeyListener(tabListener);
            textArea.addKeyListener(tabListener);
        }

        int size = ((Integer) defaultProps.get("DEFAULT_TAB_SIZE")).intValue();
        textArea.setTabSize(size);

        JScrollPane scroller = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Decorate jif 		
        jif.setScrollPane(scroller);
        jif.setContentPane(scroller);
        jif.setSize(400, 250);	// A necessary statement	
        jif.setVisible(true);
        jif.addVetoableChangeListener(new CloseListener(jif, undo));

        JTextArea gutter = jif.getLineArea();
        gutter.setCaretPosition(0);

        // Add frame to desktop pane.
        desktop.add(jif);

        try {
            jif.setMaximum(true);
            jif.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }

        return jif;

        // jif adapter activated called 1st before here 
    } // End createFrame

    public void createPopupProject() {
        popupProj = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(deleteAction);
        popupProj.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setAction(new RunProjectAction());
        popupProj.add(menuItem);
    }

    public void createPopup() {

        popup = new JPopupMenu();

        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(cutAction);
        menuItem.setText("Cut");
        menuItem.setIcon(new ImageIcon("/images/Cut24.gif"));
        popup.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setAction(copyAction);
        menuItem.setText("Copy");
        menuItem.setIcon(new ImageIcon("/images/Copy24.gif"));
        popup.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setAction(pasteAction);
        menuItem.setText("Paste");
        menuItem.setIcon(new ImageIcon("/images/Paste24.gif"));
        popup.add(menuItem);

        popup.add(new JSeparator());

        menuItem = new JMenuItem();
        menuItem.setAction(findAndReplaceAction);
        menuItem.setText("Find & Replace");
        popup.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setAction(deleteAction);
        popup.add(menuItem);

        popup.add(new JSeparator());

        menuItem = new JMenuItem();
        menuItem.setAction(commandAction);
        menuItem.setText("Run Command");
        popup.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setAction(selectAll);
        menuItem.setText("Select All");
        popup.add(menuItem);

    }

    private void placeDialog(JDialog dialog) {

        Dimension d1 = dialog.getSize();
        Dimension d2 = MyEditor.this.getSize();
        int x = Math.max((d2.width - d1.width) / 2, 0);
        int y = Math.max((d2.height - d1.height) / 2, 0);
        dialog.setBounds(x + MyEditor.this.getX(),
                y + MyEditor.this.getY(), d1.width, d1.height);

    }

    class MyInternalFrameAdapter extends InternalFrameAdapter {

        public void internalFrameActivated(InternalFrameEvent e) {

            MyInternalFrame frame = (MyInternalFrame) e.getInternalFrame();
            UndoManager undo = frame.getUndoManager();

            // Toggle enabled state of undo and save actions
            if (undo.canUndo()) {
                undoAction.setEnabled(true);
                saveAction.setEnabled(true);
            } else {
                undoAction.setEnabled(false);
                saveAction.setEnabled(false);
            }

            // Toggle enabled state of redo button
            if (undo.canRedo()) {
                redoAction.setEnabled(true);
            } else {
                redoAction.setEnabled(false);
            }

            endsWithJava(frame);
            updateStatusPanel();
            JTextArea textArea = frame.getJTextArea();

            // Update markedLinesList
            MyBasicTextAreaUI myUID = (MyBasicTextAreaUI) textArea.getUI();
            ArrayList list = myUID.getPositions();
            Collections.sort(list, MyBasicTextAreaUI.positionsComparator);
            DefaultListModel model = (DefaultListModel) markedLinesList.getModel();
            model.clear();
            String item = "Line: ";
            int line = 0;
            for (int i = 0; i < list.size(); i++) {
                int offset = ((Position) list.get(i)).getOffset();
                try {
                    line = textArea.getLineOfOffset(offset) + 1;
                } catch (BadLocationException ble) {
                    ble.printStackTrace();
                }
                model.addElement(item + Integer.toString(line));
            }

            cutAndCopy(textArea);
            findAndReplaceAction.setEnabled(true);
            pasteAction.setEnabled(true);
            saveAsAction.setEnabled(true);
            selectAll.setEnabled(true);
            goAction.setEnabled(true);
            goToLine.setEnabled(true);
            tabAction.setEnabled(true);
            tabField.setEnabled(true);

            foregroundMenu1.setColor(textArea.getForeground());
            backgroundMenu1.setColor(textArea.getBackground());

            // Update fonts of activated frame
            Font font = textArea.getFont();
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

            String obj = frame.getFile().getPath();
            cbFiles.setActionCommand("Return"); // Prevent infinite loop in cbFilesListener
            cbFiles.setSelectedItem(obj); // cbFiles listener won't be fired here
            cbFiles.setActionCommand("");

        }

        public void internalFrameClosed(InternalFrameEvent e) {

            // Update cbFiles
            MyInternalFrame myIF = (MyInternalFrame) e.getInternalFrame();
            String name = myIF.getFile().getPath();
            cbFiles.removeItem(name);

            // Update Actions and status labels
            int size = desktop.getAllFrames().length;
            if (size == 0) {
                statusLabel1.setText("    ");
                statusLabel2.setText("    ");
                statusLabel3.setText("    ");
            }
            cleanUp();
            transferFocusToTextArea();

        }

        public void internalFrameIconified(InternalFrameEvent e) {
            cleanUp();
        }

        public void cleanUp() {
            MyInternalFrame frame = getSelectedFrame();
            if (frame == null) {
                DefaultListModel model = (DefaultListModel) markedLinesList.getModel();
                model.clear();
                activateOmega(false);
                saveAction.setEnabled(false);
                saveAsAction.setEnabled(false);
                cutAction.setEnabled(false);
                copyAction.setEnabled(false);
                pasteAction.setEnabled(false);
                findAndReplaceAction.setEnabled(false);
                undoAction.setEnabled(false);
                redoAction.setEnabled(false);
                selectAll.setEnabled(false);
                goAction.setEnabled(false);
                goToLine.setEnabled(false);
                tabAction.setEnabled(false);
                tabField.setEnabled(false);
            }
        }
    }	 // End MyInternalFrameAdapter

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

    // Enable/disable actions associated with compiling, running java application or applet
    public void activateOmega(boolean b) {
        if (b) {
            compileAction.setEnabled(true);
            buildAction.setEnabled(true);
            runAction.setEnabled(true);
            appletAction.setEnabled(true);
        } else {
            compileAction.setEnabled(false);
            buildAction.setEnabled(false);
            runAction.setEnabled(false);
            appletAction.setEnabled(false);
        }
    }

    public void endsWithJava(MyInternalFrame frame) {
        String fileName = frame.getFile().getName();
        if (fileName.endsWith(".java")) {
            activateOmega(true);
        } else {
            activateOmega(false);
        }
    }

    // Initialize labels in status panel
    public void updateStatusPanel() {

        MyInternalFrame frame = getSelectedFrame();
        if (frame != null) {
            JTextArea textArea = frame.getJTextArea();
            int caretPos = textArea.getCaretPosition();
            int lineCount = textArea.getLineCount();
            int tabSize = textArea.getTabSize();
            int currentLine = 1;
            String str = "";
            int c = 1;
            try {

                currentLine = textArea.getLineOfOffset(caretPos);
                int startOffset = textArea.getLineStartOffset(currentLine);
                str = textArea.getText(startOffset, caretPos - startOffset);
                FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
                int spaceWidth = fm.stringWidth(" ");

                // Char width checking is done because tabbing size is unpredictable
                // Note: This doesn't always work.
                for (int i = 0; i < str.length(); i++) {
                    char C = str.charAt(i);
                    if (C == '\t') {
                        c = c + tabSize;
                    } else {
                        c++;
                    }
                }

            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }

            statusLabel1.setText("Line " + (currentLine + 1) + "  Column " + c);
            statusLabel2.setText("Total Lines: " + lineCount);
            statusLabel3.setText("Tab size: " + tabSize);

        }

    } // End updateStatusPanel

    // Show a confirmation dialog before closing a file
    public int closingCheck(MyInternalFrame frame) {
        String s = frame.getFile().getPath();
        message[0] = "File " + s + " has changed";
        return JOptionPane.showConfirmDialog(
                this, // parentComponent
                message, // message
                "MyEditor", // title
                JOptionPane.YES_NO_CANCEL_OPTION, // optionType
                JOptionPane.QUESTION_MESSAGE // messageType
        );
    }

    public boolean save(MyInternalFrame jif, boolean b) {

        File fChosen;
        String path = null,
                fileName = null;

        if (b) {	 // 'Save As' if true

            String string[] = {
                " ",
                "Do you want to replace it?",
                ""
            };

            while (true) {

                chooser.rescanCurrentDirectory();

                if (chooser.showSaveDialog(null)
                        != JFileChooser.APPROVE_OPTION) {
                    return false;
                }

                fChosen = chooser.getSelectedFile();
                if (fChosen == null) {
                    continue;
                }

                path = fChosen.getPath();
                fileName = fChosen.getName();
                string[0] = path + " already exists.";

                if (fChosen.exists()) {

                    int result = JOptionPane.showConfirmDialog(
                            this, // parentComponent
                            string, // message
                            "Save File As", // title
                            JOptionPane.YES_NO_OPTION, // optionType
                            JOptionPane.WARNING_MESSAGE // messageType
                    );

                    if (result == JOptionPane.YES_OPTION) {
                        break;
                    }

                } else {
                    break;
                }

            }	// End while loop.

            String title = null;
            boolean c = checkRepeatFileName(fileName);

            if (c) {
                title = path;
            } else {
                title = fileName;
            }

            String item = (String) cbFiles.getSelectedItem();
            cbFiles.removeItem(item);
            cbFiles.addItem(path);

            jif.setTitle(title);
            jif.setFile(fChosen);
            if (path != null) {
                insertPathName(path);
            }

            endsWithJava(jif);

        } else {  // 'Save...' action

            fChosen = jif.getFile();

        }

        JTextArea textArea = jif.getJTextArea();
        Document doc = textArea.getDocument();
        DefaultEditorKit kit = new DefaultEditorKit();

        try {
            OutputStream out = new FileOutputStream(fChosen);
            kit.write(out, doc, 0, doc.getLength());
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Return focus to JTextArea. JTextArea.requestFocus(); doesn't 
        // work here
        try {
            jif.setSelected(true);
        } catch (PropertyVetoException e1) {
            e1.printStackTrace();
        }

        // Make sure chooser is updated to reflect new file
        chooser.rescanCurrentDirectory();

        return true;

    } // End Save

    private void insertPathName(String path) {

        // Insert path into fileHistory only if fileHistory doesn't have path entry.
        // Ignoring case of path name.
        ArrayList list = fileHistory.getCurrentFileList();
        File file = null;
        boolean exist = false;
        String s = null;
        for (int i = 0; i < list.size(); i++) {
            s = ((File) list.get(i)).getPath();
            if (s.equalsIgnoreCase(path)) {
                exist = true;
                break;
            }
        }

        if (!exist) {
            fileHistory.insertPathname(path);
        } // Hook into FileHistory class

    }

    public boolean checkRepeatFileName(String fileName) {

        // Check to see if intended file to open has the same file name
        // with an opened file, if it has, return true, false otherwise.
        boolean fileNameAlreadyExist = false;
        JInternalFrame array[] = desktop.getAllFrames();
        for (int i = 0; i < array.length; i++) {
            String s = ((MyInternalFrame) array[i]).getFile().getName();
            if (s.equals(fileName)) {
                fileNameAlreadyExist = true;
                break;
            }
        }
        return fileNameAlreadyExist;

    }  // End checkRepeatFileName

    // Check if given path to file is already open
    public boolean checkFileAlreadyOpen(String path) {
        boolean fileAlreadyOpen = false;
        JInternalFrame array[] = desktop.getAllFrames();
        for (int i = 0; i < array.length; i++) {
            if (((MyInternalFrame) array[i]).getFile() != null
                    && ((MyInternalFrame) array[i]).getFile().getPath().equalsIgnoreCase(path)) {
                fileAlreadyOpen = true;
                break;
            }
        }
        return fileAlreadyOpen;
    }

    // Return frame corresponding to given path 
    public JInternalFrame getFrame(String path) {
        JInternalFrame frame = null;
        JInternalFrame array[] = desktop.getAllFrames();
        String s = null;
        for (int i = 0; i < array.length; i++) {
            s = ((MyInternalFrame) array[i]).getFile().getPath();
            if (s.equals(path)) {
                frame = array[i];
                break;
            }
        }
        return frame;
    }  // End getFrame

    // Close all frames displayed in desktop. Showed a nice trick of using
    // PropertyVetoException to check each file before closing. Check 
    // CloseListener for how.
    //
    public void closeEditor() {

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
            exit();
        }

    }   // End closeEditor

    // Return selected frame in desktop
    public MyInternalFrame getSelectedFrame() {
        return (MyInternalFrame) desktop.getSelectedFrame();
    }

    public JViewport getViewport() {
        return viewport;
    }

    public JFileChooser getChooser() {
        return chooser;
    }

    // For use by commandDialog
    public JList getConsolesList() {
        return consolesList;
    }

    // For use by commandDialog
    public Action getStopAction() {
        return stopAction;
    }

    public MouseListener getPopupListener() {
        return popupListener;
    }

    public JTextArea getSelectedConsole() {
        int i = consolesList.getSelectedIndex();
        ArrayList al = (ArrayList) outputList.get(i);
        return (JTextArea) al.get(1);
    }

    // --- Implementation of FileHistory.IFileHistory interface ----------------
    public String getApplicationName() {
        return "MyEditor";
    }

    public JMenu getFileMenu() {
        return menuBar.getMenu(0);
    }

    public Dimension getSize() {
        return super.getSize();
    }

    public JFrame getFrame() {
        return this;
    }

    public Hashtable getDefaultProps() {
        return defaultProps;
    }

    public void loadFile(String path) {
        String fileName = path.substring(path.lastIndexOf(FILE_SEPARATOR) + 1);
        ArrayList list = fileHistory.getCurrentFileList();
        File file = null;
        for (int i = 0; i < list.size(); i++) {
            String s = ((File) list.get(i)).getPath();
            if (s.equals(path)) {
                file = (File) list.get(i);
                break;
            }
        }
        if (file == null) {
            return;
        }
        openFile(file);

    }

    // ----- Implementation of FileHistory.IFileHistory Ends -------------
    public void updateCommandPanel(ArrayList list, int index) {
        JTextArea jta = (JTextArea) list.get(1);
        viewport.setView(jta);
        if (index == consolesList.getSelectedIndex()) // Can't select item already selected
        {
            updateStopAction();
        } else {
            consolesList.setSelectedIndex(index);
        }
    }

    public void updateStopAction() {
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

    class MyMouseListener extends MouseAdapter {

        JTextArea textArea;
        MyBasicTextAreaUI myUID;

        public MyMouseListener(JTextArea textArea, BasicTextAreaUI myUID) {
            this.textArea = textArea;
            this.myUID = (MyBasicTextAreaUI) myUID;
        }

        public void mousePressed(MouseEvent e) {
            int click = e.getClickCount();
            if (click == 2) {
                int y = e.getY();
                int h = textArea.getFontMetrics(textArea.getFont()).getHeight();
                int line = y / h;
                int count = textArea.getLineCount();
                if (line < count) {
                    myUID.addPosition(line, true);
                }
            }
        }
    } // End MyMouseListener

    private void exit() {
        setVisible(false);
        // Clean up unwanted resource
        dispose();
        findDialog.dispose();
        commandDialog.dispose();
        optionDialog.dispose();

        fileHistory.saveHistoryEntries(); // save entries for next session
        saveProperties();
        Toolkit.getDefaultToolkit().beep();
        System.exit(0);
    }

    public void reloadProjectTree() {
        projectListModel.clear();
        String savedProjects = (String) defaultProps.get("PROJECTS");
        JSONObject savedProjJSON = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            savedProjJSON = (JSONObject) parser.parse(savedProjects);
            JSONArray projectsJSON = (JSONArray) savedProjJSON.get("prj");
            Iterator<JSONObject> objs = projectsJSON.iterator();
            int selectedIndex = 0;
            int cnt = 0;
            while (objs.hasNext()) {

                final JSONObject p = (JSONObject) objs.next();
                //if (( (String) p.get("name")).equals(txtProjName.getText()))
                //   selectedIndex = cnt;
                cnt++;
                projectListModel.addElement(new ProjectItem() {
                    @Override
                    public String toString() {
                        return (String) p.get("name");
                    }

                    @Override
                    public String getJSONObject() {
                        return p.toJSONString();
                    }
                });
            }// end while
            projectList.setSelectedIndex(cnt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadProperties() {
        defaultProps = null;
        try {
            FileInputStream fis = new FileInputStream("defaultProperties");
            ObjectInputStream ois = new ObjectInputStream(fis);
            defaultProps = (Hashtable) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Color getLineColor(String setting, String type) {
        // Setting is not returned here becaue this method is going to
        // be reused by FontColorPanel.java
        //
        Color color = null;
        if (setting.equals("DEFAULT") && type.equals("EDITOR")) {
            color = (Color) defaultProps.get("DEFAULT_EDITOR_HIGHLIGHTED_LINE");
        } else if (setting.equals("DEFAULT") && type.equals("CONSOLE")) {
            color = (Color) defaultProps.get("DEFAULT_CONSOLE_HIGHLIGHTED_LINE");
        } else if (setting.equals("CUSTOM") && type.equals("EDITOR")) {
            color = (Color) defaultProps.get("CUSTOM_EDITOR_HIGHLIGHTED_LINE");
        } else if (setting.equals("CUSTOM") && type.equals("CONSOLE")) {
            color = (Color) defaultProps.get("CUSTOM_CONSOLE_HIGHLIGHTED_LINE");
        }
        return color;
    }

    // Save Hashtable defaultProps to a file called defaultProperties
    //
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

    public void customizeTextArea(JTextArea textArea, String type) {
        JTextArea jta = (JTextArea) defaultProps.get(type);
        Font font = jta.getFont();
        textArea.setFont(font);
        textArea.setForeground(jta.getForeground());
        textArea.setBackground(jta.getBackground());
        textArea.setCaretColor(jta.getCaretColor());
        Integer intObj = (Integer) defaultProps.get("DEFAULT_TAB_SIZE");
        textArea.setTabSize(intObj.intValue());
    }

    // Called by FontColorPanel.java to update buttons of font style on tool bar.
    //
    public void updateFontPanel(JTextArea textArea, String type) {
        if ((type.equals("CONSOLE") && radioButton.isSelected())
                || (type.equals("EDITOR") && !radioButton.isSelected())) {
            Font font = textArea.getFont();
            boldButton.setSelected(font.isBold());
            italicButton.setSelected(font.isItalic());
            cbSizes.setSelectedItem(Integer.toString(font.getSize()));
            cbFonts.setSelectedItem(font.getName());
        }
    }

    // ----- Main method of MyEditor.java -------
    public static void main(String[] args) {

        SplashScreen splash = new SplashScreen("/images/MyEditor.jpg");
        splash.setVisible(true);
        MyEditor frame = new MyEditor();
        splash.close();
        frame.setVisible(true);
    }
}  // End MyEditor

// Default caret of JTextArea disappear occassionally after gaining and 
// regaining focus in desktop pane. myCaret gets around this problem.
//
class MyCaret extends DefaultCaret {

    public void focusLost(FocusEvent e) {
        setSelectionVisible(true);
    }
}

class CustomFileView extends FileView {

    private Icon fileIcon = new ImageIcon("/images/FILE.gif");

    public Icon getIcon(File f) {
        Icon icon;
        String suffix = getSuffix(f);
        if (suffix == null) {
            return null;
        }

        if (suffix.equals("java")) {
            icon = fileIcon;
        } else {
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

class MnemonicTabbedPane extends JTabbedPane {

    Hashtable mnemonics = null;

    int condition;

    public MnemonicTabbedPane() {
        setUI(new MnemonicTabbedPaneUI());
        mnemonics = new Hashtable();

        // I don't know which one is more suitable for mnemonic action.
        //setMnemonicCondition(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setMnemonicCondition(WHEN_IN_FOCUSED_WINDOW);
    }

    public void setMnemonicAt(int index, char c) {
        int key = (int) c;
        if ('a' <= key && key <= 'z') {
            key -= ('a' - 'A');
        }
        setMnemonicAt(index, key);
    }

    public void setMnemonicAt(int index, int keyCode) {
        ActionListener action = new MnemonicAction(index);
        KeyStroke stroke = KeyStroke
                .getKeyStroke(keyCode, ActionEvent.ALT_MASK);
        registerKeyboardAction(action, stroke, condition);
        mnemonics.put(new Integer(index), new Integer(keyCode));
    }

    public int getMnemonicAt(int index) {
        int keyCode = 0;
        Integer m = (Integer) mnemonics.get(new Integer(index));
        if (m != null) {
            keyCode = m.intValue();
        }
        return keyCode;
    }

    public void setMnemonicCondition(int condition) {
        this.condition = condition;
    }

    public int getMnemonicCondition() {
        return condition;
    }

    class MnemonicAction implements ActionListener {

        int index;

        public MnemonicAction(int index) {
            this.index = index;
        }

        public void actionPerformed(ActionEvent e) {
            MnemonicTabbedPane tabbedPane = (MnemonicTabbedPane) e.getSource();
            tabbedPane.setSelectedIndex(index);
            tabbedPane.requestFocus();
        }
    }

    class MnemonicTabbedPaneUI extends MetalTabbedPaneUI {

        protected void paintText(Graphics g, int tabPlacement, Font font,
                FontMetrics metrics, int tabIndex, String title,
                Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            MnemonicTabbedPane mtabPane = (MnemonicTabbedPane) tabPane;
            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                g.setColor(tabPane.getForegroundAt(tabIndex));
                BasicGraphicsUtils.drawString(g, title, mtabPane
                        .getMnemonicAt(tabIndex), textRect.x, textRect.y
                        + metrics.getAscent());
            } else {
                g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
                BasicGraphicsUtils.drawString(g, title, mtabPane
                        .getMnemonicAt(tabIndex), textRect.x, textRect.y
                        + metrics.getAscent());
                g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
                BasicGraphicsUtils.drawString(g, title, mtabPane
                        .getMnemonicAt(tabIndex), textRect.x - 1, textRect.y
                        + metrics.getAscent() - 1);
            }
        }
    }
}
