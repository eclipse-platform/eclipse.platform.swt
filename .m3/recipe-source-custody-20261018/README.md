<!-- SPDX-FileCopyrightText: 2026 Hitesh Soliwal and contributors
     SPDX-License-Identifier: Apache-2.0 -->
# Canonical source custody proof mirrors

These two source-sealed packets bind this public target to Synexia `215546d1ca0e724eb2c8e2e8ef6b4adce724733a`. They use the existing `com.synexia.rewrite.Replay` filesystem adapter and its canonical manifest loader. The recipe source owner remains `hsoliwal/com.synexia`; these files are review and replay mirrors.

`binding/` contains exact previous control bytes, sealed after-payloads, manifest and base revision. `execution/` installs a receipt generated only after the binding ran successfully. Its receipt describes the binding execution; it cannot recursively attest its own later installation. `logs/` and `RUNS.json` preserve actual Maven invocation evidence for both stages.

In an exclusive checkout at the recorded public base, use the qualified Java 21 canonical engine classes and Maven offline cache described by the canonical engine provenance. Run `Replay --plan`, `--apply`, and `--check` with the repository root and one packet directory as its two path arguments. Apply `binding/` before `execution/`. A repeated check must report zero changes. Different current source bytes are a refusal, never a reason to update historical preimage hashes.

The [current recipe census](https://github.com/hsoliwal/com.synexia/blob/215546d1ca0e724eb2c8e2e8ef6b4adce724733a/synexia-openrewrite-recipes/recipe-crates/public-fork-home-20261018/evidence/CURRENT_RECIPE_CENSUS.json) (SHA-256 `cfa9edb42a99cb2e54a988b28de7078a3233fdc667c5e4d8f979675e0f767ee0`) records the admission and execution evidence for separate product packets, including any native qualification holds. These binding records do not authorize product promotion, replace native builds, or show an OpenRewrite SDK scheduler run. Existing build/proof callers and pinned source mirrors remain until qualified canonical handoff.
