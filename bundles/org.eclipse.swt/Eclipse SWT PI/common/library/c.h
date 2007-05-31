/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef INC_c_H
#define INC_c_H

#include <stdlib.h>
#include <string.h>

#define PTR_sizeof() sizeof(void *)

/* Functions excludes */
#ifdef _WIN32_WCE
#define NO_getenv
#endif /* _WIN32_WCE */

#endif /* INC_c_H */
