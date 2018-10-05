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

// Author: Samuel Huang, 12/1/2002

import articlecreator.gui.components.ui.InnerFramesUI;
import articlecreator.gui.components.ui.PropertiesUI;
import java.awt.*;
import java.awt.event.*;
import java.util.*;     
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import articlecreator.gui.dl.*;
import articlecreator.gui.run.ArticleManagmentMain;

class TextFormatPanel extends JPanel {

  private int m_option = JOptionPane.CLOSED_OPTION;
  private JTextField tabField, wrapColumnField;
  private JRadioButton softTabRadio, hardTabRadio,
							  currentRadio, defaultRadio;
 // private MyEditor myEditor;
   private ArticleManagmentMain myEditor;
  private Hashtable defaultProps;
  private EmptyBorder emptyBorder;
  private JLabel tabLabel, tabSizeLabel;
				
  public TextFormatPanel( ArticleManagmentMain myEditor )
  {
	    this.myEditor = myEditor;
	    this.defaultProps = PropertiesUI.getInstance().getDefaultProps();// myEditor.getDefaultProps();
       setLayout(new BoxLayout( this, BoxLayout.Y_AXIS));

       softTabRadio = new JRadioButton();
       hardTabRadio  = new JRadioButton();
       currentRadio = new JRadioButton();
       defaultRadio = new JRadioButton();
       emptyBorder = new EmptyBorder(0, 7, 4, 7 );
       
       tabField = new JTextField();
       try {
          tabField.setColumns(5);
       }
       catch ( IllegalArgumentException iae ) {}
       
       // ---------------------------------------

	    JPanel p1 = new JPanel( new BorderLayout() ); 
	    p1.setBorder( new CompoundBorder( new TitledBorder( new EtchedBorder(),
	    	 "Tabbing" ), emptyBorder ) );

	    JPanel p1_1 = new JPanel( new DialogLayout(5, 5) ); 
       p1_1.add( hardTabRadio );
       p1_1.add( new JLabel("Hard Tabs: Tabbing with TAB characters" ) );
       p1_1.add( softTabRadio );
       p1_1.add( new JLabel("Soft Tabs: Tabbing with SPACE characters" ) );
       
       ButtonGroup bg = new ButtonGroup();
		 bg.add( hardTabRadio );
		 bg.add( softTabRadio );
       
       JPanel p1_2 = new JPanel( new BorderLayout() );
       JPanel p1_3 = new JPanel( );
       p1_3.add( new JLabel("Tabbing size: ") );
       p1_3.add( tabField ); 
       p1_2.add( p1_3, BorderLayout.WEST );
 		 p1.add( p1_1, BorderLayout.EAST );
 		 p1.add( p1_2, BorderLayout.SOUTH );

		 //----------------------------------------

       JPanel p3 = new JPanel( new DialogLayout(5, 5) ); 
	    p3.setBorder( new CompoundBorder( new TitledBorder( new EtchedBorder(),
	    	 "Apply To" ), emptyBorder ) );

       p3.add( currentRadio );
       p3.add( new JLabel("Current Editor" ) );
       p3.add( defaultRadio );
       p3.add( new JLabel("Default Editor" ) );
       
       bg = new ButtonGroup();
		 bg.add( currentRadio );
		 bg.add( defaultRadio );

 		 //----------------------------------------
 		 
 		 JPanel p4 = new JPanel( new DialogLayout(5, 5) );  
	    p4.setBorder( new CompoundBorder( new TitledBorder( new EtchedBorder(),
	    	 "Default Setting" ), emptyBorder ) );

	    tabLabel = new JLabel(" ");
	    tabSizeLabel = new JLabel(" ");

	    p4.add( new JLabel("Tab: ") );
	    p4.add( tabLabel );
	    p4.add( new JLabel("Tab Size: ") );
	    p4.add( tabSizeLabel );

		 //----------------------------------------

		 JLabel heading = new JLabel( "Text Formatting" );
		 heading.setAlignmentX(Component.CENTER_ALIGNMENT);
		 
		 add( heading );
		 add( Box.createRigidArea( new Dimension(0,5) ) );
		 add( new JSeparator() );
		 add( Box.createRigidArea( new Dimension(0,5) ) );

       add( p1 );
       add( p3 );
       add( p4 );

       initSetting();

  }
  
  private void initSetting() {
  	  currentRadio.setSelected( true );
  	  String tab = (String)defaultProps.get("TAB");
  	  if ( tab.equals("HARD") )
  	  	  tabLabel.setText( "Hard Tabbing" );
  	  else
  	  	  tabLabel.setText( "Soft Tabbing" );
  	  Integer intObj = (Integer)defaultProps.get("DEFAULT_TAB_SIZE");
  	  tabSizeLabel.setText( intObj.toString() );
  }

  public void applySetting( ) {
  	  if ( softTabRadio.isSelected() ) {
  	  	  softTab();
  	  }
  	  if ( hardTabRadio.isSelected() ) {
  	  	  hardTab();
  	  }
  	  if ( !tabField.getText().equals("") )
  	  	  tabSize();	  
  }
  
  public void tabSize() {
  	  int tabSize = 0;
     try {
   	 tabSize = Integer.parseInt( tabField.getText() );
   	 if ( tabSize < 1 ) 
   	 	 throw new NumberFormatException();
     }
     catch ( NumberFormatException nfe ) {
    	  invalidInput("Invalid input in Tabbing Size Field !");
    	  return;
     }
     if ( defaultRadio.isSelected() ) {
  	  		tabSizeLabel.setText( tabField.getText() );
  			defaultProps.put("DEFAULT_TAB_SIZE", new Integer(tabSize) );
  	  }
  	  else {
  	  	   
  	  	   MyInternalFrame jif = InnerFramesUI.getInstance().getSelectedFrame();// myEditor.getSelectedFrame();
  			if ( jif != null ) {
  				JTextArea textArea = jif.getJTextArea();
  				textArea.setTabSize( tabSize );
  				textArea.setUI( textArea.getUI() ); // Fix JTextArea.setTabSize() bug
  	  		}
  	  		
  	  }
  }
  
  public void softTab() {
  	  if ( defaultRadio.isSelected() ) {
  	  		tabLabel.setText("Soft Tabbing");
  			defaultProps.put("TAB","SOFT");
  	  }
  	  else {
  		   MyInternalFrame jif = InnerFramesUI.getInstance().getSelectedFrame();// myEditor.getSelectedFrame();
  			if ( jif != null ) {
  				MyTabKey tabListener = new MyTabKey();
  				jif.setTabKeyListener( tabListener );
  				JTextArea textArea = jif.getJTextArea();
  				textArea.addKeyListener( tabListener );
  			}
  	  }	
  }
  
  public void hardTab() {
  	  if ( defaultRadio.isSelected() ) {
  	  		tabLabel.setText("Hard Tabbing");
  			defaultProps.put("TAB","HARD");
  	  }
  	  else {
  			MyInternalFrame jif = InnerFramesUI.getInstance().getSelectedFrame();// myEditor.getSelectedFrame();
  			if ( jif != null ) {
  				MyTabKey tabListener = (MyTabKey)jif.getTabKeyListener();
  				JTextArea textArea = jif.getJTextArea();
  				textArea.removeKeyListener( tabListener );
  			}
     }		 
  }
 
  public void invalidInput( String str ) {
	  JOptionPane.showMessageDialog(
			TextFormatPanel.this,  // parentComponent
		   str,
		   "Warning",  // title
		   JOptionPane.ERROR_MESSAGE  // optionType
	  );
  }
 
}

