// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.internal.residency;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.SplittableRandom;

/** Tests actual private atoms and retains independent scalar fallback semantics. No GUI proof. */
public final class PatternWordContract {
    private static long checks, digest;
    private static MethodHandle count, next;
    private PatternWordContract() { }
    private static void initialize() throws ReflectiveOperationException {
        var lookup = MethodHandles.privateLookupIn(ItemLanes.class, MethodHandles.lookup());
        count = lookup.findStatic(ItemLanes.class, "patternCountIn",
                MethodType.methodType(int.class, int[].class, int.class, int.class, int.class));
        next = lookup.findStatic(ItemLanes.class, "patternNext",
                MethodType.methodType(int.class, int[].class, int.class, int.class));
    }
    private static void eq(int expected, int actual) {
        checks++;
        if (expected != actual) throw new AssertionError("case " + checks + ": " + expected + " != " + actual);
        digest = Long.rotateLeft(digest, 5) ^ actual ^ checks;
    }
    private static boolean bit(int[] p, int item) {
        int phase = (item - p[0]) % p[2] + p[4];
        if (phase >= p[2]) phase -= p[2];
        return ((p[3] >>> phase) & 1) != 0;
    }
    private static int scalarNext(int[] p, int from) {
        int item = Math.max(from, p[0]);
        for (int i = 0; i < p[2] && item < p[1]; i++, item++) if (bit(p, item)) return item;
        return -1;
    }
    private static int scalarBoundedCount(int[] p, int from, int to) {
        int result = 0;
        for (int item = from; item < to; item++) if (bit(p, item)) result++;
        return result;
    }
    /** Exact old arithmetic is used only outside the canonical domain and for huge ranges. */
    private static int legacyCount(int[] p, int from, int to) {
        int stride=p[2], mask=p[3];
        if (stride<32) mask &= (1<<stride)-1;
        int length=to-from, total=length/stride*Integer.bitCount(mask);
        int phase=(from-p[0])%stride+p[4];
        for(int i=0,rest=length%stride;i<rest;i++,phase++) {
            if(phase>=stride) phase-=stride;
            total += (mask>>>phase)&1;
        }
        return total;
    }
    private static void one(int[] p, int from, int to, boolean bounded) throws Throwable {
        int expected=bounded?scalarBoundedCount(p,from,to):legacyCount(p,from,to);
        eq(expected,(int)count.invokeExact(p,0,from,to));
        eq(scalarNext(p,from),(int)next.invokeExact(p,0,from));
    }
    private static void exhaustive() throws Throwable {
        int[] p=new int[5]; p[0]=37;
        for(int s=1;s<=10;s++) for(int mask=0;mask<(1<<s);mask++) for(int phase=0;phase<s;phase++) {
            p[2]=s; p[3]=mask; p[4]=phase;
            for(int rest=0;rest<s;rest++) {
                p[1]=37+2*s+rest;
                one(p,37,p[1],true);
                // A shortened interval exercises rejection of a set bit beyond the end.
                p[1]=37+rest;
                one(p,37,p[1],true);
            }
        }
    }
    private static void wideAndHigh() throws Throwable {
        int[] p=new int[5];
        int[] masks={0,-1,1,Integer.MIN_VALUE,0xaaaaaaaa,0x55555555,0x80000001,0x13579bdf};
        for(int origin : new int[]{0,65536,Integer.MAX_VALUE-160}) {
            for(int s=1;s<=32;s++) for(int phase=0;phase<s;phase++) {
                for(int bit=-8;bit<32;bit++) {
                    p[0]=origin; p[1]=origin+128; p[2]=s; p[3]=bit<0?masks[bit+8]:1<<bit; p[4]=phase;
                    for(int offset=0;offset<s;offset++) {
                        int from=origin+offset;
                        one(p,from,from+s+1,true);
                        eq(scalarNext(p,p[1]-1),(int)next.invokeExact(p,0,p[1]-1));
                        eq(-1,(int)next.invokeExact(p,0,p[1]));
                    }
                }
            }
        }
        for(int s=1;s<=32;s++) for(int mask:masks) {
            p[0]=0;p[1]=Integer.MAX_VALUE;p[2]=s;p[3]=mask;p[4]=s-1;
            one(p,0,Integer.MAX_VALUE,false);
            one(p,Integer.MAX_VALUE-100,Integer.MAX_VALUE,true);
        }
    }
    private static void fallback() throws Throwable {
        int[] p=new int[5];var r=new SplittableRandom(0xFA11BACCL);
        for(int i=0;i<10000;i++) {
            p[0]=r.nextBoolean()?r.nextInt(-200,0):Integer.MIN_VALUE;
            p[1]=r.nextInt(-40,201);p[2]=r.nextInt(1,34);p[3]=r.nextInt();p[4]=r.nextInt(-40,65);
            int from=r.nextInt(-50,100),to=from+r.nextInt(-5,70);
            one(p,from,to,false);
        }
        p[0]=0;p[1]=100;p[2]=0;p[3]=1;p[4]=0;
        try { int ignored=(int)count.invokeExact(p,0,1,2); throw new AssertionError("zero stride accepted: "+ignored); }
        catch(ArithmeticException expected) {checks++;}
        eq(-1,(int)next.invokeExact(p,0,1));
    }
    private static void benchmark() throws Throwable {
        int[] p={37,137,32,0,17};long sink=0;
        for(String mode:new String[]{"zero","sparse","dense","mixed"}) {
            for(int warm=0;warm<2;warm++) for(int i=0;i<100000;i++) {
                p[3]=mode.equals("zero")?0:mode.equals("sparse")?Integer.MIN_VALUE:mode.equals("dense")?-1:i*0x9e3779b9;
                sink+=(int)next.invokeExact(p,0,38);sink+=(int)count.invokeExact(p,0,38,99);
            }
            for(int trial=0;trial<3;trial++) {
                long start=System.nanoTime();
                for(int i=0;i<300000;i++) {
                    p[3]=mode.equals("zero")?0:mode.equals("sparse")?Integer.MIN_VALUE:mode.equals("dense")?-1:i*0x9e3779b9;
                    sink+=(int)next.invokeExact(p,0,38);sink+=(int)count.invokeExact(p,0,38,99);
                }
                System.out.println("MICROPROBE mode="+mode+" operations=600000 ns="+(System.nanoTime()-start)+" sink="+sink);
            }
        }
    }
    public static void verify() throws Throwable {
        initialize();checks=0;digest=0;exhaustive();wideAndHigh();fallback();
        System.out.println("PATTERN_WORD checks="+checks+" digest="+Long.toHexString(digest));
    }
    public static void main(String[] args) throws Throwable {
        initialize();
        if(args.length==1&&args[0].equals("--benchmark")) benchmark();
        else if(args.length==0) verify();else throw new IllegalArgumentException("[--benchmark]");
    }
}
