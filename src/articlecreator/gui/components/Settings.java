/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components;

import articlecreator.gui.components.ui.ActionsUI;
import articlecreator.gui.components.ui.PropertiesUI;
import articlecreator.gui.run.ArticleManagmentMain;
import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import za.co.utils.AWTUtils;

/**
 *
 * @author alibaba0507
 */
public class Settings extends javax.swing.JDialog {

    private JPopupMenu popupText;

    /**
     * Creates new form Settings
     */
    public Settings(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        // cboParseType.setSelectedIndex(1);
        //cboParseType.setSelectedIndex(0);
        Hashtable prop = PropertiesUI.getInstance().getDefaultProps();
        ArrayList list = (ArrayList) prop.get("USER_SEARCH");
        if (list != null) {
            // prop.put("USER_SEARCH",new ArrayList());
            //  PropertiesUI.getInstance().saveProperties();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ((DefaultListModel) ((JList) lstSavedSearchEngines).getModel()).addElement(it.next());
            }
        }

        popupText = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem();

        menuItem.setAction(new ActionsUI().new AddClassAction());
        popupText.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setAction(new ActionsUI().new AddIDAction());
        popupText.add(menuItem);
        // menuItem = new JMenuItem();
        JMenu m = new JMenu();
        m.setText("Add TAG");
        m.setIcon(new ImageIcon(AWTUtils.getIcon(null, "/images/tag24.png")));
        //menuItem.setAction(new ActionsUI().new AddTAgAction());
        popupText.add(m);
        JMenuItem subMenu = new JMenuItem();
        subMenu.setAction(new ActionsUI().new AddH1Action());
        m.add(subMenu);

        subMenu = new JMenuItem();
        subMenu.setAction(new ActionsUI().new AddH2Action());
        m.add(subMenu);

        subMenu = new JMenuItem();
        subMenu.setAction(new ActionsUI().new AddSpanAction());
        m.add(subMenu);

        subMenu = new JMenuItem();
        subMenu.setAction(new ActionsUI().new AddDIVAction());
        m.add(subMenu);

        subMenu = new JMenuItem();
        subMenu.setAction(new ActionsUI().new AddMAINAction());
        m.add(subMenu);

        subMenu = new JMenuItem();
        subMenu.setAction(new ActionsUI().new AddSECTIONction());
        m.add(subMenu);

        subMenu = new JMenuItem();
        subMenu.setAction(new ActionsUI().new AddARTICELction());
        m.add(subMenu);
        //  Hashtable prop = PropertiesUI.getInstance().getDefaultProps();
        //  String type = (String) cboParseType.getSelectedItem();
        ArrayList listParser = (ArrayList) prop.get("USER_PARSER");
        if (listParser != null) {
            Iterator lIt = listParser.iterator();
            while (lIt.hasNext()) {
                ((DefaultListModel) lstSaveHtmlTags.getModel()).addElement(lIt.next());
            }
        }
        String gmail = (String) PropertiesUI.getInstance().getDefaultProps().get("USER_GMAIL");
        if (gmail != null) {
            txtGmailAddress.setText(gmail);
        }
        String redirect = (String) PropertiesUI.getInstance().getDefaultProps().get("SEARCH_REDIRECT");
        //boolean followRedirect = false;
        btnRedirect.setSelected(false);
        if (redirect != null && !redirect.equals("") && Integer.parseInt(redirect) > 0) {
            btnRedirect.setSelected(true);
            //followRedirect = true;
        }
        
