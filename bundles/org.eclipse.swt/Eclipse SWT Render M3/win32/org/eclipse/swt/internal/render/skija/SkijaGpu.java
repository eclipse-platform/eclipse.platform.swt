/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation; the hidden GL host window and the WGL setup
 *         follow this repository's SkijaPlaneBackend.init (Eclipse SWT GPU Skija, EPL-2.0)
 *******************************************************************************/
package org.eclipse.swt.internal.render.skija;

import org.eclipse.swt.internal.opengl.win32.*;
import org.eclipse.swt.internal.win32.*;

import io.github.humbleui.skija.*;

/**
 * The graphics hardware of the <code>gpu</code> drawn planes on win32, Java only: one OpenGL context per UI thread
 * through SWT's own WGL natives, on a hidden, disabled, never-shown popup window (not a child of any widget, no
 * input, no focus), and Skija's <code>DirectContext.makeGL</code> over it (Skija ships its own natives). Every gpu
 * plane of the thread shares it (created by the first plane, freed with the last one).
 */
final class SkijaGpu {
	private static final ThreadLocal<SkijaGpu> SHARED = new ThreadLocal<> ();
	private long hostHwnd, hostDC, hglrc;
	DirectContext context;
	private int references;

	private SkijaGpu () {
	}

	/** The context of this thread, created on first use; null when OpenGL or Skia's GL backend cannot start. */
	static SkijaGpu acquire () {
		SkijaGpu gpu = SHARED.get ();
		if (gpu == null) {
			gpu = new SkijaGpu ();
			if (!gpu.init ()) {
				gpu.dispose ();
				return null;
			}
			SHARED.set (gpu);
		}
		gpu.references++;
		return gpu;
	}

	private boolean init () {
		char [] cls = "STATIC\0".toCharArray ();
		hostHwnd = OS.CreateWindowEx (OS.WS_EX_NOACTIVATE | OS.WS_EX_TOOLWINDOW, cls, null, OS.WS_POPUP | OS.WS_DISABLED, 0, 0, 1, 1, 0, 0, OS.GetModuleHandle (null), null);
		if (hostHwnd == 0) return false;
		hostDC = OS.GetDC (hostHwnd);
		PIXELFORMATDESCRIPTOR pfd = new PIXELFORMATDESCRIPTOR ();
		pfd.nSize = (short) PIXELFORMATDESCRIPTOR.sizeof;
		pfd.nVersion = 1;
		pfd.dwFlags = WGL.PFD_DRAW_TO_WINDOW | WGL.PFD_SUPPORT_OPENGL | WGL.PFD_DOUBLEBUFFER;
		pfd.iPixelType = (byte) WGL.PFD_TYPE_RGBA;
		pfd.cColorBits = 32;
		pfd.cAlphaBits = 8;
		pfd.cStencilBits = 8;
		pfd.iLayerType = (byte) WGL.PFD_MAIN_PLANE;
		int format = WGL.ChoosePixelFormat (hostDC, pfd);
		if (format == 0 || !WGL.SetPixelFormat (hostDC, format, pfd)) return false;
		hglrc = WGL.wglCreateContext (hostDC);
		if (hglrc == 0 || !WGL.wglMakeCurrent (hostDC, hglrc)) return false;
		context = DirectContext.makeGL ();
		return context != null;
	}

	/** Makes this thread's GL context current again if application code made another one current. */
	void makeCurrent () {
		if (WGL.wglGetCurrentContext () != hglrc) {
			WGL.wglMakeCurrent (hostDC, hglrc);
			context.resetGLAll ();
		}
	}

	/** Releases one plane's hold; the last one frees the context and the host window. */
	void release () {
		if (--references > 0) return;
		SHARED.remove ();
		dispose ();
	}

	private void dispose () {
		if (context != null) {
			context.abandon ();
			context.close ();
		}
		context = null;
		if (hglrc != 0) {
			WGL.wglMakeCurrent (0, 0);
			WGL.wglDeleteContext (hglrc);
		}
		if (hostDC != 0) OS.ReleaseDC (hostHwnd, hostDC);
		if (hostHwnd != 0) OS.DestroyWindow (hostHwnd);
		hostHwnd = hostDC = hglrc = 0;
	}
}
