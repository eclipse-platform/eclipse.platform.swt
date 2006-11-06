/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class LONG {
	public int /*long*/ value;

	public LONG(int /*long*/ value) {
		this.value = value;
	}

	public boolean equals (Object object) {
		if (object == this) return true;
		if (!(object instanceof LONG)) return false;
		LONG obj = (LONG)object;
		return obj.value == this.value;
	}

	public int hashCode () {
		return (int)/*64*/value;
	}
}
