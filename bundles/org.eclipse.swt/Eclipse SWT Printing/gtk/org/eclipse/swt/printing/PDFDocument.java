/*******************************************************************************
 * Copyright (c) 2025 Eclipse Platform Contributors and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eclipse Platform Contributors - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.printing;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;

/**
 * Instances of this class are used to create PDF documents.
 * Applications create a GC on a PDFDocument using <code>new GC(pdfDocument)</code>
 * and then draw on the GC using the usual graphics calls.
 * <p>
 * A <code>PDFDocument</code> object may be constructed by providing
 * a filename and the page dimensions. After drawing is complete,
 * the document must be disposed to finalize the PDF file.
 * </p><p>
 * Application code must explicitly invoke the <code>PDFDocument.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * <p>
 * The following example demonstrates how to use PDFDocument:
 * </p>
 * <pre>
 *    PDFDocument pdf = new PDFDocument("output.pdf", 612, 792); // Letter size in points
 *    GC gc = new GC(pdf);
 *    gc.drawText("Hello, PDF!", 100, 100);
 *    gc.dispose();
 *    pdf.dispose();
 * </pre>
 *
 * @see GC
 * @since 3.133
 */
public class PDFDocument implements Drawable {
	Device device;
	long surface;
	long cairo;
	boolean isGCCreated = false;
	boolean disposed = false;

	/**
	 * Width of the page in points (1/72 inch)
	 */
	double widthInPoints;

	/**
	 * Height of the page in points (1/72 inch)
	 */
	double heightInPoints;

	/**
	 * Constructs a new PDFDocument with the specified filename and page dimensions.
	 * <p>
	 * You must dispose the PDFDocument when it is no longer required.
	 * </p>
	 *
	 * @param filename the path to the PDF file to create
	 * @param widthInPoints the width of each page in points (1/72 inch)
	 * @param heightInPoints the height of each page in points (1/72 inch)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if filename is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if width or height is not positive</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES - if the PDF surface could not be created</li>
	 * </ul>
	 *
	 * @see #dispose()
	 */
	public PDFDocument(String filename, double widthInPoints, double heightInPoints) {
		if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (widthInPoints <= 0 || heightInPoints <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		this.widthInPoints = widthInPoints;
		this.heightInPoints = heightInPoints;

		byte[] filenameBytes = Converter.wcsToMbcs(filename, true);
		surface = Cairo.cairo_pdf_surface_create(filenameBytes, widthInPoints, heightInPoints);
		if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);

		cairo = Cairo.cairo_create(surface);
		if (cairo == 0) {
			Cairo.cairo_surface_destroy(surface);
			surface = 0;
			SWT.error(SWT.ERROR_NO_HANDLES);
		}

		// Get device from the current display or create a temporary one
		try {
			device = org.eclipse.swt.widgets.Display.getDefault();
		} catch (Exception e) {
			device = null;
		}
	}

