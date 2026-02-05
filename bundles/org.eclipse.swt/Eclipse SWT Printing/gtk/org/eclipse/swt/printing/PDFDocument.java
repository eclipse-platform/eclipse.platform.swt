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
 *
 * @noreference This class is provisional API and subject to change. It is being made available to gather early feedback. The API or behavior may change in future releases as the implementation evolves based on user feedback.
 */
public final class PDFDocument extends Device {
	long surface;
	long cairo;
	boolean isGCCreated = false;
	String filename;

	/**
	 * Width of the page in points (1/72 inch)
	 */
	double widthInPoints;

	/**
	 * Height of the page in points (1/72 inch)
	 */
	double heightInPoints;

	/**
	 * Internal data class to pass PDF document parameters through
	 * the Device constructor.
	 */
	static class PDFDocumentData extends DeviceData {
		String filename;
		double widthInPoints;
		double heightInPoints;
	}

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
		super(checkData(filename, widthInPoints, heightInPoints));
	}

	/**
	 * Validates and prepares the data for construction.
	 */
	static PDFDocumentData checkData(String filename, double widthInPoints, double heightInPoints) {
		if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (widthInPoints <= 0 || heightInPoints <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		PDFDocumentData data = new PDFDocumentData();
		data.filename = filename;
		data.widthInPoints = widthInPoints;
		data.heightInPoints = heightInPoints;
		return data;
	}

	/**
	 * Creates the PDF device in the operating system.
	 * This method is called before <code>init</code>.
	 *
	 * @param data the DeviceData which describes the receiver
	 */
	@Override
	protected void create(DeviceData data) {
		PDFDocumentData pdfData = (PDFDocumentData) data;
		this.filename = pdfData.filename;
		this.widthInPoints = pdfData.widthInPoints;
		this.heightInPoints = pdfData.heightInPoints;

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
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void newPage() {
		checkDevice();
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
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void newPage(double widthInPoints, double heightInPoints) {
		checkDevice();
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
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public double getWidth() {
		checkDevice();
		return widthInPoints;
	}

	/**
	 * Returns the height of the current page in points.
	 *
	 * @return the height in points (1/72 inch)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public double getHeight() {
		checkDevice();
		return heightInPoints;
	}

	/**
	 * Returns the DPI (dots per inch) of the PDF document.
	 * PDF documents work in points where 1 point = 1/72 inch,
	 * so the DPI is always 72.
	 *
	 * @return a point whose x coordinate is the horizontal DPI and whose y coordinate is the vertical DPI
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	@Override
	public Point getDPI() {
		checkDevice();
		return new Point(72, 72);
	}

	/**
	 * Returns a rectangle describing the receiver's size and location.
	 * The rectangle dimensions are in points (1/72 inch).
	 *
	 * @return the bounding rectangle
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	@Override
	public Rectangle getBounds() {
		checkDevice();
		return new Rectangle(0, 0, (int) widthInPoints, (int) heightInPoints);
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
		checkDevice();
		if (isGCCreated) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		if (data != null) {
			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			if ((data.style & mask) == 0) {
				data.style |= SWT.LEFT_TO_RIGHT;
			}
			data.device = this;
			data.cairo = cairo;
			data.width = (int) widthInPoints;
			data.height = (int) heightInPoints;
			data.foregroundRGBA = getSystemColor(SWT.COLOR_BLACK).handle;
			data.backgroundRGBA = getSystemColor(SWT.COLOR_WHITE).handle;
			data.font = getSystemFont();
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
	 * Destroys the PDF document handle.
	 * This method is called internally by the dispose
	 * mechanism of the <code>Device</code> class.
	 */
	@Override
	protected void destroy() {
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
