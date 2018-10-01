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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.border.*;

// Finalized on 8/1/2002
// Last updated: 3/07/2002
// Author: Samuel Huang

class FontColorPanel extends JPanel {

  private int m_option = JOptionPane.CLOSED_OPTION;
  private OpenList m_lstFontName;
  private OpenList m_lstFontSize;

  private JCheckBox m_chkBold;
  private JCheckBox m_chkItalic;
  private Border emptyBorder;
    
  // To be accessed by colorOptionPanel.
  public static JTextPane m_preview;
  public static MyBasicTextPaneUI myUI;
  
  private JRadioButton defaultConsoleRadio, defaultDocRadio,
  							  currentConsoleRadio, currentDocRadio, 
							  defaultColorRadio, customColorRadio; 
  
  private static StyledDocument document;
  private MyEditor myEditor;
  private ColorOptionPanel colorPanel;
  private Hashtable defaultProps;
  private static SimpleAttributeSet attributes;

  public FontColorPanel( final MyEditor myEditor )
  {
	    
	    this.myEditor = myEditor;
	    this.defaultProps = myEditor.getDefaultProps();
    	 String[] names;
  		 String[] sizes = new String[65];

		 m_chkBold = new JCheckBox("Bold");
		 m_chkItalic = new JCheckBox("Italic");

		 defaultColorRadio = new JRadioButton("Use Look & Feel Color");
		 customColorRadio = new JRadioButton("Use Custom Color");

		 colorPanel = new ColorOptionPanel( myEditor );
		 
		 ButtonGroup bg = new ButtonGroup();
		 bg.add( defaultColorRadio );
		 bg.add( customColorRadio );

  		 defaultConsoleRadio = new JRadioButton("Default Custom Console");
  		 currentConsoleRadio = new JRadioButton("Current Console");
		 defaultDocRadio = new JRadioButton("Default Custom Editor");
		 currentDocRadio = new JRadioButton("Current Editor");

		 bg = new ButtonGroup();
		 bg.add( defaultConsoleRadio );
		 bg.add( currentDocRadio );
		 bg.add( defaultDocRadio );
		 bg.add( currentConsoleRadio );
		 
  		 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	 names = ge.getAvailableFontFamilyNames();
    	 for ( int i = 0; i <= 64; i++ )
    		 sizes[i] = Integer.toString( 8+i );
    
	    setLayout(new BoxLayout( this, 
	      BoxLayout.Y_AXIS));

		 JPanel p = new JPanel();
		 p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
		 p.setBorder( new CompoundBorder( 
	    	 new TitledBorder( new EtchedBorder(), "Font" ), 
			 new EmptyBorder(0,5,0,5) ) );
	    JPanel p0 = new JPanel(new GridLayout(1, 2, 10, 2));
	    m_lstFontName = new OpenList(names, "Name:");
		 m_lstFontSize = new OpenList(sizes, "Size:");
	    p0.add(m_lstFontName);
	    p0.add(m_lstFontSize);

		 JPanel p1 = new JPanel( new GridLayout( 1,2) );
		 p1.setBorder( new EmptyBorder( 0,0,0,0 ) );
	    p1.add(m_chkBold);
	    p1.add(m_chkItalic);
		 p.add( p0 );
		 p.add( p1 );
	    add(p);

		 p = new JPanel();
		 p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
	    p.setBorder( new CompoundBorder( 
			 new TitledBorder(new EtchedBorder(), "Color"),
			 new EmptyBorder( 0, 5, 5, 5 ) ) );
		 p1 = new JPanel( new GridLayout( 1,2) );
		 p1.setBorder( new EmptyBorder( 0,0,0,0 ) );
		 p1.add( defaultColorRadio );
		 p1.add( customColorRadio );
		 p.add(p1);
		 p.add( colorPanel );
		 add(p);

		 p = new JPanel(new GridLayout(2, 2));
	    p.setBorder( new CompoundBorder( 
			 new TitledBorder( new EtchedBorder(), "Preview And Apply Setting To" ),
			 new EmptyBorder( 0, 5, 0, 0 ) ) );
		 p.add( defaultDocRadio ); 
 		 p.add( defaultConsoleRadio );
    	 p.add( currentDocRadio );
    	 p.add( currentConsoleRadio );
		 add( p );
		
	    p = new JPanel(new BorderLayout());
	    p.setBorder( new CompoundBorder( 
	    	 new TitledBorder( new EtchedBorder(), "Preview" ), 
			 new EmptyBorder(5,5,5,5) ) );

	    m_preview = new JTextPane();
	    document = (StyledDocument)m_preview.getDocument();
	    m_preview.setText(" Normal Text \n Selected Text \n Highlighted line \n");
	    m_preview.setEditable( false );
	    myUI = new MyBasicTextPaneUI();

       String setting = (String)defaultProps.get( "SETTING" );

	    myUI.setColor( myEditor.getLineColor( setting, "EDITOR" ) );
	    m_preview.setUI( myUI );
	    
	    // Don't use setPreferredSize here or the size won't be set properly in the first time
	    m_preview.setSize(new Dimension(100, 30));
	    
	    if ( setting.equals( "DEFAULT" ) )
		    setAttributes( (JTextArea)defaultProps.get("DEFAULT_TEXTAREA") );
		 else {
		 	 setAttributes( (JTextArea)defaultProps.get("CUSTOM_EDITOR") );
		 	 defaultDocRadio.setSelected( true );
		 }
	    
	    JScrollPane jsp = new JScrollPane( m_preview, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
		 	 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
	    add( jsp );
	  
	    ListSelectionListener lsel = new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e) {
	        updatePreview();
	      }
	    };
	    m_lstFontName.addListSelectionListener(lsel);
	    m_lstFontSize.addListSelectionListener(lsel);

