package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.widgets.*;

/**
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 */
public class Clipboard {
	
	private Display display;
	private final int MAX_RETRIES = 10;


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
	if (display.isDisposed() ) return null;
	
	Object result = null;
	
	int ig = OS.PhInputGroup(0);
	int cbdata = OS.PhClipboardPasteStart((short)ig);
	if (cbdata == 0) return result;
	try {
		String[] names = transfer.getTypeNames();
		int[] ids = transfer.getTypeIds();
		for (int i = 0; i < names.length; i++) {
			byte[] name = Converter.wcsToMbcs(null, names[i], true);
			name [Math.min(8, name.length) - 1] = (byte)0;
			int pClipHeader = OS.PhClipboardPasteType(cbdata, name);
			if (pClipHeader != 0) {
				PhClipHeader clipHeader = new PhClipHeader();
				OS.memmove(clipHeader, pClipHeader, PhClipHeader.sizeof);
				TransferData data = new TransferData();
				data.pData = clipHeader.data;
				data.length = clipHeader.length;
				data.type = ids[i];
				result = transfer.nativeToJava(data);
				break;
			}
		}
	} finally {
		OS.PhClipboardPasteFinish(cbdata);
	}
	
	return result;
}
public void setContents(Object[] data, Transfer[] transferAgents){
	if (display.isDisposed() ) return;
	
	if (data == null) {
		int ig = OS.PhInputGroup(0);
		if (OS.PhClipboardCopy((short)ig, 0, null) != 0) {
			DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
		}
		return;
	}
	if (transferAgents == null || data.length != transferAgents.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	PhClipHeader[] clips = new PhClipHeader[0];
	int count = 0;
	for (int i = 0; i < transferAgents.length; i++) {
		String[] names = transferAgents[i].getTypeNames();
		int[] ids = transferAgents[i].getTypeIds();
		for (int j = 0; j < names.length; j++) {
			TransferData transferData = new TransferData();
			transferData.type = ids[j];
			transferAgents[i].javaToNative(data[i], transferData);
			PhClipHeader clip = new PhClipHeader();
			clip.data = transferData.pData;
			clip.length = (short)transferData.length;
			byte[] temp = Converter.wcsToMbcs(null, names[j], false);
			byte[] type = new byte[8];
			System.arraycopy(temp, 0, type, 0, Math.min(type.length - 1, temp.length));
			clip.type_0 = type[0];
			clip.type_1 = type[1];
			clip.type_2 = type[2];
			clip.type_3 = type[3];
			clip.type_4 = type[4];
			clip.type_5 = type[5];
			clip.type_6 = type[6];
			clip.type_7 = type[7];
			PhClipHeader[] newClips = new PhClipHeader[count + 1];
			System.arraycopy(clips, 0, newClips, 0, clips.length);
			clips = newClips;
			clips[count++] = clip;
		}
	}
	
	if (count == 0) return;

	// Copy data to clipboard
	byte[] buffer = new byte[count * PhClipHeader.sizeof];
	byte[] temp = new byte[PhClipHeader.sizeof];
	for (int i = 0; i < count; i++) {
		OS.memmove(temp, clips[i], PhClipHeader.sizeof);
		System.arraycopy(temp, 0, buffer, i * PhClipHeader.sizeof, temp.length);
	}	
	int ig = OS.PhInputGroup(0);
	if (OS.PhClipboardCopy((short)ig, count, buffer) != 0) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	}
	
	// Free allocated data
	for (int i = 0; i < count; i++) {
		OS.free(clips[i].data);
	}
}
/*
 * Note: getAvailableTypeNames is a tool for writing a Transfer sub-class only.  It should
 * NOT be used within an application because it provides platform specfic 
 * information.
 */
public String[] getAvailableTypeNames() {
	String[] types = new String[0];
	int ig = OS.PhInputGroup(0);
	int cbdata = OS.PhClipboardPasteStart((short)ig);
	if (cbdata == 0) return types;
	try {
		int pClipHeader = 0;
		int n = 0;
		while ((pClipHeader = OS.PhClipboardPasteTypeN(cbdata, n++)) != 0) {
			PhClipHeader clipHeader = new PhClipHeader();
			OS.memmove(clipHeader, pClipHeader, PhClipHeader.sizeof);
			byte[] buffer = new byte[8];
			buffer[0] = clipHeader.type_0;
			buffer[1] = clipHeader.type_1;
			buffer[2] = clipHeader.type_2;
			buffer[3] = clipHeader.type_3;
			buffer[4] = clipHeader.type_4;
			buffer[5] = clipHeader.type_5;
			buffer[6] = clipHeader.type_6;
			buffer[7] = clipHeader.type_7;
			char [] unicode = Converter.mbcsToWcs (null, buffer);
			
			String[] newTypes = new String[types.length + 1];
			System.arraycopy(types, 0, newTypes, 0, types.length);
			newTypes[types.length] = new String (unicode).trim();
			types = newTypes;
		}
	} finally {
		OS.PhClipboardPasteFinish(cbdata);
	}
	return types;
}
}
