/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;
//package api.visibility;
//
//import static org.junit.Assert.*;
//
//import java.lang.reflect.Field;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
//import org.junit.Test;
//
//public class Bug497705_nullCordsOnInvisible {
//	
//
//	// The jvm doesn't permit one to set the environmental values of parents.
//	// TODO
//	// Linux only.
//	// https://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java
//	public static void setEnv(Map<String, String> newenv) throws Exception {
//	    Class[] classes = Collections.class.getDeclaredClasses();
//	    Map<String, String> env = System.getenv();
//	    for(Class cl : classes) {
//	        if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
//	            Field field = cl.getDeclaredField("m");
//	            field.setAccessible(true);
//	            Object obj = field.get(env);
//	            Map<String, String> map = (Map<String, String>) obj;
//	            map.clear();
//	            map.putAll(newenv);
//	        }
//	    }
//	}
//
//
//	@Test
//	public void myTestG2() {
//		System.gc();
//		Map<String, String> newEnv = new HashMap<>();
//		newEnv.put("SWT_GTK3", "0");
//		try {
//			setEnv(newEnv);
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("Faild to set proper version of gtk");
//		}
//		
//		String property = System.getenv("SWT_GTK3");
//		System.err.println("GTK" + (property.equals("1") ? "3" : "2"));
//		Display display = Display.getDefault();
//		Shell shell = new Shell(display);
//		shell.setSize(400, 400);
//		StringBuffer log = new StringBuffer("");
//		int x = 5;
//		int y = 10;
//		int height = 100;
//		int width = 200;
//		boolean passed = true;
//		
//		
//		Button myButton = new Button(shell, SWT.PUSH);
//		myButton.setText("Sized button");
//		myButton.setVisible(false);
//		myButton.setVisible(true);
//		myButton.setBounds(x, y, width, height);
//		Rectangle bounds = myButton.getBounds();
//		if (bounds.x != x | bounds.y != y) {
//			passed = false;
//			log.append("\nERROR: x,y do not match. Expected:" + x + "/" + y + " Actual:" + bounds.x + "/" + bounds.y);
//		}
//		if (bounds.height != height | bounds.width != width) {
//			passed = false;
//			log.append("\nERROR: width,height do not match. Expected:" + width + "/" + height + " Actual:" + bounds.width + "/" + bounds.height);
//		}
//		display.dispose();
//		assertTrue(log.toString(), passed);
//		
//	}
//	
//	@Test
//	public void myTestG3() {
//		System.gc();
//		Map<String, String> newEnv = new HashMap<>();
//		newEnv.put("SWT_GTK3", "1");
//		try {
//			setEnv(newEnv);
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("Faild to set proper version of gtk");
//		}
//		
//		String property = System.getenv("SWT_GTK3");
//		System.err.println("GTK" + (property.equals("1") ? "3" : "2"));
//		Display display = Display.getDefault();
//		Shell shell = new Shell(display);
//		shell.setSize(400, 400);
//		StringBuffer log = new StringBuffer("");
//		int x = 5;
//		int y = 10;
//		int height = 100;
//		int width = 200;
//		boolean passed = true;
//		
//		
//		Button myButton = new Button(shell, SWT.PUSH);
//		myButton.setText("Sized button");
//		myButton.setVisible(false);
//		myButton.setVisible(true);
//		myButton.setBounds(x, y, width, height);
//		Rectangle bounds = myButton.getBounds();
//		if (bounds.x != x | bounds.y != y) {
//			passed = false;
//			log.append("\nERROR: x,y do not match. Expected:" + x + "/" + y + " Actual:" + bounds.x + "/" + bounds.y);
//		}
//		if (bounds.height != height | bounds.width != width) {
//			passed = false;
//			log.append("\nERROR: width,height do not match. Expected:" + width + "/" + height + " Actual:" + bounds.width + "/" + bounds.height);
//		}
//		display.dispose();
//		assertTrue(log.toString(), passed);
//	
//		
////		shell.open();
////		while (!shell.isDisposed()) {
////			if (!display.readAndDispatch())
////				display.sleep();
////		}
////		display.dispose();
//	}
//}
