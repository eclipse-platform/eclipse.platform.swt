package org.eclipse.swt.browser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.*;

import org.eclipse.swt.internal.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
class EdgeTests {

	/**
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/1912
	 */
	@Test
	public void handlingOfTempDirWithSpacesAndUnicodeCharacters() throws Exception {
		String originalTempDir = System.getProperty("java.io.tmpdir");
		String temporaryTempDirWithSpacesAndUnicode = Files.createTempDirectory("spaces and únîcòde").toString();
		System.setProperty("java.io.tmpdir", temporaryTempDirWithSpacesAndUnicode);
		try {
			Edge.setupLocationForCustomTextPage();

			// URI_FOR_CUSTOM_TEXT_PAGE must already be encoded internally
			assertTrue(Edge.URI_FOR_CUSTOM_TEXT_PAGE.toString().contains("spaces%20and%20%C3%BAn%C3%AEc%C3%B2de"));
			assertTrue(Edge.isLocationForCustomText(Edge.URI_FOR_CUSTOM_TEXT_PAGE.toASCIIString()));

		} finally {
			System.setProperty("java.io.tmpdir", originalTempDir);
			Edge.setupLocationForCustomTextPage(); // restore the original value
		}
	}
}
