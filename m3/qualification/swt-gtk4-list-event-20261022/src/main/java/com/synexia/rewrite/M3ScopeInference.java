// SPDX-License-Identifier: Apache-2.0
package com.synexia.rewrite;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Narrowest physical target scope inferable from exact Java source paths. */
public final class M3ScopeInference {
    private M3ScopeInference() {}

    public static M3EditScope inferJavaTargets(List<String> targetPaths) {
        List<String> paths = List.copyOf(Objects.requireNonNull(targetPaths, "targetPaths"));
        if (paths.isEmpty()) throw new IllegalArgumentException("targetPaths");
        if (paths.size() == 1) {
            describeJavaTarget(paths.getFirst());
            return M3EditScope.FILE;
        }

        Set<String> modules = new HashSet<>();
        Set<String> packages = new HashSet<>();
        for (String path : paths) {
            JavaLocation location = describeJavaTarget(path);
            modules.add(location.module());
            packages.add(location.module() + "|" + location.packageName());
        }
        if (modules.size() > 1) return M3EditScope.MULTI_MODULE;
        if (packages.size() > 1) return M3EditScope.MODULE;
        return M3EditScope.PACKAGE;
    }

    /**
     * Path locality cannot infer declaration visibility or exported API authority.
     *
     * <p>VISIBILITY and LIBRARY_API must be explicitly declared by the recipe/work packet.
     */
    public static M3EditScope requireExplicit(
            M3EditScope inferred,
            M3EditScope declaredFloor) {
        return Objects.requireNonNull(inferred, "inferred")
                .promote(Objects.requireNonNull(declaredFloor, "declaredFloor"));
    }

