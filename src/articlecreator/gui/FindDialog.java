package articlecreator.gui;

 /** 
 *  Copyright 1999-2002 Matthew Robinson and Pavel Vorobiev. 
 *  All Rights Reserved. 
 * 
 *  =================================================== 
 *  This program contains code from the book "Swing" 
 *  1st Edition by Matthew Robinson and Pavel Vorobiev 
 *  http://www.spindoczine.com/sbe 
 *  =================================================== 
 * 
 *  The above paragraph must be included in full, unmodified 
 *  and completely intact in the beginning of any source code 
 *  file that references, copies or uses (in any way, shape 
 *  or form) code contained in this file. 
 */

// Courtesy of authors of the book "Swing": GUI layout, find and replace text 
// features are adapted from chapter 20 of the excellent book "Swing", 1st 
// edition. You can download this chapter from their website for code
// walkthrough of features mentioned above.
//
// Last Modified: 29/01/2002, 10:55 PM
// Author: Samuel Huang, 23/04/2001

import articlecreator.gui.dl.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.util.*;
import javax.swing.plaf.basic.*;

class FindDialog extends JDialog
{
  protected JComboBox findCombo, replaceCombo;
  protected final MyEditor editor;
  protected int m_searchIndex = -1;
  protected boolean m_searchUp = false;
  protected String  m_searchData = null;
  protected JCheckBox chkCase;
  protected JRadioButton rdDown, rdUp; 
  protected JButton btFind, btReplace, btReplaceAll, btClose;
  protected JPopupMenu popup;
  protected Action cut, copy, paste, selectAll;
  protected DeleteAction delete;
  protected Border lineBorder, emptyBorder;
  protected JTextField findTextField, replaceTextField;

  public FindDialog( MyEditor editor) {
  
     super(editor, "Find and Replace", false);
     this.editor = editor;
     
     lineBorder = new LineBorder( Color.black.brighter() );
     emptyBorder = new EmptyBorder(0,0,0,0);
 
     // Initialize CheckBoxes
     chkCase = new JCheckBox("Match case");
     chkCase.setSelected( false );
   
	  // Initialize Radiobuttons
     rdDown = new JRadioButton("Search down", true);
     rdUp = new JRadioButton("Search up");
     
     // Set combo box 'findCombo'
     findCombo = new JComboBox();
     findCombo.setModel( new MyComboBoxModel() );
     findCombo.setEditable( true );

     // Set combo box 'replaceCombo'
 	  replaceCombo = new JComboBox();
 	  replaceCombo.setModel( new MyComboBoxModel() );
     replaceCombo.setEditable( true );
   
     createPopup();
			 
     // Code "Find Next" button and its action
     ActionListener findAction = new ActionListener() { 
        public void actionPerformed(ActionEvent e) {
        	  int r =findNext(false, true);
           if ( r == -1 )  // Error occured
           	 return;
        	  findCombo.addItem( findTextField.getText() );
        	  btFind.requestFocus();
        }
     };
     btFind = new JButton("Find Next");
     btFind.addActionListener( findAction );
     
     // Code "Replace" button and its action
     ActionListener replaceAction = new ActionListener() { 
        public void actionPerformed(ActionEvent e) {
        	  int r = findNext(true, true);
           if ( r == -1 )  // Error occured
           	 return;
        	  replaceCombo.addItem( replaceTextField.getText() ); 
        	  btReplace.requestFocus();
        }
     };
     btReplace = new JButton("Replace");
     btReplace.addActionListener(replaceAction);
     
     // Code "Close" button and its action
     ActionListener closeAction = new ActionListener() { 
        public void actionPerformed(ActionEvent e) {
           setVisible(false);
           findCombo.setSelectedItem("");
           replaceCombo.setSelectedItem("");
        }
     };
     btClose = new JButton("Close");
     btClose.addActionListener(closeAction);

	  // Code "Replace All" button and its action
     ActionListener replaceAllAction = new ActionListener() { 
        public void actionPerformed(ActionEvent e) {
           int counter = 0;
           while (true) {
              int result = findNext(true, false);
              if (result < 0)          // error
                 return;
              else if (result == 0)    // no more
                 break;
              counter++;
           }
           replaceCombo.addItem( replaceCombo.getSelectedItem() );
           JOptionPane.showMessageDialog( FindDialog.this, 
              counter + " replacement(s) have been done", "Info",
              JOptionPane.INFORMATION_MESSAGE);
           requestFocus();
        }
           
    };
    btReplaceAll = new JButton("Replace All");
    btReplaceAll.addActionListener(replaceAllAction);

    // Set button group bg
    ButtonGroup bg = new ButtonGroup();
    bg.add(rdUp);
    bg.add(rdDown);
    
    update();
   
    //*******************
 	 //    GUI layout 
    //*******************
    
	 // Layout pc panel
    JPanel pc = new JPanel();
    pc.setLayout(new DialogLayout(10, 5));
    pc.setBorder(new EmptyBorder(8, 5, 8, 0));     
    pc.add(new JLabel("Find what:"));
    pc.add(findCombo);
    pc.add(new JLabel("Replace:"));
    pc.add(replaceCombo);
  
    // Layout po panel
    JPanel po = new JPanel(new GridLayout(2, 2, 8, 2));
    po.setBorder(new TitledBorder(new EtchedBorder(), "Options"));
    po.add(rdUp);  
    po.add(chkCase);
    po.add(rdDown);
 
    // Layout pc1 panel
    JPanel pc1 = new JPanel(new BorderLayout());
	 pc1.add(pc, BorderLayout.CENTER);
    pc1.add(po, BorderLayout.SOUTH);
    pc1.setPreferredSize( new Dimension( 250, 170 ) );
    
    // Layout p panel 
    JPanel p = new JPanel(new GridLayout(5, 1, 2, 4));
    p.add( btFind );
    p.add( btReplace );
    p.add( btReplaceAll );
    p.add( btClose );
    p.add( new JPanel() );
  
    // Layout po1 panel
    JPanel p01 = new JPanel(new FlowLayout());
    p01.add(p);    
    
    // Layout p1 panel
    JPanel p1 = new JPanel(new BorderLayout());
    p1.add(pc1, BorderLayout.CENTER);
    p1.add(p01, BorderLayout.EAST);
   
    getContentPane().add( p1, BorderLayout.CENTER );
    setResizable(false);
  
  }

