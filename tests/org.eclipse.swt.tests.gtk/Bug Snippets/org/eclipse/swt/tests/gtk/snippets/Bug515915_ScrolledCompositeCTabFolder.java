package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 515915 - [GTK3] ScrolledComposite within CTabFolder does not show scrollbars until resize
 * How to run: launch snippet and observe scrollbar area
 * Bug description: The scrollbar does not appear until the window is resized
 * Expected results: The scrollbar appears immediately when the snippet runs
 * GTK Version(s): GTK3
 */
public class Bug515915_ScrolledCompositeCTabFolder
{
	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(100, 150);

		CTabFolder tabFolder = new CTabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("Tab");

		ScrolledComposite scroller = new ScrolledComposite(tabFolder, SWT.V_SCROLL | SWT.H_SCROLL);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);
		tabItem.setControl(scroller);

		Label label = new Label(scroller, SWT.NONE);
		label.setText("Label\n with\n a\n tall\n text\n string\n inside\n of\n it.");
		label.pack();

		scroller.setContent(label);
		scroller.setMinSize(label.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		shell.open();

		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}

		display.dispose();
	}
}