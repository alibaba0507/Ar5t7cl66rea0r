/*
 * OpenPoject.java
 *
 * Created on October 1, 2018, 9:28 AM
 */
package articlecreator.gui;

import java.awt.FlowLayout;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author alibaba0507
 */
public class OpenPoject extends javax.swing.JPanel {

    private Hashtable defaultProperties;
    private DefaultListModel projectListModel;
    private JList projectList;
    /**
     * Creates new form OpenPoject
     */
    public OpenPoject(DefaultListModel projectListModel,JList projectList) {
        super();
        this.projectList = projectList;
        this.projectListModel = projectListModel;
        //this.defaultProperties = defaultProperties;
        setLayout(new FlowLayout());
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtProjName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeyWords = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        txtProjectDirectory = new javax.swing.JTextField();
        btnProjectFileChooser = new javax.swing.JButton();
        btnProjectSave = new javax.swing.JButton();
        btnCancelProject = new javax.swing.JButton();

        jLabel1.setText("Project Name");

        jLabel2.setText("KeyWords");

        jLabel3.setText("One Per Line");

        jLabel4.setText("Or comma separated");

        txtKeyWords.setColumns(20);
        txtKeyWords.setRows(5);
        jScrollPane1.setViewportView(txtKeyWords);
        txtKeyWords.getAccessibleContext().setAccessibleName("");
        txtKeyWords.getAccessibleContext().setAccessibleDescription("");

        jLabel5.setText("Saved Directory");

        btnProjectFileChooser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Open24.gif"))); // NOI18N

        btnProjectSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Save24.gif"))); // NOI18N
        btnProjectSave.setText("Save");
        btnProjectSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProjectSaveActionPerformed(evt);
            }
        });

        btnCancelProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Stop24.gif"))); // NOI18N
        btnCancelProject.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addComponent(jLabel3)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                                .addGap(75, 75, 75))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(txtProjectDirectory, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnProjectFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(9, 9, 9)
                                                                .addComponent(btnProjectSave)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                                                                .addComponent(btnCancelProject)))
                                                .addGap(35, 35, 35))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtProjName, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(txtProjName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel4))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(txtProjectDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(btnProjectFileChooser)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnCancelProject)
                                        .addComponent(btnProjectSave))
                                .addGap(23, 23, 23))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnProjectSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProjectSaveActionPerformed

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

        String projProperties = (String) getDefaultProperties().get("PROJECTS");
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
        String savedProjects = (String) defaultProperties.get("PROJECTS");
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
        getDefaultProperties().put("PROJECTS", savedProjJSON.toJSONString());
        saveProperties();
        reloadProjectTree();
    }//GEN-LAST:event_btnProjectSaveActionPerformed

    public void reloadProjectTree() {
        if (projectListModel == null) {
            return;
        }

        projectListModel.clear();
        String savedProjects = (String) defaultProperties.get("PROJECTS");
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
               if (( (String) p.get("name")).equals(txtProjName.getText()))
                   selectedIndex = cnt;
               cnt++;
                projectListModel.addElement(new projectItem() {
                    @Override
                    public String toString() {
                        return  (String) p.get("name");
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

    public void saveProperties() {
        try {
            FileOutputStream fos = new FileOutputStream("defaultProperties");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(getDefaultProperties());
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelProject;
    private javax.swing.JButton btnProjectFileChooser;
    private javax.swing.JButton btnProjectSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtKeyWords;
    private javax.swing.JTextField txtProjName;
    private javax.swing.JTextField txtProjectDirectory;
    // End of variables declaration//GEN-END:variables

    public Hashtable getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Hashtable defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public interface  projectItem
    {
        public String toString();
        public String getJSONObject();            
    }
}
