/*
 * OpenPoject.java
 *
 * Created on October 1, 2018, 9:28 AM
 */
package articlecreator.gui.components;

import articlecreator.gui.components.ui.ActionsUI;
import articlecreator.gui.components.ui.ProjectItem;
import articlecreator.gui.components.ui.FileChooserUI;
import articlecreator.gui.components.ui.ProjectsUI;
import articlecreator.gui.components.ui.PropertiesUI;
import articlecreator.net.ConnectionManagerUI;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.EditorKit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.nodes.Document;

/**
 *
 * @author alibaba0507
 */
public class OpenPoject extends javax.swing.JPanel {

    //private Hashtable defaultProperties;
    // private DefaultListModel projectListModel;
    // private JList projectList;
    private JPopupMenu popupList;
    private JFileChooser chooser;
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    private ProjectItem selectedObj;
    private JPopupMenu popupArticleList;
    private String objectDir;

    /**
     * Creates new form OpenPoject
     */
    public OpenPoject() {
        super();
        //     this.propertiesUI = propertiesUI;
        /* this.projectList = projectList;
        this.projectListModel = projectListModel;
         */

        chooser = new FileChooserUI().createFileChooser(FileChooserUI.DIR_ONLY);
        if (ProjectsUI.selectedProjectItem != null
                && ProjectsUI.selectedProjectItem instanceof ProjectItem) {
            this.selectedObj = (ProjectItem) ProjectsUI.selectedProjectItem;
            //this.objectDir = ((ProjectItem)ProjectsUI.selectedProjectItem).
        }
        //this.defaultProperties = defaultProperties;
        setLayout(new FlowLayout());

        initComponents();
        jPanel1.setPreferredSize(new Dimension(400, 450));
        jPanel2.setPreferredSize(new Dimension(400, 450));
        jPanel3.setPreferredSize(new Dimension(400, 450));

        jScrollPane3.setPreferredSize(new Dimension(300, 350));
        jScrollPane2.setPreferredSize(new Dimension(300, 350));
        // txtWebView.setSize(550, 550);
        //jPanel3.setSize(650,650);
        DefaultTableModel tblModel = new DefaultTableModel(new Object[][]{
            {null, null, null, null}
        },
                new String[]{
                    "Key Words", "Title", "URL", "Words Count"
                }) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        jTable1.setModel(tblModel);
        parseSelectedObject();
        String type = ("text/html");
        final EditorKit kit = txtWebView.getEditorKitForContentType(type);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                txtWebView.setEditorKit(kit);
            }
        });
        popupList = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem();

        menuItem.setAction(new ActionsUI().new PasteAction());
        popupList.add(menuItem);
        txtKeyWords.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                ShowPopup(e);
            }

            private void ShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupList.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        popupArticleList = new JPopupMenu();
        final JMenuItem menuItemArticles = new JMenuItem();
        menuItemArticles.setAction(new ActionsUI().new CleanSelectedArticles(this));
        menuItemArticles.setAction(new ActionsUI().new SpinSelectedArticles(this));
        popupArticleList.add(menuItemArticles);
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Hashtable prop = PropertiesUI.getInstance().initProjectProperties(objectDir);
                    int row = jTable1.getSelectedRow();
                    String keyWord = (String) ((DefaultTableModel) jTable1.getModel()).getValueAt(row, 0);
                    String url = (String) ((DefaultTableModel) jTable1.getModel()).getValueAt(row, 2);
                    ArrayList l = (ArrayList) prop.get(keyWord);
                    Iterator it = l.iterator();
                    while (it.hasNext()) {
                        LinksObject obj = (LinksObject) it.next();
                        if (obj.equals(url)) {
                            if (obj.getLocalHTMLFile() == null || obj.getLocalHTMLFile().equals("")) {
                                break;
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    Cursor cursor = OpenPoject.this.getParent().getCursor();
                                    OpenPoject.this.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                    Document doc = new ConnectionManagerUI().openFile(obj.getLocalHTMLFile());
                                    Document docSpin = new ConnectionManagerUI().spinFile(obj.getLocalHTMLFile());
                                    if (doc != null) {
                                        txtWebView.setText(doc.outerHtml());
                                        jTabbedPane1.setSelectedIndex(2);
                                        txtWebView.setCaretPosition(0);
                                        txtWebView.getCaret().setSelectionVisible(true);
                                        // this will move scroll to the top for JEDitor
                                        jScrollPane3.getVerticalScrollBar().setValue(0);
                                    }
                                    OpenPoject.this.getParent().setCursor(cursor);

                                }
                            });

                        }
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
                    if (jTable1.getSelectedRows().length > 0) {
                        menuItemArticles.setEnabled(true);
                    } else {
                        menuItemArticles.setEnabled(false);
                    }
                    popupArticleList.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtProjectDirectory = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeyWords = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btnProjectFileChooser = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnProjectSave = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtProjName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnImport = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtWebView = new javax.swing.JEditorPane();

        jLabel4.setText("Or comma separated");

        txtKeyWords.setColumns(20);
        txtKeyWords.setRows(5);
        txtKeyWords.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(txtKeyWords);
        txtKeyWords.getAccessibleContext().setAccessibleName("");
        txtKeyWords.getAccessibleContext().setAccessibleDescription("");

        jLabel1.setText("Project Name");

        btnProjectFileChooser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Open24.gif"))); // NOI18N
        btnProjectFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProjectFileChooserActionPerformed(evt);
            }
        });

        jLabel2.setText("KeyWords");

        btnProjectSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Save24.gif"))); // NOI18N
        btnProjectSave.setText("Save");
        btnProjectSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProjectSaveActionPerformed(evt);
            }
        });

        jLabel5.setText("Saved Directory");

        jLabel3.setText("One Per Line");

        btnImport.setIcon(new javax.swing.ImageIcon("C:\\Users\\alibaba0507\\Downloads\\dev\\java projects\\dev\\ArticleCreator\\src\\images\\Import24.gif")); // NOI18N
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnProjectSave)
                .addGap(20, 20, 20))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(450, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(70, 70, 70))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addComponent(jLabel3))
                            .addGap(133, 133, 133)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(txtProjectDirectory)
                            .addGap(18, 18, 18)
                            .addComponent(btnProjectFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(23, 23, 23))
                        .addComponent(jScrollPane1)
                        .addComponent(txtProjName, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(btnProjectSave)
                .addGap(22, 22, 22))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtProjName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel4))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtProjectDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(btnProjectFileChooser)))
                    .addContainerGap(63, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(550, 550));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setHorizontalScrollBar(null);
        jScrollPane2.setMaximumSize(new java.awt.Dimension(780, 800));

        jScrollPane2.setViewportView(jTable1);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("tab2", jPanel2);

        jPanel3.setMaximumSize(new java.awt.Dimension(550, 550));
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtWebView.setMaximumSize(new java.awt.Dimension(550, 550));
        jScrollPane3.setViewportView(txtWebView);

        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("tab4", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void save() {
        String projName, keyWords, dir;
        if (txtProjName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(btnProjectSave, "Please enter valid Project Name",
                    "Save Project", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtKeyWords.getText().isEmpty()) {
            JOptionPane.showMessageDialog(btnProjectSave, "Please enter KeyWords",
                    "Save Project", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtProjectDirectory.getText().isEmpty()) {
            JOptionPane.showMessageDialog(btnProjectSave, "Please enter Project Directory",
                    "Save Project", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String projProperties = (String) PropertiesUI.getInstance().getDefaultProps().get("PROJECTS");
        if (projProperties == null || projProperties.isEmpty()) {
            projProperties = "";
        }
        JSONObject projJson = new JSONObject();
        projJson.put("name", txtProjName.getText());
        String s_keyWords = txtKeyWords.getText();
        s_keyWords = s_keyWords.replace("\r\n", ",").replace("\n", ",");
        projJson.put("keyWords", s_keyWords);
        projJson.put("dir", txtProjectDirectory.getText());
        JSONArray projectsJSON = new JSONArray();
        String savedProjects = (String) PropertiesUI.getInstance().getDefaultProps().get("PROJECTS");
        JSONObject savedProjJSON = new JSONObject();
        if (savedProjects != null && !savedProjects.equals("")) {
            JSONParser parser = new JSONParser();
            try {
                savedProjJSON = (JSONObject) parser.parse(savedProjects);
                projectsJSON = (JSONArray) savedProjJSON.get("prj");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Iterator<JSONObject> objs = projectsJSON.iterator();
        boolean prjFound = false;
        while (objs.hasNext()) {
            JSONObject p = (JSONObject) objs.next();
            if (((String) p.get("name")).equals(txtProjName.getText())) {
                prjFound = true;
                p.put("keyWords", s_keyWords);
                p.put("dir", txtProjectDirectory.getText());
                break;
            }
        }// end while
        if (!prjFound) {
            projectsJSON.add(projJson);

        }
        savedProjJSON.put("prj", projectsJSON);
        PropertiesUI.getInstance().getDefaultProps().put("PROJECTS", savedProjJSON.toJSONString());
        PropertiesUI.getInstance().saveProperties();

        reloadProjectTree();
    }
    private void btnProjectSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProjectSaveActionPerformed
        save();

    }//GEN-LAST:event_btnProjectSaveActionPerformed

    private void btnProjectFileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProjectFileChooserActionPerformed

        while (true) { // To keep chooser open if open a directory, then press open button

            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            //chooser.rescanCurrentDirectory();
            int returnVal = chooser.showOpenDialog(OpenPoject.this);
            File file = chooser.getSelectedFile();

            if (returnVal == JFileChooser.CANCEL_OPTION || returnVal == -1) {
                break;
            }
            // Necessary checking for 3 conditions to avoid exceptions
            if (returnVal == JFileChooser.APPROVE_OPTION && file != null && file.isDirectory()) {
                txtProjectDirectory.setText(file.getAbsolutePath());
                break;
            }
        }  // End while
    }//GEN-LAST:event_btnProjectFileChooserActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        while (true) { // To keep chooser open if open a directory, then press open button

            chooser = new FileChooserUI().createFileChooser(FileChooserUI.FILE_ONLY);
            chooser.setAcceptAllFileFilterUsed(true);
            //chooser.rescanCurrentDirectory();
            int returnVal = chooser.showOpenDialog(OpenPoject.this);
            File file = chooser.getSelectedFile();

            if (returnVal == JFileChooser.CANCEL_OPTION || returnVal == -1) {
                break;
            }
            // Necessary checking for 3 conditions to avoid exceptions
            if (returnVal == JFileChooser.APPROVE_OPTION && file != null && file.isFile()) {
                //txtProjectDirectory.setText(file.getAbsolutePath());
                break;
            }
        }
    }//GEN-LAST:event_btnImportActionPerformed
    
    public String getProjectDir()
    {
        return this.objectDir;
    }
    public void refreshLinksTable() {
        try {
            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                tableModel.removeRow(0);
            }
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(350);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(10);
            String dir = this.objectDir;
            if (dir == null || dir.equals("")) {
                return;
            }
            Hashtable prop = PropertiesUI.getInstance().initProjectProperties(dir);//editor.initProjectProperties((String) p.get("dir" ));
            Iterator it = prop.keySet().iterator();
            while (it.hasNext()) {
                String keyWord = (String) it.next();
                //String jsonString = (String)prop.get(keyWord);
                //   Hashtable links = (Hashtable)prop.get(keyWord);
                ArrayList links = (ArrayList) prop.get(keyWord);
// JSONParser tableParser = new JSONParser();
                // JSONArray arr = (JSONArray)  tableParser.parse(jsonString);
                //Iterator jsonIt=  arr.iterator();
                // Iterator jsonIt = links.keySet().iterator();
                Iterator jsonIt = links.iterator();
                while (jsonIt.hasNext()) {
                    LinksObject obj = (LinksObject) jsonIt.next();
                    //  JSONObject ob = (JSONObject)jsonIt.next();
                    //  String wordCnt = (String)ob.get("wordCnt");
                    //  if (wordCnt == null) wordCnt = "";
                    String[] values = {obj.getKeyWord(),
                        obj.getTitle(), obj.getLink(), obj.getWordCount()};
                    tableModel.addRow(values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseSelectedObject() {
        if (selectedObj == null) {
            return;
        }
        JSONParser parser = new JSONParser();
        try {
            JSONObject p = (JSONObject) parser.parse(selectedObj.getJSONObject());
            txtProjName.setText((String) p.get("name"));
            txtKeyWords.setText((String) p.get("keyWords"));
            txtProjectDirectory.setText((String) p.get("dir"));
            this.objectDir = (String) p.get("dir");
            refreshLinksTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadProjectTree() {
        if (ProjectsUI.projectListModel == null) {
            return;
        }

        ProjectsUI.projectListModel.clear();
        String savedProjects = (String) PropertiesUI.getInstance().getDefaultProps().get("PROJECTS");
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
                if (((String) p.get("name")).equals(txtProjName.getText())) {
                    selectedIndex = cnt;
                }
                cnt++;
                ProjectsUI.projectListModel.addElement(new ProjectItem() {
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
            ProjectsUI.projectList.setSelectedIndex(cnt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnProjectFileChooser;
    private javax.swing.JButton btnProjectSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea txtKeyWords;
    private javax.swing.JTextField txtProjName;
    private javax.swing.JTextField txtProjectDirectory;
    private javax.swing.JEditorPane txtWebView;
    // End of variables declaration//GEN-END:variables

}
