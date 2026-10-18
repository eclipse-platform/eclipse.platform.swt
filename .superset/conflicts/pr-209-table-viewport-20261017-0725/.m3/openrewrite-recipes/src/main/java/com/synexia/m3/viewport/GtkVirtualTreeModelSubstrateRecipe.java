package com.synexia.m3.viewport;

import java.util.List;
import java.util.Set;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.text.PlainText;

/** Exact-source replay for the dormant GTK virtual Tree logical-model substrate. */
public final class GtkVirtualTreeModelSubstrateRecipe extends Recipe {
    private final boolean enforceHash;

    public GtkVirtualTreeModelSubstrateRecipe() {
        this(true);
    }

    GtkVirtualTreeModelSubstrateRecipe(boolean enforceHash) {
        this.enforceHash = enforceHash;
    }

    @Override public String getDisplayName() {
        return "Add the GTK virtual Tree logical-model substrate";
    }

    @Override public String getDescription() {
        return "Add a primitive VirtualTreeTopology snapshot and a dormant hierarchical "
                + "SwtVirtualTreeModel without activating it in Tree.";
    }

    @Override public Set<String> getTags() {
        return Set.of("synexia", "m3", "swt", "gtk", "tree", "viewport", "jni", "recipe-first");
    }

    @Override public int maxCycles() {
        return 1;
    }

    @Override public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new TreeVisitor<Tree, ExecutionContext>() {
            @Override public Tree visit(Tree tree, ExecutionContext context) {
                if (!(tree instanceof SourceFile source)) return tree;
                String current = source.printAll();
                String candidate = GtkVirtualTreeModelSubstrateCandidate.propose(
                        source.getSourcePath(), current, enforceHash);
                if (candidate.equals(current)) return tree;
                String path = source.getSourcePath().toString().replace('\\', '/');
                if (path.endsWith(GtkVirtualTreeModelSubstrateCandidate.TOPOLOGY_PATH)) {
                    try (var parsed = JavaParser.fromJavaVersion().build().parseInputs(
                            List.of(Parser.Input.fromString(source.getSourcePath(), candidate)),
                            null, context)) {
                        List<SourceFile> files = parsed.toList();
                        if (files.size() != 1 || !(files.getFirst() instanceof J.CompilationUnit replacement)
                                || !candidate.equals(replacement.printAll())) {
                            throw new IllegalStateException("M3_TREE_TOPOLOGY_JAVA_ROUNDTRIP");
                        }
                        return replacement.withId(source.getId()).withMarkers(source.getMarkers())
                                .withSourcePath(source.getSourcePath());
                    }
                }
                if (source instanceof PlainText plainText) {
                    return plainText.withText(candidate);
                }
                throw new IllegalStateException("M3_TREE_NATIVE_SOURCE_NOT_PLAINTEXT:" + source.getSourcePath());
            }
        };
    }
}
