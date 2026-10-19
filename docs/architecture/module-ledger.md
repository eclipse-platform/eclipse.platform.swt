# Project and OSGi module ledger

Source revision: `c05dff79c9cc737c9cffa501c8f5cef353141dc6`.

This ledger accounts for 34 descriptor-backed project/build units and 24 OSGi bundles/fragments. A project is evidenced by a POM (including alternate filenames), Eclipse .project, feature.xml, bundle manifest, Ant build.xml or pomless build.properties; a directory without such evidence is not promoted to a project. Nested test fixture manifests remain separate fixture modules. The [package ledger](package-ledger.md) maps source occurrences to their nearest such owner.

All entries are included. Documentation status is **partial**, including entries with a lifecycle guide: descriptor extraction is not a full implementation contract. No entry is reviewed-complete and no build/test run is implied. Each entry still needs a complete public/exported surface review, configuration/defaults and side-effect/error/lifecycle contracts, artifact/build membership validation and source-linked test evidence at this revision. Exact headers, versions, optionality and friend/internal restrictions are retained at the linked manifest; the abbreviated dependency names below are navigation, not a replacement manifest.

## Reading the boundaries

- OSGi bundles/fragments are the module units counted here; automatic module names are recorded separately
- Maven/pomless aggregators, Eclipse-only projects and feature projects are project units even when they have no runtime module
- A fixture bundle under tests/data is not a deployable production component merely because it has a manifest
- Physical containment establishes the ledger owner, not proof of active compilation; build selection and profiles still apply
- Source roots are copied from source.. in build.properties when present; SWT custom/platform source selection is documented in [lifecycle contracts](lifecycle-contracts.md)

## repository-root

- Identity: `eclipse.platform.swt`; production/build; eclipse.platform.swt
- Discovery: [pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/pom.xml)
- Build identity: `eclipse.platform.swt`; packaging `pom`; declared module references (including profile declarations): `bundles/org.eclipse.swt.gpu.m3`, `tests/org.eclipse.swt.tests.cocoa`, `tests/org.eclipse.swt.tests.gtk`, `tests/org.eclipse.swt.tests.win32`, `bundles`, `binaries`, `examples/org.eclipse.swt.examples`, `examples/org.eclipse.swt.examples.browser.demos`, `examples/org.eclipse.swt.examples.launcher`, `examples/org.eclipse.swt.examples.ole.win32`, `examples/org.eclipse.swt.examples.views`, `tests/org.eclipse.swt.tests`, `features/org.eclipse.swt.tools.feature`
- Source roots: no source.. value read here; follow the descriptor/custom build route; 20 Java source-directory occurrences in [the package ledger](package-ledger.md#repository-root)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## .m3/openrewrite-recipes

- Identity: `m3-recipe-first-bootstrap`; tooling; m3-recipe-first-bootstrap
- Discovery: [openrewrite-recipes/pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/.m3/openrewrite-recipes/pom.xml)
- Build identity: `m3-recipe-first-bootstrap`; packaging `(default jar)`
- Source roots: no source.. value read here; follow the descriptor/custom build route; 4 Java source-directory occurrences in [the package ledger](package-ledger.md#m3openrewrite-recipes)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries

- Identity: `eclipse.platform.swt.binaries`; platform-fragment; eclipse.platform.swt.binaries
- Discovery: [binaries/pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/pom.xml)
- Build identity: `eclipse.platform.swt.binaries`; packaging `pom`; declared module references (including profile declarations): `org.eclipse.swt.cocoa.macosx.x86_64`, `org.eclipse.swt.cocoa.macosx.aarch64`, `org.eclipse.swt.gtk.linux.aarch64`, `org.eclipse.swt.gtk.linux.ppc64le`, `org.eclipse.swt.gtk.linux.riscv64`, `org.eclipse.swt.gtk.linux.x86_64`, `org.eclipse.swt.win32.win32.aarch64`, `org.eclipse.swt.win32.win32.x86_64`
- Source roots: no source.. value read here; follow the descriptor/custom build route; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.cocoa.macosx.aarch64

- Identity: `org.eclipse.swt.cocoa.macosx.aarch64`; platform-fragment; Standard Widget Toolkit for Mac OS X (Cocoa) on aarch64
- Discovery: [org.eclipse.swt.cocoa.macosx.aarch64/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.cocoa.macosx.aarch64/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.cocoa.macosx.aarch64/META-INF/MANIFEST.MF); [org.eclipse.swt.cocoa.macosx.aarch64/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.cocoa.macosx.aarch64/build.properties)
- Module: version `3.137.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.cocoa.macosx.aarch64`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 16 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=cocoa) (osgi.os=macosx) (osgi.arch=aarch64) )`
- Source roots: `../legal_files/cocoa.macosx,../../bundles/org.eclipse.swt/Eclipse SWT/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/bidi,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/coolbar,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/expand,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/tooltip,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT WebKit/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.cocoa.macosx.x86_64

