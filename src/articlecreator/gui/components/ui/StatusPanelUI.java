/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

/**
 *
 * @author alibaba0507
 */
public class StatusPanelUI {
    
    private static StatusPanelUI instance;
    private JLabel statusLabel1;
    private JLabel statusLabel2;
    private JLabel statusLabel3;
    private JButton clearButton;
    private JTextField goToLine;
    private JButton goButton;
    private JTextField tabField;
    private JButton tabButton;
    public StatusPanelUI()
    {
        super();
        
    }
    public static StatusPanelUI getInstance()
    {
        if (instance == null)
             instance = new StatusPanelUI();
       return instance;
    }
public JPanel createStatusPanel(JList consolesList,ArrayList outputList) {

        statusLabel1 = new JLabel("    ");  // Display caret position
        statusLabel2 = new JLabel("    ");  // Display total number of lines in file
        statusLabel3 = new JLabel("    ");  // Display tab size
        // Create lowered labels
        Border loweredBorder = new SoftBevelBorder(SoftBevelBorder.LOWERED);
        Border emptyBorder = new EmptyBorder(-3, 0, -3, 0);

        clearButton = new JButton("    Clear Console    ");
        clearButton.addActionListener(new ActionsUI().new ClearAction(consolesList,outputList));

        JPanel panel = new JPanel(new BorderLayout());

        // Nice trick of drawing a thin white line around the panel.
        // Setting panel.setBorder( new EtchedBorder(EtchedBorder.RAISED) );
        // doesn't look as good.
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(4, 1, 1, 1),
                new EtchedBorder(EtchedBorder.RAISED)));

        statusLabel1.setBorder(BorderFactory.createCompoundBorder(
                loweredBorder, emptyBorder));
        statusLabel2.setBorder(BorderFactory.createCompoundBorder(
                loweredBorder, emptyBorder));
        statusLabel3.setBorder(BorderFactory.createCompoundBorder(
                loweredBorder, emptyBorder));

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.add(Box.createRigidArea(new Dimension(1, 0)));
        panel2.add(statusLabel1);
        panel2.add(Box.createRigidArea(new Dimension(4, 0)));
        panel2.add(statusLabel2);
        panel2.add(Box.createRigidArea(new Dimension(4, 0)));
        panel2.add(statusLabel3);
        panel2.add(Box.createRigidArea(new Dimension(4, 0)));

        JLabel label = new JLabel("Go to Line: ");
        label.setBorder(emptyBorder);

        goToLine = new JTextField();
        goToLine.setEnabled(false);
        goToLine.setBorder(new CompoundBorder(new LineBorder(Color.black),
                new EmptyBorder(-2, 0, -2, 0)));
        try {
            goToLine.setColumns(5);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        goButton = new JButton("   Go   ");
        goButton.addActionListener(new ActionsUI().new GoAction(goToLine));
        goButton.setBorder(BorderFactory.createCompoundBorder(
                new EtchedBorder(EtchedBorder.LOWERED), emptyBorder));
        goToLine.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    goButton.doClick();
                }
            }
        });

        JPanel panel3 = new JPanel();
        panel3.add(label);
        panel3.add(goToLine);
        panel3.add(goButton);

        panel2.add(panel3);

        JPanel panel4 = new JPanel();
        panel4.add(clearButton);
        clearButton.setBorder(BorderFactory.createCompoundBorder(
                new EtchedBorder(EtchedBorder.LOWERED), emptyBorder));

        JPanel panel5 = new JPanel();
        label = new JLabel("Tab Size: ");
        label.setBorder(emptyBorder);
        tabField = new JTextField();
        tabField.setEnabled(false);
        tabField.setBorder(new CompoundBorder(new LineBorder(Color.black),
                new EmptyBorder(-2, 0, -2, 0)));

        try {
            tabField.setColumns(3);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        tabButton = new JButton("  Change  ");
        tabButton.addActionListener(new ActionsUI().new TabAction(tabField));
        tabButton.setBorder(BorderFactory.createCompoundBorder(
                new EtchedBorder(EtchedBorder.LOWERED), emptyBorder));
        tabField.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    tabButton.doClick();
                }
            }
        });

        panel5.add(label);
        panel5.add(tabField);
        panel5.add(tabButton);

        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(panel2, BorderLayout.WEST);
        panel1.add(new JSeparator(), BorderLayout.CENTER);
        panel1.add(panel5, BorderLayout.EAST);

        panel.add(panel1, BorderLayout.WEST);
        panel.add(panel4, BorderLayout.EAST);

        return panel;

    }
    
}
