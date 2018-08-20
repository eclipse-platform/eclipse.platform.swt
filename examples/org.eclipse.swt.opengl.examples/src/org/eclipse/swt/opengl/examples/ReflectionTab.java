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

class ReflectionTab extends OpenGLTab {
	private float ballY = 0.0f;
	private float ballZ = 1.6666666f;
	private float ballX = 0.0f;
	private float ballRot = 0.0f;
	private float cubeX = 2.5f;
	private float cubeY = -2.0f;
	private float cubeZ = 2.0f;
	private float xPos = 0.0f;
	private float yPos = 0.0f;
	private float zPos = 22.0f;
	private float ballRotSpeed = 0.0f;
	private int quadratic;
	private int[] textures = new int[3];
	private final static int
		LIST_INDEX_BALL = 1,LIST_INDEX_BOX = 2,
		LIST_INDEX_MIRROR = 3, LIST_INDEX_ROOM = 4;
	private final static int
		TEXTURE_INDEX_BALL = 0, TEXTURE_INDEX_FLOOR = 1,
		TEXTURE_INDEX_BOX = 2;
	private final static String[] IMAGES = {
		"images/Ball.jpg", "images/Floor.jpg", "images/Box.bmp" };
	private final static int SLEEP_LENGTH = 50;

	/**
	 * Draws the ball
	 */
	void createBall() {
		GL.glNewList(LIST_INDEX_BALL, GL.GL_COMPILE);
		GL.glColor3f(1.0f, 1.0f, 1.0f);
		GL.glBindTexture(GL.GL_TEXTURE_2D, textures[TEXTURE_INDEX_BALL]);
		GLU.gluSphere(quadratic, 0.35f, 32, 16);
		GL.glDisable(GL.GL_BLEND);
		GL.glEndList();
	}

