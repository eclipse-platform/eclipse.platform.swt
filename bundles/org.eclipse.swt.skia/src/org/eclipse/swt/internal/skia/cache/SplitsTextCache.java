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

/**
 * The analysis of texts for the text drawing is quite expensive, so cache and prevent exhaustive analysis.
 */
public class SplitsTextCache {

	public final String text;
	public final boolean replaceAmpersand;
	public final boolean delimiter;
	public final boolean tabulatorExpansion;

	private String[] splits;

	public SplitsTextCache(String text, boolean replaceAmpersand, boolean delimiter, boolean tabulatorExpansion) {
		super();
		this.text = text;
		this.replaceAmpersand = replaceAmpersand;
		this.delimiter = delimiter;
		this.tabulatorExpansion = tabulatorExpansion;
	}

	public String[] getSplits() {
		if (splits == null) {
			splits = text.split("\n", -1); //$NON-NLS-1$
		}
		return splits;
	}

	@Override
	public int hashCode() {
		return Objects.hash(delimiter, replaceAmpersand, tabulatorExpansion, text);
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
		final SplitsTextCache other = (SplitsTextCache) obj;
		return delimiter == other.delimiter && replaceAmpersand == other.replaceAmpersand
				&& tabulatorExpansion == other.tabulatorExpansion && Objects.equals(text, other.text);
	}





}
