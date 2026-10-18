# Shared RowWindow capacity candidate

This recipe-generated change continues SWT PR #72's shared viewport owner and
the UI contract in [Synexia PR #9164](https://github.com/hsoliwal/com.synexia/pull/9164).

- Retain requested viewport capacity when actual coverage is clipped by a short
  logical tail, a shrink/remove operation, or an empty model.
- Use that capacity when `ensureVisible` repositions the window.
- Check logical-count overflow before changing insertion coordinates.

Recipe custody: [Synexia draft PR #9194](https://github.com/hsoliwal/com.synexia/pull/9194),
commit `14211a7b22d3b9f72db3ef3ed36d914e38041dcb`, crate
`swt-row-window-capacity-v1`. The canonical engine is unchanged.

Baseline: `1a652f7a27f3c9ebb04db5afdafb08c3ce9f98ef`.
Preimage SHA-256: `08f5e69b725f91b22738832b6a723199d55f4fbecc67cc26f203fc15cd05490d`.
Postimage SHA-256: `8aeb64adf5a03dadccfcef6fe59c35dccc1340669db29098b06d681fdab91e02`.

Executed: exact OpenRewrite application (one changed file), unchanged second
application, Java 21 compilation of the shared owner, both adapters and five
model dependencies with `-Xlint:all -Werror`, and `git diff --check`.

Behavioral/refusal tests, native/platform/screenshots, complete reactors,
atom-packet reconciliation, strict M3 promotion and merged source readback
remain pending. This is a draft candidate.
