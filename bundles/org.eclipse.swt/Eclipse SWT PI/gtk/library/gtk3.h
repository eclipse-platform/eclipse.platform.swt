/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
  
#ifndef INC_gtk3_H
#define INC_gtk3_H

#define NDEBUG

#define G_DISABLE_DEPRECATED
#define GTK_DISABLE_SINGLE_INCLUDES

#include <gtk/gtk.h>
// Hard-link code generated from GTK3.java to LIB_GTK
#define GTK3_LOAD_FUNCTION(var, name) LOAD_FUNCTION_LIB(var, LIB_GTK, name)

#endif /* INC_gtk3_H */
