# SWT: source tour and development cheat sheet

This guide helps readers navigate the experimental `hsoliwal/eclipse.platform.swt` fork. Read the [root README](../README.md) for the application example and upstream project context, and [CONTRIBUTING.md](../CONTRIBUTING.md) for contribution routing. Existing copyright, licensing and upstream identity remain unchanged.

SWT is a desktop widget toolkit with Java APIs and platform-specific native integration. A working Java compilation alone does not prove that the matching native libraries load, widgets render correctly, or resources are released.

## Architecture map

| Area | What it owns | Start here |
| --- | --- | --- |
| Main SWT bundle | Common and platform-specific Java APIs and implementation | [org.eclipse.swt](../bundles/org.eclipse.swt) |
| Platform integration | JNI declarations and native sources under `Eclipse SWT PI` | [Native development guide](../bundles/org.eclipse.swt/Readme.md) |
| Native fragments | OS/window-system/architecture-specific libraries | [binaries](../binaries) |
| Examples and snippets | Small application/API reproductions | [snippets](../examples/org.eclipse.swt.snippets) |
| Tests | Common tests and GTK, Win32 and Cocoa test fragments | [tests](../tests) |
| Development tools | JNI generation and other developer tooling | [SWT tools](../bundles/org.eclipse.swt.tools) |

Inside the main bundle, `Eclipse SWT/common/` contains shared code; `Eclipse SWT/gtk/`, `win32/` and `cocoa/` contain platform implementations. Do not assume a class with the same name has one implementation shared by every OS.

The [root POM](../pom.xml) selects a host-specific test fragment through its OS profiles. It clears inherited target environments for ordinary modules; the binaries module has its own packaging rules. Packaging fragments for multiple platforms is not the same as executing tests on all of them.

## Prerequisites and version source of truth

- Git and **Git LFS** when using the checked-in precompiled native binaries. Unresolved LFS pointers are not usable native libraries. Alternatively, build natives using the matching platform guide.
- A JDK and Maven matching the current workflow when reproducing CI. The [matrix](../.github/workflows/maven.yml) currently selects **Java 25**; the [reusable build workflow](../.github/workflows/build.yml) selects **Maven 3.9.16**.
- Network access for the Maven parent and target-platform dependencies. [.mvn/maven.config](../.mvn/maven.config) activates `build-individual-bundles`; the root POM supplies the Eclipse repository for parent resolution.
- Native tools/libraries and a usable GUI environment for the host backend. Follow the existing [Linux](../bundles/org.eclipse.swt/Readme.Linux.md), [Windows](../bundles/org.eclipse.swt/Readme.Win32.md) or [macOS](../bundles/org.eclipse.swt/Readme.macOS.md) instructions.

Older text that mentions Java 17 or 21 should not be taken as the current CI JDK selection. Recheck workflow and parent configuration at your revision; this guide does not redefine every bundle's runtime minimum.

## Command cheat sheet

Run from the repository root unless noted. These are source-checked commands, **not commands executed or test results obtained for this documentation change**.

```sh
git clone https://github.com/hsoliwal/eclipse.platform.swt.git
cd eclipse.platform.swt
git rev-parse HEAD
java -version
mvn -version

# Normal artifact build and tests (requires native/display prerequisites)
mvn clean verify

# Artifact build only; explicitly does not establish test success
mvn clean verify -DskipTests
```

For native work, first read the platform guide. This concrete example applies only to a suitably provisioned Linux x86_64 GTK host:

```sh
# From the repository root; builds natives as part of verification
mvn clean verify -Dnative=gtk.linux.x86_64
```

The current Linux CI adds backend-specific environment, dependencies and display setup around Maven. Follow [build.yml](../.github/workflows/build.yml) to reproduce a particular GTK/X11/Wayland lane rather than assuming the short command is equivalent to the full matrix.

If a Java native declaration changes, regenerate glue using the existing source-defined command:

```sh
mvn --non-recursive -Pjni-generator exec:exec@run-jni-generator
```

This command changes generated sources; it is not a read-only check. Review the generated diff with the Java change. Never hand-edit generated `os.c`, stats or structs files, and do not commit locally built native binaries.

## Worked exploration: the event loop

[Snippet1.java](../examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet1.java) is the smallest source tour:

1. `Display` establishes the application's UI/event context.
2. `Shell` creates a top-level window on that display; the snippet sets its title and opens it.
3. The loop calls `readAndDispatch()`; when no event is ready, `sleep()` waits rather than busy-spinning.
4. Closing/disposal ends the loop, and the standalone application disposes the display it created.

Use the README's Oomph setup and the [snippet-running instructions](../bundles/org.eclipse.swt/Readme.md) to launch it in a configured workspace. Check opening, repainting, resizing and closing on the actual backend. This is a proposed exploration, not a recorded native run.

Next read the README's button/text example to see listeners and layout data added around that lifecycle. Keep UI operations on the display thread. When scheduling work from a background thread, use the appropriate display dispatch API and account for controls that may have been disposed before the runnable executes.

