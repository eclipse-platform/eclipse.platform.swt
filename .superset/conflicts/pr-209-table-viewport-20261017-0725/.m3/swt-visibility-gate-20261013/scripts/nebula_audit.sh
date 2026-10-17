#!/bin/sh
# NEBULA_HIDDEN_WORK.tsv: grep-only audit of hsoliwal/nebula production sources.
# Categories:
#  PAINT_IGNORES_CLIP   paint handler in file, no read of event x/y/width/height or gc.getClipping
#  FULL_CLIENT_BUFFER   new Image(...) sized from getClientArea()/getSize()/getBounds() in a file with a paint handler
#  TIMER_NO_VIS_CHECK   timerExec in file, no isVisible()/VisibilityGate check in file
#  TIMER_WITH_VIS_CHECK timerExec in file and an isVisible() check exists (still runs while covered/clipped)
# Matches on comment lines (*, //, /*) are ignored.
cd /c/m3-work-c/nebula || exit 1
rev=$(git rev-parse HEAD)
printf 'module\tfile\tline\tcategory\tevidence\tnebula_commit\n'
find . -name '*.java' -path '*/src/*' | grep -v -i -E '/tests?/|[.]tests?/|snippet|example|/demo' | sort | while read -r f; do
  mod=$(echo "$f" | sed -E 's#^\./widgets/([^/]+)/.*#\1#; s#^\./([^/]+)/.*#\1#')
  ph=$(grep -n -E 'SWT\.Paint\b|addPaintListener|paintControl *\(' "$f" | grep -v -E '^[0-9]+:[[:space:]]*(\*|//|/\*)' | head -1 | cut -d: -f1)
  if [ -n "$ph" ]; then
    if ! grep -q -E '\b(e|event|evt|pe|paintEvent|ev)\.(x|y|width|height)\b|getClipping *\(' "$f"; then
      printf '%s\t%s\t%s\tPAINT_IGNORES_CLIP\t%s\t%s\n' "$mod" "${f#./}" "$ph" "$(sed -n "${ph}p" "$f" | tr -d '\t' | cut -c1-100)" "$rev"
    fi
    grep -n -E 'new Image *\(' "$f" | while IFS=: read -r ln rest; do
      win=$(sed -n "$((ln>3?ln-3:1)),$((ln+1))p" "$f")
      if echo "$win" | grep -q -E 'getClientArea *\(|getSize *\(|getBounds *\('; then
        printf '%s\t%s\t%s\tFULL_CLIENT_BUFFER\t%s\t%s\n' "$mod" "${f#./}" "$ln" "$(echo "$rest" | tr -d '\t' | cut -c1-100)" "$rev"
      fi
    done
  fi
  tl=$(grep -n -E 'timerExec *\(' "$f" | grep -v -E '^[0-9]+:[[:space:]]*(\*|//|/\*)' | head -1 | cut -d: -f1)
  if [ -n "$tl" ]; then
    if grep -q -E 'isVisible *\(\)|VisibilityGate' "$f"; then cat=TIMER_WITH_VIS_CHECK; else cat=TIMER_NO_VIS_CHECK; fi
    printf '%s\t%s\t%s\t%s\t%s\t%s\n' "$mod" "${f#./}" "$tl" "$cat" "$(sed -n "${tl}p" "$f" | tr -d '\t' | cut -c1-100)" "$rev"
  fi
done