	    ActionListener lst = new ActionListener() { 
	      public void actionPerformed(ActionEvent e) {
	        updatePreview();
	      }
	    };
	    m_chkBold.addActionListener(lst);
	    m_chkItalic.addActionListener(lst);

    	 initSetting();
    	 
    	 defaultColorRadio.addActionListener( new ActionListener() {
		 	 public void actionPerformed( ActionEvent e ) {
			 	 defaultColorRadioAction();
	  		 }
		 });
		 
		 customColorRadio.addActionListener( new ActionListener() {
		 	 public void actionPerformed( ActionEvent e ) {
			 	 customColorRadioAction();
	  		 }
		 });
		 
		 currentConsoleRadio.addActionListener( new ActionListener() {
		 	 public void actionPerformed( ActionEvent e ) {
			 	 JTextArea jta = myEditor.getSelectedConsole();
			 	 update( jta, false, "CONSOLE" );
	  		 }
		 });
		 
		 defaultConsoleRadio.addActionListener( new ActionListener() {
		 	 public void actionPerformed( ActionEvent e ) {
			 	 JTextArea jta = (JTextArea)defaultProps.get("CUSTOM_CONSOLE");
			 	 update( jta, true, "CONSOLE" );
	  		 }
		 });
		 
		 currentDocRadio.addActionListener( new ActionListener() {
		 	 public void actionPerformed( ActionEvent e ) {
		 	 	 MyInternalFrame frame = myEditor.getSelectedFrame();
		 	 	 if ( frame == null )
		 	 	 	return;
			 	 JTextArea jta = frame.getJTextArea();
			 	 update( jta, false, "EDITOR" );
	  		 }
		 });
		 
