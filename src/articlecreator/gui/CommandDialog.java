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

// Courtesy of authors of the book "Swing".
//
// Last Modified: 3/07/2002, 12:42 PM
// Author: Samuel Huang, 23/12/2001

import java.awt.*;
import java.awt.event.*;
import java.util.*;     
import javax.swing.*;	
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.io.File;
import articlecreator.gui.dl.*;
import articlecreator.gui.run.ArticleManagmentMain;
	
class CommandDialog extends JDialog {

  //private MyEditor editor;
  private ArticleManagmentMain editor;
    private JTextField commandField;
  private JTextField directoryField;
  private JButton okButton;
  private JButton cancelButton;
  private JButton browseButton;
  private String fileName;
  private JPopupMenu popup;
  private Action cut, copy, paste, selectAll;
  private DeleteAction delete;

  public CommandDialog( ArticleManagmentMain editor )
  {
	     super( editor, "Command Dialog", true); // true -> modal dialog
        this.editor = editor;
   
        EmptyBorder emptyBorder = new EmptyBorder(10, 10, 10, 10); // Top, left, bottom, right

		  KeyListener enterListener = new EnterKeyListener();
		  commandField = new JTextField();
		  commandField.addKeyListener ( enterListener  );
		  commandField.setDocument( new RemoveLineDocument() );
		  directoryField = new JTextField();
		  directoryField.addKeyListener ( enterListener );
		  directoryField.setDocument( new RemoveLineDocument() );

        JPanel p1 = new JPanel( new DialogLayout(10, 5) );
        p1.setBorder( emptyBorder ); 
        p1.add(new JLabel("Command:"));
        p1.add( commandField );
	     p1.add(new JLabel("Directory:"));
        p1.add( directoryField );
 		  p1.setPreferredSize( new Dimension( 400, 100 ) );
 		  
        okButton = new JButton("OK");
        okButton.addActionListener( okAction );
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener( cancelAction );
        
        browseButton = new JButton("Browse");
        browseButton.addActionListener( browseAction );
        
        JPanel p2 = new JPanel( new GridLayout(1, 3, 10, 5) );
        p2.setBorder( emptyBorder );
        p2.add( okButton );
        p2.add( cancelButton );
        p2.add( browseButton );
 
 		  getRootPane().setDefaultButton( okButton );
     	  getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( p1, BorderLayout.NORTH );
        getContentPane().add( p2, BorderLayout.SOUTH );
        setResizable(false);

  }
  
  ActionListener cancelAction = new ActionListener() { 
      public void actionPerformed(ActionEvent e) {
         setVisible(false);
      }
  };
  
  ActionListener okAction = new ActionListener() { 
      public void actionPerformed(ActionEvent e) {
      	
    //  	ArrayList list = editor.outputArrayList();
	//	   int index = editor.getConsolesList().getSelectedIndex(); 
		   String command = commandField.getText();
		   String dirPath = directoryField.getText();
		   
		   if ( command.length() == 0 || dirPath.length() == 0 ) {
		   	String message = "Command or Directory field is empty !";  
		   	JOptionPane.showMessageDialog( CommandDialog.this, message, "Warning", 
		   		JOptionPane.WARNING_MESSAGE );
      		return;
		   }
		   
        //	Run run = new Run( command, dirPath, fileName, list, 4, index, editor );
	     // run.start();
	     // editor.getStopAction().setEnabled( true );
	     	
         setVisible(false);
         
      }
  };
  
  ActionListener browseAction = new ActionListener() { 
      public void actionPerformed(ActionEvent e) {

        /*
         JFileChooser chooser = editor.getChooser();
         while ( true ) {  // To keep chooser open if open a directory, then press open button.
	         chooser.rescanCurrentDirectory();
	         int returnVal = chooser.showOpenDialog( CommandDialog.this );  	
		      if ( returnVal == JFileChooser.CANCEL_OPTION || returnVal == -1 )
		      	return;
				File file = chooser.getSelectedFile();
	         if ( returnVal == JFileChooser.APPROVE_OPTION && file != null && file.isFile() ) {             
	            String dirName = file.getParent(); 
	            fileName = file.getName();
	            fileName = fileName.substring( 0, fileName.length() - 5 );
	         	commandField.setText( "java " + fileName );
	         	directoryField.setText( dirName );
	         	editor.openFile( file );
					return;
	         }  
	      }  // End while
	 */        
      }
  };  
  
  public JTextField getCommandField() {
  	  return commandField;
  }
  
  public JTextField getDirectoryField() {
  	  return directoryField;
  }
  
  public void setFileName( String name ) {
  	  this.fileName = name;
  }
  
  private void createPopup() {

	  popup = new JPopupMenu();

	 /*
         cut = editor.getAction(DefaultEditorKit.cutAction);
 	  copy = editor.getAction(DefaultEditorKit.copyAction);
	  paste = editor.getAction(DefaultEditorKit.pasteAction);
	  delete = new DeleteAction();
	  selectAll = editor.getAction(DefaultEditorKit.selectAllAction);
	 */ 
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

  class EnterKeyListener extends KeyAdapter {	
	   public void keyPressed(KeyEvent e) {
		  int keyCode = e.getKeyCode();
		  if (keyCode == KeyEvent.VK_ENTER )	{
			  Object obj = e.getSource();
			  if ( obj instanceof JTextField ) 
				  okButton.doClick();
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
  
  public void show() {
  	 super.show();
  	 commandField.requestFocus();
  }
  
}

