package articlecreator.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;

// Author: Samuel Huang
// Finalize: 12/01/2002 
// Last updated: 23/04/2002

class Run extends Thread {
    
		 private ArrayList list;
		 private JViewport vp;
		 private JScrollBar scrollBar;
		 private int option, index;
		 private MyEditor editor;
		 private JTextArea console;
		 private Boolean Locks[];
		 private MyBasicTextAreaUI myUID;
		 private boolean compilingSuccess = false;
       private volatile boolean execute = true;
       private long startTime = -1,
       				  endTime = -1;
	
		 private String compile = "Process compiling java class started... ",
		 		  application = "Process running java applicaiton started... ",
		 		  applet = "Process running java applet started... ",
		 		  stop = "< Press the STOP button on the toolbar to force " + 
		 		  			"termination of current process >",
		 		  finish = "Process returned.",
		 		  commandDialog = "Process running command from command dialog... ",		 		 
		 		  command, currentDir;

		 private final String fileName; 

		 Run( String command, String currentDir, String fileName, ArrayList list, int option, int index, MyEditor editor ) {
		 	 this.command = command;
		 	 this.currentDir = currentDir;
		 	 this.fileName = fileName;
		 	 this.list = list;
		 	 this.option = option; 
		 	 this.editor = editor;
		 	 this.index = index;	 	 
		 }
		 
    	 public void run() {
    	 
    	 	 this.vp = editor.getViewport();
    	 	 this.console = (JTextArea) list.get(1); 
    	 	 this.myUID = (MyBasicTextAreaUI)console.getUI();
    	 	 this.Locks = new Boolean[2];
			 Locks[0] = new Boolean(true);
			 Locks[1] = new Boolean(true);
			 vp.setView( console );
          list.set( 0, new Boolean( false ) );

			 try { 
		
					Runtime rt = Runtime.getRuntime();
					startTime = System.currentTimeMillis();
			      Process p = rt.exec( command, null, new File( currentDir ) ); 
			      BufferedReader reader1 = new BufferedReader(
						new InputStreamReader( p.getInputStream() ) );
					
					BufferedReader reader2 = new BufferedReader(
						new InputStreamReader( p.getErrorStream() ) );
			      
			      list.set( 2, p );
	
			      console.append("**** Console Number " + (index+1) + " **** \n" );
		   		console.append( stop + "\n" );
				   console.append(currentDir + "> " + command + "\n" );
				   
				   if ( option == 1 )
				   	console.append( compile );
				   else if ( option == 2 )
				   	console.append( application );
				   else if ( option == 3 )
				   	console.append( applet );
				   else if ( option == 4 )
				   	console.append( commandDialog );

				   console.append( "\n\n" );
				   console.setCaretPosition(console.getText().length());
	
					Thread t1 = new Thread( new Print(reader1,0) );
					Thread t2 = new Thread( new Print(reader2,1) );
					t1.start();
					t2.start();
	
					while ( execute ) {
					   if ( Locks[0].equals( Boolean.FALSE ) && Locks[1].equals( Boolean.FALSE ) )
							break;
						else {
							try {
								Thread.sleep(500);
							}
							catch (InterruptedException ie ) {}
						}
					}

					reader1.close();
					reader2.close();
					endTime = System.currentTimeMillis() - startTime;
					long lg = endTime/1000;
					String time = "Time taken: " + lg + "." + (endTime-lg*1000) + " seconds.";
               if ( execute ) {

						if ( option == 1 ) {
							if ( compilingSuccess )
								console.append( "Compiling Java Code Success. "  );
							else
								console.append( "\n" + "Compiling Java Code Failed!!! ");
							console.append( time + "\n" );
						} 
						
						if ( option != 1 )
							finish = "\n" + finish;
						console.append( finish + "\n" + currentDir + "> " );

               } else {
                  
                 console.append( "The Process has been Terminated !!!" );                  

               }

				   console.append("\n\n---------------------------------------------------------------------------------------------------\n");	
					console.setCaretPosition(console.getText().length()); // Alternative method scrollBar.setValue(scrollBar.getMaximum());
	      		p.destroy();

			 } 
			 catch(Exception e) {
			    JOptionPane.showMessageDialog( editor, 
      			 "Error: Invalid Command!!!", "Warning", JOptionPane.ERROR_MESSAGE);
		 	 } 
		 	 
    	 	 list.set(0, new Boolean( true ) );
			 list.set( 2, null );
			 editor.updateCommandPanel( list, index ); // Call back method in MyEditor
			 Toolkit.getDefaultToolkit().beep();
			 
		}  // End run()

      public void terminate() {
         execute = false;
      }

		class Print implements Runnable {
			BufferedReader reader;
			int ind;
			Print( BufferedReader reader, int ind ) {
				this.reader = reader;
			   this.ind = ind;
			}
			public void run() {
			   int i = 0;
				try {
					String msg;
					while ( (msg = reader.readLine()) != null ) {
						i++;
						console.append( msg + "\n" ); 
						console.setCaretPosition(console.getText().length());
						final int line = console.getLineCount()-1;
						final String str = msg;
						// Has to use Runnable thread or else myUID.add() called
						// only in the end
						SwingUtilities.invokeLater( new Runnable() {
			       	   public void run() {
			       	 	   if ( str.indexOf( fileName ) != -1 ) {
									myUID.add( line-1 );
								}	       	 
			       	 	}
			     		});	
					}  //  while
					Locks[ind] = new Boolean(false);
				 } catch ( IOException e ) {
					e.printStackTrace();
				 }
				 if ( i == 0 && ind == 1 )
				 	 compilingSuccess = true;
			}
		}
	
}
