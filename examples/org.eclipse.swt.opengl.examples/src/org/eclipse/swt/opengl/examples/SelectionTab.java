/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.opengl.*;

abstract class SelectionTab extends OpenGLTab {
	private final static int BUFFER_LENGTH = 64;

	/**
	 * Sets the selected object.  Subclasses must override this method.
	 */
	abstract void processPick(int[] pSelectBuff, int hits);

	/**
	 * Invoke processPick for the object at the specified coordinate.
	 */
	int processSelection(int xPos, int yPos, int pointSize) {
		int[] selectBuffer = new int[BUFFER_LENGTH];
		int[] viewport = new int[4];
		// new fix for sun jre
		int ptr = getContext().getSelectBufferPtr(selectBuffer);

		GL.glSelectBuffer(BUFFER_LENGTH, ptr);
		GL.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		GL.glRenderMode(GL.GL_SELECT);
		GL.glMatrixMode(GL.GL_PROJECTION);
		GL.glPushMatrix();
		GL.glLoadIdentity();
		GLU.gluPickMatrix(xPos, yPos, pointSize, pointSize, viewport);
		Rectangle rect = getGlCanvas().getClientArea();
		float fAspect = (float) rect.width / (float) rect.height;
		GLU.gluPerspective(45.0f, fAspect, 0.5f, 600.0f);
		GL.glMatrixMode(GL.GL_MODELVIEW);
		GL.glInitNames();
		render();
		int hits = GL.glRenderMode(GL.GL_RENDER);
		// new fix for sun jre
		// must be called after render mode is switched
		getContext().getSelectBuffer(ptr, selectBuffer);
		if (hits != 0) processPick(selectBuffer, hits);
		GL.glMatrixMode(GL.GL_PROJECTION);
		GL.glPopMatrix();
		GL.glMatrixMode(GL.GL_MODELVIEW);
		return hits;
	}
}
