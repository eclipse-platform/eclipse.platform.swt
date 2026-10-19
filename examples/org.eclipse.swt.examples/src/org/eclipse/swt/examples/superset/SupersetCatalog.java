/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Loads the two packaged catalog resources: the concept catalog
 * ({@code superset-catalog.tsv}) and the demo table ({@code superset-demos.tsv}).
 * Order is file order, which keeps tab order and matrix order stable.
 */
public final class SupersetCatalog {

	/** Category tab order, matching the catalog's category column. */
	public static final String[] CATEGORIES = {"data_view", "text", "selection", "temporal", "input",
			"display", "feedback", "navigation", "container", "chart", "not_applicable"};

	private final Map<String, ConceptInfo> concepts = new LinkedHashMap<>();
	private final List<DemoSpec> demos = new ArrayList<>();

	public SupersetCatalog() {
		read("superset-catalog.tsv", ConceptInfo::parse).forEach(c -> concepts.put(c.concept(), c));
		demos.addAll(read("superset-demos.tsv", DemoSpec::parse));
	}

	private static <T> List<T> read(String resource, Function<String, T> parser) {
		List<T> out = new ArrayList<>();
		try (InputStream in = SupersetCatalog.class.getResourceAsStream(resource);
				BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			r.readLine();
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				if (!line.isBlank()) out.add(parser.apply(line));
			}
		} catch (IOException e) {
			throw new UncheckedIOException(resource, e);
		}
		return out;
	}

	public ConceptInfo concept(String name) {
		return concepts.get(name);
	}

	public List<ConceptInfo> concepts(String category) {
		return concepts.values().stream().filter(c -> c.category().equals(category)).toList();
	}

	public List<DemoSpec> demos() {
		return demos;
	}

	public List<DemoSpec> demos(String concept) {
		return demos.stream().filter(d -> d.concept().equals(concept)).toList();
	}

	/** @return the demo for concept + widget class, or null */
	public DemoSpec find(String concept, String widgetClass) {
		return demos.stream().filter(d -> d.concept().equals(concept) && d.widgetClass().equals(widgetClass))
				.findFirst().orElse(null);
	}

	public int conceptCount() {
		return concepts.size();
	}
}
