package org.eclipse.swt.tests.manual;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Issue0519_ButtonAlignmentExample {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Button Alignment Example for Issue0519");
		shell.setLayout(new GridLayout(1, false));

		// Load an image (you should replace "imagePath" with your image file path)
		//Image image = ImageDescriptor.createFromFile(Snippet.class, "error_tsk.png").createImage();
		Image image = new Image(display, "data/eclipse32.png");

		// Create a button with text and image
		Button btnRight = createButton("Right Text", image, shell);
		btnRight.setAlignment(SWT.RIGHT);

		Button btnLeft = createButton("Left Text", image, shell);
		btnLeft.setAlignment(SWT.LEFT);

		Button btnCenter = createButton("Center Text", image, shell);
		btnCenter.setAlignment(SWT.CENTER);

		Button btnNoAlign = createButton("No Alignment assumes CENTER", image, shell);

		Button enabledCheckbox = new Button(shell, SWT.CHECK);
		enabledCheckbox.setText("Enable");
		enabledCheckbox.setSelection(true);
		enabledCheckbox.addListener(SWT.Selection, event -> {
			boolean enabled = enabledCheckbox.getSelection();
			btnRight.setEnabled(enabled);
			btnLeft.setEnabled(enabled);
			btnCenter.setEnabled(enabled);
			btnNoAlign.setEnabled(enabled);
		});

		shell.setSize(500, 300);
		shell.open();
		//shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		// Dispose of the image and resources
		image.dispose();
		display.dispose();
	}

	private static Button createButton(String text, Image image, Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		button.setImage(image);
		button.setForeground(new Color(0, 0, 128));
		button.setSize(200, 50);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return button;
	}
}