    public static JavaLocation describeJavaTarget(String value) {
        String path = Objects.requireNonNull(value, "path").replace('\\', '/');
        if (path.isBlank()
                || path.startsWith("/")
                || path.indexOf('\0') >= 0
                || path.contains("/../")
                || path.startsWith("../")
                || path.endsWith("/..")
                || !path.endsWith(".java")) {
            throw new IllegalArgumentException("normalized relative Java path required: " + value);
        }

        String module;
        String relative;

        if (path.startsWith("src/")) {
            String[] parts = path.split("/", -1);
            if (parts.length >= 5 && "classes".equals(parts[3])) {
                module = parts[1];
                relative = String.join("/", java.util.Arrays.copyOfRange(parts, 4, parts.length));
                return location(module, relative, value);
            }
        }

        if (path.startsWith("test/")) {
            String[] parts = path.split("/", -1);
            // startsWith("test/") guarantees the family slot, including an empty slot.
            String testFamily = parts[1];
            module = "<jdk-test:" + testFamily + ">";
            relative = parts.length > 2
                    ? String.join("/", java.util.Arrays.copyOfRange(parts, 2, parts.length))
                    : parts[parts.length - 1];
            return location(module, relative, value);
        }

        int swtManual = path.indexOf("/ManualTests/");
        if (path.startsWith("tests/org.eclipse.swt.tests/") && swtManual >= 0) {
            module = path.substring(0, swtManual);
            relative = path.substring(swtManual + "/ManualTests/".length());
            return location(module, relative, value);
        }

        int swtJUnit = path.indexOf("/JUnit Tests/");
        if (path.startsWith("tests/org.eclipse.swt.tests/") && swtJUnit >= 0) {
            module = path.substring(0, swtJUnit);
            relative = path.substring(swtJUnit + "/JUnit Tests/".length());
            return location(module, relative, value);
        }

        if (path.startsWith("bundles/")) {
            String[] parts = path.split("/", -1);
            for (int index = 0; index + 2 < parts.length; index++) {
                if (!parts[index].startsWith("Eclipse SWT")) continue;
                module = String.join("/", java.util.Arrays.copyOfRange(parts, 0, index + 1));
                relative = String.join("/", java.util.Arrays.copyOfRange(parts, index + 2, parts.length));
                return location(module, relative, value);
            }
        }

        if ((path.startsWith("bundles/") || path.startsWith("tests/") || path.startsWith("widgets/")
                || path.startsWith("examples/"))
                && path.contains("/src/")) {
            int source = path.indexOf("/src/");
            module = path.substring(0, source);
            relative = path.substring(source + "/src/".length());
            return location(module, relative, value);
        }

        // Admit only the existing Draw2d PDE packages, retaining Windows path normalization.
        if (path.startsWith("org.eclipse.draw2d/src/org/eclipse/draw2d/")
                || path.startsWith("org.eclipse.draw2d.tests/src/org/eclipse/draw2d/test/")) {
            for (String part : path.split("/", -1)) {
                if (part.isBlank() || part.equals(".") || part.equals("..")
                        || part.chars().anyMatch(Character::isISOControl)) {
                    throw new IllegalArgumentException("normalized relative Java path required: " + value);
                }
            }
            int source = path.indexOf("/src/");
            return location(path.substring(0, source),
                    path.substring(source + "/src/".length()), value);
        }

        // The existing CPULLM JNI parity test has a legacy physical location.
        // Preserve its actual module and package; refuse all adjacent unknown paths.
        if (path.equals("cpullm-java/facet-release/tests/FacetExactCoverageChecks.java")) {
            return new JavaLocation("cpullm-java", "com.synexia.cpullm.facets");
        }

        // Lucene core uses Gradle's src/java and src/test roots instead of Maven paths.
        // Qualify only its Apache Lucene Java package; other Gradle paths still refuse.
        if (path.startsWith("lucene/core/src/java/org/apache/lucene/")
                || path.startsWith("lucene/core/src/test/org/apache/lucene/")) {
            int source = path.indexOf("/src/");
            String luceneModule = path.substring(0, source);
            String relativePath = path.substring(source + "/src/".length());
            relativePath = relativePath.startsWith("java/")
                    ? relativePath.substring("java/".length())
                    : relativePath.substring("test/".length());
            return location(luceneModule, relativePath, value);
        }

        // M3 core retains a deliberate legacy source/test layout rather than Maven's roots.
        // Resolve both to the same physical module/package without opening sibling legacy trees.
        String m3TextMain = "m3/core/src/com/m3/text/";
        String m3TextTest = "m3/core/test/com/m3/text/";
        if (path.startsWith(m3TextMain)) {
            return location("m3/core", "com/m3/text/" + path.substring(m3TextMain.length()), value);
        }
        if (path.startsWith(m3TextTest)) {
            return location("m3/core", "com/m3/text/" + path.substring(m3TextTest.length()), value);
        }

        int main = path.indexOf("/src/main/java/");
        int test = path.indexOf("/src/test/java/");
        int markerIndex = main >= 0 ? main : test;
        String marker = main >= 0 ? "/src/main/java/" : "/src/test/java/";
        if (markerIndex >= 0) {
            module = path.substring(0, markerIndex);
            relative = path.substring(markerIndex + marker.length());
        } else if (path.startsWith("src/main/java/")) {
            module = "<current>";
            relative = path.substring("src/main/java/".length());
        } else if (path.startsWith("src/test/java/")) {
            module = "<current>";
            relative = path.substring("src/test/java/".length());
        } else {
            throw new IllegalArgumentException("Java Maven source path required: " + value);
        }

        if (module.isBlank()) module = "<current>";
        return location(module, relative, value);
    }

    private static JavaLocation location(
            String module,
            String relative,
            String original) {
        // Private validated domain: every caller retains the public .java suffix and
        // at least that filename while extracting its layout-specific relative path.
        int slash = relative.lastIndexOf('/');
        String packageName = slash < 0
                ? "<default>"
                : relative.substring(0, slash).replace('/', '.');
        return new JavaLocation(module, packageName);
    }

    public record JavaLocation(String module, String packageName) {
        public JavaLocation {
            if (module == null || module.isBlank()) throw new IllegalArgumentException("module");
            if (packageName == null || packageName.isBlank()) throw new IllegalArgumentException("packageName");
        }
    }
}