- Identity: `org.eclipse.swt.cocoa.macosx.x86_64`; platform-fragment; Standard Widget Toolkit for Mac OS X (Cocoa) on x86_64
- Discovery: [org.eclipse.swt.cocoa.macosx.x86_64/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.cocoa.macosx.x86_64/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.cocoa.macosx.x86_64/META-INF/MANIFEST.MF); [org.eclipse.swt.cocoa.macosx.x86_64/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.cocoa.macosx.x86_64/build.properties)
- Module: version `3.137.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.cocoa.macosx.x86_64`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 16 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=cocoa) (osgi.os=macosx) (osgi.arch=x86_64) )`
- Source roots: `../legal_files/cocoa.macosx,../../bundles/org.eclipse.swt/Eclipse SWT/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/bidi,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/coolbar,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/expand,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/tooltip,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT WebKit/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/cocoa,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.gtk.linux.aarch64

- Identity: `org.eclipse.swt.gtk.linux.aarch64`; platform-fragment; Standard Widget Toolkit for GTK on aarch64
- Discovery: [org.eclipse.swt.gtk.linux.aarch64/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.aarch64/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.aarch64/META-INF/MANIFEST.MF); [org.eclipse.swt.gtk.linux.aarch64/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.aarch64/build.properties)
- Module: version `3.137.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.gtk.linux.aarch64`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 21 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=gtk) (osgi.os=linux) (osgi.arch=aarch64))`
- Source roots: `../legal_files/gtk.linux,../../bundles/org.eclipse.swt/Eclipse SWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT/cairo,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/bidi,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/coolbar,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/taskbar,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/gtk,../../bundles/org.eclipse.swt/Eclipse SWT PI/cairo,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/glx,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common,../../bundles/org.eclipse.swt/Eclipse SWT WebKit/gtk`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.gtk.linux.loongarch64

- Identity: `org.eclipse.swt.gtk.linux.loongarch64`; platform-fragment; Standard Widget Toolkit for GTK on loongarch64
- Discovery: [org.eclipse.swt.gtk.linux.loongarch64/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.loongarch64/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.loongarch64/META-INF/MANIFEST.MF); [org.eclipse.swt.gtk.linux.loongarch64/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.loongarch64/build.properties)
- Module: version `3.135.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.gtk.linux.loongarch64`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 21 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=gtk) (osgi.os=linux) (osgi.arch=loongarch64))`
- Source roots: `../legal_files/gtk.linux,../../bundles/org.eclipse.swt/Eclipse SWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT/cairo,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/bidi,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/coolbar,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/taskbar,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/gtk,../../bundles/org.eclipse.swt/Eclipse SWT PI/cairo,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/glx,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common,../../bundles/org.eclipse.swt/Eclipse SWT WebKit/gtk`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.gtk.linux.ppc64le

