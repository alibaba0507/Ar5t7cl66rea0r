package articlecreator.gui;

import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Finalized on 9/1/2002
// Author: Samuel Huang

// This class implements functionality of soft tab for JTextArea.
//
class MyTabKey extends KeyAdapter {

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_TAB )	{
			e.consume();
			JTextArea textArea = (JTextArea)e.getSource();
			int tabSize = textArea.getTabSize();			
			StringBuffer buffer = new StringBuffer();
			for ( int i = 0; i < tabSize; i++ )
				buffer.append(" ");	
			textArea.insert( buffer.toString(), textArea.getCaretPosition() );
		}
	}

}

