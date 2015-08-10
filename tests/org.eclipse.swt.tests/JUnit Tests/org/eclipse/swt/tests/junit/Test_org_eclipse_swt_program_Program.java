/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.program.Program
 *
 * @see org.eclipse.swt.program.Program
 */
public class Test_org_eclipse_swt_program_Program {
	
@Before
public void setUp() {
	Display.getDefault();
}

@Test
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

@Test
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
		assertSWTProblem("Failed to throw ERROR_NULL_ARGUMENT", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

@Test
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
		assertSWTProblem("Failed to throw ERROR_NULL_ARGUMENT", SWT.ERROR_NULL_ARGUMENT, e);
	} catch (Exception e) {
		fail("Invalid Exception thrown of type "+e.getClass());
	} catch (Error e) {
		fail("Invalid Error thrown of type "+e.getClass());
	}
}

@Test
public void test_getExtensions() {
	String[] extensions = Program.getExtensions();
	// No assertion here because the doc does not guarantee a non-null result.
	if (extensions != null) {
		for (int i=0; i<extensions.length; i++) {
			assertNotNull(extensions[i]);
		}
	}
}

@Test
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

@Test
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

@Test
public void test_getPrograms() {
	Program[] programs = Program.getPrograms();
	
	// The result is not well-documented, but it should 
	// be non-null and contain no null entries.
	
	assertNotNull(programs);
	
	Hashtable<Integer, Program> lookup = new Hashtable<Integer, Program>();
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

@Test
public void test_launchLjava_lang_String() {

	// This test is incomplete because a true test of launch would open
	// an application that cannot be programmatically closed.
	
	// Cannot test empty string argument because it may launch something.
	
	// test null argument
	
	try {
		Program.launch(null);
		fail("Failed to throw ERROR_NULL_ARGUMENT");
	} catch (IllegalArgumentException e) {
		assertSWTProblem("Failed to throw ERROR_NULL_ARGUMENT", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

@Test
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

}
