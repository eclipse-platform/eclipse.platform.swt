package com.synexia.m3.viewport;

import java.util.List;
import java.util.Set;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;

/** Sealed OpenRewrite replay for the GTK virtual Tree sparse bulk-traversal atom. */
public final class GtkVirtualTreeSparseBulkRecipe extends Recipe {
    private final boolean enforceTargetHash;

    public GtkVirtualTreeSparseBulkRecipe() {
        this(true);
    }

    GtkVirtualTreeSparseBulkRecipe(boolean enforceTargetHash) {
        this.enforceTargetHash = enforceTargetHash;
    }

    @Override
    public String getDisplayName() {
        return "Keep GTK virtual Tree bulk collapse sparse";
    }

    @Override
    public String getDescription() {
        return "Replace the exact admitted GTK Tree modelChildren atom so non-materializing "
                + "bulk operations traverse only already-materialized topology.";
    }

    @Override
    public Set<String> getTags() {
        return Set.of("synexia", "m3", "swt", "viewport", "gtk", "recipe-first");
    }

    @Override
    public int maxCycles() {
        return 1;
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.CompilationUnit visitCompilationUnit(
                    J.CompilationUnit compilationUnit, ExecutionContext context) {
                J.CompilationUnit visited = super.visitCompilationUnit(compilationUnit, context);
                String current = visited.printAll();
                String candidate = GtkVirtualTreeSparseBulkCandidate.propose(
                        visited.getSourcePath(), current, enforceTargetHash);
                if (current.equals(candidate)) return visited;

                Parser.Input input = Parser.Input.fromString(visited.getSourcePath(), candidate);
                List<SourceFile> parsed;
                try (var stream = JavaParser.fromJavaVersion().build()
                        .parseInputs(List.of(input), null, context)) {
                    parsed = stream.toList();
                }
                if (parsed.size() != 1
                        || !(parsed.getFirst() instanceof J.CompilationUnit replacement)
                        || !candidate.equals(replacement.printAll())) {
                    throw new IllegalStateException(
                            "M3_GTK_TREE_JAVA_ROUNDTRIP:" + visited.getSourcePath());
                }
                return replacement.withSourcePath(visited.getSourcePath())
                        .withId(visited.getId())
                        .withMarkers(visited.getMarkers());
            }
        };
    }
}
