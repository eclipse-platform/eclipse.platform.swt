"""Pixel diff of two PNGs written by SWT ImageLoader: count and bounding box of differing pixels, plus row histogram."""
import sys, zlib, struct
def load(p):
    b=open(p,'rb').read(); i=8; idat=b''
    while i < len(b):
        n=struct.unpack('>I',b[i:i+4])[0]; t=b[i+4:i+8]; d=b[i+8:i+8+n]; i+=12+n
        if t==b'IHDR': w,h,bd,ct=struct.unpack('>IIBB',d[:10])
        elif t==b'IDAT': idat+=d
    raw=zlib.decompress(idat); bpp={2:3,6:4}[ct]; stride=w*bpp; rows=[]; prev=bytearray(stride); k=0
    for y in range(h):
        f=raw[k]; k+=1; line=bytearray(raw[k:k+stride]); k+=stride
        for x in range(stride):
            a=line[x-bpp] if x>=bpp else 0; up=prev[x]; c=prev[x-bpp] if x>=bpp else 0
            if f==1: line[x]=(line[x]+a)&255
            elif f==2: line[x]=(line[x]+up)&255
            elif f==3: line[x]=(line[x]+((a+up)>>1))&255
            elif f==4:
                pp=a+up-c; pa,pb,pc=abs(pp-a),abs(pp-up),abs(pp-c)
                line[x]=(line[x]+(a if pa<=pb and pa<=pc else (up if pb<=pc else c)))&255
        rows.append(bytes(line)); prev=line
    return w,h,bpp,rows
a,b=sys.argv[1],sys.argv[2]
w,h,bpp,ra=load(a); _,_,_,rb=load(b); n=0; box=None; rowsd={}
for y in range(h):
    if ra[y]!=rb[y]:
        for x in range(w):
            if ra[y][x*bpp:x*bpp+bpp]!=rb[y][x*bpp:x*bpp+bpp]:
                n+=1; rowsd[y]=rowsd.get(y,0)+1
                box=(x,y,x,y) if box is None else (min(box[0],x),min(box[1],y),max(box[2],x),max(box[3],y))
print(f"{w}x{h} diff={n} box={box} rows={sorted(rowsd.items())[:12]}")
