# Dedup raw BLLSE rows on (file, line, rule) and classify.
# Input files: wfpath.tsv (workflow -> yml), then rows (raw.tsv + annotations.tsv).
# Output: run_id workflow job step file line col severity rule message class count
BEGIN { FS = OFS = "\t" }
FNR == NR { wf[$1] = $2; next }
{
  file = $5
  if (file == "") file = ($2 in wf) ? wf[$2] : $2
  key = file SUBSEP $6 SUBSEP $9
  if (!(key in first)) { first[key] = ++n; order[n] = key; row[key] = $0; fil[key] = file }
  cnt[key]++
  if (index(";" runs[key] ";", ";" $1 ";") == 0) runs[key] = runs[key] (runs[key] == "" ? "" : ";") $1
  if (index(";" jobs[key] ";", ";" $3 ";") == 0) jobs[key] = jobs[key] (jobs[key] == "" ? "" : ";") $3
}
function cls(r, f, m) {
  if (r ~ /^SUREFIRE_/ || r ~ /maven-surefire-plugin/) return "FORK_SOURCE"
  if (r ~ /tycho-p2-extras/) return "FORK_SOURCE"
  if (r ~ /QUEUED_JOBS_LIMIT|FORK_WORKFLOWS_DISABLED/) return "INFRA"
  if (r == "GH_CLI_ERROR" && m ~ /Egress is over the account limit/) return "INFRA"
  if (r ~ /INVALID_WORKFLOW|EMPTY_MATRIX/) return "WORKFLOW_CONFIG"
  if (f ~ /version-increments/) return "INFRA"            # PLATFORM_BOT_PAT absent on fork
  if (f ~ /native-source-distillation/) return "INFRA"    # private repo token absent on fork
  if (f ~ /pr-checks/ && m ~ /version increments/) return "FORK_SOURCE"
  if (r == "GHA_ERROR" && m ~ /^Exception in thread/) return "UPSTREAM_BASE"
  if (r == "GHA_EXIT" && f ~ /maven\.yml/) return "FORK_SOURCE"
  if (r == "GHA_EXIT" && f ~ /junit\.yml/) return "INFRA"
  if (r == "GHA_EXIT" && f ~ /pr-checks/) return "FORK_SOURCE"
  return "UNCLASSIFIED"
}
END {
  print "run_id", "workflow", "job", "step", "file", "line", "col", "severity", "rule", "message", "class", "count"
  for (i = 1; i <= n; i++) {
    k = order[i]; split(row[k], c, "\t")
    print runs[k], c[2], jobs[k], c[4], fil[k], c[6], c[7], c[8], c[9], c[10], cls(c[9], fil[k], c[10]), cnt[k]
  }
}
