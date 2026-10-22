// SPDX-License-Identifier: Apache-2.0
package com.synexia.rewrite;

/**
 * Maximum authority required by one M3 recipe/pass.
 *
 * <p>Promotion is monotonic. VISIBILITY is intentionally distinct from PACKAGE: changing one
 * declaration's accessibility is not equivalent to coordinating multiple files in one package.
 */
public enum M3EditScope {
    FILE(0),
    VISIBILITY(1),
    PACKAGE(2),
    MODULE(3),
    MULTI_MODULE(4),
    LIBRARY_API(5);

    private final int level;

    M3EditScope(int level) {
        this.level = level;
    }

    public boolean permits(M3EditScope required) {
        return level >= required.level;
    }

    public M3EditScope promote(M3EditScope other) {
        return level >= other.level ? this : other;
    }

    public M3EditScope next() {
        return switch (this) {
            case FILE -> VISIBILITY;
            case VISIBILITY -> PACKAGE;
            case PACKAGE -> MODULE;
            case MODULE -> MULTI_MODULE;
            case MULTI_MODULE, LIBRARY_API -> LIBRARY_API;
        };
    }
}
