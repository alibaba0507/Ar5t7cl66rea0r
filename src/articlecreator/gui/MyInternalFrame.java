package articlecreator.gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.KeyListener;
import javax.swing.text.*;
import java.io.File;

// Author: Samuel Huang, 12/01/2002

public class MyInternalFrame extends JInternalFrame {

   private int lineNumber = 1;
   private JScrollPane jsp = null;
				   
	private UndoManager undo;
	private JTextArea textArea, lineArea;
	private File file;
	private long lastModified;
	private KeyListener tabKeyListener = null; 
	
	public MyInternalFrame( String title, boolean a, boolean b, boolean c, boolean d) {
		super( title, a, b, c, d );
		lineArea = new JTextArea();
		lineArea.setEditable( false );
		lineArea.setBackground(Color.lightGray);
	}
	
	public void setUndoManager( UndoManager undo ) {
		this.undo = undo;
	}
	
	public UndoManager getUndoManager( ) {
		return undo;
	}
	
	public void setJTextArea( JTextArea textArea ) {
		this.textArea = textArea;
		// Necessary here to remove flickering when pressing arrows up and down
		// where text area has more lines than it can show in visible screen
		textArea.setDoubleBuffered( true );
		// Just join the fun. No visual proof this helps.
		lineArea.setDoubleBuffered( true );
	}
	
	public void setScrollPane( final JScrollPane jsp ) {
	
	   Border border = BorderFactory.createEtchedBorder();
	   JPanel jpb = new JPanel();
	   jpb.setBorder( border );
	   
	   this.jsp = jsp;
	   jsp.setRowHeaderView( lineArea );
	  
	   if ( textArea.getLineCount() == 0 )	  
		   lineArea.append( 1 + " \n" );
	   else {
	      int x = textArea.getLineCount();
	      for ( int i = 0; i < x; i++ ) {
	     	  lineArea.append( lineNumber + " \n" );
	     	  lineNumber++;
	      }
	      lineNumber--;
	   }
	  
	   textArea.addCaretListener( new CaretListener() {
      	public void caretUpdate(CaretEvent e) {
      		int lineCount = textArea.getLineCount();
				while ( lineNumber < lineCount ) 
					 lineArea.append( (++lineNumber) + " \n" );	
			}	
		});

	   jsp.setCorner( ScrollPaneConstants.LOWER_LEFT_CORNER, jpb );
	  
  } // End setScrollPane
  
  public JScrollPane getScrollPane() {
  	  return jsp;
  }
  
  public void setFile( File file ) {
		this.file = file;  
  }
  
  public File getFile() {
  	  return file;
  }
  
  public JTextArea getLineArea() {
	  return lineArea;
  }
	
  public JTextArea getJTextArea() {
	  return textArea;		
  }
  
  public void setTabKeyListener( KeyListener l ) {
  	  tabKeyListener = l;
  }
  
  public KeyListener getTabKeyListener() {
  	  return tabKeyListener;
  }
  
} // End MyInternalFrame