- Identity: `org.eclipse.swt.gtk.linux.ppc64le`; platform-fragment; Standard Widget Toolkit for GTK on ppc64le
- Discovery: [org.eclipse.swt.gtk.linux.ppc64le/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.ppc64le/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.ppc64le/META-INF/MANIFEST.MF); [org.eclipse.swt.gtk.linux.ppc64le/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.ppc64le/build.properties)
- Module: version `3.137.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.gtk.linux.ppc64le`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 21 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=gtk) (osgi.os=linux) (osgi.arch=ppc64le))`
- Source roots: `../legal_files/gtk.linux,../../bundles/org.eclipse.swt/Eclipse SWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT/cairo,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/bidi,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/coolbar,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/taskbar,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/gtk,../../bundles/org.eclipse.swt/Eclipse SWT PI/cairo,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/glx,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common,../../bundles/org.eclipse.swt/Eclipse SWT WebKit/gtk`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.gtk.linux.riscv64

- Identity: `org.eclipse.swt.gtk.linux.riscv64`; platform-fragment; Standard Widget Toolkit for GTK on riscv64
- Discovery: [org.eclipse.swt.gtk.linux.riscv64/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.riscv64/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.riscv64/META-INF/MANIFEST.MF); [org.eclipse.swt.gtk.linux.riscv64/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.riscv64/build.properties)
- Module: version `3.137.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.gtk.linux.riscv64`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 21 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=gtk) (osgi.os=linux) (osgi.arch=riscv64))`
- Source roots: `../legal_files/gtk.linux,../../bundles/org.eclipse.swt/Eclipse SWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT/cairo,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/bidi,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/coolbar,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/taskbar,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/gtk,../../bundles/org.eclipse.swt/Eclipse SWT PI/cairo,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/glx,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common,../../bundles/org.eclipse.swt/Eclipse SWT WebKit/gtk`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.gtk.linux.x86_64

