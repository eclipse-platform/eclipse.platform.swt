package org.eclipse.swt.examples.controls;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import java.util.ResourceBundle;

/**
 * <code>Controls</code> is a simple demonstration
 * of the controls defined by SWT.  It consists of a workbench
 * view and tab folder where each tab in the folder allows the
 * user to interact with a control.
 * 
 * @see ViewPart
 */
public class ControlView extends ViewPart {
	private ShellTab shellTab;
	private TabFolder tabFolder;
	private Composite mainFrame;

	/**
	 * The constructor.
	 */
	public ControlView() {
		ControlPlugin.initResources();
	}

	/**
	 * Create the example
	 * 
	 * @see ViewPart#createPartControl
	 */
	public void createPartControl(Composite frame) {
		this.mainFrame = frame;

		/* Create the tab folder */
		tabFolder = new TabFolder (frame, SWT.NULL);
		Tab [] tabs = new Tab [] {
			new ButtonTab (),
			new ComboTab (),
			new DialogTab (),
			new LabelTab (),
			new ListTab (),
			new ProgressBarTab (),
			new SashTab (),
			shellTab = new ShellTab(),
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

	}

	/**
	 * Called when we must grab focus.
	 * 
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus()  {
		//tabFolder.setFocus();
		mainFrame.setFocus();
	}

	/**
	 * Called when the View is to be disposed
	 */
	
	public void dispose() {
		/*
		 * Destroy any shells that may have been created
		 * by the Shells tab.  When a shell is disposed,
		 * all child shells are also disposed.  Therefore
		 * it is necessary to check for disposed shells
		 * in the shells list to avoid disposing a shell
		 * twice.
		 */
		if (shellTab != null) shellTab.closeAllShells ();
		shellTab = null;
		super.dispose();
	}
}
