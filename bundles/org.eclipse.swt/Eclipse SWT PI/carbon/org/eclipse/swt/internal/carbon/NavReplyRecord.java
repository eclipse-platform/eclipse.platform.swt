/**********************************************************************
 * Copyright (c) 2003-2004 IBM Corp.
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

 
public class NavReplyRecord {
	public short version;
	public boolean validRecord;
	public boolean replacing;
	public boolean isStationery;
	public boolean translationNeeded;
	//AEDescList selection;
	public int selection_descriptorType;
	public int selection_dataHandle;
	public short keyScript;
	public int fileTranslation;
	public int reserved1;
	public int saveFileName;
	public boolean saveFileExtensionHidden;
	public byte reserved2;
	public byte[] reserved = new byte[225];
	public static final int sizeof = 256;	
}
