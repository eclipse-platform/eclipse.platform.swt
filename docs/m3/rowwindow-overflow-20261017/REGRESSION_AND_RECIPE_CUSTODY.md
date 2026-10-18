# SWT row-window integer range bug: additive test receiver

**Status: DRAFT / failing pre-fix regression by design.** No production source
changed here. This complements Synexia [#12083](https://github.com/hsoliwal/com.synexia/pull/12083),
a qualified-candidate Maven/OpenRewrite recipe reusing `M3InsertStatements`.

## Source and history
- Frozen receiving master: `678da68f34007af0aab0689aa72bd85fb44b800e`.
- Original `VirtualRowWindowPlan.java` Git SHA-1:
  `eb0f9eac614ad0362d68e718294a60a5436f78f1`.
- Existing SWT integrated viewport owners #201/#203 and history audit #214
  and native-handle repair #215 are preserved untouched.
- SPDX: EPL-2.0; no external donor source copied.

## Verified isolated Java21 arithmetic reproduction

Original `inserted` and `removed` method bodies were compiled with a
small Java21 harness. Three cases reproduce the defect:

| Case | Old result | Required fix |
| --- | --- | --- |
| `plan(MAX-2,2); commit(); inserted(0,1)` | resident `end() == Integer.MIN_VALUE` | reject, no mutation |
| `plan(MAX-2,2); commit(); inserted(MAX-2,1)` | resident `end() == Integer.MIN_VALUE` | reject, no mutation |
| `plan(100,40); commit(); removed(MAX-2,5)` | returns `5`, resident shifts to `first=95,size=35` | reject, no mutation |

A locally staged arithmetic-guard variant passed those cases and 10,000
small-range original-vs-guarded comparisons. **This does not constitute an
OpenRewrite execution, full SWT build, or native proof.**

## Tests, gates and promotion
- New JUnit5 test: `Test_VirtualRowWindowBounds`; reflective like original
  `Test_VirtualRowWindowPlan`, no production-only test hooks.
- Protect valid `Integer.MAX_VALUE` endpoint and zero-length operations.
- This new test **should fail** against the current original source and
  pass only after the exact recipe candidate is materialized.
- Run original `Test_VirtualRowWindowPlan`, the new regression, full SWT
  tests and native Windows/GTK/Cocoa/Skija/GPU and pixel/event differential.
- Apply production change only by executing the canonical recipe against an
  exact source preimage on a new receiver PR. No manual product patching,
  force-push, history rewrite, or automatic merging.

**Current evidence**: Java arithmetic harness reproduced bug and compared
a guarded candidate; new JUnit native tests, OpenRewrite Maven and hosted CI
are **NOT_RUN**.
