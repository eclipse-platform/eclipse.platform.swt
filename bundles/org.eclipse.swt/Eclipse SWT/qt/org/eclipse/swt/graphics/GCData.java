/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.SWT;

/**
 * Instances of this class are descriptions of GCs in terms of unallocated
 * platform-specific data fields.
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the public API for SWT.
 * It is marked public only so that it can be shared within the packages
 * provided by SWT. It is not available on all platforms, and should never be
 * called from application code.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noinstantiate This class is not intended to be instantiated by clients.
 */

public final class GCData {
	public Device device;
	public int style, state = -1;
	public Color foregroundColor = null;
	public Color backgroundColor = null;
	public Font font;
	public Pattern foregroundPattern;
	public Pattern backgroundPattern;
	public int lineStyle = SWT.LINE_SOLID;
	public float lineWidth;
	public int lineCap = SWT.CAP_FLAT;
	public int lineJoin = SWT.JOIN_MITER;
	public float lineDashesOffset;
	public float[] lineDashes;
	public float lineMiterLimit = 10;
	public int alpha = 0xFF;
	public Image image;
	public int layout = -1;
	public int uiState = 0;
	public boolean focusDrawn;
	public int handle;
}
