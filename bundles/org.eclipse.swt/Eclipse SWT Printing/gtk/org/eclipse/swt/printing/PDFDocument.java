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
 * <b>Note:</b> On Windows, this class uses the built-in "Microsoft Print to PDF"
 * printer which is available on Windows 10 and later.
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
	 * Constructs a new PDFDocument with the specified filename and page size.
	 * <p>
	 * The page size specifies the preferred dimensions in points (1/72 inch). On Windows,
	 * the Microsoft Print to PDF driver only supports standard paper sizes, so the actual
	 * page size may be larger than requested. Use {@link #getBounds()} to query the actual
	 * page dimensions after construction.
	 * </p>
	 * <p>
	 * You must dispose the PDFDocument when it is no longer required.
	 * </p>
	 *
	 * @param filename the path to the PDF file to create
	 * @param pageSize the page size specifying width and height in points (1/72 inch)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if filename or pageSize is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if width or height is not positive</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES - if the PDF printer is not available</li>
	 * </ul>
	 *
	 * @see PageSize
	 * @see #dispose()
	 * @see #getBounds()
	 */
	public PDFDocument(String filename, PageSize pageSize) {
		super(checkData(filename, pageSize));
	}

	/**
	 * Constructs a new PDFDocument with the specified filename and preferred page dimensions.
	 * <p>
	 * The dimensions specify the preferred page size in points (1/72 inch). On Windows,
	 * the Microsoft Print to PDF driver only supports standard paper sizes, so the actual
	 * page size may be larger than requested. Use {@link #getBounds()} to query the actual
	 * page dimensions after construction.
	 * </p>
	 * <p>
	 * You must dispose the PDFDocument when it is no longer required.
	 * </p>
	 *
	 * @param filename the path to the PDF file to create
	 * @param widthInPoints the preferred width of each page in points (1/72 inch)
	 * @param heightInPoints the preferred height of each page in points (1/72 inch)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if filename is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if width or height is not positive</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES - if the PDF printer is not available</li>
	 * </ul>
	 *
	 * @see #dispose()
	 * @see #getBounds()
	 */
	public PDFDocument(String filename, double widthInPoints, double heightInPoints) {
		this(filename, new PageSize(widthInPoints, heightInPoints));
	}

	/**
	 * Validates and prepares the data for construction.
	 */
	static PDFDocumentData checkData(String filename, PageSize pageSize) {
		if (pageSize == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return checkData(filename, pageSize.width(), pageSize.height());
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
	 * <p>
	 * <b>Note:</b> On Windows, changing page dimensions after the document
	 * has been started may not be fully supported by all printer drivers.
	 * </p>
	 *
	 * @param widthInPoints the preferred width of the new page in points (1/72 inch)
	 * @param heightInPoints the preferred height of the new page in points (1/72 inch)
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
	 * Returns the actual width of the current page in points.
	 * <p>
	 * On Windows, this may be larger than the preferred width specified
	 * in the constructor due to standard paper size constraints.
	 * </p>
	 *
	 * @return the actual width in points (1/72 inch)
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
	 * Returns the actual height of the current page in points.
	 * <p>
	 * On Windows, this may be larger than the preferred height specified
	 * in the constructor due to standard paper size constraints.
	 * </p>
	 *
	 * @return the actual height in points (1/72 inch)
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
	 * Since the coordinate system is scaled to work in points (1/72 inch),
	 * this always returns 72 DPI, consistent with GTK and Cocoa implementations.
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
	 * <p>
	 * On Windows, this returns the actual page size which may be larger
	 * than the preferred size specified in the constructor.
	 * </p>
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
