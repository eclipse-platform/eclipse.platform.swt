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
package org.eclipse.swt.internal.motif;

 
public class GnomeVFSMimeApplication {
	public int id;
	public int name;
	public int command;
	public boolean can_open_multiple_files;
	public int expects_uris;
	public int supported_uri_schemes;
	public boolean requires_terminal;
	public static final int sizeof = 28;
}
