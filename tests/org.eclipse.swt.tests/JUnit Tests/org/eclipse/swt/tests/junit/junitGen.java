/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.util.zip.*;
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import java.net.*;

/**
 * Create SWT Junit stubs based on a swt jar used as a reference
 * Command line tool - run without parameters to get help information
 */
public class junitGen {
	String destFolder = null;
	int counter = 0;
	
	/**
	 * @param jarfile the url of a jar file
	 * @return all public classes in the jar
	 */
	public Vector getClasses(String jarfile) {
		Vector vector = new Vector();
		try {
			ZipFile zipFile = new ZipFile(jarfile);
			File file = new File(jarfile);
			URL url = file.toURL();
			ClassLoader loader = new URLClassLoader(new URL[] { url });

			Enumeration entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry)entries.nextElement();
				String name = entry.getName();
				if (name.endsWith(".class")) {
					name = name.substring(0, name.length() - ".class".length());
					name = name.replace('/', '.');
					Class clazz = Class.forName(name, false, loader);
					if (filterClass(clazz)) vector.add(clazz);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception " + e);
			return null;
		}
		return vector;
	}
	/**
	 * Create a Junit java test file
	 * @param clazz class to create the test for
	 */
	public void createFileTest(Class clazz) {
		try {
			String name = getClassName(clazz);
			name = destFolder + "/" + name + ".java";
			FileWriter writer = new FileWriter(name);
			createSwtTest(writer, clazz);
			writer.close();
		} catch (Exception e) {
			System.out.println("Exception " + e);
		}
		
	}

	void createSwtTest(FileWriter writer, Class clazz) throws IOException {
		SwtTest test = new SwtTest(writer, clazz);
		test.writeCopyRights();
		test.writePackage();
		test.newLine();
		test.writeImports();
		test.newLine();
		test.writeClassComments();
		test.writeClassDeclaration();
		test.newLine();
		test.writeConstructor();		
		test.newLine();
		test.writeMain();
		test.newLine();
		test.writeSetUp();
		test.newLine();
		test.writeTearDown();
		test.newLine();
		test.writeMethods();
		test.newLine();
		test.writeSuite();
		test.newLine();
		test.writeMethodNames();
		test.newLine();
		test.writeRunTest();
		test.writeClassClosure();
	}
	
	public void setDestinationFolder(String dest) {
		destFolder = dest;	
	}

