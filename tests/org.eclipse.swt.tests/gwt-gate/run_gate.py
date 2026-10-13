# Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
# SPDX-License-Identifier: Apache-2.0
"""GWT compile gate for the shared SWT viewport layer.

Copies the pure-Java viewport classes into a scratch GWT module, compiles them with the GWT
compiler in strict mode, runs the same seeded workload in the JVM and in the generated
JavaScript (Node), and fails unless every line of output is identical.

Usage: python run_gate.py --swt-root REPO --gwt-lib DIR --work NEW_DIR [--java-home DIR] [--node EXE]
DIR holds gwt-dev, gwt-user and their dependencies (any ecj-*.jar is ignored: gwt-dev bundles
its own JDT, and an older ecj in front of it breaks the compiler).
"""
import argparse
import os
import shutil
import subprocess
import sys

BUNDLE = os.path.join("bundles", "org.eclipse.swt", "Eclipse SWT", "common", "org", "eclipse", "swt")
SOURCES = [
    ("internal", "ViewportRuntime.java"),
    ("widgets", "VirtualViewportPlanner.java"),
    ("widgets", "VirtualSelectionModel.java"),
    ("widgets", "VirtualScrollMetrics.java"),
    ("widgets", "VirtualListModel.java"),
    ("widgets", "VirtualListWidth.java"),
    ("widgets", "VirtualRowExtents.java"),
]
MODULE = "org.eclipse.swt.SwtViewport"


def run(cmd, **kw):
    print("+", " ".join(cmd[:4]), "...", flush=True)
    return subprocess.run(cmd, check=False, capture_output=True, text=True, **kw)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--swt-root", required=True)
    ap.add_argument("--gwt-lib", required=True)
    ap.add_argument("--work", required=True)
    ap.add_argument("--java-home", default=os.environ.get("JAVA_HOME", ""))
    ap.add_argument("--node", default="node")
    a = ap.parse_args()
    here = os.path.dirname(os.path.abspath(__file__))
    if os.path.exists(a.work):
        sys.exit("work directory must not exist: " + a.work)
    tool = lambda n: os.path.join(a.java_home, "bin", n) if a.java_home else n
    src = os.path.join(a.work, "src")
    for pkg in ("widgets", "internal"):
        os.makedirs(os.path.join(src, "org", "eclipse", "swt", pkg))
    for pkg, name in SOURCES:
        shutil.copy(os.path.join(a.swt_root, BUNDLE, pkg, name), os.path.join(src, "org", "eclipse", "swt", pkg, name))
    gate_src = os.path.join(here, "src", "org", "eclipse", "swt")
    shutil.copy(os.path.join(gate_src, "SwtViewport.gwt.xml"), os.path.join(src, "org", "eclipse", "swt"))
    for name in ("GateEntry.java", "GateWorkload.java", "GateJava.java"):
        shutil.copy(os.path.join(gate_src, "widgets", name), os.path.join(src, "org", "eclipse", "swt", "widgets", name))
    jars = sorted(f for f in os.listdir(a.gwt_lib) if f.endswith(".jar") and not f.startswith("ecj-"))
    jars.sort(key=lambda f: (not f.startswith("gwt-dev"), not f.startswith("gwt-user"), f))
    cp = os.pathsep.join([src] + [os.path.join(a.gwt_lib, j) for j in jars])
    war, workdir = os.path.join(a.work, "war"), os.path.join(a.work, "gwt-work")
    os.makedirs(workdir)
    gwt = run([tool("java"), "-Xmx1400m", "-XX:+UseSerialGC", "-cp", cp, "com.google.gwt.dev.Compiler", "-strict",
               "-sourceLevel", "17", "-style", "PRETTY", "-war", war, "-workDir", workdir, "-XnoclassMetadata",
               "-nocompileReport", MODULE])
    print(gwt.stdout[-1500:])
    if gwt.returncode != 0 or "Compile of permutations succeeded" not in gwt.stdout:
        print(gwt.stderr[-1500:])
        sys.exit("GWT_GATE_FAIL: GWT compile did not succeed")
    classes = os.path.join(a.work, "classes")
    os.makedirs(classes)
    java_files = [os.path.join(src, "org", "eclipse", "swt", pkg, name) for pkg, name in SOURCES]
    java_files += [os.path.join(src, "org", "eclipse", "swt", "widgets", n) for n in ("GateWorkload.java", "GateJava.java")]
    jc = run([tool("javac"), "-Xlint:all", "-d", classes] + java_files)
    if jc.returncode != 0:
        print(jc.stdout, jc.stderr)
        sys.exit("GWT_GATE_FAIL: javac failed")
    jvm = run([tool("java"), "-cp", classes, "org.eclipse.swt.widgets.GateJava"])
    js = run([a.node, os.path.join(here, "run_node.mjs"), os.path.join(war, MODULE, MODULE + ".nocache.js")])
    if jvm.returncode != 0 or js.returncode != 0:
        print(jvm.stderr, js.stderr)
        sys.exit("GWT_GATE_FAIL: workload did not run")
    jvm_lines, js_lines = jvm.stdout.strip().splitlines(), js.stdout.strip().splitlines()
    if jvm_lines != js_lines or not jvm_lines:
        print("JVM:", *jvm_lines, sep="\n  ")
        print("JS:", *js_lines, sep="\n  ")
        sys.exit("GWT_GATE_FAIL: JVM and GWT JavaScript disagree")
    print("GWT_GATE_OK", len(jvm_lines), "workloads identical in the JVM and in GWT JavaScript")


if __name__ == "__main__":
    main()
