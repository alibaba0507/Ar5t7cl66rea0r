package articlecreator.gui;

import java.awt.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.*;

// Finalized on 12/1/2002
// Last Modified: 22/04/2002
// Author: Samuel Huang

public class MyBasicTextAreaUI extends BasicTextAreaUI {

	private JTextArea textArea;
	private ArrayList positions = new ArrayList();
	private Document document;
	private DefaultListModel model;
	private boolean commandPanel;
	private Color color;
	private JList list;

	public MyBasicTextAreaUI( JTextArea textArea, JList list, boolean commandPanel ) {
		this.textArea = textArea;
		this.document = textArea.getDocument();
		this.list = list;
		this.commandPanel = commandPanel;
		if ( !commandPanel ) 
			this.model = (DefaultListModel)list.getModel();		
	}
	
	static final Comparator positionsComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            int p1 = ((Position)o1).getOffset();
            int p2 = ((Position)o2).getOffset();
            return ( p1 < p2 ? -1 : (p1 == p2 ? 0 : 1));
        }
   };
 
 	Comparator lineComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
        		String s1 = (String)o1;
        		String s2 = (String)o2;
        		int p1 = -1;
        		int p2 = -1;
        		try {
            	p1 = Integer.parseInt( s1.substring( s1.indexOf( " " )+1 ) );
            	p2 = Integer.parseInt( s2.substring( s2.indexOf( " " )+1 ) );
				}
				catch ( Exception e ) {
					e.printStackTrace();
				}
            return ( p1 < p2 ? -1 : (p1 == p2 ? 0 : 1));
        }
   };
   
   public ArrayList getPositions( ) {
		return positions;
	}
	
	public void setPositions( ArrayList list ) {
		positions = list;
	}
	
	 public void setColor( Color color ) {
   	this.color = color;
   }
   
   public Color getColor( ) {
   	return color;
   }

	public void addPosition( int line, boolean remove ) {
		boolean b; 
		if ( contains( line, remove ) ) {
			if ( !remove)
				return;
	      updateList( false, line );
	   }
      else {
			updateList( true, line );
			add( line );	
		}
		textArea.repaint();	
	}
	
	public void add( int line ) {
		Position position = null;
		try {
			int offset = textArea.getLineStartOffset(line);
			positions.add( document.createPosition(offset) );
		}
      catch ( BadLocationException ble ) {
       	ble.printStackTrace();
			return;
      }
		textArea.repaint();
	}
	
	public void updateList( boolean b, int line ) {
	   String item = "Line: " + Integer.toString( line+1 );
	   if ( b ) {
	   	model.addElement( item );
	   	Object objA[] = model.toArray();
	   	Arrays.sort( objA, lineComparator );
	   	model.clear();
	   	for ( int i = 0; i < objA.length; i++ )
	   		model.addElement( objA[i] );
		}
	   else
	   	model.removeElement( item );   	
	}

	public boolean contains( int line, boolean remove ) {
		for ( int i = 0; i < positions.size(); i++ ) {	
	 	   Position pos = (Position)positions.get(i);
	 	   int offset = pos.getOffset();
	 	   try {
	 	   	int test = textArea.getLineOfOffset(offset);
	 	   	if ( test == line ) {
	 	   		if ( remove ) {
	 	   			positions.remove(i);
	 	   			textArea.repaint();
	 	   		}
	 	   		return true;
	 	   	}
	 	   }
	 	   catch ( BadLocationException ble ) {
	 	   	ble.printStackTrace();
	 	   }	
	   }
		return false;
	}

	public void paintBackground(Graphics g) {
	 	 super.paintBackground( g );
	 	 
	 	 try {
	 	 
	 	    Position p;
	 	    int width = textArea.getWidth();
	 	 	 FontMetrics fm = g.getFontMetrics();
	 	 	 int h = fm.getHeight();
	 	 	
	 	    for ( int i = 0; i < positions.size(); i++ ) {
	 	      p = (Position) positions.get(i);
	 	 	 	Rectangle rec = textArea.modelToView(p.getOffset());
	 	 	 	g.setColor( color );
	 	 	 	g.fillRect( 0, rec.y, width, h );	 	 	 	
	 	 	 } 	 
	 	 	 
	 	 	 Document doc = textArea.getDocument();
	 	 	 Position endPos = doc.getEndPosition();
	 	 	 Rectangle rec = textArea.modelToView(endPos.getOffset());
	 	 	 g.setColor( Color.red );
	 	 	 g.drawLine( 0, rec.y+h, width, rec.y+h ); 

 	 	 }
       catch ( BadLocationException ble ) {
       	ble.printStackTrace();
       }    
    }

} // End MyBasicTextAreaUI 
