package articlecreator.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.*;
import java.util.*;

// Author: Samuel Huang, 23/02/2002
// Last updated: 23/04/2002

class Build extends Thread {
    
		 private ArrayList list, errorList;
		 private JViewport vp;
		 private JScrollBar scrollBar;
		 private int option, index;
		 private MyEditor editor;
		 private JTextArea console;
		 private boolean lock;
		 private MyBasicTextAreaUI myUID;
		 private boolean compilingSuccess = false;
	    private File project, file;
       private volatile boolean execute = true;
		 private long startTime = -1,
       				  endTime = -1;

		 private String stop = "< Press the STOP button on the toolbar to force " + 
		 		  			 "termination of current process >",
		 		   finish = "Project Building Finished. ",
		 		   command, currentDir, fileName;		 		  	

		 Build( String currentDir, File project, ArrayList list, int index, MyEditor editor ) {
		 	 this.currentDir = currentDir;
          this.errorList = new ArrayList();
		 	 this.project = project;
		 	 this.list = list;
		 	 this.editor = editor;
		 	 this.index = index;	
          this.currentDir = project.getPath();
		 }
		 
    	 public void run() {
    							 
    	    this.vp = editor.getViewport();
    	 	 this.console = (JTextArea) list.get(1); 
    	 	 this.myUID = (MyBasicTextAreaUI)console.getUI();	 
			 vp.setView( console );
    	 	 list.set(0, new Boolean( false ) );

          console.append("**** Console Number " + (index+1) + " **** \n" );
   		 console.append( stop + "\n" );
		    console.append( "Building Project of Directory: " + currentDir + "\n\n" );          

			 try { 

               File files[] = project.listFiles(); 
					startTime = System.currentTimeMillis();
		         for ( int i = 0; i < files.length; i++ ) {

                  file = (File)files[i];
                  if ( file.isDirectory() || !file.getName().endsWith(".java") )
                     continue;

                  fileName = file.getName();
                  command = "javac " + fileName;

						lock = true;                

						Runtime rt = Runtime.getRuntime();
				      Process p = rt.exec( command, null, project ); 
				      BufferedReader reader1 = new BufferedReader(
							new InputStreamReader( p.getInputStream() ) );
						
						BufferedReader reader2 = new BufferedReader(
							new InputStreamReader( p.getErrorStream() ) );
				     
				      list.set( 2, p );
		
					   console.append( currentDir + "> " + command + "\n" );
					   console.setCaretPosition(console.getText().length());
		
						Thread t1 = new Thread( new Print( reader1, fileName ) );
						Thread t2 = new Thread( new Print(reader2, fileName ) );
						t1.start();
						t2.start();
		
						while ( execute ) {
						   if ( !lock )
								break;
							else {
								try {
									Thread.sleep(500);
								}
								catch (InterruptedException ie ) {}
							}
						}

	               p.destroy();
                  reader1.close();
						reader2.close();

                  if ( !execute ) 
                     break;

                  console.append("\n========================================================");
						console.append( "\n" );

					
               }  // for

               if ( execute ) {
	               report();
						console.append( "\n" + finish + "\n" + currentDir + "> \n" );
               } else {
                  console.append( "\n" + "The Process has been Terminated !!! \n" );                  
               }

				   console.append("\n---------------------------------------------------------------------------------------------------\n");	
					console.setCaretPosition(console.getText().length()); // Alternative method scrollBar.setValue(scrollBar.getMaximum());

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

      private void report() {
         if ( errorList.size() != 0 ) {
              console.append( "Following files failed to compile:\n" );
              for ( int i = 0; i < errorList.size(); i++ )
                 console.append( (String)errorList.get(i) + "\n" );
         } 
         else {
              console.append( "All files within project directory compiled successfully. \n" );
         }
			endTime = System.currentTimeMillis() - startTime;
			long lg = endTime/1000;
			String time = "Time taken: " + lg + "." + (endTime-lg*1000) + " seconds.";
			console.append( time + "\n" );
      }

		class Print implements Runnable {
			BufferedReader reader;
         String fileName;
			int ind;
			Print( BufferedReader reader, String fileName ) {
				this.reader = reader;
            this.fileName = fileName;
			}
			public void run() {
			   int i = 0;
				try {
					String msg;
					while ( (msg = reader.readLine()) != null) {
						i++;
						console.append( msg + "\n" ); 
						console.setCaretPosition(console.getText().length());
						int line = console.getLineCount()-1;
						if ( msg.indexOf( fileName ) != -1 ) 
							myUID.add( line );
					}
					lock = false;
				 } catch ( IOException e ) {
					e.printStackTrace();
				 }
             if ( i != 0 )
                 errorList.add( fileName );
			}
		}
	
}
