/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
 * is not included in swt by default.  For information on this plugin see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/opengl/opengl.html  
 * 
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;

public class Snippet174 {

public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("OpenGL in SWT");
    shell.setLayout(new FillLayout());
    final Canvas canvas = new Canvas(shell, SWT.NO_BACKGROUND);
    canvas.addControlListener(new ControlAdapter() {
        public void controlResized(ControlEvent e) {
            resize(canvas);
        }
    });
    final GLContext context = init(canvas);
    shell.addDisposeListener(new DisposeListener() {
        public void widgetDisposed(DisposeEvent e) {
            context.dispose();
        }
    });
    new Runnable() {
        public void run() {
            if (canvas.isDisposed()) return;
            render();
            context.swapBuffers();
            canvas.getDisplay().timerExec(50, this);
        }
    }.run();
    shell.open();
    while (!shell.isDisposed()) {
        if (!display.readAndDispatch()) display.sleep();
    }
    display.dispose();
}

static GLContext init(Canvas canvas) {
    GLContext context = new GLContext(canvas);
    context.setCurrent();
    resize(canvas);
    GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    GL.glColor3f(0.0f, 0.0f, 0.0f);
    GL.glClearDepth(1.0f);
    GL.glEnable(GL.GL_DEPTH_TEST);
    GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
    return context;
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

static void resize(Canvas canvas) {
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
