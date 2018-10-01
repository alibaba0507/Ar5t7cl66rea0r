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
 
// Authors: Matthew Robinson and Pavel Vorobiev.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

class OpenList extends JPanel implements ListSelectionListener, ActionListener
{
	protected JLabel m_title;
	protected JTextField m_text;
	protected JList m_list;
	protected JScrollPane m_scroll;

	public OpenList(String[] data, String title) {
	 setLayout(null);
	 m_title = new JLabel(title, JLabel.LEFT);
	 add(m_title);
	 m_text = new JTextField();
	 m_text.addActionListener(this);
	 add(m_text);
	 m_list = new JList(data);
	 m_list.setVisibleRowCount(4);
	 m_list.addListSelectionListener(this);
	 m_scroll = new JScrollPane(m_list);
	 add(m_scroll);
	}

	public OpenList(String title, int numCols) {
	 setLayout(null);
	 m_title = new JLabel(title, JLabel.LEFT);
	 add(m_title);
	 m_text = new JTextField(numCols);
	 m_text.addActionListener(this);
	 add(m_text);
	 m_list = new JList();
	 m_list.setVisibleRowCount(4);
	 m_list.addListSelectionListener(this);
	 m_scroll = new JScrollPane(m_list);
	 add(m_scroll);
	}

	
	public void setSelected(String sel) {
	 m_list.setSelectedValue(sel, true);
	 m_text.setText(sel);
	}

	public String getSelected() { return m_text.getText(); }

	public void setSelectedInt(int value) {
	 setSelected(Integer.toString(value));
	}

	public int getSelectedInt() {
	 try { 
	   return Integer.parseInt(getSelected());
	 }
	 catch (NumberFormatException ex) { return -1; }
	}

	public void valueChanged(ListSelectionEvent e) {
	 Object obj = m_list.getSelectedValue();
	 if (obj != null)
	   m_text.setText(obj.toString());
	}

	public void actionPerformed(ActionEvent e) {
	 ListModel model = m_list.getModel();
	 String key = m_text.getText().toLowerCase();
	 for (int k=0; k<model.getSize(); k++) {
	   String data = (String)model.getElementAt(k);
	   if (data.toLowerCase().startsWith(key)) {
	     m_list.setSelectedValue(data, true);
	     break;
	   }
	 }
	}

	public void addListSelectionListener(ListSelectionListener lst) {
	 m_list.addListSelectionListener(lst);
	}

	public Dimension getPreferredSize() {
	 Insets ins = getInsets();
	 Dimension d1 = m_title.getPreferredSize();
	 Dimension d2 = m_text.getPreferredSize();
	 Dimension d3 = m_scroll.getPreferredSize();
	 int w = Math.max(Math.max(d1.width, d2.width), d3.width);
	 int h = d1.height + d2.height + d3.height;
	 return new Dimension(w+ins.left+ins.right, 
	   h+ins.top+ins.bottom);
	}

	public Dimension getMaximumSize() {
	 Insets ins = getInsets();
	 Dimension d1 = m_title.getMaximumSize();
	 Dimension d2 = m_text.getMaximumSize();
	 Dimension d3 = m_scroll.getMaximumSize();
	 int w = Math.max(Math.max(d1.width, d2.width), d3.width);
	 int h = d1.height + d2.height + d3.height;
	 return new Dimension(w+ins.left+ins.right, 
	   h+ins.top+ins.bottom);
	}

	public Dimension getMinimumSize() {
	 Insets ins = getInsets();
	 Dimension d1 = m_title.getMinimumSize();
	 Dimension d2 = m_text.getMinimumSize();
	 Dimension d3 = m_scroll.getMinimumSize();
	 int w = Math.max(Math.max(d1.width, d2.width), d3.width);
	 int h = d1.height + d2.height + d3.height;
	 return new Dimension(w+ins.left+ins.right, 
	   h+ins.top+ins.bottom);
	}

	public void doLayout() {
	 Insets ins = getInsets();
	 Dimension d = getSize();
	 int x = ins.left;
	 int y = ins.top;
	 int w = d.width-ins.left-ins.right;
	 int h = d.height-ins.top-ins.bottom;

	 Dimension d1 = m_title.getPreferredSize();
	 m_title.setBounds(x, y, w, d1.height);
	 y += d1.height;
	 Dimension d2 = m_text.getPreferredSize();
	 m_text.setBounds(x, y, w, d2.height);
	 y += d2.height;
	 m_scroll.setBounds(x, y, w, h-y);
	}
	
	public void disable( boolean b ) {
		if ( b ) {
			m_text.setEnabled( false );
			m_list.setEnabled( false );
		}
		else {
			m_text.setEnabled( true );
			m_list.setEnabled( true );
		}
	}
		
}

	
