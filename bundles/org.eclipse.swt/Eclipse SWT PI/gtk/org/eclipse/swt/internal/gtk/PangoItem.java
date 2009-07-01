/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;

public class PangoItem {
	public int offset;
	public int length;
	public int num_chars;
	/** @field accessor=analysis.shape_engine,cast=(PangoEngineShape *) */
	public int /*long*/ analysis_shape_engine;
	/** @field accessor=analysis.lang_engine,cast=(PangoEngineLang *) */
	public int /*long*/ analysis_lang_engine;
	/** @field accessor=analysis.font,cast=(PangoFont *) */
	public int /*long*/ analysis_font;
	/** @field accessor=analysis.level */
	public byte analysis_level;
	/** @field accessor=analysis.language,cast=(PangoLanguage *) */
	public int /*long*/ analysis_language;
	/** @field accessor=analysis.extra_attrs,cast=(GSList *) */
	public int /*long*/ analysis_extra_attrs;
	public static final int sizeof = OS.PangoItem_sizeof();
}
