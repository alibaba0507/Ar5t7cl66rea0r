/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.run;

//import articlecreator.gui.MyBasicTextAreaUI;
import articlecreator.gui.components.OpenPoject;
import articlecreator.gui.components.Settings;
import articlecreator.gui.components.ui.ActionsUI;
import articlecreator.gui.components.ui.InnerFramesUI;
import articlecreator.gui.components.ui.ProjectItem;
import articlecreator.gui.components.ui.ProjectsUI;

import articlecreator.gui.components.ui.PropertiesUI;
import articlecreator.gui.components.ui.StatusPanelUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import za.co.utils.AWTUtils;

/**
 *
 * @author alibaba0507
 */
public class ArticleManagmentMain extends JFrame {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String JAVA_HOME = System.getProperty("java.home");
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    public static final int MAX_AMOUNT_OF_BAD_WORDS = 100;
    public static final String REQUEST_URL = "http://thesaurus.altervista.org/thesaurus/v1?";
    public static final String MY_API_KEY = "zyQOWZsKlGVzWBGhc47S"; // "Thesaurus" API KEY
    public static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    

    // private ColorMenu foregroundMenu1, backgroundMenu1, foregroundMenu2, backgroundMenu2;
    private final JDesktopPane desktop;
    private final JToolBar toolBar;
    private final JMenuBar menuBar;
    private JPopupMenu popup;
    private JPopupMenu popupProj;
    private ActionsUI actions;
    private JList consolesList, projectList;
    private DefaultListModel consolesListModel, projectListModel;
    //private final JList markedLinesList;
    private JSplitPane listSplit, hSplit;
    private ArrayList outputList = new ArrayList();
    private int console_count;
    private JViewport viewport;
    private final JSplitPane vSplit;
    private final JComboBox cbFiles;
    private static ArticleManagmentMain instance;
    private JTextArea console;

    public ArticleManagmentMain() {
        super("ArticleManagentSystem");
        if (ArticleManagmentMain.instance == null) {
            ArticleManagmentMain.instance = this;
        }
        desktop = new JDesktopPane();
        toolBar = new JToolBar();
        menuBar = new JMenuBar();
        // create instance this will be singleton instance
        InnerFramesUI innerFramesUI = new InnerFramesUI(desktop);
        // Create menu bars
        setJMenuBar(createMenuBar());
        createPopup();
        createPopupProject();

        // Make dragging faster:
        desktop.putClientProperty("JDesktopPane.dragMode", "outline");
        consolesList = new JList();
        consolesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        consolesListModel = new DefaultListModel();

        projectList = new JList();
        projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectListModel = new DefaultListModel();
        
        ProjectsUI.projectList = projectList;
        ProjectsUI.projectListModel = projectListModel;
        projectList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            Cursor cursor = projectList.getParent().getCursor();
                            projectList.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                            JInternalFrame frame = InnerFramesUI.getInstance().ceateProjectFrame(null, null);
                            // JInternalFrame frame = createProjectFrame(null, null, null);
                            try {
                                frame.moveToFront();
                                frame.setMaximum(true);
                                frame.setSelected(true);
                            } catch (Exception ez) {
                                ez.printStackTrace();
                            }finally{
                               projectList.getParent().setCursor(cursor);
                            }
                        }
                    });

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

        // JScrollPane commandScrollPane = new JScrollPane(consolesList);
        JScrollPane projectsScrollPane = new JScrollPane(projectList);

        /*JPanel commandPanel = new JPanel(new BorderLayout());
        JLabel commandLabel = new JLabel("Consoles:");
        commandLabel.setIcon(new ImageIcon(AWTUtils.getIcon(desktop, "/images/History24.gif")));
        commandLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        commandPanel.add(commandLabel, BorderLayout.NORTH);
        commandPanel.add(commandScrollPane, BorderLayout.CENTER);
         */
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
        //tabbedPane.addTab(tabs[1], commandPanel);
        // tabbedPane.setMnemonicAt(1, ms[1]);
        //tabbedPane.setMnemonicAt(i, keys[i]);

        // markedLinesList = new JList();
        // markedLinesList.setModel(new DefaultListModel());
        //  markedLinesList.addListSelectionListener(new ActionsUI().new MarkedLinesListener(markedLinesList));
        //  markedLinesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Dimension dim = new Dimension(0, 0);

