/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.widgets.LinkParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestLinkParser {
	@Test
	public void testNormal() {
		testLink(List.of(), "");

		testLink(List.of(new LinkParser.Part("hello", null)), "hello");

		testLink(List.of(new LinkParser.Part("", "")), "<a></a>");
		testLink(List.of(new LinkParser.Part("", "")), "<A></A>");
		testLink(List.of(new LinkParser.Part("a", "")), "<a>a</a>");
		testLink(List.of(new LinkParser.Part("a", "")), "<A>a</A>");
		testLink(List.of(new LinkParser.Part("a", "")), "<a href=\"\">a</a>");
		testLink(List.of(new LinkParser.Part("a", "")), "<A HREF=\"\">a</a>");
		testLink(List.of(new LinkParser.Part("a", "target")), "<a href=\"target\">a</a>");
		testLink(List.of(new LinkParser.Part("a", "target")), "<A hReF=\"target\">a</a>");

		testLink(List.of(new LinkParser.Part("hello", null),
				new LinkParser.Part("", ""),
				new LinkParser.Part("world", null)), "hello<a></a>world");
		testLink(List.of(new LinkParser.Part(" ", null),
				new LinkParser.Part("a", ""),
				new LinkParser.Part("\n", null)), " <a>a</a>\n");
		testLink(List.of(new LinkParser.Part("s", null),
				new LinkParser.Part("u", ""),
				new LinkParser.Part("n", null)), "s<a href=\"\">u</a>n");
	}

	@Test
	public void testEdgeCases() {
		testLink(List.of(), "<a>a</ab>");
		testLink(List.of(new LinkParser.Part("b", null),
				new LinkParser.Part("c", null)), "b<a>a</ab>c");
		testLink(List.of(), "<a>a</b>");
		testLink(List.of(new LinkParser.Part("b", null)), "b<a></a");
		testLink(List.of(new LinkParser.Part("b", null)), "b<a></");
		testLink(List.of(new LinkParser.Part("b", null)), "b<a><");
		testLink(List.of(new LinkParser.Part("b", null)), "b<a>");
		testLink(List.of(new LinkParser.Part("b", null)), "b<a");
		testLink(List.of(new LinkParser.Part("b", null)), "b<");
		testLink(List.of(new LinkParser.Part("b", null),
				new LinkParser.Part("a", null),
				new LinkParser.Part("c", null)), "b<ahref=\"\">a</a>c");
		testLink(List.of(new LinkParser.Part("b", null)), "b<a href=\">c");
		testLink(List.of(new LinkParser.Part("b", null),
				new LinkParser.Part("c", null)), "b<a href=>c");
		testLink(List.of(new LinkParser.Part("b", null),
				new LinkParser.Part("c", null)), "b<a href>c");
		testLink(List.of(new LinkParser.Part("b", null),
				new LinkParser.Part("c", null)), "b<a hre>c");
		testLink(List.of(new LinkParser.Part("b", null),
				new LinkParser.Part("c", null)), "b<a hr>c");
		testLink(List.of(new LinkParser.Part("b", null),
				new LinkParser.Part("c", null)), "b<a h>c");
		testLink(List.of(new LinkParser.Part("b", null),
				new LinkParser.Part("c", null)), "b<a >c");
		testLink(List.of(new LinkParser.Part("b", null)), "b<a c");
	}

	private void testLink(List<LinkParser.Part> expected, String text) {
		final List<LinkParser.Part> actual = LinkParser.parse(text);
		Assert.assertEquals(expected, actual);
	}
}