	public void incrementCounter() {
		counter++;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public static void createTests(String jarfile, String destFolder) {
		junitGen gen = new junitGen();
		Vector classes = gen.getClasses(jarfile);
		gen.setDestinationFolder(destFolder);
		if (classes == null) {
			System.out.println("No classes found.");
			return;
		}
		Enumeration entries = classes.elements();
		while (entries.hasMoreElements()) {
			gen.createFileTest((Class)entries.nextElement());
		}
		System.out.println("Test stubs written: " + gen.getCounter());
	}

	public void diff(Vector oldClasses, Vector newClasses) {
		SwtDiff diff = new SwtDiff(oldClasses, newClasses);
		diff.compute();		
	}
	
	public static void diffTests(String jarfileOld, String jarfileNew) {
		junitGen gen = new junitGen();
		Vector oldClasses = gen.getClasses(jarfileOld);
		if (oldClasses == null) {
			System.out.println("No classes found in old jar");
			return;
		}
		Vector newClasses = gen.getClasses(jarfileNew);
		if (newClasses == null) {
			System.out.println("No classes found in new jar");
			return;
		}
		gen.diff(oldClasses, newClasses);	
	}

	public static void main(String[] args) {
		if (args == null || args.length < 3) {
			System.out.println("Usage: java junitGen flag arg1 arg2 ...");
			System.out.println(" where flag is one of:");
			System.out.println("create: create the junit test stubs");
			System.out.println("       arg1: source jar e.g. d:/swt.jar");
			System.out.println("       arg2: destination e.g. d:/tests");
			System.out.println("diff:   diff 2 jars showing which tests must be removed,");
			System.out.println("        and added");
			System.out.println("       arg1: old jar e.g. d:/old/swt.jar");
			System.out.println("       arg2: new jar e.g. d:/new/swt.jar");	
			return;
		}
		if (args[0].equals("create")) createTests(args[1], args[2]);
		else if (args[0].equals("diff")) diffTests(args[1], args[2]);
	}
	
	boolean filterClass(Class clazz) {
		/* JUnit tests for public classes only */
		int modifiers = clazz.getModifiers();
		if (!Modifier.isPublic(modifiers)) return false;

		/* JUnit tests for classes which are not part of an internal package */
		String packageName = clazz.getPackage().getName();
		if (packageName.indexOf("internal") != -1) return false;

		/* JUnit tests for classes with public constructors or public methods */
		Constructor[] constructors = getFilteredConstructors(clazz);
		Method[] methods = getFilteredMethods(clazz);
		if (constructors.length == 0 && methods.length == 0) return false;
		return true;
	}

	String getClassName(Class clazz) {
		String name = clazz.getName();
		name = "Test_" + name.replace('.', '_');
		return name;
	}

	String getMethodName(Constructor constructor) {
		String name = "test_Constructor";
		Class[] parameters = constructor.getParameterTypes();
		for (int i = 0; i < parameters.length; i++) {
			name += getTypeSignature(parameters[i]);
		}
		return name;
	}

	String getMethodName(Method method) {
		String name = "test_" + method.getName();
		Class[] parameters = method.getParameterTypes();
		for (int i = 0; i < parameters.length; i++) {
			name += getTypeSignature(parameters[i]);
		}
		return name;
	}
		
	String getTypeSignature(Class clazz) {
		if (clazz == Integer.TYPE) return "I";
		if (clazz == Boolean.TYPE) return "Z";
		if (clazz == Long.TYPE) return "J";
		if (clazz == Short.TYPE) return "S";
		if (clazz == Character.TYPE) return "C";
		if (clazz == Byte.TYPE) return "B";
		if (clazz == Float.TYPE) return "F";
		if (clazz == Double.TYPE) return "D";
		if (clazz.isArray()) {
			Class componentType = clazz.getComponentType();
			return "$" + getTypeSignature(componentType);
		}
		return "L" + clazz.getName().replace('.', '_');
	}

	Constructor[] getFilteredConstructors(Class clazz) {
		Constructor[] constructors = clazz.getConstructors();
		Arrays.sort(constructors, new Comparator() {
			public int compare(Object a, Object b) {
				return (getMethodName((Constructor)a).compareTo(getMethodName((Constructor)b)));					
			}
		});
		int count = 0;
		for (int i = 0; i < constructors.length; i++) {
			Constructor constructor = constructors[i];
			int modifiers = constructor.getModifiers();
			if (Modifier.isPublic(modifiers)) {
				constructors[count++] = constructors[i];
			}
		}
		Constructor[] result = new Constructor[count];
		System.arraycopy(constructors, 0, result, 0, count);
		return result;
	}
		
	Method[] getFilteredMethods(Class clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		Arrays.sort(methods, new Comparator() {
			public int compare(Object a, Object b) {
				return (getMethodName((Method)a).compareTo(getMethodName((Method)b)));					
			}
		});
		int count = 0;
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			int modifiers = method.getModifiers();
			if (Modifier.isPublic(modifiers)) {
				methods[count++] = methods[i];
			}
		}
		Method[] result = new Method[count];
		System.arraycopy(methods, 0, result, 0, count);
		return result;		
	}


	public class SwtTest {
		FileWriter writer;
		Class clazz;
		String lineSep = "\r\n";
		
		SwtTest(FileWriter writer, Class clazz) {
			this.writer = writer;
			this.clazz = clazz;
		}
		
		void newLine() throws IOException {
			writer.write(lineSep);
		}
		
