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

class TransparencyTab extends OpenGLTab {
	private float[] alphas = { 0.3f, 0.5f, 1.0f };
	private int quadratic;
	private int currentSelection = 1;
	private final static int
		INDEX_TRIANGLE = 1, INDEX_SPHERE = 2, INDEX_DISK = 3;
	private final static String[] OBJECTS = {"Triangle", "Sphere", "Disk" };

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(Composite composite) {
		Composite objectGroup = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		objectGroup.setLayout(layout);
		objectGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		new Label(objectGroup, SWT.NONE).setText("Object:");
		final Combo objectCombo = new Combo(objectGroup, SWT.READ_ONLY);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.grabExcessHorizontalSpace = true;
		objectCombo.setLayoutData(data);
		objectCombo.setItems(OBJECTS);
		objectCombo.select(0);
		
		new Label(composite, SWT.NONE).setText("Transparency:");
		final Slider transparencySlider = new Slider(composite, SWT.HORIZONTAL);
		transparencySlider.setValues(0, 0, 11, 1, 1, 2);
		transparencySlider.setSelection(7);
		transparencySlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				float alpha = transparencySlider.getSelection();
				alpha = 1.0f - alpha / 10;
				alphas[currentSelection - 1] = alpha;
			}
		});
		objectCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				currentSelection = objectCombo.getSelectionIndex() + 1;
				transparencySlider.setSelection(
					(int) ((1.0f - alphas[currentSelection - 1]) * 10));
			}
		});
	}

	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GLU.gluDeleteQuadric(quadratic);
		GL.glDeleteLists(INDEX_DISK, 1);
		GL.glDeleteLists(INDEX_SPHERE, 1);
		GL.glDeleteLists(INDEX_TRIANGLE, 1);
	}

	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Transparency";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		quadratic = GLU.gluNewQuadric();
		GLU.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
		GL.glEnable(GL.GL_CULL_FACE);
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glEnable(GL.GL_BLEND);
		// create display lists		
		GL.glNewList(INDEX_SPHERE, GL.GL_COMPILE);
		GLU.gluQuadricDrawStyle(quadratic, GLU.GLU_FILL);
		GLU.gluSphere(quadratic, 1.5, 32, 32);
		GL.glEndList();
		GL.glNewList(INDEX_DISK, GL.GL_COMPILE);
		GLU.gluQuadricDrawStyle(quadratic, GLU.GLU_FILL);
		GLU.gluDisk(quadratic, 1.0, 3, 32, 32);
		GL.glEndList();
		GL.glNewList(INDEX_TRIANGLE, GL.GL_COMPILE);
		GL.glBegin(GL.GL_TRIANGLES);
		GL.glVertex3f(0.0f, 2.0f, 0.0f);
		GL.glVertex3f(-2.0f, -2.0f, 2.0f);
		GL.glVertex3f(2.0f, -2.0f, 2.0f);
		GL.glEnd();
		GL.glEndList();
	}

	/**
	 * @see SelectionTab.processPick (int[], int)
	 */
	void processPick(int[] pSelectBuff, int hits) {
		int zDepth;
		int tempSelection = pSelectBuff[3];
		zDepth = pSelectBuff[2];
		// pick object with largest z value
		for (int i = 4; i <= 4 * (hits - 1); i = i + 4) {
			if (zDepth > pSelectBuff[i + 2]) {
				zDepth = pSelectBuff[i + 2];
				tempSelection = pSelectBuff[i + 3];
			}
		}
		if (tempSelection > 0) currentSelection = tempSelection;
	}

	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		// draw items with transparent qualities in reverse Z order
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glTranslatef(0.0f, 0.0f, -14.0f);
		GL.glPushName(0);

		// draw disk
		GL.glPushMatrix();
		GL.glTranslatef(-1.0f, 0.0f, 1.0f);
		GL.glColor4f(0.0f, 1.0f, 0.0f, alphas[2]);
		GL.glLoadName(INDEX_DISK);
		GL.glCallList(INDEX_DISK);
		if (currentSelection == INDEX_DISK) {
			GL.glColor3f(1.0f, 0.0f, 0.0f);
			GLU.gluQuadricDrawStyle(quadratic, GLU.GLU_LINE);
			GLU.gluDisk(quadratic, 0.98, 3.02, 32, 32);
		}
		GL.glPopMatrix();

		// draw triangle
		GL.glTranslatef(0.0f, 0.0f, +3.0f);
		GL.glLoadName(INDEX_TRIANGLE);
		GL.glColor4f(1.0f, 0.0f, 0.0f, alphas[0]);
		GL.glCallList(INDEX_TRIANGLE);
		GL.glPointSize(5.0f);
		if (currentSelection == INDEX_TRIANGLE) {
			GL.glBegin(GL.GL_POINTS);
			GL.glColor3f(1.0f, 0.0f, 0.0f);
			GL.glVertex3f(0.0f, 2.0f, 0.0f);
			GL.glVertex3f(-2.0f, -2.0f, 2.0f);
			GL.glVertex3f(2.0f, -2.0f, 2.0f);
			GL.glEnd();
		}

		// draw sphere
		GL.glPushMatrix();
		GL.glTranslatef(1.0f, 0.0f, +3.0f);
		GL.glColor4f(0.0f, 0.0f, 1.0f, alphas[1]);
		GL.glLoadName(INDEX_SPHERE);
		GL.glCallList(INDEX_SPHERE);
		if (currentSelection == INDEX_SPHERE) {
			GL.glColor3f(1.0f, 0.0f, 0.0f);
			GLU.gluQuadricDrawStyle(quadratic, GLU.GLU_LINE);
			GLU.gluSphere(quadratic, 1.51, 8, 8);
		}
		GL.glPopMatrix();
	}
}
