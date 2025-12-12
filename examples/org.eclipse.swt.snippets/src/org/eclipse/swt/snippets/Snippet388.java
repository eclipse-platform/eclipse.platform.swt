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
package org.eclipse.swt.snippets;

import java.util.concurrent.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet388 {

	public static void main(final String[] args) {
		final Display display = new Display();
		try {
			concurrentPlainImage(display);
		} catch (Exception e)  {
			e.printStackTrace();
		}
		try {
			concurrentDataProvider(display);
		} catch (Exception e)  {
			e.printStackTrace();
		}
		display.dispose();
		System.exit(0);
	}

	public static void concurrentPlainImage(final Display display) throws InterruptedException, ExecutionException {
		Image image = new Image(display, 1, 1);
		Future<ImageData> future1 = Executors.newSingleThreadExecutor().submit(() -> image.getImageData());
		Thread.sleep(1000);
		Future<ImageData> future2 = Executors.newSingleThreadExecutor().submit(() -> image.getImageData());
		future1.get();
		future2.get();
	}

	private static void concurrentDataProvider(final Display display) throws InterruptedException, ExecutionException {
		ImageDataProvider provider = zoom -> new ImageLoader().load("src/org/eclipse/swt/snippets/eclipse16.png")[0];
		Image image = new Image(display, provider);
		Future<?> future1 = Executors.newSingleThreadExecutor().submit(() -> image.win32_getHandle(image, 100));
		Thread.sleep(1000);
		Future<?> future2 = Executors.newSingleThreadExecutor().submit(() -> image.win32_getHandle(image, 100));
		future1.get();
		future2.get();
	}

}