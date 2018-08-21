/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef INC_os_H
#define INC_os_H

#include <windows.h>
#include <WindowsX.h>
#include <commctrl.h>
#include <commdlg.h>
#include <oaidl.h>
#include <shlobj.h>
#include <ole2.h>
#include <olectl.h>
#include <objbase.h>
#include <shlwapi.h>
#include <shellapi.h>
#include <wininet.h>
#include <mshtmhst.h>
#include <Tabflicks.h>
#include <initguid.h>
#include <oleacc.h>
#include <usp10.h>
#include <uxtheme.h>
#include <msctf.h>
#include <intsafe.h>

/* Optional custom definitions to exclude some types */
#include "defines.h"

#define OS_LOAD_FUNCTION LOAD_FUNCTION

#include "os_custom.h"

#define NATIVE_TRY(env, that, func) \
	__try {
#define NATIVE_CATCH(env, that, func) \
	} __except(EXCEPTION_EXECUTE_HANDLER) { \
		jclass expClass = (*env)->FindClass(env, "org/eclipse/swt/SWTError");  \
		if (expClass) { \
			char buffer[64]; \
			wsprintfA(buffer, "caught native exception: 0x%x", GetExceptionCode()); \
			(*env)->ThrowNew(env, expClass, buffer); \
		} \
	}

#define OS_NATIVE_ENTER_TRY(env, that, func) \
	OS_NATIVE_ENTER(env, that, func); \
	NATIVE_TRY(env, that, func);
#define OS_NATIVE_EXIT_CATCH(env, that, func) \
	NATIVE_CATCH(env, that, func); \
	OS_NATIVE_EXIT(env, that, func);

#endif /* INC_os_H */