Resource ownership matters: release application-created `GC` and `Image` objects, and do not dispose borrowed/framework-owned resources. The standalone snippet owns its display; an Eclipse workbench plug-in generally does not own the workbench display.

## UI dispatch and resource ownership in a real interaction

Consider a view that displays a background calculation's result. The [GTK Display implementation and API documentation](../bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Display.java) explain the dispatch boundary; check the corresponding platform implementation when investigating a backend-specific failure.

1. Create the controls and capture the owning display while on its UI thread.
2. Perform the calculation away from UI code. Return ordinary data, not a control or a live graphics context to manipulate from the worker.
3. Schedule the short presentation update with `asyncExec`. It queues work and returns without waiting for the update.
4. Inside that runnable, recheck the target control's disposal state before accessing it. The shell may have closed between scheduling and execution.
5. Coordinate application shutdown with outstanding workers. Calling `asyncExec` on an already disposed display can fail; checking a control inside the callback does not solve that earlier scheduling boundary.

A minimal callback fragment, assuming `display` is still accepting work and `label` is an existing label owned by it, is:

```java
display.asyncExec(() -> {
    if (!label.isDisposed()) {
        label.setText(resultText);
    }
});
```

This is an illustrative fragment, not a complete application or an executed test. Do not obtain a control's display from arbitrary worker-side widget calls; capture the required ownership context during UI setup.

`syncExec` waits for the UI runnable to complete. If the UI thread is simultaneously waiting for that worker, the two waits can deadlock. Diagnose both thread stacks before replacing asynchronous work with synchronous calls. Also note that `Display.execute` can run immediately when called on the UI thread, while using asynchronous dispatch from other threads; it is not an unconditional delay mechanism.

### Owned versus borrowed graphics contexts

For an off-screen operation, an application-created image and GC need paired cleanup. A scoped pattern is:

```java
Image image = new Image(display, 32, 32);
try {
    GC gc = new GC(image);
    try {
        gc.drawRectangle(1, 1, 28, 28);
        // Consume the result while the image is alive.
    } finally {
        gc.dispose();
    }
} finally {
    image.dispose();
}
```

The fragment assumes the SWT graphics imports, a valid display and the appropriate UI context. It demonstrates ownership only; it was not run here and is not visible-screen capture evidence. If a control will keep using the image, its owner must retain it and dispose it after that use ends instead of copying this short scope unchanged. Conversely, a GC supplied by a paint event is borrowed: do not dispose it as though you constructed it.

### Debug the first failed boundary

- **Invalid thread access:** inspect the caller and the [Widget checks](../bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Widget.java), not just the final painting method.
- **Disposed widget in delayed work:** put the liveness check inside the UI callback and review close/cancel ordering.
- **Disposed display while scheduling:** coordinate worker cancellation/shutdown; a callback-only guard is too late.
- **Frozen UI with a worker waiting:** capture both stacks and look for a `syncExec`/join or lock cycle.
- **Missing image or resource growth:** trace the creator, current consumer and final disposer separately; neither an early disposal nor a missing disposal is repaired by repainting.

Use the common [Display tests](../tests/org.eclipse.swt.tests/JUnit%20Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_widgets_Display.java), [GC tests](../tests/org.eclipse.swt.tests/JUnit%20Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_graphics_GC.java) and [Image tests](../tests/org.eclipse.swt.tests/JUnit%20Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_graphics_Image.java) to locate existing lifecycle assertions. A useful regression sequence covers normal completion, closing before dispatch, cancellation during work and repeated open/close cycles. Run the appropriate native/UI harness and record failures/skips; a source review of these paths is not a runtime qualification.

## Troubleshooting by failure boundary

| Symptom | First checks |
| --- | --- |
| Missing native library / architecture mismatch | Matching native fragment, resolved LFS objects, OS and CPU architecture |
| Parent or p2 resolution failure | Maven settings, network, selected target and repository availability |
| UI harness/display failure | Native dependencies and actual display/backend configuration |
| Different result on GTK versus Cocoa/Win32 | Platform-specific implementation and the corresponding test fragment |
| JNI generated-file check fails | Java declaration/annotation change and regenerated glue diff |
| No Maven run for a documentation PR | Workflow path filters; absence is not a test pass |

## Evidence and contribution checklist

Record revision, JDK/Maven, OS/architecture, SWT backend, display scale, exact command, test counts and failures. Report unexecuted or unsupported platforms explicitly. The Maven workflow excludes Markdown/docs-only changes; that saves a build but provides no new runtime evidence.

For rendering/resource work, use the repository's automated acceptance gates. Visible-screen capture, pixel assertions and CPU/heap/native-memory measurements are distinct evidence. A `Control.print` image is not a substitute for screen capture; a blank, stale or missing image is not a pass. Do not replace those checks with manual screenshot approval.

Fork research profiles are opt-in tooling and do not replace ordinary SWT API/JNI, native rendering, resource-lifetime and platform tests. Keep proposed behavior separate from measured results. Preserve notices and contributor requirements, and keep the change focused on its actual owning component.
