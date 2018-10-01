package articlecreator.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;     
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;

class OptionDialog extends JDialog {

  private JButton applyButton, closeButton;
  private JList optionsList;
  private Hashtable hashTable;
  private MyEditor myEditor;
  
  private JPanel optionPanel, layeredPane, emptyPanel;
  private EmptyBorder emptyBorder;
  
  private TextFormatPanel textFormatPanel;
  private FontColorPanel fontColorPanel;

  public OptionDialog( MyEditor myEditor )
  {
	    super( myEditor, "Options Dialog", true); // true -> modal dialog
	    this.myEditor = myEditor;
	    
	    textFormatPanel = new TextFormatPanel( myEditor );
	    fontColorPanel = new FontColorPanel( myEditor );
	    emptyPanel = new JPanel();
       getContentPane().setLayout( new BorderLayout() );

       initHashTable();
       initOptionsList();
      
       JScrollPane jsp = new JScrollPane( optionsList );

       applyButton = new JButton(" Apply ");
       applyButton.addActionListener( applyAction );
       closeButton = new JButton(" Close ");
       closeButton.addActionListener( closeAction );

       emptyBorder = new EmptyBorder(10, 10, 10, 10); // Top, left, bottom, right

       JPanel p1 = new JPanel();
       p1.setLayout( new GridLayout( 1, 2, 7, 0 ) );
       p1.setBorder( emptyBorder );
       p1.add( applyButton );
       p1.add( closeButton );
       
		 optionPanel = new JPanel( new BorderLayout() );
		 optionPanel.add( jsp, BorderLayout.NORTH );
       optionPanel.add( p1, BorderLayout.SOUTH );
       optionPanel.setBorder( emptyBorder );
       Dimension dim = optionPanel.getPreferredSize();
       emptyPanel.add( Box.createRigidArea( dim ) );
       
       initGUI();

       optionsList.addListSelectionListener( new ListSelectionListener() {
	    	 public void valueChanged(ListSelectionEvent e) {  
      		 String option = (String)optionsList.getSelectedValue();
      	    if ( option == null )
      	    	 return;
      		 JPanel panel = (JPanel)hashTable.get( option );
				 Layout( panel );
				 if ( panel == fontColorPanel )
				 	fontColorPanel.doClick(); // Update fontColorPanel in case L&F has changed.
		    }
	    });
	
  }
  
  ActionListener closeAction = new ActionListener() { 
     public void actionPerformed(ActionEvent e) {
         setVisible(false);
     }
  };
   
  ActionListener applyAction = new ActionListener() { 
     public void actionPerformed(ActionEvent e) {
        if ( layeredPane == textFormatPanel ) {
        	  textFormatPanel.applySetting();
        }
        else if ( layeredPane == fontColorPanel ) {
           fontColorPanel.applySetting();
        }
     }
  }; 
 
  public void initHashTable() {
  	  hashTable = new Hashtable();
  	  hashTable.put( "Text Format", textFormatPanel );
  	  hashTable.put( "Text Font & Color", fontColorPanel );
  }
  
  public void initOptionsList() {
  	  optionsList = new JList();
  	  DefaultListModel listModel = new DefaultListModel();
  	  listModel.addElement( "Text Format" );
  	  listModel.addElement( "Text Font & Color" );
  	  /*
     DefaultListModel listModel = new DefaultListModel();
     Enumeration e = hashTable.keys();
  	  while ( e.hasMoreElements() ) {
		  listModel.addElement( (String)e.nextElement() );
     }
     */
     optionsList.setModel(listModel);
  }

  public void Layout( JPanel panel ) {
 	  remove( layeredPane );
 	  layeredPane = panel;
 	  layeredPane.setBorder( emptyBorder );
     getContentPane().add( layeredPane, BorderLayout.EAST ); 
  	  pack();
  }
  
  public void Layout() {
  	  optionsList.clearSelection();
  	  Layout( emptyPanel );
  }
  
  public void initGUI() {
     layeredPane = emptyPanel;
  	  getContentPane().add( optionPanel, BorderLayout.WEST );
     getContentPane().add( new JSeparator( JSeparator.VERTICAL ), BorderLayout.CENTER ); 
     getContentPane().add( emptyPanel, BorderLayout.EAST ); 
  	  pack();
  }
 
  public void dispose() {
	 super.dispose();
  	 textFormatPanel.removeAll();
    fontColorPanel.removeAll();
  }
  
}

