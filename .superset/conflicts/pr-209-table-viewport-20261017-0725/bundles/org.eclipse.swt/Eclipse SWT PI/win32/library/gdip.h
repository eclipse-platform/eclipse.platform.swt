/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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

#ifndef INC_gdip_H
#define INC_gdip_H

/*
 * Windows headers will sometimes have warnings above level 2, just
 * ignore all of them, we can't do anything about it anyway.
 */
#pragma warning(push, 2)

#include <windows.h>
#include <gdiplus.h>
using namespace Gdiplus;

/* Restore warnings */
#pragma warning(pop)

/* Optional custom definitions to exclude some types */
#include "defines.h"

#endif /* INC_gdip_H */
