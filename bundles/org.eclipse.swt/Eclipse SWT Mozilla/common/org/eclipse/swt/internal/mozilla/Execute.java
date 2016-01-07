/*******************************************************************************
 * Copyright (c) 2014 Neil Rashbrook and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Neil Rashbrook <neil@parkwaycc.co.uk> - Bug 429739
 *    Matthew Painter <matthew.painter@import.io>
 *******************************************************************************/
package org.eclipse.swt.internal.mozilla;


public class Execute extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	static final String EXECUTE_IID_STR = "75a03044-d129-4d96-9292-bc1623876de1";

	static {
		IIDStore.RegisterIID(Execute.class, MozillaVersion.VERSION_BASE, new nsID(EXECUTE_IID_STR));
	}

	public Execute(long /*int*/ address) {
		super(address);
	}

	public int EvalInWindow(nsIDOMWindow aWindow, nsEmbedString code, long /*int*/[] aVariant) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aWindow.getAddress(), code.getAddress(), aVariant);
	}

	public int EvalAsChrome(nsIDOMWindow aWindow, nsEmbedString code, long /*int*/[] aVariant) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aWindow.getAddress(), code.getAddress(), aVariant);
 	}

}
