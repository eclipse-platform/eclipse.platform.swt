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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class GradientTab extends OpenGLTab {
	private float xPos = 0.0f, yPos = 0.0f, zPos = -7.0f;
	private float xRot = 180.0f, yRot = 180.0f, zRot = 180.0f;
	private int currentSelection = 1;
	private final static float[] BEZIER_COLORS = {
		0.0f, 1.0f, 0.0f, 0.0f, 0.3f, 0.6f, 0.1f, 0.0f,
		0.8f, 0.2f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 1.0f, 0.0f, 0.8f, 0.8f, 0.8f, 0.0f
	};
	private final static float[] BEZIER_POINTS = {
		-1.5f, -1.5f, 4.0f, -0.5f, -1.5f, 2.0f, 0.5f, -1.5f,
		-1.0f, 1.5f, -1.5f, 2.0f, -1.5f, -0.5f, 1.0f, -0.5f,
		-0.5f, 3.0f, 0.5f, -0.5f, 0.0f, 1.5f, -0.5f, -1.0f,
		-1.5f, 0.5f, 4.0f, -0.5f, 0.5f, 0.0f, 0.5f, 0.5f,
		3.0f, 1.5f, 0.5f, 4.0f, -1.5f, 1.5f, -2.0f, -0.5f,
		1.5f, -2.0f, 0.5f, 1.5f, 0.0f, 1.5f, 1.5f, -1.0f,
	};
	private final static String[] OBJECT_NAMES = { "Bezier", "Square" };

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(Composite composite) {
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
				xPos = xMove.getSelection() - 5;
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
				yPos = yMove.getSelection() - 5;
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
				zPos = zMove.getSelection() - 12;
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
		xRotation.setSelection(180);
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
		yRotation.setSelection(180);
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
		zRotation.setSelection(180);
		zRotation.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				zRot = zRotation.getSelection();
			}
		});
		
		Composite objectGroup = new Composite(composite,SWT.NONE);
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		objectGroup.setLayout(layout);
		objectGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		new Label(objectGroup, SWT.NONE).setText("Object:");
		final Combo objectCombo = new Combo(objectGroup, SWT.READ_ONLY);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.grabExcessHorizontalSpace = true;
		objectCombo.setLayoutData(data);
		objectCombo.setItems(OBJECT_NAMES);
		objectCombo.select(0);
		objectCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				currentSelection = objectCombo.getSelectionIndex() + 1;
			}
		});
	}
	
	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GL.glDeleteLists(1, 2);
	}
	
	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Gradients";
	}
	
	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		GL.glMap2f(GL.GL_MAP2_VERTEX_3, 0, 1, 3, 4, 0, 1, 12, 4, BEZIER_POINTS);
		GL.glMap2f(GL.GL_MAP2_COLOR_4, 0, 1, 4, 1, 0, 1, 4, 6, BEZIER_COLORS);
		GL.glMapGrid2f(20, 0.0f, 1.0f, 20, 0.0f, 1.0f);
		GL.glShadeModel(GL.GL_SMOOTH);
		GL.glEnable(GL.GL_LINE_SMOOTH);
		GL.glEnable(GL.GL_LINE_STIPPLE);
		GL.glEnable(GL.GL_BLEND);
		GL.glEnable(GL.GL_MAP2_COLOR_4);
		GL.glEnable(GL.GL_MAP2_VERTEX_3);
		GL.glEnable(GL.GL_DEPTH_TEST);

		// create display lists
		GL.glNewList(1, GL.GL_COMPILE);
		GL.glEvalMesh2(GL.GL_FILL, 0, 20, 0, 20);
		GL.glEndList();
		GL.glNewList(2, GL.GL_COMPILE);
		GL.glBegin(GL.GL_TRIANGLE_FAN);
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(0.0f, 0.0f, 0.0f);
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(0.0f, 2.0f, 0.0f);
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(-2.0f, 2.0f, 0.0f);
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3f(-2.0f, 0.0f, 0.0f);
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(-2.0f, -2.0f, 0.0f);
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(0.0f, -2.0f, 0.0f);
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(2.0f, -2.0f, 0.0f);
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3f(2.0f, 0.0f, 0.0f);
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(2.0f, 2.0f, 0.0f);
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(0.0f, 2.0f, 0.0f);
		GL.glEnd();
		GL.glEndList();
	}
	
	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glTranslatef(xPos, yPos, zPos);
		GL.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		GL.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		GL.glRotatef(zRot, 0.0f, 0.0f, 1.0f);
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glCallList(currentSelection);
	}
}
