#!/bin/sh
# Compiles the canonical Synexia recipe owner (read-only source) and this crate's
# runner, exports the base preimages and executes the fixed-point/refusal proof.
# usage: verify.sh <swt-repo-root> <base-commit> <synexia-recipes-src> <rewrite-classpath-file> <work-dir>
# env:   JAVA_HOME (JDK 21)
set -eu
repo=$1; base=$2; owner=$3; cpfile=$4; work=$5
here=$(cd "$(dirname "$0")" && pwd)
w() { cygpath -w "$1" 2>/dev/null || echo "$1"; }
rm -rf "$work"; mkdir -p "$work/classes" "$work/pre"
cp=$(cat "$cpfile")
"$JAVA_HOME/bin/javac" -nowarn -d "$(w "$work/classes")" -cp "$cp" \
	"$(w "$owner")"/com/synexia/rewrite/*.java "$(w "$here/ApplyStyledTextVisibilityGateRecipe.java")"
python -B - "$here/resources/com/synexia/rewrite/hash-pinned-java/swt-styledtext-visibility-gate-20261013/manifest.tsv" "$repo" "$base" "$work/pre" <<'EOF'
import pathlib, subprocess, sys
manifest, repo, base, out = sys.argv[1:5]
for line in pathlib.Path(manifest).read_text(encoding="utf-8").splitlines():
    path, before = line.split("\t")[:2]
    if before == "ABSENT":
        continue
    data = subprocess.run(["git", "-C", repo, "show", f"{base}:{path}"], check=True, capture_output=True).stdout
    target = pathlib.Path(out, path)
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_bytes(data.replace(b"\r\n", b"\n"))
EOF
"$JAVA_HOME/bin/java" -cp "$(w "$work/classes");$(w "$here/resources");$cp" ApplyStyledTextVisibilityGateRecipe "$(w "$work/pre")"