  public int findNext(boolean doReplace, boolean showWarnings) {
  
  	 m_searchUp = rdUp.isSelected();
    String key = "";
    
    key = ((JTextField)findCombo.getEditor().getEditorComponent()).getText();
    if ( key == null || key.length() == 0 ) {
      warning("Please enter the target to search");
      return -1;
    }

    // Translate "\\n"(appears as  string "\n" in combo box) into new line
    // char '\n'. StringTokenizer doesn't work here.
    String str = "";
    int ind = 0;
    while ( true ) {
         int start = ind;
         ind = key.indexOf("\\n",ind);
         int end = ind;
         if ( ind == - 1 ) {
            str = str + key.substring( start );
            break;
         }
         ind = ind + 2;
         if ( ind > key.length() )
            break;
         str = str + key.substring( start, end ) + "\n";
    }
    key = str;

    MyInternalFrame frame = editor.getSelectedFrame();
    JTextArea textArea = frame.getJTextArea();
    int pos = textArea.getCaretPosition();
    
    String replacement = null;
    String selectedText = null;
    
    if (doReplace) {
      replacement = (String)replaceCombo.getSelectedItem();
      if ( replacement == null || replacement.length() == 0 ) {
        warning("Please enter the replacement");
        return -1;
      }
      selectedText = textArea.getSelectedText();
      if ( selectedText != null ) {
	      if ( !replacement.equals( selectedText ) ) {
		     textArea.replaceSelection( replacement );
		     setSelection( textArea, textArea.getSelectionStart(),
		      				 textArea.getSelectionEnd(), m_searchUp );
		     doReplace = false;
	        showWarnings = false;
	      }
	   }
    }

    try {
        Document doc = textArea.getDocument();
        if (m_searchUp)
          m_searchData = doc.getText(0, pos);
        else
          m_searchData = doc.getText(pos, doc.getLength()-pos);
        m_searchIndex = pos;
    }
    catch (BadLocationException ex) {
        ex.printStackTrace();
        return -1;
    }
    
    if (!chkCase.isSelected()) {
      m_searchData = m_searchData.toLowerCase();
      key = key.toLowerCase();
    }

    int xStart = -1;
    int xFinish = -1;
 
    if (m_searchUp)
      xStart = m_searchData.lastIndexOf(key, pos-1);
    else 
      xStart = m_searchData.indexOf(key, pos-m_searchIndex);
	
	 if (xStart < 0) {   
	   if (showWarnings)       
	     warning("Text not found");
	   return 0;
	 }
    xFinish = xStart+key.length();
       
    if (!m_searchUp) {
      xStart += m_searchIndex;
      xFinish += m_searchIndex;
    }

    if (doReplace) {
      setSelection(textArea, xStart, xFinish, m_searchUp);
      textArea.replaceSelection(replacement);
      setSelection(textArea, xStart, xStart+replacement.length(), m_searchUp); 
    }
    else
      setSelection(textArea, xStart, xFinish, m_searchUp);

    return 1;
    
  }

