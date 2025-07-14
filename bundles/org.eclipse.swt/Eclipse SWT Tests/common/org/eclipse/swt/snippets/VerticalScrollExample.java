package org.eclipse.swt.snippets;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class VerticalScrollExample {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
		shell.setText("SWT Native ScrollBar Example");
        shell.setLayout(new FillLayout());

		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);

		Composite content = new Composite(scrolledComposite, SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.marginLeft = 10;
		rowLayout.marginTop = 10;
		rowLayout.spacing = 5;
		content.setLayout(rowLayout);

		for (int i = 0; i < 50; i++) {
			new Label(content, SWT.NONE).setText("Label " + (i + 1));
        }

		content.pack();
		scrolledComposite.setContent(content);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		shell.setSize(400, 300);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
