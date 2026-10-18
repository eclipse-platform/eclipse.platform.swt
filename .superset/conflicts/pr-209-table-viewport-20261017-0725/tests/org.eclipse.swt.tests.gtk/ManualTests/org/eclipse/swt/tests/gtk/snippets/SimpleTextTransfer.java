package org.eclipse.swt.tests.gtk.snippets;


/*******************************************************************************
 * Copyright (c) 2004, 2018 Actuate Corporation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/


import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 *
 */

public class SimpleTextTransfer extends ByteArrayTransfer
{

	private static final SimpleTextTransfer instance = new SimpleTextTransfer( );

	private static final String TYPE_NAME = "simple-widget.text-transfer";//$NON-NLS-1$

	private static final int TYPEID = registerType( TYPE_NAME );

	private SimpleTextTransfer( )
	{

	}

	public static SimpleTextTransfer getInstance( )
	{
		return instance;
	}

	@Override
	protected int[] getTypeIds( )
	{
		return new int[]{
			TYPEID
		};
	}

	@Override
	protected String[] getTypeNames( )
	{
		return new String[]{
			TYPE_NAME
		};
	}

	private boolean checkText( Object object )
	{
		return ( object != null && object instanceof String && ( (String) object ).length( ) > 0 );
	}

	@Override
	protected boolean validate( Object object )
	{
		return checkText( object );
	}

	/**
	 * This implementation of <code>javaToNative</code> converts plain widget.text
	 * represented by a java <code>String</code> to a platform specific
	 * representation.
	 * 
	 * @param object
	 *            a java <code>String</code> containing widget.text
	 * @param transferData
	 *            an empty <code>TransferData</code> object; this object will
	 *            be filled in on return with the platform specific format of
	 *            the data
	 * 
	 * @see Transfer#javaToNative
	 */
	@Override
	public void javaToNative( Object object, TransferData transferData )
	{
		if ( checkText( object ) && isSupportedType( transferData ) )
		{
			super.javaToNative( ( (String) object ).getBytes( ), transferData );
		}
	}

	/**
	 * This implementation of <code>nativeToJava</code> converts a platform
	 * specific representation of plain widget.text to a java <code>String</code>.
	 * 
	 * @param transferData
	 *            the platform specific representation of the data to be
	 *            converted
	 * @return a java <code>String</code> containing widget.text if the conversion
	 *         was successful; otherwise null
	 * 
	 * @see Transfer#nativeToJava
	 */
	@Override
	public Object nativeToJava( TransferData transferData )
	{
		if ( !isSupportedType( transferData ) )
		{
			return null;
		}
		byte[] bytes = (byte[]) super.nativeToJava( transferData );
		return new String( bytes );
	}

}
