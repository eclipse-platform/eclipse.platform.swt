/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

/* Special sizeof's */
#define ELEMDESC_sizeof() sizeof(ELEMDESC)
#define TYPEDESC_sizeof() sizeof(TYPEDESC)

/* Libraries for dynamic loaded functions */
#define AccessibleChildren_LIB "oleacc.dll"
#define AccessibleObjectFromWindow_LIB "oleacc.dll"
#define CreateStdAccessibleObject_LIB "oleacc.dll"
#define LresultFromObject_LIB "oleacc.dll"
#define CreateCoreWebView2EnvironmentWithOptions_LIB "WebView2Loader.dll"

/* Custom functions */
#ifdef __cplusplus
extern "C" {
#endif
HRESULT PathToPIDL(_In_ PCWSTR pszName, _Outptr_ PIDLIST_ABSOLUTE *ppidl);
#ifdef __cplusplus
}
#endif
