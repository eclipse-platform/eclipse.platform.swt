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
 *     Hitesh Soliwal (Synexia) - texts from TV_5.0 treeviewer filters/resources/messages.properties
 *         (docs/m3/sort-filter/PROVENANCE.tsv)
 *******************************************************************************/
package org.eclipse.swt.m3;

import java.util.*;

import org.eclipse.swt.*;

/*
 * The texts of the M3 filter UI: SWT message keys (SWTMessages.properties) with their English
 * text as the fallback when the running SWT carries no such key.
 */
final class M3Messages {

	static final String MATCHES = "SWT_M3_Filter_Matches"; //$NON-NLS-1$
	static final String DOES_NOT_MATCH = "SWT_M3_Filter_Does_Not_Match"; //$NON-NLS-1$
	static final String MATCH_REGEXP = "SWT_M3_Filter_Match_Regexp"; //$NON-NLS-1$
	static final String DOES_NOT_MATCH_REGEXP = "SWT_M3_Filter_Does_Not_Match_Regexp"; //$NON-NLS-1$
	static final String CONTAINS = "SWT_M3_Filter_Contains"; //$NON-NLS-1$
	static final String STARTS_WITH = "SWT_M3_Filter_Starts_With"; //$NON-NLS-1$
	static final String EQUALS = "SWT_M3_Filter_Equals"; //$NON-NLS-1$
	static final String WILDCARD_INFO = "SWT_M3_Filter_Wildcard_Info"; //$NON-NLS-1$
	static final String REGEXP_INFO = "SWT_M3_Filter_Regexp_Info"; //$NON-NLS-1$
	static final String TEXT_INFO = "SWT_M3_Filter_Text_Info"; //$NON-NLS-1$
	static final String VALIDITY_ERROR = "SWT_M3_Filter_Validity_Error"; //$NON-NLS-1$
	static final String SET_DEFAULT_TEXT = "SWT_M3_Filter_Set_Default_Text"; //$NON-NLS-1$
	static final String DEFAULT = "SWT_M3_Filter_Default"; //$NON-NLS-1$
	static final String PASTE = "SWT_M3_Filter_Paste"; //$NON-NLS-1$
	static final String PASTE_FROM_CLIPBOARD = "SWT_M3_Filter_Paste_From_Clipboard"; //$NON-NLS-1$
	static final String CLOSE = "SWT_M3_Filter_Close"; //$NON-NLS-1$
	static final String CLOSE_FILTER = "SWT_M3_Filter_Close_Tooltip"; //$NON-NLS-1$

	private static final Map<String, String> ENGLISH = Map.ofEntries (
		Map.entry (MATCHES, "matches"), //$NON-NLS-1$
		Map.entry (DOES_NOT_MATCH, "doesn't match"), //$NON-NLS-1$
		Map.entry (MATCH_REGEXP, "matches regexp"), //$NON-NLS-1$
		Map.entry (DOES_NOT_MATCH_REGEXP, "doesn't match regexp"), //$NON-NLS-1$
		Map.entry (CONTAINS, "contains"), //$NON-NLS-1$
		Map.entry (STARTS_WITH, "starts with"), //$NON-NLS-1$
		Map.entry (EQUALS, "equals"), //$NON-NLS-1$
		Map.entry (WILDCARD_INFO, "Enter ',' separated wildcard (*,?) patterns."), //$NON-NLS-1$
		Map.entry (REGEXP_INFO, "Enter ',' separated regular expressions."), //$NON-NLS-1$
		Map.entry (TEXT_INFO, "Enter ',' separated texts."), //$NON-NLS-1$
		Map.entry (VALIDITY_ERROR, "Error in creating a matcher, hence not applied:"), //$NON-NLS-1$
		Map.entry (SET_DEFAULT_TEXT, "Set default text"), //$NON-NLS-1$
		Map.entry (DEFAULT, "Default"), //$NON-NLS-1$
		Map.entry (PASTE, "Paste"), //$NON-NLS-1$
		Map.entry (PASTE_FROM_CLIPBOARD, "Paste contents of the clipboard"), //$NON-NLS-1$
		Map.entry (CLOSE, "Close"), //$NON-NLS-1$
		Map.entry (CLOSE_FILTER, "Close the filter")); //$NON-NLS-1$

	private M3Messages () {
	}

	static String get (String key) {
		String message = SWT.getMessage (key);
		return message.startsWith (key) ? ENGLISH.getOrDefault (key, key) : message;
	}
}
