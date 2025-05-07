package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetToolbar {

	final static boolean USE_SET_SELECTION = true;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SnippetToolbar");
		ToolBar bar = new ToolBar(shell, SWT.BORDER);
		for (int i = 0; i < 8; i++) {
			ToolItem item = new ToolItem(bar, SWT.RADIO);
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ToolItem source = ((ToolItem) e.widget);
					System.out.println(
							"Selection event. item " + source.getText() + " is selected: " + source.getSelection());
				}
			});
			item.setText("Item " + i);
		}
		Rectangle clientArea = shell.getClientArea();
		bar.setLocation(clientArea.x, clientArea.y);
		bar.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
			if (USE_SET_SELECTION)
				bar.getItem((int) (Math.random() * bar.getItemCount())).setSelection(true);

		}
		display.dispose();
	}

}
