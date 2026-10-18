package org.eclipse.swt.tests.manual;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
		Button button;
		button = new Button(shell, SWT.PUSH);
		button.setText("Right Text");
		button.setImage(image);
		button.setSize(200, 50);
		button.setAlignment(SWT.RIGHT);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		button = new Button(shell, SWT.PUSH);
		button.setText("Left Text");
		button.setImage(image);
		button.setSize(200, 50);
		button.setAlignment(SWT.LEFT);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		button = new Button(shell, SWT.PUSH);
		button.setText("Center Text");
		button.setImage(image);
		button.setSize(200, 50);
		button.setAlignment(SWT.CENTER);

		// Set layout data to take up the whole shell
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		button = new Button(shell, SWT.PUSH);
		button.setText("No Alignment assumes CENTER");
		button.setImage(image);
		button.setSize(200, 50);
		//button.setAlignment(SWT.CENTER);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

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
}