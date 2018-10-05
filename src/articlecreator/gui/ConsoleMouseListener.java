package articlecreator.gui;

import articlecreator.gui.components.ui.InnerFramesUI;
import articlecreator.gui.run.ArticleManagmentMain;
import java.awt.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

// Last Modified: 25/12/2001, 1:05 PM
// Author: Samuel Huang, 25/12/2001

class ConsoleMouseListener extends MouseAdapter {

	JTextArea console;
	MyBasicTextAreaUI myUID;
	String fileName;
	//MyEditor editor;
        ArticleManagmentMain editor;
        Document doc;
	
	public ConsoleMouseListener( JTextArea textArea, BasicTextAreaUI myUID, ArticleManagmentMain editor ) {
		this.console = textArea;
		this.myUID = (MyBasicTextAreaUI) myUID;
		this.editor = editor;
		this.doc = textArea.getDocument();			
	}

	public void mousePressed(MouseEvent e) {
 
         int y = e.getY();
         int h = console.getFontMetrics(console.getFont()).getHeight();
	 	   int line = y/h;
			int click = e.getClickCount();
			int count = console.getLineCount();
 	   	if ( line >= count ) 
 	   		return;
  
 	   	if ( click == 1 ) {
 	   	
 	   		MyInternalFrame frame =  InnerFramesUI.getInstance().getSelectedFrame();//editor.getSelectedFrame();
			  	if ( !myUID.contains( line, false ) )
				   return;
				
				if ( frame == null )
					return;
		 	   fileName = frame.getFile().getName();
 	   
 	   		try {
 	   		
 	   			if ( frame == null )
						return;
 	   		   
	   			int start = console.getLineStartOffset( line );
					int end = console.getLineEndOffset( line );
					String content = doc.getText(start,end-start);
					int index = content.indexOf( fileName );
					
					if ( index != -1 ) {
					
						// If the highlighted line clicked on in the console has class name
						// and its exception line number, plus the selected editor has the
						// same class name as the highlighted line clicked on, then move 
						// the cursor of teh Aeditor to exception line number.
				
						StringBuffer buffer = new StringBuffer();
					   Character character = null;
					   index = content.indexOf(":") + 1;
					   
					   while ( true ) {    
					   
						   try {
						   	character = new Character( content.charAt( index ) );
						   } catch ( IndexOutOfBoundsException ie ) {
						   	ie.printStackTrace();
						   }
						   if ( Character.isDigit( character.charValue() ) )
						   	buffer.append( content.substring( index, index+1 ) );
						   else
								break;	
							index++;	     
					   }
					   try {
					   	line = Integer.parseInt( buffer.toString() );
					   } catch ( NumberFormatException nfe ) {
					   	nfe.printStackTrace();
					   }
					   
						JScrollPane jsp = frame.getScrollPane();
         		   JViewport vp = jsp.getViewport();
                  JTextArea fileWindow = frame.getJTextArea();
                  h = fileWindow.getFontMetrics(fileWindow.getFont()).getHeight();

			      	Point p = new Point();
			      	p.x = 0;
			      	p.y = h*(line-1);
			      	
			      	MyBasicTextAreaUI frameUID = (MyBasicTextAreaUI)fileWindow.getUI();
			      	frameUID.addPosition(line-1,false);

			      	vp.setViewPosition( p );
			      	
			      	// Necessary here to stop bad visual effect of selected text area
			      	frame.show();

			      }  // End if

 	   		} catch ( BadLocationException ble ) {
 					ble.printStackTrace();
 				}
		 		
		 	} else if ( click == 2 ) {		
		 		// Highlight a line if not already highlighted, else remove highlight.
			 	if ( ! myUID.contains( line, true ) )
			 		myUID.add( line );
		 	}

   }  // mousePressed
   

} // End ConsoleMouseListener