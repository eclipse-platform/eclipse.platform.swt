/*******************************************************************************
 * Copyright (c) 2009, 2010s compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.qt;

public class SWQT {

	/* Widget Event Constants */

	/**
	 * The null event type (value is 0).
	 * 
	 * @since 3.0
	 */
	public static final int StyleSheetChange = 1024;

	/**
	 * 
	 */
	public static final String STYLESHEET_KEY = "stylesheet"; //$NON-NLS-1$

	// Qt QFont::Weight
	public static final int QT_FONTNORMAL = 50;
	public static final int QT_FONTBOLD = 75;

	// QFont::Style
	public static final int QFONT_STYLE_NORMAL = 0;
	public static final int QFONT_STYLE_ITALIC = 1;
	public static final int QFONT_STYLE_OBLIQUE = 2;

	// QFont::StyleStrategy
	public static final int QFONT_STYLESTRATEGY_PREFERDEFALUT = 0x0001;
	public static final int QFONT_STYLESTRATEGY_PREFERBITMAP = 0x0002;
	public static final int QFONT_STYLESTRATEGY_PREFERDEVICE = 0x0004;
	public static final int QFONT_STYLESTRATEGY_PREFEROUTLINE = 0x0008;
	public static final int QFONT_STYLESTRATEGY_FORCEOUTLINE = 0x0010;
	public static final int QFONT_STYLESTRATEGY_NOANTIALIAS = 0x0100;
	public static final int QFONT_STYLESTRATEGY_PREFERANTIALIAS = 0x0080;
	public static final int QFONT_STYLESTRATEGY_OPENGLCOMPATIABLE = 0x0200;
	public static final int QFONT_STYLESTRATEGY_NOFONTMERGING = 0x8000;
	public static final int QFONT_STYLESTRATEGY_PREFERMATCH = 0x0020;
	public static final int QFONT_STYLESTRATEGY_PREFERQUALITY = 0x0040;

}
