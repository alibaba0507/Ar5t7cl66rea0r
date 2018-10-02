/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.run;

import articlecreator.gui.MyEditor;
import articlecreator.gui.MyInternalFrame;
import articlecreator.gui.components.Settings;
import articlecreator.gui.components.ui.ActionsUI;
import articlecreator.gui.components.ui.InterFamesUI;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import za.co.utils.AWTUtils;

/**
 *
 * @author alibaba0507
 */
public class ArticleManagmentMain extends JFrame {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String JAVA_HOME = System.getProperty("java.home");
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    // private ColorMenu foregroundMenu1, backgroundMenu1, foregroundMenu2, backgroundMenu2;
    private final JDesktopPane desktop;
    private final JToolBar toolBar;
    private final JMenuBar menuBar;
    private JFileChooser chooser;
    private JPopupMenu popup;
    private JPopupMenu popupProj;
    private ActionsUI actions;
    
    public ArticleManagmentMain() {
        super("ArticleManagentSystem");

        desktop = new JDesktopPane();
        toolBar = new JToolBar();
        menuBar = new JMenuBar();
        // create instance this will be singleton instance
        new InterFamesUI(desktop);
        // Create menu bars
        setJMenuBar(createMenuBar());
    }

    public void createPopupProject() {
        popupProj = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(actions.deletAection);
        popupProj.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setAction(new MyEditor.RunProjectAction());
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

    private JMenuBar createMenuBar() {
        JButton button = null;
        JMenuItem menuItem = null;
        JMenu fileMenu = new JMenu("File"),
                editMenu = new JMenu("Edit"),
                executeMenu = new JMenu("Execute"),
                optionMenu = new JMenu("Option"),
                fileWindowMenu = new JMenu("Editor"),
                consoleMenu = new JMenu("Console"),
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

        actions.openFile = new ActionsUI().new OpenAction();
        // Opening an existing file
        button = new JButton(actions.openFile);
        button.setText(null);
        button.setToolTipText("Open File");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        menuItem = fileMenu.add(actions.openFile);
        menuItem.setMnemonic('o');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                KeyEvent.CTRL_MASK));

        actions.saveFile = new ActionsUI().new SavedAction();
        // Add Save button function to toolbar 
        button = new JButton(actions.saveFile);
        button.setText(null);
        button.setToolTipText("Save File");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);

        // Menu items for "Save..." and "Save As" in fileMenu
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

        actions.exitAction = new ActionsUI().new ExitAction();
        // Exit menu item in file menu
        menuItem = fileMenu.add(actions.exitAction);
        menuItem.setMnemonic('e');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                KeyEvent.CTRL_MASK));

        // Menu item for preferences
        // This is preference box where we can put all
        // our goodies
        final Settings settings = new Settings();
        menuItem = optionMenu.add(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                settings.pack();
                settings.show();
                //settings.Layout();
                transferFocusToTextArea();
            }
        });
        menuItem.setText("Preferences");
        menuItem.setMnemonic('p');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                KeyEvent.CTRL_MASK));

        // Cut button and menu item
        actions.cutAction = new ActionsUI().new CutAction();
        button = new JButton(actions.cutAction);
        button.setText(null);
        //button.setIcon(cutIcon);
        button.setToolTipText("Cut Text");
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);

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

        // Add Stop function  to toolbar and menu item
        actions.stopAction = new ActionsUI().new StopAction();
        button = new JButton(actions.stopAction);
        button.setText(null);
        button.setRequestFocusEnabled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setToolTipText("Terminate Current Process");
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
        menuItem = helpMenu.add(actions.readMeAction);  // Menu item for About MyEditor
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
}
