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
 * -  Copyright (C) 2004, 2006 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

#ifdef NATIVE_STATS
extern int XPCOM_PROFILE_nativeFunctionCount;
extern int XPCOM_PROFILE_nativeFunctionCallCount[];
extern char* XPCOM_PROFILE_nativeFunctionNames[];
#define XPCOM_PROFILE_NATIVE_ENTER(env, that, func) XPCOM_PROFILE_nativeFunctionCallCount[func]++;
#define XPCOM_PROFILE_NATIVE_EXIT(env, that, func) 
#else
#define XPCOM_PROFILE_NATIVE_ENTER(env, that, func) 
#define XPCOM_PROFILE_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	NS_1NewProfileDirServiceProvider_FUNC,
	ProfileDirServiceProvider_1Register_FUNC,
	ProfileDirServiceProvider_1SetProfileDir_FUNC,
	ProfileDirServiceProvider_1Shutdown_FUNC,
} XPCOM_PROFILE_FUNCS;
