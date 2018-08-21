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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;

public class Bug218935_SashFlicker {
	private class CursorListener implements MouseMoveListener, MouseTrackListener {	
		private Bug218935_SashFlicker main;
	
		public CursorListener(Bug218935_SashFlicker main) {
			this.main = main;
		}
	
		@Override
		public void mouseMove(MouseEvent e) {
			main.setCursor(e.x);
		}
	
		@Override
		public void mouseEnter(MouseEvent e) {
			main.setVisible(true);
		}
	
		@Override
		public void mouseExit(MouseEvent e) {
			main.setVisible(false);
		}
	
		@Override
		public void mouseHover(MouseEvent e) {			
		}
	}
	
	public Bug218935_SashFlicker() {
		final Display display = new Display();
		final Shell shell = new Shell(display);	
		shell.setBounds(10, 10, 400, 400);
		shell.setText("GTK Sash flicker");
		shell.setLayout(new FillLayout());
		
		Composite composite = new Composite(shell, SWT.NO_BACKGROUND);
		composite.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
		
		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		
		composite.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.fillRectangle(e.x, e.y, e.width, e.height);
			}
		});
		
		
		Canvas canvas = new Canvas(composite, SWT.None);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.fillRectangle(e.x, e.y, e.width, e.height);
			}			
		});
		
		Sash sash = new Sash(composite, SWT.NO_BACKGROUND);
		sash.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		
		final GridData sashData = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1);
		sashData.heightHint = 40;
		sash.setLayoutData(sashData);
		
		sash.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				System.err.println(e);
				e.gc.fillRectangle(e.x, e.y, e.width, e.height);
			}
		});
		
		createCursor(composite);
		
		CursorListener cursorListener = new CursorListener(this);
		canvas.addMouseMoveListener(cursorListener);
		canvas.addMouseTrackListener(cursorListener);
		
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Bug218935_SashFlicker();
	}
	
	private Canvas cursorLine;

	private void createCursor(Composite parent) {
		cursorLine = new Canvas(parent, SWT.NO_BACKGROUND);
		cursorLine.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
	
		final GridData cursorLineData = new GridData();
		cursorLineData.exclude = true;
		cursorLine.setLayoutData(cursorLineData);
	
		cursorLine.setSize(1, Integer.MAX_VALUE);
		cursorLine.setVisible(false);
		cursorLine.setEnabled(false);
		
		cursorLine.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Canvas w = ((Canvas)e.widget);
				Point p = w.getSize();
				e.gc.drawRectangle(0, 0, p.x, p.y);
			}
		});
	}


	void setVisible(boolean visible) {
		cursorLine.setVisible(visible);
	
		if (visible) {
			//Place the cursor above all widgets
			cursorLine.moveAbove(null);
		}
	}


	void setCursor(int x) {
		cursorLine.setLocation(x, 0);
		cursorLine.getParent().update();
	}
}