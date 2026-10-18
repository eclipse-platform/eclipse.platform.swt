# Live GTK Tree residency source milestone

Implemented: bounded attached native residency with primitive paint/ancestor planning; separate logical model and stable Java Item IDs; logical scrolling, focus and compressed selection; Java presentation/geometry; demand-driven logical accessibility with table-cell metadata, rank selection and disposal tombstones.

The candidate retains SWT master #78's single-pass sparse traversal, #79's stack-safe deletion and #80's logical model activation. It is based on SWT `97be47c95bc6ee075b3992bb8f0fa2d2e0cbbe46` and current canonical develop `aa37ab2d7bb851bb2e86f314225dc43e118df071`. The canonical parser engine gained optional bounded parser dependencies; this crate leaves that option absent and reuses the current engine unchanged.

## Executed operations

- Final actual source recipe: nine changes, then zero changes, using current develop engine bytes.
- Offline recipe wrapper/engine compilation: five sources, JDK 21.0.7, `-Xlint:all -Werror`.
- Shared common compilation: eight sources, Java 21, `-Xlint:all -Werror`.
- Full GTK Java compilation: 493 sources, exit 0, candidate/baseline identical 148-warning inventory.
- GTK3 C object compilation: GCC 13.3.0 / GTK 3.24.41, `-O -Wall -Werror`, exit 0.
- Read-only final review: disposed multi-selection target and no-op keyboard selection event findings resolved in source.

No behavioral, JUnit, refusal, GUI or native runtime tests were added or run. Native linking, live AT interoperability, DND/editor/owner-draw/keyboard parity, platform qualification, benchmarks, full reactors, strict admission and merge qualification remain pending. This is a draft source milestone, not runtime qualification.

## Publication

- [SWT draft #81](https://github.com/hsoliwal/eclipse.platform.swt/pull/81): source commit `9fc138c56f36112c9233f86aca586c3376bc5d28`; exact tree `fc5a0dbd7b77c0bfd47e1466f30b9dbd62d4b344`.
- [Canonical recipe draft #9339](https://github.com/hsoliwal/com.synexia/pull/9339): source commit `6eb587dd91d8721df99b27bb9f94b306dde83cba`; exact tree `946a6179dc0e265c0304d82a4f547c38b74cf764`.
- All 58 source/recipe/receipt files were read from those immutable commits and matched exact content and Git blob identities.
- [Publication receipt](publication.json) records every reviewed file hash. Final metadata commits only complete this receipt and plan; the compiled nine-owner production cohort is unchanged.

The requested implementation/draft-source milestone is delivered. Runtime and merge qualification remain pending, as listed above. Canonical #9293 remains the merged historical phase1 delivery.
