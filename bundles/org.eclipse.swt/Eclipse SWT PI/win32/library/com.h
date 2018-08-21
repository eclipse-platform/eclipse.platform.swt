/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

#ifndef INC_com_H
#define INC_com_H

#include "os_structs.h"
#include "com_custom.h"

#define COM_LOAD_FUNCTION LOAD_FUNCTION

#define COM_NATIVE_ENTER_TRY(env, that, func) \
	COM_NATIVE_ENTER(env, that, func); \
	NATIVE_TRY(env, that, func);
#define COM_NATIVE_EXIT_CATCH(env, that, func) \
	NATIVE_CATCH(env, that, func); \
	COM_NATIVE_EXIT(env, that, func);

#endif /* INC_com_H */
