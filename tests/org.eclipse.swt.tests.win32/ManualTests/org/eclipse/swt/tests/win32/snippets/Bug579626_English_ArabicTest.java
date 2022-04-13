package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

class Bug579626_English_ArabicTest {

    private static final String MIXED_TEXT = "English جارِ التحميل";

    public static void main(String[] args) {
        final Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());

        System.out.println("Console: " + MIXED_TEXT);

		// This is rendered correctly
        Group group = new Group (shell, SWT.NONE);
    	group.setLayout (new RowLayout (SWT.VERTICAL));
    	group.setText ("Group: " + MIXED_TEXT);

    	// This is rendered correctly
        Text text = new Text(group, SWT.BORDER);
        text.setText("Text: " + MIXED_TEXT);

		// This is rendered correctly
		Button push = new Button(group, SWT.PUSH);
		push.setText("Push: " + MIXED_TEXT);
		Button check = new Button(group, SWT.CHECK);
		check.setText("Check: " + MIXED_TEXT);
		Button radio = new Button(group, SWT.RADIO);
		radio.setText("Radio: " + MIXED_TEXT);
		Button toggle = new Button(group, SWT.TOGGLE);
		toggle.setText("Toggle: " + MIXED_TEXT);

		// This is rendered correctly
		Link link = new Link(group, SWT.PUSH);
		link.setText("<a>Link: " + MIXED_TEXT + "</a>");

		// This is rendered correctly
		Label label = new Label(group, SWT.PUSH);
		label.setText("Label: " + MIXED_TEXT);

		// This is rendered correctly
		Combo combo = new Combo(group, SWT.DROP_DOWN);
		combo.add("Combo: " + MIXED_TEXT);
		combo.select(0);

		// This is rendered correctly
		List list = new List(group, SWT.MULTI | SWT.BORDER);
		list.add("This is a List");
		list.add(MIXED_TEXT);
		list.select(1);

		// This is rendered correctly
		final Table table = new Table(group, SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Table No");
		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(MIXED_TEXT);
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"1", MIXED_TEXT});
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"2", MIXED_TEXT});
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"3", MIXED_TEXT});
		column1.setWidth(100);
		column2.setWidth(200);

		// This is rendered correctly
		final Tree tree = new Tree (group, SWT.BORDER);
		tree.setHeaderVisible(true);
		TreeColumn column = new TreeColumn(tree, SWT.LEFT);
		column.setText("Tree Header: " + MIXED_TEXT);
		column.setWidth(300);
		for (int i=1; i<2; i++) {
			TreeItem iItem = new TreeItem (tree, 0);
			iItem.setText (new String[] { i + ": " + MIXED_TEXT});
			for (int j=1; j<4; j++) {
				TreeItem jItem = new TreeItem (iItem, 0);
				jItem.setText (new String[] { i + "." + j + ": " + MIXED_TEXT});
			}
			iItem.setExpanded(true);
		}

        // This is not rendered correctly
        StyledText styled = new StyledText(shell, SWT.BORDER | SWT.MULTI);
        styled.setText("StyledText: " + MIXED_TEXT);

        shell.pack();
        shell.open();

        while(!shell.isDisposed()) {
            if(!display.readAndDispatch()) display.sleep();
        }
    }
}
