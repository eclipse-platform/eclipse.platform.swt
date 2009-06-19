/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import java.util.*;
import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.program.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.program.Program
 *
 * @see org.eclipse.swt.program.Program
 */
public class Test_org_eclipse_swt_program_Program extends SwtTestCase {
	
public Test_org_eclipse_swt_program_Program(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	Display.getDefault();
}

public void test_equalsLjava_lang_Object() {
	String[] extensions = Program.getExtensions();
	// No assertion here because the doc does not guarantee a non-null result.
	if (extensions != null) {
		for (int i=0; i<extensions.length; i++) {
			Program program = Program.findProgram(extensions[i]);
			if (program != null) {
				assertTrue(program.equals(program));
			}
		}
	}
}

public void test_executeLjava_lang_String() {
	
	// This test is incomplete because a true test of execute would open
	// an application that cannot be programmatically closed.
	
	try {
		Program[] programs = Program.getPrograms();
		if (programs != null && programs.length > 0) {

			// Cannot test empty string argument because it may launch something.
			//boolean result = programs[0].execute("");
			//assertFalse(result);
			
			// test null argument
				
			programs[0].execute(null);
			fail("Failed to throw ERROR_NULL_ARGUMENT");
		}
	} catch (IllegalArgumentException e) {
		assertEquals("Failed to throw ERROR_NULL_ARGUMENT", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

public void test_findProgramLjava_lang_String() {
	String[] extensions = Program.getExtensions();
	// No assertion here because the doc does not guarantee a non-null result.
	if (extensions != null) {
		for (int i=0; i<extensions.length; i++) {
			Program.findProgram(extensions[i]);
			// No assertion here because a null result is allowed.
		}
	}
	
	try {
		Program.findProgram(null);
		fail("Failed to throw ERROR_NULL_ARGUMENT");
	} catch (IllegalArgumentException e) {
		assertEquals("Failed to throw ERROR_NULL_ARGUMENT", SWT.ERROR_NULL_ARGUMENT, e);
	} catch (Exception e) {
		fail("Invalid Exception thrown of type "+e.getClass());
	} catch (Error e) {
		fail("Invalid Error thrown of type "+e.getClass());
	}
}

public void test_getExtensions() {
	String[] extensions = Program.getExtensions();
	// No assertion here because the doc does not guarantee a non-null result.
	if (extensions != null) {
		for (int i=0; i<extensions.length; i++) {
			assertNotNull(extensions[i]);
		}
	}
}

public void test_getImageData() {
	String[] extensions = Program.getExtensions();
	// No assertion here because the doc does not guarantee a non-null result.
	if (extensions != null) {
		for (int i=0; i<extensions.length; i++) {
			Program program = Program.findProgram(extensions[i]);
			if (program != null) {
				program.getImageData();
				// Nothing to do.
			}
		}
	}
}

public void test_getName() {
	String[] extensions = Program.getExtensions();
	// No assertion here because the doc does not guarantee a non-null result.
	if (extensions != null) {
		for (int i=0; i<extensions.length; i++) {
			Program program = Program.findProgram(extensions[i]);
			if (program != null) {
				String name = program.getName();
				assertNotNull("Program has null name",name);
			}
		}
	}
}

public void test_getPrograms() {
	Program[] programs = Program.getPrograms();
	
	// The result is not well-documented, but it should 
	// be non-null and contain no null entries.
	
	assertNotNull(programs);
	
	Hashtable lookup = new Hashtable();
	for (int i=0; i<programs.length; i++) {
		
		// test non-null entry
		assertNotNull(programs[i]);
		
		// test unique hash code
		int hashCode = programs[i].hashCode();
		Integer key = new Integer(hashCode);
		if (lookup.contains(key)) {
			fail("Duplicate hash code for "+programs[i]+" (same as "+lookup.get(key)+")");
		}
		else {
			lookup.put(key,programs[i]);
		}
	}
}

public void test_hashCode() {
	// tested in test_getPrograms
}

public void test_launchLjava_lang_String() {

	// This test is incomplete because a true test of launch would open
	// an application that cannot be programmatically closed.
	
	// Cannot test empty string argument because it may launch something.
	
	// test null argument
	
	try {
		Program.launch(null);
		fail("Failed to throw ERROR_NULL_ARGUMENT");
	} catch (IllegalArgumentException e) {
		assertEquals("Failed to throw ERROR_NULL_ARGUMENT", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

public void test_toString() {
	String[] extensions = Program.getExtensions();
	// No assertion here because the doc does not guarantee a non-null result.
	if (extensions != null) {
		for (int i=0; i<extensions.length; i++) {
			Program program = Program.findProgram(extensions[i]);
			if (program != null) {
				String string = program.toString();
				assertNotNull("toString returned null",string);
			}
		}
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_program_Program((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_executeLjava_lang_String");
	methodNames.addElement("test_getExtensions");
	methodNames.addElement("test_findProgramLjava_lang_String");
	methodNames.addElement("test_getImageData");
	methodNames.addElement("test_getName");
	methodNames.addElement("test_getPrograms");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_launchLjava_lang_String");
	methodNames.addElement("test_toString");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_executeLjava_lang_String")) test_executeLjava_lang_String();
	else if (getName().equals("test_findProgramLjava_lang_String")) test_findProgramLjava_lang_String();
	else if (getName().equals("test_getExtensions")) test_getExtensions();
	else if (getName().equals("test_getImageData")) test_getImageData();
	else if (getName().equals("test_getName")) test_getName();
	else if (getName().equals("test_getPrograms")) test_getPrograms();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_launchLjava_lang_String")) test_launchLjava_lang_String();
	else if (getName().equals("test_toString")) test_toString();
}
}
