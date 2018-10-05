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
 
// Authors: Matthew Robinson and Pavel Vorobiev

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.*;

class SmallToggleButton extends JToggleButton implements ItemListener
{
  protected Border m_raised;
  protected Border m_lowered;

  public SmallToggleButton(boolean selected, ImageIcon imgUnselected, 
   ImageIcon imgSelected, String tip) 
  {
    super(imgUnselected, selected);
    setHorizontalAlignment(CENTER);
    setBorderPainted(true);
    m_raised = new BevelBorder(BevelBorder.RAISED);
    m_lowered = new BevelBorder(BevelBorder.LOWERED);
    setBorder(selected ? m_lowered : m_raised);
    setMargin(new Insets(1,1,1,1));
    setToolTipText(tip);
    setRequestFocusEnabled(false);
    setSelectedIcon(imgSelected);
    addItemListener(this);
  }

  public float getAlignmentY() { return 0.5f; }

  public void itemStateChanged(ItemEvent e) {
    setBorder(isSelected() ? m_lowered : m_raised);
  }
  
  public void initBorder() {
  	  setBorder( m_raised );
  }
  
}
