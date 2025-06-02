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
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Parses link elements.
 */
public class LinkParser {

	public static List<Part> parse(String text) {
		final LinkParser parser = new LinkParser(text);
		parser.parse();
		return parser.parts;
	}

	private final List<Part> parts = new ArrayList<>();
	private final String text;

	private int next;
	private int index;

	private LinkParser(String text) {
		this.text = text;

		next = next();
	}

	private void parse() {
		if (next < 0) {
			return;
		}

		final StringBuilder buffer = new StringBuilder();
		while (next >= 0) {
			if (isConsume('<')) {
				handleNonEmptyText(buffer);
				final String target = getTarget();
				if (target == null) {
					continue;
				}

				while (true) {
					if (next < 0) {
						return;
					}
					if (isConsume('<')) {
						if (isConsume('/')
								&& isConsumeIgnoreCase('a')
								&& isConsume('>')) {
							parts.add(new Part(buffer.toString(), target));
						} else {
							skipAllUntilTagClose();
						}

						buffer.setLength(0);
						break;
					}
					buffer.append((char) next);
					consume();
				}
			} else {
				buffer.append((char) next);
				consume();
			}
		}
		handleNonEmptyText(buffer);
	}

	private void handleNonEmptyText(StringBuilder buffer) {
		if (buffer.length() > 0) {
			parts.add(new Part(buffer.toString(), null));
			buffer.setLength(0);
		}
	}

	private String getTarget() {
		if (!isConsumeIgnoreCase('a')) {
			skipAllUntilTagClose();
			return null;
		}

		if (isConsume('>')) {
			return "";
		}

		if (!isConsumeWhitespace()) {
			skipAllUntilTagClose();
			return null;
		}

		while (isConsumeWhitespace()) {
			if (next < 0) {
				return null;
			}
		}

		if (!isConsumeIgnoreCase('h')
				|| !isConsumeIgnoreCase('r')
				|| !isConsumeIgnoreCase('e')
				|| !isConsumeIgnoreCase('f')
				|| !isConsume('=')
				|| !isConsume('"')) {
			skipAllUntilTagClose();
			return null;
		}

		final StringBuilder target = new StringBuilder();
		while (!isConsume('"')) {
			if (next < 0) {
				return null;
			}
			target.append((char) next);
			consume();
		}

		if (!skipAllUntilTagClose()) {
			return null;
		}

		return target.toString();
	}

	private boolean skipAllUntilTagClose() {
		while (!isConsume('>')) {
			if (next < 0) {
				return false;
			}
			consume();
		}
		return true;
	}

	private boolean isConsume(char expected) {
		if (next != expected) {
			return false;
		}
		consume();
		return true;
	}

	private boolean isConsumeIgnoreCase(char expected) {
		if (Character.toLowerCase(next) != expected) {
			return false;
		}
		consume();
		return true;
	}

	private boolean isConsumeWhitespace() {
		if (next < 0 || !Character.isWhitespace(next)) {
			return false;
		}
		consume();
		return true;
	}

	private void consume() {
		next = next();
	}

	private int next() {
		if (index >= text.length()) {
			return -1;
		}

		final char c = text.charAt(index);
		index++;
		return c;
	}

	public record Part(String text, String linkData) {
		public Part {
			if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
	}
}
