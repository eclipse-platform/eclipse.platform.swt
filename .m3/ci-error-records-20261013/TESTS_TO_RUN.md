# SWT fork CI: tests to run

These checks were not run locally while the fixes were being written. Each row
names the CI_ERROR_RECORDS.tsv row it closes.

| # | Check | Where | Closes | Status |
|---|-------|-------|--------|--------|
| 1 | Re-enable workflows on the fork (Actions tab banner) | github.com UI, maintainer | GHA_BANNER:FORK_WORKFLOWS_DISABLED | needs the user |
| 2 | `SWT Matrix Build` does not start for a PR that changes only `**/*.md`, `docs/**` or `.m3/**` | GitHub, after 1 | load reduction | NOT_RUN |
| 3 | `Publish Unit Test Results` ends green with a "No test results" warning when the matrix run uploaded no `test-results-*` | GitHub, after 1 | GHA_ANNOTATION:EMPTY_MATRIX (junit.yml:77) | NOT_RUN |
| 4 | `Publish Version Check Results` skips `publish-version-check-results` and emits a notice when `PLATFORM_BOT_PAT` is absent | GitHub, after 1 | GIT_FATAL / ACTION_ERROR (version-increments.yml) | NOT_RUN |
| 5 | `SWT Native Source Distillation` checks out `hsoliwal/com.synexia` once `SYNEXIA_READ_TOKEN` is stored | GitHub, after 1 + secret | GHA_ERROR repository not found | NOT_RUN (secret missing) |
| 6 | `Pull-Request Checks / check-versions` green after the ole.win32 increment | GitHub, after 1 | MOJO tycho-p2-extras compare-version-with-baselines | NOT_RUN |
| 7 | `Test_org_eclipse_swt_widgets_Table#test_virtualDndHitPinsColdTableCoordinateWithoutDenseMaterialization` | GTK4 x11 and wayland, macOS x86_64 and aarch64 | Test_org_eclipse_swt_widgets_Table.java:163 | NOT_RUN (no GTK4 or macOS host) |
| 8 | `Test_org_eclipse_swt_widgets_Tree#test_virtualDndHitPinsColdTreeChildAcrossCollapseCompaction` | macOS | Test_org_eclipse_swt_widgets_Tree.java:367 | NOT_RUN (no macOS host) |
| 9 | `Test_org_eclipse_swt_widgets_Tree#test_virtualPackedStateFollowsLogicalInsertAndRemoveOnCocoa` | macOS | Test_org_eclipse_swt_widgets_Tree.java:1444 | NOT_RUN (no macOS host) |
| 10 | `Test_org_eclipse_swt_widgets_Text#test_backspaceAndDelete` | macOS | Test_org_eclipse_swt_widgets_Text.java:1585 | NOT_RUN (no macOS host) |

Regenerate the records from downloaded `gh run view <id> --log-failed` logs:

```sh
: > raw.tsv
cut -f1,2 runs.tsv | while IFS=$'\t' read id wf; do
  awk -v RUN="$id" -v WF="$wf" -f bllse.awk "logs/$id.log" >> raw.tsv
done
cat raw.tsv annotations.tsv > rows.tsv
awk -f records.awk wfpath.tsv rows.tsv | awk -F'\t' 'BEGIN{OFS="\t"}
  NR==1 {print $0, "in_latest_master_matrix_37772479345"; next}
  {s="n/a"; if ($9 ~ /^SUREFIRE|surefire/ || $5 ~ /maven\.yml/) s = ($1 ~ /37772479345/) ? "yes" : "no"; print $0, s}' \
  > CI_ERROR_RECORDS.tsv
```

`annotations.tsv` holds the run-page annotations that the logs API does not
return: invalid workflow, empty matrix, queued-jobs limit and the fork banner.
