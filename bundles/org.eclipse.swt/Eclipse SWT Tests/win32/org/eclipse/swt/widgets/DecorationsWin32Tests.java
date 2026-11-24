/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
class DecorationsWin32Tests {

	private Display display;
	private Shell shell;
	private Image previousImage;
	private Image failingImage;
	private final AtomicBoolean failImageDataRetrieval = new AtomicBoolean();

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
		previousImage = new Image(display, 16, 16);
		failingImage = new Image(display, (ImageDataProvider) zoom -> {
			if (failImageDataRetrieval.get()) {
				SWT.error(SWT.ERROR_NO_HANDLES);
			}
			return new ImageData(16 * zoom / 100, 16 * zoom / 100, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
		});
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
		previousImage.dispose();
		failingImage.dispose();
	}

	@Test
	public void testImageUnchangedWhenSettingImageFails() {
		shell.setImage(previousImage);

		assertSettingImageFails(() -> shell.setImage(failingImage));

		assertSame(previousImage, shell.getImage());
	}

	@Test
	public void testImagesUnchangedWhenSettingImagesFails() {
		shell.setImages(new Image[] { previousImage });

		assertSettingImageFails(() -> shell.setImages(new Image[] { failingImage }));

		assertArrayEquals(new Image[] { previousImage }, shell.getImages());
	}

	private void assertSettingImageFails(Runnable imageSetter) {
		failImageDataRetrieval.set(true);
		assertThrows(SWTError.class, imageSetter::run);
		failImageDataRetrieval.set(false);
	}
}
