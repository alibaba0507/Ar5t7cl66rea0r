package articlecreator.gui;


// An extension of PlainDocument that remove '\n' char. JTextField equipped with
// this document will not draw abnormally when multiple lines are copied and 
// pasted to it.
//
// Author: Samuel Huang, 22/12/2001
// Last Modified: 23/12/2001

import javax.swing.*;
import javax.swing.text.*;
import java.util.*;

class RemoveLineDocument extends PlainDocument {
 
   public void insertString(int offs, String str, AttributeSet a) 
   	throws BadLocationException {

       if (str == null) {
      	return;
       }
       char[] charArray = str.toCharArray();
       ArrayList list = new ArrayList();
       for (int i = 0; i < charArray.length; i++) {
       	list.add( new Character( charArray[i] ) );
		 }
	    for ( int i = 0; i < list.size(); i++ ) {
       	 Character c = (Character)list.get( i ); 
       	 if ( c.equals( new Character('\n') )  ) {
       	 	 list.remove(i);
       	 	 i--;
       	 }
       }
       charArray = new char[ list.size() ];
       for ( int i = 0; i < list.size(); i++ ) {
       	charArray[i] = ((Character)list.get(i)).charValue();
       }
       super.insertString(offs, new String(charArray), a);
       
   }

}
