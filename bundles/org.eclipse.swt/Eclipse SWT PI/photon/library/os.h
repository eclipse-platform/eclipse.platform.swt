/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef INC_os_H
#define INC_os_H

#define NDEBUG

#include <Ph.h>
#include <Pt.h>
#include <photon/PhRender.h>
#include <photon/PtWebClient.h>
#include <sys/utsname.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <malloc.h>

void *PtCreateAppContext();

#define utsname struct utsname

#endif /* INC_os_H */
