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
import org.openrewrite.text.PlainText;

class GtkVirtualTreeModelSubstrateRecipeTest {
    private static final String TOPOLOGY_FIXTURE =
            "package org.eclipse.swt.widgets;\n"
            + "class VirtualTreeTopology {\n"
            + "\tstatic final int ROOT=-1, UNKNOWN_CHILD_COUNT=-1;\n"
            + "\tint[] parentIds=new int[4], childIndices=new int[4], childCounts=new int[4];\n"
            + "\tint rootChildCount; void requirePresent(int id){}\n"
            + GtkVirtualTreeModelSubstrateCandidate.TOPOLOGY_ANCHOR
            + "}\n";

    private static final String NATIVE_FIXTURE =
            "#if !defined(GTK4)\n"
            + "static void swt_virtual_table_model_init(SwtVirtualTableModel *self) {}\n"
            + "#endif\n\n"
            + "static void *content_providers_copy(void *ptr) { return ptr; }\n"
            + "void register_model(const char *lpname) { long rc;\n"
            + "#if !defined(GTK4)\n"
            + "\tif (lpname && strcmp(lpname, \"SwtVirtualTableModel\") == 0) {\n"
            + "\t\trc = swt_virtual_table_model_get_type();\n"
            + "\t} else\n"
            + "#endif\n"
            + "\t{ rc = 0; }\n"
            + "}\n";

    @Test void exactCandidatesAreFixedPointsAndRejectDrift() {
        String topology = GtkVirtualTreeModelSubstrateCandidate.propose(
                Path.of(GtkVirtualTreeModelSubstrateCandidate.TOPOLOGY_PATH),
                TOPOLOGY_FIXTURE, false);
        assertTrue(topology.contains("nativeModelSnapshot"));
        assertSame(topology, GtkVirtualTreeModelSubstrateCandidate.propose(
                Path.of(GtkVirtualTreeModelSubstrateCandidate.TOPOLOGY_PATH), topology, false));

        String nativeSource = GtkVirtualTreeModelSubstrateCandidate.propose(
                Path.of(GtkVirtualTreeModelSubstrateCandidate.NATIVE_PATH),
                NATIVE_FIXTURE, false);
        assertTrue(nativeSource.contains("SwtVirtualTreeModel"));
        assertTrue(nativeSource.contains("swt_virtual_tree_model_get_type"));
        assertSame(nativeSource, GtkVirtualTreeModelSubstrateCandidate.propose(
                Path.of(GtkVirtualTreeModelSubstrateCandidate.NATIVE_PATH), nativeSource, false));

        assertThrows(IllegalStateException.class, () -> GtkVirtualTreeModelSubstrateCandidate.propose(
                Path.of(GtkVirtualTreeModelSubstrateCandidate.TOPOLOGY_PATH), TOPOLOGY_FIXTURE));
        assertThrows(IllegalStateException.class, () -> GtkVirtualTreeModelSubstrateCandidate.propose(
                Path.of(GtkVirtualTreeModelSubstrateCandidate.NATIVE_PATH), NATIVE_FIXTURE));
        assertSame(TOPOLOGY_FIXTURE, GtkVirtualTreeModelSubstrateCandidate.propose(
                Path.of("other/VirtualTreeTopology.java"), TOPOLOGY_FIXTURE));
    }

    @Test void recipeReparsesJavaAndReplaysNativeText() {
        InMemoryExecutionContext context = new InMemoryExecutionContext(Throwable::printStackTrace);
        GtkVirtualTreeModelSubstrateRecipe recipe = new GtkVirtualTreeModelSubstrateRecipe(false);

        J.CompilationUnit java = parseJava(TOPOLOGY_FIXTURE, context);
        J.CompilationUnit rewritten = (J.CompilationUnit) recipe.getVisitor().visit(java, context);
        assertTrue(rewritten.printAll().contains("nativeModelSnapshot"));

        PlainText nativeText = PlainText.builder()
                .sourcePath(Path.of(GtkVirtualTreeModelSubstrateCandidate.NATIVE_PATH))
                .text(NATIVE_FIXTURE).build();
        PlainText rewrittenNative = (PlainText) recipe.getVisitor().visit(nativeText, context);
        assertTrue(rewrittenNative.getText().contains("SwtVirtualTreeModel"));
        assertEquals(rewrittenNative, recipe.getVisitor().visit(rewrittenNative, context));
    }

    private static J.CompilationUnit parseJava(String source, InMemoryExecutionContext context) {
        try (var parsed = JavaParser.fromJavaVersion().build().parseInputs(
                List.of(Parser.Input.fromString(
                        Path.of(GtkVirtualTreeModelSubstrateCandidate.TOPOLOGY_PATH), source)),
                null, context)) {
            List<SourceFile> files = parsed.toList();
            assertEquals(1, files.size());
            return (J.CompilationUnit) files.getFirst();
        }
    }
}
