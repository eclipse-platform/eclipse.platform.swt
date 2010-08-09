/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.dnd;

/**
 * The class <code>FileTransfer</code> provides a platform specific mechanism
 * for converting a list of files represented as a java <code>String[]</code> to
 * a platform specific representation of the data and vice versa. Each
 * <code>String</code> in the array contains the absolute path for a single file
 * or directory.
 * 
 * <p>
 * An example of a java <code>String[]</code> containing a list of files is
 * shown below:
 * </p>
 * 
 * <code><pre>
 *     File file1 = new File("C:\temp\file1");
 *     File file2 = new File("C:\temp\file2");
 *     String[] fileData = new String[2];
 *     fileData[0] = file1.getAbsolutePath();
 *     fileData[1] = file2.getAbsolutePath();
 * </code></pre>
 * 
 * @see Transfer
 */
public class FileTransfer extends ByteArrayTransfer {
	/**
	 *
	 */
	private static final String SEPARATOR = "\n";
	private static final String URI_LIST = "text/uri-list"; //$NON-NLS-1$
	private static final int URI_LIST_ID = registerType(URI_LIST);

	private FileTransfer() {
	}

	private static FileTransfer _instance = new FileTransfer();

	/**
	 * Returns the singleton instance of the FileTransfer class.
	 * 
	 * @return the singleton instance of the FileTransfer class
	 */
	public static FileTransfer getInstance() {
		return _instance;
	}

	/**
	 * This implementation of <code>javaToNative</code> converts a list of file
	 * names represented by a java <code>String[]</code> to a platform specific
	 * representation. Each <code>String</code> in the array contains the
	 * absolute path for a single file or directory.
	 * 
	 * @param object
	 *            a java <code>String[]</code> containing the file names to be
	 *            converted
	 * @param transferData
	 *            an empty <code>TransferData</code> object that will be filled
	 *            in on return with the platform specific format of the data
	 * 
	 * @see Transfer#nativeToJava
	 */
	@Override
	public void javaToNative(Object object, TransferData transferData) {
		if (!checkStringArray(object) || !isSupportedType(transferData)) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		String[] files = (String[]) object;
		for (String file : files) {
			if (file == null || file.length() == 0) {
				continue;
			}
			System.out.println("dnd file: " + file);
			if (first) {
				first = false;
			} else {
				sb.append(SEPARATOR);
			}
			sb.append("file://");
			sb.append(file);
		}
		transferData.data = sb.toString().getBytes();
		transferData.format = URI_LIST;
	}

	/**
	 * This implementation of <code>nativeToJava</code> converts a platform
	 * specific representation of a list of file names to a java
	 * <code>String[]</code>. Each String in the array contains the absolute
	 * path for a single file or directory.
	 * 
	 * @param transferData
	 *            the platform specific representation of the data to be
	 *            converted
	 * @return a java <code>String[]</code> containing a list of file names if
	 *         the conversion was successful; otherwise null
	 * 
	 * @see Transfer#javaToNative
	 */
	@Override
	public Object nativeToJava(TransferData transferData) {
		byte[] data = (byte[]) super.nativeToJava(transferData);
		if (data == null) {
			return null;
		}
		String[] files = new String(data).split(SEPARATOR);
		return files;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { URI_LIST_ID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { URI_LIST };
	}

	boolean checkStringArray(Object object) {
		if (object == null || !(object instanceof String[]) || ((String[]) object).length == 0) {
			return false;
		}
		String[] strings = (String[]) object;
		for (int i = 0; i < strings.length; i++) {
			if (strings[i] == null || strings[i].length() == 0) {
				return false;
			}
		}
		return true;
	}
}
