package org.eclipse.swt.internal.accessibility.gtk;

/*
 * Copyright (c) IBM Corp. 2000, 2002.  All rights reserved.
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
 
public class AtkActionIface {
//	GTypeInterface parent;
	public int do_action;
	public int get_n_actions;
	public int get_description;
	public int get_name;
	public int get_keybinding;
	public int set_description;	
//   AtkFunction             pad1;
//   AtkFunction             pad2;
}

