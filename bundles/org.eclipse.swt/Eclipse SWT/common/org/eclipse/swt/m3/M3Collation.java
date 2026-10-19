/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.m3;

import java.text.*;
import java.util.*;

import org.eclipse.swt.*;

/**
 * Locale-correct sort keys for widget rows (enhanced M3 language API).
 * <p>
 * {@link #keys(String[], Locale)} builds the collation key of every item once, with the
 * <code>java.text.Collator</code> of the locale, and keeps them in one byte lane. Comparing two
 * items is then an unsigned byte-range compare that allocates nothing, and agrees in sign with
 * <code>Collator.compare</code>. This is the order users expect: in German "ä" sorts with "a",
 * in Swedish after "z", and Turkish distinguishes the dotted and dotless i.
 * </p>
 *
 * @see M3SortKeys
 * @since 3.138
 */
public final class M3Collation {

	private M3Collation () {
	}

	/**
	 * Builds the sort keys of the items for the locale at tertiary strength (letters, accents and
	 * case are distinguished).
	 *
	 * @param items the items
	 * @param locale the locale
	 * @return the sort keys
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the items, an item or the locale is null</li>
	 * </ul>
	 */
	public static M3SortKeys keys (String [] items, Locale locale) {
		return keys (items, locale, Collator.TERTIARY);
	}

	/**
	 * Builds the sort keys of the items for the locale at the given strength.
	 *
	 * @param items the items
	 * @param locale the locale
	 * @param strength <code>Collator.PRIMARY</code>, <code>SECONDARY</code>, <code>TERTIARY</code> or <code>IDENTICAL</code>
	 * @return the sort keys
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the items, an item or the locale is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the strength is not one of the four</li>
	 * </ul>
	 */
	public static M3SortKeys keys (String [] items, Locale locale, int strength) {
		if (items == null || locale == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (strength != Collator.PRIMARY && strength != Collator.SECONDARY && strength != Collator.TERTIARY && strength != Collator.IDENTICAL) {
			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		}
		Collator collator = Collator.getInstance (locale);
		collator.setStrength (strength);
		int [] offsets = new int [items.length + 1];
		byte [][] built = new byte [items.length][];
		for (int i = 0; i < items.length; i++) {
			if (items [i] == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
			built [i] = collator.getCollationKey (items [i]).toByteArray ();
			offsets [i + 1] = Math.addExact (offsets [i], built [i].length);
		}
		byte [] lane = new byte [offsets [items.length]];
		for (int i = 0; i < items.length; i++) System.arraycopy (built [i], 0, lane, offsets [i], built [i].length);
		return new M3SortKeys (lane, offsets);
	}
}
