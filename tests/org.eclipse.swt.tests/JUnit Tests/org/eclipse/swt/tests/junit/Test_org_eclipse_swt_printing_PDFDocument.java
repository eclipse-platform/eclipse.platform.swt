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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.PDFDocument;
import org.eclipse.swt.printing.PageSize;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.PDFDocument
 *
 * @see org.eclipse.swt.printing.PDFDocument
 */
public class Test_org_eclipse_swt_printing_PDFDocument {

	Display display;
	PDFDocument pdfDocument;
	GC gc;
	File tempFile;

	@TempDir
	Path tempDir;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
	}

	@AfterEach
	public void tearDown() {
		if (gc != null && !gc.isDisposed()) {
			gc.dispose();
		}
		if (pdfDocument != null && !pdfDocument.isDisposed()) {
			pdfDocument.dispose();
		}
		if (tempFile != null && tempFile.exists()) {
			tempFile.delete();
		}
	}

	@Test
	public void test_createPDFDocumentWithHelloWorld() throws IOException {
		// Create a temporary file for the PDF
		tempFile = Files.createTempFile(tempDir, "test", ".pdf").toFile();
		String filename = tempFile.getAbsolutePath();

		// Create PDF document with standard letter size (612 x 792 points)
		pdfDocument = new PDFDocument(filename, 612, 792);
		assertNotNull(pdfDocument, "PDFDocument should be created");

		// Create a GC on the PDF document
		gc = new GC(pdfDocument);
		assertNotNull(gc, "GC should be created on PDFDocument");

		// Draw "hello world" text
		gc.drawText("hello world", 100, 100);

		// Dispose of resources to finalize the PDF
		gc.dispose();
		gc = null;
		pdfDocument.dispose();
		pdfDocument = null;

		// Verify the PDF file was created and is not empty
		assertTrue(tempFile.exists(), "PDF file should exist");
		assertTrue(tempFile.length() > 0, "PDF file should not be empty");

		// Verify PDF magic bytes and content
		byte[] fileContent = Files.readAllBytes(tempFile.toPath());
		assertTrue(fileContent.length >= 5, "PDF file should have at least 5 bytes for header");

		// Check for PDF magic bytes: %PDF-
		String headerString = new String(fileContent, 0, Math.min(5, fileContent.length));
		assertTrue(headerString.startsWith("%PDF-"),
			"PDF file should start with %PDF- magic bytes, but got: " + headerString);
	}

	@Test
	public void test_createPDFDocumentWithPageSize() throws IOException {
		// Create a temporary file for the PDF
		tempFile = Files.createTempFile(tempDir, "test_pagesize", ".pdf").toFile();
		String filename = tempFile.getAbsolutePath();

		// Create PDF document using PageSize constant
		pdfDocument = new PDFDocument(filename, PageSize.A4);
		assertNotNull(pdfDocument, "PDFDocument should be created with PageSize");

		// Create a GC on the PDF document
		gc = new GC(pdfDocument);
		assertNotNull(gc, "GC should be created on PDFDocument");

		// Draw text
		gc.drawText("Hello from PageSize.A4!", 100, 100);

		// Dispose of resources to finalize the PDF
		gc.dispose();
		gc = null;
		pdfDocument.dispose();
		pdfDocument = null;

		// Verify the PDF file was created and is not empty
		assertTrue(tempFile.exists(), "PDF file should exist");
		assertTrue(tempFile.length() > 0, "PDF file should not be empty");

		// Verify PDF magic bytes
		byte[] fileContent = Files.readAllBytes(tempFile.toPath());
		String headerString = new String(fileContent, 0, Math.min(5, fileContent.length));
		assertTrue(headerString.startsWith("%PDF-"),
			"PDF file should start with %PDF- magic bytes, but got: " + headerString);
	}

	@Test
	public void test_pageSizePredefinedConstants() {
		// Verify predefined paper sizes have correct dimensions
		assertEquals(612, PageSize.LETTER.width());
		assertEquals(792, PageSize.LETTER.height());

		assertEquals(612, PageSize.LEGAL.width());
		assertEquals(1008, PageSize.LEGAL.height());

		assertEquals(595, PageSize.A4.width());
		assertEquals(842, PageSize.A4.height());

		assertEquals(842, PageSize.A3.width());
		assertEquals(1191, PageSize.A3.height());

		assertEquals(420, PageSize.A5.width());
		assertEquals(595, PageSize.A5.height());

		assertEquals(522, PageSize.EXECUTIVE.width());
		assertEquals(756, PageSize.EXECUTIVE.height());

		assertEquals(792, PageSize.TABLOID.width());
		assertEquals(1224, PageSize.TABLOID.height());
	}

	@Test
	public void test_pageSizeCustom() {
		// Verify custom page sizes can be created
		PageSize custom = new PageSize(300, 400);
		assertEquals(300, custom.width());
		assertEquals(400, custom.height());
	}

	@Test
	public void test_createPDFDocumentWithNullPageSize() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PDFDocument("test.pdf", (PageSize) null);
		});
	}

	@Test
	public void test_pageSizeEquality() {
		// Records should have value-based equality
		PageSize a4Copy = new PageSize(595, 842);
		assertEquals(PageSize.A4, a4Copy);
	}

}
