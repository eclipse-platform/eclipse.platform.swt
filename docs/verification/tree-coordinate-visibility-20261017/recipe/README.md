<!-- SPDX-FileCopyrightText: 2026 Hitesh Soliwal and Contributors to the Synexia Project -->
<!-- SPDX-License-Identifier: Apache-2.0 -->
# SWT logical coordinate visibility

This packet repairs `VirtualTreeVisibleProjection.visibleIndexOfCoordinate` in the original common SWT tree projection. A valid child coordinate below a collapsed parent or an ancestor hidden by collapse must have no visible row. The existing implementation could return a visible row belonging to another item; GTK accessibility selection therefore disagreed with its visible selection count. The correction preserves validation, root coordinates, sparse parent-owned storage, stable Item identities, event order and SetData behavior.

Five target paths are source-sealed at SWT commit `678da68f34007af0aab0689aa72bd85fb44b800e`: the original projection owner, two regression tests, additive suite registration and the existing `Test_VirtualTreeWindow` numeric expected-value correction. That pre-existing test compared boxed Integer 1828 with boxed Long 1828. Changing only `Math.max(0, ...)` to `Math.max(0L, ...)` preserves the geometry assertion and fixes its type; the baseline failure is retained.

## Executed evidence

The finite headless collapse/oracle regression has three baseline failures and passes all three on the candidate. The real current-source GTK test fails on the untouched baseline when a hidden selected leaf contributes an unrelated accessibility row; the candidate passes. The test invokes public `Tree.select` on a hidden leaf after collapse and checks stable identity, event and SetData behavior. The final received checkout compiles all 546 current GTK Java sources, passes the 49 related headless tests and passes the real GTK accessibility test. The before/after javap API digest is unchanged. Native/source compilation and process observations are retained separately under `evidence/target`; `received-verification.json` binds the final target proof to source commit `ca77c0272fb6c1ea12594ac00baca8fb9b50ce3d`.

`evidence/sdk` retains the final five-target Maven/OpenRewrite result and exact recipe/input/output binding. Earlier four-target SDK qualification is preserved under `evidence/preliminary-four-targets`; that earlier successful packet did not include the later numeric test repair and is not the final receiving authority. The five shared SDK methods prove named descriptor execution, exact Java LST output, fixed point, source order invariance, mixed pre/postimage application and malformed-source refusal. Code-looking decoy text remains opaque.

## Reproduce

From this crate with Java 21 and an approved Maven cache:

```sh
mvn -B -o -ntp verify
python3 receive.py --target /absolute/path/to/eclipse.platform.swt --check
python3 receive.py --target /absolute/path/to/eclipse.platform.swt
python3 receive.py --target /absolute/path/to/eclipse.platform.swt --check
```

The source/input/output SHA-256s and Git blob identities are in `crate.json`, the manifest and `SDK_RESULTS.tsv`. All receiving targets are admitted before serial writes. Only actual SDK Results may be materialized. Changing a source/template after SDK qualification is refused; update the recipe packet and requalify it.

## Custody and limits

The unchanged Synexia engine, SDK harness and receiver are pinned to `892e3f6c51d78bc0948e8a8cf736055b4c70eafa`; that pin identifies reused machinery, not this newly created recipe. The new containing commit supplies recipe custody. SDK proof uses OpenRewrite 8.17.1, Java 21, Maven 3.9.11 and JUnit Jupiter 5.10.2. Existing Eclipse EPL-2.0 payload notices remain unchanged; Synexia proof/recipe code remains Apache-2.0.

This is a bounded correction inside an already-public original SWT owner, with no new runtime service, public widget API, native peer or JNI symbol. The proof does not establish all-widget qualification, Win32/Cocoa parity, full SWT/Tycho reactor or complete viewport memory/performance acceptance. Native GTK and headless results are distinct from SDK recipe proof. Preserve the original API/platform gates before promotion.
