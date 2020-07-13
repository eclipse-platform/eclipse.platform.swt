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

#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

typedef struct {
  int32_t id;
  int32_t port;
  uintptr_t args;
} FunctionSt;

const char *cefswt_cefstring_to_java(cef_string_t *cefstring);

void cefswt_close_browser(cef_browser_t *browser, int force);

void cefswt_context_menu_cancel(cef_run_context_menu_callback_t *callback);

char *cefswt_cookie_to_java(cef_cookie_t *cookie);

char *cefswt_cookie_value(cef_cookie_t *cookie);

const cef_browser_t *cefswt_create_browser(void* hwnd,
                                           const char *url,
                                           cef_client_t *client,
                                           int w,
                                           int h,
                                           int js,
                                           cef_color_t bg);

void cefswt_delete_cookies(void);

void cefswt_dialog_close(cef_jsdialog_callback_t *callback, int success, cef_string_t *prompt);

void cefswt_auth_callback(cef_auth_callback_t *callback, const char *user, const char *pass, int cont);

int cefswt_do_message_loop_work(void);

int cefswt_eval(cef_browser_t *browser,
                const char *text,
                int32_t id,
                void (*callback)(int work, int kind, const char *value));

void cefswt_execute(cef_browser_t *browser, const char *text);

void cefswt_free(cef_browser_t *obj);

int cefswt_function(cef_browser_t *browser, const char *name, int32_t id);

int cefswt_function_arg(cef_process_message_t *message,
                        int32_t index,
                        void (*callback)(int work, int kind, const char *value));

void cefswt_function_id(cef_process_message_t *message, FunctionSt *st);

int cefswt_function_return(cef_browser_t *_browser,
                           int32_t _id,
                           int32_t port,
                           int32 kind,
                           const char *ret);

int cefswt_get_cookie(const char *jurl, cef_cookie_visitor_t *jvisitor);

int cefswt_get_id(cef_browser_t *browser);

void cefswt_get_text(cef_browser_t *browser, cef_string_visitor_t *visitor);

char *cefswt_get_url(cef_browser_t *browser);

void cefswt_go_back(cef_browser_t *browser);

void cefswt_go_forward(cef_browser_t *browser);

void cefswt_init(cef_app_t *japp, const char *cefrust_path, const char *cef_path, const char *version, int debug_port);

int32_t cefswt_is_main_frame(cef_frame_t *frame);

int cefswt_is_same(cef_browser_t *browser, cef_browser_t *that);

void cefswt_load_text(cef_browser_t *browser, const char *text);

void cefswt_load_url(cef_browser_t *browser,
                     const char *url,
                     const void *post_bytes,
                     uintptr_t post_size,
                     const char *headers,
                     uintptr_t headers_size);

void cefswt_reload(cef_browser_t *browser);

char *cefswt_request_to_java(cef_request_t *request);

void cefswt_resized(cef_browser_t *browser, int32_t width, int32_t height);

int cefswt_set_cookie(const char *jurl,
                      const char *jname,
                      const char *jvalue,
                      const char *jdomain,
                      const char *jpath,
                      int32_t secure,
                      int32_t httponly,
                      double max_age);

void cefswt_set_focus(cef_browser_t *browser, bool set, void *parent);

void cefswt_set_window_info_parent(cef_window_info_t *window_info,
                                   cef_client_t **client,
                                   cef_client_t *jclient,
                                   void* hwnd,
                                   int x,
                                   int y,
                                   int w,
                                   int h);

void cefswt_shutdown(void);

void cefswt_stop(cef_browser_t *browser);

