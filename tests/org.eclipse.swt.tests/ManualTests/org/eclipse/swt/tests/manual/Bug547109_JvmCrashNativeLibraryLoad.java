/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.manual;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.Library;
import org.eclipse.swt.internal.Platform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Bug547109_JvmCrashNativeLibraryLoad {
	private static final String ARG_NO_UI = "-noUI";
	private static final int EXIT_CODE_GOOD = 0;

	private static void createEmptyFolder(File folder) {
		if (!folder.exists()) {
			folder.mkdir();
		}
		else {
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}

	private static void test(Shell shell) {
		// Create a folder to hold the native libraries
		// Delete any libraries if already present
		File libsFolder = new File("nativeLibs");
		createEmptyFolder(libsFolder);

		boolean isMacOS = Platform.PLATFORM.equalsIgnoreCase("cocoa");

		// Run processes
		ProcessBuilder processBuilder = new ProcessBuilder(
				System.getProperty("java.home") + "/bin/java",
				"-Dswt.library.path=" + libsFolder.getAbsolutePath(),
				"-classpath", System.getProperty("java.class.path"),
				isMacOS ? "-XstartOnFirstThread" : "",
				Bug547109_JvmCrashNativeLibraryLoad.class.getName(),
				ARG_NO_UI
		);
		processBuilder.inheritIO();

		int numErrors = 0;
		try {
			Process[] processes = new Process[15];

			// Run many processes at once
			for (int i = 0; i < processes.length; i++) {
				processes[i] = processBuilder.start();
			}

			// Test for errors
			for (int i = 0; i < processes.length; i++) {
				int exitCode = processes[i].waitFor();
				if (EXIT_CODE_GOOD != exitCode) numErrors++;
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}

		// Report
		String message = String.format("%d crashes/errors detected", numErrors);
		MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION);
		dialog.setMessage(message);
		dialog.open();
	}

	private static boolean isSwtJar() {
		// Code from Library.loadLibrary
		String libName = "swt-" + Platform.PLATFORM + "-" + Library.getVersionString();
		// Code from Library.mapLibraryName
		libName = System.mapLibraryName (libName);
		String ext = ".dylib"; //$NON-NLS-1$
		if (libName.endsWith(ext)) {
			libName = libName.substring(0, libName.length() - ext.length()) + ".jnilib"; //$NON-NLS-1$
		}

		// Is it available?
		try (InputStream inputStream = Display.class.getResourceAsStream("/" + libName))
		{
			return (inputStream != null);
		} catch (Throwable e) {
			return false;
		}
	}

	public static void main(String[] args) {
		final Display display = new Display();

		if (Arrays.asList(args).contains(ARG_NO_UI)) {
			System.exit(EXIT_CODE_GOOD);
		}

		final Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setSize(300, 200);

		FillLayout layout = new FillLayout();
		layout.marginWidth = 30;
		layout.marginHeight = 30;
		shell.setLayout(layout);

		final Button button = new Button(shell, SWT.PUSH | SWT.WRAP);

		if (isSwtJar()) {
			button.setText("Click me && wait for msgbox\n\nIf you have crashes/errors, bug exists. Check console to see detected problems.");
			button.addListener(SWT.Selection, event -> {
				test(shell);
			});
		} else {
			// Only swt.jar has the libraries
			button.setText("Native library resource not found. Add it to classpath or run with swt.jar");
		}

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
