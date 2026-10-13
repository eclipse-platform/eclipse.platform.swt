import sys, re, difflib, subprocess
def norm(src):
    src = re.sub(r'/\*.*?\*/', '', src, flags=re.S)
    src = re.sub(r'//[^\n]*', '', src)
    src = re.sub(r'[\s{}]+', '', src)
    return [s for s in re.split(r';', src) if s]
def show(rev, path):
    return subprocess.run(['git', 'show', f'{rev}:{path}'], capture_output=True, text=True, encoding='utf-8', errors='replace').stdout
a, b, path = sys.argv[1:4]
A, B = norm(show(a, path)), norm(show(b, path))
for l in difflib.unified_diff(A, B, lineterm='', n=0):
    if l.startswith(('---', '+++', '@@')): continue
    print(l[:400])
