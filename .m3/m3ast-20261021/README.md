<!-- SPDX-License-Identifier: Apache-2.0 -->
# M3JDK21 original compiler/source receiver

Canonical exact-source recipe: `com.synexia.eclipse.EstateAstEclipsePlatformSwt20261021`, `hsoliwal/com.synexia@b77c892d7519c6aa037c2b5cebd8658867ea712b`.
This applies only an opt-in Maven source audit. The existing Java/ECJ/Tycho
and native/OSGi behavior, original contracts, M3JDK21 JDK ownership and
all public APIs are unchanged. Run
`python3 .m3/m3ast-20261021/verify.py` for the direct offline source gate.
Selecting `-Pm3-ast-source-audit-20261021` executes this source gate in
Maven validate, subject to original Maven profile and toolchain behavior.

The source gate is not an independent AST parser or proof of runtime/JNI
behavior. Subsequent functionality requires its own per-file recipe,
compiler/JUnit differential, JNI/native and installed product gates.
