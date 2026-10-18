"""jni_check.py - compare `native` declarations in SWT Java sources with JNI exports of official DLLs.

usage: python -I jni_check.py <dll-exports.txt> <out.tsv> <source-root>...
Writes one row per native (class, method, overloads_declared, exports_found, status).
status: OK | MISSING (no export with that short name) | OVERLOAD_COUNT (fewer exports than overloads).
Exit code 0 always; the summary line on stdout carries the counts.
"""
import os
import re
import sys

NATIVE = re.compile(r"\bnative\b[^;{}()]*?\b([A-Za-z_$][\w$]*)\s*\(([^)]*)\)\s*;", re.S)
PKG = re.compile(r"^\s*package\s+([\w.]+)\s*;", re.M)
COMMENT = re.compile(r"/\*.*?\*/|//[^\n]*", re.S)


def mangle(s):
    return s.replace("_", "_1").replace(".", "_")


def scan(roots):
    decls = {}
    for root in roots:
        for d, _, files in os.walk(root):
            for f in files:
                if not f.endswith(".java"):
                    continue
                text = open(os.path.join(d, f), encoding="utf-8", errors="replace").read()
                body = COMMENT.sub(" ", text)
                m = PKG.search(body)
                if not m or " native " not in body:
                    continue
                cls = f[:-5]
                for nm in NATIVE.finditer(body):
                    key = "Java_%s_%s_%s" % (mangle(m.group(1)), mangle(cls), mangle(nm.group(1)))
                    decls.setdefault(key, [m.group(1), cls, nm.group(1), 0])[3] += 1
    return decls


def main():
    exports_file, out, roots = sys.argv[1], sys.argv[2], sys.argv[3:]
    counts = {}
    for line in open(exports_file, encoding="ascii", errors="replace"):
        short = line.strip().split("__", 1)[0]
        if short:
            counts[short] = counts.get(short, 0) + 1
    decls = scan(roots)
    rows, missing, overload = [], 0, 0
    for key in sorted(decls):
        pkg, cls, meth, n = decls[key]
        found = counts.get(key, 0)
        status = "OK" if found >= n else ("MISSING" if found == 0 else "OVERLOAD_COUNT")
        missing += status == "MISSING"
        overload += status == "OVERLOAD_COUNT"
        rows.append("\t".join([pkg + "." + cls, meth, str(n), str(found), status]))
    with open(out, "w", encoding="utf-8", newline="\n") as fh:
        fh.write("class\tmethod\toverloads_declared\texports_found\tstatus\n")
        fh.write("\n".join(rows) + "\n")
    print("natives=%d ok=%d missing=%d overload_count=%d" % (len(rows), len(rows) - missing - overload, missing, overload))


if __name__ == "__main__":
    main()
