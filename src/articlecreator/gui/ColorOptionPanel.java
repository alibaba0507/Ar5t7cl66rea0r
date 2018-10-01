package articlecreator.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

// Note: This class is adapted from JEdit's ColorOptionPane.java by Slava Pestov.

// Finalized on 8/1/2002
// Last updated: 25/02/2002
// Author: Samuel Huang

public class ColorOptionPanel extends JPanel {
   
	 // private members
	 private ColorTableModel colorModel; 
	 private JTable colorTable;
	 private MyEditor editor;
	 private Hashtable defaultProps;
	 private TableColumn colorColumn;
	 private JColorChooser colorChooser;
	 private JDialog dialog;
	 private int row;
	 private boolean tableLock;

    public ColorOptionPanel( MyEditor editor ) {
    	 this.editor = editor;
    	 this.defaultProps = editor.getDefaultProps();
       _init();
       initColors();
    }

	 // protected members
	 protected void _init()
	 {
      setLayout( new BorderLayout() );
		add(createColorTableScroller(),BorderLayout.CENTER);
		colorChooser = new JColorChooser();
		tableLock = true;
		dialog = JColorChooser.createDialog( 
					 ColorOptionPanel.this,
					 "Pick A Color",
					 true, colorChooser,
					 new OkListener(),
					 null );
	 }

	 private JScrollPane createColorTableScroller()
	 {
		colorModel = createColorTableModel();
		colorTable = new JTable(colorModel);
		colorTable.setRowSelectionAllowed(false);
		colorTable.setColumnSelectionAllowed(false);
		colorTable.setCellSelectionEnabled(false);
		colorTable.getTableHeader().setReorderingAllowed(false);
		colorTable.addMouseListener(new MouseHandler());
		TableColumnModel tcm = colorTable.getColumnModel();
 		colorColumn = tcm.getColumn(1);
		colorColumn.setCellRenderer(new ColorTableModel.ColorRenderer());
		Dimension d = colorTable.getPreferredSize();
		d.height = colorTable.getRowHeight()*5;	// Display only 5 rows in JTable
		JScrollPane scroller = new JScrollPane(colorTable);
		scroller.setPreferredSize(d);
		return scroller;
	 }

	 private ColorTableModel createColorTableModel()
	 {
		return new ColorTableModel( editor );
	 }

    // Also called back by FontColorPanel.java
    //	 
	 public void updateColors( JTextArea jta, Color lineColor ) {
		 colorModel.clear();
		 colorModel.addColorChoice("Foreground", jta.getForeground() );
		 colorModel.addColorChoice("Background", jta.getBackground());
		 colorModel.addColorChoice("Selected Background", jta.getSelectionColor() );
		 colorModel.addColorChoice("Selected Foreground", jta.getSelectedTextColor() );   
		 colorModel.addColorChoice("Highlighted Line", lineColor );
		 colorModel.addColorChoice("Caret", jta.getCaretColor() );
	 }
	 
	 private void initColors() {
		 JTextArea jta;
		 String setting = getSetting();	
		 if ( setting.equals("DEFAULT") )
			 jta = (JTextArea)defaultProps.get("DEFAULT_TEXTAREA");
		 else 
			 jta = (JTextArea)defaultProps.get("CUSTOM_EDITOR");
	 }
	 
	 public void enableTable( boolean b ) {
	 	 tableLock = b;	 
	 }
	
    public String getSetting() {
       return (String)defaultProps.get( "SETTING" );
    }

	 class MouseHandler extends MouseAdapter
	 {
		 public void mouseClicked(MouseEvent evt)
		 {
		 	if ( tableLock )
		 		return;
			row = colorTable.rowAtPoint(evt.getPoint());
			if (row == -1)
				return;
			colorChooser.setColor( (Color)colorModel.getValueAt(row,1) );
			dialog.show();
		 }
	 }
	 
