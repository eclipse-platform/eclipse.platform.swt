/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 *
 * Red Hat Inc.
 * - Binding to permit interfacing between Cairo and SWT
 * - Copyright (C) 2012 Red Hat Inc. All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.cairo;

public class cairo_rectangle_int_t {
	public int x;
	public int y;
	public int width;
	public int height;
	public static final int sizeof = Cairo.cairo_rectangle_int_t_sizeof();
}