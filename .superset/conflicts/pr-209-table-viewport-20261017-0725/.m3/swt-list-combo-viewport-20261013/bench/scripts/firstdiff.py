import sys, io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
a_name = sys.argv[1] if len(sys.argv) > 1 else 'after'
base=[l.rstrip('\n').split('\t') for l in open('traces/base.trace',encoding='utf-8')]
aft=[l.rstrip('\n').split('\t') for l in open('traces/%s.trace' % a_name,encoding='utf-8')]
seen=set(); hist={}
for i,(b,a) in enumerate(zip(base,aft)):
    sc=b[0]
    if not b[2].startswith('state.'): hist.setdefault(sc,[]).append((b[2],b[3][:30],a[3][:30]))
    if b!=a and sc not in seen:
        seen.add(sc)
        ops=' ; '.join('%s=%s/%s'%h if h[1]!=h[2] else h[0] for h in hist.get(sc,[])[-3:])
        print(sc, '|', ops, '| diff:', b[2], 'base=',b[3][:60], 'after=',a[3][:60])