	/**
	 * Draws the box
	 */
	void createBox() {
		float size = 1.0f;
		GL.glNewList(LIST_INDEX_BOX, GL.GL_COMPILE);
		GL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glBindTexture(GL.GL_TEXTURE_2D, textures[TEXTURE_INDEX_BOX]);
		GL.glBegin(GL.GL_QUADS);
		// front	
		GL.glNormal3f(0.0f, 0.0f, 1.0f);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-size, -size, size);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(size, -size, size);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(size, size, size);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(-size, size, size);
		// back	
		GL.glNormal3f(0.0f, 0.0f, -1.0f);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-size, -size, -size);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(size, -size, -size);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(size, size, -size);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(-size, size, -size);
		// left	
		GL.glNormal3f(-1.0f, 0.0f, 0.0f);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-size, -size, -size);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(-size, -size, size);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(-size, size, size);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(-size, size, -size);
		// right	
		GL.glNormal3f(1.0f, 0.0f, 0.0f);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(size, -size, -size);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(size, -size, size);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(size, size, size);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(size, size, -size);
		// top	
		GL.glNormal3f(0.0f, 1.0f, 0.0f);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(size, size, -size);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(-size, size, -size);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(-size, size, size);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(size, size, size);
		// bottom	
		GL.glNormal3f(0.0f, -1.0f, 0.0f);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(size, -size, -size);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(-size, -size, -size);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(-size, -size, size);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(size, -size, size);
		GL.glEnd();
		GL.glEndList();
	}

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(Composite composite) {
		Group ballTranslation = new Group(composite, SWT.NONE);
		ballTranslation.setLayout(new GridLayout(2, false));
		ballTranslation.setText("Ball Translation");

		new Label(ballTranslation, SWT.NONE).setText("X:");
		final Slider ballXPosSlider = new Slider(ballTranslation, SWT.NONE);
		ballXPosSlider.setIncrement(1);
		ballXPosSlider.setMaximum(38);
		ballXPosSlider.setMinimum(0);
		ballXPosSlider.setThumb(2);
		ballXPosSlider.setPageIncrement(2);
		ballXPosSlider.setSelection(18);
		ballXPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				ballX = (float) (ballXPosSlider.getSelection() - 18) / 5;
			}
		});

		new Label(ballTranslation, SWT.NONE).setText("Y:");
		final Slider ballYPosSlider = new Slider(ballTranslation, SWT.NONE);
		ballYPosSlider.setIncrement(1);
		ballYPosSlider.setMaximum(38);
		ballYPosSlider.setMinimum(0);
		ballYPosSlider.setThumb(2);
		ballYPosSlider.setPageIncrement(2);
		ballYPosSlider.setSelection(18);
		ballYPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				ballY = (float) (ballYPosSlider.getSelection() - 18) / 5;
			}
		});

		new Label(ballTranslation, SWT.NONE).setText("Z:");
		final Slider ballZPosSlider = new Slider(ballTranslation, SWT.NONE);
		ballZPosSlider.setIncrement(1);
		ballZPosSlider.setMaximum(24);
		ballZPosSlider.setMinimum(0);
		ballZPosSlider.setThumb(1);
		ballZPosSlider.setPageIncrement(2);
		ballZPosSlider.setSelection(4);
		ballZPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				ballZ = (float) (ballZPosSlider.getSelection() + 1) / 3;
			}
		});

		Group cubeTranslation = new Group(composite, SWT.NONE);
		cubeTranslation.setLayout(new GridLayout(2, false));
		cubeTranslation.setText("Cube Translation");

		new Label(cubeTranslation, SWT.NONE).setText("X:");
		final Slider cubeXPosSlider = new Slider(cubeTranslation, SWT.NONE);
		cubeXPosSlider.setIncrement(1);
		cubeXPosSlider.setMaximum(14);
		cubeXPosSlider.setMinimum(0);
		cubeXPosSlider.setThumb(2);
		cubeXPosSlider.setPageIncrement(2);
		cubeXPosSlider.setSelection(11);
		cubeXPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				cubeX = (float) (cubeXPosSlider.getSelection() - 6) / 2;
			}
		});

		new Label(cubeTranslation, SWT.NONE).setText("Y:");
		final Slider cubeYPosSlider = new Slider(cubeTranslation, SWT.NONE);
		cubeYPosSlider.setIncrement(1);
		cubeYPosSlider.setMaximum(14);
		cubeYPosSlider.setMinimum(0);
		cubeYPosSlider.setThumb(2);
		cubeYPosSlider.setPageIncrement(2);
		cubeYPosSlider.setSelection(2);
		cubeYPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				cubeY = (float) (cubeYPosSlider.getSelection() - 6) / 2;
			}
		});

		new Label(cubeTranslation, SWT.NONE).setText("Z:");
		final Slider cubeZPosSlider = new Slider(cubeTranslation, SWT.NONE);
		cubeZPosSlider.setIncrement(1);
		cubeZPosSlider.setMaximum(10);
		cubeZPosSlider.setMinimum(1);
		cubeZPosSlider.setThumb(1);
		cubeZPosSlider.setPageIncrement(2);
		cubeZPosSlider.setSelection(2);
		cubeZPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				cubeZ = (float) (cubeZPosSlider.getSelection() + 2) / 2;
			}
		});

		Group movementGroup = new Group(composite, SWT.NONE);
		movementGroup.setLayout(new GridLayout(2, false));
		movementGroup.setText("Room Rotation");

		new Label(movementGroup, SWT.NONE).setText("X:");
		final Slider screenXPosSlider = new Slider(movementGroup, SWT.NONE);
		screenXPosSlider.setIncrement(1);
		screenXPosSlider.setMaximum(42);
		screenXPosSlider.setMinimum(0);
		screenXPosSlider.setThumb(2);
		screenXPosSlider.setPageIncrement(2);
		screenXPosSlider.setSelection(20);
		screenXPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				xPos = screenXPosSlider.getSelection() - 20;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Y:");
		final Slider screenYPosSlider = new Slider(movementGroup, SWT.NONE);
		screenYPosSlider.setIncrement(1);
		screenYPosSlider.setMaximum(42);
		screenYPosSlider.setMinimum(0);
		screenYPosSlider.setThumb(2);
		screenYPosSlider.setPageIncrement(2);
		screenYPosSlider.setSelection(20);
		screenYPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				yPos = screenYPosSlider.getSelection() - 20;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Z:");
		final Slider screenZPosSlider = new Slider(movementGroup, SWT.NONE);
		screenZPosSlider.setIncrement(1);
		screenZPosSlider.setMaximum(42);
		screenZPosSlider.setMinimum(0);
		screenZPosSlider.setThumb(2);
		screenZPosSlider.setPageIncrement(2);
		screenZPosSlider.setSelection(20);
		screenZPosSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				zPos = screenZPosSlider.getSelection() + 2;
			}
		});

		new Label(composite, SWT.NONE).setText("Ball Rotation Speed:");
		final Slider ballRotSlider = new Slider(composite, SWT.NONE);
		ballRotSlider.setIncrement(1);
		ballRotSlider.setMaximum(22);
		ballRotSlider.setMinimum(0);
		ballRotSlider.setThumb(2);
		ballRotSlider.setPageIncrement(2);
		ballRotSlider.setSelection(0);
		ballRotSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				ballRotSpeed = ballRotSlider.getSelection();
			}
		});
	}
	
	/**
	 * Draws the mirror
	 */
	void createMirror() {
		GL.glNewList(LIST_INDEX_MIRROR, GL.GL_COMPILE);
		GL.glDisable(GL.GL_TEXTURE_2D);
		GL.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);
		GL.glNormal3f(0.0f, 0.0f, 1.0f);
		GL.glBegin(GL.GL_QUADS);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-2.0f, 2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(-2.0f, -2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(2.0f, -2.0f, 0.0f);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(2.0f, 2.0f, 0.0f);
		GL.glEnd();
		GL.glEnable(GL.GL_TEXTURE_2D);
		GL.glEndList();
	}
	
	/**
	 * Draws the room
	 */
	void createRoom() {
		GL.glNewList(LIST_INDEX_ROOM, GL.GL_COMPILE);
		GL.glDisable(GL.GL_TEXTURE_2D);
		GL.glBegin(GL.GL_QUADS);
		// left wall
		GL.glNormal3f(1.0f, 0.0f, 0.0f);
		GL.glVertex3f(-4.0f, 4.0f, 0.0f);
		GL.glVertex3f(-4.0f, 4.0f, 6.0f);
		GL.glVertex3f(-4.0f, -4.0f, 6.0f);
		GL.glVertex3f(-4.0f, -4.0f, 0.0f);
		// right wall
		GL.glNormal3f(-1.0f, 0.0f, 01.0f);
		GL.glVertex3f(4.0f, 4.0f, 0.0f);
		GL.glVertex3f(4.0f, 4.0f, 6.0f);
		GL.glVertex3f(4.0f, -4.0f, 6.0f);
		GL.glVertex3f(4.0f, -4.0f, 0.0f);
		// ceiling	
		GL.glNormal3f(0.0f, -1.0f, 0.0f);
		GL.glColor3f(0.9f, 0.9f, 0.9f);
		GL.glVertex3f(-4.0f, 4.0f, 0.0f);
		GL.glVertex3f(-4.0f, 4.0f, 6.0f);
		GL.glVertex3f(4.0f, 4.0f, 6.0f);
		GL.glVertex3f(4.0f, 4.0f, 0.0f);
		GL.glEnd();
		// back wall with triangle strip to illiminate t-intersections 
		GL.glBegin(GL.GL_TRIANGLE_STRIP);
		GL.glNormal3f(0.0f, 0.0f, 1.0f);
		GL.glVertex3d(-4.0, -4.0, 0.0);
		GL.glVertex3d(-2.2, -4.0, 0.0);
		GL.glVertex3d(-2.2, -2.2, 0.0);
		GL.glVertex3d(2.2, -4.0, 0.0);
		GL.glVertex3d(2.2, -2.2, 0.0);
		GL.glVertex3d(4.0, -4.0, 0.0);
		GL.glVertex3d(2.2, -2.2, 0.0);
		GL.glVertex3d(4.0, -2.2, 0.0);
		GL.glVertex3d(2.2, 2.2, 0.0);
		GL.glVertex3d(4.0, 2.2, 0.0);
		GL.glVertex3d(2.2, 2.2, 0.0);
		GL.glVertex3d(4.0, 4.0, 0.0);
		GL.glVertex3d(2.2, 2.2, 0.0);
		GL.glVertex3d(2.2, 4.0, 0.0);
		GL.glVertex3d(-2.2, 2.2, 0.0);
		GL.glVertex3d(-2.2, 4.0, 0.0);
		GL.glVertex3d(-4.0, 4.0, 0.0);
		GL.glVertex3d(-2.2, 2.2, 0.0);
		GL.glVertex3d(-4.0, 2.2, 0.0);
		GL.glVertex3d(-2.2, -2.2, 0.0);
		GL.glVertex3d(-4.0, -2.2, 0.0);
		GL.glVertex3d(-4.0, -4.0, 0.0);
		GL.glEnd();
		GL.glEnable(GL.GL_TEXTURE_2D);
		// draw floor
		GL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glBindTexture(GL.GL_TEXTURE_2D, textures[TEXTURE_INDEX_FLOOR]);
		GL.glNormal3f(0.0f, 1.0f, 0.0f);
		GL.glBegin(GL.GL_QUADS);
		// floor
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-4.0f, -4.0f, 6.0f);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(-4.0f, -4.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(4.0f, -4.0f, 0.0f);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(4.0f, -4.0f, 6.0f);
		GL.glEnd();
		// draw mirror frame
		GL.glBindTexture(GL.GL_TEXTURE_2D, textures[TEXTURE_INDEX_BOX]);
		GL.glNormal3f(0.0f, 0.0f, 1.0f);
		GL.glBegin(GL.GL_QUADS);
		// top
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-2.2f, 2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(2.2f, 2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(2.2f, 2.2f, 0.0f);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(-2.2f, 2.2f, 0.0f);
		// right
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(2.0f, 2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(2.0f, -2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(2.2f, -2.0f, 0.0f);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(2.2f, 2.0f, 0.0f);
		// bottom
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-2.2f, -2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(2.2f, -2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(2.2f, -2.2f, 0.0f);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(-2.2f, -2.2f, 0.0f);
		// left
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-2.0f, 2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(-2.0f, -2.0f, 0.0f);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(-2.2f, -2.0f, 0.0f);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(-2.2f, 2.0f, 0.0f);
		GL.glEnd();
		GL.glEndList();
	}

	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GLU.gluDeleteQuadric(quadratic);
		GL.glDeleteLists(LIST_INDEX_BALL, 1);
		GL.glDeleteLists(LIST_INDEX_BOX, 1);
		GL.glDeleteLists(LIST_INDEX_MIRROR, 1);
		GL.glDeleteLists(LIST_INDEX_ROOM, 1);
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
		return "Reflection";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		if (!hasStencilSupport()) return;

		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glClearDepth(1.0f);
		GL.glClearStencil(0);
		GL.glGenTextures(IMAGES.length, textures);
		for (int i = 0; i < IMAGES.length; i++) {
			loadTexture(getGlCanvas(), IMAGES[i], i, textures);
		}

		GL.glShadeModel(GL.GL_SMOOTH);
		quadratic = GLU.gluNewQuadric();
		GLU.gluQuadricNormals(quadratic, GL.GL_SMOOTH);
		GLU.gluQuadricTexture(quadratic, true);
		GL.glDepthFunc(GL.GL_LEQUAL);
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

		// set up the lights
		float[] LightAmb = { 0.7f, 0.7f, 0.7f, 1.0f };
		float[] LightDif = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] lightsAmbient = { 0.5f, 0.5f, 0.5f, 1.0f };
		GL.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lightsAmbient);
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, LightAmb);
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, LightDif);

		GL.glEnable(GL.GL_LIGHT0);
		GL.glEnable(GL.GL_LIGHTING);
		GL.glEnable(GL.GL_LINE_SMOOTH);
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glEnable(GL.GL_TEXTURE_2D);
		GL.glEnable(GL.GL_BLEND);

		createRoom();
		createBall();
		createBox();
		createMirror();
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
		GLU.gluLookAt(xPos, yPos, zPos, 0, 0, 0, 0, 1, 0);
		// clip plane equation
		double clipEquation[] = { 0.0f, 0.0f, -1.0f, 0.0f };
		float[] LightPos = { 0.0f, 2.0f, 7.0f, 1.0f };
		GL.glColorMask(false, false, false, false);
		// enable stencil buffer for "marking" the mirror		
		GL.glEnable(GL.GL_STENCIL_TEST);
		// set the stencil buffer to 1 where a polygon is drawn
		GL.glStencilFunc(GL.GL_ALWAYS, 1, 1);
		GL.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);
		GL.glDisable(GL.GL_DEPTH_TEST);
		GL.glCallList(LIST_INDEX_MIRROR);
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glColorMask(true, true, true, true);
		GL.glStencilFunc(GL.GL_EQUAL, 1, 1);
		// draw where the stencil is 1						
		GL.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
		GL.glEnable(GL.GL_CLIP_PLANE0);
		GL.glClipPlane(GL.GL_CLIP_PLANE0, clipEquation);
		GL.glPushMatrix();
		GL.glScalef(1.0f, 1.0f, -1.0f);
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, LightPos);
		GL.glCallList(LIST_INDEX_ROOM);			// reflection
		GL.glPushMatrix();
		GL.glTranslatef(cubeX, cubeY, cubeZ);
		GL.glCallList(LIST_INDEX_BOX);			// reflection
		GL.glPopMatrix();
		GL.glTranslatef(ballX, ballY, ballZ);
		GL.glRotatef(ballRot, 1.0f, 0.0f, 0.0f);
		GL.glCallList(LIST_INDEX_BALL);			// reflection
		GL.glPopMatrix();
		GL.glDisable(GL.GL_CLIP_PLANE0);
		GL.glDisable(GL.GL_STENCIL_TEST);
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, LightPos);
		GL.glEnable(GL.GL_BLEND);
		GL.glDisable(GL.GL_LIGHTING);
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL.glCallList(LIST_INDEX_MIRROR);
		GL.glEnable(GL.GL_LIGHTING);
		GL.glDisable(GL.GL_BLEND);
		GL.glCallList(LIST_INDEX_ROOM);
		GL.glPushMatrix();
		GL.glTranslatef(cubeX, cubeY, cubeZ);
		GL.glCallList(LIST_INDEX_BOX);
		GL.glPopMatrix();
		GL.glTranslatef(ballX, ballY, ballZ);
		GL.glRotatef(ballRot, 1.0f, 0.0f, 0.0f);
		GL.glCallList(LIST_INDEX_BALL);
		ballRot += ballRotSpeed;
	}
}
