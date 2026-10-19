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
 *     Hitesh Soliwal (Synexia) - adapted from TV_5.0 treeviewer FilterEditPanel and
 *         ViewerFilterUI (edit area, tool items) (docs/m3/sort-filter/PROVENANCE.tsv)
 *******************************************************************************/
package org.eclipse.swt.m3;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * The edit panel of one column filter: an enablement combo (empty entry = no filter, then the
 * column), a match-type combo, the pattern text, tool items (default text, paste, close) and a
 * feedback line that shows how to write the pattern or why it is not valid. Edits apply after a
 * short delay (TV_5.0 DeferredRun); an invalid pattern leaves the filter as it was. Return and
 * Escape close the panel's popup.
 */
final class M3FilterEditPanel extends Composite {

	static final int DELAY = 250;
	static final int [] MODES = {
		M3TextMatch.WILDCARD | M3TextMatch.IGNORE_CASE,
		M3TextMatch.WILDCARD | M3TextMatch.IGNORE_CASE | M3TextMatch.NEGATE,
		M3TextMatch.REGEX,
		M3TextMatch.REGEX | M3TextMatch.NEGATE,
		M3TextMatch.CONTAINS | M3TextMatch.IGNORE_CASE,
		M3TextMatch.STARTS_WITH | M3TextMatch.IGNORE_CASE,
		M3TextMatch.EXACT | M3TextMatch.IGNORE_CASE,
	};
	private static final String [] LABELS = {
		M3Messages.MATCHES, M3Messages.DOES_NOT_MATCH, M3Messages.MATCH_REGEXP, M3Messages.DOES_NOT_MATCH_REGEXP,
		M3Messages.CONTAINS, M3Messages.STARTS_WITH, M3Messages.EQUALS,
	};

	private final M3ColumnFilter filter;
	private final int column;
	private final Runnable close, applied;
	final Combo enablement, matchType;
	final Text pattern;
	final Label feedback;
	final ToolItem defaultItem, pasteItem, closeItem;
	private final Runnable apply = this::apply;

	M3FilterEditPanel (Composite parent, M3ColumnFilter filter, int column, String columnName, Runnable close, Runnable applied) {
		super (parent, SWT.NONE);
		this.filter = filter;
		this.column = column;
		this.close = close;
		this.applied = applied;
		setLayout (new GridLayout (4, false));
		enablement = new Combo (this, SWT.READ_ONLY);
		enablement.setItems ("", columnName); //$NON-NLS-1$
		matchType = new Combo (this, SWT.READ_ONLY);
		for (String label : LABELS) matchType.add (M3Messages.get (label));
		pattern = new Text (this, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData (SWT.FILL, SWT.CENTER, true, false);
		data.widthHint = 100;
		pattern.setLayoutData (data);
		ToolBar bar = new ToolBar (this, SWT.FLAT);
		defaultItem = item (bar, M3Messages.DEFAULT, M3Messages.SET_DEFAULT_TEXT);
		pasteItem = item (bar, M3Messages.PASTE, M3Messages.PASTE_FROM_CLIPBOARD);
		closeItem = item (bar, M3Messages.CLOSE, M3Messages.CLOSE_FILTER);
		feedback = new Label (this, SWT.WRAP);
		feedback.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false, 4, 1));
		initialize ();
		hook ();
	}

	private static ToolItem item (ToolBar bar, String text, String toolTip) {
		ToolItem item = new ToolItem (bar, SWT.PUSH);
		item.setText (M3Messages.get (text));
		item.setToolTipText (M3Messages.get (toolTip));
		return item;
	}

	private void initialize () {
		M3TextMatch match = filter.getMatch (column);
		enablement.select (match == null ? 0 : 1);
		matchType.select (match == null ? 0 : indexOf (match.mode ()));
		pattern.setText (match == null ? defaultText () : match.pattern ());
		enableControls ();
		feedback.setText (info ());
	}

	static int indexOf (int mode) {
		for (int i = 0; i < MODES.length; i++) {
			if (MODES [i] == mode) return i;
		}
		for (int i = 0; i < MODES.length; i++) {
			if ((MODES [i] & ~M3TextMatch.IGNORE_CASE) == (mode & ~M3TextMatch.IGNORE_CASE)) return i;
		}
		return 0;
	}

	private void hook () {
		Listener listener = event -> {
			switch (event.type) {
				case SWT.Modify: schedule (); break;
				case SWT.Selection: tool (event.widget); break;
				default: traverse (event); break;
			}
		};
		for (Control control : new Control [] {enablement, matchType, pattern}) {
			control.addListener (SWT.Modify, listener);
			control.addListener (SWT.Traverse, listener);
		}
		for (ToolItem item : new ToolItem [] {defaultItem, pasteItem, closeItem}) item.addListener (SWT.Selection, listener);
	}

	private void traverse (Event event) {
		if (event.detail == SWT.TRAVERSE_RETURN || event.detail == SWT.TRAVERSE_ESCAPE) {
			event.doit = false;
			if (event.detail == SWT.TRAVERSE_RETURN) apply ();
			close.run ();
		}
	}

	private void tool (Widget item) {
		if (item == defaultItem) {
			pattern.setText (defaultText ());
		} else if (item == pasteItem) {
			Clipboard clipboard = new Clipboard (getDisplay ());
			Object text = clipboard.getContents (TextTransfer.getInstance ());
			clipboard.dispose ();
			if (text instanceof String) pattern.setText ((String) text);
		} else {
			close.run ();
		}
	}

	private void schedule () {
		enableControls ();
		getDisplay ().timerExec (DELAY, apply);
	}

	/* applies the panel to the filter now; returns whether the pattern was valid */
	boolean apply () {
		if (isDisposed ()) return false;
		getDisplay ().timerExec (-1, apply);
		if (enablement.getSelectionIndex () == 0) {
			if (filter.getMatch (column) != null) filter.clear (column);
			feedback.setText (info ());
			applied.run ();
			return true;
		}
		int mode = MODES [matchType.getSelectionIndex ()];
		String problem = M3TextMatch.check (pattern.getText (), mode);
		if (!problem.isEmpty ()) {
			feedback.setText (M3Messages.get (M3Messages.VALIDITY_ERROR) + ' ' + problem);
			return false;
		}
		filter.setText (column, pattern.getText (), mode);
		feedback.setText (info ());
		applied.run ();
		return true;
	}

	private void enableControls () {
		boolean enabled = enablement.getSelectionIndex () > 0;
		matchType.setEnabled (enabled);
		pattern.setEnabled (enabled);
	}

	private String defaultText () {
		int kind = MODES [Math.max (0, matchType.getSelectionIndex ())] & 0xFF;
		return kind == M3TextMatch.WILDCARD ? "*" : kind == M3TextMatch.REGEX ? ".*" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private String info () {
		int kind = MODES [Math.max (0, matchType.getSelectionIndex ())] & 0xFF;
		return M3Messages.get (kind == M3TextMatch.WILDCARD ? M3Messages.WILDCARD_INFO : kind == M3TextMatch.REGEX ? M3Messages.REGEXP_INFO : M3Messages.TEXT_INFO);
	}
}
