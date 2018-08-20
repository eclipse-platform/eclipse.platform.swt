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

class FogTab extends OpenGLTab {

	private float rotY = 0.0f;
	private float yPos = 0.0f, xPos = 0.0f, zPos = -15.0f;
	private int cubeListIndexBase;
	private final static int[] FOG_TYPES = { GL.GL_LINEAR, GL.GL_EXP, GL.GL_EXP2 };
	private final static String[] FOG_NAMES = { "LINEAR", "GL_EXP", "GL_EXP2" };
	private final static int SLEEP_LENGTH = 0;

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
		zMove.setMaximum(24);
		zMove.setMinimum(0);
		zMove.setThumb(4);
		zMove.setPageIncrement(2);
		zMove.setSelection(10);
		zMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				zPos = zMove.getSelection() - 25;
			}
		});

		Composite fogTypesGroup = new Composite(composite,SWT.NONE);
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		fogTypesGroup.setLayout(layout);
		fogTypesGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		new Label(fogTypesGroup, SWT.NONE).setText("Fog Types:");
		final Combo fogTypeCombo = new Combo(fogTypesGroup, SWT.READ_ONLY);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.grabExcessHorizontalSpace = true;
		fogTypeCombo.setLayoutData(data);
		fogTypeCombo.setItems(FOG_NAMES);
		fogTypeCombo.select(0);

		new Label(composite, SWT.NONE).setText("Fog Density:");
		final Slider fogDensitySlider = new Slider(composite, SWT.NONE);
		fogDensitySlider.setIncrement(1);
		fogDensitySlider.setMaximum(32);
		fogDensitySlider.setMinimum(0);
		fogDensitySlider.setThumb(2);
		fogDensitySlider.setPageIncrement(5);
		fogDensitySlider.setSelection(0);
		fogDensitySlider.setEnabled(false);
		fogDensitySlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				float fogDensity = ((float)fogDensitySlider.getSelection()) / 100;
				GL.glFogf(GL.GL_FOG_DENSITY, fogDensity);
			}
		});
		fogTypeCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int currentSelection = fogTypeCombo.getSelectionIndex();
				// fog type GL.GL_LINEAR does not utilize fogDensity, but the other fog types do
				fogDensitySlider.setEnabled(currentSelection != 0);
				GL.glFogf(GL.GL_FOG_MODE, FOG_TYPES[currentSelection]);
			}
		});
	}

	/**
	 * Creates a cube at 0,0 in the viewport.
	 */
 	void createCube() {
		GL.glNewList(cubeListIndexBase, GL.GL_COMPILE);
		GL.glBegin(GL.GL_QUADS);
		// front
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(-0.5f, -0.5f, 0.5f);	// bottom left 
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3f(0.5f, -0.5f, 0.5f);	// bottom right
		GL.glColor3f(1.0f, 1.0f, 0.0f);
		GL.glVertex3f(0.5f, 0.5f, 0.5f);	// top right
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(-0.5f, 0.5f, 0.5f);	// top left
		// back
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3f(-0.5f, -0.5f, -0.5f);	// bottom left 
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(0.5f, -0.5f, -0.5f);	// bottom right
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(0.5f, 0.5f, -0.5f);	// top right
		GL.glColor3f(1.0f, 1.0f, 0.0f);
		GL.glVertex3f(-0.5f, 0.5f, -0.5f);	// top left
		// left
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3f(-0.5f, -0.5f, -0.5f);	// bottom left
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(-0.5f, -0.5f, 0.5f);	// bottom right
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(-0.5f, 0.5f, 0.5f);	// top right
		GL.glColor3f(1.0f, 1.0f, 0.0f);
		GL.glVertex3f(-0.5f, 0.5f, -0.5f);	// top left	
		// right
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3f(0.5f, -0.5f, 0.5f);	// bottom left
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(0.5f, -0.5f, -0.5f);	// bottom right
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(0.5f, 0.5f, -0.5f);	// top right
		GL.glColor3f(1.0f, 1.0f, 0.0f);
		GL.glVertex3f(0.5f, 0.5f, 0.5f);	// top left
		// top
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(0.5f, 0.5f, -0.5f);
		GL.glColor3f(1.0f, 1.0f, 0.0f);
		GL.glVertex3f(-0.5f, 0.5f, -0.5f);
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(-0.5f, 0.5f, 0.5f);
		GL.glColor3f(1.0f, 1.0f, 0.0f);
		GL.glVertex3f(0.5f, 0.5f, 0.5f);
		// bottom
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3f(0.5f, -0.5f, 0.5f);
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(-0.5f, -0.5f, 0.5f);
		GL.glColor3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3f(-0.5f, -0.5f, -0.5f);
		GL.glColor3f(0.0f, 1.0f, 0.0f);
		GL.glVertex3f(0.5f, -0.5f, -0.5f);
		GL.glEnd();
		GL.glEndList();
	}

	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GL.glDeleteLists(cubeListIndexBase, 1);
	}

	/**
	 * @see OpenGLTab#getSleepLength()
	 */
	int getSleepLength() {
		return SLEEP_LENGTH;	
	}
	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Fog";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		// fog color should be the same as the clear color
		// to look appropriate
		float[] fogColor = { 1.0f, 1.0f, 1.0f, 1.0f };
		GL.glFogfv(GL.GL_FOG_COLOR, fogColor);
		GL.glHint(GL.GL_FOG_HINT, GL.GL_DONT_CARE);
		GL.glFogf(GL.GL_FOG_START, 0);
		GL.glFogf(GL.GL_FOG_DENSITY, 0.0f);
		// set the end of the start distance; anything > 15
		// units from the camera will be covered in fog
		GL.glFogf(GL.GL_FOG_END, 15);
		GL.glFogf(GL.GL_FOG_MODE, FOG_TYPES[0]);
		GL.glEnable(GL.GL_FOG);
		GL.glEnable(GL.GL_DEPTH_TEST);

		cubeListIndexBase = GL.glGenLists(1);
		createCube();
	}

	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glTranslatef(xPos, yPos, zPos);
		GL.glRotatef(rotY, 0.0f, 1.0f, 0.0f);

		GL.glCallList(cubeListIndexBase);

		GL.glPushMatrix();
		GL.glTranslatef(3, 0, -3);
		GL.glCallList(cubeListIndexBase);
		GL.glPopMatrix();

		GL.glPushMatrix();
		GL.glTranslatef(-3, 0, -3);
		GL.glCallList(cubeListIndexBase);
		GL.glPopMatrix();

		GL.glPushMatrix();
		GL.glTranslatef(0, 0, 4);
		GL.glCallList(cubeListIndexBase);
		GL.glPopMatrix();

		rotY += 0.6f;
	}
}
