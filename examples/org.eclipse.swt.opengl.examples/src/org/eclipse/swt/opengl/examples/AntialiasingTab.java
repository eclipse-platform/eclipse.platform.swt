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

class AntialiasingTab extends OpenGLTab {
	private Button antiAliasButton;
	private float xPos = 0.0f, yPos = 0.0f, zPos = -6.0f;
	
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
				zPos = zMove.getSelection() - 11;
			}
		});

		antiAliasButton = new Button(composite, SWT.CHECK);
		antiAliasButton.setText("Anti-Aliasing");
		antiAliasButton.setSelection(true);
		antiAliasButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (antiAliasButton.getSelection()) {
					GL.glEnable(GL.GL_LINE_SMOOTH);
				} else {
					GL.glDisable(GL.GL_LINE_SMOOTH);
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
		GL.glDeleteLists(1, 1);
	}

	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Anti-aliasing";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glColor3f(1.0f, 0.0f, 0.0f);
		final float[] BEZIER_POINTS = {
			-1.5f, -1.5f, 4.0f, -0.5f, -1.5f, 2.0f, 0.5f, -1.5f,
			-1.0f, 1.5f, -1.5f, 2.0f, -1.5f, -0.5f, 1.0f, -0.5f,
			-0.5f, 3.0f, 0.5f, -0.5f, 0.0f, 1.5f, -0.5f, -1.0f,
			-1.5f, 0.5f, 4.0f, -0.5f, 0.5f, 0.0f, 0.5f, 0.5f,
			3.0f, 1.5f, 0.5f, 4.0f, -1.5f, 1.5f, -2.0f, -0.5f,
			1.5f, -2.0f, 0.5f, 1.5f, 0.0f, 1.5f, 1.5f, -1.0f,
		};
		GL.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL.glMap2f(GL.GL_MAP2_VERTEX_3, 0, 1, 3, 4, 0, 1, 12, 4, BEZIER_POINTS);
		GL.glMapGrid2f(30, 0.0f, 1.0f, 30, 0.0f, 1.0f);

		GL.glEnable(GL.GL_AUTO_NORMAL);
		GL.glEnable(GL.GL_LINE_SMOOTH);
		GL.glEnable(GL.GL_LINE_STIPPLE);
		GL.glEnable(GL.GL_BLEND);
		GL.glEnable(GL.GL_MAP2_VERTEX_3);

		// create display lists
		GL.glNewList(1, GL.GL_COMPILE);
		GL.glEvalMesh2(GL.GL_LINE, 0, 30, 0, 30);
		GL.glEndList();
	}

	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glTranslatef(xPos, yPos, zPos);
		GL.glCallList(1);	// draw the beizer surface
	}
}
