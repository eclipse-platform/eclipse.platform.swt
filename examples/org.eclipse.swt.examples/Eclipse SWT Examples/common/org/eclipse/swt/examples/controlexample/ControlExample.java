package org.eclipse.swt.examples.controlexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import java.util.ResourceBundle;

/**
* <code>ControlExample</code> is a simple demonstration
* of the controls defined by SWT.  It consists of a shell
* and tab folder where each tab in the folder allows the
* user to interact with a control.
*/
 
public class ControlExample {
	Shell shell;
	TabFolder tabFolder;
	ResourceBundle resControls = ResourceBundle.getBundle("examples_control");
/**
* Create a new example and open it.
* 
* @param args the command line arguments
*
*/
public static void main (String[] args) {
	new ControlExample ().open ();
}
/**
* Open the example.
*/
void open () {
	
	/* Load resources */
	Images.loadImages ();

	/* Create the shell */
	shell = new Shell ();
	shell.setText (resControls.getString("Control_Example"));
	shell.addControlListener (new ControlAdapter () {
		public void controlResized (ControlEvent e) {
			tabFolder.setBounds (shell.getClientArea ());
		}
	});

	/* Create the tab folder */
	ShellTab shellTab = new ShellTab ();
	tabFolder = new TabFolder (shell, SWT.NULL);
	Tab [] tabs = new Tab [] {
		new ButtonTab (),
		new ComboTab (),
		new DialogTab (),
		new LabelTab (),
		new ListTab (),
		new ProgressBarTab (),
		new SashTab (),
		shellTab,
		new SliderTab (),
		new TableTab (),
		new TextTab (),
		new ToolBarTab (),
		new TreeTab (),
	};
	for (int i=0; i<tabs.length; i++) {
		TabItem item = new TabItem (tabFolder, SWT.NULL);
	    item.setText (tabs [i].getTabText ());
	    item.setControl (tabs [i].createTabFolderPage (tabFolder));
	}

	/* Run the event loop */
	shell.open ();
	Display display = Display.getDefault ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}

	/*
	* Destroy any shells that may have been created
	* by the Shells tab.  When a shell is disposed,
	* all child shells are also disposed.  Therefore
	* it is necessary to check for disposed shells
	* in the shells list to avoid disposing a shell
	* twice.
	*/
	shellTab.closeAllShells ();
	
	/* Free resources */
	Images.freeImages ();
}
}
