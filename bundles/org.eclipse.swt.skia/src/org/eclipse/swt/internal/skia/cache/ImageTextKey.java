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
package org.eclipse.swt.internal.skia.cache;

import java.util.Objects;

import org.eclipse.swt.internal.canvasext.FontProperties;

public class ImageTextKey {
	public final String text;
	public final FontProperties fontProperties;
	public final boolean transparent;
	public final int background;
	public final int foreground;
	public final boolean antiAlias;

	public ImageTextKey(String text, FontProperties fontProperties, boolean transparent, int background, int foreground, boolean antiAlias) {
		this.text = text;
		this.fontProperties = fontProperties;
		this.transparent = transparent;
		this.background = background;
		this.foreground = foreground;
		this.antiAlias = antiAlias;
	}

	@Override
	public int hashCode() {

		if(transparent) {
			return Objects.hash(fontProperties, foreground, text, transparent, antiAlias);
		}
		return Objects.hash(background, fontProperties, foreground, text, antiAlias);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ImageTextKey other = (ImageTextKey) obj;

		if(transparent ) {
			return  Objects.equals(fontProperties, other.fontProperties)
					&& foreground == other.foreground && Objects.equals(text, other.text) &&
					other.transparent && antiAlias == other.antiAlias;
		}

		return background == other.background && Objects.equals(fontProperties, other.fontProperties)
				&& foreground == other.foreground && Objects.equals(text, other.text) && antiAlias == other.antiAlias;
	}



}