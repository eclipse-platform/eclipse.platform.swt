/*******************************************************************************
 * Copyright (c) 2008 Nokia Corporation and/or its subsidiary(-ies).
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Nokia Corporation - initial implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux 
 *******************************************************************************/

package org.eclipse.swt.internal.qt;

import com.trolltech.qt.gui.QFont.Style;
import com.trolltech.qt.gui.QFont.StyleStrategy;

public final class QtSupplementaryFontData {
	public int underline = -1;
	public int overline = -1;
	public int strikeOut = -1;
	public int stretch = -1;
	public int fixedPitch = -1;
	public Style style = null;
	public int weight = -1;
	public StyleStrategy styleStrategy = null;
}