  protected void warning(String message) {
  	 // Bug alert: If the first argument of showMessageDialog is null,
  	 // application freezes after the option pane appears, then losing
  	 // and regaining focus of application.
    JOptionPane.showMessageDialog( this, 
      message, "Warning", JOptionPane.WARNING_MESSAGE );
  }
  
  public void setSelection( JTextArea textArea, int xStart, int xFinish, boolean moveUp ) {
    if (moveUp) {
      textArea.setCaretPosition(xFinish);
      textArea.moveCaretPosition(xStart);
    }
    else
      textArea.select(xStart, xFinish);   
  }
  
  public void setEditItem( String str  ) {
  	  if ( !str.equals("") )
		  findTextField.setText( str );
	  else if ( findCombo.getModel().getSize() != 0 )
	  	  findCombo.setSelectedIndex( 0 );
  	  findTextField.selectAll();
  }

  private void createPopup() {

	  popup = new JPopupMenu();

	  cut = editor.getAction(DefaultEditorKit.cutAction);
	  copy = editor.getAction(DefaultEditorKit.copyAction);
	  paste = editor.getAction(DefaultEditorKit.pasteAction);
	  delete = new DeleteAction();
	  selectAll = editor.getAction(DefaultEditorKit.selectAllAction);

	  JMenuItem menuItem = new JMenuItem();
     menuItem.setAction( cut );
     menuItem.setText("Cut");
     menuItem.setIcon(new ImageIcon( "images/Cut24.gif" ));
	  popup.add(menuItem);
     
     menuItem = new JMenuItem();
     menuItem.setAction( copy );
     menuItem.setText("Copy");
     menuItem.setIcon(new ImageIcon( "images/Copy24.gif" ));
	  popup.add(menuItem);
     
     menuItem = new JMenuItem();
     menuItem.setAction( paste );
     menuItem.setText("Paste");
     menuItem.setIcon(new ImageIcon( "images/Paste24.gif" ));
	  popup.add(menuItem);
	  
	  menuItem = new JMenuItem();
     menuItem.setAction( delete );
     menuItem.setIcon(new ImageIcon( "images/Delete24.gif" ));
	  popup.add(menuItem);
	  
	  popup.add( new JSeparator() );
	  
	  menuItem = new JMenuItem();
     menuItem.setAction( selectAll );
     menuItem.setText("Select All");
	  popup.add(menuItem);

  }
  
  // Note listeners are added to editor of JComboBox because 
  // listeners added to JCombobox does nothing.
  //
  public void update() { 
     findCombo.setModel( new MyComboBoxModel() );
     replaceCombo.setModel( new MyComboBoxModel() );
     findTextField = (JTextField)findCombo.getEditor().getEditorComponent();
	  findTextField.addKeyListener( new EnterKeyListener() );
	  findTextField.addMouseListener( new PopupListener() );
     replaceTextField = (JTextField)replaceCombo.getEditor().getEditorComponent();
	  replaceTextField.addKeyListener( new EnterKeyListener() );
	  replaceTextField.addMouseListener( new PopupListener() );
	  btFind.addKeyListener( new EnterKeyListener() );
	  btReplace.addKeyListener( new EnterKeyListener() );
	  btClose.addKeyListener( new EnterKeyListener() );
	  btReplaceAll.addKeyListener( new EnterKeyListener() );
  }
  