	 class OkListener implements ActionListener {
		 public void actionPerformed( ActionEvent e ) {
		 	 Color color = colorChooser.getColor();
		 	 colorModel.setValueAt(color,row,1);
		 	 String label = (String)colorModel.getValueAt( row, 0 );
		 	 if ( label.equals( "Foreground" ) )
		 	 	 FontColorPanel.m_preview.setForeground( color );
		 	 else if ( label.equals( "Background" ) )
		 	 	 FontColorPanel.m_preview.setBackground( color );
		 	 else if ( label.equals( "Selected Background" ) )	{ 
		 	 	 FontColorPanel.setAttributesSelectedBackground( color );
		 	 	 FontColorPanel.m_preview.setSelectionColor( color );
		 	 }
		 	 else if ( label.equals( "Selected Foreground" ) )	{ 
		 	 	 FontColorPanel.setAttributesSelectedForeground( color );
		 	 	 FontColorPanel.m_preview.setSelectedTextColor( color );
		 	 }
		 	 else if ( label.equals( "Highlighted Line" ) )	{
		 	 	 FontColorPanel.myUI.setColor( color );
		 	 	 FontColorPanel.m_preview.repaint();
		 	 }
		 	 else if ( label.equals( "Caret" ) )	{
		 	 	 FontColorPanel.m_preview.setCaretColor( color );
		 	 }
	 	 }
	 }

}

class ColorTableModel extends AbstractTableModel
{
		static private LineBorder noFocusBorder = new LineBorder( Color.white, 1 );
		static private LineBorder FocusBorder = new LineBorder( Color.magenta, 2 );

		private Vector colorChoices;
		private MyEditor editor;
		private Hashtable defaultProps;

		ColorTableModel( MyEditor editor )
		{
			this.editor = editor;
			defaultProps = editor.getDefaultProps();
			colorChoices = new Vector();
		}
	
		public int getColumnCount()
		{
			return 2;
		}
	
		public int getRowCount()
		{
			return colorChoices.size();
		}
	
		public Object getValueAt(int row, int col)
		{
			ColorChoice ch = (ColorChoice)colorChoices.elementAt(row);
			switch(col)
			{
			case 0:
				return ch.label;
			case 1:
				return ch.color;
			default:
				return null;
			}
		}
	
		public void setValueAt(Object value, int row, int col)
		{
			ColorChoice ch = (ColorChoice)colorChoices.elementAt(row);
			if(col == 1)
				ch.color = (Color)value;
			fireTableRowsUpdated(row,row);
		}
	
		public String getColumnName(int index)
		{
			switch(index)
			{
			case 0:
				return "Object";
			case 1:
				return "Color";
			default:
				return null;
			}
		}
		
		public void clear() {
			fireTableRowsDeleted( 0, getRowCount() ); // Necessary to keep table draw properly
			colorChoices.clear();
		}
	
		public void addColorChoice(String label, Color color)
		{
			colorChoices.addElement(new ColorChoice( label, color ) );
		}
	
		static class ColorChoice
		{
			String label;
			Color color;
	
			ColorChoice(String label, Color color)
			{
				this.label = label;
				this.color = color;
			}
		}
	
		static class ColorRenderer extends JLabel
			implements TableCellRenderer
		{
			public ColorRenderer()
			{
				setOpaque(true);
				setBorder( noFocusBorder );
			}
		
			// TableCellRenderer implementation
			public Component getTableCellRendererComponent(
				JTable table,
				Object value,
				boolean isSelected,
				boolean cellHasFocus,
				int row,
				int col)
			{
				if (isSelected)
				{
					setBackground(table.getSelectionBackground());
					setForeground(table.getSelectionForeground());
				}
				else
				{
					setBackground(table.getBackground());
					setForeground(table.getForeground());
				}
		
				if (value != null)
					setBackground((Color)value);
		
				setBorder((cellHasFocus) ? FocusBorder : noFocusBorder );
				return this;
			}
			
		}	// End getTableCellRendererComponent

} // End ColorTableModel
