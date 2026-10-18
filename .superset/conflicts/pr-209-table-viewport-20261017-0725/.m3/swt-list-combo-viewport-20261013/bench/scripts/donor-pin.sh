#!/bin/sh
# Pins donor repo HEAD commits and fetches the specific files read for patterns (read-only, no clone).
# output: donor-read/<repo>__<file>, logs/donor-pin.tsv (repo, commit, path, bytes, sha256)
cd /c/m3-work-c/lc-bench
out=logs/donor-pin.tsv; : > $out
pin() { repo=$1; path=$2
  sha=$(timeout 60 gh api "repos/$repo/commits/HEAD" --jq .sha 2>/dev/null)
  f="donor-read/$(echo "$repo" | tr / _)__$(basename "$path")"
  timeout 90 gh api -H "Accept: application/vnd.github.raw" "repos/$repo/contents/$path?ref=$sha" > "$f" 2>/dev/null
  printf '%s\t%s\t%s\t%s\t%s\n' "$repo" "$sha" "$path" "$(wc -c < "$f")" "$(sha256sum "$f" | cut -d' ' -f1)" >> $out
}
pin ocornut/imgui imgui.cpp
pin ocornut/imgui imgui.h
pin bvaughn/react-window lib/components/list/List.tsx
pin TanStack/virtual packages/virtual-core/src/index.ts
pin AvaloniaUI/Avalonia src/Avalonia.Controls/VirtualizingStackPanel.cs
pin dotnet/wpf src/Microsoft.DotNet.Wpf/src/PresentationFramework/System/Windows/Controls/VirtualizingStackPanel.cs
pin microsoft/microsoft-ui-xaml controls/dev/Repeater/ViewportManager.cpp
pin androidx/androidx compose/foundation/foundation/src/commonMain/kotlin/androidx/compose/foundation/lazy/LazyList.kt
pin qt/qtbase src/widgets/itemviews/qlistview.cpp
pin GNOME/gtk gtk/gtklistview.c
pin MicrosoftDocs/win32 desktop-src/Controls/list-box-styles.md
pin MicrosoftDocs/win32 desktop-src/Controls/lb-setcount.md
pin MicrosoftDocs/win32 desktop-src/Controls/combo-box-styles.md
echo DONOR-PIN-DONE
