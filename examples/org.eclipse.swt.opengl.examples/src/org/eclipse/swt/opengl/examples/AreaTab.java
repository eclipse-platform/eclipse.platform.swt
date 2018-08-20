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

class AreaTab extends OpenGLTab {
	abstract class Shape {
		abstract void draw();
	}
	class State {
		private String name;
		private int index;
		/**
		 * Constructor.
		 * 
		 * @param name the display name of this state
		 * @param index the display list index corresponding to this state
		 */
		State (String name, int index) {
			super();
			this.index = index;
			this.name = name;
		}
		void display() {
			GL.glCallList(index);
		}
		void dispose() {
			GL.glDeleteLists(index, 1);
		}
		String getName() {
			return name;
		}
	}

	private State[] states;
	private State currentState;
	private float xPos = 0.0f, yPos = 0.0f, zPos = -30.0f;
	private float xRot = 90.0f, yRot = 0.0f, zRot = 0.0f;
	private int quadratic, disk;

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
		xMove.setMaximum(22);
		xMove.setMinimum(0);
		xMove.setThumb(2);
		xMove.setPageIncrement(2);
		xMove.setSelection(10);
		xMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				xPos = xMove.getSelection() - 10;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Y:");
		final Slider yMove = new Slider(movementGroup, SWT.NONE);
		yMove.setIncrement(1);
		yMove.setMaximum(22);
		yMove.setMinimum(0);
		yMove.setThumb(2);
		yMove.setPageIncrement(2);
		yMove.setSelection(10);
		yMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				yPos = yMove.getSelection() - 10;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Z:");
		final Slider zMove = new Slider(movementGroup, SWT.NONE);
		zMove.setIncrement(1);
		zMove.setMaximum(22);
		zMove.setMinimum(0);
		zMove.setThumb(2);
		zMove.setPageIncrement(2);
		zMove.setSelection(10);
		zMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				zPos = zMove.getSelection() - 40;
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
		xRotation.setSelection(90);
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

		Composite optionsGroup = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		optionsGroup.setLayout(layout);

		new Label(optionsGroup, SWT.NONE).setText("Shape:");

