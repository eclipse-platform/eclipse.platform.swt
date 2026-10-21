// SPDX-License-Identifier: Apache-2.0
package com.synexia.rewrite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.openrewrite.DelegatingExecutionContext;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Parser;
import org.openrewrite.ScanningRecipe;
import org.openrewrite.SourceFile;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;

/**
 * Reusable, candidate-only Java migration from an exact source snapshot to reviewed Java LSTs.
 *
 * <p>Each crate supplies a manifest and UTF-8 templates as classpath resources. All target
 * preimages and all output templates are admitted before any output is produced. Hashes cover
 * rendered Java text encoded as UTF-8, not the original on-disk byte encoding. Replacements
 * preserve the input charset and BOM flag and discard its obsolete checksum. This recipe checks source identity and
 * syntax; the sealed M3 contract, coverage, compiler, test and runtime gates remain authoritative.
 * Optional hash-pinned parse contexts supply bounded co-input source declarations without
 * entering the generated or replacement target set. An optional bounded parser-dependencies
 * resource selects source-only attribution context and refuses parser callbacks before candidate
 * publication. Both protocols may be combined when their source identities are disjoint.
 */
public final class M3HashPinnedJavaSnapshotRecipe
        extends ScanningRecipe<M3HashPinnedJavaSnapshotRecipe.Inventory> {
    private static final String RESOURCE_ROOT = "/com/synexia/rewrite/hash-pinned-java/";
    // An explicit host classpath overrides parser defaults; an empty list remains explicit.
    private static final String PARSER_CLASSPATH =
            "com.synexia.rewrite.hashPinnedJavaSnapshotRecipe.parserClasspath";
    private static final int MAX_PARSE_CONTEXT_SOURCES = 64;
    private static final int MAX_PARSE_CONTEXT_UTF8_BYTES = 8 * 1024 * 1024;
    private static final int MAX_PARSER_DEPENDENCIES = 16;
    private static final int MAX_PARSER_DEPENDENCY_MANIFEST_BYTES = 65_536;
    private static final int MAX_PARSER_DEPENDENCY_BYTES = 262_144;

    @Option(
            displayName = "Crate name",
            description = "Exact classpath crate directory below hash-pinned-java.",
            example = "sealed-readonly-requirement")
    private final String crateName;

    private record Target(String path, String before, String after, String text) { }

    private record ContextSource(String path, String hash, String text) { }

    private record ParserDependency(String path, String text) { }

    public static final class Inventory {
        private final List<Target> targets;
        private final List<ContextSource> contextSources;
        private final List<ParserDependency> parserDependencies;
        private final Map<String, String> seen = new HashMap<>();
        private final List<String> conflicts = new ArrayList<>();
        private Map<String, SourceFile> candidates;

        private Inventory(List<Target> targets, List<ContextSource> contextSources,
                List<ParserDependency> parserDependencies) {
            this.targets = List.copyOf(targets);
            this.contextSources = List.copyOf(contextSources);
            this.parserDependencies = List.copyOf(parserDependencies);
        }
    }

    @JsonCreator
    public M3HashPinnedJavaSnapshotRecipe(@JsonProperty("crateName") String crateName) {
        if (crateName == null || !crateName.matches("[a-z0-9][a-z0-9-]{0,79}")) {
            throw new IllegalArgumentException("invalid hash-pinned Java crate");
        }
        this.crateName = crateName;
    }

    @Override public String getDisplayName() {
        return "M3 hash-pinned Java snapshot candidate";
    }

    @Override public String getDescription() {
        return "Reconstructs reviewed Java LST candidates only when every source matches "
                + "the crate's exact SHA-256 preimage or already matches its SHA-256 output.";
    }

    @Override public Set<String> getTags() {
        return Set.of("synexia", "m3", "openrewrite", "recipe-first", "candidate-only");
    }

    @Override public int maxCycles() { return 1; }

    @Override public Inventory getInitialValue(ExecutionContext context) {
        String manifest = resource(RESOURCE_ROOT + crateName + "/manifest.tsv");
        List<Target> targets = targets(manifest);
        List<ContextSource> contexts = contextSources(targets, manifest);
        List<ParserDependency> dependencies = parserDependencies(targets);
        requireDisjointParserContexts(targets, contexts, dependencies);
        return new Inventory(targets, contexts, dependencies);
    }

    @Override public TreeVisitor<?, ExecutionContext> getScanner(Inventory inventory) {
        return new TreeVisitor<Tree, ExecutionContext>() {
            @Override public Tree preVisit(Tree tree, ExecutionContext context) {
                if (tree instanceof SourceFile file) {
                    stopAfterPreVisit();
                    String path = normalized(file.getSourcePath());
                    for (Target target : inventory.targets) {
                        if (!target.path().equals(path)) continue;
                        String hash = sha256(file.printAll());
                        synchronized (inventory) {
                            if (!(file instanceof J.CompilationUnit)) {
                                inventory.conflicts.add("target is not a Java compilation unit: " + path);
                            }
                            if (inventory.seen.putIfAbsent(path, hash) != null) {
                                inventory.conflicts.add("duplicate target: " + path);
                            }
                            if (!hash.equals(target.before()) && !hash.equals(target.after())) {
                                inventory.conflicts.add("source drift: " + path);
                            }
                        }
                    }
                }
                return tree;
            }
        };
    }

    @Override public Collection<? extends SourceFile> generate(
            Inventory inventory, ExecutionContext context) {
        prepare(inventory, context);
        List<SourceFile> generated = new ArrayList<>();
        for (Target target : inventory.targets) {
            if (!inventory.seen.containsKey(target.path())) {
                generated.add(inventory.candidates.get(target.path()));
            }
        }
        return generated;
    }

    @Override public TreeVisitor<?, ExecutionContext> getVisitor(Inventory inventory) {
        requireAdmissible(inventory);
        return new TreeVisitor<Tree, ExecutionContext>() {
            @Override public Tree preVisit(Tree tree, ExecutionContext context) {
                if (tree instanceof SourceFile file) {
                    prepare(inventory, context);
                    stopAfterPreVisit();
                    String path = normalized(file.getSourcePath());
                    for (Target target : inventory.targets) {
                        if (target.path().equals(path)) {
                            String current = sha256(file.printAll());
                            String scanned = inventory.seen.get(path);
                            boolean matchesManifest = current.equals(target.before())
                                    || current.equals(target.after());
                            boolean matchesScan = scanned == null
                                    ? target.before().equals("ABSENT")
                                            && current.equals(target.after())
                                    : current.equals(scanned);
                            if (!(file instanceof J.CompilationUnit)
                                    || !matchesManifest || !matchesScan) {
                                throw new IllegalStateException("target changed after scan: " + path);
                            }
                            if (current.equals(target.after())) return tree;
                            SourceFile parsed = inventory.candidates.get(target.path());
                            SourceFile replacement = parsed.withId(file.getId());
                            replacement = replacement.withSourcePath(file.getSourcePath());
                            replacement = replacement.withMarkers(file.getMarkers());
                            replacement = replacement.withFileAttributes(file.getFileAttributes());
                            replacement = replacement.withCharset(file.getCharset());
                            replacement = replacement.withCharsetBomMarked(file.isCharsetBomMarked());
                            return replacement.withChecksum(null);
                        }
                    }
                }
                return tree;
            }
        };
    }

    public String getCrateName() { return crateName; }

    /** Exact, sorted target paths sealed by this recipe crate. */
    public List<String> targetPaths() {
        return targets().stream().map(Target::path).toList();
    }

    /** Narrowest physical edit scope implied by the crate's exact Java target set. */
    public M3EditScope requiredScope() {
        return M3ScopeInference.inferJavaTargets(targetPaths());
    }

    /** Exact path -> expected preimage SHA-256 (or ABSENT for additive targets). */
    public Map<String, String> preimageManifest() {
        Map<String, String> result = new java.util.LinkedHashMap<>();
        for (Target target : targets()) {
            result.put(target.path(), target.before());
        }
        return Map.copyOf(result);
    }

    private static void prepare(Inventory inventory, ExecutionContext context) {
        synchronized (inventory) {
            requireAdmissible(inventory);
            if (inventory.candidates == null) {
                List<Path> parserClasspath = parserClasspath(context);
                validateParserDependencies(inventory.parserDependencies, context, parserClasspath);
                Map<String, SourceFile> prepared = new HashMap<>();
                for (Target target : inventory.targets) {
                    prepared.put(target.path(), parse(target, inventory.contextSources,
                            inventory.parserDependencies, context, parserClasspath));
                }
                inventory.candidates = Map.copyOf(prepared);
            }
        }
    }

    private static void requireAdmissible(Inventory inventory) {
        synchronized (inventory) {
            for (Target target : inventory.targets) {
                if (!target.before().equals("ABSENT")
                        && !inventory.seen.containsKey(target.path())) {
                    throw new IllegalStateException("required source missing: " + target.path());
                }
            }
            if (!inventory.conflicts.isEmpty()) {
                throw new IllegalStateException(String.join("; ", inventory.conflicts));
            }
        }
    }

    /** Null means no host override; an empty list explicitly excludes the runtime classpath. */
    private static List<Path> parserClasspath(ExecutionContext context) {
        Object configured = context.getMessage(PARSER_CLASSPATH);
        if (configured == null) return null;
        if (!(configured instanceof List<?> values)) {
            throw new IllegalArgumentException("hash-pinned Java parser classpath must be a list of paths");
        }
        List<Path> classpath = new ArrayList<>(values.size());
        for (Object value : values) {
            if (!(value instanceof Path path)) {
                throw new IllegalArgumentException("hash-pinned Java parser classpath must be a list of paths");
            }
            classpath.add(path);
        }
        return List.copyOf(classpath);
    }

    private static JavaParser.Builder<?, ?> parserBuilder(
            List<Path> parserClasspath, boolean runtimeFallback) {
        JavaParser.Builder<?, ?> builder = JavaParser.fromJavaVersion();
        if (parserClasspath != null) builder.classpath(parserClasspath);
        else if (runtimeFallback) builder.classpath(JavaParser.runtimeClasspath());
        return builder;
    }

    private static SourceFile parse(
            Target target, List<ContextSource> contextSources,
            List<ParserDependency> dependencies, ExecutionContext context,
            List<Path> parserClasspath) {
        List<Parser.Input> inputs = new ArrayList<>(contextSources.size() + 1);
        Map<String, String> expected = new HashMap<>();
        for (ContextSource source : contextSources) {
            inputs.add(Parser.Input.fromString(Path.of(source.path()), source.text()));
            expected.put(source.path(), source.text());
        }
        inputs.add(Parser.Input.fromString(Path.of(target.path()), target.text()));
        expected.put(target.path(), target.text());
        JavaParser.Builder<?, ?> builder = parserBuilder(parserClasspath, dependencies.isEmpty());
        if (!dependencies.isEmpty()) builder.dependsOn(dependencyInputs(dependencies));
        String failureMessage = dependencies.isEmpty()
                ? "Java template parse error: " + target.path()
                : "Java parser failed while preparing " + target.path();
        List<SourceFile> parsed = parseChecked(builder.build(), inputs, context, failureMessage);
        if (parsed.size() != expected.size()) {
            throw new IllegalStateException("Java template parse/format drift: " + target.path());
        }
        Set<String> seen = new HashSet<>();
        SourceFile selected = null;
        for (SourceFile source : parsed) {
            String path = normalized(source.getSourcePath());
            String expectedText = expected.get(path);
            if (!(source instanceof J.CompilationUnit) || expectedText == null
                    || !seen.add(path) || !expectedText.equals(source.printAll())) {
                throw new IllegalStateException("Java template parse/format drift: " + target.path());
            }
            if (target.path().equals(path)) selected = source;
        }
        if (selected == null || seen.size() != expected.size()) {
            throw new IllegalStateException("Java template parse/format drift: " + target.path());
        }
        return selected;
    }

    private static void requireDisjointParserContexts(
            List<Target> targets, List<ContextSource> contexts,
            List<ParserDependency> dependencies) {
        if (contexts.isEmpty() || dependencies.isEmpty()) return;
        Set<String> targetTypes = new HashSet<>();
        for (Target target : targets) targetTypes.add(javaIdentity(target.path()));
        Set<String> contextPaths = new HashSet<>();
        Set<String> contextTypes = new HashSet<>();
        for (ContextSource source : contexts) {
            String identity = javaIdentity(source.path());
            if (targetTypes.contains(identity)) {
                throw new IllegalStateException(
                        "Java parse context type collides with target: " + source.path());
            }
            contextPaths.add(source.path());
            contextTypes.add(identity);
        }
        for (ParserDependency dependency : dependencies) {
            if (contextPaths.contains(dependency.path())
                    || contextTypes.contains(javaIdentity(dependency.path()))) {
                throw new IllegalStateException(
                        "Java parser dependency collides with parse context: " + dependency.path());
            }
        }
    }

    private List<ParserDependency> parserDependencies(List<Target> targets) {
        String root = RESOURCE_ROOT + crateName + "/";
        String manifest = dependencyResource(root + "parser-dependencies.tsv",
                MAX_PARSER_DEPENDENCY_MANIFEST_BYTES, true);
        if (manifest == null) return List.of();
        Set<String> reservedPaths = new HashSet<>();
        Set<String> reservedTypes = new HashSet<>();
        for (Target target : targets) {
            reservedPaths.add(target.path());
            reservedTypes.add(javaIdentity(target.path()));
        }
        Set<String> resources = new HashSet<>();
        List<ParserDependency> dependencies = new ArrayList<>();
        String previous = "";
        int remaining = MAX_PARSER_DEPENDENCY_BYTES;
        for (String line : manifest.lines().toList()) {
            if (line.isBlank() || line.startsWith("#")) continue;
            String[] cells = line.split("\t", -1);
            if (cells.length != 3 || !javaPath(cells[0]) || previous.compareTo(cells[0]) >= 0
                    || !reservedPaths.add(cells[0]) || !reservedTypes.add(javaIdentity(cells[0]))
                    || !sha(cells[1]) || cells[2].length() > 256
                    || !cells[2].matches("[A-Za-z0-9_][A-Za-z0-9_.-]*\\.java\\.txt")
                    || !resources.add(cells[2])) {
                throw new IllegalStateException("invalid Java parser dependency manifest row");
            }
            if (dependencies.size() == MAX_PARSER_DEPENDENCIES) {
                throw new IllegalStateException("Java parser dependency count budget");
            }
            String text = dependencyResource(root + cells[2], remaining, false);
            if (text.isBlank() || !sha256(text).equals(cells[1])) {
                throw new IllegalStateException("Java parser dependency hash/content drift: " + cells[0]);
            }
            remaining -= text.getBytes(StandardCharsets.UTF_8).length;
            dependencies.add(new ParserDependency(cells[0], text));
            previous = cells[0];
        }
        if (dependencies.isEmpty()) throw new IllegalStateException("empty Java parser dependency manifest");
        return List.copyOf(dependencies);
    }

    private static String javaIdentity(String path) {
        return M3ScopeInference.describeJavaTarget(path).packageName() + "."
                + Path.of(path).getFileName();
    }

    private static List<Parser.Input> dependencyInputs(List<ParserDependency> dependencies) {
        return dependencies.stream()
                .map(dependency -> Parser.Input.fromString(Path.of(dependency.path()), dependency.text()))
                .toList();
    }

    private static void validateParserDependencies(
            List<ParserDependency> dependencies, ExecutionContext context,
            List<Path> parserClasspath) {
        if (dependencies.isEmpty()) return;
        List<SourceFile> parsed = parseChecked(parserBuilder(parserClasspath, false).build(),
                dependencyInputs(dependencies), context,
                "Java parser failed while preparing parser dependencies");
        Map<String, String> expected = new HashMap<>();
        for (ParserDependency dependency : dependencies) expected.put(dependency.path(), dependency.text());
        Set<String> seen = new HashSet<>();
        for (SourceFile source : parsed) {
            String path = normalized(source.getSourcePath());
            if (!(source instanceof J.CompilationUnit) || !seen.add(path)
                    || !source.printAll().equals(expected.get(path))) {
                throw new IllegalStateException("Java parser dependency parse/format drift: " + path);
            }
        }
        if (!seen.equals(expected.keySet())) {
            throw new IllegalStateException("incomplete Java parser dependency parse");
        }
    }

    private static List<SourceFile> parseChecked(
            JavaParser parser, List<Parser.Input> inputs, ExecutionContext context, String failureMessage) {
        AtomicReference<Throwable> parseFailure = new AtomicReference<>();
        ExecutionContext checked = new DelegatingExecutionContext(context) {
            @Override public Consumer<Throwable> getOnError() {
                return failure -> {
                    parseFailure.compareAndSet(null, failure);
                    context.getOnError().accept(failure);
                };
            }
        };
        List<SourceFile> parsed = parser.parseInputs(inputs, null, checked).toList();
        if (parseFailure.get() != null) {
            throw new IllegalStateException(failureMessage, parseFailure.get());
        }
        return parsed;
    }

    private static String dependencyResource(String name, int maxBytes, boolean optional) {
        try (var stream = M3HashPinnedJavaSnapshotRecipe.class.getResourceAsStream(name)) {
            if (stream == null) {
                if (optional) return null;
                throw new IllegalStateException("missing Java parser dependency resource: " + name);
            }
            byte[] bytes = stream.readNBytes(maxBytes + 1);
            if (bytes.length > maxBytes) throw new IllegalStateException("Java parser dependency byte budget");
            String text = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(bytes)).toString();
            if (text.indexOf('\0') >= 0) throw new IllegalStateException("NUL in Java parser dependency resource");
            return text;
        } catch (IOException failure) {
            throw new IllegalStateException("cannot read Java parser dependency resource", failure);
        }
    }

    private List<Target> targets() {
        return targets(resource(RESOURCE_ROOT + crateName + "/manifest.tsv"));
    }

    private List<Target> targets(String manifest) {
        String root = RESOURCE_ROOT + crateName + "/";
        List<Target> targets = new ArrayList<>();
        Set<String> paths = new HashSet<>();
        String previousPath = "";
        for (String line : manifest.lines().toList()) {
            if (line.isBlank() || line.startsWith("#")) continue;
            String[] cells = line.split("\t", -1);
            if (cells.length != 4 || !javaPath(cells[0]) || !paths.add(cells[0])
                    || previousPath.compareTo(cells[0]) >= 0
                    || !("ABSENT".equals(cells[1]) || sha(cells[1]))
                    || !sha(cells[2]) || !cells[3].matches("[A-Za-z0-9_.-]+")) {
                throw new IllegalStateException("invalid hash-pinned Java manifest row");
            }
            previousPath = cells[0];
            String text = resource(root + cells[3]);
            if (!sha256(text).equals(cells[2])) {
                throw new IllegalStateException("Java template hash drift: " + cells[0]);
            }
            targets.add(new Target(cells[0], cells[1], cells[2], text));
        }
        if (targets.isEmpty() || targets.size() > 256) {
            throw new IllegalStateException("hash-pinned Java target budget");
        }
        return List.copyOf(targets);
    }

    private List<ContextSource> contextSources(List<Target> targets, String targetManifest) {
        String root = RESOURCE_ROOT + crateName + "/";
        String expectedManifestSha256 = null;
        for (String line : targetManifest.lines().toList()) {
            if (!line.startsWith("# parse-context-sha256")) continue;
            String[] cells = line.split("\t", -1);
            if (cells.length != 2 || !cells[0].equals("# parse-context-sha256")
                    || !sha(cells[1]) || expectedManifestSha256 != null) {
                throw new IllegalStateException("invalid Java parse context manifest pin");
            }
            expectedManifestSha256 = cells[1];
        }
        String manifest = resource(root + "parse-context.tsv", true);
        if (expectedManifestSha256 != null
                && (manifest == null || !expectedManifestSha256.equals(sha256(manifest)))) {
            throw new IllegalStateException("Java parse context manifest hash drift");
        }
        if (manifest == null) return List.of();
        Set<String> targetPaths = new HashSet<>();
        for (Target target : targets) targetPaths.add(target.path());
        List<ContextSource> sources = new ArrayList<>();
        Set<String> paths = new HashSet<>();
        String previousPath = "";
        int remainingBytes = MAX_PARSE_CONTEXT_UTF8_BYTES;
        for (String line : manifest.lines().toList()) {
            if (line.isBlank() || line.startsWith("#")) continue;
            String[] cells = line.split("\t", -1);
            if (cells.length != 3 || !javaPath(cells[0]) || !paths.add(cells[0])
                    || previousPath.compareTo(cells[0]) >= 0
                    || !sha(cells[1]) || !cells[2].matches("[A-Za-z0-9_.-]+")) {
                throw new IllegalStateException("invalid hash-pinned Java parse context row");
            }
            if (targetPaths.contains(cells[0])) {
                throw new IllegalStateException("Java parse context collides with target: " + cells[0]);
            }
            if (sources.size() >= MAX_PARSE_CONTEXT_SOURCES) {
                throw new IllegalStateException("hash-pinned Java parse context source budget");
            }
            previousPath = cells[0];
            String text = resource(root + cells[2], false, remainingBytes);
            remainingBytes -= text.getBytes(StandardCharsets.UTF_8).length;
            if (!sha256(text).equals(cells[1])) {
                throw new IllegalStateException("Java parse context hash drift: " + cells[0]);
            }
            sources.add(new ContextSource(cells[0], cells[1], text));
        }
        return List.copyOf(sources);
    }

    private static boolean javaPath(String value) {
        int srcSegment = value.startsWith("src/") ? 0 : value.indexOf("/src/");
        int javaSegment = srcSegment < 0 ? -1 : value.indexOf("/java/", srcSegment + 1);
        boolean mavenSource = srcSegment >= 0 && javaSegment > srcSegment;
        // Lucene's Gradle layout uses src/test/org rather than Maven's src/test/java.
        // Admit only the current core/analysis test subtree; all other paths still refuse.
        boolean luceneCoreAnalysisTest = value.startsWith(
                "lucene/core/src/test/org/apache/lucene/analysis/");
        boolean eclipsePdeSource =
                value.startsWith("bundles/") && value.contains("/src/");
        boolean eclipsePdeTestSource =
                value.startsWith("tests/") && value.contains("/src/");
        boolean eclipseNebulaWidgetSource =
                value.startsWith("widgets/") && value.contains("/src/");
        boolean eclipseExampleSource =
                value.startsWith("examples/") && value.contains("/src/");
        boolean eclipseSwtLegacySource = eclipseSwtPdePath(value);
        boolean eclipseSwtExampleSource = value.startsWith(
                "examples/org.eclipse.swt.opengl.examples/src/org/eclipse/swt/opengl/examples/");
        boolean eclipseSwtTestSource =
                value.startsWith("tests/org.eclipse.swt.tests/")
                        && (value.contains("/JUnit Tests/") || value.contains("/ManualTests/"));
        boolean openJdkClassSource =
                value.startsWith("src/") && value.contains("/share/classes/");
        boolean openJdkTestSource = value.startsWith("test/jdk/")
                || value.startsWith("test/langtools/");
        // Existing standalone CPULLM Java/JNI proof is outside Maven src/test/java.
        // Admit this exact first-party path only, never an arbitrary Java bypass.
        boolean cpullmFacetReleaseTest = value.equals(
                "cpullm-java/facet-release/tests/FacetExactCoverageChecks.java");
        // M3 core text uses explicit legacy source and test roots; admit only these packages.
        boolean m3CoreTextSource = value.startsWith("m3/core/src/com/m3/text/");
        boolean m3CoreTextTestSource = value.startsWith("m3/core/test/com/m3/text/");
        if ((!mavenSource && !eclipsePdeSource && !eclipsePdeTestSource && !eclipseNebulaWidgetSource
                && !eclipseExampleSource && !eclipseSwtLegacySource && !eclipseSwtTestSource && !eclipseSwtExampleSource
                && !openJdkClassSource && !openJdkTestSource && !cpullmFacetReleaseTest && !luceneCoreAnalysisTest
                && !m3CoreTextSource && !m3CoreTextTestSource)
                || !value.endsWith(".java")
                || value.startsWith("/")
                || value.indexOf('\\') >= 0
                || value.length() > 4096) {
            return false;
        }
        for (String part : value.split("/", -1)) {
            if (part.isBlank() || part.equals(".") || part.equals("..")
                    || part.chars().anyMatch(Character::isISOControl)) return false;
        }
        return true;
    }

    private static boolean eclipseSwtPdePath(String value) {
        if (!value.startsWith("bundles/")) return false;
        for (String part : value.split("/", -1)) {
            if (part.startsWith("Eclipse SWT")) return true;
        }
        return false;
    }

    private static String normalized(Path path) {
        return path.normalize().toString().replace('\\', '/');
    }

    private static boolean sha(String value) {
        return value.matches("[0-9a-f]{64}");
    }

    private static String resource(String name) {
        return resource(name, false);
    }

    private static String resource(String name, boolean optional) {
        return resource(name, optional, -1);
    }

    private static String resource(String name, boolean optional, int maxUtf8Bytes) {
        try (var stream = M3HashPinnedJavaSnapshotRecipe.class.getResourceAsStream(name)) {
            if (stream == null) {
                if (optional) return null;
                throw new IllegalStateException("missing Java recipe resource: " + name);
            }
            byte[] bytes = maxUtf8Bytes < 0
                    ? stream.readAllBytes() : stream.readNBytes(maxUtf8Bytes + 1);
            if (maxUtf8Bytes >= 0 && bytes.length > maxUtf8Bytes) {
                throw new IllegalStateException("hash-pinned Java parse context byte budget");
            }
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes)).toString();
        } catch (IOException failure) {
            throw new IllegalStateException("cannot read Java recipe resource", failure);
        }
    }

    private static String sha256(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException impossible) {
            throw new ExceptionInInitializerError(impossible);
        }
    }
}
