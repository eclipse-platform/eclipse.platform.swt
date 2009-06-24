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

 
public class NavReplyRecord {
	/** @field cast=(UInt16) */
	public short version;
	/** @field cast=(Boolean) */
	public boolean validRecord;
	/** @field cast=(Boolean) */
	public boolean replacing;
	/** @field cast=(Boolean) */
	public boolean isStationery;
	/** @field cast=(Boolean) */
	public boolean translationNeeded;
	//AEDescList selection;
	/** @field accessor=selection.descriptorType,cast=(DescType) */
	public int selection_descriptorType;
	/** @field accessor=selection.dataHandle,cast=(AEDataStorage) */
	public int selection_dataHandle;
	/** @field cast=(ScriptCode) */
	public short keyScript;
	/** @field cast=(FileTranslationSpecArrayHandle) */
	public int fileTranslation;
	/** @field cast=(UInt32) */
	public int reserved1;
	/** @field cast=(CFStringRef) */
	public int saveFileName;
	/** @field cast=(Boolean) */
	public boolean saveFileExtensionHidden;
	/** @field cast=(UInt8) */
	public byte reserved2;
	/** @field cast=(char[]) */
	public byte[] reserved = new byte[225];
	public static final int sizeof = 256;	
}
