package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Example usage of BarChart.
 */
public class SnippetBarChart {
    private static final boolean USE_SKIA = true;

	public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("BarChart Example");
        shell.setLayout(new GridLayout(1, false));

        org.eclipse.swt.examples.accessibility.BarChart chart = new org.eclipse.swt.examples.accessibility.BarChart(shell,  SWT.BORDER | (USE_SKIA ? SWT.SKIA : SWT.NONE));
        chart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        chart.setTitle("Sample Bar Chart");
        chart.setColor(SWT.COLOR_GREEN);
        chart.setValueMin(0);
        chart.setValueMax(10);
        chart.setValueIncrement(1);
        chart.addData("A", 2);
        chart.addData("B", 7);
        chart.addData("C", 5);
        chart.addData("D", 9);
        chart.addData("E", 4);

        shell.setSize(400, 250);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