		 defaultDocRadio.addActionListener( new ActionListener() {
		 	 public void actionPerformed( ActionEvent e ) {
			 	 JTextArea jta = (JTextArea)defaultProps.get("CUSTOM_EDITOR");
			 	 update( jta, true, "EDITOR" );
	  		 }
		 });
	}

	public void setAttributes( JTextArea jta ) {
		attributes = new SimpleAttributeSet();
		StyleConstants.setBackground( attributes, jta.getSelectionColor() );
		StyleConstants.setForeground( attributes, jta.getSelectedTextColor() );
		document.setCharacterAttributes(15,13,attributes, false );
	}
	
	// Used by ColorOptionPanel
	public static void setAttributesSelectedBackground( Color color ) {
		StyleConstants.setBackground( attributes, color );
		document.setCharacterAttributes(15,13,attributes, false );	
	}
	
	// Used by ColorOptionPanel
	public static void setAttributesSelectedForeground( Color color ) {
		StyleConstants.setForeground( attributes, color );
		document.setCharacterAttributes(15,13,attributes, false );	
	}

	private void defaultColorRadioAction() {
		 currentDocRadio.setSelected( true );
		 updateSetting( false );
	 	 JTextArea jta = (JTextArea)defaultProps.get("DEFAULT_TEXTAREA");
		 update( jta, true, "EDITOR" );
		 customColorRadio.setSelected( false );
	}
	
	private void customColorRadioAction() {
		 updateSetting( true );
		 if ( myEditor.getSelectedFrame() == null ) {
		 	 	currentDocRadio.setEnabled( false );
		 }
		 if ( currentConsoleRadio.isSelected() ) {
			 currentConsoleRadio.doClick();
		 }
		 else if ( currentDocRadio.isSelected() ) {
			 currentDocRadio.doClick();
		 }
		 else if ( defaultDocRadio.isSelected() ) {
		 	 defaultDocRadio.doClick();
		 }
		 else if ( defaultConsoleRadio.isSelected() ) {
		 	 defaultConsoleRadio.doClick();
		 }
	}
	
	private void update( final JTextArea jta, boolean useDefault, String type ) {
      Color color = getLineColor( useDefault, jta, type );
		colorPanel.updateColors( jta, color );
		updateFontPanel( jta );
		updatePreviewPanel( jta, color );
	}
	
	private void updatePreview() {
	    String name = m_lstFontName.getSelected();
	    int size = m_lstFontSize.getSelectedInt();
	    if (size <= 0)
	      return;
	    int style = Font.PLAIN;
	    if (m_chkBold.isSelected())
	      style |= Font.BOLD;
	    if (m_chkItalic.isSelected())
	      style |= Font.ITALIC;

	    Font fn = new Font(name, style, size);
	    m_preview.setFont(fn);
	    m_preview.repaint();
	}
	
	// Invoked to update preview panel in case Look & Feel of
	// text area setting changed.
	public void doClick() {
		if ( defaultColorRadio.isSelected() ) 
			  defaultColorRadio.doClick();
		if ( customColorRadio.isSelected() )  
			  customColorRadio.doClick();
	}
	
	  
   public void applySetting() {
   	if ( defaultColorRadio.isSelected() ) {
   		defaultProps.put( "SETTING", "DEFAULT" );
   	} else if ( customColorRadio.isSelected() ) {
   	  JTextArea jta = applyPreviewSetting( null );
		  jta.setCaretColor( m_preview.getCaretColor() );
   	  if ( defaultConsoleRadio.isSelected() ) {
   	  		defaultProps.put( "SETTING", "CUSTOM" );
   	  		defaultProps.put( "CUSTOM_CONSOLE", jta );
            defaultProps.put( "CUSTOM_CONSOLE_HIGHLIGHTED_LINE", 
               ((MyBasicTextPaneUI)m_preview.getUI()).getColor() );            	
   	  }
   	  else if ( defaultDocRadio.isSelected() ) {
   	  		defaultProps.put( "SETTING", "CUSTOM" );
   	  	  	defaultProps.put( "CUSTOM_EDITOR", jta );
            defaultProps.put( "CUSTOM_EDITOR_HIGHLIGHTED_LINE", 
               ((MyBasicTextPaneUI)m_preview.getUI()).getColor() );
   	  }
   	  else if ( currentConsoleRadio.isSelected() ) {
   	  		JTextArea console = myEditor.getSelectedConsole();
   	  		customizeTextArea( console );
            myEditor.updateFontPanel( console, "CONSOLE" );
   	  }
 		  else if ( currentDocRadio.isSelected() && currentDocRadio.isEnabled() ) {
 		  		JTextArea doc = ((MyInternalFrame)myEditor.getSelectedFrame()).getJTextArea();
 		  		customizeTextArea( doc );
            myEditor.updateFontPanel( doc, "EDITOR" );
   	  }
	   }
   }
   
   public void customizeTextArea( JTextArea textArea ) {
	   textArea.setForeground( m_preview.getForeground() );
		textArea.setBackground( m_preview.getBackground() );
		textArea.setCaretColor( m_preview.getCaretColor() ); 
      textArea.setFont( m_preview.getFont() );
		textArea.setSelectionColor( m_preview.getSelectionColor() );
		MyBasicTextAreaUI textAreaUI = (MyBasicTextAreaUI)textArea.getUI();
		MyBasicTextPaneUI textPaneUI = (MyBasicTextPaneUI)m_preview.getUI();
		textAreaUI.setColor( textPaneUI.getColor() );  
   }
   
   private void initSetting() {
   	String setting = (String)defaultProps.get("SETTING");
   	if ( setting.equals("DEFAULT") ) 
   		defaultColorRadio.doClick();
   	else 
   		customColorRadio.doClick();
   }
     
   public void updateFontPanel( JTextArea jta ) {
     	Font font = jta.getFont();
     	m_lstFontName.setSelected( font.getName() );
     	m_lstFontSize.setSelectedInt( font.getSize() );
     	m_chkBold.setSelected( font.isBold() );
     	m_chkItalic.setSelected( font.isItalic() );
   }
   
   public void updatePreviewPanel( JTextArea jta, Color lineColor ) {
   
		setAttributes( jta );
     	Font font = jta.getFont();
     	m_preview.setFont( font );
     	m_preview.setForeground( jta.getForeground() );
     	m_preview.setBackground( jta.getBackground() );
     	m_preview.setSelectionColor( jta.getSelectionColor() );
     	m_preview.setSelectedTextColor( jta.getSelectedTextColor() );
		MyBasicTextPaneUI textPaneUI = (MyBasicTextPaneUI)m_preview.getUI();
		textPaneUI.setColor( lineColor );
     	m_preview.repaint();
 
   }

   // Also used by FontColorPanel.java
   public Color getLineColor( boolean useDefault, JTextArea jta, String type ) {
      Color color = null;
      if ( useDefault ) {
         color = myEditor.getLineColor( "CUSTOM", type );
      }
		else {
		   MyBasicTextAreaUI jtaUI = (MyBasicTextAreaUI)jta.getUI();
         color = jtaUI.getColor();
		}
      return color;
   }
   
   private JTextArea applyPreviewSetting( JTextArea jta ) {
   	if ( jta == null )
	   	jta = new JTextArea();
   	jta.setFont( m_preview.getFont() );
  		jta.setForeground( m_preview.getForeground() );
     	jta.setBackground( m_preview.getBackground() );
     	jta.setSelectionColor( m_preview.getSelectionColor() );
     	jta.setSelectedTextColor( m_preview.getSelectedTextColor() );
     	jta.repaint();
     	return jta;
   }

   private void updateSetting( boolean b ) {
   	defaultConsoleRadio.setEnabled( b );
	 	defaultDocRadio.setEnabled( b );
		currentConsoleRadio.setEnabled( b );
		currentDocRadio.setEnabled( b );
		colorPanel.enableTable( !b );
		m_lstFontName.disable( !b );
		m_lstFontSize.disable( !b );	
		m_chkBold.setEnabled( b );
		m_chkItalic.setEnabled( b );
   }
	  
   class MyBasicTextPaneUI extends BasicTextPaneUI {
	   Color color;		
		public void paintBackground(Graphics g) {
		 	 super.paintBackground( g );
	 	 	 try {
		 	    Position p;
		 	    int width = m_preview.getWidth();
		 	 	 FontMetrics fm = g.getFontMetrics();
		 	 	 int h = fm.getHeight();
	 	 		 Rectangle rec = m_preview.modelToView( 30 );
	 	 	 	 g.setColor( color );
	 	 	 	 g.fillRect( 0, rec.y, width, h );	 
	 	 	 }
	 	 	 catch ( BadLocationException ble ) {
       		 ble.printStackTrace();
       	 }    
   	}	
   	public void setColor( Color color ) {
   		this.color = color;
   	}
   	public Color getColor( ) {
   		return color;
   	}
   } // End MyBasicTextPaneUI 
  
}
    
