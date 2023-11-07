/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Sebastian Davids - initial implementation
 *     IBM Corporation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * SWT OpenGL snippet: draw a square
 * 
 * This snippet requires the experimental org.eclipse.swt.opengl plugin, which
 * is not included in SWT by default and should only be used with versions of
 * SWT prior to 3.2.  For information on using OpenGL in SWT see
 * http://www.eclipse.org/swt/opengl/ .
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */
import org.eclipse.opengl.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;

/**
 * @deprecated 
 * This snippet is being deprecated as the org.eclipse.swt.opengl plugin it needs
 * is not available anymore.
 */
@Deprecated
public class Snippet174 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("OpenGL in SWT");
	shell.setLayout(new FillLayout());
	GLData data = new GLData();
	data.doubleBuffer = true;
	final GLCanvas canvas = new GLCanvas(shell, SWT.NO_BACKGROUND, data);
	canvas.addControlListener(ControlListener.controlResizedAdapter(e -> {
		resize(canvas);
	}));
	init(canvas);
	new Runnable() {
		@Override
		public void run() {
			if (canvas.isDisposed()) return;
			render();
			canvas.swapBuffers();
			canvas.getDisplay().timerExec(50, this);
		}
	}.run();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

static void init(GLCanvas canvas) {
	canvas.setCurrent();
	resize(canvas);
	GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	GL.glColor3f(0.0f, 0.0f, 0.0f);
	GL.glClearDepth(1.0f);
	GL.glEnable(GL.GL_DEPTH_TEST);
	GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
}

static void render() {
	GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	GL.glLoadIdentity();
	GL.glTranslatef(0.0f, 0.0f, -6.0f);
	GL.glBegin(GL.GL_QUADS);
	GL.glVertex3f(-1.0f, 1.0f, 0.0f);
	GL.glVertex3f(1.0f, 1.0f, 0.0f);
	GL.glVertex3f(1.0f, -1.0f, 0.0f);
	GL.glVertex3f(-1.0f, -1.0f, 0.0f);
	GL.glEnd();
}

static void resize(GLCanvas canvas) {
	canvas.setCurrent();
	Rectangle rect = canvas.getClientArea();
	int width = rect.width;
	int height = Math.max(rect.height, 1);
	GL.glViewport(0, 0, width, height);
	GL.glMatrixMode(GL.GL_PROJECTION);
	GL.glLoadIdentity();
	float aspect = (float) width / (float) height;
	GLU.gluPerspective(45.0f, aspect, 0.5f, 400.0f);
	GL.glMatrixMode(GL.GL_MODELVIEW);
	GL.glLoadIdentity();
}
}