		void writePackage() throws IOException {
			writer.write("package org.eclipse.swt.tests.junit;");
			writer.write(lineSep);
			writer.write(lineSep);
		}
		
		void writeCopyRights() throws IOException {
			writer.write(
				"/*******************************************************************************" + lineSep +
 				" * Copyright (c) 2000, 2004 IBM Corporation and others." + lineSep +
 				" * All rights reserved. This program and the accompanying materials" + lineSep +
 				" * are made available under the terms of the Eclipse Public License v1.0" + lineSep +
 				" * which accompanies this distribution, and is available at" + lineSep +
 				" * http://www.eclipse.org/legal/epl-v10.html" + lineSep +
 				" * " + lineSep +
 				" * Contributors:" + lineSep +
 				" *     IBM Corporation - initial API and implementation" + lineSep +
				" *******************************************************************************/");
			writer.write(lineSep);
		}
		
		void writeImports() throws IOException {
			writer.write(
				"import junit.framework.*;" + lineSep +
				"import junit.textui.*;");
			writer.write(lineSep);
		}
		
		void writeClassComments() throws IOException {
			writer.write(
				"/**" + lineSep +
				" * Automated Test Suite for class " + clazz.getName() + lineSep +
				" *" + lineSep +
				" * @see " + clazz.getName() + lineSep +
				" */"
			);
			writer.write(lineSep);
		}

		boolean hasSwtJunitSuperClass(Class clazz) {
			Class superClass = clazz.getSuperclass();
			if (superClass == null) return false;
			String packageName = superClass.getPackage().getName();
			if (!packageName.startsWith("org.eclipse.swt")) return false;
			if (!filterClass(superClass)) return false;
			return true;						
		}
		
		String getSwtJunitSuperClassName(Class clazz) {
			String defaultClass = "SwtTestCase";
			Class superClass = clazz.getSuperclass();
			if (superClass == null) return defaultClass;
			String packageName = superClass.getPackage().getName();
			if (!packageName.startsWith("org.eclipse.swt")) return defaultClass;
			if (!filterClass(superClass)) return defaultClass;
			return getClassName(superClass);
		}
		
		void writeClassDeclaration() throws IOException {
			String name = getClassName(clazz);
			String superClassName = getSwtJunitSuperClassName(clazz);
			writer.write(
				"public class " + name + " extends " + superClassName +" {"
			);
			writer.write(lineSep);
		}
		
		void writeConstructor() throws IOException {
			String name = getClassName(clazz);
			writer.write(
				"public " + name + "(String name) {" + lineSep +
				"	super(name);" + lineSep +
				"}"
			);
			writer.write(lineSep);
		}		
		
		void writeMain() throws IOException {
			writer.write(
				"public static void main(String[] args) {" + lineSep +
				"	TestRunner.run(suite());" + lineSep +
				"}"
			);
			writer.write(lineSep);
		}

		void writeSetUp() throws IOException {
			writer.write(
				"protected void setUp() {"
			);
			writer.write(lineSep);
			if (hasSwtJunitSuperClass(clazz)) {
				writer.write("	super.setUp();");
				writer.write(lineSep);
			}
			writer.write("}");
			writer.write(lineSep);
		}

		void writeTearDown() throws IOException {
			writer.write(
				"protected void tearDown() {"
			);
			writer.write(lineSep);
			if (hasSwtJunitSuperClass(clazz)) {
				writer.write("	super.tearDown();");
				writer.write(lineSep);
			}
			writer.write("}");
			writer.write(lineSep);
		}
		
		void writeMethods() throws IOException {
			Constructor[] constructors = getFilteredConstructors(clazz);
			for (int i = 0; i < constructors.length; i++) {
				writeMethod(constructors[i]);
				if (i < constructors.length - 1) writer.write(lineSep);
			}
			
			Method[] methods = getFilteredMethods(clazz);
			if (constructors.length > 0 && methods.length > 0) writer.write(lineSep);
			for (int i = 0; i < methods.length; i++) {
				writeMethod(methods[i]);
				if (i < methods.length - 1) writer.write(lineSep);
			}
		}
		
