# Initiative31 integration, 2026-10-16

## Why the Initiative31 pull requests could not merge

Pull requests #154, #156, #158, #170, #172 and #177 to #181 sit on the history of
[swt-initiative31/prototype-skija](https://github.com/swt-initiative31/prototype-skija), at its `master`
(`146eee2a23`, 2025-09-22, EPL-2.0). That is the custom-drawn SWT built on Skija 0.116.3.

The fork's `master` was re-rooted by parentless snapshot commits (`5094846b2f` for #114 and `0b6a31ff61` for #116),
so it shares no history with prototype-skija. Against `master`, every one of these pull requests shows more than 100 files
and about 227,000 deleted lines, and it conflicts. Their own changes are small:

| PR | Branch | Own commits | Files |
|---|---|---|---|
| #154 | m3/initiative31-list-primitive-selection-20261014 | 3 | 3 |
| #156 | m3/initiative31-selection-access-20261014 | 6 | 3 |
| #158 | m3/initiative31-direct-list-gc-lease-20261014 | 3 | 2 |
| #170 | m3/initiative31-drawing-ownership-20261014 | 6 | 5 |
| #172 | m3/initiative31-tree-demand-mediation-20261015 | 6 | 10 |
| #177 | m3/widget-owner-viewport-20261015 | 2 | 4 |
| #178 | m3/tree-scroll-geometry-current-20261015 | 3 | 5 |
| #179 | m3/initiative31-header-viewport-custody-20261015 | 4 | 7 |
| #180 | m3/list-paint-access-20261015 | 4 | 6 |
| #181 | m3/widget-state-bugfix-20261015 | 5 | 8 |

They belong on a base branch `initiative31/master` at `146eee2a23`. This branch merges all ten onto that base.

## Conflict decisions (viewport logic first)

| PR | File | Decision |
|---|---|---|
| #177 | `TreeItemsHandler.java` (3 blocks) | #177's loop structure and `windowLimit` overflow guard, with #172's long logical top index (`Math.toIntExact(tree.getTopIndex())`). `windowLimit` now takes the long last-visible index. |
| #178 | `Tree.java` `onScrollBar` | #172's logical scroll model (virtual metrics, end affinity), plus #178's column geometry cache reset. The non-virtual path goes through `setTopIndex`. |
| #178 | `Tree.java` `setTopIndex` | A virtual tree uses #172's logical top index. A non-virtual tree uses #178's endpoint clamp to `count - thumb` (no blank rows after the last item) and scroll bar sync, applied through `applyLogicalTopIndex`. |
| #180 | `DefaultListRenderer.java` | The bounded visible-row paint window (partial last row, no whole-list copy), with its count from #180's `paintItemCount()` (keeps subclass `getItems()` dispatch) and text from `paintItemText()`. |

## Verification

| Gate | Result |
|---|---|
| javac `--release 21`, win32 x86_64 source set, against the bundled Skija 0.116.3 jars | 737 sources, 0 errors |
| javac, the same source set at `146eee2a23` (baseline) | 733 sources, 0 errors |
| JUnit (JUnit 4 suite of the prototype) | NOT_RUN: on 2026-10-16 the build output directory was put under a write-deny storage hold by another agent on the host |

The prototype loads SWT natives `4969r18`, which ship in the official SWT 3.130.0 jar (Maven Central).

## Next

1. Create the base branch: `git push origin 146eee2a23faec511de96f2539f7d650ef24300e:refs/heads/initiative31/master`.
2. Retarget the ten pull requests: `gh pr edit <n> --base initiative31/master`.
3. Push this branch and open its pull request against `initiative31/master`.
4. Run the prototype's JUnit suite once the storage hold is lifted.
5. Bringing the custom-drawn widgets into the superset `master` is a separate design step: a Skija rendering backend beside the native win32/GTK/Cocoa widgets, not a replacement of them.
