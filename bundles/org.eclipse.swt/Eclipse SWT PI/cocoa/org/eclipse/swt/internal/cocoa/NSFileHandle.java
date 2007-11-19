/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSFileHandle extends NSObject {

public NSFileHandle() {
	super();
}

public NSFileHandle(int id) {
	super(id);
}

public void acceptConnectionInBackgroundAndNotify() {
	OS.objc_msgSend(this.id, OS.sel_acceptConnectionInBackgroundAndNotify);
}

public void acceptConnectionInBackgroundAndNotifyForModes(NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_acceptConnectionInBackgroundAndNotifyForModes_1, modes != null ? modes.id : 0);
}

public NSData availableData() {
	int result = OS.objc_msgSend(this.id, OS.sel_availableData);
	return result != 0 ? new NSData(result) : null;
}

public void closeFile() {
	OS.objc_msgSend(this.id, OS.sel_closeFile);
}

public int fileDescriptor() {
	return OS.objc_msgSend(this.id, OS.sel_fileDescriptor);
}

public static id fileHandleForReadingAtPath(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSFileHandle, OS.sel_fileHandleForReadingAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id fileHandleForUpdatingAtPath(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSFileHandle, OS.sel_fileHandleForUpdatingAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id fileHandleForWritingAtPath(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSFileHandle, OS.sel_fileHandleForWritingAtPath_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id fileHandleWithNullDevice() {
	int result = OS.objc_msgSend(OS.class_NSFileHandle, OS.sel_fileHandleWithNullDevice);
	return result != 0 ? new id(result) : null;
}

public static id fileHandleWithStandardError() {
	int result = OS.objc_msgSend(OS.class_NSFileHandle, OS.sel_fileHandleWithStandardError);
	return result != 0 ? new id(result) : null;
}

public static id fileHandleWithStandardInput() {
	int result = OS.objc_msgSend(OS.class_NSFileHandle, OS.sel_fileHandleWithStandardInput);
	return result != 0 ? new id(result) : null;
}

public static id fileHandleWithStandardOutput() {
	int result = OS.objc_msgSend(OS.class_NSFileHandle, OS.sel_fileHandleWithStandardOutput);
	return result != 0 ? new id(result) : null;
}

public id initWithFileDescriptor_(int fd) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFileDescriptor_1, fd);
	return result != 0 ? new id(result) : null;
}

public id initWithFileDescriptor_closeOnDealloc_(int fd, boolean closeopt) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFileDescriptor_1closeOnDealloc_1, fd, closeopt);
	return result != 0 ? new id(result) : null;
}

public long offsetInFile() {
	return (long)OS.objc_msgSend(this.id, OS.sel_offsetInFile);
}

public NSData readDataOfLength(int length) {
	int result = OS.objc_msgSend(this.id, OS.sel_readDataOfLength_1, length);
	return result != 0 ? new NSData(result) : null;
}

public NSData readDataToEndOfFile() {
	int result = OS.objc_msgSend(this.id, OS.sel_readDataToEndOfFile);
	return result != 0 ? new NSData(result) : null;
}

public void readInBackgroundAndNotify() {
	OS.objc_msgSend(this.id, OS.sel_readInBackgroundAndNotify);
}

public void readInBackgroundAndNotifyForModes(NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_readInBackgroundAndNotifyForModes_1, modes != null ? modes.id : 0);
}

public void readToEndOfFileInBackgroundAndNotify() {
	OS.objc_msgSend(this.id, OS.sel_readToEndOfFileInBackgroundAndNotify);
}

public void readToEndOfFileInBackgroundAndNotifyForModes(NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_readToEndOfFileInBackgroundAndNotifyForModes_1, modes != null ? modes.id : 0);
}

public long seekToEndOfFile() {
	return (long)OS.objc_msgSend(this.id, OS.sel_seekToEndOfFile);
}

public void seekToFileOffset(long offset) {
	OS.objc_msgSend(this.id, OS.sel_seekToFileOffset_1, offset);
}

public void synchronizeFile() {
	OS.objc_msgSend(this.id, OS.sel_synchronizeFile);
}

public void truncateFileAtOffset(long offset) {
	OS.objc_msgSend(this.id, OS.sel_truncateFileAtOffset_1, offset);
}

public void waitForDataInBackgroundAndNotify() {
	OS.objc_msgSend(this.id, OS.sel_waitForDataInBackgroundAndNotify);
}

public void waitForDataInBackgroundAndNotifyForModes(NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_waitForDataInBackgroundAndNotifyForModes_1, modes != null ? modes.id : 0);
}

public void writeData(NSData data) {
	OS.objc_msgSend(this.id, OS.sel_writeData_1, data != null ? data.id : 0);
}

}
