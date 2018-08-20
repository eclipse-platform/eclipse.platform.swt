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

class LightTab extends OpenGLTab {
	private float[] materialShininess = { 25.0f };
	private float[] lightPosition = { 0.0f, 0.0f, 0.0f, 1.0f };
	private float[][] colorValues = {
		{ 1.0f, 1.0f, 1.0f, 1.0f },	// light diffuse
		{ 1.0f, 1.0f, 1.0f, 1.0f },	// light ambient
		{ 1.0f, 1.0f, 1.0f, 1.0f },	// light specular
		{ 0.5f, 0.5f, 0.5f, 1.0f },	// light main ambient
		{ 0.0f, 0.0f, 1.0f, 1.0f },	// material diffuse
		{ 0.3f, 0.3f, 0.3f, 1.0f },	// material ambient
		{ 1.0f, 1.0f, 1.0f, 1.0f }	// material specular
	};
	private float yPos = 0.0f, xPos = 0.0f, zPos = -20.0f;
	private int quadratic, sphere;

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
				zPos = zMove.getSelection() - 30;
			}
		});

		Group positionGroup = new Group(composite, SWT.NONE);
		positionGroup.setText("Light Position");
		positionGroup.setLayout(new GridLayout(2, false));

		new Label(positionGroup, SWT.NONE).setText("X:");
		final Slider lightX = new Slider(positionGroup, SWT.NONE);
		lightX.setIncrement(1);
		lightX.setMaximum(22);
		lightX.setMinimum(0);
		lightX.setThumb(2);
		lightX.setPageIncrement(2);
		lightX.setSelection(10);
		lightX.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				lightPosition[0] = ((float) lightX.getSelection() * 10) - 100;
			}
		});

		new Label(positionGroup, SWT.NONE).setText("Y:");
		final Slider lightY = new Slider(positionGroup, SWT.NONE);
		lightY.setIncrement(1);
		lightY.setMaximum(22);
		lightY.setMinimum(0);
		lightY.setThumb(2);
		lightY.setPageIncrement(2);
		lightY.setSelection(10);
		lightY.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				lightPosition[1] = ((float) lightY.getSelection() * 10) - 100;
			}
		});

		new Label(positionGroup, SWT.NONE).setText("Z:");
		final Slider lightZ = new Slider(positionGroup, SWT.NONE);
		lightZ.setIncrement(1);
		lightZ.setMaximum(22);
		lightZ.setMinimum(0);
		lightZ.setThumb(2);
		lightZ.setPageIncrement(2);
		lightZ.setSelection(10);
		lightZ.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				lightPosition[2] = ((float) lightZ.getSelection() * 10) - 20;
			}
		});

		Group lightColorsGroup = new Group(composite, SWT.NONE);
		lightColorsGroup.setText("Light Colors");
		lightColorsGroup.setLayout(new GridLayout());
		GridData data = new GridData(GridData.FILL_BOTH);
		lightColorsGroup.setLayoutData(data);

		ColorSelectionGroup lightDiffuseButton =
			new ColorSelectionGroup(lightColorsGroup, SWT.NONE);
		lightDiffuseButton.setText("Diffuse");
		lightDiffuseButton.setRGB(
			new RGB(
				(int) (colorValues[0][0] * 255),
				(int) (colorValues[0][1] * 255),
				(int) (colorValues[0][2] * 255)));
		lightDiffuseButton
			.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				colorValues[0][0] = ((float) rgb.red) / 255;
				colorValues[0][1] = ((float) rgb.green) / 255;
				colorValues[0][2] = ((float) rgb.blue) / 255;
				setColorValues();
			}
		});

		ColorSelectionGroup lightAmbientButton =
			new ColorSelectionGroup(lightColorsGroup, SWT.NONE);
		lightAmbientButton.setText("Ambient");
		lightAmbientButton.setRGB(
			new RGB(
				(int) (colorValues[1][0] * 255),
				(int) (colorValues[1][1] * 255),
				(int) (colorValues[1][2] * 255)));
		lightAmbientButton
			.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				colorValues[1][0] = ((float) rgb.red) / 255;
				colorValues[1][1] = ((float) rgb.green) / 255;
				colorValues[1][2] = ((float) rgb.blue) / 255;
				setColorValues();
			}
		});

		ColorSelectionGroup lightSpecularButton =
			new ColorSelectionGroup(lightColorsGroup, SWT.NONE);
		lightSpecularButton.setText("Specular");
		lightSpecularButton.setRGB(
			new RGB(
				(int) (colorValues[2][0] * 255),
				(int) (colorValues[2][1] * 255),
				(int) (colorValues[2][2] * 255)));
		lightSpecularButton
			.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				colorValues[2][0] = ((float) rgb.red) / 255;
				colorValues[2][1] = ((float) rgb.green) / 255;
				colorValues[2][2] = ((float) rgb.blue) / 255;
				setColorValues();
			}
		});

		ColorSelectionGroup lightMainAmbientButton =
			new ColorSelectionGroup(lightColorsGroup, SWT.NONE);
		lightMainAmbientButton.setText("Main Ambient");
		lightMainAmbientButton.setRGB(
			new RGB(
				(int) (colorValues[3][0] * 255),
				(int) (colorValues[3][1] * 255),
				(int) (colorValues[3][2] * 255)));
		lightMainAmbientButton
			.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				colorValues[3][0] = ((float) rgb.red) / 255;
				colorValues[3][1] = ((float) rgb.green) / 255;
				colorValues[3][2] = ((float) rgb.blue) / 255;
				setColorValues();
			}
		});

		Group materialColorsGroup = new Group(composite, SWT.NONE);
		materialColorsGroup.setText("Material Colors");
		materialColorsGroup.setLayout(new GridLayout());
		data = new GridData(GridData.FILL_BOTH);
		materialColorsGroup.setLayoutData(data);

		ColorSelectionGroup materialDiffuseButton =
			new ColorSelectionGroup(materialColorsGroup, SWT.NONE);
		materialDiffuseButton.setText("Diffuse");
		materialDiffuseButton.setRGB(
			new RGB(
				(int) (colorValues[4][0] * 255),
				(int) (colorValues[4][1] * 255),
				(int) (colorValues[4][2] * 255)));
		materialDiffuseButton
			.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				colorValues[4][0] = ((float) rgb.red) / 255;
				colorValues[4][1] = ((float) rgb.green) / 255;
				colorValues[4][2] = ((float) rgb.blue) / 255;
				setColorValues();
			}
		});

		ColorSelectionGroup materialAmbientButton =
			new ColorSelectionGroup(materialColorsGroup, SWT.NONE);
		materialAmbientButton.setText("Ambient");
		materialAmbientButton.setRGB(
			new RGB(
				(int) (colorValues[5][0] * 255),
				(int) (colorValues[5][1] * 255),
				(int) (colorValues[5][2] * 255)));
		materialAmbientButton
			.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				colorValues[5][0] = ((float) rgb.red) / 255;
				colorValues[5][1] = ((float) rgb.green) / 255;
				colorValues[5][2] = ((float) rgb.blue) / 255;
				setColorValues();
			}
		});

		ColorSelectionGroup materialSpecularButton =
			new ColorSelectionGroup(materialColorsGroup, SWT.NONE);
		materialSpecularButton.setText("Specular");
		materialSpecularButton.setRGB(
			new RGB(
				(int) (colorValues[6][0] * 255),
				(int) (colorValues[6][1] * 255),
				(int) (colorValues[6][2] * 255)));
		materialSpecularButton
			.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				colorValues[6][0] = ((float) rgb.red) / 255;
				colorValues[6][1] = ((float) rgb.green) / 255;
				colorValues[6][2] = ((float) rgb.blue) / 255;
				setColorValues();
			}
		});

		new Label(composite, SWT.NONE).setText("Material Shininess:");
		final Slider shineSlider = new Slider(composite, SWT.NONE);
		shineSlider.setIncrement(5);
		shineSlider.setMaximum(102);
		shineSlider.setMinimum(0);
		shineSlider.setThumb(2);
		shineSlider.setPageIncrement(10);
		shineSlider.setSelection(25);
		shineSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				materialShininess[0] = shineSlider.getSelection();
				GL.glMaterialfv(GL.GL_FRONT_AND_BACK,
					GL.GL_SHININESS,
					materialShininess);
			}
		});
	}

	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GLU.gluDeleteQuadric(quadratic);
		GL.glDeleteLists(sphere, 1);
	}

	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Light";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glClearDepth(1.0);
		setColorValues();
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, materialShininess);
		quadratic = GLU.gluNewQuadric();
		GLU.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
		GL.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
		GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL.glPointSize(2.0f);
		GL.glEnable(GL.GL_LIGHTING);
		GL.glEnable(GL.GL_LIGHT0);
		GL.glEnable(GL.GL_DEPTH_TEST);
		sphere = GL.glGenLists(1);
		GL.glNewList(sphere, GL.GL_COMPILE);
		GLU.gluSphere(quadratic, 3, 32, 32);
		GL.glEndList();
	}

	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition);
		GL.glTranslatef(xPos, yPos, zPos);
		GL.glCallList(sphere);
	}

	/**
	 * Sets the current color values into the rendered display.
	 */
	void setColorValues() {
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, colorValues[0]);
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, colorValues[4]);
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, colorValues[5]);
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, colorValues[1]);
		GL.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, colorValues[2]);
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, colorValues[6]);
		GL.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, colorValues[3]);
		GL.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, materialShininess);
	}
}
