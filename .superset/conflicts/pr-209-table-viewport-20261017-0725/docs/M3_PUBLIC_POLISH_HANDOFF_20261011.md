<!-- SPDX-License-Identifier: EPL-2.0 -->
# Synexia public-code polish handoff — 2026-10-11

Status: docs-first receiver workspace. This commit changes no production source and grants no mutation or promotion authority.

## Exact custody

- Synexia donor/convergence workspace: `hsoliwal/com.synexia`.
- Synexia execution proof head: `bc90989194c38a1e3d726685f20fcf416ff666ba` (draft PR #10417).
- Receiving repository: `hsoliwal/eclipse.platform.swt`.
- Receiving default branch: `master`.
- Revalidated receiving baseline: `3d57f02361fae4fc2da05951a37514fc917a12ff`.
- Target-source license boundary: `EPL-2.0`.
- Required handoff gate: `TARGET_LICENSE_COMPATIBILITY_PLUS_RECIPE_DAG_PROOF`.

## Locked application rule

Before any production/configuration mutation, re-read `master`. If its head differs from the baseline above, stop and reconcile inventory/history; do not replay stale postimages.

Portable OpenRewrite/Maven recipes, donor catalogues, DAGs and reusable proof tooling remain authored and proven in Synexia. This target owns only target-local application, target-specific build/runtime truth, and eventual promotion after its handoff gate passes.

No donor source body is authorized by this handoff. LeetCode, HackerRank and GeeksforGeeks remain taxonomy/oracle evidence only. Third-party source retains its original license and provenance.

Verification order for later recipe outputs is fixed: diff/whitespace -> format/static analysis -> compile -> focused tests -> wider tests -> runtime/target-specific proof. Public API, ABI and observable behavior remain locked unless a later target PR explicitly unlocks them.
