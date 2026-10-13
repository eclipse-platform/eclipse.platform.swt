/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
import java.io.*;
import java.lang.management.*;
import java.nio.charset.*;

/**
 * Process counters for the List/Combo bench: Java heap used (after GC), process
 * private bytes, kernel handle count, GDI and USER object counts. The process
 * counters are read by one PowerShell call per sample (about 1-2 s), so samples
 * are taken only at checkpoints, never inside a timed section.
 */
final class ProcStat {
	final long heapUsed;
	final long privateBytes;
	final long handles;
	final long gdi;
	final long user;

	private ProcStat (long heapUsed, long privateBytes, long handles, long gdi, long user) {
		this.heapUsed = heapUsed;
		this.privateBytes = privateBytes;
		this.handles = handles;
		this.gdi = gdi;
		this.user = user;
	}

	static ProcStat sample () {
		System.gc ();
		System.gc ();
		MemoryMXBean memory = ManagementFactory.getMemoryMXBean ();
		long heap = memory.getHeapMemoryUsage ().getUsed ();
		long pid = ProcessHandle.current ().pid ();
		String script = "Add-Type -Name G -Namespace W -MemberDefinition '[DllImport(\"user32.dll\")] public static extern int GetGuiResources(IntPtr h, int f);';"
			+ "$p=Get-Process -Id " + pid + ";"
			+ "'{0} {1} {2} {3}' -f $p.PrivateMemorySize64,$p.HandleCount,[W.G]::GetGuiResources($p.Handle,0),[W.G]::GetGuiResources($p.Handle,1)";
		long [] values = {-1, -1, -1, -1};
		try {
			Process process = new ProcessBuilder ("powershell", "-NoProfile", "-NonInteractive", "-EncodedCommand",
				java.util.Base64.getEncoder ().encodeToString (script.getBytes (StandardCharsets.UTF_16LE)))
				.redirectErrorStream (true).start ();
			String line;
			try (BufferedReader reader = new BufferedReader (new InputStreamReader (process.getInputStream (), StandardCharsets.UTF_8))) {
				String last = null;
				while ((line = reader.readLine ()) != null) {
					if (!line.isBlank ()) last = line.trim ();
				}
				line = last;
			}
			process.waitFor ();
			if (line != null) {
				String [] parts = line.split (" ");
				for (int i = 0; i < Math.min (parts.length, 4); i++) values [i] = Long.parseLong (parts [i]);
			}
		} catch (IOException | InterruptedException | NumberFormatException e) {
			// counters stay -1: reported as unavailable, never guessed
		}
		return new ProcStat (heap, values [0], values [1], values [2], values [3]);
	}

	static String header () {
		return "heap_mb\tprivate_mb\thandles\tgdi\tuser";
	}

	String row () {
		return mb (heapUsed) + "\t" + mb (privateBytes) + "\t" + handles + "\t" + gdi + "\t" + user;
	}

	private static String mb (long bytes) {
		return bytes < 0 ? "NA" : String.format ("%.1f", bytes / 1048576.0);
	}
}