        ArrayList blogEmails = (ArrayList)PropertiesUI.getInstance().getDefaultProps().get("BLOGGER_MAILS");
        if (blogEmails != null)
        { 
             DefaultListModel bloggerModel = (DefaultListModel)lstBloggerEmails.getModel();
             bloggerModel.clear();
            Iterator it = blogEmails.iterator();
            while(it.hasNext())
            {
                String email = (String)it.next();
                bloggerModel.addElement(email);
            }
        }
        btnRedirect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton radioButton = (JRadioButton) e.getSource();
                if (radioButton.isSelected()) {
                    PropertiesUI.getInstance().getDefaultProps().put("SEARCH_REDIRECT", "1");
                } else {
                    PropertiesUI.getInstance().getDefaultProps().put("SEARCH_REDIRECT", "0");
                }
                PropertiesUI.getInstance().saveProperties();

            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    //@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearchURL = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstSavedSearchEngines = new javax.swing.JList();
        btnAddToSearch = new javax.swing.JButton();
        btnDelSearch = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtSearchSyntax = new javax.swing.JTextField();
        btnRedirect = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtHtmlTagName = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSaveHtmlTags = new javax.swing.JList();
        btnAddToTags = new javax.swing.JButton();
        btnDelTags = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtGmailAddress = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstBloggerEmails = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        txtBloggerEmil = new javax.swing.JTextField();
        btnAddBlogEmail = new javax.swing.JButton();
        btnRemoveBlogEmail = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Search URL");

