/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *******************************************************************************/
package org.eclipse.swt.internal.accessibility.gtk;

 
public class GTypeInfo {
	public short class_size;
	public int base_init;
	public int base_finalize;
	public int class_init;
	public int class_finalize;
	public int class_data;
	public short instance_size;
	public short n_preallocs;
	public int instance_init;
	public int value_table;
	public static final int sizeof = 36; //(34 from struct plus 2 bits caused by compiler aligment)	
}