		void writeMethod(Constructor constructor) throws IOException {
			String name = getMethodName(constructor);
			writer.write(
				"public void " + name + "() {" + lineSep +
				"	warnUnimpl(\"Test " + name + " not written\");" + lineSep +
				"}"
			);
			writer.write(lineSep);
			incrementCounter();
		}
		
		void writeMethod(Method method) throws IOException {
			String name = getMethodName(method);
			writer.write(
				"public void " + name + "() {" + lineSep +
				"	warnUnimpl(\"Test " + name + " not written\");" + lineSep +
				"}"
			);
			writer.write(lineSep);
			incrementCounter();
		}
		
		void writeSuite() throws IOException {
			String name = getClassName(clazz);
			writer.write(
				"public static Test suite() {" + lineSep +
				"	TestSuite suite = new TestSuite();" + lineSep +	
				"	java.util.Vector methodNames = methodNames();" + lineSep +
				"	java.util.Enumeration e = methodNames.elements();" + lineSep +
				"	while (e.hasMoreElements()) {" + lineSep +
				"		suite.addTest(new " + name + "((String)e.nextElement()));" + lineSep +
				"	}" + lineSep +
				"	return suite;" + lineSep +
				"}"
			);
			writer.write(lineSep);
		}

		void writeMethodNames() throws IOException {
			writer.write(
				"public static java.util.Vector methodNames() {" + lineSep +
				"	java.util.Vector methodNames = new java.util.Vector();"
			);
			writer.write(lineSep);
			Constructor[] constructors = getFilteredConstructors(clazz);
			for (int i = 0; i < constructors.length; i++) {
				String name = getMethodName(constructors[i]);
				writer.write(
					"	methodNames.addElement(\"" + name + "\");"
				);
				writer.write(lineSep);
			}
			Method[] methods = getFilteredMethods(clazz);
			for (int i = 0; i < methods.length; i++) {
				String name = getMethodName(methods[i]);
				writer.write(
					"	methodNames.addElement(\"" + name + "\");"
				);
				writer.write(lineSep);
			}
			if (hasSwtJunitSuperClass(clazz)) {
				String superClassName = getSwtJunitSuperClassName(clazz);
				writer.write("	methodNames.addAll(" + superClassName + ".methodNames()); // add superclass method names");
				writer.write(lineSep);
			}
			writer.write(
				"	return methodNames;" + lineSep +
				"}"
			);
			writer.write(lineSep);
		}
		
		void writeRunTest() throws IOException {
			writer.write(
				"protected void runTest() throws Throwable {"
			);
			writer.write(lineSep);
			Constructor[] constructors = getFilteredConstructors(clazz);
			Method[] methods = getFilteredMethods(clazz);
			for (int i = 0; i < constructors.length + methods.length; i++) {
				if (i == 0) writer.write("	if"); else writer.write("	else if");
				String name = i < constructors.length ? getMethodName(constructors[i]) : getMethodName(methods[i - constructors.length]);
				writer.write(
					" (getName().equals(\"" + name + "\")) " + name + "();"
				);
				writer.write(lineSep);
			}
			if (hasSwtJunitSuperClass(clazz)) {
				if (constructors.length + methods.length > 0) {
					writer.write("	else ");
				} else {
					writer.write("	");
				}
				writer.write("super.runTest();");
				writer.write(lineSep);
			}			
			writer.write("}");
			writer.write(lineSep);		
		}
		
		void writeClassClosure() throws IOException {
			writer.write("}");
			writer.write(lineSep);
		}
	}
	
	public class DiffClass {
		/* the newClass has been added */
		public static final int NEW = 1;
		/* the oldClass has been removed */
		public static final int REMOVED = 2;
		/* the removedMethods from oldClass have been removed (if any)
		 * the newMethods from newClass have been added
		 */
		public static final int MODIFIED = 3;
		
