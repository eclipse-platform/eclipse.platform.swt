/*******************************************************************************
 * Copyright (c) 2022 Mat Booth and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.swt.tests.manual;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Under the following conditions:
 * <ul>
 * <li>At a scaling factor of 2</li>
 * <li>And smooth antialiasing model is enabled</li>
 * <li>And the <code>ImageDataProvider</code> can only provide images at a
 * scaling factor of 1</li>
 * </ul>
 * Then SWT should scale up the image by 200% and the resulting image should
 * be blurry, not pixelated.
 * <p>
 * SWT should <b>especially</b> not throw an
 * <code>java.lang.IllegalArgumentException</code> and die.
 * <p>
 * On Linux you may need to run this with GDK_SCALE=2 set in the environment.
 *
 * @see <a href=
 *      "https://github.com/eclipse-platform/eclipse.platform.swt/issues/445">Issue
 *      #445</a> for details.
 */
public class Issue0445_HiDPISmoothScaling {

	public static void main(String[] args) {
		// Enable smooth anti-aliasing and scaling factor of 2
		System.setProperty("swt.autoScale", "200");
		System.setProperty("swt.autoScale.method", "smooth");

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(100, 100);
		shell.setLayout(new GridLayout(1, true));

		System.out.println("Device Zoom: " + DPIUtil.getDeviceZoom());

		ImageDataProvider provider1x = zoom -> {
			if (zoom == 100) {
				ImageData result = null;
				try (InputStream in = new BufferedInputStream(
						Issue0445_HiDPISmoothScaling.class.getResourceAsStream("gear_icon1.png"))) {
					if (in != null) {
						result = new ImageData(in);
					}
				} catch (IOException | SWTException e) {
				}
				return result;
			}
			return null;
		};
		Image image1x = new Image(display, (ImageDataProvider) provider1x::getImageData);

		Label label = new Label(shell, SWT.NONE);
		label.setImage(image1x);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		image1x.dispose();
		display.dispose();
	}
}
