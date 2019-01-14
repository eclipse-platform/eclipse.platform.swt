package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug542940_NPETreeSetEditor {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Text Tree Editor");
		shell.setLayout(new FillLayout());

		final Tree tree = new Tree(shell, SWT.SINGLE);
		for (int i = 0; i < 3; i++) {
			TreeItem iItem = new TreeItem(tree, SWT.NONE);
			iItem.setText("Item " + (i + 1));
			for (int j = 0; j < 3; j++) {
				TreeItem jItem = new TreeItem(iItem, SWT.NONE);
				jItem.setText("Sub Item " + (j + 1));
				for (int k = 0; k < 3; k++) {
					new TreeItem(jItem, SWT.NONE).setText("Sub Sub Item " + (k + 1));
				}
				jItem.setExpanded(true);
			}
			iItem.setExpanded(true);
		}

		final TreeEditor editor = new TreeEditor(tree);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		tree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.F2 && tree.getSelectionCount() == 1) {
					final TreeItem item = tree.getSelection()[0];

					final Text text = new Text(tree, SWT.NONE);
//					text.setBounds(0, 0, 1, 1);
					text.setText(item.getText());
					text.selectAll();
					text.setFocus();

					text.addFocusListener(new FocusAdapter() {
						@Override
						public void focusLost(FocusEvent event) {
							item.setText(text.getText());
							text.dispose();
						}
					});

					text.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent event) {
							switch (event.keyCode) {
							case SWT.CR:
								item.setText(text.getText());
							case SWT.ESC:
								text.dispose();
								break;
							}
						}
					});
					editor.setEditor(text, item);
				}
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}