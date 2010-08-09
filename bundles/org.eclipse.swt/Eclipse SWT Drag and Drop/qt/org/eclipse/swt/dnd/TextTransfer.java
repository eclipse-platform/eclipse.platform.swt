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
 * The class <code>TextTransfer</code> provides a platform specific mechanism
 * for converting plain text represented as a java <code>String</code> to a
 * platform specific representation of the data and vice versa.
 * 
 * <p>
 * An example of a java <code>String</code> containing plain text is shown
 * below:
 * </p>
 * 
 * <code><pre>
 *     String textData = "Hello World";
 * </code></pre>
 * 
 * @see Transfer
 */
public class TextTransfer extends ByteArrayTransfer {
	private static final String TYPENAME = "text/plain"; //$NON-NLS-1$
	private static final int TYPEID = registerType(TYPENAME);
	private static TextTransfer _instance = new TextTransfer();

	private TextTransfer() {
	}

	/**
	 * Returns the singleton instance of the TextTransfer class.
	 * 
	 * @return the singleton instance of the TextTransfer class
	 */
	public static TextTransfer getInstance() {
		return _instance;
	}

	/**
	 * This implementation of <code>javaToNative</code> converts plain text
	 * represented by a java <code>String</code> to a platform specific
	 * representation.
	 * 
	 * @param object
	 *            a java <code>String</code> containing text
	 * @param transferData
	 *            an empty <code>TransferData</code> object that will be filled
	 *            in on return with the platform specific format of the data
	 * 
	 * @see Transfer#nativeToJava
	 */
	@Override
	public void javaToNative(Object object, TransferData transferData) {
		if (!checkText(object) || !isSupportedType(transferData)) {
			DND.error(DND.ERROR_INVALID_DATA);
			return;
		}
		transferData.data = ((String) object).getBytes();
		transferData.format = TYPENAME;
		transferData.type = TYPEID;
	}

	private boolean checkText(Object object) {
		return object != null && object instanceof String && ((String) object).length() > 0;
	}

	/**
	 * This implementation of <code>nativeToJava</code> converts a platform
	 * specific representation of plain text to a java <code>String</code>.
	 * 
	 * @param transferData
	 *            the platform specific representation of the data to be
	 *            converted
	 * @return a java <code>String</code> containing text if the conversion was
	 *         successful; otherwise null
	 * 
	 * @see Transfer#javaToNative
	 */
	@Override
	public Object nativeToJava(TransferData transferData) {
		byte[] data = (byte[]) super.nativeToJava(transferData);
		if (data == null) {
			return null;
		}
		return new String(data);
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { TYPENAME };
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { TYPEID };
	}
}
