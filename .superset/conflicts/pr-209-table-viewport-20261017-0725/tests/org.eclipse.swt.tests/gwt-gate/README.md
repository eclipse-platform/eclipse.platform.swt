# GWT compile gate for the shared viewport layer

Directive (2026-10-13): the web UI is generated from Java, with Eclipse RAP (RWT) on the server and GWT compiling the client-side widget logic to JavaScript. The viewport math, packed selection and row-extent index therefore have to compile under GWT and behave the same as in the JVM. This gate checks that.

`run_gate.py` copies the pure-Java classes below into a scratch GWT module, compiles them with the GWT compiler in `-strict` mode, then runs the same seeded workload (`GateWorkload`: 8 seeds x 400 steps over `VirtualListModel`, `VirtualListWidth` and `VirtualRowExtents`) once in the JVM and once as the generated JavaScript in Node. It fails unless every digest line is identical.

Classes under the gate: `internal/ViewportRuntime`, `widgets/VirtualViewportPlanner`, `VirtualSelectionModel`, `VirtualScrollMetrics`, `VirtualListModel`, `VirtualListWidth`, `VirtualRowExtents`. A new shared-layer class joins the gate by adding it to `SOURCES` in `run_gate.py` and to the workload.

```text
python run_gate.py --swt-root <repo root> --gwt-lib <dir with gwt-dev, gwt-user and deps> --work <new dir> \
       [--java-home <JDK 17+>] [--node <node>]
```

Tested with GWT 2.12.2 (`org.gwtproject:gwt-dev` and `gwt-user`), JDK 21, Node 25. Obtain the jars with Maven (`dependency:copy-dependencies`); the script ignores any `ecj-*.jar`, because an older ecj in front of `gwt-dev` breaks the compiler with `NoSuchMethodError: SourceTypeBinding.isRecord()`.

## What the gate has already found

- `String[].clone()` is not supported (`VirtualListModel`): replaced by `Arrays.copyOf`.
- `Objects.checkIndex` (Java 9 API) is not in GWT's JRE emulation (`ViewportRuntime.Selection.rangeStart/rangeEndExclusive`): replaced by an explicit check with the same exception type and message.
- GWT does not wrap `int` `+`, `-` or `*` to 32 bits. The workload digest therefore folds through masked `long` arithmetic, which is exact in both engines. Shared-layer code must not rely on `int` overflow wrapping; the atoms keep sums in `long` and detect overflow with `Math.addExact`.

## Limits

- It compiles and runs the model classes only. No SWT, RWT or RAP class is involved, and no browser is used (the generated script runs in Node with a minimal `window`/`document` stub).
- JNI accelerators have no web equivalent: the GWT build always takes the pure-Java authority path, so every JNI atom needs its pure-Java twin in this gate.
