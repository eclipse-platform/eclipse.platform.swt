package org.eclipse.swt.widgets;

public final class GateJava {
	public static void main (String [] args) {
		for (int seed = 1; seed <= 8; seed++) System.out.println (GateWorkload.run (seed, 400));
	}
}
