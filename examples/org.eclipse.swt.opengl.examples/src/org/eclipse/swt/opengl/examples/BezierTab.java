/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;

class BezierTab extends SelectionTab {
	private boolean showCtrlPoints = true;
	private Point offset;
	private int currentPoint = -1;
	private int lineDivisions = 30;
	private double[][] ctrlPts = {
		{	1.5, 0.5, 0.0, 0.6, 0.9, 0.0, 0.85, 0.12,
			0.0, 1.1, 1.0, 0.0, 0.53, 1.4, 0.0
		},
		{	0.53, 1.4, 0.0, 1.03, 1.87, 0.0, 1.52, 0.26,
			0.0, 1.86, 0.43, 0.0, 1.5, 0.5, 0.0
		}
	};
	private static final int LENGTH = 5;
	private static final int[][] PICK_NAMES = {
		{ 1, 2, 3, 4, 5 },
		{ 6, 7, 8, 9, 10 }
	};

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(Composite composite) {
		new Label(composite, SWT.NONE).setText("Click and drag points to adjust shape.");
		new Label(composite, SWT.NONE).setText("Line division count:");
		final Slider divisions = new Slider(composite, SWT.NONE);
		divisions.setIncrement(1);
		divisions.setMaximum(52);
		divisions.setMinimum(1);
		divisions.setThumb(2);
		divisions.setPageIncrement(2);
		divisions.setSelection(30);
		divisions.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				lineDivisions = divisions.getSelection();
			}
		});
		
		final Button showPointsButton = new Button(composite, SWT.CHECK);
		showPointsButton.setText("Show Points");
		showPointsButton.setSelection(true);
		showPointsButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				showCtrlPoints = showPointsButton.getSelection();
			}
		});
		
		final Button blendButton = new Button(composite, SWT.CHECK);
		blendButton.setText("Blend");
		blendButton.setSelection(true);
		blendButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (blendButton.getSelection()) {
					GL.glEnable(GL.GL_BLEND);
				} else {
					GL.glDisable(GL.GL_BLEND);
				}
			}
		});
		
		final Button smoothLineButton = new Button(composite, SWT.CHECK);
		smoothLineButton.setText("Smooth Line");
		smoothLineButton.setSelection(true);
		smoothLineButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (smoothLineButton.getSelection()) {
					GL.glEnable(GL.GL_LINE_SMOOTH);
				} else {
					GL.glDisable(GL.GL_LINE_SMOOTH);
				}
			}
		});
		
		final Canvas glCanvas = getGlCanvas();
		glCanvas.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				offset = null;
			}

		});
		
		glCanvas.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event e) {
				e.y = glCanvas.getClientArea().height - e.y;
				if (e.button == 1) {
					if (processSelection(e.x, e.y, 10) > 0) {
						offset = new Point(e.x, e.y);
					} else {
						currentPoint = 0;
					}
				}
			}
		});
		
		glCanvas.addListener(SWT.MouseMove, new Listener() {
			public void handleEvent(Event e) {
				if (offset == null) return;
				int currentSegment = (currentPoint - 1) / LENGTH;
				int current = (currentPoint - 1) * 3;
				if (currentPoint > LENGTH) {
					current = (currentPoint - 6) * 3;
				}
				Rectangle rect = glCanvas.getClientArea();
				e.y = rect.height - e.y;
				if (0 < e.x && e.x < rect.width && 0 < e.y && e.y < rect.height && currentPoint > 0) {
					ctrlPts[currentSegment][current] = (float) e.x / (float) 200;
					ctrlPts[currentSegment][current + 1] = (float) e.y / (float) 200;
					switch (currentPoint) {
						case 10 :
							ctrlPts[0][0] = (float) e.x / (float) 200;
							ctrlPts[0][1] = (float) e.y / (float) 200;
							break;
						case 6 :
							ctrlPts[0][12] = (float) e.x / (float) 200;
							ctrlPts[0][13] = (float) e.y / (float) 200;
							break;
					}
				}
			}
		});
	}
	
	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Bezier";
	}
	
	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL.glPointSize(7.0f);
		GL.glLineWidth(4.0f);

		GL.glEnable(GL.GL_AUTO_NORMAL);
		GL.glEnable(GL.GL_MAP1_VERTEX_3);
		GL.glEnable(GL.GL_LINE_SMOOTH);
		GL.glEnable(GL.GL_BLEND);
	}
	
	/**
	 * @see SelectionTab.processPick (int[], int)
	 */
	void processPick(int[] pSelectBuff, int hits) {
		int counter = 0;
		currentPoint = 0;
		for (int i = 0; i < hits; i++) {
			int count = pSelectBuff[counter];
			counter += 3;
			for (int j = 0; j < count; j++) {
				currentPoint = pSelectBuff[counter];
				counter++;
			}
		}
	}
	
	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glTranslatef(-1.0f, -1.0f, -2.45f);
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		for (int i = 0; i < ctrlPts.length; i++) {
			GL.glMapGrid1d(lineDivisions, 0.0f, 1f);
			GL.glMap1d(GL.GL_MAP1_VERTEX_3, 0, 1, 3, LENGTH, ctrlPts[i]);
			GL.glEvalMesh1(GL.GL_LINE, 0, lineDivisions);
		}
		if (showCtrlPoints) {
			GL.glPushName(0);
			for (int j = 0; j < ctrlPts.length; j++) {
				for (int i = 0; i < LENGTH * 3; i += 3) {
					GL.glLoadName(PICK_NAMES[j][i / 3]);
					GL.glBegin(GL.GL_POINTS);
					if (PICK_NAMES[j][i / 3] == currentPoint) {
						GL.glColor3f(1.0f, 0.0f, 0.0f);
					} else {
						GL.glColor3f(0.0f, 0.0f, 0.0f);
					}
					GL.glVertex3d(ctrlPts[j][i], ctrlPts[j][i + 1], ctrlPts[j][i + 2]);
					GL.glEnd();
				}
			}
		}
	}
}
