# BLLSE extractor for `gh run view --log-failed` output.
# Input line: <job>\t<step>\t<timestamp> <text>
# Output TSV: run_id workflow job step file line col severity rule message
# Usage: awk -v RUN=<id> -v WF=<workflow> -f bllse.awk <log>
BEGIN { FS = "\t"; OFS = "\t" }
function emit(job, step, file, line, col, sev, rule, msg) {
  gsub(/\t/, " ", msg); gsub(/\r/, "", msg)
  print RUN, WF, job, step, file, line, col, sev, rule, substr(msg, 1, 300)
}
{
  job = $1; step = $2; text = $0
  sub(/^[^\t]*\t[^\t]*\t/, "", text)
  sub(/^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9:.]+Z ?/, "", text)
  gsub(/\x1b\[[0-9;]*m/, "", text)
}
# surefire failure header -> open block
text ~ /^\[ERROR\] [A-Za-z0-9_.$]+ -- Time elapsed: .* <<< (FAILURE|ERROR)!/ {
  pend = 1; pjob = job; pstep = step; pmsg = ""
  pkind = (text ~ /<<< ERROR!/) ? "SUREFIRE_ERROR" : "SUREFIRE_FAILURE"
  ptest = text; sub(/^\[ERROR\] /, "", ptest); sub(/ -- Time elapsed.*/, "", ptest)
  next
}
pend && pmsg == "" && text !~ /^[ \t]*at / { pmsg = text; next }
pend && text ~ /^[ \t]*at org\.eclipse\.swt\.tests\./ {
  fr = text; sub(/.*\(/, "", fr); sub(/\).*/, "", fr)
  split(fr, a, ":")
  exc = pmsg; sub(/:.*/, "", exc); n = split(exc, e, "."); exc = e[n]
  emit(pjob, pstep, a[1], a[2], "", "ERROR", pkind ":" exc, ptest " | " pmsg)
  pend = 0; next
}
pend && text ~ /^\[(ERROR|INFO|WARNING)\]/ {
  emit(pjob, pstep, "", "", "", "ERROR", pkind ":NOFRAME", ptest " | " pmsg); pend = 0
}
# javac via maven
text ~ /^\[ERROR\] .*\.java:\[[0-9]+,[0-9]+\]/ {
  m = text; sub(/^\[ERROR\] /, "", m)
  f = m; sub(/:\[.*/, "", f); n = split(f, p, "/"); f = p[n]
  lc = m; sub(/^[^[]*\[/, "", lc); sub(/\].*/, "", lc); split(lc, q, ",")
  msg = m; sub(/^[^]]*\] ?/, "", msg)
  emit(job, step, f, q[1], q[2], "ERROR", "JAVAC", msg); next
}
# gcc / clang
text ~ /^[^ :]+\.(c|h|m|cpp):[0-9]+:[0-9]+: (fatal )?error:/ {
  split(text, g, ":"); n = split(g[1], p, "/")
  msg = text; sub(/^[^:]+:[0-9]+:[0-9]+: /, "", msg)
  emit(job, step, p[n], g[2], g[3], "ERROR", "CC", msg); next
}
# maven goal failure
text ~ /^\[ERROR\] Failed to execute goal / {
  pl = text; sub(/^\[ERROR\] Failed to execute goal /, "", pl); sub(/ .*/, "", pl)
  n = split(pl, pp, ":"); rule = "MOJO:" pp[1] ":" pp[2] ":" pp[n]
  sub(/:[^:]*$/, "", rule)
  proj = text; sub(/.* on project /, "", proj); sub(/:.*/, "", proj)
  msg = text; sub(/.* on project [^:]+: /, "", msg); sub(/ -> \[Help 1\]$/, "", msg)
  emit(job, step, proj, "", "", "ERROR", rule, msg); next
}
text ~ /^error [a-z]+ing / { emit(job, step, "", "", "", "ERROR", "GH_CLI_ERROR", text); next }
text ~ /^fatal: / { emit(job, step, "", "", "", "ERROR", "GIT_FATAL", text); next }
text ~ /^Error: / { emit(job, step, "", "", "", "ERROR", "ACTION_ERROR", text); next }
text ~ /^##\[error\]/ {
  msg = text; sub(/^##\[error\]/, "", msg)
  rule = (msg ~ /^Process completed with exit code/) ? "GHA_EXIT" : "GHA_ERROR"
  emit(job, step, "", "", "", "ERROR", rule, msg); next
}