- Identity: `org.eclipse.swt.gtk.linux.x86_64`; platform-fragment; Standard Widget Toolkit for GTK on x86_64
- Discovery: [org.eclipse.swt.gtk.linux.x86_64/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.x86_64/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.x86_64/META-INF/MANIFEST.MF); [org.eclipse.swt.gtk.linux.x86_64/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.gtk.linux.x86_64/build.properties)
- Module: version `3.137.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.gtk.linux.x86_64`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 21 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=gtk) (osgi.os=linux) (osgi.arch=x86_64))`
- Source roots: `../legal_files/gtk.linux,../../bundles/org.eclipse.swt/Eclipse SWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT/cairo,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/bidi,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/coolbar,../../bundles/org.eclipse.swt/Eclipse SWT/emulated/taskbar,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/gtk,../../bundles/org.eclipse.swt/Eclipse SWT PI/cairo,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/gtk,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/gtk,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/gtk,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/glx,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common,../../bundles/org.eclipse.swt/Eclipse SWT WebKit/gtk`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.win32.win32.aarch64

- Identity: `org.eclipse.swt.win32.win32.aarch64`; platform-fragment; Standard Widget Toolkit for Windows on aarch64
- Discovery: [org.eclipse.swt.win32.win32.aarch64/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.win32.win32.aarch64/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.win32.win32.aarch64/META-INF/MANIFEST.MF); [org.eclipse.swt.win32.win32.aarch64/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.win32.win32.aarch64/build.properties)
- Module: version `3.137.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.win32.win32.aarch64`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 20 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=win32) (osgi.os=win32) (osgi.arch=aarch64))`
- Source roots: `../legal_files/win32.win32,../../bundles/org.eclipse.swt/Eclipse SWT/win32,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/win32,../../bundles/org.eclipse.swt/Eclipse SWT OLE Win32/win32,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/win32,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/win32,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/win32,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/win32,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/win32,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/win32,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/win32,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## binaries/org.eclipse.swt.win32.win32.x86_64

- Identity: `org.eclipse.swt.win32.win32.x86_64`; platform-fragment; Standard Widget Toolkit for Windows on x86_64
- Discovery: [org.eclipse.swt.win32.win32.x86_64/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.win32.win32.x86_64/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.win32.win32.x86_64/META-INF/MANIFEST.MF); [org.eclipse.swt.win32.win32.x86_64/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/binaries/org.eclipse.swt.win32.win32.x86_64/build.properties)
- Module: version `3.137.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.win32.win32.x86_64`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"`
- Surface: 20 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(& (osgi.ws=win32) (osgi.os=win32) (osgi.arch=x86_64))`
- Source roots: `../legal_files/win32.win32,../../bundles/org.eclipse.swt/Eclipse SWT/win32,../../bundles/org.eclipse.swt/Eclipse SWT/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/common,../../bundles/org.eclipse.swt/Eclipse SWT PI/win32,../../bundles/org.eclipse.swt/Eclipse SWT OLE Win32/win32,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/win32,../../bundles/org.eclipse.swt/Eclipse SWT Accessibility/common,../../bundles/org.eclipse.swt/Eclipse SWT AWT/win32,../../bundles/org.eclipse.swt/Eclipse SWT AWT/common,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/win32,../../bundles/org.eclipse.swt/Eclipse SWT Drag and Drop/common,../../bundles/org.eclipse.swt/Eclipse SWT Printing/win32,../../bundles/org.eclipse.swt/Eclipse SWT Printing/common,../../bundles/org.eclipse.swt/Eclipse SWT Program/win32,../../bundles/org.eclipse.swt/Eclipse SWT Program/common,../../bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/common,../../bundles/org.eclipse.swt/Eclipse SWT Browser/win32,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/win32,../../bundles/org.eclipse.swt/Eclipse SWT OpenGL/common`; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## bundles

- Identity: `swt-bundles`; pomless-build; swt-bundles
- Discovery: [bundles/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/build.properties)
- Build identity: `swt-bundles`
- Source roots: no source.. value read here; follow the descriptor/custom build route; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## bundles/org.eclipse.swt

- Identity: `org.eclipse.swt`; production/build; Standard Widget Toolkit
- Discovery: [org.eclipse.swt/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/META-INF/MANIFEST.MF); [org.eclipse.swt/pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/pom.xml); [org.eclipse.swt/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/build.properties)
- Module: version `3.138.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses
- Surface: 17 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Build identity: `org.eclipse.swt`; packaging `eclipse-plugin`
- Source roots: no source.. value read here; follow the descriptor/custom build route; 84 Java source-directory occurrences in [the package ledger](package-ledger.md#bundlesorgeclipseswt)
- Coverage: partial. Selected APIs are explained in [lifecycle contracts](lifecycle-contracts.md); other exports remain unreviewed.

## bundles/org.eclipse.swt.gpu.m3

- Identity: `org.eclipse.swt.gpu.m3`; production/build; SWT retained M3 backend
- Discovery: [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.gpu.m3/META-INF/MANIFEST.MF); [org.eclipse.swt.gpu.m3/pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.gpu.m3/pom.xml)
- Module: version `3.137.0`; execution environment `JavaSE-21`; automatic module name `not declared`
- Dependency direction: requires none declared in Require-Bundle; 0 imported package clauses; fragment host `org.eclipse.swt;bundle-version="[3.137.0,4.0.0)"`
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Platform selection: `(&(osgi.ws=win32)(osgi.os=win32)(osgi.arch=x86_64))`
- Build identity: `org.eclipse.swt.gpu.m3`; packaging `(default jar)`
- Source roots: no source.. value read here; follow the descriptor/custom build route; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## bundles/org.eclipse.swt.svg

- Identity: `org.eclipse.swt.svg`; production/build; SWT SVG Rendering Support
- Discovery: [org.eclipse.swt.svg/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.svg/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.svg/META-INF/MANIFEST.MF); [org.eclipse.swt.svg/pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.svg/pom.xml); [org.eclipse.swt.svg/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.svg/build.properties)
- Module: version `3.133.100.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.svg`
- Dependency direction: requires none declared in Require-Bundle; 4 imported package clauses; fragment host `org.eclipse.swt`
- Surface: 1 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Build identity: `org.eclipse.swt.svg`; packaging `eclipse-plugin`
- Source roots: `src/,resources/`; 3 Java source-directory occurrences in [the package ledger](package-ledger.md#bundlesorgeclipseswtsvg)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## bundles/org.eclipse.swt.tools

- Identity: `org.eclipse.swt.tools`; production/build; SWT Tools
- Discovery: [org.eclipse.swt.tools/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.tools/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.tools/META-INF/MANIFEST.MF); [org.eclipse.swt.tools/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt.tools/build.properties)
- Module: version `3.112.200.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.tools`
- Dependency direction: requires `org.eclipse.core.runtime`, `org.eclipse.core.resources`, `org.eclipse.jdt.core`, `org.eclipse.jface.text`, [org.eclipse.swt](#bundlesorgeclipseswt), `org.eclipse.e4.core.di`, `org.eclipse.e4.ui.di`, `org.eclipse.e4.ui.model.workbench`; 1 imported package clauses
- Surface: 1 exported package clauses; inspect manifest for visibility, version, split-package and friend directives; activation `lazy`
- Source roots: `src/,JNI Generation/,Mac Generation/,Icon Exe/,JavadocBasher/`; 6 Java source-directory occurrences in [the package ledger](package-ledger.md#bundlesorgeclipseswttools)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## bundles/org.eclipse.swt/build

- Identity: `bundles/org.eclipse.swt/build`; production/build; build
- Discovery: [build/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/bundles/org.eclipse.swt/build/.project)
- Source roots: no source.. value read here; follow the descriptor/custom build route; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## docs

- Identity: `docs`; documentation; docs
- Discovery: [docs/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/docs/.project)
- Source roots: no source.. value read here; follow the descriptor/custom build route; 1 Java source-directory occurrences in [the package ledger](package-ledger.md#docs)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## examples/org.eclipse.swt.examples

- Identity: `org.eclipse.swt.examples`; example; SWT Standalone Examples Plugin
- Discovery: [org.eclipse.swt.examples/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples/META-INF/MANIFEST.MF); [org.eclipse.swt.examples/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples/build.properties)
- Module: version `3.109.300.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.examples`
- Dependency direction: requires [org.eclipse.swt](#bundlesorgeclipseswt); 0 imported package clauses
- Surface: 16 exported package clauses; inspect manifest for visibility, version, split-package and friend directives; activation `lazy`
- Source roots: `src/`; 16 Java source-directory occurrences in [the package ledger](package-ledger.md#examplesorgeclipseswtexamples)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## examples/org.eclipse.swt.examples.browser.demos

- Identity: `org.eclipse.swt.examples.browser.demos`; example; SWT Browser Demos Plugin
- Discovery: [org.eclipse.swt.examples.browser.demos/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.browser.demos/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.browser.demos/META-INF/MANIFEST.MF); [org.eclipse.swt.examples.browser.demos/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.browser.demos/build.properties)
- Module: version `3.109.100.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.examples.browser.demos`
- Dependency direction: requires `org.eclipse.ui`, `org.eclipse.core.runtime`, [org.eclipse.swt](#bundlesorgeclipseswt); 0 imported package clauses
- Surface: 2 exported package clauses; inspect manifest for visibility, version, split-package and friend directives; activator `org.eclipse.swt.examples.browser.demos.BrowserDemoPlugin`; activation `lazy`
- Source roots: `src/`; 2 Java source-directory occurrences in [the package ledger](package-ledger.md#examplesorgeclipseswtexamplesbrowserdemos)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## examples/org.eclipse.swt.examples.launcher

- Identity: `org.eclipse.swt.examples.launcher`; example; SWT Launcher Example Plugin
- Discovery: [org.eclipse.swt.examples.launcher/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.launcher/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.launcher/META-INF/MANIFEST.MF); [org.eclipse.swt.examples.launcher/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.launcher/build.properties)
- Module: version `3.109.100.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.examples.launcher`
- Dependency direction: requires `org.eclipse.core.runtime`, `org.eclipse.ui`, [org.eclipse.swt](#bundlesorgeclipseswt); 0 imported package clauses
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives; activator `org.eclipse.swt.examples.launcher.LauncherPlugin`; activation `lazy`
- Source roots: `src/`; 1 Java source-directory occurrences in [the package ledger](package-ledger.md#examplesorgeclipseswtexampleslauncher)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## examples/org.eclipse.swt.examples.ole.win32

- Identity: `org.eclipse.swt.examples.ole.win32`; example; SWT OLE Example Plugin (Win32)
- Discovery: [org.eclipse.swt.examples.ole.win32/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.ole.win32/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.ole.win32/META-INF/MANIFEST.MF); [org.eclipse.swt.examples.ole.win32/pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.ole.win32/pom.xml); [org.eclipse.swt.examples.ole.win32/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.ole.win32/build.properties)
- Module: version `3.110.200.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.examples.ole.win32`
- Dependency direction: requires `org.eclipse.ui.ide`, `org.eclipse.ui.views`, `org.eclipse.jface.text`, `org.eclipse.ui.workbench.texteditor`, `org.eclipse.ui.editors`, `org.eclipse.core.runtime`, `org.eclipse.core.resources`, `org.eclipse.ui`, [org.eclipse.swt](#bundlesorgeclipseswt); 0 imported package clauses
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives; activator `org.eclipse.swt.examples.ole.win32.OlePlugin`; activation `lazy`
- Build identity: `org.eclipse.swt.examples.ole.win32`; packaging `eclipse-plugin`
- Source roots: `src/`; 1 Java source-directory occurrences in [the package ledger](package-ledger.md#examplesorgeclipseswtexamplesolewin32)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## examples/org.eclipse.swt.examples.views

- Identity: `org.eclipse.swt.examples.views`; example; SWT Views Example Plugin
- Discovery: [org.eclipse.swt.examples.views/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.views/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.views/META-INF/MANIFEST.MF); [org.eclipse.swt.examples.views/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.views/build.properties)
- Module: version `3.109.100.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.examples.views`
- Dependency direction: requires `org.eclipse.core.runtime`, `org.eclipse.ui`, [org.eclipse.swt.examples](#examplesorgeclipseswtexamples), [org.eclipse.swt](#bundlesorgeclipseswt); 0 imported package clauses
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives; activation `lazy`
- Source roots: `src/`; 4 Java source-directory occurrences in [the package ledger](package-ledger.md#examplesorgeclipseswtexamplesviews)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## examples/org.eclipse.swt.examples.watchdog

- Identity: `org.eclipse.swt.examples.watchdog`; example; %bundleName
- Discovery: [org.eclipse.swt.examples.watchdog/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.watchdog/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.watchdog/META-INF/MANIFEST.MF); [org.eclipse.swt.examples.watchdog/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.examples.watchdog/build.properties)
- Module: version `1.3.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.examples.watchdog`
- Dependency direction: requires `org.eclipse.core.runtime`, [org.eclipse.swt](#bundlesorgeclipseswt), `org.eclipse.ui.workbench`; 0 imported package clauses
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives; activator `org.eclipse.swt.examples.watchdog.WatchdogPlugin`; activation `lazy`
- Source roots: `src/`; 1 Java source-directory occurrences in [the package ledger](package-ledger.md#examplesorgeclipseswtexampleswatchdog)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## examples/org.eclipse.swt.opengl.examples

- Identity: `examples/org.eclipse.swt.opengl.examples`; example; org.eclipse.swt.opengl.examples
- Discovery: [org.eclipse.swt.opengl.examples/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.opengl.examples/.project); [org.eclipse.swt.opengl.examples/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.opengl.examples/build.properties)
- Source roots: no source.. value read here; follow the descriptor/custom build route; 1 Java source-directory occurrences in [the package ledger](package-ledger.md#examplesorgeclipseswtopenglexamples)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## examples/org.eclipse.swt.snippets

- Identity: `org.eclipse.swt.snippets`; example; SWT Snippets
- Discovery: [org.eclipse.swt.snippets/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.snippets/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.snippets/META-INF/MANIFEST.MF); [org.eclipse.swt.snippets/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/examples/org.eclipse.swt.snippets/build.properties)
- Module: version `3.5.0`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.snippets`
- Dependency direction: requires [org.eclipse.swt](#bundlesorgeclipseswt); 0 imported package clauses
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Source roots: `src/`; 1 Java source-directory occurrences in [the package ledger](package-ledger.md#examplesorgeclipseswtsnippets)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## features/org.eclipse.swt.tools.feature

- Identity: `features/org.eclipse.swt.tools.feature`; feature; org.eclipse.swt.tools.feature
- Discovery: [org.eclipse.swt.tools.feature/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/features/org.eclipse.swt.tools.feature/.project); [org.eclipse.swt.tools.feature/feature.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/features/org.eclipse.swt.tools.feature/feature.xml); [org.eclipse.swt.tools.feature/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/features/org.eclipse.swt.tools.feature/build.properties)
- Source roots: no source.. value read here; follow the descriptor/custom build route; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## tests

- Identity: `swt-tests`; pomless-build; swt-tests
- Discovery: [tests/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/build.properties)
- Build identity: `swt-tests`
- Source roots: no source.. value read here; follow the descriptor/custom build route; 0 Java source-directory occurrences
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## tests/org.eclipse.swt.tests

- Identity: `org.eclipse.swt.tests`; test; Eclipse SWT Tests
- Discovery: [org.eclipse.swt.tests/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests/META-INF/MANIFEST.MF); [org.eclipse.swt.tests/pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests/pom.xml); [org.eclipse.swt.tests/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests/build.properties)
- Module: version `3.108.100.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.tests`
- Dependency direction: requires `junit-jupiter-engine`, `junit-jupiter-params`, `junit-jupiter-api`, `junit-platform-suite-api`, `junit-platform-suite-commons`, `junit-platform-suite-engine`, `org.opentest4j`, [org.eclipse.swt](#bundlesorgeclipseswt), `org.eclipse.test`, `org.eclipse.test.performance`; 0 imported package clauses
- Surface: 2 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Build identity: `org.eclipse.swt.tests`; packaging `eclipse-test-plugin`
- Source roots: `JUnit Tests/,data/`; 7 Java source-directory occurrences in [the package ledger](package-ledger.md#testsorgeclipseswttests)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## tests/org.eclipse.swt.tests.cocoa

- Identity: `org.eclipse.swt.tests.cocoa`; test; Eclipse SWT Tests Macos/cocoa
- Discovery: [org.eclipse.swt.tests.cocoa/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.cocoa/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.cocoa/META-INF/MANIFEST.MF); [org.eclipse.swt.tests.cocoa/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.cocoa/build.properties)
- Module: version `3.109.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.tests.cocoa`
- Dependency direction: requires `junit-jupiter-api`, [org.eclipse.swt](#bundlesorgeclipseswt), `junit-platform-suite-api`; 0 imported package clauses
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Source roots: `JUnit Tests/`; 2 Java source-directory occurrences in [the package ledger](package-ledger.md#testsorgeclipseswttestscocoa)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## tests/org.eclipse.swt.tests.gtk

- Identity: `org.eclipse.swt.tests.gtk`; test; Eclipse SWT Tests Linux/gtk
- Discovery: [org.eclipse.swt.tests.gtk/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.gtk/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.gtk/META-INF/MANIFEST.MF); [org.eclipse.swt.tests.gtk/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.gtk/build.properties)
- Module: version `3.110.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.tests.gtk`
- Dependency direction: requires `junit-jupiter-api`, [org.eclipse.swt](#bundlesorgeclipseswt), `junit-platform-suite-api`; 0 imported package clauses
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Source roots: `JUnit Tests/,ManualTests/`; 3 Java source-directory occurrences in [the package ledger](package-ledger.md#testsorgeclipseswttestsgtk)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## tests/org.eclipse.swt.tests.win32

- Identity: `org.eclipse.swt.tests.win32`; test; Eclipse SWT Tests Windows/win32
- Discovery: [org.eclipse.swt.tests.win32/.project](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.win32/.project); [META-INF/MANIFEST.MF](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.win32/META-INF/MANIFEST.MF); [org.eclipse.swt.tests.win32/build.properties](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/tests/org.eclipse.swt.tests.win32/build.properties)
- Module: version `3.109.0.qualifier`; execution environment `JavaSE-21`; automatic module name `org.eclipse.swt.tests.win32`
- Dependency direction: requires [org.eclipse.swt](#bundlesorgeclipseswt); 5 imported package clauses
- Surface: 0 exported package clauses; inspect manifest for visibility, version, split-package and friend directives
- Source roots: `JUnit Tests/`; 5 Java source-directory occurrences in [the package ledger](package-ledger.md#testsorgeclipseswttestswin32)
- Coverage: partial. Purpose/title and structural boundaries are established by descriptors; complete behavior and API semantics remain open.

## .m3/analysis-pom.xml

- Identity: alternate Maven analysis project, distinct from the production reactor and the adjacent recipe-bootstrap project
- Discovery: [analysis-pom.xml](https://github.com/hsoliwal/eclipse.platform.swt/blob/c05dff79c9cc737c9cffa501c8f5cef353141dc6/.m3/analysis-pom.xml)
- Purpose: declares a repository-root source directory and OpenRewrite analysis/task profiles with data-table export. Configured exclusions cover generated output/build directories and the tooling directory itself
- Boundary: the source root overlaps production projects for analysis; this does not transfer runtime ownership of their packages to the analysis project
- Coverage: partial. Profile/plugin declarations were read; recipe behavior, dependency availability, output completeness and any previous run claims are not qualified here. NOT_RUN

