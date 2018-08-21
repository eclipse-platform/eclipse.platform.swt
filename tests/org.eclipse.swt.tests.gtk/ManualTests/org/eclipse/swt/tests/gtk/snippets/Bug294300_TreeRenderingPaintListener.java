/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug294300_TreeRenderingPaintListener implements Listener {

	private Tree tree;
	private TextLayout textLayout;

	Bug294300_TreeRenderingPaintListener(Tree tree) {
		this.tree = tree;
	}

	public void setListeners(boolean add) {
		if (add) {
			tree.addListener(SWT.EraseItem, this);
			tree.addListener(SWT.PaintItem, this);
		} else {
			tree.removeListener(SWT.EraseItem, this);
			tree.removeListener(SWT.PaintItem, this);
		}
		redraw();
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.PaintItem:
			paint(event);
			break;
		case SWT.EraseItem:
			erase(event);
			break;
		}
	}

	private void erase(Event event) {
		event.detail &= ~(SWT.BACKGROUND | SWT.FOREGROUND | SWT.SELECTED | SWT.HOT);
	}

	private void paint(Event event) {
		TreeItem item = (TreeItem) event.item;
		GC gc = event.gc;
		// remember colors to restore the GC later
		Color oldForeground = gc.getForeground();
		Color oldBackground = gc.getBackground();

		int index = event.index;

		Color foreground = item.getForeground(index);
		if (foreground != null) {
			gc.setForeground(foreground);
		}

		Color background = item.getBackground(index);
		if (background != null) {
			gc.setBackground(background);
		}

		if ((event.detail & SWT.SELECTED) != 0) {
			gc.fillRectangle(item.getBounds(index));
		}

		Image image = item.getImage(index);
		if (image != null) {
			Rectangle imageBounds = item.getImageBounds(index);
			if (imageBounds != null) {
				Rectangle bounds = image.getBounds();

				// center the graphics.image in the given space
				int x = imageBounds.x
						+ Math.max(0, (imageBounds.width - bounds.width) / 2);
				int y = imageBounds.y
						+ Math.max(0, (imageBounds.height - bounds.height) / 2);
				gc.drawImage(image, x, y);
			}
		}

		Rectangle textBounds = item.getTextBounds(index);
		if (textBounds != null) {
			TextLayout layout = getTextLayout();
			layout.setText(item.getText(index));
			layout.setFont(item.getFont(index));

			Rectangle layoutBounds = layout.getBounds();

			int x = textBounds.x;
			int avg = (textBounds.height - layoutBounds.height) / 2;
			int y = textBounds.y + Math.max(0, avg);

			layout.draw(gc, x, y);
		}

		gc.setForeground(oldForeground);
		gc.setBackground(oldBackground);
	}

	public void redraw() {
		Rectangle rect = tree.getClientArea();
		tree.redraw(rect.x, rect.y, rect.width, rect.height, true);
	}

	private TextLayout getTextLayout() {
		if (textLayout == null) {
			int orientation = tree.getStyle()
					& (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
			textLayout = new TextLayout(tree.getDisplay());
			textLayout.setOrientation(orientation);
		} else {
			textLayout.setText("");
		}
		return textLayout;
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(10, 10, 800, 600);
		shell.setLayout(new GridLayout());
		final Tree tree = new Tree(shell, SWT.BORDER);
		tree.setLinesVisible(true);
		for (int i = 0; i < 5; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText("item " + i);
			for (int j = 0; j < 5; j++) {
				TreeItem child = new TreeItem(item, SWT.NONE);
				child.setText("item " + i + "-" + j);
			}
		}
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Bug294300_TreeRenderingPaintListener painer = new Bug294300_TreeRenderingPaintListener(tree);
		final Button button = new Button(shell, SWT.CHECK);
		button.setText("Enable custom painter");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				painer.setListeners(button.getSelection());
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}