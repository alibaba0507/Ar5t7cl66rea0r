/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.net;

import java.awt.*;
import javax.swing.*;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * SendMail is an example of client sockets. This program presents a simple
 * dialog box that prompts the user for information about how to send a mail.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */

/*
 * Using the SMTP Program
 * 
 * To use the program in Listing 1.2, you must know the address of an SMTP
 * server usually provided by your ISP. If you are unsure of your SMTP server,
 * you should contact your ISP's customer service. In order for outbound e-mail
 * messages to be sent, your e-mail program must have this address. Once it
 * does, you can enter who is sending the e-mail (if you are sending it, you
 * would type your e-mail address in) and who will be on the receiving end. This
 * is usually entered under the Reply To field of your e-mail program. Both of
 * these addresses must be valid. If they are invalid, the e-mail may not be
 * sent. After you have entered these addresses, you should continue by entering
 * the subject, writing the actual message, and then clicking send. Note
 * 
 * For more information on how to compile examples in this book, see Appendix E
 * 'How to Compile Examples Under Windows.'
 * 
 * As stated earlier, to send an e-mail with this program, you must enter who is
 * sending the message. You may be thinking that you could enter any e-mail
 * address you want here, right? Yes, this is true; as long as the SMTP server
 * allows it, this program will allow you to impersonate anyone you enter into
 * the To address field. However, as previously stated, a savvy Internet user
 * can tell whether the e-mail address is fake.
 * 
 * After the mention of possible misrepresentation of identity on the sender's
 * end, you may now be asking yourself, Is this program dangerous? This
 * program is no more dangerous than any e-mail client (such as Microsoft
 * Outlook Express or Eudora) that also requires you to tell it who you are. In
 * general, all e-mail programs must request both your identity and that of the
 * SMTP server. Examining the SMTP Server
 * 
 * You will now be shown how this program works. We will begin by looking at how
 * a client socket is created. When the client socket is first instantiated, you
 * must specify two parameters. First, you must specify the host to connect to;
 * second, you must specify the port number (e.g., 80) you would like to connect
 * on. These two items are generally passed into the constructor. The following
 * line of code (from Listing 1.2) accomplishes this:
 * 
 * java.net.Socket s =new java.net.Socket( _smtp.getText(),25 );
 * 
 * This line of code creates a new socket, named s. The first parameter to the
 * constructor, _smtp .getText(), specifies the address to connect to. Here it
 * is being read directly from a text field. The second parameter specifies the
 * port to connect to. (The port for SMTP is 25.) Table 1.1 shows a listing of
 * the ports associated with most Internet services. The hostname is retrieved
 * from the _smtp class level variable, which is the JTextField control that the
 * SMTP hostname is entered into.
 * 
 * If any errors occur while you are making the connection to the specified
 * host, the Socket constructor will throw an IOException. Once this connection
 * is made, input and output streams are obtained from the Socket.getInputStream
 * and Socket.getOutputStream methods. This is done with the following lines of
 * code from Listing 1.2:
 * 
 * _out = new java.io.PrintWriter(s.getOutputStream());
 * _in = new java.io.BufferedReader(new java.io.InputStreamReader(s.getInputStream()));
 * 
 * These low-level stream types are only capable of reading binary data. Because
 * this data is needed in text format, filters are used to wrap the lower-level
 * input and output streams obtained from the socket.
 * 
 * In the code above, the output stream has been wrapped in a PrintWriter
 * object. This is because PrintWriter allows the program to output text to the
 * socket in a similar manner to the way an application would write data to the
 * System.out object by using the print and println methods. The application
 * presented here uses the println method to send commands to the SMTP server.
 * As you can see in the code, the InputStream object has also been wrapped; in
 * this case, it has been wrapped in a BufferedReader. Before this could happen,
 * however, this object must first have been wrapped in an InputStreamReader
 * object as shown here:
 * 
 * _in = new java.io.BufferedReader(new
 * java.io.InputStreamReader(s.getInputStream()));
 * 
 * This is done because the BufferedReader object provides reads that are made
 * up of lines of text instead of individual bytes. This way, the program can
 * read text up to a carriage return without having to parse the individual
 * characters. This is done with the readLine method.
 * 
 * You will now be shown how each command is sent to the SMTP server. Each of
 * these commands that is sent results in a response being issued from the SMTP
 * server. For the protocol to work correctly, each response must be read by the
 * SMTP client program. These responses start with a number and then they give a
 * textual description of what the result was. A full-featured SMTP client
 * should examine these codes and ensure that no error has occurred.
 * 
 * For the purposes of the SendMail example, we will simple ignore these
 * responses because most are informational and not needed. Instead, for our
 * purposes, the response will be read in and displayed to the _output list box.
 * Commands that have been sent to the server are displayed in this list with a
 * C: prefix to indicate that they are from the client. Responses returned from
 * the SMTP server will be displayed with the S: prefix.
 * 
 * To accomplish this, the example program will use the send method. The send
 * method accepts a single String parameter to indicate the SMTP command to be
 * issued. Once this command is sent, the send method awaits a response from the
 * SMTP host. The portion of Listing 1.2 that contains the send method is
 * displayed here:
 * 
 * protected void send(String s) throws java.io.IOException { 
        // Send the SMTP command 
         if(s!=null) { 
         _model.addElement("C:"+s); 
         _out.println(s);
        _out.flush(); } 
       // Wait for the response 
         String line = _in.readLine();
        if(line!=null) { 
       _model.addElement("S:"+line); 
       } 
 }
 * 
 * As you can see, the send method does not handle the exceptions that might
 * occur from its commands. Instead, they are thrown to the calling method as
 * indicated by the throws clause of the function declaration. The variable s is
 * checked to see if it is null. If s is null, then no command is to be sent and
 * only a response is sought. If s is not null, then the value of s is logged
 * and then sent to the socket. After this happens, the flush command is given
 * to the socket to ensure that the command was actually sent and not just
 * buffered. Once the command is sent, the readLine method is called to await
 * the response from the server. If a response is sent, then it is logged.
 * 
 * Once the socket is created and the input and output objects are created, the
 * SMTP session can begin. The following commands manage the entire SMTP
 * session:
 * 
 * send(null); 
 * send("HELO " + java.net.InetAddress.getLocalHost().getHostName() );
 * send("MAIL FROM: " + _from.getText() ); send("RCPT TO: " + _to.getText() );
 * send("DATA"); 
 * _out.println("Subject:" + _subject.getText()); _out.println(
 * _body.getText() ); send("."); s.close();
 * 
 * Tip
 * 
 * Refer to Table 1.4 in the preceding section to review the details of what
 * each of the SMTP commands actually means.
 * 
 * The rest of the SendMail program (as seen in Listing 1.2) is a typical Swing
 * application. The graphical user interface (GUI) layout for this application
 * was created using VisualCaf?. The VisualCaf? comments have been left in to
 * allow the form's GUI layout to be edited by VisualCaf? if you are using it.
 * If you are using an environment other than VisualCaf?, you may safely delete
 * the VisualCaf? comments (lines starting in //). The VisualCaf? code only
 * consists of comments and does not need to be deleted to run on other
 * platforms.
 *  
 */

