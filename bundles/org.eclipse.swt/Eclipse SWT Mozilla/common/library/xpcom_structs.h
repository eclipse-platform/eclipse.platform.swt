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

#include "xpcom.h"

#ifndef NO_nsID
void cachensIDFields(JNIEnv *env, jobject lpObject);
nsID *getnsIDFields(JNIEnv *env, jobject lpObject, nsID *lpStruct);
void setnsIDFields(JNIEnv *env, jobject lpObject, nsID *lpStruct);
#define nsID_sizeof() sizeof(nsID)
#else
#define cachensIDFields(a,b)
#define getnsIDFields(a,b,c) NULL
#define setnsIDFields(a,b,c)
#define nsID_sizeof() 0
#endif

