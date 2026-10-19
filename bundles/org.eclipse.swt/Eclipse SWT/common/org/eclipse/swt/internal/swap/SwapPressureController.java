// SPDX-License-Identifier: Apache-2.0
// Ported from hsoliwal/com.synexia 46e296ad537c6ecbd62b9783874e8f1714e278a9 synexia-mindex/runtime/src/main/java/com/synexia/mindex/swap/SwapPressureController.java; package renamed; the readjust overloads for the unported ShardedTieredSwapCache and MIndexAstSwapArchive are removed. Notice: docs/m3/item-residency/THIRD_PARTY_NOTICES.md
package org.eclipse.swt.internal.swap;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/** Caller-driven adaptive control loop for optional MIndex swap residency. */
public final class SwapPressureController {
	@FunctionalInterface public interface Source { Sample sample() throws IOException; }

	public record Sample(long heapUsedBytes, long heapMaxBytes, long cgroupUsedBytes,
			long cgroupMaxBytes, double psiSomeAvg10, double psiFullAvg10) {
		public Sample {
			if (heapUsedBytes < 0 || heapMaxBytes <= 0 || heapUsedBytes > heapMaxBytes)
				throw new IllegalArgumentException("invalid heap sample");
			boolean absent = cgroupUsedBytes == -1 && cgroupMaxBytes == -1;
			if (!absent && (cgroupUsedBytes < 0 || cgroupMaxBytes <= 0))
				throw new IllegalArgumentException("invalid cgroup sample");
			requirePsi(psiSomeAvg10); requirePsi(psiFullAvg10);
		}
		public boolean hasCgroupLimit() { return cgroupUsedBytes >= 0 && cgroupMaxBytes > 0; }
		public boolean hasPsi() { return psiSomeAvg10 >= 0 || psiFullAvg10 >= 0; }
		private static void requirePsi(double value) {
			if (value != -1.0 && (!Double.isFinite(value) || value < 0 || value > 100))
				throw new IllegalArgumentException("invalid PSI");
		}
	}

	public record Decision(Sample sample, double heapRatio, double cgroupRatio, double psiRatio,
			double rawRatio, double effectiveRatio, SwapBudgetPlane.Snapshot snapshot) {
		public Decision {
			Objects.requireNonNull(sample); Objects.requireNonNull(snapshot);
			for (double value : new double[] {heapRatio, cgroupRatio, psiRatio, rawRatio, effectiveRatio})
				requireRatio(value);
			if (Double.compare(snapshot.ratio(), effectiveRatio) != 0)
				throw new IllegalArgumentException("snapshot ratio mismatch");
		}
	}

	private final SwapBudgetPlane plane;
	private final Source source;
	private final double psiSoftStart, psiHardStop, recoveryStep;
	private double lastRatio = 1.0;

	public SwapPressureController(SwapBudgetPlane plane, Source source,
			double psiSoftStart, double psiHardStop, double recoveryStep) {
		this.plane = Objects.requireNonNull(plane); this.source = Objects.requireNonNull(source);
		if (!(psiSoftStart >= 0 && psiSoftStart < psiHardStop && psiHardStop <= 100))
			throw new IllegalArgumentException("invalid PSI thresholds");
		if (!Double.isFinite(recoveryStep) || recoveryStep <= 0 || recoveryStep > 1)
			throw new IllegalArgumentException("invalid recovery step");
		this.psiSoftStart = psiSoftStart; this.psiHardStop = psiHardStop;
		this.recoveryStep = recoveryStep;
	}

	public static SwapPressureController system(SwapBudgetPlane plane) {
		return new SwapPressureController(plane, systemSource(), 2.0, 20.0, 0.125);
	}
	public static Source systemSource() {
		return systemSource(Path.of("/proc/self/cgroup"), Path.of("/sys/fs/cgroup"));
	}
	public static Source systemSource(Path procSelfCgroup, Path cgroupRoot) {
		return new SystemSource(procSelfCgroup, cgroupRoot);
	}

	/** Worst availability wins; shrink is immediate while recovery is rate-limited. */
	public synchronized Decision decide() throws IOException {
		Sample sample = Objects.requireNonNull(source.sample());
		double heap = plane.ratio(sample.heapUsedBytes(), sample.heapMaxBytes());
		double cgroup = sample.hasCgroupLimit()
				? plane.ratio(Math.min(sample.cgroupUsedBytes(), sample.cgroupMaxBytes()),
						sample.cgroupMaxBytes()) : 1.0;
		double psi = psiRatio(sample);
		double raw = Math.min(heap, Math.min(cgroup, psi));
		double effective = raw < lastRatio ? raw : Math.min(raw, lastRatio + recoveryStep);
		lastRatio = effective;
		return new Decision(sample, heap, cgroup, psi, raw, effective, plane.snapshot(effective));
	}