public class SendMail extends javax.swing.JFrame {

  /**
   * The constructor. Do all basic setup for this application.
   */
  public SendMail() {
    //{{INIT_CONTROLS
    setTitle("SendMail Example");
    getContentPane().setLayout(null);
    setSize(736, 312);
    setVisible(false);
    JLabel1.setText("From:");
    getContentPane().add(JLabel1);
    JLabel1.setBounds(12, 12, 36, 12);
    JLabel2.setText("To:");
    getContentPane().add(JLabel2);
    JLabel2.setBounds(12, 48, 36, 12);
    JLabel3.setText("Subject:");
    getContentPane().add(JLabel3);
    JLabel3.setBounds(12, 84, 48, 12);
    JLabel4.setText("SMTP Server:");
    getContentPane().add(JLabel4);
    JLabel4.setBounds(12, 120, 84, 12);
    getContentPane().add(_from);
    _from.setBounds(96, 12, 300, 24);
    getContentPane().add(_to);
    _to.setBounds(96, 48, 300, 24);
    getContentPane().add(_subject);
    _subject.setBounds(96, 84, 300, 24);
    getContentPane().add(_smtp);
    _smtp.setBounds(96, 120, 300, 24);
    getContentPane().add(_scrollPane2);
    _scrollPane2.setBounds(12, 156, 384, 108);
    _body.setText("Enter your message here.");
    _scrollPane2.getViewport().add(_body);
    _body.setBounds(0, 0, 381, 105);
    Send.setText("Send");
    Send.setActionCommand("Send");
    getContentPane().add(Send);
    Send.setBounds(60, 276, 132, 24);
    Cancel.setText("Cancel");
    Cancel.setActionCommand("Cancel");
    getContentPane().add(Cancel);
    Cancel.setBounds(216, 276, 120, 24);
    getContentPane().add(_scrollPane);
    _scrollPane.setBounds(408, 12, 312, 288);
    getContentPane().add(_output);
    _output.setBounds(408, 12, 309, 285);
    //}}

    //{{INIT_MENUS
    //}}

    //{{REGISTER_LISTENERS
    SymAction lSymAction = new SymAction();
    Send.addActionListener(lSymAction);
    Cancel.addActionListener(lSymAction);
    //}}

    _output.setModel(_model);
    _model.addElement("Server output displayed here:");
    _scrollPane.getViewport().setView(_output);
    _scrollPane2.getViewport().setView(_body);
  }

  /**
   * Moves the app to the correct position when it is made visible.
   * 
   * @param b
   *            True to make visible, false to make invisible.
   */
  public void setVisible(boolean b) {
    if (b)
      setLocation(50, 50);
    super.setVisible(b);
  }

  /**
   * The main function basically just creates a new object, then shows it.
   * 
   * @param args
   *            Command line arguments. Not used in this application.
   */
  static public void main(String args[]) {
    (new SendMail()).show();
  }

