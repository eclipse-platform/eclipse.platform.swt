/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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

#ifndef INC_c_H
#define INC_c_H

#include <stdlib.h>
#include <string.h>

#define PTR_sizeof() sizeof(void *)

/* Functions excludes */
#ifdef _WIN32
#define NO_getenv
#define NO_setenv
#endif /* _WIN32 */

#endif /* INC_c_H */
