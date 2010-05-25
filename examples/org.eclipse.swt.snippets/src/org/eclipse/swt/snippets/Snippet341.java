/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * SWT OpenGL snippet: capture a LWJGL drawing to an SWT Image
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */
import java.nio.ByteBuffer;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.LWJGLException;

public class Snippet341 {
	static void drawTorus(float r, float R, int nsides, int rings) {
		float ringDelta = 2.0f * (float) Math.PI / rings;
		float sideDelta = 2.0f * (float) Math.PI / nsides;
		float theta = 0.0f, cosTheta = 1.0f, sinTheta = 0.0f;
		for (int i = rings - 1; i >= 0; i--) {
			float theta1 = theta + ringDelta;
			float cosTheta1 = (float) Math.cos(theta1);
			float sinTheta1 = (float) Math.sin(theta1);
			GL11.glBegin(GL11.GL_QUAD_STRIP);
			float phi = 0.0f;
			for (int j = nsides; j >= 0; j--) {
				phi += sideDelta;
				float cosPhi = (float) Math.cos(phi);
				float sinPhi = (float) Math.sin(phi);
				float dist = R + r * cosPhi;
				GL11.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
				GL11.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);
				GL11.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
				GL11.glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi);
			}
			GL11.glEnd();
			theta = theta1;
			cosTheta = cosTheta1;
			sinTheta = sinTheta1;
		}
	}

	public static void main(String [] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		GLData data = new GLData ();
		data.doubleBuffer = true;
		final GLCanvas canvas = new GLCanvas(shell, SWT.NONE, data);
		canvas.setLayoutData(new GridData(640,480));

		canvas.setCurrent();
		try {
			GLContext.useContext(canvas);
		} catch(LWJGLException e) { e.printStackTrace(); }

		canvas.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				Rectangle bounds = canvas.getBounds();
				float fAspect = (float) bounds.width / (float) bounds.height;
				canvas.setCurrent();
				try {
					GLContext.useContext(canvas);
				} catch(LWJGLException e) { e.printStackTrace(); }
				GL11.glViewport(0, 0, bounds.width, bounds.height);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GLU.gluPerspective(45.0f, fAspect, 0.5f, 400.0f);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
			}
		});

		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glClearDepth(1.0);
		GL11.glLineWidth(2);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Capture");
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				capture(canvas);
			}
		});
		shell.pack();
		shell.open();

		display.asyncExec(new Runnable() {
			int rot = 0;
			public void run() {
				if (!canvas.isDisposed()) {
					canvas.setCurrent();
					try {
						GLContext.useContext(canvas);
					} catch(LWJGLException e) { e.printStackTrace(); }
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
					GL11.glClearColor(.3f, .5f, .8f, 1.0f);
					GL11.glLoadIdentity();
					GL11.glTranslatef(0.0f, 0.0f, -10.0f);
					float frot = rot;
					GL11.glRotatef(0.15f * rot, 2.0f * frot, 10.0f * frot, 1.0f);
					GL11.glRotatef(0.3f * rot, 3.0f * frot, 1.0f * frot, 1.0f);
					rot++;
					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
					GL11.glColor3f(0.9f, 0.9f, 0.9f);
					drawTorus(1, 1.9f + ((float) Math.sin((0.004f * frot))), 15, 15);
					canvas.swapBuffers();
					display.asyncExec(this);
				}
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	static void capture(GLCanvas glCanvas) {
		final int PAD = 4;
		Display display = glCanvas.getDisplay();
		Rectangle bounds = glCanvas.getBounds();
		int size = bounds.width * PAD * bounds.height;

		ByteBuffer buffer = ByteBuffer.allocateDirect(size);
		GL11.glReadPixels(0, 0, bounds.width, bounds.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		byte[] bytes = new byte[size];
		buffer.get(bytes);
		/* 
		 * In OpenGL (0,0) is at the bottom-left corner, and y values ascend in the upward
		 * direction.  This is opposite to SWT which defines (0,0) to be at the top-left
		 * corner with y values ascendingin the downwards direction.  Re-order the OpenGL-
		 * provided bytes to SWT's expected order so that the Image will not appear inverted.  
		 */
		byte[] temp = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i += bounds.width * PAD) {
			System.arraycopy(bytes, bytes.length - i - bounds.width * PAD, temp, i, bounds.width * PAD);
		}
		bytes = temp;
		ImageData data = new ImageData(bounds.width, bounds.height, 32, new PaletteData(0xFF000000, 0xFF0000, 0xFF00), PAD, bytes);
		final Image image = new Image(display, data);

		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setLayoutData(new GridData(bounds.width, bounds.height));
		canvas.addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event event) {
				event.gc.drawImage(image, 0, 0);
			}
		});
		shell.pack();
		shell.open();
		shell.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				image.dispose();
			}
		});
	}
}
