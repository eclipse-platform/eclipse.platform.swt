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

class StencilTab extends OpenGLTab {
	private abstract class Shape {
		private String name;
		/**
		 * Constructor
		 * 
		 * @param name the shape's name
		 */
		Shape(String name) {
			super();
			this.name = name;
		}
		/**
		 * Draws this shape.
		 */
		abstract void draw();
		/**
		 * Returns the name.
		 * 
		 * @return String
		 */
		String getName() {
			return name;
		}
	}

	private Shape[] shapes = new Shape[5];
	private Shape currentShape;
	private float xPos = 0, yPos = 0;
	private float size = 0.45f;
	private int texture;
	private int quadratic;
	private final static String IMAGE = "images/splash.bmp";
	private final static int SLEEP_LENGTH = 50;

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(Composite composite) {
		Composite controls = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		controls.setLayout(layout);
		new Label(controls, SWT.NONE).setText("Object:");
		final Combo shapeCombo = new Combo(controls, SWT.READ_ONLY);
		for (int i = 0; i < shapes.length; i++) {
			shapeCombo.add(shapes[i].getName());
		}
		shapeCombo.select(0);
		shapeCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				currentShape = shapes[shapeCombo.getSelectionIndex()];
			}
		});

		new Label(composite, SWT.NONE).setText("Size:");
		final Slider sizeSlider = new Slider(composite, SWT.HORIZONTAL);
		sizeSlider.setValues(0, 15, 75, 5, 5, 10);
		sizeSlider.setSelection(45);
		sizeSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				size = ((float) sizeSlider.getSelection()) / 100;
			}
		});
	}

	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GLU.gluDeleteQuadric(quadratic);
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
		return "Stencil";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		if (!hasStencilSupport()) return;
		
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glClearDepth(1.0f);
		int[] textureOut = new int[1];
		GL.glGenTextures(1, textureOut);
		loadTexture(getGlCanvas(), IMAGE, 0, textureOut);
		this.texture = textureOut[0];
		quadratic = GLU.gluNewQuadric();
		GLU.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
		GL.glDepthFunc(GL.GL_LEQUAL);
		GL.glEnable(GL.GL_STENCIL_TEST);
		GL.glEnable(GL.GL_TEXTURE_2D);
		GL.glEnable(GL.GL_DEPTH_TEST);
		// create shapes
		shapes[0] = new Shape("Triangle") {
			public void draw() {
				GL.glBegin(GL.GL_TRIANGLES);
				GL.glVertex3f(0.0f, size, 0.0f);
				GL.glVertex3f(-size, -size, 0.0f);
				GL.glVertex3f(size, -size, 0.0f);
				GL.glEnd();
			}
		};

		shapes[1] = new Shape("Disk") {
			public void draw() {
				GLU.gluDisk(quadratic, 0.0f, size, 32, 32);
			}
		};

		shapes[2] = new Shape("Square") {
			public void draw() {
				GL.glRectf(-size, -size, size, size);
			}
		};

		shapes[3] = new Shape("Hour Glass") {
			public void draw() {
				GL.glBegin(GL.GL_TRIANGLES);
				GL.glVertex3f(0.0f, size, 0.0f);
				GL.glVertex3f(-size, -size, 0.0f);
				GL.glVertex3f(size, -size, 0.0f);
				GL.glVertex3f(0.0f, -size, 0.0f);
				GL.glVertex3f(size, size, 0.0f);
				GL.glVertex3f(-size, size, 0.0f);
				GL.glEnd();
			}
		};

		shapes[4] = new Shape("Star") {
			public void draw() {
				GL.glBegin(GL.GL_TRIANGLES);
				GL.glVertex3f(-0.3f, 0, 0.0f);
				GL.glVertex3f(2 * size - 0.3f, 0, 0.0f);
				GL.glVertex3f(size - 0.3f, 2 * size * 0.85f, 0.0f);
				GL.glVertex3f(2 * size - 0.3f, size, 0.0f);
				GL.glVertex3f(0 - 0.3f, size, 0.0f);
				GL.glVertex3f(size - 0.3f, -size * 0.85f, 0.0f);
				GL.glEnd();
			}
		};

		currentShape = shapes[0];
	}

	/**
	 * @see OpenGLTab#isStencilSupportNeeded
	 */
	boolean isStencilSupportNeeded() {
		return true;
	}

	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(
			GL.GL_COLOR_BUFFER_BIT
				| GL.GL_DEPTH_BUFFER_BIT
				| GL.GL_STENCIL_BUFFER_BIT);

		GL.glLoadIdentity();
		GL.glTranslatef(
			(float) (1.5 * Math.cos(xPos)),
			(float) (1.0 * Math.sin(yPos)),
			-3.0f);
		xPos += 0.15f;
		yPos += 0.3f;

		GL.glColorMask(false, false, false, false);
		GL.glStencilFunc(GL.GL_ALWAYS, 1, 1);
		GL.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);
		GL.glDisable(GL.GL_DEPTH_TEST);
		GL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		currentShape.draw();

		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glColorMask(true, true, true, true);
		GL.glStencilFunc(GL.GL_EQUAL, 1, 1);
		GL.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);

		GL.glLoadIdentity();
		// load the texture behind the stencil object
		GL.glTranslatef(0.0f, 0.0f, -3.1f);
		GL.glBindTexture(GL.GL_TEXTURE_2D, texture);
		GL.glBegin(GL.GL_QUADS);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-0.85f, -0.85f, 1.0f);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(0.85f, -0.85f, 1.0f);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(0.85f, 0.85f, 1.0f);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(-0.85f, 0.85f, 1.0f);
		GL.glEnd();
	}
}
