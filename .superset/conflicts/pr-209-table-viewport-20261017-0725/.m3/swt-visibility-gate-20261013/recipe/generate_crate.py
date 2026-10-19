"""Seal the SWT visibility gate leaf as an M3HashPinnedJavaSnapshotRecipe crate.

Reused unchanged from swt-gpu-retained-planes-20261013 (same author, Apache-2.0).

Usage: python -B generate_crate.py <swt-repo-root> <base-commit> <out-dir>

Targets are every .java file under bundles/ and tests/ that differs between <base-commit>
and the working tree (git diff --name-only plus untracked additions). Writes
<out-dir>/{manifest.tsv, N.java, pre/N.java.txt, .gitattributes}. Preimages
are the base commit's repository (LF) bytes; postimages are the working-tree
sources normalized to LF. Hashes are SHA-256 of the UTF-8 text, the canonical
engine's identity rule.
"""
import hashlib
import pathlib
import subprocess
import sys


def sha(text):
    return hashlib.sha256(text.encode("utf-8")).hexdigest()


def git(repo, *args):
    return subprocess.run(["git", "-C", str(repo), *args], capture_output=True, check=True).stdout.decode("utf-8")


def targets(repo, base):
    changed = git(repo, "diff", "--name-only", base, "--", "bundles", "tests").splitlines()
    added = git(repo, "ls-files", "--others", "--exclude-standard", "--", "bundles", "tests").splitlines()
    return sorted(p for p in set(changed + added) if p.endswith(".java"))


def main():
    repo, base, out = pathlib.Path(sys.argv[1]), sys.argv[2], pathlib.Path(sys.argv[3])
    (out / "pre").mkdir(parents=True, exist_ok=True)
    rows = []
    for index, path in enumerate(targets(repo, base), 1):
        shown = subprocess.run(["git", "-C", str(repo), "show", f"{base}:{path}"], capture_output=True)
        if shown.returncode != 0:
            before = "ABSENT"
        else:
            pre = shown.stdout.decode("utf-8").replace("\r\n", "\n")
            before = sha(pre)
            (out / "pre" / f"{index}.java.txt").write_bytes(pre.encode("utf-8"))
        after_text = (repo / path).read_bytes().decode("utf-8").replace("\r\n", "\n")
        (out / f"{index}.java").write_bytes(after_text.encode("utf-8"))
        rows.append("\t".join([path, before, sha(after_text), f"{index}.java"]))
    (out / "manifest.tsv").write_bytes(("\n".join(rows) + "\n").encode("utf-8"))
    (out / ".gitattributes").write_bytes(b"* text eol=lf\n")
    print("\n".join(rows))


if __name__ == "__main__":
    main()
