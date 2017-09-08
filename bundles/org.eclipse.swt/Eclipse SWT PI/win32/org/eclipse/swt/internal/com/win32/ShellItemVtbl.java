/*******************************************************************************
 * Copyright (c) 2017 Martin Karpisek and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Martin Karpisek <martin.karpisek@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.com.win32;

/**
 * Function number constants for IShellItem COM interface
 */
public class ShellItemVtbl{
    public static final int QUERY_INTERFACE = 0;
    public static final int ADD_REF = 1;
    public static final int RELEASE = 2;
    public static final int BIND_TO_HANDLER = 3;
    public static final int GET_PARENT = 4;
    public static final int GET_DISPLAY_NAME = 5;
    public static final int GET_ATTRIBUTES = 6;
    public static final int COMPARE = 7;
}