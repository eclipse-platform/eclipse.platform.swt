// SPDX-License-Identifier: EPL-2.0
package com.synexia.m3.bootstrap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaParser;

/**
 * Executes the actual OpenRewrite SDK, source read-only, preserving SWT APIs.
 * Original shared classes are pinned to their canonical Synexia Git blobs.
 */
class M3BootstrapCrossForkExecutionTest {
    @TempDir Path scratch;

    @Test
    void readOnlyInventoryIsAnSdkFixedPoint() {
        var recipe = new M3BootstrapInventoryRecipe();
        assertFalse(recipe.mutationAuthority());
        var context = new InMemoryExecutionContext(failure -> {
            throw new IllegalStateException(failure);
        });
        String source = "package fixture; public class Fixture { "
                + "String text = \"class Fake { void run() {} }\"; "
                + "int value(int n) { return n + 1; } }";
        try (var parsed = JavaParser.fromJavaVersion().build().parseInputs(
                List.of(Parser.Input.fromString(Path.of("fixture/Fixture.java"), source)),
                null, context)) {
            List<SourceFile> before = parsed.toList();
            assertEquals(1, before.size());
            var first = recipe.run(new InMemoryLargeSourceSet(before), context, 1);
            assertTrue(first.getChangeset().getAllResults().isEmpty());
            var second = recipe.run(new InMemoryLargeSourceSet(before), context, 1);
            assertTrue(second.getChangeset().getAllResults().isEmpty());
            assertEquals(source, before.getFirst().printAll());
        }
    }

    @Test
    void taskGateRejectsUnknownAndChangedContent() throws Exception {
        Path task = Files.writeString(scratch.resolve("task.md"), "M3 bounded recipe evidence");
        String digest = HexFormat.of().formatHex(
                MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(task)));
        String oldFile = System.getProperty("m3.llm.taskCrateFile");
        String oldHash = System.getProperty("m3.llm.taskCrateRoot");
        try {
            System.clearProperty("m3.llm.taskCrateFile");
            System.clearProperty("m3.llm.taskCrateRoot");
            assertThrows(IllegalArgumentException.class, M3BootstrapTaskRecipe::verify);
            System.setProperty("m3.llm.taskCrateFile", task.toString());
            System.setProperty("m3.llm.taskCrateRoot", digest);
            M3BootstrapTaskRecipe.verify();
            assertEquals(1, new M3BootstrapTaskRecipe().getRecipeList().size());
            Files.writeString(task, "changed");
            assertThrows(IllegalStateException.class, M3BootstrapTaskRecipe::verify);
        } finally {
            if (oldFile == null) System.clearProperty("m3.llm.taskCrateFile");
            else System.setProperty("m3.llm.taskCrateFile", oldFile);
            if (oldHash == null) System.clearProperty("m3.llm.taskCrateRoot");
            else System.setProperty("m3.llm.taskCrateRoot", oldHash);
        }
    }
}
