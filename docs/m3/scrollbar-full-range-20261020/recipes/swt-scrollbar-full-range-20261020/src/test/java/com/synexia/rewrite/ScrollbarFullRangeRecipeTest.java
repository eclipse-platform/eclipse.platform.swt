/* Copyright 2026 Hitesh Soliwal and contributors. SPDX-License-Identifier: Apache-2.0 */
package com.synexia.rewrite;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;

final class ScrollbarFullRangeRecipeTest {
    private static final String CRATE = "swt-scrollbar-full-range-20261020";
    private static final String ROOT = "/com/synexia/rewrite/hash-pinned-java/" + CRATE + "/";
    private static final String TARGET = "bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/ViewportScrollBarGeometry.java";
    private static final String AFTER = "df4f8498b1f633f2ffeae310f761ecaa0ea686e392557b8cfe2c684864a7f9b4";

    private static String text(String name) throws Exception {
        try (var input = ScrollbarFullRangeRecipeTest.class.getResourceAsStream(ROOT + name)) {
            assertNotNull(input, name);
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
    private static InMemoryExecutionContext context() {
        return new InMemoryExecutionContext(e -> { throw new IllegalStateException("recipe gate failed", e); });
    }
    private static List<SourceFile> parse(String content) {
        var files = JavaParser.fromJavaVersion().build().parse(content).toList();
        assertEquals(1, files.size());
        assertInstanceOf(J.CompilationUnit.class, files.getFirst());
        return List.of(files.getFirst().withSourcePath(Path.of(TARGET)));
    }
    private static M3HashPinnedJavaSnapshotRecipe recipe() { return new M3HashPinnedJavaSnapshotRecipe(CRATE); }
    @Test void exactGeneration() throws Exception {
        var r = recipe();
        assertEquals(List.of(TARGET), r.targetPaths());
        var results = r.run(new InMemoryLargeSourceSet(parse(text("ViewportScrollBarGeometry.before.java.txt"))), context(), 1).getChangeset().getAllResults();
        assertEquals(1, results.size());
        var after = results.getFirst().getAfter();
        assertNotNull(results.getFirst().getBefore());
        assertNotNull(after);
        assertEquals(TARGET, after.getSourcePath().toString().replace('\\', '/'));
        assertEquals(text("ViewportScrollBarGeometry.java.txt"), after.printAll());
        String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(after.printAll().getBytes(StandardCharsets.UTF_8)));
        assertEquals(AFTER, hash);
        Path destination = Path.of("target/generated-receiver").resolve(TARGET);
        Files.createDirectories(destination.getParent());
        Files.writeString(destination, after.printAll(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        System.out.println("OPENREWRITE_GENERATED_SHA256=" + hash);
    }
    @Test void fixedPoint() throws Exception {
        assertTrue(recipe().run(new InMemoryLargeSourceSet(parse(text("ViewportScrollBarGeometry.java.txt"))), context(), 1).getChangeset().getAllResults().isEmpty());
    }
    @Test void refusesDrift() throws Exception {
        var files = parse(text("ViewportScrollBarGeometry.before.java.txt") + "\n");
        assertThrows(RuntimeException.class, () -> recipe().run(new InMemoryLargeSourceSet(files), context(), 1));
    }
    @Test void refusesMissingSource() {
        assertThrows(RuntimeException.class, () -> recipe().run(new InMemoryLargeSourceSet(List.of()), context(), 1));
    }
    @Test void refusesDuplicateSource() throws Exception {
        var files = new ArrayList<>(parse(text("ViewportScrollBarGeometry.before.java.txt")));
        files.addAll(parse(text("ViewportScrollBarGeometry.before.java.txt")));
        assertThrows(RuntimeException.class, () -> recipe().run(new InMemoryLargeSourceSet(files), context(), 1));
    }
    @Test void refusesChangedBehavior() throws Exception {
        String before = text("ViewportScrollBarGeometry.before.java.txt");
        String changed = before.replace("if (c == 0) return 0;", "if (c == 0) return 1;");
        assertNotEquals(before, changed);
        var files = parse(changed);
        assertThrows(RuntimeException.class, () -> recipe().run(new InMemoryLargeSourceSet(files), context(), 1));
    }
    @Test void ignoresCodeLookingDecoyInBothOrders() throws Exception {
        var decoy = JavaParser.fromJavaVersion().build().parse("class Decoy { String text = \"thumb + 1; maximum - minimum + 1\"; }").findFirst().orElseThrow().withSourcePath(Path.of("src/main/java/Decoy.java"));
        var files = new ArrayList<>(parse(text("ViewportScrollBarGeometry.before.java.txt")));
        files.add(decoy);
        for (int order = 0; order < 2; order++) {
            Collections.reverse(files);
            var results = recipe().run(new InMemoryLargeSourceSet(files), context(), 1).getChangeset().getAllResults();
            assertEquals(1, results.size());
            assertEquals(text("ViewportScrollBarGeometry.java.txt"), results.getFirst().getAfter().printAll());
        }
    }
}
