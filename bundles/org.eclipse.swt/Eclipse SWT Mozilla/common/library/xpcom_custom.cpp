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
* -  Copyright (C) 2003, 2004 IBM Corp.  All Rights Reserved.
*
* ***** END LICENSE BLOCK ***** */

#include "swt.h"
#include "xpcom_structs.h"
#include "xpcom_stats.h"

extern "C" {

#define XPCOM_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_##func

#ifndef NO_strlen_1PRUnichar
JNIEXPORT jint JNICALL XPCOM_NATIVE(strlen_1PRUnichar)
	(JNIEnv *env, jclass that, SWT_PTR arg0)
{
	jint rc;
	XPCOM_NATIVE_ENTER(env, that, strlen_1PRUnichar_FUNC);
	{
	const PRUnichar* lparg0 = NULL;
	if (arg0) lparg0 = (const PRUnichar *)arg0;
	PRUint32 len = 0;
	if (lparg0 != NULL)	while (*lparg0++ != 0) len++;
	rc = (jint)len;
	}
	XPCOM_NATIVE_EXIT(env, that, strlen_1PRUnichar_FUNC);
	return rc;
}
#endif

}
