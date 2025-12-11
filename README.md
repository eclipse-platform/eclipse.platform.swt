[![SWT Matrix Build](https://github.com/eclipse-platform/eclipse.platform.swt/actions/workflows/maven.yml/badge.svg)](https://github.com/eclipse-platform/eclipse.platform.swt/actions/workflows/maven.yml)
[![Publish Unit Test Results](https://github.com/eclipse-platform/eclipse.platform.swt/actions/workflows/junit.yml/badge.svg)](https://github.com/eclipse-platform/eclipse.platform.swt/actions/workflows/junit.yml)
[![License](https://img.shields.io/github/license/eclipse-platform/eclipse.platform)](https://github.com/eclipse-platform/eclipse.platform.swt/blob/master/LICENSE)

# About

SWT is a cross-platform GUI library for JVM based desktop applications.
The best known SWT-based application is [Eclipse](https://www.eclipse.org).

For more information about SWT, visit the [official SWT page](https://eclipse.dev/eclipse/swt/).

## Getting Started

SWT comes with platform-specific jar files.
Download them from https://download.eclipse.org/eclipse/downloads/index.html and add the jar file to your classpath.

### Example
![Example](example.png)
```java
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class HelloWorld {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setText("Hello World");
		shell.setLayout(new GridLayout(2, false));

		final Label label = new Label(shell, SWT.LEFT);
		label.setText("Your &Name:");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		final Text text = new Text(shell, SWT.BORDER | SWT.SINGLE);
		final GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		data.minimumWidth = 120;
		text.setLayoutData(data);

		final Button button = new Button(shell, SWT.PUSH);
		button.setText("Say Hello");
		shell.setDefaultButton(button);
		button.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false, 2, 1));

		final Label output = new Label(shell, SWT.CENTER);
		output.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		button.addListener(SWT.Selection, event -> {
			String name = text.getText().trim();
			if (name.length() == 0) {
				name = "world";
			}
			output.setText("Hello " + name + "!");
		});

		shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
```
First, a `Display` is created which is something like the central place of all GUI-related code.
Then a `Shell` is created which in our example is a top-level window.
Then all child controls and listeners are created, including their layout information.
Finally, we set the window's size determines by its child controls and open the window.
The `while`-loop processes all GUI related events until the shell is disposed which happens when closing.
Before exiting, any claimed GUI resources needs to be freed.


# Contributing to SWT

Thanks for your interest in this project.

For information about contributing to Eclipse Platform in general, see the general [CONTRIBUTING](https://github.com/eclipse-platform/.github/blob/main/CONTRIBUTING.md) page.

[![Create Eclipse Development Environment for Eclipse SWT](https://download.eclipse.org/oomph/www/setups/svg/SWT.svg)](
https://www.eclipse.org/setups/installer/?url=https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.swt/master/bundles/org.eclipse.swt.tools/Oomph/PlatformSWTConfiguration.setup&show=true
"Click to open Eclipse-Installer Auto Launch or drag into your running installer")


## Developer Resources

See the following description for how to contribute a feature or a bug fix to SWT.

- <https://www.eclipse.org/swt/fixbugs.php>

Information regarding source code management, builds, coding standards, and more can be found under the following link.

- <https://projects.eclipse.org/projects/eclipse.platform.swt/developer>

Also see in the SWT section of the Eclipse FAQ for more background information about SWT.

- <https://github.com/eclipse-platform/eclipse.platform/blob/master/docs/FAQ/The_Official_Eclipse_FAQs.md#standard-widget-toolkit-swt>

## Contributor License Agreement

Before your contribution can be accepted by the project, you need to create and electronically sign the Eclipse Foundation Contributor License Agreement (CLA).

- <http://www.eclipse.org/legal/CLA.php>

## Contact

Contact the project developers via GitHub:

- [GitHub Discussions](https://github.com/eclipse-platform/eclipse.platform.swt/discussions) - For questions and general discussions
- [GitHub Issues](https://github.com/eclipse-platform/eclipse.platform.swt/issues) - For bug reports and feature requests

You can register bugs and feature requests in the GitHub Issue Tracker. Remember that contributions are always welcome!

Please bear in mind that this project is almost entirely developed by volunteers. If you do not provide the implementation yourself (or pay someone to do it for you), the bug might never get fixed. If it is a serious bug, other people than you might care enough to provide a fix.

**Note:** SWT used to track ongoing development and issues in [Bugzilla](https://bugs.eclipse.org/bugs/buglist.cgi?product=Platform&component=SWT), but we now use GitHub Issues and Discussions.


# Evaluation of a Single, Cross-Platform SWT Implementation

In project [Initiative 31](https://github.com/swt-initiative31) an evaluation on the feasibility of implementing SWT in an OS-agnostic way based on a cross-platform graphics/widgets library has been conducted.
The goal was to assess if the existing three implementations could eventually be replaced by an OS-agnostic one in order to reduce maintenance efforts, enable highly customizable look and feel, and improve configurability.
A demonstrator based on the graphics library Skia was implemented and multiple prototypes for mitigating risks have been implemented, such that bringing that to a production-ready implementation is a matter of effort but not of conceptual or technical/technological risk.

The actual prototyping work has started on four technologies: Skia with Visual Class Library (VCL), Skia with custom-implemented widgets, GTK, and Swing\
All those prototypes can be found in the according GitHub organization: https://github.com/swt-initiative31 \
The work finally focused on the Skia graphics library with custom-implemented widgets: https://github.com/swt-initiative31/prototype-skija

The project concluded with the feasibility demonstration and a [report summarizing the results](https://github.com/swt-initiative31/documents/blob/main/report/overall_report.md).
Given sufficient resources, a follow-up project could convert the demonstrator into a production-ready state.
