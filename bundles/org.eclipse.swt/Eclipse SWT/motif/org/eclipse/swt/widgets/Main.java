package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class Main {
	public static void main(String[] args) {
		Display display = new Display();
		Image image = new Image(display, "/opt/smiley.ico");
		Shell shell = new Shell(display);
		shell.setBounds(10,10,200,200);
		ToolBar bar = new ToolBar(shell, SWT.FLAT | SWT.BORDER);
		bar.setBounds(10,10,150,50);
		final ToolItem item = new ToolItem(bar, SWT.CHECK);
		item.setImage(image);
		item.setSelection(true);
		item.setEnabled(false);
		final ToolItem item2 = new ToolItem(bar, SWT.CHECK);
		item2.setImage(image);
		item2.setEnabled(false);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		image.dispose();
		display.dispose();
	}
}
