package org.eclipse.swt.examples.hover;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */

import org.eclipse.swt.events.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import java.util.ResourceBundle;

/* This example demonstrates how to implement hover help feedback
 * using the MouseTrackListener.
 */
public class HoverHelpExample {
	private static ResourceBundle resHover = ResourceBundle.getBundle("examples_hover");

public static void main (String args []) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	GridLayout layout = new GridLayout();
	layout.numColumns = 3;
	shell.setLayout(layout);
	
	final Shell tip = new Shell (shell, SWT.NONE);
	tip.setLayout(new FillLayout());
	Label label = new Label (tip, SWT.NONE);
	label.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
	label.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
	
	shell.addHelpListener (new HelpListener () {
		public void helpRequested (HelpEvent event) {
			if (tip.isVisible ()) {
				tip.setVisible (false);
				Shell help = new Shell (shell, SWT.SHELL_TRIM);
				help.setLayout(new FillLayout());
				Label label = new Label (help, SWT.NONE);
				label.setText (resHover.getString("F1_help_for") + " " + tip.getData ("TIP_WIDGET"));
				help.pack ();
				help.open ();
			}
		}
	});
	
	ToolBar bar = new ToolBar (shell, SWT.BORDER);
	for (int i=0; i<5; i++) {
		ToolItem item = new ToolItem (bar, SWT.PUSH);
		item.setText (resHover.getString("Item") + " " + i);
		item.setData ("TIP_TEXT", item.getText ());
	}
	GridData gridData = new GridData();
	gridData.horizontalSpan = 3;
	bar.setLayoutData(gridData);
	setHoverHelp (bar, tip);

	Table table = new Table (shell, SWT.BORDER);
	for (int i=0; i<4; i++) {
		TableItem item = new TableItem (table, SWT.PUSH);
		item.setText (resHover.getString("Item") + " " + i);
		item.setData ("TIP_TEXT", item.getText ());
	}
	table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
	setHoverHelp (table, tip);
	
	Tree tree = new Tree (shell, SWT.BORDER);
	for (int i=0; i<4; i++) {
		TreeItem item = new TreeItem (tree, SWT.PUSH);
		item.setText (resHover.getString("Item") + " " + i);
		item.setData ("TIP_TEXT", item.getText ());
	}
	tree.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
	setHoverHelp (tree, tip);
	
	Button button = new Button (shell, SWT.PUSH);
	button.setText (resHover.getString("Hello"));
	button.setData ("TIP_TEXT", button.getText ());
	setHoverHelp (button, tip);
		
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
}

static void setHoverHelp (final Control control, final Shell tip) {
	control.addMouseListener (new MouseAdapter () {
		public void mouseDown (MouseEvent e) {
			if (tip.isVisible ()) tip.setVisible (false);
		}		
	});
	control.addMouseTrackListener (new MouseTrackAdapter () {
		public void mouseExit (MouseEvent e) {
			if (tip.isVisible ()) tip.setVisible (false);
			tip.setData ("TIP_WIDGET", null);
		}
		public void mouseHover (MouseEvent e) {
			Point pt = new Point (e.x, e.y);
			Widget widget = e.widget;
			if (widget instanceof ToolBar) {
				ToolBar w = (ToolBar) widget;
				widget = w.getItem (pt);
			}
			if (widget instanceof Table) {
				Table w = (Table) widget;
				widget = w.getItem (pt);
			}
			if (widget instanceof Tree) {
				Tree w = (Tree) widget;
				widget = w.getItem (pt);
			}
			if (widget == null) {
				tip.setVisible (false);
				tip.setData ("TIP_WIDGET", null);
				return;
			}
			if (widget == tip.getData ("TIP_WIDGET")) return;
			tip.setData ("TIP_WIDGET", widget);
			pt.y += 16;
			pt = control.toDisplay (pt);
			tip.setLocation (pt);
			Label label = (Label) (tip.getChildren () [0]);
			String text = (String) widget.getData ("TIP_TEXT");
			if (text != null) label.setText (text);  
			tip.pack ();
			tip.setVisible (true);
		}
	});
}

}