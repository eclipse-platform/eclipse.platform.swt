package com.synexia.m3.viewport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;

class GtkVirtualTreeSparseBulkRecipeTest {
    private static final String FIXTURE =
            "package org.eclipse.swt.widgets;\n"
            + "class Tree {\n"
            + "\tVirtualTreeTopology virtualTopology; TreeItem [] items;\n"
            + GtkVirtualTreeSparseBulkCandidate.BEFORE + "\n"
            + "}\n"
            + "class TreeItem { boolean isDisposed(){ return false; } TreeItem[] getItems(){ return null; } }\n"
            + "class VirtualTreeTopology {\n"
            + "\tstatic final int ROOT=-1; boolean contains(int i){return true;}\n"
            + "\tint firstMaterializedChildId(int p){return -1;} int nextMaterializedSiblingId(int i){return -1;}\n"
            + "}\n";

    @Test
    void candidateIsFixedPointAndRejectsDrift() {
        Path target = Path.of(GtkVirtualTreeSparseBulkCandidate.TARGET);
        String after = GtkVirtualTreeSparseBulkCandidate.propose(target, FIXTURE, false);
        assertTrue(after.contains("firstMaterializedChildId"));
        assertSame(after, GtkVirtualTreeSparseBulkCandidate.propose(target, after, false));
        assertThrows(IllegalStateException.class,
                () -> GtkVirtualTreeSparseBulkCandidate.propose(
                        target, FIXTURE.replace("GTK and Win32", "GTK only"), false));
        assertThrows(IllegalStateException.class,
                () -> GtkVirtualTreeSparseBulkCandidate.propose(target, FIXTURE));
        assertSame(FIXTURE, GtkVirtualTreeSparseBulkCandidate.propose(
                Path.of("other/Tree.java"), FIXTURE));
    }

    @Test
    void openRewriteParsesAndReplaysTheCandidateExactly() {
        InMemoryExecutionContext context = new InMemoryExecutionContext(Throwable::printStackTrace);
        J.CompilationUnit before = parse(FIXTURE, context);
        J.CompilationUnit after = (J.CompilationUnit) new GtkVirtualTreeSparseBulkRecipe(false)
                .getVisitor().visit(before, context);
        String expected = GtkVirtualTreeSparseBulkCandidate.propose(
                Path.of(GtkVirtualTreeSparseBulkCandidate.TARGET), FIXTURE, false);
        assertEquals(expected, after.printAll());

        J.CompilationUnit repeat = (J.CompilationUnit) new GtkVirtualTreeSparseBulkRecipe(false)
                .getVisitor().visit(after, context);
        assertEquals(expected, repeat.printAll());
    }

    private static J.CompilationUnit parse(String source, InMemoryExecutionContext context) {
        try (var stream = JavaParser.fromJavaVersion().build().parseInputs(
                List.of(Parser.Input.fromString(
                        Path.of(GtkVirtualTreeSparseBulkCandidate.TARGET), source)),
                null, context)) {
            List<SourceFile> files = stream.toList();
            assertEquals(1, files.size());
            return (J.CompilationUnit) files.getFirst();
        }
    }
}
