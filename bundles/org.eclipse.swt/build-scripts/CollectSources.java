/*******************************************************************************
 * Copyright (c) 2024, 2024 Hannes Wellmann and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Hannes Wellmann - initial API and implementation
 *******************************************************************************/
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Script to collect the SWT native sources for the specified native fragment.
 * <p>
 * In order to be able to launch this directly as source script <b>only
 * reference standard java classes!</b>
 * </p>
 */
public class CollectSources {

	private record ScriptEnvironment(Path swtProjectRoot, Path targetDirectory, String ws, String arch) {
		private static ScriptEnvironment read(String[] args) {
			if (args.length != 2) {
				throw new IllegalArgumentException("task and target directory must be defined as only argument");
			}
			Path swtProjectRoot = Path.of("").toAbsolutePath();
			if (!swtProjectRoot.endsWith(Path.of("bundles/org.eclipse.swt"))) { // Consistency check
				throw new IllegalStateException("Sript must be excuted from org.eclipse.swt project");
			}
			Path targetDirectory = Path.of(args[1]);
			String ws = System.getProperty("ws");
			String arch = System.getProperty("arch");
			return new ScriptEnvironment(swtProjectRoot, targetDirectory, ws, arch);
		}
	}

	public static void main(String[] args) throws Exception {
		ScriptEnvironment env = ScriptEnvironment.read(args);
		switch (args[0]) {
		case "-nativeSources":
			collectNativeSources(env);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + args[0]);
		}
	}

	private static void collectNativeSources(ScriptEnvironment env) throws IOException {
		Path props = env.swtProjectRoot.resolve("nativeSourceFolders.properties");
		Map<String, String> sources = loadProperty(props);

		String commonSources = sources.get("src_common");
		String nativeSources = sources.get("src_" + env.ws);
		List<String> allSources = Arrays.asList((commonSources + "," + nativeSources).split(","));
		System.out.println("Copy " + allSources.size() + " java source folders for " + env.ws + "." + env.arch);
		copySubDirectories(env.swtProjectRoot, allSources, env.targetDirectory);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, String> loadProperty(Path path) throws IOException {
		try (InputStream in = Files.newInputStream(path)) {
			Properties props = new Properties();
			props.load(in);
			return (Map) props;
		}
	}

	private static void copySubDirectories(Path root, Collection<String> allSources, Path target) throws IOException {
		System.out.println("from " + root + "\nto " + target);
		for (String srcPath : allSources) {
			Path srcFolder = root.resolve(srcPath);
			try (var files = Files.walk(srcFolder).filter(Files::isRegularFile)) {
				for (Path sourceFile : (Iterable<Path>) files::iterator) {
					Path targetFile = target.resolve(srcFolder.relativize(sourceFile));
					Files.createDirectories(targetFile.getParent());
					Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}
}
