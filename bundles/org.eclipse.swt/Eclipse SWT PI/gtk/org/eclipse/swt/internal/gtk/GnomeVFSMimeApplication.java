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
package org.eclipse.swt.internal.gtk;

 
public class GnomeVFSMimeApplication {
	public int id;
	public int name;
	public int command;
	public boolean can_open_multiple_files;
	public int expects_uris;
	public int supported_uri_schemes;
	public boolean requires_terminal;
	public static final int sizeof = GNOME.GnomeVFSMimeApplication_sizeof();
}
