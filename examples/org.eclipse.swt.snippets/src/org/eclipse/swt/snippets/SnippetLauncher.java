/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.io.*;
/*
 * Simple "hackable" code that runs all of the SWT Snippets,
 * typically for testing. One example of a useful "hack" is
 * to add the line:
 *    if (source.indexOf("Table") == -1 && source.indexOf("Tree") == -1) continue;
 * after the line:
 *    String source = String.valueOf(buffer);
 * in order to run all of the Table and Tree Snippets.
 */
import java.lang.reflect.*;

import org.eclipse.swt.*;

public class SnippetLauncher {

	public static void main (String [] args) {
		File sourceDir = new File("src/org/eclipse/swt/snippets");
		boolean hasSource = sourceDir.exists();
		int count = 500;
		if (hasSource) {
			File [] files = sourceDir.listFiles();
			if (files.length > 0) count = files.length;
		}
		for (int i = 1; i < count; i++) {
			if (i == 132 || i == 133 || i == 318) continue; // avoid printing to printer
			String className = "Snippet" + i;
			Class<?> clazz = null;
			try {
				clazz = Class.forName("org.eclipse.swt.snippets." + className);
			} catch (ClassNotFoundException e) {}
			if (clazz != null) {
				System.out.println("\n" + clazz.getName());
				if (hasSource) {
					File sourceFile = new File(sourceDir, className + ".java");
					try (FileReader reader = new FileReader(sourceFile);){
						char [] buffer = new char [(int)sourceFile.length()];
						reader.read(buffer);
						String source = String.valueOf(buffer);
						int start = source.indexOf("package");
						start = source.indexOf("/*", start);
						int end = source.indexOf("* For a list of all");
						System.out.println(source.substring(start, end-3));
						boolean skip = false;
						String platform = SWT.getPlatform();
						if (source.indexOf("PocketPC") != -1) {
							platform = "PocketPC";
							skip = true;
						} else if (source.indexOf("OpenGL") != -1) {
							platform = "OpenGL";
							skip = true;
						} else if (source.indexOf("JavaXPCOM") != -1) {
							platform = "JavaXPCOM";
							skip = true;
						} else {
							String [] platforms = {"win32", "gtk"};
							for (int p = 0; p < platforms.length; p++) {
								if (!platforms[p].equals(platform) && source.indexOf("." + platforms[p]) != -1) {
									platform = platforms[p];
									skip = true;
									break;
								}
							}
						}
						if (skip) {
							System.out.println("...skipping " + platform + " example...");
							continue;
						}
					} catch (Exception e) {
					}
				}
				Method method = null;
				String [] param = new String [0];
				if (i == 81) param = new String[] {"Shell.Explorer"};
				try {
					method = clazz.getMethod("main", param.getClass());
				} catch (NoSuchMethodException e) {
					System.out.println("   Did not find main(String [])");
				}
				if (method != null) {
					try {
						method.invoke(clazz, new Object [] {param});
					} catch (IllegalAccessException e) {
						System.out.println("   Failed to launch (illegal access)");
					} catch (IllegalArgumentException e) {
						System.out.println("   Failed to launch (illegal argument to main)");
					} catch (InvocationTargetException e) {
						System.out.println("   Exception in Snippet: " + e.getTargetException());
					}
				}
			}
		}
	}
}
