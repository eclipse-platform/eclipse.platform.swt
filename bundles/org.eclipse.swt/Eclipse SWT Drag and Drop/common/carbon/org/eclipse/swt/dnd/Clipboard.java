package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CFRange;

public class Clipboard {
	
	private Display display;

public Clipboard(Display display) {	
	checkSubclass ();
	if (display == null) {
		display = Display.getCurrent();
		if (display == null) {
			display =  Display.getDefault();
		}
	}
	if (display.getThread() != Thread.currentThread()) {
		SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = Clipboard.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

public void dispose () {
	display = null;
}

public Object getContents(Transfer transfer) {
	if (display == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.isDisposed()) DND.error(SWT.ERROR_DEVICE_DISPOSED);
	if (transfer == null) DND.error(SWT.ERROR_NULL_ARGUMENT);
		
	int[] scrapHandle = new int[1];
	OS.GetCurrentScrap(scrapHandle);
	int scrap= scrapHandle[0];
		
	// Does Clipboard have data in required format?
	int[] typeIds = transfer.getTypeIds();
	for (int i= 0; i < typeIds.length; i++) {
		int type = typeIds[i];
		int[] size = new int[1];
		if (OS.GetScrapFlavorSize(scrap, type, size) == OS.noErr) {
			if (size[0] > 0) {
				TransferData tdata = new TransferData();
				tdata.type = type;		
				tdata.data = new byte[size[0]];
				OS.GetScrapFlavorData(scrap, type, size, tdata.data);
				return transfer.nativeToJava(tdata);
			}
		}
	}		
	return null;	// No data available for this transfer
}

public void setContents(Object[] data, Transfer[] dataTypes) {
	if (display == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.isDisposed()) DND.error(SWT.ERROR_DEVICE_DISPOSED);
	if (data == null || dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	OS.ClearCurrentScrap();
	int[] scrapHandle = new int[1];
	OS.GetCurrentScrap(scrapHandle);
	int scrap = scrapHandle[0];
	// copy data directly over to System clipboard (not deferred)
	for (int i= 0; i < dataTypes.length; i++) {
		int[] ids = dataTypes[i].getTypeIds();
		for (int j= 0; j < ids.length; j++) {
			TransferData transferData = new TransferData();
			transferData.type = ids[j];
			dataTypes[i].javaToNative(data[i], transferData);
			if (transferData.result != OS.noErr)
				DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
			if (OS.PutScrapFlavor(scrap, transferData.type, 0, transferData.data.length, transferData.data) != OS.noErr){
				DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
			}
		}
	}
}

public String[] getAvailableTypeNames() {
	if (display == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.isDisposed()) DND.error(SWT.ERROR_DEVICE_DISPOSED);
	
	int[] scrapHandle = new int[1];
	OS.GetCurrentScrap(scrapHandle);
	int scrap = scrapHandle[0];	
	int[] count = new int[1];
	OS.GetScrapFlavorCount(scrap, count);
	if (count [0] == 0) return new String [0];
	int[] info = new int[count[0] * 2];
	OS.GetScrapFlavorInfoList(scrap, count, info);
	String[] result = new String[count[0]];
	for (int i= 0; i < count [0]; i++) {
		int type = info[i*2];
		StringBuffer sb = new StringBuffer();
		sb.append((char)((type & 0xff000000) >> 24));
		sb.append((char)((type & 0x00ff0000) >> 16));
		sb.append((char)((type & 0x0000ff00) >> 8));
		sb.append((char)((type & 0x000000ff) >> 0));
		result[i] = sb.toString();
	}
	return result;
}
}