		final Combo statesCombo = new Combo(optionsGroup, SWT.READ_ONLY);
		for (int i = 0; i < states.length; i++) {
			statesCombo.add(states[i].getName());
		}
		statesCombo.select(0);
		statesCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				currentState = states[statesCombo.getSelectionIndex()];
			}
		});

		final Button lightsButton = new Button(composite, SWT.CHECK);
		lightsButton.setText("Lights");
		lightsButton.setSelection(true);
		lightsButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (lightsButton.getSelection()) {
					GL.glEnable(GL.GL_LIGHTING);
				} else {
					GL.glDisable(GL.GL_LIGHTING);
				}
			}
		});
	}

	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GLU.gluDeleteQuadric(quadratic);
		GLU.gluDeleteQuadric(disk);
		if (states != null) {
			for (int i = 0; i < states.length; i++) {
				states [i].dispose();
			}
		}
	}

	/**
	 * Draws the logical AND of two shapes.
	 *
	 * @param a shape A
	 * @param b shape B
	 */
	void drawAandB(Shape a, Shape b) {
		// draw parts of B that are inside A
		drawAinsideB(a, b, GL.GL_BACK, GL.GL_NOTEQUAL);
		// we do not want the following to show up
		GL.glColorMask(false, false, false, false);
		// turn on depth testing
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glDepthFunc(GL.GL_ALWAYS);
		// render the front face of B
		b.draw();
		// reset the depth function
		GL.glDepthFunc(GL.GL_LESS);
		// draw parts of A that are inside B
		drawAinsideB(b, a, GL.GL_BACK, GL.GL_NOTEQUAL);
	}

	/**
	 * Draws the contents of one shape that appear within another.
	 *
	 * @param a the shape to draw
	 * @param b the constraining shape
	 * @param face
	 * @param test
	 */
	void drawAinsideB(Shape a, Shape b, int face, int test) {
		// turn off the color buffer
		GL.glColorMask(false, false, false, false);
		// clear the stencil buffer
		GL.glClearStencil(0);
		GL.glEnable(GL.GL_DEPTH_TEST);
		// set to proper Culling
		GL.glCullFace(face);
		// render shape A
		a.draw();
		// set depth mask
		GL.glDepthMask(false);
		// enable stencil test
		GL.glEnable(GL.GL_STENCIL_TEST);
		GL.glStencilFunc(GL.GL_ALWAYS, 0, 0);
		// set the stencil buffer to increment if the depth test passes
		GL.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_INCR);
		// turn on back face culling
		GL.glCullFace(GL.GL_BACK);
		// render B
		b.draw();
		// set the stencil buffer to decrement if the depth test passes
		GL.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_DECR);
		// cull the front face
		GL.glCullFace(GL.GL_FRONT);
		// render B again
		b.draw();
		// set depth mask
		GL.glDepthMask(true);
		GL.glColorMask(true, true, true, true);
		// set the stencil buffer
		GL.glStencilFunc(test, 0, 1);
		// turn off depth testing
		GL.glDisable(GL.GL_DEPTH_TEST);
		// set to proper culling
		GL.glCullFace(face);
		// render A
		a.draw();
		// disable stencil test
		GL.glDisable(GL.GL_STENCIL_TEST);
	}

	/**
	 * Draws the logical OR of two shapes.
	 *
	 * @param a shape A
	 * @param b shape B
	 */
	void drawAorB(Shape a, Shape b) {
		GL.glEnable(GL.GL_DEPTH_TEST);
		a.draw();
		b.draw();
		GL.glDisable(GL.GL_DEPTH_TEST);
	}

	/**
	 * Draws one shape subtracted from another.
	 *
	 * @param a the base shape
	 * @param b the shape to subtract
	 */
	void drawAsubB(Shape a, Shape b) {
		// draw back parts of B inside A
		drawAinsideB(b, a, GL.GL_FRONT, GL.GL_NOTEQUAL);
		// we do not want the following to show up
		GL.glColorMask(false, false, false, false);
		GL.glEnable(GL.GL_DEPTH_TEST);
		// change the depth test to GL_ALWAYS
		GL.glDepthFunc(GL.GL_ALWAYS);
		// render the front face of B
		a.draw();
		// reset the depth function
		GL.glDepthFunc(GL.GL_LESS);
		// draw front parts of A outside B
		drawAinsideB(a, b, GL.GL_BACK, GL.GL_EQUAL);
	}

	/**
	 * Draws the specifed shape.
	 * 
	 * @param shape the shape to draw
	 */
	void drawShape(Shape shape) {
		GL.glEnable(GL.GL_DEPTH_TEST);
		shape.draw();
		GL.glDisable(GL.GL_DEPTH_TEST);
	}

	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Area";
	}
	
	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		if (!hasStencilSupport()) return;
		
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		float[] lightPos = { 0.0f, 5.0f, -10.0f, 1.0f };
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos);
		quadratic = GLU.gluNewQuadric();
		disk = GLU.gluNewQuadric();
	
		GL.glEnable(GL.GL_CULL_FACE);
		GL.glEnable(GL.GL_LIST_MODE);
		GL.glEnable(GL.GL_LIGHT0);
		GL.glEnable(GL.GL_LIGHTING);
		GL.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE);

		final float[] sphereMaterial = { 0.0f, 1.0f, 0.0f, 1.0f };
		final float[] cylinderMaterial = { 1.0f, 0.0f, 0.0f, 1.0f };

		Shape sphere = new Shape() {
			public void draw() {
				GL.glMaterialfv(
					GL.GL_FRONT_AND_BACK,
					GL.GL_AMBIENT_AND_DIFFUSE,
					sphereMaterial);
				GL.glColor3f(0.0f, 1.0f, 0.0f);
				GLU.gluSphere(quadratic, 3, 32, 32);
			}
		};
		Shape cylinder = new Shape() {
			public void draw() {
				GL.glMaterialfv(
					GL.GL_FRONT_AND_BACK,
					GL.GL_AMBIENT_AND_DIFFUSE,
					cylinderMaterial);
				GL.glColor3f(1.0f, 0.0f, 0.0f);
				GLU.gluQuadricOrientation(disk, GLU.GLU_INSIDE);
				GL.glPushMatrix();
				GL.glTranslatef(1.0f, 1.0f, 0.0f);
				GLU.gluDisk(disk, 0, 3, 32, 32);
				GLU.gluCylinder(quadratic, 3, 3, 6, 32, 32);
				GL.glPushMatrix();
				GL.glTranslatef(0.0f, 0.0f, 6.0f);
				GLU.gluQuadricOrientation(disk, GLU.GLU_OUTSIDE);
				GLU.gluDisk(disk, 0, 3, 32, 32);
				GL.glPopMatrix();
				GL.glPopMatrix();
			}
		};

		// create the display lists and states
		states = new State[6];
		int index = 1;
		
		GL.glNewList(index, GL.GL_COMPILE);
		drawShape(cylinder);
		GL.glEndList();
		states[0] = new State("Cylinder",index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawShape(sphere);
		GL.glEndList();
		states[1] = new State("Sphere",index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawAorB(cylinder, sphere);
		GL.glEndList();
		states[2] = new State("Cylinder OR Sphere",index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawAandB(cylinder, sphere);
		GL.glEndList();
		states[3] = new State("Cylinder AND Sphere",index++);

		GL.glNewList(index, GL.GL_COMPILE);
		drawAsubB(cylinder, sphere);
		GL.glEndList();
		states[4] = new State("Cylinder SUB Sphere",index++);
		
		GL.glNewList(index, GL.GL_COMPILE);
		drawAsubB(sphere, cylinder);
		GL.glEndList();
		states[5] = new State("Sphere SUB Cylinder",index++);

		currentState = states[0];
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
		GL.glTranslatef(xPos, yPos, zPos);
		GL.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		GL.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		GL.glRotatef(zRot, 0.0f, 0.0f, 1.0f);

		currentState.display();
	}
}
