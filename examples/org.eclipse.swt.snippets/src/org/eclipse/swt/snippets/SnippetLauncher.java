package org.eclipse.swt.snippets;

import java.lang.reflect.*;
import java.io.*;

public class SnippetLauncher {

	public static void main (String [] args) {
		File sourceDir = new File("src/org/eclipse/swt/snippets");
		boolean hasSource = sourceDir.exists();
		int count = 300;
		if (hasSource) {
			File [] files = sourceDir.listFiles();
			if (files.length > 0) count = files.length;
		}
		for (int i = 1; i < count; i++) {
			String className = "Snippet" + i;
			Class clazz = null;
			try {
				clazz = Class.forName("org.eclipse.swt.snippets." + className);
			} catch (ClassNotFoundException e) {}
			if (clazz != null) {
				System.out.println("\n" + clazz.getName());
				if (hasSource) {
					File sourceFile = new File(sourceDir, className + ".java");
					try {
						FileReader reader = new FileReader(sourceFile);
						char [] buffer = new char [(int)sourceFile.length()];
						reader.read(buffer);
						String source = String.valueOf(buffer);
						int start = source.indexOf("package");
						start = source.indexOf("/*", start);
						int end = source.indexOf("* For a list of all");
						System.out.println(source.substring(start, end-3));
						if (source.indexOf("PocketPC") != -1) {
							System.out.println("...skipping PocketPC example...");
							continue;
						}
					} catch (Exception e) {}
				}
				Method method = null;
				String [] param = new String [0];
				try {
					method = clazz.getMethod("main", new Class[] {param.getClass()});
				} catch (NoSuchMethodException e) {
					System.out.println("   Did not find main(String [])");
				}
				if (method != null) {
					try {
						method.invoke(clazz, new Object [] {param});
					} catch (Exception e) {
						System.out.println("   Failed to launch");
					}
				}
			}
		}
	}
}
