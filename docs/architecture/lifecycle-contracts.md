# SWT package and lifecycle contracts

Inspected source revision: `c05dff79c9cc737c9cffa501c8f5cef353141dc6` (14 October 2026).
This is a source-inspected contract guide, not a native execution report. Platform-specific observations below identify GTK explicitly; shared Java code and the classpath manifests establish the common boundaries. Remaining per-package gaps are retained in the [package ledger](package-ledger.md).

## Module boundary and source selection

The [SWT host manifest](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/META-INF/MANIFEST.MF) declares `org.eclipse.swt`, JavaSE-21, and the automatic module name `org.eclipse.swt`. It exports the widget, event, graphics, layout and integration API packages and also marks several exported internal packages `x-internal:=true`. Java visibility alone does not make an internal type a supported client API.

SWT is not three independent sets of application APIs. Shared sources are combined with a selected native implementation. The [GTK](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/.classpath_gtk), [Win32](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/.classpath_win32), and [Cocoa](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/.classpath_cocoa) source lists show the common/platform/emulated composition. For example GTK selects Cairo, GLX and emulated bidi/coolbar/taskbar sources, while Cocoa selects its own bridge plus emulated bidi/coolbar/expand/tooltip. Compile the selected set; do not put all definitions of `Display`, `Widget` or `Font` on one Java classpath.

The host's [build properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/build.properties) set `generateSourceBundle=false`, with sources intended for fragment source bundles. [The binaries reactor](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/pom.xml) names eight native fragments; the tree also contains a LoongArch64 fragment descriptor that is not in that POM's module list. Presence in the tree is not evidence of selection by the current build or CI matrix. The [module ledger](module-ledger.md) keeps all nine native-fragment directories visible.

## org.eclipse.swt.graphics: allocation, borrowing and disposal

