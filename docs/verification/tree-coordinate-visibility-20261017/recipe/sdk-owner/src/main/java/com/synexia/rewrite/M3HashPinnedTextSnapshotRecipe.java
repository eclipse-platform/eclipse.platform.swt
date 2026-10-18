// SPDX-License-Identifier: Apache-2.0
// SPDX-FileCopyrightText: 2026 Hitesh Soliwal and contributors
// Modified 2026: admit only the exact root .gitattributes metadata owner.
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
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Parser;
import org.openrewrite.ScanningRecipe;
import org.openrewrite.SourceFile;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.text.PlainText;
import org.openrewrite.text.PlainTextParser;

/**
 * Hash-pinned whole-file candidate for native/plain-text sources without a richer admitted parser.
 *
 * <p>This is the TEXT counterpart of {@link M3HashPinnedJavaSnapshotRecipe}. It does not parse or
 * reinterpret C/C++ semantics: exact preimage identity and exact reviewed postimage bytes are
 * required, then ordinary compiler/test/runtime proof remains authoritative.</p>
 */
public final class M3HashPinnedTextSnapshotRecipe
    extends ScanningRecipe<M3HashPinnedTextSnapshotRecipe.Inventory> {
  private static final String RESOURCE_ROOT = "/com/synexia/rewrite/hash-pinned-text/";

  @Option(
      displayName = "Crate name",
      description = "Exact classpath crate directory below hash-pinned-text.",
      example = "indexstring-direct-array-native")
  private final String crateName;

  private record Target(String path, String before, String after, String text) {}

  public static final class Inventory {
    private final List<Target> targets;
    private final Map<String, String> seen = new HashMap<>();
    private final List<String> conflicts = new ArrayList<>();
    private Map<String, SourceFile> candidates;

    private Inventory(List<Target> targets) {
      this.targets = targets;
    }
  }

  @JsonCreator
  public M3HashPinnedTextSnapshotRecipe(@JsonProperty("crateName") String crateName) {
    if (crateName == null || !crateName.matches("[a-z0-9][a-z0-9-]{0,79}")) {
      throw new IllegalArgumentException("invalid hash-pinned text crate");
    }
    this.crateName = crateName;
  }

  @Override
  public String getDisplayName() {
    return "M3 hash-pinned native/text snapshot candidate";
  }

  @Override
  public String getDescription() {
    return "Replays reviewed plain-text/native postimages only from exact SHA-256 preimages "
        + "or their fixed-point postimages.";
  }

  @Override
  public Set<String> getTags() {
    return Set.of("synexia", "m3", "openrewrite", "text", "native", "recipe-first", "candidate-only");
  }

  @Override
  public int maxCycles() {
    return 1;
  }

  @Override
  public Inventory getInitialValue(ExecutionContext context) {
    return new Inventory(targets());
  }

  @Override
  public TreeVisitor<?, ExecutionContext> getScanner(Inventory inventory) {
    return new TreeVisitor<Tree, ExecutionContext>() {
      @Override
      public Tree preVisit(Tree tree, ExecutionContext context) {
        if (tree instanceof SourceFile file) {
          stopAfterPreVisit();
          String path = normalized(file.getSourcePath());
          for (Target target : inventory.targets) {
            if (!target.path().equals(path)) continue;
            String hash = sha256(file.printAll());
            synchronized (inventory) {
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

  @Override
  public Collection<? extends SourceFile> generate(Inventory inventory, ExecutionContext context) {
    prepare(inventory, context);
    List<SourceFile> generated = new ArrayList<>();
    for (Target target : inventory.targets) {
      if (!inventory.seen.containsKey(target.path())) {
        generated.add(inventory.candidates.get(target.path()));
      }
    }
    return generated;
  }

  @Override
  public TreeVisitor<?, ExecutionContext> getVisitor(Inventory inventory) {
    requireAdmissible(inventory);
    return new TreeVisitor<Tree, ExecutionContext>() {
      @Override
      public Tree preVisit(Tree tree, ExecutionContext context) {
        if (tree instanceof SourceFile file) {
          prepare(inventory, context);
          stopAfterPreVisit();
          String path = normalized(file.getSourcePath());
          for (Target target : inventory.targets) {
            if (!target.path().equals(path)) continue;
            String current = sha256(file.printAll());
            String scanned = inventory.seen.get(path);
            boolean matchesManifest = current.equals(target.before()) || current.equals(target.after());
            boolean matchesScan = scanned == null
                ? target.before().equals("ABSENT") && current.equals(target.after())
                : current.equals(scanned);
            if (!matchesManifest || !matchesScan) {
              throw new IllegalStateException("target changed after scan: " + path);
            }
            if (current.equals(target.after())) return tree;
            SourceFile replacement = PlainTextParser.convert(file).withText(target.text());
            replacement = replacement.withId(file.getId());
            replacement = replacement.withSourcePath(file.getSourcePath());
            replacement = replacement.withMarkers(file.getMarkers());
            replacement = replacement.withFileAttributes(file.getFileAttributes());
            replacement = replacement.withCharset(file.getCharset());
            replacement = replacement.withCharsetBomMarked(file.isCharsetBomMarked());
            return replacement.withChecksum(null);
          }
        }
        return tree;
      }
    };
  }

  public String getCrateName() {
    return crateName;
  }

  public List<String> targetPaths() {
    return targets().stream().map(Target::path).toList();
  }

  public Map<String, String> preimageManifest() {
    Map<String, String> result = new java.util.LinkedHashMap<>();
    for (Target target : targets()) result.put(target.path(), target.before());
    return Map.copyOf(result);
  }

  private static void prepare(Inventory inventory, ExecutionContext context) {
    synchronized (inventory) {
      requireAdmissible(inventory);
      if (inventory.candidates == null) {
        Map<String, SourceFile> prepared = new HashMap<>();
        for (Target target : inventory.targets) prepared.put(target.path(), parse(target, context));
        inventory.candidates = Map.copyOf(prepared);
      }
    }
  }

  private static void requireAdmissible(Inventory inventory) {
    synchronized (inventory) {
      for (Target target : inventory.targets) {
        if (!target.before().equals("ABSENT") && !inventory.seen.containsKey(target.path())) {
          throw new IllegalStateException("required source missing: " + target.path());
        }
      }
      if (!inventory.conflicts.isEmpty()) {
        throw new IllegalStateException(String.join("; ", inventory.conflicts));
      }
    }
  }

  private static SourceFile parse(Target target, ExecutionContext context) {
    List<SourceFile> parsed = PlainTextParser.builder().build()
        .parseInputs(
            List.of(Parser.Input.fromString(Path.of(target.path()), target.text())),
            null,
            context)
        .toList();
    if (parsed.size() != 1
        || !(parsed.getFirst() instanceof PlainText)
        || !target.text().equals(parsed.getFirst().printAll())) {
      throw new IllegalStateException("text template parse/format drift: " + target.path());
    }
    return parsed.getFirst();
  }

  private List<Target> targets() {
    String root = RESOURCE_ROOT + crateName + "/";
    List<Target> targets = new ArrayList<>();
    Set<String> paths = new HashSet<>();
    String previousPath = "";
    for (String line : resource(root + "manifest.tsv").lines().toList()) {
      if (line.isBlank() || line.startsWith("#")) continue;
      String[] cells = line.split("\t", -1);
      if (cells.length != 4
          || !textPath(cells[0])
          || !paths.add(cells[0])
          || previousPath.compareTo(cells[0]) >= 0
          || !("ABSENT".equals(cells[1]) || sha(cells[1]))
          || !sha(cells[2])
          || !cells[3].matches("[A-Za-z0-9_.-]+")) {
        throw new IllegalStateException("invalid hash-pinned text manifest row");
      }
      // The one malformed Java test syntax bootstrap is admitted only by its named
      // recovery crate and immutable before/after digests. No unrelated text crate may
      // use this path as an alternative to the canonical Java parser and source recipe.
      if (cells[0].equals("test/jdk/java/lang/String/M3StringPrecomputeSearchTest.java")
          && (!crateName.equals("m3jdk21-string-invalid-escape-syntax-20261008")
              || !cells[1].equals("825bf78d1684665fb85c3a93241700cb1e5d9403f9cb71603db7208303abcc56")
              || !cells[2].equals("1fd4c8d856e7de971c874936ef8561167041cd3c087e6ad8796d3225cc6087dc"))) {
        throw new IllegalStateException("unauthorized invalid Java syntax bootstrap");
      }
      previousPath = cells[0];
      String text = resource(root + cells[3]);
      if (!sha256(text).equals(cells[2])) {
        throw new IllegalStateException("text template hash drift: " + cells[0]);
      }
      targets.add(new Target(cells[0], cells[1], cells[2], text));
    }
    if (targets.isEmpty() || targets.size() > 256) {
      throw new IllegalStateException("hash-pinned text target budget");
    }
    return List.copyOf(targets);
  }

  private static boolean textPath(String value) {
    if (value == null
        || value.length() > 4096
        || value.indexOf('\\') >= 0
        || value.chars().anyMatch(Character::isISOControl)) {
      return false;
    }
    boolean nativePath =
        value.startsWith("src/main/native/")
            || value.startsWith("src/test/native/")
            || value.startsWith("native/")
            || value.contains("/native/")
            || (value.startsWith("src/") && value.contains("/share/native/"))
            || value.startsWith("src/hotspot/share/")
            || (value.startsWith("test/jdk/")
                && (value.endsWith(".c") || value.endsWith(".h")
                    || value.endsWith(".cc") || value.endsWith(".cpp")
                    || value.endsWith(".hpp")));
    // Ordinary Maven C owners need the same hash-pinned admission as native/ trees.
    // No arbitrary .java, top-level .c, traversal or unowned source path is admitted.
    boolean mavenCPath = value.matches(
        "[A-Za-z0-9_.-]+/(?:crates/[A-Za-z0-9_.-]+/)?src/(?:main|test)/c/"
            + "(?:[A-Za-z0-9_.-]+/)*[A-Za-z0-9_.-]+\\.(?:c|h|cc|cpp|hpp)");
    // First-party SWT Initiative 31 CPU/JNI kernel lives in the M3 recipe module's
    // explicit C++ source root. Preserve AST/text separation: this gate admits
    // only exact source files under that owner, not arbitrary repository paths.
    boolean m3RecipeCpp = value.matches(
        "synexia-m3-recipe/src/(?:main|test)/cpp/"
            + "(?:[A-Za-z0-9_.-]+/)*[A-Za-z0-9_.-]+\\.(?:c|h|cc|cpp|hpp)")
        || value.equals("synexia-m3-recipe/src/main/cpp/CMakeLists.txt");
    // Eclipse bundles and SWT fragments use manifest-first Tycho metadata; no XML/Java parser owns this format.
    // Scope exception to the exact recovered JNI C atom; no generic module-native bypass.
    boolean m3MasteryNativeAtom =
        value.equals("synexia-openrewrite-recipes/native/recipe-mastery/m3_atom_pattern_signals_jni.c");
    boolean eclipseBundleManifest = value.matches(
        "bundles/[A-Za-z0-9_.-]+/META-INF/MANIFEST[.]MF");
    boolean swtManifest = value.matches(
        "binaries/org[.]eclipse[.]swt[.][a-z0-9_]+[.][a-z0-9_]+[.][a-z0-9_]+/META-INF/MANIFEST[.]MF");
    // Explicit bootstrap targets whose preimages are outside the pinned Java parser's domain.
    // The task crate seals every byte; typed postimage and Java 21 compiler proofs remain required.
    boolean swtDistillationBootstrap = switch (value) {
      case "bundles/org.eclipse.swt.tools/.settings/org.eclipse.jdt.core.prefs",
          "bundles/org.eclipse.swt.tools/JNI Generation/org/eclipse/swt/tools/internal/JNIGeneratorAppUI.java",
          "bundles/org.eclipse.swt.tools/META-INF/MANIFEST.MF",
          "bundles/org.eclipse.swt.tools/Mac Generation/org/eclipse/swt/tools/internal/MacGenerator.java",
          "bundles/org.eclipse.swt.tools/Mac Generation/org/eclipse/swt/tools/internal/MacGeneratorUI.java",
          "bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/Display.java",
          "bundles/org.eclipse.swt/Eclipse SWT/gtk/org/eclipse/swt/widgets/Display.java",
          "bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/Display.java" -> true;
      default -> false;
    };
    // Hand-maintained SWT GTK owner; generated JNI files remain excluded.
    boolean swtGtkNativeOwner = value.equals(
        "bundles/org.eclipse.swt/Eclipse SWT PI/gtk/library/os_custom.c");
    // JDK bootstrap targets whose Javadoc the pinned Java parser (OpenRewrite 8.17.1) cannot
    // convert; the crate seals every byte and the Java 21 --patch-module compile proof remains.
    boolean jdkJavadocBootstrap = value.equals(
        "src/java.base/share/classes/java/util/regex/Pattern.java")
        || value.equals("src/java.base/share/classes/java/lang/String.java")
        || value.equals("src/java.base/share/classes/java/lang/invoke/StringConcatFactory.java");
    // One exact historical test has an invalid Java preimage (an unterminated escape at line
    // 807). The AST recipe cannot parse it. Admit only this fail-closed source path through
    // the existing digest-pinned text bootstrap; its successor must pass Java 21 syntax proof.
    // This does not admit arbitrary Java sources to the text mutation plane.
    boolean invalidJavaSyntaxBootstrap = value.equals(
        "test/jdk/java/lang/String/M3StringPrecomputeSearchTest.java");
    // Gradle scripts and resource-owned Java service descriptors are build inputs.
    // Restrict extensionless descriptors to standard Maven source-resource roots.
    boolean javaServiceDescriptor = value.matches(
        "(?:[A-Za-z0-9_.-]+/)*src/(?:main|test)/resources/META-INF/services/"
            + "[A-Za-z_$][A-Za-z0-9_$]*(?:\\.[A-Za-z_$][A-Za-z0-9_$]*)*");
    boolean m3BuildLauncher = value.matches(
        "m3/tooling/module-packs/build-tools/launchers/m3-(?:ant|gradle|mvn)(?:[.]cmd)?");
    // Keep the existing Gradle compiler-argument contract in its native source language.
    boolean gradleCompilerArgumentsContract = value.equals(
        "platforms/jvm/java-compiler-worker/src/test/groovy/org/gradle/api/internal/tasks/compile/JavaCompilerArgumentsBuilderTest.groovy");
    String lower = value.toLowerCase(java.util.Locale.ROOT);
    boolean structuredText = value.equals(".gitattributes") || lower.endsWith(".py") || lower.endsWith(".sh")
        || lower.endsWith(".json") || lower.endsWith(".md") || lower.endsWith(".txt")
        || lower.endsWith(".tsv") || lower.endsWith(".yml") || lower.endsWith(".yaml")
        || lower.endsWith(".xml") || lower.endsWith(".properties")
        || lower.endsWith(".gradle") || lower.endsWith(".gradle.kts");
    if (!nativePath && !mavenCPath && !m3RecipeCpp && !m3MasteryNativeAtom && !eclipseBundleManifest && !swtManifest && !swtDistillationBootstrap && !swtGtkNativeOwner
        && !jdkJavadocBootstrap && !invalidJavaSyntaxBootstrap && !structuredText && !javaServiceDescriptor && !m3BuildLauncher && !gradleCompilerArgumentsContract) return false;
    for (String part : value.split("/", -1)) {
      if (part.isBlank() || part.equals(".") || part.equals("..")) return false;
    }
    return m3RecipeCpp || eclipseBundleManifest || swtManifest || swtDistillationBootstrap || jdkJavadocBootstrap
        || invalidJavaSyntaxBootstrap || structuredText || javaServiceDescriptor || m3BuildLauncher || gradleCompilerArgumentsContract
        || value.endsWith(".c") || value.endsWith(".h")
        || value.endsWith(".cc") || value.endsWith(".cpp") || value.endsWith(".hpp")
        || value.endsWith("CMakeLists.txt");
  }

  private static String normalized(Path path) {
    return path.normalize().toString().replace('\\', '/');
  }

  private static boolean sha(String value) {
    return value.matches("[0-9a-f]{64}");
  }

  private static String resource(String name) {
    try (var stream = M3HashPinnedTextSnapshotRecipe.class.getResourceAsStream(name)) {
      if (stream == null) throw new IllegalStateException("missing text recipe resource: " + name);
      return StandardCharsets.UTF_8.newDecoder()
          .onMalformedInput(CodingErrorAction.REPORT)
          .onUnmappableCharacter(CodingErrorAction.REPORT)
          .decode(ByteBuffer.wrap(stream.readAllBytes()))
          .toString();
    } catch (IOException failure) {
      throw new IllegalStateException("cannot read text recipe resource", failure);
    }
  }

  private static String sha256(String value) {
    try {
      return HexFormat.of().formatHex(
          MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException impossible) {
      throw new ExceptionInInitializerError(impossible);
    }
  }
}
