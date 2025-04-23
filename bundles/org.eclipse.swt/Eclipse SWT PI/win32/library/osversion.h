/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

#ifndef INC_osversion_H
#define INC_osversion_H

/*
 * Windows headers will sometimes have warnings above level 2, just
 * ignore all of them, we can't do anything about it anyway.
 */
#pragma warning(push, 2)

#include <windows.h>

/* Restore warnings */
#pragma warning(pop)

/* Optional custom definitions to exclude some types */
#include "defines.h"

#define OsVersion_LOAD_FUNCTION LOAD_FUNCTION

/* Libraries for dynamic loaded functions */
#define RtlGetVersion_LIB "ntdll.dll"

#endif /* INC_osversion_H */