		int type;
		Class oldClass;
		Class newClass;
		Method[] removedMethods;
		Method[] newMethods;
		Constructor[] removedConstructors;
		Constructor[] newConstructors;

		public DiffClass(int type, 
			Class oldClass, Class newClass, 
			Method[] removedMethods, Method[] newMethods,
			Constructor[] removedConstructors, Constructor[] newConstructors) {
			this.type = type;
			this.oldClass = oldClass;
			this.newClass = newClass;
			this.removedMethods = removedMethods;
			this.newMethods = newMethods;
			this.removedConstructors = removedConstructors;
			this.newConstructors = newConstructors;
		}
		
		public String toString() {
			switch (type) {
				case NEW: {
					return "NEW: " + getClassName(newClass);
				}
				case REMOVED: {
					return "REMOVED: " + getClassName(oldClass);
				}
				case MODIFIED: {
					String string = "MODIFIED: " + getClassName(oldClass);
					if (removedConstructors != null) {
						string += "\r\n\t" + removedConstructors.length + " constructor(s) removed";
						for (int i = 0; i < removedConstructors.length; i++) {
							string += "\r\n\t" + getMethodName(removedConstructors[i]);
						}
					}

					if (newConstructors != null) {
						string += "\r\n\t" + newConstructors.length + " constructor(s) added";
						for (int i = 0; i < newConstructors.length; i++) {
							string += "\r\n\t" + getMethodName(newConstructors[i]);
						}
					}
					if (removedMethods != null) {
						string += "\r\n\t" + removedMethods.length + " method(s) removed";
						for (int i = 0; i < removedMethods.length; i++) {
							string += "\r\n\t" + getMethodName(removedMethods[i]);
						}
					}

					if (newMethods != null) {
						string += "\r\n\t" + newMethods.length + " method(s) added";
						for (int i = 0; i < newMethods.length; i++) {
							string += "\r\n\t" + getMethodName(newMethods[i]);
						}
					}
					return string;
				}
				default: {
					return "invalid DiffClass "+type;
				}
			}
		}
	}
	
	public class SwtDiff {
		Class[] oldClasses, newClasses;
		/* removed[i] true means oldClasses[i] has been removed */
		boolean[] removed;
		/* added[i] true means newClasses[i] has been added */
		boolean[] added;
		/* old2New[i] == j >= 0 means oldClasses[i] corresponds to newClasses[j] */
		int[] old2New;
		int[] new2Old;
		Vector diffs = new Vector();
		
		public SwtDiff(Vector oldClasses, Vector newClasses) {
			Enumeration oldClassesEnum = oldClasses.elements();
			this.oldClasses = new Class[oldClasses.size()];
			int cnt = 0;
			while (oldClassesEnum.hasMoreElements()) {
				this.oldClasses[cnt++] = (Class)oldClassesEnum.nextElement();
			}

			Enumeration newClassesEnum = newClasses.elements();
			this.newClasses = new Class[newClasses.size()];
			cnt = 0;
			while (newClassesEnum.hasMoreElements()) {
				this.newClasses[cnt++] = (Class)newClassesEnum.nextElement();
			}
			old2New = new int[this.oldClasses.length];
			new2Old = new int[this.newClasses.length];
		}
		
		public void compute() {
			compareClasses();
			createClassDiff();
			outputClassDiff();		
		}

		void compareClasses() {
			for (int i = 0; i < oldClasses.length; i++) {
				old2New[i] = -1;
				for (int j = 0; j < newClasses.length; j++) {
					if (oldClasses[i].getName().equals(newClasses[j].getName())) {
						old2New[i] = j;
					}
				}
			}
			
			for (int i = 0; i < newClasses.length; i++) {
				new2Old[i] = -1;
				for (int j = 0; j < oldClasses.length; j++) {
					if (newClasses[i].getName().equals(oldClasses[j].getName())) {
						new2Old[i] = j;
					}
				}
			}
		}
		