//        JScrollPane lineScrollPane = new JScrollPane(markedLinesList);
        //  JPanel linePanel = new JPanel(new BorderLayout());
        //      JLabel lineLabel = new JLabel("Marked Lines:");
        //    lineLabel.addMouseListener(new ActionsUI().new LinesMouseListener(markedLinesList));
        //    lineLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        //   linePanel.add(lineLabel, BorderLayout.NORTH);
        //   linePanel.add(lineScrollPane, BorderLayout.CENTER);
        listSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane/*commandPanel*/, null/*linePanel*/);
        listSplit.setDividerLocation(140);

        hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listSplit, desktop);
        hSplit.setOneTouchExpandable(true);
        hSplit.setDividerLocation(100);
        hSplit.setMinimumSize(dim);

        // Initialize default console window for displaying program output
        console = new JTextArea();
        ProjectsUI.console = console;
        String setting = (String) PropertiesUI.getInstance().getDefaultProps().get("SETTING");
        if (setting != null && setting.equals("CUSTOM")) {
            customizeTextArea(console, "CUSTOM_CONSOLE");
        } else {
            customizeTextArea(console, "DEFAULT_TEXTAREA");
        }
        //
        console.setWrapStyleWord(true);
        console.setEditable(false);
        console.setDoubleBuffered(true);

        console.addCaretListener(new ActionsUI().new ConsoleCaretListener(console));
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
        //  MyBasicTextAreaUI myUID = new MyBasicTextAreaUI(console, null, true);
        //  myUID.setColor(getLineColor((String) PropertiesUI.getInstance().getDefaultProps().get("SETTING"), "CONSOLE"));
        // console.setUI(myUID);
        //console.addMouseListener(new ConsoleMouseListener(console, myUID, this));
        console.setText("JUST TO TEST >>>>");
        consolesList.setSelectedIndex(0);
        consolesList.addListSelectionListener(new ActionsUI().new ConsoleListener(consolesList, outputList, viewport));
        projectList.setSelectedIndex(0);
        projectList.addListSelectionListener(new ActionsUI().new ProjectListener(projectList));
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
        // cbFiles.addActionListener(cbFilesListener);
        JPanel northPanel = new JPanel();

        northPanel.setLayout(new BorderLayout());
        northPanel.add(toolBar, BorderLayout.NORTH);
        northPanel.add(cbFiles, BorderLayout.SOUTH);
        Container contentPane = getContentPane();
        contentPane.add(northPanel, BorderLayout.NORTH);
        contentPane.add(vSplit, BorderLayout.CENTER);
        contentPane.add(StatusPanelUI.getInstance().createStatusPanel(consolesList, outputList), BorderLayout.SOUTH);

        addWindowListener(new ActionsUI().new MyWindowListener(desktop, this));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height - 50);
        reloadProjectTree();
        // This is where  the articles get scann
        //TODO: Implement this later

        //hasStartProcess = true;
        // Start a process to extract articles if any
        //  new ActionsUI().new ExtractArticlesAction().actionPerformed(null);
        /*
         articleExtractor = new ArticleExtractor(defaultProps, console);
        articleScanerTimer = new java.util.Timer();
        articleScanerTimer.schedule(articleExtractor, 200, 500);
         */
    }

    public void reloadProjectTree() {
        projectListModel.clear();
        String savedProjects = (String) PropertiesUI.getInstance().getDefaultProps().get("PROJECTS");
        if (savedProjects == null) {
            return;
        }
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

    public static ArticleManagmentMain getInstance() {
        if (ArticleManagmentMain.instance == null) {
            ArticleManagmentMain.instance = new ArticleManagmentMain();
        }
        return ArticleManagmentMain.instance;
    }

    public Color getLineColor(String setting, String type) {
        // Setting is not returned here becaue this method is going to
        // be reused by FontColorPanel.java
        //
        Color color = null;
        if (setting.equals("DEFAULT") && type.equals("EDITOR")) {
            color = (Color) PropertiesUI.getInstance().getDefaultProps().get("DEFAULT_EDITOR_HIGHLIGHTED_LINE");
        } else if (setting.equals("DEFAULT") && type.equals("CONSOLE")) {
            color = (Color) PropertiesUI.getInstance().getDefaultProps().get("DEFAULT_CONSOLE_HIGHLIGHTED_LINE");
        } else if (setting.equals("CUSTOM") && type.equals("EDITOR")) {
            color = (Color) PropertiesUI.getInstance().getDefaultProps().get("CUSTOM_EDITOR_HIGHLIGHTED_LINE");
        } else if (setting.equals("CUSTOM") && type.equals("CONSOLE")) {
            color = (Color) PropertiesUI.getInstance().getDefaultProps().get("CUSTOM_CONSOLE_HIGHLIGHTED_LINE");
        }
        return color;
    }

    public void customizeTextArea(JTextArea textArea, String type) {
        JTextArea jta = (JTextArea) PropertiesUI.getInstance().getDefaultProps().get(type);
        Font font = jta.getFont();
        textArea.setFont(font);
        textArea.setForeground(jta.getForeground());
        textArea.setBackground(jta.getBackground());
        textArea.setCaretColor(jta.getCaretColor());
        Integer intObj = (Integer) PropertiesUI.getInstance().getDefaultProps().get("DEFAULT_TAB_SIZE");
        textArea.setTabSize(intObj.intValue());
    }

    public void createPopupProject() {
        popupProj = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(actions.deletAection);
        popupProj.add(menuItem);

        menuItem = new JMenuItem();
        actions = new ActionsUI();
        actions.runProjectAction = new ActionsUI().new RunProjectAction();//rojectAction();
        menuItem.setAction(actions.runProjectAction);
        popupProj.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setAction(new ActionsUI().new StopAction());
        popupProj.add(menuItem);
    }

    public void createPopup() {

        popup = new JPopupMenu();

        JMenuItem menuItem = new JMenuItem();
        actions = new ActionsUI();
        actions.cutAction = new ActionsUI().new CutAction();
        menuItem.setAction(actions.cutAction);
        menuItem.setText("Cut");
        // menuItem.setIcon(new ImageIcon("/images/Cut24.gif"));
        popup.add(menuItem);

        menuItem = new JMenuItem();
        actions.copyAction = new ActionsUI().new CopyAction();
        menuItem.setAction(actions.copyAction);
        menuItem.setText("Copy");
        // menuItem.setIcon(new ImageIcon("/images/Copy24.gif"));
        popup.add(menuItem);

        menuItem = new JMenuItem();
        actions.pasteAction = new ActionsUI().new PasteAction();
        menuItem.setAction(actions.pasteAction);
        menuItem.setText("Paste");
        //  menuItem.setIcon(new ImageIcon("/images/Paste24.gif"));
        popup.add(menuItem);

        popup.add(new JSeparator());

        menuItem = new JMenuItem();
        actions.findAndReplaceAction = new ActionsUI().new FindAndReplaceAction();
        menuItem.setAction(actions.findAndReplaceAction);
        menuItem.setText("Find & Replace");
        popup.add(menuItem);

        menuItem = new JMenuItem();
        actions.deletAection = new ActionsUI().new DeleteAction();
        menuItem.setAction(actions.deletAection);
        popup.add(menuItem);

        popup.add(new JSeparator());

        actions.selectAll = new ActionsUI().new SelectAllAction();
        menuItem = new JMenuItem();
        menuItem.setAction(actions.selectAll);
        menuItem.setText("Select All");
        popup.add(menuItem);

    }

    private JMenuBar createMenuBar() {
        JButton button = null;
        JMenuItem menuItem = null;
        JMenu fileMenu = new JMenu("File"),
                /*     editMenu = new JMenu("Edit"),*/
                executeMenu = new JMenu("Execute"),
                optionMenu = new JMenu("Option"),
                /*fileWindowMenu = new JMenu("Editor"),*/
                /*consoleMenu = new JMenu("Console"),*/
                helpMenu = new JMenu("Help");
        fileMenu.setMnemonic('f');
        // editMenu.setMnemonic('e');
        optionMenu.setMnemonic('o');
        executeMenu.setMnemonic('x');
        helpMenu.setMnemonic('h');

        // Opening a new file
        actions = new ActionsUI();
        actions.newProject = new ActionsUI().new NewAction();
        button = new JButton(actions.newProject);
        button.setText(null);
        button.setToolTipText("New Project");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = fileMenu.add(actions.newProject);
        menuItem.setMnemonic('n');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                KeyEvent.CTRL_MASK));

        /*
        actions.openFile = new ActionsUI().new OpenAction();
        // Opening an existing file
        button = new JButton(actions.openFile);
        button.setText(null);
        button.setToolTipText("Open File");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
         */
        // menuItem = fileMenu.add(actions.openFile);
        // menuItem.setMnemonic('o');
        // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
        //        KeyEvent.CTRL_MASK));
        // actions.saveFile = new ActionsUI().new SavedAction();
        // Add Save button function to toolbar 
        /* 
        button = new JButton(actions.saveFile);
        button.setText(null);
        button.setToolTipText("Save File");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
         */
        // Menu items for "Save..." and "Save As" in fileMenu
        /*
        menuItem = fileMenu.add(actions.saveFile);
        menuItem.setMnemonic('s');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_MASK));

        actions.saveFileAs = new ActionsUI().new SaveFileAsAction();
        
        menuItem = fileMenu.add(actions.saveFileAs);
        menuItem.setMnemonic('a');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                InputEvent.SHIFT_MASK));

        fileMenu.addSeparator();
         */
        actions.exitAction = new ActionsUI().new ExitAction();
        // Exit menu item in file menu
        menuItem = fileMenu.add(actions.exitAction);
        menuItem.setMnemonic('e');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                KeyEvent.CTRL_MASK));

        // Menu item for preferences
        // This is preference box where we can put all
        // our goodies
        final Settings settings = new Settings(this, true);
        Action settingsAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                settings.pack();
                settings.show();
                //settings.Layout();
                transferFocusToTextArea();
            }
        };
        menuItem = optionMenu.add(settingsAction);
        menuItem.setText("Preferences");
        menuItem.setIcon(new ImageIcon(AWTUtils.getIcon(null, "/images/24-settings-silver.png")));
        menuItem.setMnemonic('p');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                KeyEvent.CTRL_MASK));

        // Cut button and menu item
        button = new JButton(settingsAction);
        button.setText(null);
        button.setIcon(new ImageIcon(AWTUtils.getIcon(null, "/images/24-settings-silver.png")));
        button.setToolTipText("Preferences");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);

        /*
        // Cut button and menu item
        actions.cutAction = new ActionsUI().new CutAction();
        button = new JButton(actions.cutAction);
        button.setText(null);
        //button.setIcon(cutIcon);
        button.setToolTipText("Cut Text");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
         */
 /*
       menuItem = editMenu.add(actions.cutAction);
        menuItem.setText("Cut");
        menuItem.setMnemonic('x');
        // menuItem.setIcon(cutIcon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                KeyEvent.CTRL_MASK));

        // Copy button and menu item
        actions.copyAction = new ActionsUI().new CopyAction();
        button = new JButton(actions.copyAction);
        button.setText(null);
        // button.setIcon(copyIcon);
        button.setToolTipText("Copy Text");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = editMenu.add(actions.copyAction);
        menuItem.setMnemonic('c');
        menuItem.setText("Copy");
        //  menuItem.setIcon(copyIcon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                KeyEvent.CTRL_MASK));

        // Paste button and menu item
        actions.pasteAction = new ActionsUI().new PasteAction();
        button = new JButton(actions.pasteAction);
        button.setText(null);
        //button.setIcon(pasteIcon);
        button.setToolTipText("Paste Text");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = editMenu.add(actions.pasteAction);
        menuItem.setMnemonic('v');
        menuItem.setText("Paste");
        //menuItem.setIcon(pasteIcon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                KeyEvent.CTRL_MASK));

        editMenu.addSeparator();

        // Find-&-Replace button and menu item
        actions.findAndReplaceAction = new ActionsUI().new FindAndReplaceAction();
        button = new JButton(actions.findAndReplaceAction);
        button.setText(null);
        button.setRequestFocusEnabled(false);
        button.setToolTipText("Find a string");
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = editMenu.add(actions.findAndReplaceAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                KeyEvent.CTRL_MASK));
        menuItem.setMnemonic('f');

        // Delete menu item
        actions.deletAection = new ActionsUI().new DeleteAction();
        menuItem = editMenu.add(actions.deletAection);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                KeyEvent.CTRL_MASK));
        menuItem.setMnemonic('d');
        menuBar.add(editMenu);

        editMenu.addSeparator();

        // Undo button for tool bar
        actions.undoAction = new ActionsUI().new UndoAction();
        button = new JButton(actions.undoAction);
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("Undo last edit action");
        button.setText(null);
        toolBar.add(button);
        menuItem = editMenu.add(actions.undoAction);

        // Redo button for toolbar
        actions.redoAction = new ActionsUI().new RedoAction();
        button = new JButton(actions.redoAction);
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("Redo last edit action");
        button.setText(null);
        toolBar.add(button);
        menuItem = editMenu.add(actions.redoAction);

        editMenu.add(new JSeparator());

        // 'Select All' menu item in edit menu
        actions.selectAll = new ActionsUI().new SelectAllAction();
        menuItem = editMenu.add(actions.selectAll);
        menuItem.setText("Select All");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                KeyEvent.CTRL_MASK));

        executeMenu.addSeparator();
         */
        // Add Stop function  to toolbar and menu item
        actions.stopAction = new ActionsUI().new StopAction();
        button = new JButton(actions.stopAction);
        button.setText(null);
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("Terminate Current Process");
        toolBar.add(button);
        
        button = new JButton(new ActionsUI().new Help(null));
        button.setText(null);
     //   button.setIcon(new ImageIcon(AWTUtils.getIcon(null, "/images/24-settings-silver.png")));
        button.setToolTipText("Help");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        
        menuItem = executeMenu.add(actions.stopAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
                KeyEvent.CTRL_MASK));

        actions.tipsAction = new ActionsUI().new TipsAction();
        menuItem = helpMenu.add(actions.tipsAction);  // Menu item for Tips
        menuItem.setMnemonic('t');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                KeyEvent.CTRL_MASK));

        actions.readMeAction = new ActionsUI().new ReadMeAction();
        menuItem = helpMenu.add(actions.readMeAction);  // Menu item for ReadMe
        menuItem.setMnemonic('m');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
                KeyEvent.CTRL_MASK));
       

        // Add all JMenu instances to menu bar
        menuBar.add(fileMenu);
        //  menuBar.add(editMenu);
        menuBar.add(executeMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        return menuBar;
    }

    public void transferFocusToTextArea() {

        JInternalFrame frame = getSelectedFrame();
        if (frame == null) {
            return;
        }

        // frame.setSelected( true );	doesn't work if 
        // transferFocusToTextArea() is called within
        // catch clause of exception handling
        //  JTextArea jta = frame.getJTextArea();
        //  jta.requestFocus();
    }

    // Return selected frame in desktop
    public JInternalFrame getSelectedFrame() {
        return (JInternalFrame) desktop.getSelectedFrame();
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
    
   public static String encrypt(String plain) {
   String b64encoded = Base64.getEncoder().encodeToString(plain.getBytes());

   // Reverse the string
   String reverse = new StringBuffer(b64encoded).reverse().toString();

   StringBuilder tmp = new StringBuilder();
   final int OFFSET = 4;
   for (int i = 0; i < reverse.length(); i++) {
      tmp.append((char)(reverse.charAt(i) + OFFSET));
   }
   return tmp.toString();
}
   
   
   public static String decrypt(String secret) {
   StringBuilder tmp = new StringBuilder();
   final int OFFSET = 4;
   for (int i = 0; i < secret.length(); i++) {
      tmp.append((char)(secret.charAt(i) - OFFSET));
   }

   String reversed = new StringBuffer(tmp.toString()).reverse().toString();
   return new String(Base64.getDecoder().decode(reversed));
}
}
