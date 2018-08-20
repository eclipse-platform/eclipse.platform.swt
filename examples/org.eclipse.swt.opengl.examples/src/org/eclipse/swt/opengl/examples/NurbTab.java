/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;


import org.eclipse.opengl.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class NurbTab extends OpenGLTab {
	private float xPos = -5.0f, yPos = -5.0f, zPos = -25.0f;
	private float xRot = 330.0f, yRot = 0.0f, zRot = 0.0f;
	private int nurb;

	/**
	 * Returns a 3 dimensional array to store vertex points for the nurb
	 * surface.
	 * 
	 * @return the array
	 */
	float[][][] buildNurb() {
		float[][][] ctrl = new float[4][4][3];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// set the x value
				ctrl[i][j][0] = 2 * (i + (float) Math.cos(i + j));
				// set the y value
				ctrl[i][j][1] = 2 * (j + (float) Math.cos(i + j));
				// set the z value
				if ((i == 1 && j == 1) || (i == 2 && j == 2)) {
					// this makes the hill
					ctrl[i][j][2] = 6.0f;
				} else {
					ctrl[i][j][2] = 0.0f;
				}
			}
		}
		// sets this particular z value to a large number to make a high
		// hill
		ctrl[2][2][2] = 8.0f;

		return ctrl;
	}
	
	/**
	 * Returns a 1 dimensional array representing the 3 dimensional
	 * argument.  This result can be passed to the nurb renderer
	 * 
	 * @param ctrl source 3D array
	 * @return 1D array
	 */
	float[] convert(float[][][] ctrl) {
		float[] pts =
			new float[ctrl.length * ctrl[0].length * ctrl[0][0].length];
		int row = 0;
		int col = 0;
		// set rowIncr to number of columns * depth 
		int rowIncr = ctrl[0].length * ctrl[0][0].length;
		// set colIncr to depth 
		int colIncr = ctrl[0][0].length;
		// loop through rows 
		for (int i = 0; i < ctrl.length; i++) {
			col = 0;
			// loop through columns
			for (int j = 0; j < ctrl[0].length; j++) {
				// loop through to get the depth values
				for (int k = 0; k < ctrl[0][0].length; k++) {
					pts[row + col + k] = ctrl[i][j][k];
				}
				col += colIncr;
			}
			row += rowIncr;
		}
		return pts;
	}
	
	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(final Composite composite) {
		Group movementGroup = new Group(composite, SWT.NONE);
		movementGroup.setText("Translation");
		movementGroup.setLayout(new GridLayout(2, false));

		new Label(movementGroup, SWT.NONE).setText("X:");
		final Slider xMove = new Slider(movementGroup, SWT.NONE);
		xMove.setIncrement(1);
		xMove.setMaximum(12);
		xMove.setMinimum(0);
		xMove.setThumb(2);
		xMove.setPageIncrement(2);
		xMove.setSelection(5);
		xMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				xPos = xMove.getSelection() - 10;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Y:");
		final Slider yMove = new Slider(movementGroup, SWT.NONE);
		yMove.setIncrement(1);
		yMove.setMaximum(12);
		yMove.setMinimum(0);
		yMove.setThumb(2);
		yMove.setPageIncrement(2);
		yMove.setSelection(5);
		yMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				yPos = yMove.getSelection() - 10;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Z:");
		final Slider zMove = new Slider(movementGroup, SWT.NONE);
		zMove.setIncrement(1);
		zMove.setMaximum(12);
		zMove.setMinimum(0);
		zMove.setThumb(2);
		zMove.setPageIncrement(2);
		zMove.setSelection(5);
		zMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				zPos = zMove.getSelection() - 30;
			}
		});

		Group rotationGroup = new Group(composite, SWT.NONE);
		rotationGroup.setText("Rotation");
		rotationGroup.setLayout(new GridLayout(2, false));

		new Label(rotationGroup, SWT.NONE).setText("X:");
		final Slider xRotation = new Slider(rotationGroup, SWT.NONE);
		xRotation.setIncrement(10);
		xRotation.setMaximum(362);
		xRotation.setMinimum(0);
		xRotation.setThumb(2);
		xRotation.setPageIncrement(20);
		xRotation.setSelection(330);
		xRotation.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				xRot = xRotation.getSelection();
			}
		});

		new Label(rotationGroup, SWT.NONE).setText("Y:");
		final Slider yRotation = new Slider(rotationGroup, SWT.NONE);
		yRotation.setIncrement(10);
		yRotation.setMaximum(362);
		yRotation.setMinimum(0);
		yRotation.setThumb(2);
		yRotation.setPageIncrement(20);
		yRotation.setSelection(0);
		yRotation.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				yRot = yRotation.getSelection();
			}
		});

		new Label(rotationGroup, SWT.NONE).setText("Z:");
		final Slider zRotation = new Slider(rotationGroup, SWT.NONE);
		zRotation.setIncrement(10);
		zRotation.setMaximum(362);
		zRotation.setMinimum(0);
		zRotation.setThumb(2);
		zRotation.setPageIncrement(20);
		zRotation.setSelection(0);
		zRotation.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				zRot = zRotation.getSelection();
			}
		});

		new Label(composite, SWT.NONE).setText("Sampling Tolerance:");
		final Slider sSlider = new Slider(composite, SWT.NONE);
		sSlider.setIncrement(1);
		sSlider.setMaximum(101);
		sSlider.setMinimum(0);
		sSlider.setThumb(2);
		sSlider.setPageIncrement(2);
		sSlider.setSelection(75);
		sSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				GLU.gluNurbsProperty(
					nurb,
					GLU.GLU_SAMPLING_TOLERANCE,
					sSlider.getSelection() + 1);
			}
		});

		final Button fillButton = new Button(composite, SWT.CHECK);
		fillButton.setText("Fill");
		fillButton.setSelection(true);
		fillButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (fillButton.getSelection()) {
					GLU.gluNurbsProperty(
						nurb,
						GLU.GLU_DISPLAY_MODE,
						GLU.GLU_FILL);
				} else {
					GLU.gluNurbsProperty(
						nurb,
						GLU.GLU_DISPLAY_MODE,
						GLU.GLU_OUTLINE_POLYGON);
				}
			}
		});
		
		ColorSelectionGroup colorGroup =
			new ColorSelectionGroup(composite, SWT.NONE);
		colorGroup.setText("Foreground color");
		colorGroup.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				GL.glColor3ub((byte) rgb.red, (byte) rgb.green, (byte) rgb.blue);
			}
		});
	}
	
	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GLU.gluDeleteNurbsRenderer(nurb);
	}
	
	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Nurb";
	}
	
	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		// set up material color which helps show curves
		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float mat_shininess[] = { 100.0f };
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular);
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, mat_shininess);
		GL.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
		
		GL.glEnable(GL.GL_LIGHTING);
		GL.glEnable(GL.GL_LIGHT0);
		GL.glEnable(GL.GL_COLOR_MATERIAL);
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glEnable(GL.GL_AUTO_NORMAL);
		GL.glEnable(GL.GL_NORMALIZE);
		
		nurb = GLU.gluNewNurbsRenderer();

		GLU.gluNurbsProperty(
			nurb,
			GLU.GLU_SAMPLING_METHOD,
			GLU.GLU_PATH_LENGTH);
		// sampling tolerance is specific for GLU.GLU_PATH_LENGTH
		GLU.gluNurbsProperty(nurb, GLU.GLU_SAMPLING_TOLERANCE, 75.0f);
		GLU.gluNurbsProperty(nurb, GLU.GLU_DISPLAY_MODE, GLU.GLU_FILL);
	}
	
	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();
		// do translation and rotation
		GL.glTranslatef(xPos, yPos, zPos);
		GL.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		GL.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		GL.glRotatef(zRot, 0.0f, 0.0f, 1.0f);
		// define nurbs surface
		float[] knots = { 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f };
		GLU.gluBeginSurface(nurb);
		GLU.gluNurbsSurface(
			nurb, 8, knots, 8, knots, 12, 3,
			convert(buildNurb()), 4, 4, GL.GL_MAP2_VERTEX_3);
		GLU.gluEndSurface(nurb);
	}
}
