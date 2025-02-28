/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

abstract class NativeBasedCustomControl extends Control {

	protected NativeBasedCustomControl(Composite parent, int style) {
		super(parent, style);
	}

	boolean hasBorder() {
		return (style & SWT.BORDER) != 0;
	}

	@Override
	void createHandle() {
		super.createHandle();

		int bits = OS.GetWindowLong(handle, OS.GWL_STYLE);
		// for transparency of custom widgets this is critical.
		// if the widgets are not transparent, the recatangle needs to be
		// filled with a color,
		// else the old drawings stay in the area.
		state |= CANVAS;

		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0 || findThemeControl() == parent) {
			state |= THEME_BACKGROUND;
		}
		if ((style & SWT.TRANSPARENT) != 0) {
			bits = OS.GetWindowLong(handle, OS.GWL_EXSTYLE);
			bits |= OS.WS_EX_TRANSPARENT;
			OS.SetWindowLong(handle, OS.GWL_EXSTYLE, bits);
		}
	}

	@Override
	TCHAR windowClass() {
		if (display.useOwnDC)
			return display.windowOwnDCClass;
		return display.windowClass;
	}

	@Override
	long windowProc() {
		return display.windowProc;
	};

	@Override
	long callWindowProc(long hwnd, int msg, long wParam, long lParam) {
		if (handle == 0)
			return 0;
		return OS.DefWindowProc(hwnd, msg, wParam, lParam);
	}

	@Override
	LRESULT WM_PAINT(long wParam, long lParam) {
		return extended_WM_PAINT(wParam, lParam);
	}

	private void updateUIState() {
		long hwndShell = getShell().handle;
		int uiState = (int) OS.SendMessage(hwndShell, OS.WM_QUERYUISTATE, 0, 0);
		if ((uiState & OS.UISF_HIDEFOCUS) != 0) {
			OS.SendMessage(hwndShell, OS.WM_CHANGEUISTATE, OS.MAKEWPARAM(OS.UIS_CLEAR, OS.UISF_HIDEFOCUS), 0);
		}
	}

	private LRESULT extended_WM_PAINT(long wParam, long lParam) {
		if ((state & DISPOSE_SENT) != 0)
			return LRESULT.ZERO;
		if ((state & CANVAS) == 0 || (state & FOREIGN_HANDLE) != 0) {
			return _internal_WM_PAINT(wParam, lParam);
		}

		/* Set the clipping bits */
		int oldBits = OS.GetWindowLong(handle, OS.GWL_STYLE);
		int newBits = oldBits | OS.WS_CLIPSIBLINGS | OS.WS_CLIPCHILDREN;
		if (newBits != oldBits)
			OS.SetWindowLong(handle, OS.GWL_STYLE, newBits);

		/* Paint the control and the background */
		PAINTSTRUCT ps = new PAINTSTRUCT();
		if (hooks(SWT.Paint) || filters(SWT.Paint)) {

			/* Use the buffered paint when possible */
			boolean bufferedPaint = false;
			if ((style & SWT.DOUBLE_BUFFERED) != 0) {
				if ((style & (SWT.NO_MERGE_PAINTS | SWT.RIGHT_TO_LEFT | SWT.TRANSPARENT)) == 0) {
					bufferedPaint = true;
				}
			}
			if (bufferedPaint) {
				long hDC = OS.BeginPaint(handle, ps);
				int width = ps.right - ps.left;
				int height = ps.bottom - ps.top;
				if (width != 0 && height != 0) {
					long[] phdc = new long[1];
					int flags = OS.BPBF_COMPATIBLEBITMAP;
					RECT prcTarget = new RECT();
					OS.SetRect(prcTarget, ps.left, ps.top, ps.right, ps.bottom);
					long hBufferedPaint = OS.BeginBufferedPaint(hDC, prcTarget, flags, null, phdc);
					GCData data = new GCData();
					data.device = display;
					data.foreground = getForegroundPixel();
					Control control = findBackgroundControl();
					if (control == null)
						control = this;
					data.background = control.getBackgroundPixel();
					data.font = Font.win32_new(display, OS.SendMessage(handle, OS.WM_GETFONT, 0, 0), nativeZoom);
					data.uiState = (int) OS.SendMessage(handle, OS.WM_QUERYUISTATE, 0, 0);
					if ((style & SWT.NO_BACKGROUND) != 0) {
						/*
						 * This code is intentionally commented because it may be slow to copy bits from
						 * the screen
						 */
						// paintGC.copyArea (image, ps.left, ps.top);
					} else {
						RECT rect = new RECT();
						OS.SetRect(rect, ps.left, ps.top, ps.right, ps.bottom);
						drawBackground(phdc[0], rect);
					}
					GC gc = createNewGC(phdc[0], data);
					Event event = new Event();
					event.gc = gc;
					event.setBounds(DPIUtil.scaleDown(new Rectangle(ps.left, ps.top, width, height), getZoom()));
					sendEvent(SWT.Paint, event);
					if (data.focusDrawn && !isDisposed())
						updateUIState();
					gc.dispose();
					OS.EndBufferedPaint(hBufferedPaint, true);
				}
				OS.EndPaint(handle, ps);
			} else {

				/* Create the paint GC */
				GCData data = new GCData();
				data.ps = ps;
				data.hwnd = handle;
				GC gc = GC.win32_new(this, data);
				NativeGC ngc = (NativeGC) gc.innerGC;

				/* Get the system region for the paint HDC */
				long sysRgn = 0;
				if ((style & (SWT.DOUBLE_BUFFERED | SWT.TRANSPARENT)) != 0 || (style & SWT.NO_MERGE_PAINTS) != 0) {
					sysRgn = OS.CreateRectRgn(0, 0, 0, 0);
					if (OS.GetRandomRgn(ngc.handle, sysRgn, OS.SYSRGN) == 1) {
						if ((OS.GetLayout(ngc.handle) & OS.LAYOUT_RTL) != 0) {
							int nBytes = OS.GetRegionData(sysRgn, 0, null);
							int[] lpRgnData = new int[nBytes / 4];
							OS.GetRegionData(sysRgn, nBytes, lpRgnData);
							long newSysRgn = OS.ExtCreateRegion(new float[] { -1, 0, 0, 1, 0, 0 }, nBytes, lpRgnData);
							OS.DeleteObject(sysRgn);
							sysRgn = newSysRgn;
						}
						POINT pt = new POINT();
						OS.MapWindowPoints(0, handle, pt, 1);
						OS.OffsetRgn(sysRgn, pt.x, pt.y);
					}
				}

				/* Send the paint event */
				int width = ps.right - ps.left;
				int height = ps.bottom - ps.top;
				if (width != 0 && height != 0) {
					GC paintGC = null;
					Image image = null;
					if ((style & (SWT.DOUBLE_BUFFERED | SWT.TRANSPARENT)) != 0) {
						image = new Image(display, width, height);
						paintGC = gc;
						NativeGC npaintGC = (NativeGC) paintGC.innerGC;
						gc = new GC(image, paintGC.getStyle() & SWT.RIGHT_TO_LEFT);
						ngc = (NativeGC) gc.innerGC;
						GCData gcData = gc.getGCData();
						gcData.uiState = data.uiState;
						gc.setForeground(getForeground());
						gc.setBackground(getBackground());
						gc.setFont(getFont());
						if ((style & SWT.TRANSPARENT) != 0) {
							OS.BitBlt(ngc.handle, 0, 0, width, height, npaintGC.handle, ps.left, ps.top, OS.SRCCOPY);
						}
						OS.OffsetRgn(sysRgn, -ps.left, -ps.top);
						OS.SelectClipRgn(ngc.handle, sysRgn);
						OS.OffsetRgn(sysRgn, ps.left, ps.top);
						OS.SetMetaRgn(ngc.handle);
						OS.SetWindowOrgEx(ngc.handle, ps.left, ps.top, null);
						OS.SetBrushOrgEx(ngc.handle, ps.left, ps.top, null);
						if ((style & (SWT.NO_BACKGROUND | SWT.TRANSPARENT)) != 0) {
							/*
							 * This code is intentionally commented because it may be slow to copy bits from
							 * the screen
							 */
							// paintGC.copyArea (image, ps.left, ps.top);
						} else {
							RECT rect = new RECT();
							OS.SetRect(rect, ps.left, ps.top, ps.right, ps.bottom);
							drawBackground(ngc.handle, rect);
						}
					}
					Event event = new Event();
					event.gc = gc;
					RECT rect = null;
					ngc = (NativeGC) gc.innerGC;
					int zoom = getZoom();
					if ((style & SWT.NO_MERGE_PAINTS) != 0
							&& OS.GetRgnBox(sysRgn, rect = new RECT()) == OS.COMPLEXREGION) {
						int nBytes = OS.GetRegionData(sysRgn, 0, null);
						int[] lpRgnData = new int[nBytes / 4];
						OS.GetRegionData(sysRgn, nBytes, lpRgnData);
						int count = lpRgnData[2];
						for (int i = 0; i < count; i++) {
							int offset = 8 + (i << 2);
							OS.SetRect(rect, lpRgnData[offset], lpRgnData[offset + 1], lpRgnData[offset + 2],
									lpRgnData[offset + 3]);
							if ((style & (SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND | SWT.TRANSPARENT)) == 0) {
								drawBackground(ngc.handle, rect);
							}
							event.setBounds(DPIUtil.scaleDown(
									new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top),
									zoom));
							event.count = count - 1 - i;
							sendEvent(SWT.Paint, event);
						}
					} else {
						if ((style & (SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND | SWT.TRANSPARENT)) == 0) {
							if (rect == null)
								rect = new RECT();
							OS.SetRect(rect, ps.left, ps.top, ps.right, ps.bottom);
							drawBackground(ngc.handle, rect);
						}
						event.setBounds(DPIUtil.scaleDown(new Rectangle(ps.left, ps.top, width, height), zoom));
						sendEvent(SWT.Paint, event);
					}
					// widget could be disposed at this point
					event.gc = null;
					if ((style & (SWT.DOUBLE_BUFFERED | SWT.TRANSPARENT)) != 0) {
						if (!gc.isDisposed()) {
							GCData gcData = gc.getGCData();
							if (gcData.focusDrawn && !isDisposed())
								updateUIState();
						}
						gc.dispose();
						if (!isDisposed()) {
							paintGC.drawImage(image, DPIUtil.scaleDown(ps.left, zoom), DPIUtil.scaleDown(ps.top, zoom));
						}
						image.dispose();
						gc = paintGC;
					}
				}
				if (sysRgn != 0)
					OS.DeleteObject(sysRgn);
				if (data.focusDrawn && !isDisposed())
					updateUIState();

				/* Dispose the paint GC */
				gc.dispose();
			}
		} else {
			long hDC = OS.BeginPaint(handle, ps);
			if ((style & (SWT.NO_BACKGROUND | SWT.TRANSPARENT)) == 0) {
				RECT rect = new RECT();
				OS.SetRect(rect, ps.left, ps.top, ps.right, ps.bottom);
				drawBackground(hDC, rect);
			}
			OS.EndPaint(handle, ps);
		}

		/* Restore the clipping bits */
		if (!isDisposed()) {
			if (newBits != oldBits) {
				/*
				 * It is possible (but unlikely), that application code could have disposed the
				 * widget in the paint event. If this happens, don't attempt to restore the
				 * style.
				 */
				if (!isDisposed()) {
					OS.SetWindowLong(handle, OS.GWL_STYLE, oldBits);
				}
			}
		}
		return LRESULT.ZERO;
	}

	private LRESULT _internal_WM_PAINT(long wParam, long lParam) {

		if ((state & DISPOSE_SENT) != 0)
			return LRESULT.ZERO;
		return wmPaint(handle, wParam, lParam);
	}

}
