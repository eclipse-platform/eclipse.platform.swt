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
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

import org.jacoco.core.analysis.*;
import org.jacoco.core.tools.*;

/**
 * Line and branch coverage of the render-modes classes from JaCoCo execution data (brief rule 15): merges the given
 * <code>.exec</code> files, analyzes the selected classes of the SWT jars and writes a TSV (class, lines covered/total,
 * branches covered/total, missed line numbers) plus a total line.
 * <p>
 * Usage: <code>CoverageReport &lt;out.tsv&gt; &lt;class-prefixes, comma separated&gt; &lt;jar&gt;[;&lt;jar&gt;] &lt;exec&gt;...</code>
 * </p>
 */
public final class CoverageReport {
	public static void main (String [] args) throws IOException {
		Path out = Paths.get (args [0]);
		String [] prefixes = args [1].split (",");
		ExecFileLoader loader = new ExecFileLoader ();
		for (int i = 3; i < args.length; i++) loader.load (new File (args [i]));
		CoverageBuilder builder = new CoverageBuilder ();
		Analyzer analyzer = new Analyzer (loader.getExecutionDataStore (), builder);
		for (String jar : args [2].split (";")) {
			try (ZipFile zip = new ZipFile (jar)) {
				for (ZipEntry entry : Collections.list (zip.entries ())) {
					String name = entry.getName ();
					if (!name.endsWith (".class") || Arrays.stream (prefixes).noneMatch (name::startsWith)) continue;
					try (InputStream in = zip.getInputStream (entry)) {
						analyzer.analyzeClass (in, name);
					}
				}
			}
		}
		List<IClassCoverage> classes = new ArrayList<> (builder.getClasses ());
		classes.sort (Comparator.comparing (IClassCoverage::getName));
		List<String> lines = new ArrayList<> ();
		lines.add ("class\tlines_covered\tlines_total\tline_pct\tbranches_covered\tbranches_total\tbranch_pct\tmissed_lines");
		int lc = 0, lt = 0, bc = 0, bt = 0;
		for (IClassCoverage c : classes) {
			ICounter l = c.getLineCounter (), b = c.getBranchCounter ();
			StringBuilder missed = new StringBuilder ();
			for (int line = c.getFirstLine (); line <= c.getLastLine () && line > 0; line++) {
				int status = c.getLine (line).getStatus ();
				if (status == ICounter.NOT_COVERED || status == ICounter.PARTLY_COVERED) {
					missed.append (line).append (status == ICounter.PARTLY_COVERED ? "p" : "").append (' ');
				}
			}
			lines.add (c.getName () + "\t" + l.getCoveredCount () + "\t" + l.getTotalCount () + "\t" + pct (l) + "\t" + b.getCoveredCount () + "\t"
				+ b.getTotalCount () + "\t" + pct (b) + "\t" + missed.toString ().trim ());
			lc += l.getCoveredCount ();
			lt += l.getTotalCount ();
			bc += b.getCoveredCount ();
			bt += b.getTotalCount ();
		}
		lines.add (String.format (Locale.ROOT, "TOTAL\t%d\t%d\t%.1f\t%d\t%d\t%.1f\t-", lc, lt, lt == 0 ? 100.0 : 100.0 * lc / lt, bc, bt, bt == 0 ? 100.0 : 100.0 * bc / bt));
		Files.write (out, lines);
		System.out.println (lines.get (lines.size () - 1));
	}

	private static String pct (ICounter counter) {
		return counter.getTotalCount () == 0 ? "100.0" : String.format (Locale.ROOT, "%.1f", 100.0 * counter.getCoveredCount () / counter.getTotalCount ());
	}
}
