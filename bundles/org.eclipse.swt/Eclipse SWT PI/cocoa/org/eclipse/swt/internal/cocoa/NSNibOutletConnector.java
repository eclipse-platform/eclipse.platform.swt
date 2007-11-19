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

public class NSNibOutletConnector extends NSNibConnector {

public NSNibOutletConnector() {
	super();
}

public NSNibOutletConnector(int id) {
	super(id);
}

public void establishConnection() {
	OS.objc_msgSend(this.id, OS.sel_establishConnection);
}

}
