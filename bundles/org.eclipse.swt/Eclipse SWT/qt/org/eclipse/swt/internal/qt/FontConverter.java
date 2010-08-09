/*******************************************************************************
 * Copyright (c) 2009, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.qt;

import java.util.LinkedList;
import java.util.List;

import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFont.Weight;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

/**
 * @author kriese
 * 
 */
public class FontConverter {

	private List<FontNameTuple> fontNames;
	private List<FontStyleTuple> fontStyles;

	public FontConverter() {
		fontNames = new LinkedList<FontNameTuple>();
		fontNames.add(new FontNameTuple("Arial", "Times")); //$NON-NLS-1$ //$NON-NLS-2$

		fontStyles = new LinkedList<FontStyleTuple>();
		fontStyles.add(new FontStyleTuple(SWT.BOLD, QFont.Weight.Bold));
		fontStyles.add(new FontStyleTuple(SWT.NORMAL, QFont.Weight.Normal));
	}

	public QFont getQtFont(Font swtFont) {

		FontNameTuple tuple = null;
		for (FontNameTuple f : fontNames) {
			if (f.getSwtFontName().equals(swtFont.getFontData()[0].getName())) {
				tuple = f;
			}
		}

		// swtFont.getFontData()[0].get

		FontStyleTuple style = null;
		for (FontStyleTuple s : fontStyles) {
			if (s.getSwtStyle() == swtFont.getFontData()[0].getStyle()) {
				style = s;
			}
		}

		if (null != tuple) {

			if (null != style) {
				return new QFont(tuple.getQtFontName(), swtFont.getFontData()[0].getHeight(), style.getQtStyle()
						.value());
			}

			return new QFont(tuple.getQtFontName(), swtFont.getFontData()[0].getHeight());
		}

		return null;
	}

	private static class FontStyleTuple {
		private QFont.Weight qtStyle;
		private int swtStyle;

		public FontStyleTuple(int swtStyle, Weight qtStyle) {
			super();
			this.qtStyle = qtStyle;
			this.swtStyle = swtStyle;
		}

		public QFont.Weight getQtStyle() {
			return qtStyle;
		}

		public int getSwtStyle() {
			return swtStyle;
		}
	}

	private static class FontNameTuple {
		private String swtFontName;
		private String qtFontName;

		public FontNameTuple(String swtFontName, String qtFontName) {
			this.swtFontName = swtFontName;
			this.qtFontName = qtFontName;
		}

		public String getSwtFontName() {
			return swtFontName;
		}

		public String getQtFontName() {
			return qtFontName;
		}
	}
}