	public synchronized TieredSwapCache.Pressure readjust(TieredSwapCache<?> cache) throws IOException {
		return Objects.requireNonNull(cache).readjust(decide().snapshot());
	}
	/** Emergency fail-safe plane used when telemetry itself cannot be sampled. */
	public SwapBudgetPlane.Snapshot zeroResidencySnapshot() {
		return plane.snapshot(0.0);
	}

	public synchronized double lastRatio() { return lastRatio; }

	private double psiRatio(Sample sample) {
		if (!sample.hasPsi()) return 1.0;
		double some = sample.psiSomeAvg10() < 0 ? 0 : sample.psiSomeAvg10();
		double full = sample.psiFullAvg10() < 0 ? 0 : sample.psiFullAvg10();
		double pressure = Math.max(some, full);
		if (pressure <= psiSoftStart) return 1.0;
		if (pressure >= psiHardStop) return 0.0;
		return (psiHardStop - pressure) / (psiHardStop - psiSoftStart);
	}
	private static void requireRatio(double ratio) {
		if (!Double.isFinite(ratio) || ratio < 0 || ratio > 1) throw new IllegalArgumentException("ratio");
	}

	private static final class SystemSource implements Source {
		private final Path proc, root;
		SystemSource(Path proc, Path root) {
			this.proc = Objects.requireNonNull(proc);
			this.root = Objects.requireNonNull(root).toAbsolutePath().normalize();
		}
		@Override public Sample sample() {
			MemoryUsage heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
			long max = heap.getMax() > 0 ? heap.getMax() : heap.getCommitted();
			if (max <= 0) max = Runtime.getRuntime().maxMemory();
			long used = Math.min(heap.getUsed(), max);
			Cgroup cgroup = readCgroup();
			return new Sample(used, max, cgroup.used, cgroup.max, cgroup.some, cgroup.full);
		}
		private Cgroup readCgroup() {
			try {
				Path dir = resolve();
				if (dir == null) return Cgroup.unavailable();
				double[] psi = readPsi(dir.resolve("memory.pressure"));
				String maxText = read(dir.resolve("memory.max"));
				String usedText = read(dir.resolve("memory.current"));
				if (maxText == null || usedText == null || maxText.equals("max"))
					return new Cgroup(-1, -1, psi[0], psi[1]);
				long max = Long.parseLong(maxText), used = Long.parseLong(usedText);
				return max > 0 && used >= 0 ? new Cgroup(used, max, psi[0], psi[1])
						: new Cgroup(-1, -1, psi[0], psi[1]);
			} catch (IOException | NumberFormatException ignored) { return Cgroup.unavailable(); }
		}
		private Path resolve() throws IOException {
			if (!Files.isRegularFile(proc) || !Files.isDirectory(root)) return null;
			for (String line : Files.readAllLines(proc)) {
				if (!line.startsWith("0::")) continue;
				String relative = line.substring(3).strip();
				while (relative.startsWith("/")) relative = relative.substring(1);
				Path resolved = root.resolve(relative).normalize();
				return resolved.startsWith(root) ? resolved : null;
			}
			return null;
		}
		private static String read(Path path) throws IOException {
			if (!Files.isRegularFile(path)) return null;
			String value = Files.readString(path).strip();
			return value.isEmpty() ? null : value;
		}
		private static double[] readPsi(Path path) throws IOException {
			if (!Files.isRegularFile(path)) return new double[] {-1, -1};
			double some = -1, full = -1;
			for (String line : Files.readAllLines(path)) {
				if (line.startsWith("some ")) some = avg10(line);
				else if (line.startsWith("full ")) full = avg10(line);
			}
			return new double[] {some, full};
		}
		private static double avg10(String line) {
			for (String token : line.split("\\s+")) if (token.startsWith("avg10=")) {
				double value = Double.parseDouble(token.substring(6));
				if (!Double.isFinite(value) || value < 0 || value > 100) throw new NumberFormatException();
				return value;
			}
			return -1;
		}
	}
	private record Cgroup(long used, long max, double some, double full) {
		static Cgroup unavailable() { return new Cgroup(-1, -1, -1, -1); }
	}
}
