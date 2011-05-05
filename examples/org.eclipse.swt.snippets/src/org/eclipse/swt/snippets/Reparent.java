package org.eclipse.swt.snippets;

import org.eclipse.e4.ui.widgets.CTabFolder;
import org.eclipse.e4.ui.widgets.CTabItem;
import org.eclipse.e4.ui.workbench.renderers.swt.CTabRendering;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Reparent {
  public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout(1, false));
	shell.setText("Shell 1");
	
	CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
	for (int i = 0; i < 4; i++) {
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText("Item "+i);
		Text text = new Text(folder, SWT.MULTI);
		text.setText("Content for Item "+i);
		item.setControl(text);
	}
	CTabRendering renderer = new CTabRendering(folder);
	folder.setRenderer(renderer);
	
	final ToolBar bar = new ToolBar(folder, SWT.NONE);
	for (int i = 0; i < 5; i++) {
		ToolItem item = new ToolItem(bar, SWT.PUSH);
		item.setText("Item " + i);
	}
	folder.addTabControl(bar, SWT.TRAIL);

	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
