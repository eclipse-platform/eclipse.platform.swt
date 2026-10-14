# Win32 Tree item mediation

Provider exceptions previously left Tree.currentItem pointing to a failed SetData item. Public clearAll(true) then skipped that item and a retry returned empty data. The Tree now restores the previous callback context in finally, including nested callbacks and provider Error paths.

TreeItem keeps its logical ID while its facade lives. Tree validates that the current items slot still belongs to that exact facade before using the ID, and uses the CPU topology for the SetData child index when it owns the slot. The original native lookup remains available for other cases.

The unchanged native Win32 public-API harness fails three recovery cases on master8d607fc and passes all nine on this candidate. Qualification also includes fifteen canonical recipe checks, twenty-four source-body behavior checks, eight harness recipe checks, compilation of both actual widget owners, and identical complete public/protected javap declarations. The single source-aware review reports no findings; its supervisor required cleanup of a remaining owned descendant after the review helper exited successfully. The original supervisor result and cleanup evidence are preserved.

The Maven recipes, before/after images, tests, source pins, donor decisions and executed evidence are maintained together in [Synexia PR11379](https://github.com/hsoliwal/com.synexia/pull/11379). Generated widget sources retain their original EPL notices.

This change qualifies item identity and public SetData mediation. Bounded native peer retirement, distant row tracking, both reported scrollbar defects and GPU painting remain separate open work.