`Resource` is the common base for device-associated graphics resources. Its [dispose implementation](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/common/org/eclipse/swt/graphics/Resource.java#L155) disables leak reporting, checks whether the device is available, invokes the concrete resource's destruction routine, updates device tracking, and clears its device reference. Repeated disposal is supported; disposal after device teardown is not a substitute for releasing an owned native handle while its device is live.

Ownership is the deciding rule:

- An application-created `Font` must be disposed. The [GTK Font implementation](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/graphics/Font.java#L159) calls `pango_font_description_free` and zeroes the handle. Its constructors reject null font data/name, empty font-data arrays, and negative heights as documented. The [Win32](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/win32/org/eclipse/swt/graphics/Font.java) and [Cocoa](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/cocoa/org/eclipse/swt/graphics/Font.java) declarations also require explicit disposal. Do not generalize newer Color behavior to Font.
- [Color](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/graphics/Color.java) does not require disposal at this revision. Device-free constructors are recommended by its class contract; calling `dispose()` remains supported for compatibility. Color components are restricted to 0–255.
- A font returned by [Device.getSystemFont](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/graphics/Device.java#L644) is borrowed from the system and must not be freed by the caller.
- A resource obtained through another owner, such as a JFace resource manager or image registry, must follow that owner's release protocol. Setting a resource on a widget is not proof that ownership moved to the widget.
- Native fields and helper factories marked `@noreference`, such as `Font.gtk_new`, are implementation details. That factory wraps a borrowed handle and suppresses leak reporting; it is not an application allocation API.

The leak callback installed with [Resource.setNonDisposeHandler](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/common/org/eclipse/swt/graphics/Resource.java#L253) runs on a different thread. It must not block or throw. The cleaner reports an unclosed resource; it does not perform the concrete native destruction on behalf of the application.

This source-backed rule differs from the existing short statement that Font is automatically disposed in the repository's general agent guide. The concrete type's ownership contract and current implementation are the evidence used here; that guide has not been edited in this documentation pass.

Minimal owned-font pattern, inside an existing live UI owner:

```java
Font font = new Font(display, "Sans", 12, SWT.NORMAL);
try {
    // Use font only while it is live; do not let a widget outlive it.
} finally {
    font.dispose();
}
```

This is an ownership illustration, not a font-availability or rendering guarantee.
The [Font test source](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests/JUnit%20Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_graphics_Font.java#L173) contains a create/dispose/isDisposed assertion. It was inspected, not executed.

## org.eclipse.swt.widgets: thread affinity and lifetime

`Display` owns the UI event processing context; `Widget` is the widget base, and `Composite` owns a control-child hierarchy. In the inspected [GTK Widget.checkWidget](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Widget.java#L517) implementation, normal widget access rejects an absent/disposed display or disposed widget with `ERROR_WIDGET_DISPOSED`, and rejects access from another display thread with `ERROR_THREAD_INVALID_ACCESS`. This is a widget-access rule, not a claim that every SWT value object or every Display method is UI-thread-only.

`Widget.dispose()` is idempotent for an already disposed widget. A live widget must be disposed from its valid UI thread. Its release path sends `SWT.Dispose` once, releases children, and then releases/destroys native state. The [Composite child-release loop](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Composite.java#L1779) releases children and skips children already disposed. The parent-child widget lifetime does not remove the need to account separately for independently allocated graphics resources.

The application owns a Display it creates for a standalone program. A workbench contributes an already managed Display and widget hierarchy; contributing code must not tear down that shared display. Avoid a blanket rule to either always dispose or never dispose every Display/Shell: identify which component created and owns it.

### Cross-thread work

- [Display.asyncExec](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Display.java#L957) queues work through the synchronizer and returns without waiting for completion. The display must still be live at submission.
- [Display.syncExec](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Display.java#L6416) delegates to [Synchronizer.syncExec](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/common/org/eclipse/swt/widgets/Synchronizer.java#L165). On the UI thread it runs directly; from another thread it queues work and waits. Do not hold a lock that the UI work needs, or wait for a worker from the UI thread while that worker waits for `syncExec`.
- The target control can disappear between submission and execution. Check `isDisposed()` inside the queued runnable before touching it. A check only before enqueueing is insufficient.
- [Display.execute](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Display.java#L1007) requires a non-null runnable, runs synchronously when already on the display thread, and otherwise queues asynchronously. It is not an unconditional background executor.
- [readAndDispatch](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Display.java#L4782) processes native and queued work in the UI event loop. Embedding code must use its framework's loop rather than starting a competing one.

```java
// display and label belong to an existing live UI.
display.asyncExec(() -> {
    if (!label.isDisposed()) {
        label.setText("Ready");
    }
});
```

Coordinate shutdown with producers as well: the snippet's control check does not guarantee that submission to an already disposed Display will succeed. [Display tests](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests/JUnit%20Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_widgets_Display.java) include asynchronous/synchronous execution and disposal cases. No such tests were run here.

## org.eclipse.swt.events and widgets event dispatch

The public event layer provides typed event objects, listener interfaces and adapters. [SelectionListener](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/common/org/eclipse/swt/events/SelectionListener.java) separates ordinary selection from default selection; the triggering keyboard/mouse gesture is platform-dependent. Register and remove the same listener according to the control's lifetime.

At the untyped boundary, [Widget.notifyListeners](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/gtk/org/eclipse/swt/widgets/Widget.java#L1422) checks widget validity, creates an Event if its argument is null, and dispatches it. Display filters run before the widget's event table in the inspected GTK path.

The shared [EventTable.sendEvent](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/common/org/eclipse/swt/widgets/EventTable.java#L97) implementation supports multiple listeners, nested dispatch and removal while dispatching. It stops when the event type becomes `SWT.None`, records listener RuntimeExceptions/Errors through an exception stash, and compacts removed entries after the outer dispatch finishes. This is an implementation observation, not a new promise of snapshot iteration, cancellation for every event, or identical native event order on every OS. Treat listener callbacks as reentrant application code.

[TypedListener](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/common/org/eclipse/swt/widgets/TypedListener.java) translates typed callbacks back into the untyped event, copying only the fields appropriate to each event. For example Verify copies `text` and `doit`, Selection copies `x`, `y` and `doit`, and Traverse copies `detail` and `doit`. It is marked internal and deprecated for removal; applications should use the public listener APIs, not instantiate the bridge.

## org.eclipse.swt.layout: per-control configuration

`GridLayout` positions a Composite's children; `GridData` belongs to one child. [GridData's class contract](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/common/org/eclipse/swt/layout/GridData.java) explicitly forbids sharing the same GridData object across controls. A missing GridData is created by the layout, while an incompatible layout-data object is not a supported substitute.

[GridLayout](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT/common/org/eclipse/swt/layout/GridLayout.java) starts with one column, unequal column widths, 5-point common margins and spacing, and zero individual edge margins. A column count below one performs no child positioning. Its layout pass omits children with `GridData.exclude`; exclusion from layout is not by itself a request to change a control's visibility. Layout computes and applies bounds through the widget APIs and therefore belongs in the owner's UI lifecycle.

## org.eclipse.swt.internal.gtk and the native bridge

The supported client boundary is the SWT API above the native bridge. [OS.java](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT%20PI/gtk/org/eclipse/swt/internal/gtk/OS.java) declares native entry points and generator annotations describing pointer casts and input/output marshaling. The corresponding generated [os.c](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Eclipse%20SWT%20PI/gtk/library/os.c) entry for `pango_font_description_free` casts the Java long to `PangoFontDescription *` and calls the native function. It does not independently establish ownership or validate arbitrary handles.

Consequently the Java caller must preserve allocation/release pairing, device lifetime and backend selection. Handle width, native struct layout, callbacks, and matching native libraries are part of the platform boundary; a Java-only read cannot qualify them.

The [native source map](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/nativeSourceFolders.properties) groups common library inputs with the selected Win32/GTK/Cocoa directories. [CollectSources](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/build-scripts/CollectSources.java) reads that map and copies selected files into a target directory; it is a build-time single-source Java tool, not a widget runtime API. Follow [the JNI regeneration guide](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/Readme.md) when native declarations change. Generated JNI C must not be manually maintained independently of its declarations.

## Integration packages and remaining contracts

The host also exposes accessibility, AWT integration, browser embedding, custom widgets, drag-and-drop, OpenGL, printing and program integration. Their existing package pages and every source variant are linked in the [package ledger](package-ledger.md). They are not subsumed by the generic Widget/Resource rules: clipboard ownership, browser navigation, accessibility callbacks, printing sessions, OpenGL contexts, AWT/SWT thread handoff and StyledText models require their own complete body-level review.

The internal residency/swap packages and optional Skija bundle are present in this fork. The [module ledger](module-ledger.md) identifies their owners and profile/manifest evidence. No portable rendering, memory bound, performance gain or full platform qualification is inferred from their presence.

## Verification and continuation

- Source inspection: the methods and declarations linked above were read at the pinned revision; the Font, Display, Widget test sources were inspected as references
- Not run: Java/Javadoc/API checks, Maven/Tycho tests, JNI regeneration, native compilation, UI execution, screenshots, leak/memory/CPU gates
- The [coverage ledger](README.md#coverage-and-open-gates) distinguishes this selected lifecycle work from the remaining package and API inventory
- Preserve the repository's [license](../../LICENSE), [notice](../../NOTICE), and [contribution guidance](../../CONTRIBUTING.md); this documentation does not change any source or license
