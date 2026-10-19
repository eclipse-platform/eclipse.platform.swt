/* SPDX-License-Identifier: Apache-2.0 OR EPL-2.0 */
package org.eclipse.swt.widgets;

import java.util.Arrays;

/** Executes the complete original planner; long arithmetic is the independent oracle. */
public final class VirtualRowWindowGeometryProof {
    private static long checks;
    private VirtualRowWindowGeometryProof() { }
    private static void eq(long a,long b) { checks++; if(a!=b)throw new AssertionError(a+" != "+b); }
    private static VirtualRowWindowPlan plan(int first,int size) {
        var p=new VirtualRowWindowPlan(); p.plan(first,size);p.commit(); return p;
    }
    private static int[] state(VirtualRowWindowPlan p) {
        return new int[]{p.first(),p.size(),p.end(),p.plannedFirst(),p.plannedSize(),p.retiredHead(),
                p.retiredTail(),p.insertedHead(),p.insertedTail(),p.replaced()?1:0};
    }
    private static void reject(VirtualRowWindowPlan p,int row,int count,boolean insert) {
        int[] before=state(p);
        try { if(insert)p.inserted(row,count);else p.removed(row,count);throw new AssertionError("missing refusal"); }
        catch(IllegalArgumentException e){ if(!e.getMessage().equals(insert?"insert":"remove"))throw e; checks++; }
        if(!Arrays.equals(before,state(p)))throw new AssertionError("refusal changed state");checks++;
    }
    private static void boundaries() {
        int max=Integer.MAX_VALUE;
        var p=plan(max-4,4);p.plan(10,20);reject(p,0,1,true);reject(p,max-3,1,true);
        reject(p,max,1,true);reject(p,max-2,5,false);reject(p,-1,0,false);
        reject(p,1,-1,true);eq(-1,p.inserted(max,0));eq(0,p.removed(max,0));
        p=plan(100,40);reject(p,max-2,5,false);eq(100,p.first());eq(40,p.size());
        p=plan(max-5,4);eq(1,p.inserted(max-4,1));eq(max,p.end());
        p=plan(max,0);reject(p,max,1,true);reject(p,0,1,true);
        p=plan(100,40);eq(-1,p.inserted(200,max-200));eq(140,p.end());
    }
    private static void ordinary() {
        for(int first=0;first<64;first++)for(int size=0;size<33;size++)
            for(int row=0;row<100;row++)for(int count:new int[]{0,1,7,31}) {
                var p=plan(first,size);int expected=-1;long f=first,s=size;
                if(count!=0) {
                    if(row<first || row==first&&size==0)f+=count;
                    else if(row<first+size){s+=count;expected=row-first;}
                }
                eq(expected,p.inserted(row,count));eq(f,p.first());eq(s,p.size());eq(f+s,p.end());
                p=plan(first,size);long end=(long)row+count;
                long before=Math.max(0,Math.min(end,first)-row);
                long removed=Math.max(0,Math.min(end,(long)first+size)-Math.max(row,first));
                eq(removed,p.removed(row,count));eq(first-before,p.first());eq(size-removed,p.size());
                eq((long)first-before+size-removed,p.end());
            }
    }
    public static void main(String[] args) {
        if(args.length>0&&args[0].equals("overflow-witness")) {
            var p=plan(100,40);p.removed(Integer.MAX_VALUE-2,5);
            System.out.println("BASELINE_AFTER_OVERFLOW first="+p.first()+" size="+p.size());
            if(p.first()!=100||p.size()!=40)throw new AssertionError("wrapped removal mutated unrelated viewport");
            return;
        }
        if(args.length==0 || !args[0].equals("ordinary"))boundaries();
        ordinary();System.out.println("SWT_ROW_WINDOW_PASS checks="+checks);
    }
}
