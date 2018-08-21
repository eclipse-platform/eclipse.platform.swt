/*******************************************************************************
 * Copyright (c) 2015, 2016 IBM Corporation and others.
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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.fail;

import org.eclipse.swt.graphics.RGBA;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.RGBA
 *
 * @see org.eclipse.swt.graphics.RGBA
 */
public class Test_org_eclipse_swt_graphics_RGBA {

@Test
public void test_ConstructorIIII() {
	// Test RGBA(int red, int green, int blue, int alpha)
	new RGBA(20,100,200,255);

	new RGBA(0,0,0,0);



	try {
		new RGBA(-1, 20, 50, 255);
		fail("No exception thrown for red < 0");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(256, 20, 50, 255);
		fail("No exception thrown for red > 255");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(20, -1, 50, 0);
		fail("No exception thrown for green < 0");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(20, 256, 50, 0);
		fail("No exception thrown for green > 255");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(20, 50, -1, 0);
		fail("No exception thrown for blue < 0");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(20, 50, 256, 0);
		fail("No exception thrown for blue > 255");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(20, 50, 10, -1);
		fail("No exception thrown for alpha < 0");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(20, 50, 10, 256);
		fail("No exception thrown for alpha > 255");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorFFFF() {

	new RGBA(0f,0f,0f,0f);

	new RGBA(0f,1f,0f,0f);
	new RGBA(0f,0f,1f,1f);
	new RGBA(0f,0.6f,0.4f,0.8f);
	new RGBA(1f,0f,1f,1f);
	new RGBA(1f,1f,1f,1f);
	new RGBA(1f,0f,1f,0f);
	new RGBA(1f,1f,0f,1f);
	new RGBA(1f,0.6f,0.4f,0.8f);
	new RGBA(59f,0f,1f,1f);
	new RGBA(59f,1f,1f,1f);
	new RGBA(59f,0f,1f,1f);
	new RGBA(59f,1f,0f,1f);
	new RGBA(59f,0.6f,0.4f,0.8f);
	new RGBA(60f,0f,1f,1f);
	new RGBA(60f,1f,1f,1f);
	new RGBA(60f,0f,1f,1f);
	new RGBA(60f,1f,0f,1f);
	new RGBA(60f,0.6f,0.4f,0.8f);
	new RGBA(61f,0f,1f,1f);
	new RGBA(61f,1f,1f,1f);
	new RGBA(61f,0f,1f,1f);
	new RGBA(61f,1f,0f,1f);
	new RGBA(61f,0.6f,0.4f,0.8f);
	new RGBA(119f,0f,1f,1f);
	new RGBA(119f,1f,1f,1f);
	new RGBA(119f,0f,1f,1f);
	new RGBA(119f,1f,0f,0f);
	new RGBA(119f,0.6f,0.4f,0.8f);
	new RGBA(120f,0f,1f,1f);
	new RGBA(120f,1f,1f,1f);
	new RGBA(120f,0f,1f,1f);
	new RGBA(120f,1f,0f,0f);
	new RGBA(120f,0.6f,0.4f,0.8f);
	new RGBA(121f,0f,1f,1f);
	new RGBA(121f,1f,1f,1f);
	new RGBA(121f,0f,1f,1f);
	new RGBA(121f,1f,0f,0f);
	new RGBA(121f,0.6f,0.4f,0.8f);
	new RGBA(179f,0f,1f,1f);
	new RGBA(179f,1f,1f,1f);
	new RGBA(179f,0f,1f,1f);
	new RGBA(179f,1f,0f,0f);
	new RGBA(179f,0.6f,0.4f,0.8f);
	new RGBA(180f,0f,1f,1f);
	new RGBA(180f,1f,1f,1f);
	new RGBA(180f,0f,1f,1f);
	new RGBA(180f,1f,0f,0f);
	new RGBA(180f,0.6f,0.4f,0.8f);
	new RGBA(181f,0f,1f,1f);
	new RGBA(181f,1f,1f,1f);
	new RGBA(181f,0f,1f,1f);
	new RGBA(181f,1f,0f,0f);
	new RGBA(181f,0.6f,0.4f,0.8f);
	new RGBA(239f,0f,1f,1f);
	new RGBA(239f,1f,1f,1f);
	new RGBA(239f,0f,1f,1f);
	new RGBA(239f,1f,0f,0f);
	new RGBA(239f,0.6f,0.4f,0.8f);
	new RGBA(240f,0f,1f,1f);
	new RGBA(240f,1f,1f,1f);
	new RGBA(240f,0f,1f,1f);
	new RGBA(240f,1f,0f,0f);
	new RGBA(240f,0.6f,0.4f,0.8f);
	new RGBA(241f,0f,1f,1f);
	new RGBA(241f,1f,1f,1f);
	new RGBA(241f,0f,1f,1f);
	new RGBA(241f,1f,0f,0f);
	new RGBA(241f,0.6f,0.4f,0.8f);
	new RGBA(299f,0f,1f,1f);
	new RGBA(299f,1f,1f,1f);
	new RGBA(299f,0f,1f,1f);
	new RGBA(299f,1f,0f,0f);
	new RGBA(299f,0.6f,0.4f,0.8f);
	new RGBA(300f,0f,1f,1f);
	new RGBA(300f,1f,1f,1f);
	new RGBA(300f,0f,1f,1f);
	new RGBA(300f,1f,0f,0f);
	new RGBA(300f,0.6f,0.4f,0.8f);
	new RGBA(301f,0f,1f,1f);
	new RGBA(301f,1f,1f,1f);
	new RGBA(301f,0f,1f,1f);
	new RGBA(301f,1f,0f,0f);
	new RGBA(301f,0.6f,0.4f,0.8f);
	new RGBA(359f,0f,1f,1f);
	new RGBA(359f,1f,1f,1f);
	new RGBA(359f,0f,1f,1f);
	new RGBA(359f,1f,0f,0f);
	new RGBA(359f,0.6f,0.4f,0.8f);
	new RGBA(360f,0f,1f,1f);
	new RGBA(360f,1f,1f,1f);
	new RGBA(360f,0f,1f,1f);
	new RGBA(360f,1f,0f,0f);
	new RGBA(360f,0.6f,0.4f,0.8f);

	try {
		new RGBA(400f, 0.5f, 0.5f, 0.8f);
		fail("No exception thrown for hue > 360");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(-5f, 0.5f, 0.5f, 0.8f);
		fail("No exception thrown for hue < 0");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(200f, -0.5f, 0.5f, 0.8f);
		fail("No exception thrown for saturation < 0");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(200f, 300f, 0.5f, 0.8f);
		fail("No exception thrown for saturation > 1");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(200f, 0.5f, -0.5f, 0.8f);
		fail("No exception thrown for brightness < 0");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(200f, 0.5f, 400f, 0.8f);
		fail("No exception thrown for brightness > 1");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(200f, 0.5f, 0.5f, -0.5f);
		fail("No exception thrown for alpha < 0");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGBA(200f, 0.5f, 0.5f, 400f);
		fail("No exception thrown for alpha > 1");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_equalsLjava_lang_Object() {
	int r = 0, g = 127, b = 254, a = 254;
	RGBA rgba1 = new RGBA(r, g, b, a);
	RGBA rgba2;

	rgba2 = rgba1;
	if (!rgba1.equals(rgba2)) {
		fail("Two references to the same RGBA instance not found equal");
	}

	rgba2 = new RGBA(r, g, b, a);
	if (!rgba1.equals(rgba2)) {
		fail("References to two different RGBA instances with same R G B A parameters not found equal");
	}

	if (rgba1.equals(new RGBA(r+1, g, b, a)) ||
	    rgba1.equals(new RGBA(r, g+1, b, a)) ||
	    rgba1.equals(new RGBA(r, g, b+1, a)) ||
	    rgba1.equals(new RGBA(r, g, b, a+1)) ||
	    rgba1.equals(new RGBA(r+1, g+1, b+1, a+1))) {
		fail("Comparing two RGBA instances with different combination of R G B A parameters found equal");
	}

	float hue = 220f, sat = 0.6f, bright = 0.7f, alpha = 0.5f;
	rgba1 = new RGBA(hue, sat, bright, alpha);
	rgba2 = rgba1;
	if (!rgba1.equals(rgba2)) {
		fail("Two references to the same RGBA instance not found equal");
	}

	rgba2 = new RGBA(hue, sat, bright, alpha);
	if (!rgba1.equals(rgba2)) {
		fail("References to two different RGBA instances with same H S B A parameters not found equal");
	}

	if (rgba1.equals(new RGBA(hue+1, sat, bright, alpha)) ||
	    rgba1.equals(new RGBA(hue, sat+0.1f, bright, alpha)) ||
	    rgba1.equals(new RGBA(hue, sat, bright+0.1f, alpha)) ||
	    rgba1.equals(new RGBA(hue, sat, bright, alpha+1f)) ||
	    rgba1.equals(new RGBA(hue+1, sat+0.1f, bright+0.1f, alpha+1f))) {
		fail("Comparing two RGBA instances with different combination of H S B A parameters found equal");
	}
}

@Test
public void test_getHSBA() {
	float[] hsba = new float[] {
				0f,0f,0f,0f,
				0f,1f,1f,1f,
				0f,1f,0f,0f,
				0f,0f,1f,1f,
				0f,0.6f,0.4f,0.8f,
				1f,0f,1f,1f,
				1f,1f,1f,1f,
				1f,0f,1f,1f,
				1f,1f,0f,0f,
				1f,0.6f,0.4f,0.8f,
				59f,0f,1f,1f,
				59f,1f,1f,1f,
				59f,0f,1f,1f,
				59f,1f,0f,0f,
				59f,0.6f,0.4f,0.8f,
				60f,0f,1f,1f,
				60f,1f,1f,1f,
				60f,0f,1f,1f,
				60f,1f,0f,0f,
				60f,0.6f,0.4f,0.8f,
				61f,0f,1f,1f,
				61f,1f,1f,1f,
				61f,0f,1f,1f,
				61f,1f,0f,0f,
				61f,0.6f,0.4f,0.8f,
				119f,0f,1f,1f,
				119f,1f,1f,1f,
				119f,0f,1f,1f,
				119f,1f,0f,0f,
				119f,0.6f,0.4f,0.8f,
				120f,0f,1f,1f,
				120f,1f,1f,1f,
				120f,0f,1f,1f,
				120f,1f,0f,0f,
				120f,0.6f,0.4f,0.8f,
				121f,0f,1f,1f,
				121f,1f,1f,1f,
				121f,0f,1f,1f,
				121f,1f,0f,0f,
				121f,0.6f,0.4f,0.8f,
				179f,0f,1f,1f,
				179f,1f,1f,1f,
				179f,0f,1f,1f,
				179f,1f,0f,0f,
				179f,0.6f,0.4f,0.8f,
				180f,0f,1f,1f,
				180f,1f,1f,1f,
				180f,0f,1f,1f,
				180f,1f,0f,0f,
				180f,0.6f,0.4f,0.8f,
				181f,0f,1f,1f,
				181f,1f,1f,1f,
				181f,0f,1f,1f,
				181f,1f,0f,0f,
				181f,0.6f,0.4f,0.8f,
				239f,0f,1f,1f,
				239f,1f,1f,1f,
				239f,0f,1f,1f,
				239f,1f,0f,0f,
				239f,0.6f,0.4f,0.8f,
				240f,0f,1f,1f,
				240f,1f,1f,1f,
				240f,0f,1f,1f,
				240f,1f,0f,0f,
				240f,0.6f,0.4f,0.8f,
				241f,0f,1f,1f,
				241f,1f,1f,1f,
				241f,0f,1f,1f,
				241f,1f,0f,0f,
				241f,0.6f,0.4f,0.8f,
				299f,0f,1f,1f,
				299f,1f,1f,1f,
				299f,0f,1f,1f,
				299f,1f,0f,0f,
				299f,0.6f,0.4f,0.8f,
				300f,0f,1f,1f,
				300f,1f,1f,1f,
				300f,0f,1f,1f,
				300f,1f,0f,0f,
				300f,0.6f,0.4f,0.8f,
				301f,0f,1f,1f,
				301f,1f,1f,1f,
				301f,0f,1f,1f,
				301f,1f,0f,0f,
				301f,0.6f,0.4f,0.8f,
				359f,0f,1f,1f,
				359f,1f,1f,1f,
				359f,0f,1f,1f,
				359f,1f,0f,0f,
				359f,0.6f,0.4f,0.8f,
				360f,0f,1f,1f,
				360f,1f,1f,1f,
				360f,0f,1f,1f,
				360f,1f,0f,0f,
				360f,0.6f,0.4f,0.8f,
				220f,0.6f,0.7f,0.8f};
	for (int i = 0; i < hsba.length; i+=4) {
		RGBA rgba1 = new RGBA(hsba[i], hsba[i+1], hsba[i+2], hsba[i+3]);
		float[] hsba2 = rgba1.getHSBA();
		RGBA rgba2 = new RGBA(hsba2[0], hsba2[1], hsba2[2], hsba2[3]);
		if (!rgba1.equals(rgba2)) {
			fail("Two references to the same RGBA using getHSB() function not found equal");
		}
	}
}

@Test
public void test_hashCode() {
	int r = 255, g = 100, b = 0, a = 0, different = 150;
	RGBA rgba1 = new RGBA(r, g, b, a);
	RGBA rgba2 = new RGBA(r, g, b, a);

	int hash1 = rgba1.hashCode();
	int hash2 = rgba2.hashCode();

	if (hash1 != hash2) {
		fail("Two RGBA instances with same R G B A parameters returned different hash codes");
	}

	if (rgba1.hashCode() == new RGBA(g, b, r, a).hashCode() ||
		rgba1.hashCode() == new RGBA(b, r, g, a).hashCode()) {
		fail("Two RGB instances with different R G B A parameters returned the same hash code");
	}

	if (rgba1.hashCode() == new RGBA(different, g, b, a).hashCode()) {
		fail("Two RGBA instances with different RED parameters returned the same hash code");
	}

	if (rgba1.hashCode() == new RGBA(r, different, b, a).hashCode()) {
		fail("Two RGBA instances with different GREEN parameters returned the same hash code");
	}

	if (rgba1.hashCode() == new RGBA(r, g, different, a).hashCode()) {
		fail("Two RGBA instances with different BLUE parameters returned the same hash code");
	}

	if (rgba1.hashCode() == new RGBA(r, g, b, different).hashCode()) {
		fail("Two RGBA instances with different ALPHA parameters returned the same hash code");
	}
}

@Test
public void test_toString() {
	RGBA rgba = new RGBA(0, 100, 200, 255);

	String s = rgba.toString();

	if (s == null || s.length() == 0) {
		fail("RGBA.toString returns a null or empty String");
	}
}
}
