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
package org.eclipse.swt.internal.chromium;

import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.chromium.lib.*;

public class CEFFactory {

	public static final int PID_BROWSER = 0;
	public static final int PID_RENDERER = 1;

	public static void create(String cefrustPath) {
		if ("cocoa".equals(SWT.getPlatform())) {
			// On MacOS pre-loading the required lib doesn't work, system ignores it.
			return;
		}
		List<String> toLoad = new ArrayList<>();
//		String cefrustPath = System.getProperty("cefswt.path", "");
		String cefLibName = getCefLibName();
		if ("win32".equals(SWT.getPlatform())) {
			toLoad.add(cefrustPath + File.separator + "chrome_elf.dll");
		}
		toLoad.add(cefrustPath + File.separator + cefLibName);

		for (String lib : toLoad) {
			try {
				Library.loadLibrary(lib, false);
			} catch (UnsatisfiedLinkError e) {
				if ("gtk".equals(SWT.getPlatform()) && e.getMessage().contains("libgconf")) {
					try {
						Library.loadLibrary(cefrustPath + "/libgconf-2.so.4", false);
					} catch (UnsatisfiedLinkError e1) {
						throw e;
					}
					Library.loadLibrary(lib, false);
					return;
				}
				throw e;
			}
		}
	}

	public static String getCefLibName() {
		if ("win32".equals(SWT.getPlatform())) {
			return "libcef.dll";
		} else if ("cocoa".equals(SWT.getPlatform())) {
			return "Chromium Embedded Framework.framework/Chromium Embedded Framework";
		} else {
			return "libcef.so";
		}
	}

	public static cef_app_t newApp() {
		cef_app_t st = new cef_app_t();
		st.base = setBase(st, cef_app_t.sizeof);
		return st;
	}

	public static cef_browser_process_handler_t newBrowserProcessHandler() {
		cef_browser_process_handler_t st = new cef_browser_process_handler_t();
		st.base = setBase(st, cef_browser_process_handler_t.sizeof);
		return st;
	}

	public static cef_client_t newClient() {
		cef_client_t st = new cef_client_t();
		st.base = setBase(st, cef_client_t.sizeof);
		return st;
	}

	public static cef_focus_handler_t newFocusHandler() {
		cef_focus_handler_t st = new cef_focus_handler_t();
		st.base = setBase(st, cef_focus_handler_t.sizeof);
		return st;
	}

	public static cef_life_span_handler_t newLifeSpanHandler() {
		cef_life_span_handler_t st = new cef_life_span_handler_t();
		st.base = setBase(st, cef_life_span_handler_t.sizeof);
		return st;
	}

	public static cef_load_handler_t newLoadHandler() {
		cef_load_handler_t st = new cef_load_handler_t();
		st.base = setBase(st, cef_load_handler_t.sizeof);
		return st;
	}

	public static cef_display_handler_t newDisplayHandler() {
		cef_display_handler_t st = new cef_display_handler_t();
		st.base = setBase(st, cef_display_handler_t.sizeof);
		return st;
	}

	public static cef_request_handler_t newRequestHandler() {
		cef_request_handler_t st = new cef_request_handler_t();
		st.base = setBase(st, cef_request_handler_t.sizeof);
		return st;
	}

	public static cef_jsdialog_handler_t newJsDialogHandler() {
		cef_jsdialog_handler_t st = new cef_jsdialog_handler_t();
		st.base = setBase(st, cef_jsdialog_handler_t.sizeof);
		return st;
	}

	public static cef_context_menu_handler_t newContextMenuHandler() {
		cef_context_menu_handler_t st = new cef_context_menu_handler_t();
		st.base = setBase(st, cef_context_menu_handler_t.sizeof);
		return st;
	}

	public static cef_string_visitor_t newStringVisitor() {
		cef_string_visitor_t st = new cef_string_visitor_t();
		st.base = setBase(st, cef_string_visitor_t.sizeof);
		return st;
	}

	public static cef_cookie_visitor_t newCookieVisitor() {
		cef_cookie_visitor_t st = new cef_cookie_visitor_t();
		st.base = setBase(st, cef_cookie_visitor_t.sizeof);
		return st;
	}

	private static cef_base_ref_counted_t setBase(Object st, int sizeof) {
//		System.out.println("J:SIZEOF:" + st.getClass().getSimpleName() + ":" + sizeof);
		org.eclipse.swt.internal.chromium.lib.cef_base_ref_counted_t base = new org.eclipse.swt.internal.chromium.lib.cef_base_ref_counted_t();
		base.size = sizeof;
		base.add_ref = 0;
		base.has_one_ref = 0;
		base.release = 0;
//		base.name = st.getClass().getSimpleName();
		return base;
	}

	public static enum ReturnType {
		Double(0), Bool(1), Str(2), Null(3), Array(4), Error(5);

		private int value;

		private ReturnType(int value) {
			this.value = value;
		}

		public int intValue() {
			return value;
		}

		public static ReturnType from(String v) {
			try {
				int value = Integer.parseInt(v);
				for (ReturnType rt : ReturnType.values()) {
					if (rt.intValue() == value) {
						return rt;
					}
				}
			} catch (NumberFormatException e) {
			}
			throw new IllegalArgumentException(v);
		}
	}

}
