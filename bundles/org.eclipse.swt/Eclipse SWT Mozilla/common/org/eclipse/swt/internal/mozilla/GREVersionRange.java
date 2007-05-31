/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.mozilla;

public class GREVersionRange {
    public int /*long*/ lower;
    public boolean lowerInclusive;
    public int /*long*/ upper;
    public boolean upperInclusive;
    public static final int sizeof = XPCOMInit.GREVersionRange_sizeof();
}
