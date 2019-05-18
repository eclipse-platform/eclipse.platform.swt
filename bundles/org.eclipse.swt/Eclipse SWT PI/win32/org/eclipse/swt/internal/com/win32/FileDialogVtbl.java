/*******************************************************************************
 * Copyright (c) 2017 Martin Karpisek and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Martin Karpisek <martin.karpisek@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.com.win32;

/**
 * Function number constants for IFileDialog COM interface
 */
public class FileDialogVtbl{
    public static final int QUERY_INTERFACE = 0;
    public static final int ADD_REF = 1;
    public static final int RELEASE = 2;
    public static final int SHOW = 3;
    public static final int SET_FILE_TYPES = 4;
    public static final int SET_FILE_TYPE_INDEX = 5;
    public static final int GET_FILE_TYPE_INDEX = 6;
    public static final int ADVISE = 7;
    public static final int UNADVISE = 8;
    public static final int SET_OPTIONS = 9;
    public static final int GET_OPTIONS = 10;
    public static final int SET_DEFAULT_FOLDER = 11;
    public static final int SET_FOLDER = 12;
    public static final int GET_FOLDER = 13;
    public static final int GET_CURRENT_SELECTION = 14;
    public static final int SET_FILE_NAME = 15;
    public static final int GET_FILE_NAME = 16;
    public static final int SET_TITLE = 17;
    public static final int SET_OK_BUTTON_LABEL = 18;
    public static final int SET_FILE_NAME_LABEL = 19;
    public static final int GET_RESULT = 20;
    public static final int ADD_PLACE = 21;
    public static final int SET_DEFAULT_EXTENSION = 22;
    public static final int CLOSE = 23;
    public static final int SET_CLIENT_GUID = 24;
    public static final int CLEAR_CLIENT_DATA = 25;
    public static final int SET_FILTER = 26;
}