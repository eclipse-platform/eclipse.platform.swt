/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;


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