  public void show() {
  	 super.show();
  	 SwingUtilities.invokeLater( new Runnable() {
	    public void run() {
	    	 findTextField.requestFocus();	       	 
	    }
	 });	
  }
 
  class PopupListener extends MouseAdapter {
     public void mousePressed(MouseEvent e) {
        ShowPopup(e);
     }
     public void mouseReleased(MouseEvent e) {
        ShowPopup(e);
     }
     private void ShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
			  JComboBox combo = (JComboBox)e.getComponent().getParent();
			  combo.requestFocus(); 
			  JTextField jtf = (JTextField)combo.getEditor().getEditorComponent();
			  String text = jtf.getText();
			  String selectedText = jtf.getSelectedText();
			  if ( selectedText == null ) {
				 cut.setEnabled( false );
				 copy.setEnabled( false );
				 delete.setEnabled( false );
			  }
			  else {
				 cut.setEnabled( true );
				 copy.setEnabled( true );
				 delete.setTextField( jtf );
				 delete.setEnabled( true );
			  }
			  if ( text.length() == 0 )
			  	 selectAll.setEnabled( false );
			  else
			  	 selectAll.setEnabled( true );
           popup.show(e.getComponent(), e.getX(), e.getY());
        }
     }
  }

  class EnterKeyListener extends KeyAdapter {	
	  public void keyPressed(KeyEvent e) {
		  int keyCode = e.getKeyCode();
		  if ( keyCode == KeyEvent.VK_ENTER )	{
			  if ( findTextField.hasFocus() || btFind.hasFocus() )
				  btFind.doClick();
			  else if ( replaceTextField.hasFocus() || btReplace.hasFocus() )
				  btReplace.doClick();
			  else if ( btReplaceAll.hasFocus() )
			  	  btReplaceAll.doClick();
			  else if ( btClose.hasFocus() )
			  	  btClose.doClick(); 
		  }
	  }
  }  // End EnterKeyListener 
  
  class DeleteAction extends AbstractAction {
  		JTextField jtf;
  		DeleteAction() {
  			super("Delete");
  		}
 	   public void actionPerformed(ActionEvent e) {
 	   	jtf.replaceSelection("");
 	   }
 	   public void setTextField( JTextField jtf ) {
 	   	this.jtf = jtf;
 	   }
  }
  
  class FindComboListener extends FocusAdapter {
  	  public void focusGained(FocusEvent e) {
		  getRootPane().setDefaultButton( btFind );
	  }
  }
  
  class ReplaceComboListener extends FocusAdapter {
  	  public void focusGained(FocusEvent e) {
		  getRootPane().setDefaultButton( btReplace );
	  }
  }
  
}  // End findDialog class


/**
*  A combobox model that adds non-repeat object to index 0 of the model. Up to 9
*  objects can be stored in a FIFO fashion.
*/
class MyComboBoxModel extends DefaultComboBoxModel {
  
	  private int count = 0;
	  private int limit = 9;
	  
	  public void addElement(Object anObject) {	// Called by JComboBox.addItem()
	  	  if ( anObject == null || anObject.equals("") )
	  	  	    return;
	  	  insertElementAt( anObject, 0 );
	  }

	  public void insertElementAt(Object anObject, int index) {	  
	  	  String entry = (String) anObject;
	  	  if ( count < limit ) {
	  	  	  // Check for repeat entry to comboBox
	  		  for ( int i = 0; i < getSize(); i++ ) {	 
	  				String item = (String)getElementAt(i);
	  				if ( item.equals( entry ) ) {
	  					removeElementAt( i );
						break;	  				
	  				}
	  			}
	  	  		super.insertElementAt( anObject, 0 );
	  	  		setSelectedItem( anObject );
	  	  		limit++;
  	  	  }
  	  	  else {
	  			removeElementAt( limit );
	  			super.insertElementAt( anObject, 0 );
	  	  }
	  }  //  End insertElement

}	//  End MyComboBoxModel


