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


public class AtkSelectionIface {
	public int add_selection;
	public int clear_selection;
	public int ref_selection;
	public int get_selection_count;
	public int is_child_selected;
	public int remove_selection;
	public int select_all_selection;
	public int selection_changed;
}
