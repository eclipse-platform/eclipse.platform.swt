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

class ObjectsTab extends OpenGLTab {
	private class Shape {
		private String name;
		private int index;
		/**
		 * Constructor.
		 * 
		 * @param name the display name of this shape
		 * @param index the display list index corresponding to this shape
		 */
		Shape(String name, int index) {
			super();
			this.index = index;
			this.name = name;
		}
		void dispose() {
			GL.glDeleteLists(index, 1);
		}
		void draw() {
			GL.glCallList(index);
		}
		String getName() {
			return name;
		}
	}

	private Shape[] shapes;
	private Shape currentShape;
	private float xRot = 0.0f, yRot = 0.0f;
	private boolean fill = true;
	private int quadratic;
	private final static int SLEEP_LENGTH = 50;

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(final Composite composite) {
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		Composite controls = new Composite(composite, SWT.NONE);
		controls.setLayout(layout);
		
		new Label(controls, SWT.NONE).setText("Object:");
		final Combo objectsCombo = new Combo(controls, SWT.READ_ONLY);
		for (int i = 0; i < shapes.length; i++) {
			objectsCombo.add(shapes[i].getName());
		}
		objectsCombo.select(0);
		objectsCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				currentShape = shapes[objectsCombo.getSelectionIndex()];
			}
		});

		final Button fillToggleButton = new Button(controls, SWT.CHECK);
		fillToggleButton.setText("Fill");
		fillToggleButton.setSelection(true);
		fillToggleButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				fill = fillToggleButton.getSelection();
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
		GLU.gluDeleteQuadric(quadratic);
		for (int i = 0; i < shapes.length; i++) {
			shapes [i].dispose();
		}
	}
	
	/**
	 * Creates a circle centered at 0,0 in the viewport.
	 * 
	 * @param radius
	 * @param xShrink
	 * @param yShrink
	 */
	void drawCircle(float radius, float xShrink, float yShrink) {
		GL.glBegin(GL.GL_POLYGON);
		float dia = (float) 2.0 * (float) Math.PI;
		for (float angle = 0.0f; angle <= dia; angle += 0.1f) {
			GL.glVertex2d(radius * Math.cos(angle), radius * Math.sin(angle));
		}
		GL.glEnd();
	}
	
	/**
	 * Creates a cube centered at 0,0 in the viewport.
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 */
	void drawCube(float width, float height, float depth) {
		GL.glBegin(GL.GL_QUADS);
		// front
		GL.glVertex3f(-width, -height, depth);	// bottom left 			
		GL.glVertex3f(width, -height, depth);	// bottom right
		GL.glVertex3f(width, height, depth);	// top right
		GL.glVertex3f(-width, height, depth);	// top left
		// back
		GL.glVertex3f(-width, -height, -depth);	// bottom left 			
		GL.glVertex3f(width, -height, -depth);	// bottom right
		GL.glVertex3f(width, height, -depth);	// top right
		GL.glVertex3f(-width, height, -depth);	// top left
		// left
		GL.glVertex3f(-width, -height, -depth);	// bottom left				
		GL.glVertex3f(-width, -height, depth);	// bottom right
		GL.glVertex3f(-width, height, depth);	// top right
		GL.glVertex3f(-width, height, -depth);	// top left	
		// right
		GL.glVertex3f(width, -height, depth);	// bottom left				
		GL.glVertex3f(width, -height, -depth);	// bottom right
		GL.glVertex3f(width, height, -depth);	// top right
		GL.glVertex3f(width, height, depth);	// top left
		// top
		GL.glVertex3f(width, height, -depth);	// back right
		GL.glVertex3f(-width, height, -depth);	// back left
		GL.glVertex3f(-width, height, depth);	// front left
		GL.glVertex3f(width, height, depth);	// front right
		// bottom
		GL.glVertex3f(width, -height, depth);	// front right
		GL.glVertex3f(-width, -height, depth);	// front left
		GL.glVertex3f(-width, -height, -depth);	// back left
		GL.glVertex3f(width, -height, -depth);	// back right
		GL.glEnd();
	}
	
	/**
	 * Creates a pyramid centered at 0,0 in the viewport.
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 */
	void drawPyramid(float width, float height, float depth) {
		GL.glBegin(GL.GL_TRIANGLES);
		// front
		GL.glVertex3f(0.0f, height, 0.0f);		// top				
		GL.glVertex3f(-width, -height, depth);	// left	
		GL.glVertex3f(width, -height, depth);	// right
		// left
		GL.glVertex3f(0.0f, height, 0.0f);		// top				
		GL.glVertex3f(-width, -height, -depth);	// left	
		GL.glVertex3f(-width, -height, depth);	// right	
		// front
		GL.glVertex3f(-width, -height, -depth);	// left	
		GL.glVertex3f(0.0f, height, 0.0f);		// top						
		GL.glVertex3f(width, -height, -depth);	// right
		// right
		GL.glVertex3f(0.0f, height, 0.0f);		// top				
		GL.glVertex3f(width, -height, depth);	// left	
		GL.glVertex3f(width, -height, -depth);	// right			
		GL.glEnd();
		GL.glBegin(GL.GL_QUADS);
		// bottom
		GL.glVertex3f(-width, -height, depth);	// front left
		GL.glVertex3f(-width, -height, -depth);	// back left
		GL.glVertex3f(width, -height, -depth);	// back right
		GL.glVertex3f(width, -height, depth);	// front right
		GL.glEnd();
	}
	
	/**
	 * Creates a square centered at 0,0 in the viewport.
	 * 
	 * @param width
	 * @param height
	 */
	void drawSquare(float width, float height) {
		GL.glBegin(GL.GL_QUADS);
		GL.glVertex3f(-width, -height, 0.0f);	// bottom left 			
		GL.glVertex3f(width, -height, 0.0f);	// bottom right
		GL.glVertex3f(width, height, 0.0f);		// top right
		GL.glVertex3f(-width, height, 0.0f);	// top left
		GL.glEnd();
	}
	
	/**
	 * This method is from glut_shapes.c .
	 * 
	 * @param r
	 * @param R
	 * @param nsides
	 * @param rings
	 */
	void drawTorus(float r, float R, int nsides, int rings) {
		float ringDelta = 2.0f * (float) Math.PI / rings;
		float sideDelta = 2.0f * (float) Math.PI / nsides;
		float theta = 0.0f;
		float cosTheta = 1.0f;
		float sinTheta = 0.0f;
		
		for (int i = rings - 1; i >= 0; i--) {
			float theta1 = theta + ringDelta;
			float cosTheta1 = (float) Math.cos(theta1);
			float sinTheta1 = (float) Math.sin(theta1);
			GL.glBegin(GL.GL_QUAD_STRIP);
			float phi = 0.0f;
			
			for (int j = nsides; j >= 0; j--) {
				phi += sideDelta;
				float cosPhi = (float) Math.cos(phi);
				float sinPhi = (float) Math.sin(phi);
				float dist = R + r * cosPhi;
				GL.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
				GL.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);
				GL.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
				GL.glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi);
			}
			
			GL.glEnd();
			theta = theta1;
			cosTheta = cosTheta1;
			sinTheta = sinTheta1;
		}
	}
	
	/**
	 * Creates a triangle centered at 0,0 in the viewport.
	 * 
	 * @param width
	 * @param height
	 */
	void drawTriangle(float width, float height) {
		GL.glBegin(GL.GL_TRIANGLES);
		GL.glVertex3f(0.0f, height, 0.0f);		// middle 
		GL.glVertex3f(-width, -height, 0.0f);	// left 
		GL.glVertex3f(width, -height, 0.0f);	// right		 		
		GL.glEnd();
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
		return "Objects";
	}
	
	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		GL.glClearDepth(1.0);
		quadratic = GLU.gluNewQuadric();
		GLU.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
		GL.glLineWidth(2);

		GL.glEnable(GL.GL_DEPTH_TEST);

		// create the display lists and shapes
		shapes = new Shape[11];
		int index = 1;

		GL.glNewList(index, GL.GL_COMPILE);
		drawTriangle(2.0f, 2.0f);
		GL.glEndList();
		shapes[0] = new Shape("Triangle", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawPyramid(2.0f, 2.0f, 2.0f);
		GL.glEndList();
		shapes[1] = new Shape("Pyramid", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawSquare(2.0f, 2.0f);
		GL.glEndList();
		shapes[2] = new Shape("Square", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawCube(2.0f, 2.0f, 2.0f);
		GL.glEndList();
		shapes[3] = new Shape("Cube", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawCircle(2.0f, 0.0f, 0.0f);
		GL.glEndList();
		shapes[4] = new Shape("Circle", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		GLU.gluPartialDisk(quadratic, 0.5, 2.0, 18, 18, 90, 125);
		GL.glEndList();
		shapes[5] = new Shape("Partial Disk", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		GLU.gluDisk(quadratic, 0.5, 2.0, 18, 18);
		GL.glEndList();
		shapes[6] = new Shape("Disk", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawTorus(1, 2, 18, 18);
		GL.glEndList();
		shapes[7] = new Shape("Torus", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		GLU.gluSphere(quadratic, 2.0, 18, 18);
		GL.glEndList();
		shapes[8] = new Shape("Sphere", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		GLU.gluCylinder(quadratic, 2.0, 2.0, 4, 18, 18);
		GL.glEndList();
		shapes[9] = new Shape("Cylinder", index++);

		GL.glNewList(index, GL.GL_COMPILE);
		GLU.gluCylinder(quadratic, 0, 2.0, 4, 18, 18);
		GL.glEndList();
		shapes[10] = new Shape("Cone", index++);

		currentShape = shapes[0];
	}
	
	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glTranslatef(0.0f, 0.0f, -14.0f);
		// rotate around X and Y axis
		GL.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		GL.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		// sets polygon fill mode
		if (fill) {
			GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		} else {
			GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		}
		currentShape.draw();
		yRot += 1.5;
		xRot += 1.5;
	}
}