        txtSearchURL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSearchURLFocusLost(evt);
            }
        });

        lstSavedSearchEngines.setModel(new DefaultListModel<String>()
        );
        lstSavedSearchEngines.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstSavedSearchEngines.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstSavedSearchEnginesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstSavedSearchEngines);

        btnAddToSearch.setText("Add ");
        btnAddToSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToSearchActionPerformed(evt);
            }
        });

        btnDelSearch.setText("Delete");
        btnDelSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelSearchActionPerformed(evt);
            }
        });

        jLabel2.setText("Links syntax");

        btnRedirect.setText("Follow Redirect");
        btnRedirect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedirectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnRedirect)
                                .addContainerGap())
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnDelSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnAddToSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(33, 33, 33))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearchURL, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearchSyntax, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSearchURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSearchSyntax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAddToSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelSearch)
                        .addGap(18, 18, 18)
                        .addComponent(btnRedirect)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Search Engine", jPanel1);

        jLabel4.setText("HTML Tag to Parse");

        txtHtmlTagName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtHtmlTagNameMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtHtmlTagNameMouseReleased(evt);
            }
        });

        lstSaveHtmlTags.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(lstSaveHtmlTags);

        btnAddToTags.setText("Add ");
        btnAddToTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToTagsActionPerformed(evt);
            }
        });

        btnDelTags.setText("Delete");
        btnDelTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelTagsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtHtmlTagName, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnDelTags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddToTags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(33, 33, 33))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtHtmlTagName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAddToTags)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelTags)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("TagsParser", jPanel2);

        jLabel3.setText("GMail address");

        txtGmailAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGmailAddressFocusLost(evt);
            }
        });
        txtGmailAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGmailAddressActionPerformed(evt);
            }
        });

        lstBloggerEmails.setModel(new DefaultListModel());
        jScrollPane3.setViewportView(lstBloggerEmails);

        jLabel5.setText("Email for Blogger Blog");

        btnAddBlogEmail.setText("Add");
        btnAddBlogEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddBlogEmailActionPerformed(evt);
            }
        });

        btnRemoveBlogEmail.setText("REmove");
        btnRemoveBlogEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveBlogEmailActionPerformed(evt);
            }
        });

        jLabel7.setText("GMail password");

        txtPassword.setText("jPasswordField1");
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRemoveBlogEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddBlogEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGmailAddress)
                            .addComponent(txtBloggerEmil)
                            .addComponent(txtPassword))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtGmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtBloggerEmil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAddBlogEmail)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemoveBlogEmail))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Emails", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public Settings() {
        // this.lstSavedSearchEngines = lstSavedSearchEngines;
    }

    private void btnDelSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelSearchActionPerformed

        Hashtable prop = PropertiesUI.getInstance().getDefaultProps();
        ArrayList list = (ArrayList) prop.get("USER_SEARCH");

        Object value = ((JList) lstSavedSearchEngines).getSelectedValue();
        SearchObject o = (SearchObject) value;
        if (o.getSearchEngine().equals("http://www.google.com/search?q=")
                || o.getSearchEngine().equals("https://duckduckgo.com/html/?q=")
                || o.getSearchEngine().equals("https://www.bing.com/search?q=")) {
            JOptionPane.showMessageDialog(rootPane, "You can not delete system search engines");
            return;

        }
        ((DefaultListModel) ((JList) lstSavedSearchEngines).getModel()).removeElement(value);
        list.remove(value);
        PropertiesUI.getInstance().saveProperties();


    }//GEN-LAST:event_btnDelSearchActionPerformed

    private void btnDelTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelTagsActionPerformed
        Hashtable prop = PropertiesUI.getInstance().getDefaultProps();
        //  String type = (String) cboParseType.getSelectedItem();
        ArrayList list = (ArrayList) prop.get("USER_PARSER");
        String value = (String) ((JList) lstSaveHtmlTags).getSelectedValue();
        ((DefaultListModel) ((JList) lstSaveHtmlTags).getModel()).removeElement(value);
        list.remove(value);
        PropertiesUI.getInstance().saveProperties();
    }//GEN-LAST:event_btnDelTagsActionPerformed

    private void btnAddToTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToTagsActionPerformed

        Hashtable prop = PropertiesUI.getInstance().getDefaultProps();
        if (!txtHtmlTagName.getText().isEmpty()) {
            // String type = (String) cboParseType.getSelectedItem();
            ArrayList list = (ArrayList) prop.get("USER_PARSER");
            if (list == null) {
                list = new ArrayList();
            }
            if (!list.contains(txtHtmlTagName.getText())) {
                list.add(txtHtmlTagName.getText());
            }
            prop.put("USER_PARSER", list); // save list
            ((DefaultListModel) ((JList) lstSaveHtmlTags).getModel()).addElement(txtHtmlTagName.getText());
            PropertiesUI.getInstance().saveProperties();
        }
    }//GEN-LAST:event_btnAddToTagsActionPerformed

    private void btnAddToSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToSearchActionPerformed
        Hashtable prop = PropertiesUI.getInstance().getDefaultProps();
        if (!txtSearchURL.getText().isEmpty()) {

            ArrayList list = (ArrayList) prop.get("USER_SEARCH");

            if (list == null) {
                list = new ArrayList();
            }
            Iterator loop = list.iterator();
            boolean hasFound = false;
            while (loop.hasNext()) {
                if (loop.next().toString().equals(txtSearchURL.getText())) {
                    hasFound = true;
                    break;
                }
            }
            if (!hasFound) {
                SearchObject s = new SearchObject();
                s.setSearchEngine(txtSearchURL.getText());
                s.setLinksRegex(txtSearchSyntax.getText());
                list.add(s);
            }
            prop.put("USER_SEARCH", list); // save list
            ((DefaultListModel) ((JList) lstSavedSearchEngines).getModel()).addElement(txtSearchURL.getText());
            PropertiesUI.getInstance().saveProperties();
        }
    }//GEN-LAST:event_btnAddToSearchActionPerformed

    private void lstSavedSearchEnginesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstSavedSearchEnginesValueChanged
        SearchObject o = (SearchObject) ((JList) evt.getSource()).getSelectedValue();
        txtSearchURL.setText(o.getSearchEngine());
        txtSearchSyntax.setText(o.getLinksRegex());
    }//GEN-LAST:event_lstSavedSearchEnginesValueChanged

    private void txtHtmlTagNameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHtmlTagNameMousePressed
        ShowPopup(evt);
    }//GEN-LAST:event_txtHtmlTagNameMousePressed

    private void txtHtmlTagNameMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHtmlTagNameMouseReleased
        ShowPopup(evt);
    }//GEN-LAST:event_txtHtmlTagNameMouseReleased

    private void btnRedirectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedirectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRedirectActionPerformed

    private void btnAddBlogEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddBlogEmailActionPerformed
        
         if (!txtBloggerEmil.getText().equals("")) {
            if (!txtBloggerEmil.getText().matches(ArticleManagmentMain.EMAIL_PATTERN)) {
                JOptionPane.showMessageDialog(this, "Invalid Blogger Email ...", "Email Validation", JOptionPane.WARNING_MESSAGE);
            } else {
                  ArrayList blogEmails = (ArrayList)PropertiesUI.getInstance().getDefaultProps().get("BLOGGER_MAILS");
                  if (blogEmails == null)
                  {    
                      blogEmails = new ArrayList();
                  }
                  blogEmails.add(txtBloggerEmil.getText());
                  PropertiesUI.getInstance().getDefaultProps().put("BLOGGER_MAILS", blogEmails);
                 PropertiesUI.getInstance().saveProperties();
                ((DefaultListModel)lstBloggerEmails.getModel()).addElement(txtBloggerEmil.getText());
            }
        }
    }//GEN-LAST:event_btnAddBlogEmailActionPerformed

    private void btnRemoveBlogEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveBlogEmailActionPerformed
            ArrayList blogEmails = (ArrayList)PropertiesUI.getInstance().getDefaultProps().get("BLOGGER_MAILS");
             if (blogEmails == null)
                 return;
            DefaultListModel l = (DefaultListModel)lstBloggerEmails.getModel();
            int[] indx = lstBloggerEmails.getSelectedIndices();
            for (int i = 0;i < indx.length;i++)
            {
              blogEmails.remove(l.get(i));
              l.removeElement(l.get(i));
            }
            if (indx.length > 0)
                 PropertiesUI.getInstance().saveProperties();
           
    }//GEN-LAST:event_btnRemoveBlogEmailActionPerformed

    private void txtGmailAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGmailAddressActionPerformed

    }//GEN-LAST:event_txtGmailAddressActionPerformed

    private void txtSearchURLFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchURLFocusLost


    }//GEN-LAST:event_txtSearchURLFocusLost

    private void txtGmailAddressFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGmailAddressFocusLost
        if (!txtGmailAddress.getText().equals("")) {
            if (!txtGmailAddress.getText().matches(ArticleManagmentMain.EMAIL_PATTERN)) {
                JOptionPane.showMessageDialog(this, "Invalid Email ...", "Email Validation", JOptionPane.WARNING_MESSAGE);
            } else {
                PropertiesUI.getInstance().getDefaultProps().put("USER_GMAIL", txtGmailAddress.getText());
                PropertiesUI.getInstance().saveProperties();
            }
        }
    }//GEN-LAST:event_txtGmailAddressFocusLost

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
        String pswd =  new String(txtPassword.getPassword());
        if (!pswd.equals("")) {
                String encrPassw =  ArticleManagmentMain.encrypt(pswd);
                PropertiesUI.getInstance().getDefaultProps().put("USER_GMAIL_PSWD", encrPassw);
                PropertiesUI.getInstance().saveProperties();
        }
    }//GEN-LAST:event_txtPasswordFocusLost
    private void ShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popupText.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Settings dialog = new Settings(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddBlogEmail;
    private javax.swing.JButton btnAddToSearch;
    private javax.swing.JButton btnAddToTags;
    private javax.swing.JButton btnDelSearch;
    private javax.swing.JButton btnDelTags;
    private javax.swing.JRadioButton btnRedirect;
    private javax.swing.JButton btnRemoveBlogEmail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList<String> lstBloggerEmails;
    private javax.swing.JList<String> lstSaveHtmlTags;
    private javax.swing.JList<String> lstSavedSearchEngines;
    private javax.swing.JTextField txtBloggerEmil;
    private javax.swing.JTextField txtGmailAddress;
    private javax.swing.JTextField txtHtmlTagName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSearchSyntax;
    private javax.swing.JTextField txtSearchURL;
    // End of variables declaration//GEN-END:variables
}
