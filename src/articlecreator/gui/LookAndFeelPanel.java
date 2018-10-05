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

import articlecreator.gui.components.ui.PropertiesUI;
import java.awt.*;
import java.awt.event.*;
import java.util.*;     
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import articlecreator.gui.dl.*;

// Finalized on 12/01/2002
// Author: Samuel Huang

class LookAndFeelPanel extends JPanel {

  
  private JRadioButton javaRadio, systemRadio, mortifRadio;
  //private MyEditor myEditor;
  private Hashtable defaultProps;
 
  public LookAndFeelPanel(  )
  {
	   
	   // this.myEditor = myEditor;
       defaultProps = PropertiesUI.getInstance().getDefaultProps();// myEditor.getDefaultProps();
	    setLayout(new BorderLayout() );

       javaRadio = new JRadioButton();
       systemRadio = new JRadioButton();
       mortifRadio = new JRadioButton();
       initSetting();
       
       JPanel p0 = new JPanel();
       p0.setLayout(new BoxLayout( p0, BoxLayout.Y_AXIS));
       p0.add( new JLabel("Default Look And Feel") );
       p0.add( Box.createRigidArea( new Dimension(0,10) ) );
		 p0.add( new JSeparator() );
		 p0.add( Box.createRigidArea( new Dimension(0,10) ) );
       
       //----------------------------------------

	    JPanel p1 = new JPanel( new DialogLayout(5, 5) );   
       p1.add( javaRadio );
       p1.add( new JLabel("Java" ) );
       p1.add( systemRadio );
       p1.add( new JLabel("System" ) );
       p1.add( mortifRadio );
       p1.add( new JLabel("Mortif" ) );

       ButtonGroup bg = new ButtonGroup();
		 bg.add( javaRadio );
		 bg.add( systemRadio );
		 bg.add( mortifRadio );

		 //----------------------------------------

		 add( p0, BorderLayout.NORTH );	
		 add( p1, BorderLayout.CENTER );	
		 
  }
  
  public void applySetting( ) {
    if ( javaRadio.isSelected() )
  	    defaultProps.put( "LOOK&FEEL", "java" );
    else if ( mortifRadio.isSelected() )
  	    defaultProps.put( "LOOK&FEEL", "mortif" );
    else if ( systemRadio.isSelected() )
  	    defaultProps.put( "LOOK&FEEL", "system" );
  }

  private void initSetting() {
     String defaultLook = (String)defaultProps.get("LOOK&FEEL");
     if ( defaultLook.equals( "java" ) )
         javaRadio.setSelected( true );
     else if ( defaultLook.equals( "system" ))
         systemRadio.setSelected( true );
     else if ( defaultLook.equals( "mortif" ))
         mortifRadio.setSelected( true );
  }
 
}