		void createClassDiff() {
			for (int i = 0; i < old2New.length; i++) {
				if (old2New[i] < 0) {
					/* Found a class which has been removed */
					DiffClass diff = new DiffClass(
						DiffClass.REMOVED,
						oldClasses[i],
						null,
						null,
						null,
						null,
						null);
					diffs.add(diff);
				} else {
					/* Found a class which is either identical or which contains
					 * modified methods
					 */
					Class oldClass = oldClasses[i];
					Class newClass = newClasses[old2New[i]];
					Method[] removedMethods = getRemovedMethods(oldClass, newClass);
					Method[] addedMethods = getAddedMethods(oldClass, newClass);
					Constructor[] removedConstructors = getRemovedConstructors(oldClass, newClass);
					Constructor[] addedConstructors = getAddedConstructors(oldClass, newClass);
					if (removedMethods != null || addedMethods != null || 
						removedConstructors != null || addedConstructors != null) {
						DiffClass diff = new DiffClass(
					 		DiffClass.MODIFIED,
					 		oldClass,
					 		newClass,
					 		removedMethods,
					 		addedMethods,
					 		removedConstructors,
					 		addedConstructors
					 	);
					 	diffs.add(diff);
					 }
				}
			}
			for (int i = 0; i < new2Old.length; i++) {
				if (new2Old[i] < 0) {
					/* Found a class which has been added */
					DiffClass diff = new DiffClass(
						DiffClass.NEW,
						null,
						newClasses[i],
						null,
						null,
						null,
						null
					);
					diffs.add(diff);
				}
			}
		}

		Method[] getRemovedMethods(Class oldClass, Class newClass) {
			Method[] oldMethods = getFilteredMethods(oldClass);
			Method[] newMethods = getFilteredMethods(newClass);
			
			int count = 0;
			for (int i = 0; i < oldMethods.length; i++) {
				String oldName = getMethodName(oldMethods[i]);
				boolean removed = true;
				for (int j = 0; j < newMethods.length; j++) {
					String newName = getMethodName(newMethods[j]);
					if (oldName.equals(newName)) {
						removed = false;
						break;
					}
				}
				if (removed) oldMethods[count++] = oldMethods[i];
			}
			if (count == 0) return null;
			Method[] result = new Method[count];
			System.arraycopy(oldMethods, 0, result, 0, count);
			return result;
		}
		
		Method[] getAddedMethods(Class oldClass, Class newClass) {
			return getRemovedMethods(newClass, oldClass);
		}
		
		Constructor[] getRemovedConstructors(Class oldClass, Class newClass) {
			Constructor[] oldConstructors = getFilteredConstructors(oldClass);
			Constructor[] newConstructors = getFilteredConstructors(newClass);
			
			int count = 0;
			for (int i = 0; i < oldConstructors.length; i++) {
				String oldName = getMethodName(oldConstructors[i]);
				boolean removed = true;
				for (int j = 0; j < newConstructors.length; j++) {
					String newName = getMethodName(newConstructors[j]);
					if (oldName.equals(newName)) {
						removed = false;
						break;
					}
				}
				if (removed) oldConstructors[count++] = oldConstructors[i];
			}
			if (count == 0) return null;
			Constructor[] result = new Constructor[count];
			System.arraycopy(oldConstructors, 0, result, 0, count);
			return result;
		}
		
		Constructor[] getAddedConstructors(Class oldClass, Class newClass) {
			return getRemovedConstructors(newClass, oldClass);
		}
		
		void outputClassDiff() {
			Enumeration elements = diffs.elements();
			int changes = 0;
			while (elements.hasMoreElements()) {
				DiffClass diff = (DiffClass)elements.nextElement();
				System.out.println("["+changes+"] "+ diff);
				changes++;
			}
		}
	}
}
