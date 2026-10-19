/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package com.synexia.openrewrite.viewport.table;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.openrewrite.config.Environment;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaParser;

/**
 * Applies one recipe to several Java sources parsed together, with a class path, so that a recipe matching on
 * types (for example ChangeMethodName on a package-private class) sees the call sites in the other files. The
 * crate's M3PerFileApply parses one file without a class path; a rename across files needs the types.
 *
 * <p>Usage: {@code M3ClasspathApply <recipe-name> <class-path> <base-dir> <file>...}; the class path is
 * separated by {@code ;}, the files are relative to the base directory. The output lists {@code CHANGED} or
 * {@code UNCHANGED} per file; the exit status is non-zero on a parse or recipe error.</p>
 */
public final class M3ClasspathApply {
	private M3ClasspathApply () {
	}

	public static void main (String [] args) throws IOException {
		if (args.length < 4) {
			System.err.println ("usage: M3ClasspathApply <recipe-name> <class-path> <base-dir> <file>...");
			System.exit (2);
		}
		Recipe recipe = Environment.builder ().scanRuntimeClasspath ("com.synexia.openrewrite.viewport").build ().activateRecipes (args [0]);
		List<Path> classpath = Arrays.stream (args [1].split (";")).filter (entry -> !entry.isEmpty ()).map (Path::of).toList ();
		Path base = Path.of (args [2]);
		List<Path> files = new ArrayList<> ();
		for (int i = 3; i < args.length; i++) files.add (base.resolve (args [i]));
		ExecutionContext ctx = new InMemoryExecutionContext (failure -> {
			throw new IllegalStateException (failure);
		});
		List<SourceFile> sources = JavaParser.fromJavaVersion ().classpath (classpath).logCompilationWarningsAndErrors (false).build ()
			.parse (files, base, ctx).toList ();
		if (sources.size () != files.size ()) throw new IllegalStateException ("parsed " + sources.size () + " of " + files.size () + " files");
		List<Result> results = recipe.run (new InMemoryLargeSourceSet (sources), ctx).getChangeset ().getAllResults ();
		List<Path> changed = new ArrayList<> ();
		for (Result result : results) {
			if (result.getAfter () == null) throw new IllegalStateException ("a recipe deleted " + result.getBefore ().getSourcePath ());
			Path file = base.resolve (result.getAfter ().getSourcePath ());
			Files.writeString (file, result.getAfter ().printAll (), StandardCharsets.UTF_8);
			changed.add (file.normalize ());
		}
		for (Path file : files) {
			System.out.println ((changed.contains (file.normalize ()) ? "CHANGED\t" : "UNCHANGED\t") + base.relativize (file));
		}
	}
}
