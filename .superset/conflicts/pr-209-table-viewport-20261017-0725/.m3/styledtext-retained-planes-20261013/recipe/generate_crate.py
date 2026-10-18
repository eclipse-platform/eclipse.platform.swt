"""Seal the StyledText retained-planes leaf part A (epoch 4, preimages = epoch 3 postimages at SWT b07f472fb7) as an M3HashPinnedJavaSnapshotRecipe crate.

Usage: python -B generate_crate.py <swt-repo-root> <base-commit>

Writes resources/com/synexia/rewrite/hash-pinned-java/<CRATE>/{manifest.tsv,N.java}
next to this script. Preimages are the base commit's repository (LF) bytes;
postimages are the working-tree sources normalized to LF. Hashes are SHA-256 of
the UTF-8 text, the canonical engine's identity rule.
"""
import hashlib
import pathlib
import subprocess
import sys

CRATE = "swt-styledtext-retained-planes-20261013"
TARGETS = sorted([
    "bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common/org/eclipse/swt/custom/StyledText.java",
    "bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common/org/eclipse/swt/custom/StyledTextRenderer.java",
    "tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit/AllWidgetTests.java",
    "tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_custom_StyledText_RoundTrip.java",
])


def sha(text):
    return hashlib.sha256(text.encode("utf-8")).hexdigest()


def main():
    repo = pathlib.Path(sys.argv[1])
    base = sys.argv[2]
    out = pathlib.Path(__file__).resolve().parent / "resources/com/synexia/rewrite/hash-pinned-java" / CRATE
    out.mkdir(parents=True, exist_ok=True)
    rows = []
    for index, path in enumerate(TARGETS, 1):
        shown = subprocess.run(["git", "-C", str(repo), "show", f"{base}:{path}"], capture_output=True)
        before = "ABSENT" if shown.returncode != 0 else sha(shown.stdout.decode("utf-8").replace("\r\n", "\n"))
        after_text = (repo / path).read_bytes().decode("utf-8").replace("\r\n", "\n")
        template = f"{index}.java"
        (out / template).write_bytes(after_text.encode("utf-8"))
        rows.append("\t".join([path, before, sha(after_text), template]))
    (out / "manifest.tsv").write_bytes(("\n".join(rows) + "\n").encode("utf-8"))
    print("\n".join(rows))


if __name__ == "__main__":
    main()
