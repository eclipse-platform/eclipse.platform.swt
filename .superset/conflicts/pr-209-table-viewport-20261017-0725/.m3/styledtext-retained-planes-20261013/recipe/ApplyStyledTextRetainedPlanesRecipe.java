/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;

import org.openrewrite.*;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaParser;

import com.synexia.rewrite.M3HashPinnedJavaSnapshotRecipe;

/**
 * Executes the canonical {@code M3HashPinnedJavaSnapshotRecipe} with the
 * {@code swt-styledtext-retained-planes-20261013} crate and proves:
 * exact postimages from the sealed preimages, a second-run fixed point, and
 * refusal of source drift and of a missing required source.
 *
 * <p>Usage: {@code ApplyStyledTextRetainedPlanesRecipe <preimageRoot>}; preimageRoot holds the
 * base commit's LF bytes at their repository paths. Prints a JSON receipt.</p>
 */
public final class ApplyStyledTextRetainedPlanesRecipe {
	static final String CRATE = "swt-styledtext-retained-planes-20261013";

	public static void main(String[] args) throws Exception {
		Path preimages = Path.of(args[0]);
		M3HashPinnedJavaSnapshotRecipe recipe = new M3HashPinnedJavaSnapshotRecipe(CRATE);
		Map<String, String> before = recipe.preimageManifest();
		Map<String, String> templates = templates();
		List<String> checks = new ArrayList<>();

		Map<String, String> inputs = new TreeMap<>();
		for (Map.Entry<String, String> target : before.entrySet()) {
			if (!"ABSENT".equals(target.getValue())) {
				inputs.put(target.getKey(), Files.readString(preimages.resolve(target.getKey()), StandardCharsets.UTF_8));
			}
		}
		Map<String, String> first = run(recipe, inputs);
		require(first.equals(templates), "first run must produce exactly the sealed postimages");
		checks.add("exact-postimages:" + first.size());

		Map<String, String> second = run(new M3HashPinnedJavaSnapshotRecipe(CRATE), templates);
		require(second.isEmpty(), "second run must be a fixed point, changed " + second.keySet());
		checks.add("fixed-point");

		Map<String, String> drifted = new TreeMap<>(inputs);
		String driftPath = drifted.keySet().iterator().next();
		drifted.put(driftPath, drifted.get(driftPath) + "// drift\n");
		requireRefusal(drifted, "source drift");
		checks.add("drift-refused");

		Map<String, String> missing = new TreeMap<>(inputs);
		missing.remove(driftPath);
		requireRefusal(missing, "required source missing");
		checks.add("missing-source-refused");

		StringBuilder receipt = new StringBuilder("{\"crate\":\"" + CRATE + "\",\"rewrite\":\""
				+ Recipe.class.getPackage().getImplementationVersion() + "\",\"targets\":{");
		int i = 0;
		for (Map.Entry<String, String> target : templates.entrySet()) {
			receipt.append(i++ == 0 ? "" : ",").append('"').append(target.getKey()).append("\":{\"before\":\"")
					.append(before.get(target.getKey())).append("\",\"after\":\"").append(sha256(target.getValue())).append("\"}");
		}
		receipt.append("},\"checks\":").append(json(checks)).append(",\"status\":\"PASS\"}");
		System.out.println(receipt);
	}

	/** Runs the recipe on the given sources; returns path -> printed text of every changed or generated file. */
	static Map<String, String> run(Recipe recipe, Map<String, String> sources) {
		ExecutionContext context = new InMemoryExecutionContext(failure -> {
			throw failure instanceof RuntimeException runtime ? runtime : new IllegalStateException(failure);
		});
		List<Parser.Input> inputs = new ArrayList<>();
		sources.forEach((path, text) -> inputs.add(Parser.Input.fromString(Path.of(path), text)));
		List<SourceFile> parsed = JavaParser.fromJavaVersion().build().parseInputs(inputs, null, context).toList();
		for (SourceFile file : parsed) {
			String path = file.getSourcePath().toString().replace('\\', '/');
			require(file.printAll().equals(sources.get(path)), "lossless parse " + path);
		}
		RecipeRun run = recipe.run(new InMemoryLargeSourceSet(parsed), context);
		Map<String, String> changed = new TreeMap<>();
		for (Result result : run.getChangeset().getAllResults()) {
			SourceFile after = result.getAfter();
			if (after != null) {
				changed.put(after.getSourcePath().toString().replace('\\', '/'), after.printAll());
			}
		}
		return changed;
	}

	static void requireRefusal(Map<String, String> sources, String reason) {
		try {
			run(new M3HashPinnedJavaSnapshotRecipe(CRATE), sources);
		} catch (RuntimeException e) {
			for (Throwable t = e; t != null; t = t.getCause()) {
				if (String.valueOf(t.getMessage()).contains(reason)) {
					return;
				}
			}
			throw new IllegalStateException("refused for the wrong reason, expected " + reason, e);
		}
		throw new IllegalStateException("not refused: " + reason);
	}

	static Map<String, String> templates() throws Exception {
		String root = "/com/synexia/rewrite/hash-pinned-java/" + CRATE + "/";
		Map<String, String> result = new TreeMap<>();
		String manifest = new String(ApplyStyledTextRetainedPlanesRecipe.class.getResourceAsStream(root + "manifest.tsv").readAllBytes(), StandardCharsets.UTF_8);
		for (String line : manifest.lines().toList()) {
			String[] cells = line.split("\t", -1);
			result.put(cells[0], new String(ApplyStyledTextRetainedPlanesRecipe.class.getResourceAsStream(root + cells[3]).readAllBytes(), StandardCharsets.UTF_8));
		}
		return result;
	}

	static String sha256(String text) throws Exception {
		return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8)));
	}

	static String json(List<String> values) {
		StringJoiner joiner = new StringJoiner("\",\"", "[\"", "\"]");
		values.forEach(joiner::add);
		return joiner.toString();
	}

	static void require(boolean condition, String message) {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}
}
