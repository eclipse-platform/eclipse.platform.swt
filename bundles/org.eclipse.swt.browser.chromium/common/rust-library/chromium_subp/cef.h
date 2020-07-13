/********************************************************************************
 * Copyright (c) 2020 Equo
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Guillermo Zunino, Equo - initial implementation
 ********************************************************************************/

#define cef_window_handle_t void*
#define cef_cursor_handle_t void*
#define cef_event_handle_t void*

#include "include/internal/cef_export.h"

// #include "include/base/cef_basictypes.h"

#include "include/internal/cef_string_types.h"
#include "include/internal/cef_string.h"
#include "include/internal/cef_types.h"
#include "include/capi/cef_base_capi.h"

#include "include/capi/cef_values_capi.h"

//#include "include/base/cef_build.h"
#include "include/capi/cef_print_handler_capi.h"
#include "include/capi/cef_context_menu_handler_capi.h"
#include "include/capi/cef_app_capi.h"
#include "include/capi/cef_client_capi.h"
#include "include/capi/cef_urlrequest_capi.h"
//#include "include/internal/cef_types_linux.h"
//#include "include/internal/cef_types_win.h"
#include "include/capi/cef_cookie_capi.h"
