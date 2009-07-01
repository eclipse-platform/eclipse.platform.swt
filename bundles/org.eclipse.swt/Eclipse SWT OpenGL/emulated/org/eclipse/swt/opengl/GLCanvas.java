/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.widgets.*;

/**
 * GLCanvas is a widget capable of displaying OpenGL content.
 * 
 * @see GLData
 * @see <a href="http://www.eclipse.org/swt/snippets/#opengl">OpenGL snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.2
 */

public class GLCanvas extends Canvas {

/**
 * Create a GLCanvas widget using the attributes described in the GLData
 * object provided.
 *
 * @param parent a composite widget
 * @param style the bitwise OR'ing of widget styles
 * @param data the requested attributes of the GLCanvas
 *
 * @exception IllegalArgumentException
 * <ul><li>ERROR_NULL_ARGUMENT when the data is null
 *     <li>ERROR_UNSUPPORTED_DEPTH when the requested attributes cannot be provided</ul> 
 * </ul>
 */
public GLCanvas (Composite parent, int style, GLData data) {
	super (parent, style);
	SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
}

/**
 * Returns a GLData object describing the created context.
 *  
 * @return GLData description of the OpenGL context attributes
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public GLData getGLData () {
	checkWidget ();
	SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}

/**
 * Returns a boolean indicating whether the receiver's OpenGL context
 * is the current context.
 *  
 * @return true if the receiver holds the current OpenGL context,
 * false otherwise
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isCurrent () {
	checkWidget ();
	SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
	return false;
}

/**
 * Sets the OpenGL context associated with this GLCanvas to be the
 * current GL context.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCurrent () {
	checkWidget ();
	SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
}

/**
 * Swaps the front and back color buffers.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void swapBuffers () {
	checkWidget ();
	SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
}
}
