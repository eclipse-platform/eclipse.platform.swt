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
package org.eclipse.swt.graphics;

/**
 * @noreference This class is not intended to be referenced by clients.
 */
public class PatternProperties {
	private final Image image;
	private float baseX1, baseY1, baseX2, baseY2;
	private Color color1, color2;
	private int alpha1, alpha2;

	public PatternProperties(Image image, float baseX1, float baseY1, float baseX2, float baseY2, Color color1,
			int alpha1, Color color2, int alpha2) {
		super();
		this.image = image;
		this.baseX1 = baseX1;
		this.baseY1 = baseY1;
		this.baseX2 = baseX2;
		this.baseY2 = baseY2;
		this.color1 = color1;
		this.color2 = color2;
		this.alpha1 = alpha1;
		this.alpha2 = alpha2;
	}

	public PatternProperties(Image image) {
		this.image = image;
	}

	public PatternProperties(float baseX1, float baseY1, float baseX2, float baseY2, Color color1, int alpha1,
			Color color2, int alpha2) {
		super();
		this.image = null;
		this.baseX1 = baseX1;
		this.baseY1 = baseY1;
		this.baseX2 = baseX2;
		this.baseY2 = baseY2;
		this.color1 = color1;
		this.color2 = color2;
		this.alpha1 = alpha1;
		this.alpha2 = alpha2;
	}

	public Image getImage() {
		return image;
	}

	public float getBaseX1() {
		return baseX1;
	}

	public float getBaseY1() {
		return baseY1;
	}

	public float getBaseX2() {
		return baseX2;
	}

	public float getBaseY2() {
		return baseY2;
	}

	public Color getColor1() {
		return color1;
	}

	public Color getColor2() {
		return color2;
	}

	public int getAlpha1() {
		return alpha1;
	}

	public int getAlpha2() {
		return alpha2;
	}

	public static PatternProperties get(Pattern pattern) {
		return pattern.getProperties();
	}
}
