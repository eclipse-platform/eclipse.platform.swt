/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
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
 *     Christoph Läubrich - adjusted to use the default binding for OpenGL
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * SWT OpenGL snippet: use the default OpenGL binding to draw to an SWT GLCanvas
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * To run this snippet one needs to pass the follwoing options to Java (as of JDK17);
 * --add-modules jdk.incubator.foreign --enable-native-access=ALL-UNNAMED -Djava.library.path=.:/usr/lib/x86_64-linux-gnu/
 *
 * If run from a platform SDK one might need to additionally provide the location of the SWT lib in the java.library.path option
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;

@SuppressWarnings("restriction")
public class Snippet255 {
	static void drawTorus(float r, float R, int nsides, int rings) {
		float ringDelta = 2.0f * (float) Math.PI / rings;
		float sideDelta = 2.0f * (float) Math.PI / nsides;
		float theta = 0.0f, cosTheta = 1.0f, sinTheta = 0.0f;
		for (int i = rings - 1; i >= 0; i--) {
			float theta1 = theta + ringDelta;
			float cosTheta1 = (float) Math.cos(theta1);
			float sinTheta1 = (float) Math.sin(theta1);
			org.eclipse.swt.opengl.GL.glBegin(org.eclipse.swt.opengl.GL.GL_QUAD_STRIP());
			float phi = 0.0f;
			for (int j = nsides; j >= 0; j--) {
				phi += sideDelta;
				float cosPhi = (float) Math.cos(phi);
				float sinPhi = (float) Math.sin(phi);
				float dist = R + r * cosPhi;
				org.eclipse.swt.opengl.GL.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
				org.eclipse.swt.opengl.GL.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);
				org.eclipse.swt.opengl.GL.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
				org.eclipse.swt.opengl.GL.glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi);
			}
			org.eclipse.swt.opengl.GL.glEnd();
			theta = theta1;
			cosTheta = cosTheta1;
			sinTheta = sinTheta1;
		}
	}

	public static void main(String [] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Composite comp = new Composite(shell, SWT.NONE);
		comp.setLayout(new FillLayout());
		GLData data = new GLData ();
		data.doubleBuffer = true;
		final GLCanvas canvas = new GLCanvas(comp, SWT.NONE, data);

		canvas.setCurrent();

		canvas.addListener(SWT.Resize, event -> {
			Rectangle bounds = canvas.getBounds();
			float fAspect = (float) bounds.width / (float) bounds.height;
			canvas.setCurrent();
			org.eclipse.swt.opengl.GL.glViewport(0, 0, bounds.width, bounds.height);
			org.eclipse.swt.opengl.GL.glMatrixMode(org.eclipse.swt.opengl.GL.GL_PROJECTION());
			org.eclipse.swt.opengl.GL.glLoadIdentity();
			float near = 0.5f;
			float bottom = -near * (float) Math.tan(45.f / 2);
			float left = fAspect * bottom;
			org.eclipse.swt.opengl.GL.glFrustum(left, -left, bottom, -bottom, near, 400.f);
			org.eclipse.swt.opengl.GL.glMatrixMode(org.eclipse.swt.opengl.GL.GL_MODELVIEW());
			org.eclipse.swt.opengl.GL.glLoadIdentity();
		});

		org.eclipse.swt.opengl.GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		org.eclipse.swt.opengl.GL.glColor3f(1.0f, 0.0f, 0.0f);
		org.eclipse.swt.opengl.GL.glHint(org.eclipse.swt.opengl.GL.GL_PERSPECTIVE_CORRECTION_HINT(), org.eclipse.swt.opengl.GL.GL_NICEST());
		org.eclipse.swt.opengl.GL.glClearDepth(1.0);
		org.eclipse.swt.opengl.GL.glLineWidth(2);
		org.eclipse.swt.opengl.GL.glEnable(org.eclipse.swt.opengl.GL.GL_DEPTH_TEST());

		shell.setText("Oh wie schön ist Panama!");
		shell.setSize(640, 480);
		shell.open();

		final Runnable run = new Runnable() {
			int rot = 0;
			@Override
			public void run() {
				if (!canvas.isDisposed()) {
					canvas.setCurrent();
					org.eclipse.swt.opengl.GL.glClear(org.eclipse.swt.opengl.GL.GL_COLOR_BUFFER_BIT() | org.eclipse.swt.opengl.GL.GL_DEPTH_BUFFER_BIT());
					org.eclipse.swt.opengl.GL.glClearColor(.3f, .5f, .8f, 1.0f);
					org.eclipse.swt.opengl.GL.glLoadIdentity();
					org.eclipse.swt.opengl.GL.glTranslatef(0.0f, 0.0f, -10.0f);
					float frot = rot;
					org.eclipse.swt.opengl.GL.glRotatef(0.15f * rot, 2.0f * frot, 10.0f * frot, 1.0f);
					org.eclipse.swt.opengl.GL.glRotatef(0.3f * rot, 3.0f * frot, 1.0f * frot, 1.0f);
					rot++;
					org.eclipse.swt.opengl.GL.glPolygonMode(org.eclipse.swt.opengl.GL.GL_FRONT_AND_BACK(), org.eclipse.swt.opengl.GL.GL_LINE());
					org.eclipse.swt.opengl.GL.glColor3f(0.9f, 0.9f, 0.9f);
					drawTorus(1, 1.9f + ((float) Math.sin((0.004f * frot))), 15, 15);
					canvas.swapBuffers();
					display.asyncExec(this);
				}
			}
		};
		canvas.addListener(SWT.Paint, event -> run.run());
		display.asyncExec(run);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
