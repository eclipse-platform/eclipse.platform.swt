/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.opengl.carbon.*;

public class GLPBuffer {
	int glContext;
	static final int MAX_ATTRIBUTES = 32;

public GLPBuffer (int width, int height, GLFormatData attributes) {
	if (attributes == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int aglAttrib [] = new int [MAX_ATTRIBUTES];
	int pos = 0;
	aglAttrib [pos++] = 4;
	aglAttrib [pos++] = 5;
	aglAttrib [pos++] = 12;
	aglAttrib [pos++] = 16;
	aglAttrib [pos++] = 0;
	int pixelFormat = AGL.aglChoosePixelFormat(0, 0, aglAttrib);
	glContext = AGL.aglCreateContext(pixelFormat, 0);
	//create the pbuffer for this context

//	System.out.println("context: " + glContext);
//	int window = OS.GetControlOwner(handle);
//	int port = OS.GetWindowPort(window);
//	System.out.println("setdrawable: " + AGL.aglSetDrawable(glContext, port));
}

public boolean isCurrent () {
	return AGL.aglGetCurrentContext () == glContext;
}

public void setCurrent () {
	if (AGL.aglGetCurrentContext () == glContext) return;
	AGL.aglSetCurrentContext (glContext);
}

}