  /**
   * Created by VisualCafe. Sets the window size.
   */
  public void addNotify() {
    // Record the size of the window prior to
    // calling parents addNotify.
    Dimension size = getSize();

    super.addNotify();

    if (frameSizeAdjusted)
      return;
    frameSizeAdjusted = true;

    // Adjust size of frame according to the
    // insets and menu bar
    Insets insets = getInsets();
    javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
    int menuBarHeight = 0;
    if (menuBar != null)
      menuBarHeight = menuBar.getPreferredSize().height;
    setSize(insets.left + insets.right + size.width, insets.top
        + insets.bottom + size.height + menuBarHeight);
  }

  // Used by addNotify
  boolean frameSizeAdjusted = false;

  //{{DECLARE_CONTROLS

  /**
   * A label.
   */
  javax.swing.JLabel JLabel1 = new javax.swing.JLabel();

  /**
   * A label.
   */
  javax.swing.JLabel JLabel2 = new javax.swing.JLabel();

  /**
   * A label.
   */
  javax.swing.JLabel JLabel3 = new javax.swing.JLabel();

  /**
   * A label.
   */
  javax.swing.JLabel JLabel4 = new javax.swing.JLabel();

  /**
   * Who this message is from.
   */
  javax.swing.JTextField _from = new javax.swing.JTextField();

  /**
   * Who this message is to.
   */
  javax.swing.JTextField _to = new javax.swing.JTextField();

  /**
   * The subject of this message.
   */
  javax.swing.JTextField _subject = new javax.swing.JTextField();

  /**
   * The SMTP server to use to send this message.
   */
  javax.swing.JTextField _smtp = new javax.swing.JTextField();

  /**
   * A scroll pane.
   */
  javax.swing.JScrollPane _scrollPane2 = new javax.swing.JScrollPane();

  /**
   * The body of this email message.
   */
  javax.swing.JTextArea _body = new javax.swing.JTextArea();

  /**
   * The send button.
   */
  javax.swing.JButton Send = new javax.swing.JButton();

  /**
   * The cancel button.
   */
  javax.swing.JButton Cancel = new javax.swing.JButton();

  /**
   * A scroll pain.
   */
  javax.swing.JScrollPane _scrollPane = new javax.swing.JScrollPane();

  /**
   * The output area. Server messages are displayed here.
   */
  javax.swing.JList _output = new javax.swing.JList();

  //}}

  /**
   * The list of items added to the output list box.
   */
  javax.swing.DefaultListModel _model = new javax.swing.DefaultListModel();

  /**
   * Input from the socket.
   */
  java.io.BufferedReader _in;

  /**
   * Output to the socket.
   */
  java.io.PrintWriter _out;

  //{{DECLARE_MENUS
  //}}

  /**
   * Internal class created by VisualCafe to route the events to the correct
   * functions.
   * 
   * @author VisualCafe
   * @version 1.0
   */
  class SymAction implements java.awt.event.ActionListener {

    /**
     * Route the event to the correction method.
     * 
     * @param event
     *            The event.
     */
    public void actionPerformed(java.awt.event.ActionEvent event) {
      Object object = event.getSource();
      if (object == Send)
        Send_actionPerformed(event);
      else if (object == Cancel)
        Cancel_actionPerformed(event);
    }
  }

  /**
   * Called to actually send a string of text to the socket. This method makes
   * note of the text sent and the response in the JList output box. Pass a
   * null value to simply wait for a response.
   * 
   * @param s
   *            A string to be sent to the socket. null to just wait for a
   *            response.
   * @exception java.io.IOException
   */
  protected void send(String s) throws java.io.IOException {
    // Send the SMTP command
    if (s != null) {
      _model.addElement("C:" + s);
      _out.println(s);
      _out.flush();
    }
    // Wait for the response
    String line = _in.readLine();
    if (line != null) {
      _model.addElement("S:" + line);
    }
  }

  /**
   * Called when the send button is clicked. Actually sends the mail message.
   * 
   * @param event
   *            The event.
   */
  void Send_actionPerformed(java.awt.event.ActionEvent event) {
    try {

      java.net.Socket s = new java.net.Socket(_smtp.getText(), 587 );
      _out = new java.io.PrintWriter(s.getOutputStream());
      _in = new java.io.BufferedReader(new java.io.InputStreamReader(s
          .getInputStream()));

      send(null);
      send("HELO " + java.net.InetAddress.getLocalHost().getHostName());
      send("MAIL FROM: " + _from.getText());
      send("RCPT TO: " + _to.getText());
      send("DATA");
      _out.println("Subject:" + _subject.getText());
      _out.println(_body.getText());
      send(".");
      s.close();

    } catch (Exception e) {
      _model.addElement("Error: " + e);
    }

  }

  /**
   * Called when cancel is clicked. End the application.
   * 
   * @param event
   *            The event.
   */
  void Cancel_actionPerformed(java.awt.event.ActionEvent event) {
    System.exit(0);

  }
}