/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2005, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIFilePicker {

	public static final String NS_IFILEPICKER_IID_STR =
		"c47de916-1dd1-11b2-8141-82507fa02b21";

	public static final String NS_IFILEPICKER_1_8_IID_STR =
		"80faf095-c807-4558-a2cc-185ed70754ea";

	public static final String NS_IFILEPICKER_10_IID_STR =
		"f2c0e216-5d07-4df4-bbcb-37683077ae7e";

	public static final String NS_IFILEPICKER_17_IID_STR =
		"60e2dfb6-3fc7-4a2c-8137-16bef44536fc";

	public static final nsID NS_IFILEPICKER_IID =
		new nsID(NS_IFILEPICKER_IID_STR);

	public static final nsID NS_IFILEPICKER_1_8_IID =
		new nsID(NS_IFILEPICKER_1_8_IID_STR);

	public static final nsID NS_IFILEPICKER_10_IID =
		new nsID(NS_IFILEPICKER_10_IID_STR);

	public static final nsID NS_IFILEPICKER_17_IID =
		new nsID(NS_IFILEPICKER_17_IID_STR);

	public static final int modeOpen = 0;
	public static final int modeSave = 1;
	public static final int modeGetFolder = 2;
	public static final int modeOpenMultiple = 3;
	public static final int returnOK = 0;
	public static final int returnCancel = 1;
	public static final int returnReplace = 2;
	public static final int filterAll = 1;
	public static final int filterHTML = 2;
	public static final int filterText = 4;
	public static final int filterImages = 8;
	public static final int filterXML = 16;
	public static final int filterXUL = 32;
	public static final int filterApps = 64;

	/* the following constants are available as of mozilla 10 */
	public static final int filterAllowURLs = 128;
	public static final int filterAudio = 256;
	public static final int filterVideo = 512;

	/* outgoing functions removed because this is an implemented interface */
}
