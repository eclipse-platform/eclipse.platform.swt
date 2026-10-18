// SPDX-FileCopyrightText: 2026 Hitesh Soliwal and Contributors to the Synexia Project
// SPDX-License-Identifier: Apache-2.0
package com.synexia.rewrite;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;
import org.openrewrite.config.Environment;
import org.openrewrite.config.YamlResourceLoader;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.ScanningRecipe;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.text.PlainText;

/** Task qualification using the existing canonical recipes and real SDK scheduler. */
public final class M3TaskRecipeTest {
    private static final String CRATE = System.getProperty("m3.crate.name");
    private static final String NOTE = "unrelated/recipe-decoys.txt";
    private static final String DECOYS = "predicate.and(next)\n\\Qalpha.*omega\\E\n"
            + "class Decoy { String s = \"source-looking text\"; }\n";
    private record Target(String path, String beforeHash, String afterHash,
                          String before, String after) { }
    private record Lane(String kind, List<Target> targets) { }
    private record Replay(Map<String, String> output, String diff, int changes) { }

    private static List<Lane> lanes() {
        List<Lane> lanes = new ArrayList<>();
        for (String kind : List.of("java", "text")) {
            String root = "/com/synexia/rewrite/hash-pinned-" + kind + "/" + CRATE + "/";
            String manifest = resource(root + "manifest.tsv", true);
            if (manifest == null) continue;
            List<Target> targets = new ArrayList<>();
            String previous = "";
            for (String line : manifest.lines().toList()) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] cells = line.split("\t", -1);
                assertEquals(4, cells.length);
                assertTrue(previous.compareTo(cells[0]) < 0, "sorted unique target paths");
                String before = cells[1].equals("ABSENT") ? null
                        : resource(root + "pre/" + cells[3], false);
                String after = resource(root + cells[3], false);
                if (before != null) assertEquals(cells[1], sha(before), cells[0]);
                assertEquals(cells[2], sha(after), cells[0]);
                targets.add(new Target(cells[0], cells[1], cells[2], before, after));
                previous = cells[0];
            }
            assertFalse(targets.isEmpty());
            lanes.add(new Lane(kind, List.copyOf(targets)));
        }
        assertFalse(lanes.isEmpty(), "a real recipe target is mandatory");
        return List.copyOf(lanes);
    }

    private static ScanningRecipe<?> recipe(Lane lane) {
        return lane.kind.equals("java") ? new M3HashPinnedJavaSnapshotRecipe(CRATE)
                : new M3HashPinnedTextSnapshotRecipe(CRATE);
    }

    private static Map<String, String> state(Lane lane, boolean after) {
        Map<String, String> sources = new TreeMap<>();
        for (Target t : lane.targets) {
            String value = after ? t.after : t.before;
            if (value != null) sources.put(t.path, value);
        }
        sources.put(NOTE, DECOYS);
        return sources;
    }

    private static List<SourceFile> parse(Lane lane, Map<String, String> sources) {
        List<SourceFile> parsed = new ArrayList<>();
        List<Parser.Input> java = new ArrayList<>();
        sources.forEach((path, source) -> {
            if (lane.kind.equals("java") && !path.equals(NOTE)) {
                java.add(Parser.Input.fromString(Path.of(path), source));
            } else parsed.add(PlainText.builder().sourcePath(Path.of(path)).text(source).build());
        });
        if (!java.isEmpty()) {
            List<Throwable> errors = new ArrayList<>();
            List<SourceFile> units = JavaParser.fromJavaVersion().build()
                    .parseInputs(java, null, new InMemoryExecutionContext(errors::add)).toList();
            assertTrue(errors.isEmpty(), () -> "Java parser errors: " + errors);
            assertEquals(java.size(), units.size());
            for (SourceFile unit : units) {
                assertInstanceOf(J.CompilationUnit.class, unit);
                assertEquals(sources.get(path(unit)), unit.printAll(), "lossless Java LST");
                parsed.add(unit);
            }
        }
        parsed.sort(Comparator.comparing(M3TaskRecipeTest::path));
        return parsed;
    }

    private static Replay replay(Lane lane, List<SourceFile> sources) {
        Map<String, String> before = rendered(sources);
        List<Throwable> errors = new ArrayList<>();
        List<Result> results = recipe(lane).run(new InMemoryLargeSourceSet(sources),
                new InMemoryExecutionContext(errors::add), 1).getChangeset().getAllResults();
        assertTrue(errors.isEmpty(), () -> "Recipe errors: " + errors);
        Map<String, String> actual = new TreeMap<>(before);
        StringBuilder patch = new StringBuilder();
        for (Result result : results.stream()
                .sorted(Comparator.comparing(r -> path(r.getAfter()))).toList()) {
            SourceFile after = result.getAfter();
            assertNotNull(after, "no source deletions");
            assertNotEquals(NOTE, path(after));
            assertTrue(lane.targets.stream().anyMatch(t -> t.path.equals(path(after))));
            if (lane.kind.equals("java")) assertInstanceOf(J.CompilationUnit.class, after);
            if (result.getBefore() != null) {
                assertEquals(path(result.getBefore()), path(after));
                assertEquals(result.getBefore().getId(), after.getId());
            }
            actual.put(path(after), after.printAll());
            patch.append(result.diff());
        }
        assertEquals(before, rendered(sources), "input source objects remain immutable");
        assertEquals(state(lane, true), actual);
        return new Replay(Map.copyOf(actual), patch.toString(), results.size());
    }

    @Test void exactReplayFixedPointAndInputOrder() {
        namedDescriptorComposesTheQualifiedLanes();
        for (Lane lane : lanes()) {
            List<SourceFile> initial = parse(lane, state(lane, false));
            Replay result = replay(lane, initial);
            assertEquals(lane.targets.stream().filter(t -> !t.beforeHash.equals(t.afterHash)).count(), result.changes);
            Replay fixed = replay(lane, parse(lane, result.output));
            assertEquals(0, fixed.changes);
            assertEquals("", fixed.diff);
            Collections.reverse(initial);
            assertEquals(result.output, replay(lane, initial).output);
        }
    }

    private static void namedDescriptorComposesTheQualifiedLanes() {
        String resource = "/META-INF/rewrite/" + CRATE + ".yml";
        try (var stream = M3TaskRecipeTest.class.getResourceAsStream(resource)) {
            assertNotNull(stream, "named recipe descriptor");
            Recipe named = Environment.builder().load(new YamlResourceLoader(stream,
                    URI.create("classpath:" + resource), new Properties())).build()
                    .activateRecipes(System.getProperty("m3.recipe.name"));
            assertTrue(named.validateAll().stream().allMatch(v -> v.isValid()),
                    () -> "named recipe validation: " + named.validateAll());
            Map<String, SourceFile> sourceMap = new TreeMap<>();
            Map<String, String> expected = new TreeMap<>();
            for (Lane lane : lanes()) {
                for (SourceFile source : parse(lane, state(lane, false)))
                    sourceMap.put(path(source), source);
                expected.putAll(state(lane, true));
            }
            List<Throwable> errors = new ArrayList<>();
            List<SourceFile> sources = new ArrayList<>(sourceMap.values());
            List<Result> results = named.run(new InMemoryLargeSourceSet(sources),
                    new InMemoryExecutionContext(errors::add), 1).getChangeset().getAllResults();
            assertTrue(errors.isEmpty(), () -> "named recipe errors: " + errors);
            Map<String, String> actual = rendered(sources);
            for (Result result : results) {
                assertNotNull(result.getAfter(), "named recipe never deletes source");
                actual.put(path(result.getAfter()), result.getAfter().printAll());
            }
            assertEquals(expected, actual, "named YAML composes both qualified lanes");
        } catch (IOException failure) { throw new IllegalStateException(failure); }
    }

    @Test void mixedPreimagesAndPostimages() {
        for (Lane lane : lanes()) {
            // Exercise each file independently against already-converged sibling owners.
            for (Target target : lane.targets) {
                Map<String, String> input = state(lane, true);
                if (target.before == null) input.remove(target.path);
                else input.put(target.path, target.before);
                Replay result = replay(lane, parse(lane, input));
                assertEquals(target.beforeHash.equals(target.afterHash) ? 0 : 1, result.changes);
            }
        }
    }

    @Test void driftMissingDuplicateAndWrongRepresentationRefuse() {
        for (Lane lane : lanes()) {
            List<SourceFile> initial = parse(lane, state(lane, false));
            for (Target target : lane.targets) {
                Map<String, String> drift = state(lane, false);
                drift.put(target.path, target.after + "\n// source drift\n");
                refused(lane, parse(lane, drift));
                if (target.before != null) {
                    List<SourceFile> missing = without(initial, target.path);
                    refused(lane, missing);
                }
                List<SourceFile> duplicate = new ArrayList<>(without(initial, target.path));
                SourceFile post = parse(lane, Map.of(target.path, target.after)).getFirst();
                duplicate.add(post);
                duplicate.add(post.withId(java.util.UUID.randomUUID()));
                refused(lane, duplicate);
                if (lane.kind.equals("java")) {
                    List<SourceFile> wrong = new ArrayList<>(without(initial, target.path));
                    wrong.add(PlainText.builder().sourcePath(Path.of(target.path))
                            .text(target.before == null ? target.after : target.before).build());
                    refused(lane, wrong);
                }
            }
        }
    }

    @Test void postScanChangesRefuse() {
        for (Lane lane : lanes()) postScan(lane, recipe(lane));
    }

    private static <A> void postScan(Lane lane, ScanningRecipe<A> recipe) {
        for (Target target : lane.targets) {
            ExecutionContext context = new InMemoryExecutionContext(error -> { throw new AssertionError(error); });
            A inventory = recipe.getInitialValue(context);
            for (SourceFile source : parse(lane, state(lane, false)))
                recipe.getScanner(inventory).visit(source, context);
            SourceFile drift = parse(lane, Map.of(target.path,
                    target.after + "\n// change after scan\n")).getFirst();
            assertThrows(RuntimeException.class, () -> recipe.getVisitor(inventory).visit(drift, context));
        }
    }

    @Test void qualifiedSdkResultsAreRetained() throws IOException {
        Path root = Path.of(System.getProperty("m3.candidate.root"));
        StringBuilder receipt = new StringBuilder("# OpenRewrite SDK Results; no template-copy materialization\n");
        for (Lane lane : lanes()) {
            Replay result = replay(lane, parse(lane, state(lane, false)));
            assertEquals(0, replay(lane, parse(lane, result.output)).changes);
            for (Target target : lane.targets) {
                Path output = root.resolve(target.path);
                assertTrue(output.normalize().startsWith(root.normalize()));
                Files.createDirectories(output.getParent());
                String text = result.output.get(target.path);
                assertEquals(target.afterHash, sha(text));
                Files.writeString(output, text, StandardCharsets.UTF_8);
                receipt.append(target.path).append('\t').append(target.beforeHash)
                        .append('\t').append(target.afterHash).append('\n');
            }
            Files.createDirectories(root);
            Files.writeString(root.resolve(lane.kind + ".patch"), result.diff, StandardCharsets.UTF_8);
        }
        Files.writeString(root.resolve("SDK_RESULTS.tsv"), receipt, StandardCharsets.UTF_8);
        StringBuilder inputs = new StringBuilder("# Recipe inputs used by this SDK execution\n");
        Path crate = Path.of(System.getProperty("m3.crate.root"));
        List<Path> boundFiles = new ArrayList<>();
        boundFiles.add(crate.resolve("pom.xml"));
        boundFiles.add(crate.resolve("receive.py"));
        for (String domain : List.of("src", "contract")) {
            Path directory = crate.resolve(domain);
            if (!Files.exists(directory)) continue;
            try (var paths = Files.walk(directory)) {
                boundFiles.addAll(paths.filter(Files::isRegularFile).toList());
            }
        }
        for (Path source : boundFiles.stream().sorted().toList()) {
            inputs.append("crate/").append(crate.relativize(source).toString().replace('\\', '/'))
                    .append('\t').append(sha(Files.readString(source))).append('\n');
        }
        Path owner = Path.of(System.getProperty("m3.recipe.owner.root"));
        for (String name : List.of("M3EditScope.java", "M3HashPinnedJavaSnapshotRecipe.java",
                "M3HashPinnedTextSnapshotRecipe.java", "M3ScopeInference.java")) {
            String relative = "src/main/java/com/synexia/rewrite/" + name;
            inputs.append("owner/").append(relative).append('\t')
                    .append(sha(Files.readString(owner.resolve(relative)))).append('\n');
        }
        Files.writeString(root.resolve("SDK_INPUTS.tsv"), inputs, StandardCharsets.UTF_8);
    }

    private static void refused(Lane lane, List<SourceFile> input) {
        List<String> before = input.stream().map(SourceFile::printAll).toList();
        List<Throwable> errors = new ArrayList<>();
        InMemoryLargeSourceSet sources = new InMemoryLargeSourceSet(input);
        RuntimeException failure = assertThrows(RuntimeException.class, () -> recipe(lane)
                .run(sources, new InMemoryExecutionContext(errors::add), 1));
        assertNotNull(failure);
        assertTrue(sources.getChangeset().getAllResults().isEmpty(), "no partial publication");
        assertEquals(before, input.stream().map(SourceFile::printAll).toList());
    }

    private static List<SourceFile> without(List<SourceFile> input, String omitted) {
        return input.stream().filter(s -> !path(s).equals(omitted)).toList();
    }
    private static Map<String, String> rendered(List<SourceFile> sources) {
        Map<String, String> result = new TreeMap<>();
        for (SourceFile source : sources) assertNull(result.put(path(source), source.printAll()));
        return result;
    }
    private static String path(SourceFile source) {
        return source.getSourcePath().toString().replace('\\', '/');
    }
    private static String resource(String name, boolean optional) {
        try (var stream = M3TaskRecipeTest.class.getResourceAsStream(name)) {
            if (stream == null && optional) return null;
            assertNotNull(stream, name);
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException failure) { throw new IllegalStateException(failure); }
    }
    private static String sha(String text) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(text.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException impossible) { throw new ExceptionInInitializerError(impossible); }
    }
}