	/**
	 * Constructs a new PDFDocument with the specified filename and page dimensions,
	 * associated with the given device.
	 * <p>
	 * You must dispose the PDFDocument when it is no longer required.
	 * </p>
	 *
	 * @param device the device to associate with this PDFDocument
	 * @param filename the path to the PDF file to create
	 * @param widthInPoints the width of each page in points (1/72 inch)
	 * @param heightInPoints the height of each page in points (1/72 inch)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if filename is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if width or height is not positive</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES - if the PDF surface could not be created</li>
	 * </ul>
	 *
	 * @see #dispose()
	 */
	public PDFDocument(Device device, String filename, double widthInPoints, double heightInPoints) {
		if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (widthInPoints <= 0 || heightInPoints <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		this.device = device;
		this.widthInPoints = widthInPoints;
		this.heightInPoints = heightInPoints;

		byte[] filenameBytes = Converter.wcsToMbcs(filename, true);
		surface = Cairo.cairo_pdf_surface_create(filenameBytes, widthInPoints, heightInPoints);
		if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);

		cairo = Cairo.cairo_create(surface);
		if (cairo == 0) {
			Cairo.cairo_surface_destroy(surface);
			surface = 0;
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
	}

	/**
	 * Starts a new page in the PDF document.
	 * <p>
	 * This method should be called after completing the content of one page
	 * and before starting to draw on the next page. The new page will have
	 * the same dimensions as the initial page.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void newPage() {
		if (disposed) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		Cairo.cairo_show_page(cairo);
	}

	/**
	 * Starts a new page in the PDF document with the specified dimensions.
	 * <p>
	 * This method should be called after completing the content of one page
	 * and before starting to draw on the next page.
	 * </p>
	 *
	 * @param widthInPoints the width of the new page in points (1/72 inch)
	 * @param heightInPoints the height of the new page in points (1/72 inch)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if width or height is not positive</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void newPage(double widthInPoints, double heightInPoints) {
		if (disposed) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		if (widthInPoints <= 0 || heightInPoints <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		Cairo.cairo_show_page(cairo);
		Cairo.cairo_pdf_surface_set_size(surface, widthInPoints, heightInPoints);
		this.widthInPoints = widthInPoints;
		this.heightInPoints = heightInPoints;
	}

	/**
	 * Returns the width of the current page in points.
	 *
	 * @return the width in points (1/72 inch)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public double getWidth() {
		if (disposed) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		return widthInPoints;
	}

	/**
	 * Returns the height of the current page in points.
	 *
	 * @return the height in points (1/72 inch)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public double getHeight() {
		if (disposed) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		return heightInPoints;
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>PDFDocument</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param data the platform specific GC data
	 * @return the platform specific GC handle
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public long internal_new_GC(GCData data) {
		if (disposed) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		if (isGCCreated) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		if (data != null) {
			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			if ((data.style & mask) == 0) {
				data.style |= SWT.LEFT_TO_RIGHT;
			}
			data.device = device;
			data.cairo = cairo;
			data.width = (int) widthInPoints;
			data.height = (int) heightInPoints;
			if (device != null) {
				data.foregroundRGBA = device.getSystemColor(SWT.COLOR_BLACK).handle;
				data.backgroundRGBA = device.getSystemColor(SWT.COLOR_WHITE).handle;
				data.font = device.getSystemFont();
			} else {
				// Fallback: create default colors manually using GdkRGBA values
				data.foregroundRGBA = new org.eclipse.swt.internal.gtk.GdkRGBA();
				data.foregroundRGBA.red = 0;
				data.foregroundRGBA.green = 0;
				data.foregroundRGBA.blue = 0;
				data.foregroundRGBA.alpha = 1;
				data.backgroundRGBA = new org.eclipse.swt.internal.gtk.GdkRGBA();
				data.backgroundRGBA.red = 1;
				data.backgroundRGBA.green = 1;
				data.backgroundRGBA.blue = 1;
				data.backgroundRGBA.alpha = 1;
			}
		}
		isGCCreated = true;
		return cairo;
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>PDFDocument</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param hDC the platform specific GC handle
	 * @param data the platform specific GC data
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public void internal_dispose_GC(long hDC, GCData data) {
		if (data != null) isGCCreated = false;
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public boolean isAutoScalable() {
		return false;
	}

	/**
	 * Returns <code>true</code> if the PDFDocument has been disposed,
	 * and <code>false</code> otherwise.
	 *
	 * @return <code>true</code> when the PDFDocument is disposed and <code>false</code> otherwise
	 */
	public boolean isDisposed() {
		return disposed;
	}

	/**
	 * Disposes of the operating system resources associated with
	 * the PDFDocument. Applications must dispose of all PDFDocuments
	 * that they allocate.
	 * <p>
	 * This method finalizes the PDF file and writes it to disk.
	 * </p>
	 */
	public void dispose() {
		if (disposed) return;
		disposed = true;

		if (cairo != 0) {
			Cairo.cairo_destroy(cairo);
			cairo = 0;
		}
		if (surface != 0) {
			Cairo.cairo_surface_finish(surface);
			Cairo.cairo_surface_destroy(surface);
			surface = 0;
		}
	}
}
