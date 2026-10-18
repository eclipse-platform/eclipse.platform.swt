# Distillation continuation: lossless Java composition and full Linux verify

This pass starts from merged SWT `5e7b1716a5757ae0ea653919d8c3734135fb91c6`. It repairs two source dependency mismatches through the canonical Synexia OpenRewrite recipe crate `swt-dependency-owner-repair-v1`:

* `LWJGLExample` imports `org.lwjgl.util.glu.GLU`, the real owner in pinned LWJGL 2.9.3.
* `ControlWin32Tests` retains its JUnit 4 message-first assertions without a conflicting explicit Jupiter `assertEquals` import.

All 17 OpenGL example sources and 20 Win32 test sources compile. No SWT public API, widget implementation, generated native source, or built binary is changed by this continuation.

| Verification | Result |
|---|---|
| Full Linux Maven/Tycho `verify` | `BUILD SUCCESS`; 4,420 tests reported, zero failures/errors, 20 reported skips |
| Complete Java source composition | 2,455 files, 53,387 type/member atoms, 100,035 source segments |
| Export/reconstruction | Original bytes reproduced exactly from serialized segments for every Java file |
| OpenRewrite Java coverage | All 2,455 mapped; zero parser errors or unresolved declarations |
| Executable invocation attribution | Zero unresolved call targets after correct dependency/platform contexts |
| Canonical recipe/composition verification | 28 tests pass, zero failures/errors/skips |

The full Linux run used Java 21, GTK3, matching 4975r7 native libraries, headful AWT, and WebKitGTK 2.52.3. This supersedes the earlier missing-WebKit/headful-AWT verification blocker. Maven's nested suite counts are not independent totals; the table quotes its final reactor summary.

Canonical recipes, sealed pre/postimages, dependency provenance, reconstruction host, and proof logs reside in `hsoliwal/com.synexia`, under `verification/swt-distillation-continuation-20261004`. The lossless composition reuses `m3-java-contracts` rather than introducing another atomizer. Its segments preserve whole method behavior and source order; they are not permission to reorder execution or replace atoms arbitrarily.

Documentation references remain separate evidence: 2,041 unresolved occurrences, including 279 invocation-shaped Javadoc links. Twelve resolved-call occurrences retain explicitly unmodeled anonymous lexical owners. Native/non-Java semantics, Cocoa/Win32 native runtime, GTK4, broad performance, complete M3 absorption, strict Synexia admission, its full reactor, and its approved offline mirror are not certified by this result.
