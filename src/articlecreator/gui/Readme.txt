	
======================================================================================
			MyEditor - Version 1.04  < Readme File >
======================================================================================


$$$ Samuel Huang. Last updated: 4/07/2002
$$$ Feel free to give me any feedback on MyEditor@slingshot.co.nz

* MyEditor is updated to run in j2sdk1.4.0. Using previous version of jdk will not work
  properly due to changes in java swing package.

* MyEditor is a lightweight pure java editor that is good enough for any java programmer 
  who doesn't need advanced features of commercial strength editor. i.e. macros recording,
  syntax highlighting, project management, plugin support... For those who are looking for
  an editor with those advanced features, see JEdit in Thanks.txt. 

* However MyEditor does provide some advanced features as below:
  
	- Compile or run java applications/applets at runtime by clicking buttons from the
	  tool bar.

	- MyEditor now offers project build facility

	- Display output of running programs in console windows.
	
	- Console window highlights lines declaring error in code automatically after 
	  compilation. A single click on the highlighted line will then jump to the line 
	  of the program where the error occurs.
	  
	- Highlight a source code line of interest by double clicking on it and come back to it
	  latter on by clicking in the JList labelled marked lines.

	- Customize current/default color, font of editor and more... ###

* Some features of previous version of MyEditor have been taken off due to constant
  changes of java Swing package that results in maintenance problem. Features taken
  off: 

	- Setting Look & Feel of editor dynamically or by default to mortif/system Look 
     & Feel.

	- Indent and Undent selected lines

* MyEditor offers no guarantee of whatsoever, use it at your own risk.

* MyEditor doesn't show an update of the file changed by another application.

* How to run MyEditor:
  
  Unzip MyEditor.zip to a directory. A directory named MyEditor should appear.
  Compile RemoveLineDocument.java first then MyEditor.java from the 'MyEditor'
  directory. Then simply call 'java MyEditor' from the command line (assuming
  you have set up the path to \jdk1.x\bin directory correctly).
   
* Download wheelmouse support package and install it for wheelmouse support. See
  the beginning of MyEditor.java for how. 

* Don't forget to read Tips.txt for a more efficient use of MyEditor

* ChangeLog.txt records a list of bug fixes and difference between different versions
  of MyEditor.

* Thanks.txt gives credits to a list of resources I used to write MyEditor.

 
Note: Feel free to reuse the code in MyEditor but please be kind enough to give credits
      and thanks to the original authors when appropriate. Some authors require that you 
      put a paragraph at the beginning of your code as in MyEditor.java.


