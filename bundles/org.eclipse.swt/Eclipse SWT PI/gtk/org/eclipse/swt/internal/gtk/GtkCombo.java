package org.eclipse.swt.internal.gtk;

/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

public class GtkCombo extends GtkHBox {
	public int entry;
	public int button;
	public int popup;
	public int popwin;
	public int list;
	public int entry_change_id;
	public int list_change_id;
	public int value_in_list; // bitfield : 1
	public int ok_if_empty; // bitfield : 1
	public int case_sensitive; // bitfield : 1
	public int use_arrows; // bitfield : 1
	public int use_arrows_always; // bitfield : 1
	public short current_button;
	public int activate_id;
	public static final int sizeof = 104;
}
