/**********************************************************************
 * Copyright (c) 2003, 2008 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;

 
public class NavFileOrFolderInfo {
	public short version;
	public boolean isFolder;
	public boolean  visible;
	public int creationDate;
	public int modificationDate;
	//TODO add union and struct
	//public struct fileInfo;
	//public struct folderInfo;
	public static final int sizeof = 268;
}
