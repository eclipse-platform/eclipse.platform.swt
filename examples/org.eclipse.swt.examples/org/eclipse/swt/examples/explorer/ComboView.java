package org.eclipse.swt.examples.explorer;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;import org.eclipse.swt.events.*;import org.eclipse.swt.widgets.*;import java.io.*;

public class ComboView {
	/* package */ final Combo combo;
	private final FileViewer viewer;
	
	private File[] roots = null;
	private String lastText = null;

	/**
	 * Creates the combo box.
	 * 
	 * @param theViewer the viewer to attach to
	 * @param parent the parent control
	 * @param layoutData the layout data
	 */
	public ComboView(FileViewer theViewer, Composite parent, Object layoutData) {
		this.viewer = theViewer;

		combo = new Combo(parent, SWT.NONE);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = combo.getSelectionIndex();
				if (selection >= 0 && selection < roots.length) {
					viewer.notifySelectedDirectory(roots[selection]);
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				String text = combo.getText();
				if (text == null) return;
				if (lastText != null && lastText.equals(text)) return;
				lastText = text;
				viewer.notifySelectedDirectory(new File(text));
			}
		});
		combo.setLayoutData(layoutData);
	}

	/**
	 * Listens to selectedDirectory events.
	 * <p>
	 * Sets the combo box to point to the current directory.
	 * </p>
	 * 
	 * @param dir the directory that was selected, null is not permitted
	 */
	/* package */ void selectedDirectory(File dir) {
		combo.clearSelection();
		if (roots == null) return;
		
		for (int i = 0; i < roots.length; ++i) {
			if (dir.equals(roots[i])) {
				combo.select(i);
				return;
			}
		}
		combo.setText(dir.getPath());
	}

	/**
	 * Listens to refreshFiles events.
	 * <p>
	 * Refreshes information about any files in the list and their children.
	 * </p>
	 * 
	 * @param files the list of files to be refreshed, null refreshes everything
	 */
	/* package */ void refreshFiles(File[] files) {
		if (files == null) {
			roots = viewer.getRoots();

			combo.removeAll();
			for (int i = 0; i < roots.length; ++i) {
				final File file = roots[i];
				combo.add(file.getPath());
			}
		}
	}	
}
