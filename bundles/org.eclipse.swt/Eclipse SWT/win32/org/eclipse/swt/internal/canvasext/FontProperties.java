/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.canvasext;

import java.util.*;

import org.eclipse.swt.graphics.*;

/**
 * Provides extended information about a font, including its stretch factor and other properties.
 */
public class FontProperties {

	// Standard OpenType stretch values
	private static final Map<String, Integer> STRETCH_MAP = new HashMap<>();
	private static final Set<String> REGION_KEYS = new HashSet<>();

	static {
		STRETCH_MAP.put("UltraCondensed", 1); // 50.0%
		STRETCH_MAP.put("ExtraCondensed", 2); // 62.5%
		STRETCH_MAP.put("Condensed", 3); // 75.0%
		STRETCH_MAP.put("SemiCondensed", 4); // 87.5%
		STRETCH_MAP.put("Normal", 5); // 100.0%
		STRETCH_MAP.put("Medium", 5);
		STRETCH_MAP.put("SemiExpanded", 6); // 112.5%
		STRETCH_MAP.put("Expanded", 7); // 125.0%
		STRETCH_MAP.put("ExtraExpanded", 8); // 150.0%
		STRETCH_MAP.put("UltraExpanded", 9); // 200.0%


		REGION_KEYS.add(" Greek");
		REGION_KEYS.add(" TUR");
		REGION_KEYS.add(" Baltic");
		REGION_KEYS.add(" CE");
		REGION_KEYS.add(" CYR");
		REGION_KEYS.add(" Transparent");
	}





	public int lfHeight;
	public int lfWidth;
	public int lfEscapement;
	public int lfOrientation;
	public int lfWeight;
	public byte lfItalic;
	public byte lfUnderline;
	public byte lfStrikeOut;
	public String name;

	private FontProperties() {

	}

	private static FontProperties getFontProperties(FontData fd) {
		var fp = new FontProperties();
		var d = fd.data;

		String name = fd.getName();

		for(String local : REGION_KEYS) {
			if(name.endsWith(local)) {
				name = name.substring(0, name.length() - local.length());
				break;
			}

		}

		var fontName = analyzeManual(name);

		fp.name = fontName.baseName;
		fp.lfHeight = fd.getHeight();
		fp.lfItalic = d.lfItalic;
		fp.lfEscapement = d.lfEscapement;
		fp.lfOrientation = d.lfOrientation;
		fp.lfStrikeOut = d.lfStrikeOut;
		fp.lfUnderline = d.lfUnderline;
		if(d.lfWeight == 0)
			fp.lfWeight = 400; // Normal weight
		else
			fp.lfWeight = d.lfWeight;
		var stretch = STRETCH_MAP.get(fontName.detectedStretch);
		if(stretch != null)
			fp.lfWidth = stretch;

		return fp;
	}

	public static FontProperties getFontProperties(Font font) {
		return getFontProperties(font.getFontData()[0]);
	}

	private static FontName analyzeManual(String fullDescription) {
		// known stretch keywords
		String[] stretchKeywords = { "UltraCondensed", "ExtraCondensed", "Condensed", "SemiCondensed", "SemiExpanded",
				"Expanded" };

		String baseName = fullDescription;
		String detectedStretch = null;

		for (String keyword : stretchKeywords) {
			if (fullDescription.contains(keyword)) {
				detectedStretch = keyword;
				baseName = fullDescription;
				break;
			}
		}

		return new FontName(baseName, detectedStretch);
	}

	private record FontName(String baseName, String detectedStretch) {
	}

	@Override
	public int hashCode() {
		return Objects.hash(lfEscapement, lfHeight, lfItalic, lfOrientation, lfStrikeOut, lfUnderline, lfWeight,
				lfWidth, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FontProperties other = (FontProperties) obj;
		return lfEscapement == other.lfEscapement && lfHeight == other.lfHeight && lfItalic == other.lfItalic
				&& lfOrientation == other.lfOrientation && lfStrikeOut == other.lfStrikeOut
				&& lfUnderline == other.lfUnderline && lfWeight == other.lfWeight && lfWidth == other.lfWidth
				&& Objects.equals(name, other.name);
	}




}
