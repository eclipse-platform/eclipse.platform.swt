/*******************************************************************************
 * Copyright (c) 2019, 2022 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.io.*;
import java.lang.ProcessBuilder.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.regex.Pattern;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.program.*;
import org.eclipse.swt.widgets.*;

/**
 * A useful application to list, filter and run the available Snippets.
 */
public class SnippetExplorer {

	private static final String USAGE_EXPLANATION = "Welcome to the SnippetExplorer!\n"
			+ "\n"
			+ "This tool will help you to explore and test the large collection of SWT example Snippets. "
			+ "You can use the text field on top to filter the Snippets by there description or Snippet number. "
			+ "To start a Snippet you can either double click its entry, press enter or use the button below. "
			+ "It is also possible to start multiple Snippets at once. (exact behavior depends on selected Snippet-Runner)\n"
			+ "\n"
			+ "It is recommended to start the Snippet Explorer connected to a console since some of the Snippets "
			+ "print useful informations to the console or do not open a window at all.\n"
			+ "\n"
			+ "The Explorer supports (dependent on your OS and environment) different modes to start Snippets. Those runners are:\n"
			+ "\n"
			+ " \u2022 Thread Runner: Snippets are executed as threads of the Explorer.\n"
			+ "\t- This runner is only available if the environment supports multiple Displays at the same time. (only Windows at the moment)\n"
			+ "\t- Multiple Snippets can be run parallel using this runner.\n"
			+ "\t- All running Snippets are closed when the explorer exits.\n"
			+ "\t- If to many Snippets are run in parallel SWT may run out of handles.\n"
			+ "\t- If a Snippet calls System.exit it will also force the explorer itself and all other running Snippets to exit as well.\n"
			+ "\n"
			+ " \u2022 Process Runner: Snippets are executed as separate processes.\n"
			+ "\t- This runner is only available if a JRE was found which can be used to start the Snippets.\n"
			+ "\t- Multiple Snippets can be run parallel using this runner.\n"
			+ "\t- This runner is more likely to fail Snippet launch due to incomplete classpath or other launch problems.\n"
			+ "\t- When the explorer exits it try to close all running Snippets but has less control over it as the Thread runner.\n"
			+ "\t- Unlike the Thread runner the Process runner is resisted to faulty Snippets. (e.g. Snippets calling System.exit)\n"
			+ "\n"
			+ " \u2022 Serial Runner: Snippets are executed one after another instead of the explorer.\n"
			+ "\t- This runner is always available.\n"
			+ "\t- Cannot run Snippets parallel.\n"
			+ "\t- To run Snippets the explorer gets closed, executes the selected Snippets one after another in the same JVM "
			+ "and after the last Snippet has finished restarts the Snippet Explorer.\n"
			+ "\t- A Snippet calling System.exit will stop the Snippet chain and the explorer itself can not restart.";

	/** Max length for Snippet description in the main table. */
	private static final int MAX_DESCRIPTION_LENGTH_IN_TABLE = 80;
	/**
	 * If the user tries to start more than this number of Snippets at once a
	 * warning message is shown.
	 */
	private static final int START_MANY_SNIPPETS_WARNING_THREASHOLD = 10;
	/** Message shown in the filter text field if empty. */
	private static final String FILTER_HINT = "type to filter list";
	/**
	 * Delay in milliseconds before a changed filter value is applied on the list.
	 */
	private static final int FILTER_DELAY_MS = 200;
	/**
	 * Time snippets get to stop before tried to be killed forcefully. (currently
	 * applied per runner)
	 */
	private static final int SHUTDOWN_GRACE_TIME_MS = 5000;
	/** Link to online snippet source. Used if no local source is available. */
	private static final String SNIPPET_SOURCE_LINK_TEMPLATE = "https://github.com/eclipse-platform/"
			+ "eclipse.platform.swt.git/tree/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/%s.java";

	/**
	 * Whether or not SWT support creating of multiple {@link Display} instances on
	 * the current system. Required to use the thread runner mode.
	 */
	private static boolean multiDisplaySupport;
	/**
	 * The command used to invoke the java binary. May be <code>null</code> if not
	 * found. Required to use the process runner mode.
	 */
	private static String javaCommand;
	/** The list of available Snippets. */
	private static List<Snippet> snippets;

	/** The runner used for thread mode. */
	private final SnippetRunner THREAD_RUNNER = new SnippetRunnerThread();
	/** The runner used for process mode. */
	private final SnippetRunner PROCESS_RUNNER = new SnippetRunnerProcess();

	private Display display;
	private Shell shell;

	/** Helper to perform the delayed list update if filter changed. */
	private ListUpdater listUpdater;
	/** Text field to filter Snippet list. */
	private Text filterField;
	/** The main table listing available Snippets. */
	private Table snippetTable;
	/** Button to run selected Snippets. */
	private Button startSelectedButton;
	/** Snippet runner selection. */
	private Combo runnerCombo;
	/** The tabfolder to show information for selected Snippet. */
	private TabFolder infoTabs;
	/** Element to show Snippet description or general help. */
	private StyledText descriptionView;
	/**
	 * Element to show Snippets source code or link to source if not local
	 * available.
	 */
	private StyledText sourceView;
	/** Element to show Snippet preview if possible. */
	private Label previewImageLabel;

	/**
	 * The snippet runner used for next snippet start. If <code>null</code> Snippets
	 * are run serial.
	 */
	private SnippetRunner snippetRunner;
	/** Used to map {@link #runnerCombo} selection to actual Snippet runner. */
	private List<SnippetRunner> runnerMapping = new ArrayList<>();
	/** The Snippet currently shown in {@link #infoTabs}. May be <code>null</code>. */
	private Snippet currentInfoSnippet = null;
	/** Snippets currently run in serial runner mode. May be <code>null</code>. */
	private List<Snippet> serialSnippets;
	/**
	 * The SnippetExplorer location for the next {@link Shell#open()}. Used for
	 * restart after serial runner finished.
	 */
	private Point nextExplorerLocation = null;

	/**
	 * SnippetExplorer main method.
	 *
	 * @param args does not parse any arguments
	 */
	public static void main(String[] args) throws Exception {
		final String os = System.getProperty("os.name");
		multiDisplaySupport = (os != null && os.toLowerCase().contains("windows"));
		if (canRunCommand("java")) {
			javaCommand = "java";
		} else {
			final String javaHome = System.getProperty("java.home");
			if (javaHome != null) {
				final Path java = Paths.get(javaHome, "bin", "java");
				java.normalize();
				if (canRunCommand(java.toString())) {
					javaCommand = java.toString();
				}
			}
		}

		snippets = loadSnippets();
		snippets.sort((a, b) -> {
			int cmp = Integer.compare(a.snippetNum, b.snippetNum);
			if (cmp == 0) {
				cmp = a.snippetName.compareTo(b.snippetName);
			}
			return cmp;
		});

		new SnippetExplorer().open();
	}

	/**
	 * Test if the given command can be executed.
	 *
	 * @param command command to test
	 * @return <code>false</code> if executing the command failed for any reason
	 */
	private static boolean canRunCommand(String command) {
		try {
			final Process p = Runtime.getRuntime().exec(command);
			p.waitFor(150, TimeUnit.MILLISECONDS);
			if (p.isAlive()) {
				p.destroy();
				p.waitFor(100, TimeUnit.MILLISECONDS);
				if (p.isAlive()) {
					p.destroyForcibly();
				}
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public SnippetExplorer() {
	}

	/**
	 * Initializes and shows the SnippetExplorer. The method doesn't return until
	 * the explorer is closed or otherwise disposed.
	 */
	public void open() {
		initialize();
		runEventLoop();
	}

	/**
	 * Initialize the SnippetExplorer. Can be called again if the current explorer
	 * was properly disposed.
	 */
	private void initialize() {
		display = Display.getDefault();
		snippetRunner = null;
		shell = new Shell(display);
		if (nextExplorerLocation != null) {
			shell.setLocation(nextExplorerLocation);
		}
		shell.setText("SWT Snippet Explorer");

		createControls(shell);

		final String[] columns = new String[] { "Name", "Description" };
		for (String col : columns) {
			final TableColumn tableCol = new TableColumn(snippetTable, SWT.NONE);
			tableCol.setText(col);
			tableCol.setToolTipText(col);
			tableCol.setResizable(true);
			tableCol.setMoveable(true);
		}
		updateTable(null);

		for (TableColumn col : snippetTable.getColumns()) {
			col.pack();
		}
		final GridData rightSideLayout = (GridData) infoTabs.getLayoutData();
		final Point tableSize = snippetTable.getSize();
		rightSideLayout.widthHint = tableSize.x;
		rightSideLayout.heightHint = tableSize.y;
		shell.pack();
		shell.open();
	}

	/** Initialize the SnippetExplorer controls.
	 *
	 * @param shell parent shell
	 */
	private void createControls(Shell shell) {
		shell.setLayout(new FormLayout());

		if (listUpdater == null) {
			listUpdater = new ListUpdater();
			listUpdater.start();
		}

		final Composite leftContainer = new Composite(shell, SWT.NONE);
		leftContainer.setLayout(new GridLayout());

		final Sash splitter = new Sash(shell, SWT.BORDER | SWT.VERTICAL);
		final int splitterWidth = 3;
		splitter.addListener(SWT.Selection, e -> splitter.setBounds(e.x, e.y, e.width, e.height));

		final Composite rightContainer = new Composite(shell, SWT.NONE);
		rightContainer.setLayout(new GridLayout());

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(splitter, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		leftContainer.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(50, 0);
		formData.right = new FormAttachment(50, splitterWidth);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		splitter.setLayoutData(formData);
		splitter.addListener(SWT.Selection, event -> {
			final FormData splitterFormData = (FormData) splitter.getLayoutData();
			splitterFormData.left = new FormAttachment(0, event.x);
			splitterFormData.right = new FormAttachment(0, event.x + splitterWidth);
			shell.layout();
		});

		formData = new FormData();
		formData.left = new FormAttachment(splitter, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		rightContainer.setLayoutData(formData);

		filterField = new Text(leftContainer, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		filterField.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		filterField.setMessage(FILTER_HINT);
		filterField.addListener(SWT.Modify, event -> {
			listUpdater.updateInMs(FILTER_DELAY_MS);
		});
		snippetTable = new Table(leftContainer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		snippetTable.setLinesVisible(true);
		snippetTable.setHeaderVisible(true);
		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 500;
		snippetTable.setLayoutData(data);
		snippetTable.addListener(SWT.MouseDoubleClick, event -> {
			final Point clickPoint = new Point(event.x, event.y);
			launchSnippet(snippetTable.getItem(clickPoint));
		});
		snippetTable.addListener(SWT.KeyUp, event -> {
			if (event.keyCode == '\r' || event.keyCode == '\n') {
				launchSnippet(snippetTable.getSelection());
			}
		});

		final Composite buttonRow = new Composite(leftContainer, SWT.NONE);
		buttonRow.setLayout(new GridLayout(3, false));
		buttonRow.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		startSelectedButton = new Button(buttonRow, SWT.LEAD);
		startSelectedButton.setText("  Start &selected Snippets");
		snippetTable.addListener(SWT.Selection, event -> {
			startSelectedButton.setEnabled(snippetTable.getSelectionCount() > 0);
			updateInfoTab(snippetTable.getSelection());
		});
		startSelectedButton.setEnabled(snippetTable.getSelectionCount() > 0);
		startSelectedButton.addListener(SWT.Selection, event -> {
			launchSnippet(snippetTable.getSelection());
		});

		final Label runnerLabel = new Label(buttonRow, SWT.NONE);
		runnerLabel.setText("Snippet Runner:");
		runnerLabel.setLayoutData(new GridData(SWT.TRAIL, SWT.CENTER, true, false));

		runnerCombo = new Combo(buttonRow, SWT.TRAIL | SWT.DROP_DOWN | SWT.READ_ONLY);
		runnerMapping.clear();
		if (multiDisplaySupport) {
			runnerCombo.add("Thread");
			runnerMapping.add(THREAD_RUNNER);
		}
		if (javaCommand != null) {
			runnerCombo.add("Process");
			runnerMapping.add(PROCESS_RUNNER);
		}
		runnerCombo.add("Serial");
		runnerMapping.add(null);
		runnerCombo.setData(runnerMapping);
		runnerCombo.addListener(SWT.Modify, event -> {
			if (runnerMapping.size() > runnerCombo.getSelectionIndex()) {
				snippetRunner = runnerMapping.get(runnerCombo.getSelectionIndex());
			} else {
				System.err.println("Unknown runner index " + runnerCombo.getSelectionIndex());
			}
		});
		runnerCombo.select(0);

		infoTabs = new TabFolder(rightContainer, SWT.TOP);
		infoTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		descriptionView = new StyledText(infoTabs, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);

		sourceView = new StyledText(infoTabs, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
		setMonospaceFont(sourceView);

		final ScrolledComposite previewContainer = new ScrolledComposite(infoTabs, SWT.V_SCROLL | SWT.H_SCROLL);
		previewImageLabel = new Label(previewContainer, SWT.NONE);
		previewContainer.setContent(previewImageLabel);

		final TabItem descriptionTab = new TabItem(infoTabs, SWT.NONE);
		descriptionTab.setText("Description");
		descriptionTab.setControl(descriptionView);
		final TabItem sourceTab = new TabItem(infoTabs, SWT.NONE);
		sourceTab.setText("Source");
		sourceTab.setControl(sourceView);
		final TabItem previewTab = new TabItem(infoTabs, SWT.NONE);
		previewTab.setText("Preview");
		previewTab.setControl(previewContainer);

		updateInfoTab(null, true);
		updateInfoTab(snippetTable.getSelection());
	}

	/**
	 * Try to set a monospace font for this control. Other font properties like
	 * fontSize remain unchanged.
	 *
	 * @param control control to modify
	 */
	private void setMonospaceFont(Control control) {
		final FontData[] fontData = control.getFont().getFontData();
		Font font = null;
		if (font == null) {
			font = tryCreateFont("Consolas", fontData);
		}
		if (font == null) {
			font = tryCreateFont("DejaVu Sans Mono", fontData);
		}
		if (font == null) {
			font = tryCreateFont("Noto Mono", fontData);
		}
		if (font == null) {
			font = tryCreateFont("Liberation Mono", fontData);
		}
		if (font == null) {
			font = tryCreateFont("Ubuntu Mono", fontData);
		}
		if (font == null) {
			font = tryCreateFont("Courier New", fontData);
		}
		if (font == null) {
			font = tryCreateFont("Courier", fontData);
		}
		if (font == null) {
			font = tryCreateFont("Monospace", fontData);
		}

		if (font != null) {
			control.setFont(font);
		}
	}

	/**
	 * Try to create font with given name based on existing {@link FontData}.
	 *
	 * @param fontName     name of font to create
	 * @param existingData existing font data to be used for other font attributes
	 * @return the created font or <code>null</code> if failed (e.g. no font
	 *         available with given name)
	 */
	private Font tryCreateFont(String fontName, FontData[] existingData) {
		for (FontData element : existingData) {
			element.setName(fontName);
		}
		try {
			return new Font(display, existingData);
		} catch (SWTException e) {
			return null;
		}
	}

	/**
	 * Update the info tabs for the given items. The behavior may change. At the
	 * moment informations are only shown for single items.
	 *
	 * @param items items to show info for
	 */
	private void updateInfoTab(TableItem[] items) {
		// if multiple snippets are selected no info are shown
		if (items.length != 1) {
			updateInfoTab((TableItem) null, false);
		} else {
			updateInfoTab(items[0], false);
		}
	}

	/**
	 * Update the info tabs (right side of the explorer) for the given item.
	 *
	 * @param item  the selected item containing Snippet metadata (may be
	 *              <code>null</code>)
	 * @param force the tabs are only updated if they not already show info for the
	 *              given item. If this is <code>true</code> the tabs are updated
	 *              anyway.
	 */
	private void updateInfoTab(TableItem item, boolean force) {
		final Snippet snippet = (item != null && item.getData() instanceof Snippet) ? (Snippet) item.getData() : null;
		if (!force && currentInfoSnippet == snippet) {
			return;
		}
		if (snippet == null) {
			descriptionView.setText(USAGE_EXPLANATION);
			sourceView.setText("");
			updatePreviewImage(null, "");
		} else {
			descriptionView.setText(snippet.snippetName + "\n\n" + snippet.description);
			if (snippet.source == null) {
				sourceView.setWordWrap(true);
				final String msg = "No source available for " + snippet.snippetName + " but you may find it at:\n\n";
				final String link = String.format(Locale.ROOT, SNIPPET_SOURCE_LINK_TEMPLATE, snippet.snippetName);
				sourceView.setText(msg + link);

				final StyleRange linkStyle = new StyleRange();
				linkStyle.start = msg.length();
				linkStyle.length = link.length();
				linkStyle.underline = true;
				linkStyle.underlineStyle = SWT.UNDERLINE_LINK;
				sourceView.setStyleRange(linkStyle);

				sourceView.addListener(SWT.MouseDown, event -> {
					int offset = sourceView.getOffsetAtPoint(new Point(event.x, event.y));
					if (offset != -1) {
						try {
							final StyleRange style = sourceView.getStyleRangeAtOffset(offset);
							if (style != null && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
								Program.launch(link);
							}
						} catch (IllegalArgumentException e) {
							// no character under event.x, event.y
						}
					}
				});
			} else {
				sourceView.setWordWrap(false);
				sourceView.setText(snippet.source);
			}
			try {
				final Image previewImage = getPreviewImage(snippet);
				updatePreviewImage(previewImage, previewImage == null ? "No preview image available." : "");
			} catch (IOException e) {
				updatePreviewImage(null, "Failed to load preview image: " + e);
			}
		}
		currentInfoSnippet = snippet;
	}

	/**
	 * Update the control showing the image. If <em>image</em> is <code>null</code>
	 * show the <em>text</em> instead.
	 *
	 * @param image the image to show
	 * @param text  the alternative text to show if image is <code>null</code>
	 */
	private void updatePreviewImage(Image image, String text) {
		final Image previousImage = previewImageLabel.getImage();
		previewImageLabel.setImage(image);
		if (image == null && text != null) {
			previewImageLabel.setText(text);
		}
		if (previousImage != null) {
			previousImage.dispose();
		}
		previewImageLabel.pack(true);
	}

	/**
	 * Get the preview image for the Snippet.
	 *
	 * @param snippet Snippet's metadata to load preview image for
	 * @return the preview image or <code>null</code> if none available
	 * @throws IOException if image loading failed
	 */
	private Image getPreviewImage(Snippet snippet) throws IOException {
		final Path previewFile = Paths.get("previews", snippet.snippetName + ".png");
		if (Files.exists(previewFile)) {
			try (InputStream imageStream = Files.newInputStream(previewFile)) {
				return new Image(display, imageStream);
			}
		}
		try (InputStream imageStream = SnippetExplorer.class
				.getResourceAsStream("/previews/" + snippet.snippetName + ".png")) {
			if (imageStream != null) {
				return new Image(display, imageStream);
			}
		}
		try (InputStream imageStream = ClassLoader
				.getSystemResourceAsStream("previews/" + snippet.snippetName + ".png")) {
			if (imageStream != null) {
				return new Image(display, imageStream);
			}
		}
		return null;
	}

	/**
	 * Load all available Snippets from the preconfigured source path and from the
	 * current classppath.
	 *
	 * @return all found Snippets (never <code>null</code>)
	 */
	private static List<Snippet> loadSnippets() {
		// Similar to SnippetLauncher this explorer tries to load Snippet0 to Snippet500
		// even if no sources are available. This array is used to track which snippets
		// are already loaded from source.
		final boolean[] loadedSnippets = new boolean[501];
		final List<Snippet> snippets = new ArrayList<>();

		// load snippets from source directory
		final Path sourceDir = SnippetsConfig.SNIPPETS_SOURCE_DIR.toPath();
		if (Files.exists(sourceDir)) {
			try (DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir, "*.java")) {
				for (Path file : files) {
					try {
						final Snippet snippet = snippetFromSource(file);
						if (snippet == null) {
							continue;
						}
						snippets.add(snippet);
						if (snippet.snippetNum >= 0) {
							loadedSnippets[snippet.snippetNum] = true;
						}
					} catch (ClassNotFoundException | IOException ex) {
						System.err.println("Failed to load snippet from " + file + ". Error: " + ex);
					}
				}
			} catch (IOException ex) {
				System.err.println("Failed to access source directory " + sourceDir + ". Error: " + ex);
			}
		}

		// load snippets from classpath
		for (int i = 0; i < loadedSnippets.length; i++) {
			if (!loadedSnippets[i]) {
				final int snippetNum = i;
				final String snippetName = "Snippet" + snippetNum;
				final Class<?> snippetClass;
				try {
					snippetClass = Class.forName(SnippetsConfig.SNIPPETS_PACKAGE + "." + snippetName, false,
							SnippetExplorer.class.getClassLoader());
				} catch (ClassNotFoundException e) {
					continue;
				}
				final String[] arguments = SnippetsConfig.getSnippetArguments(snippetNum);
				snippets.add(new Snippet(snippetNum, snippetName, snippetClass, null, null, arguments));
			}
		}

		return snippets;
	}

	/**
	 * Load Snippet metadata from the Java source file found at the given path.
	 *
	 * @param sourceFile the source file to load
	 * @return the gathered Snippet metadata or <code>null</code> if failed
	 * @throws IOException            on errors loading the source file
	 * @throws ClassNotFoundException if loading the Snippets corresponding class
	 *                                file failed
	 */
	private static Snippet snippetFromSource(Path sourceFile) throws IOException, ClassNotFoundException {
		final Pattern snippetNamePattern = Pattern.compile("Snippet([0-9]+)", Pattern.CASE_INSENSITIVE);
		sourceFile = sourceFile.normalize();
		final String filename = sourceFile.getFileName().toString();
		final String snippetName = filename.substring(0, filename.lastIndexOf('.'));
		final Class<?> snippeClass = Class.forName(SnippetsConfig.SNIPPETS_PACKAGE + "." + snippetName, false,
				SnippetExplorer.class.getClassLoader());
		int snippetNum = Integer.MIN_VALUE;
		final Matcher snippetNameMatcher = snippetNamePattern.matcher(snippetName);
		if (snippetNameMatcher.matches()) {
			try {
				snippetNum = Integer.parseInt(snippetNameMatcher.group(1), 10);
			} catch (NumberFormatException e) {
			}
		}

		// do not load snippets without number yet
		if (snippetNum < 0) {
			return null;
		}

		final String src = getSnippetSource(sourceFile);
		final String description = extractSnippetDescription(src);
		final String[] arguments = SnippetsConfig.getSnippetArguments(snippetNum);
		return new Snippet(snippetNum, snippetName, snippeClass, src, description, arguments);
	}

	/**
	 * Read the content of the source file. (expect <code>UTF-8</code> encoding)
	 *
	 * @param sourceFile source file to load
	 * @return the files content or <code>null</code> if file does not exist
	 * @throws IOException if loading failed
	 */
	private static String getSnippetSource(Path sourceFile) throws IOException {
		if (!Files.exists(sourceFile)) {
			return null;
		}
		final String src = new String(Files.readAllBytes(sourceFile), StandardCharsets.UTF_8);
		return src;
	}

	/**
	 * Tries to extract a snippet description from the snippet source.
	 * <p>
	 * If description has multiple lines the delimiter is always in UNIX-style (\n).
	 * </p>
	 *
	 * @param snippetSrc the snippet source code
	 * @return the extracted snippet description. If none found returns
	 *         <code>null</code> or in some cases an empty string.
	 */
	private static String extractSnippetDescription(String snippetSrc) {
		if (snippetSrc == null) {
			return null;
		}
		// Usually the second block comment contains a description of the snippet
		// therefore this method returns the first block comment not containing the
		// usual copyright keywords.
		// Note: currently only real block comments are considered. A bunch of line
		// comments forming a block (like that comment you're reading right now) are
		// ignored.

		final Pattern blockCommentPattern = Pattern.compile("/\\*\\*?(.*?)\\*/", Pattern.DOTALL);
		final Matcher blockCommentMatcher = blockCommentPattern.matcher(snippetSrc);
		while (blockCommentMatcher.find()) {
			String comment = blockCommentMatcher.group(1);
			if (comment.contains("Copyright (c)") || comment.contains("https://www.eclipse.org/legal/epl-2.0/")) {
				continue;
			}
			// normalize line breaks
			comment = comment.replaceAll("\r\n?", "\n");
			// remove '*' at line start and trim lines
			comment = comment.replaceAll("[ \t]*\n[ \\t]*\\*+[ \\t]*", "\n");
			// trim start and end
			comment = comment.trim();
			return comment;
		}
		return null;
	}

	private void updateTable(String filter) {
		if (filter == null) {
			filter = "";
		}
		filter = filter.toLowerCase();
		int itemIndex = 0;
		final int itemCount = snippetTable.getItemCount();
		snippetTable.setRedraw(false);
		snippetTable.deselectAll();
		for (Snippet snippet : snippets) {
			if (filter.isEmpty() || (snippet.description != null && snippet.description.toLowerCase().contains(filter))
					|| String.valueOf(snippet.snippetNum).equals(filter)) {
				final TableItem item = itemIndex < itemCount ? snippetTable.getItem(itemIndex)
						: new TableItem(snippetTable, SWT.NONE);
				fillTableItem(item, snippet);
				itemIndex++;
			}
		}
		if (itemIndex < itemCount) {
			snippetTable.remove(itemIndex, itemCount - 1);
		}
		snippetTable.setRedraw(true);
	}

	/**
	 * Initialize the table item with information from the Snippet.
	 *
	 * @param item table item to initialize (not <code>null</code>)
	 * @param snippet source Snippet (not <code>null</code>)
	 */
	private void fillTableItem(TableItem item, Snippet snippet) {
		item.setData(snippet);

		final String shortDescription;
		if (snippet.description == null) {
			shortDescription = "";
		} else {
			int index = snippet.description.indexOf('\n');
			if (index < 0) {
				index = snippet.description.length();
			}
			if (index > MAX_DESCRIPTION_LENGTH_IN_TABLE) {
				shortDescription = snippet.description.substring(0, MAX_DESCRIPTION_LENGTH_IN_TABLE) + "...";
			} else {
				shortDescription = snippet.description.substring(0, index);
			}
		}

		item.setText(new String[] { snippet.snippetName, shortDescription });
	}

	/**
	 * Process UI event queue until explorer is closed or otherwise ended.
	 */
	private void runEventLoop() {
		// Apart from the usual "dispatch events until closed" pattern the
		// SnippetExplorer supports the special workflow where it close itself, run one
		// or more Snippets one after another and then restarts the explorer itself
		// which is all handled in this method.
		try {
			while (true) {
				serialSnippets = null;
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				if (serialSnippets == null || serialSnippets.isEmpty()) {
					break;
				}

				display.dispose();
				int i = 0;
				for (Snippet snippet : serialSnippets) {
					System.out.println(String.format("(%d/%d) %s", ++i, serialSnippets.size(), snippet.snippetName));
					runSnippetInCurrentThread(snippet);
				}
				final Display currentDisplay = Display.getCurrent();
				if (currentDisplay != null) {
					// left over from the snippet run
					currentDisplay.dispose();
				}
				initialize();
				final int index = runnerMapping.indexOf(null);
				if (index != -1) {
					runnerCombo.select(index);
				}
			}
		} finally {
			stopSnippets();
		}
	}

	/** Try to stop all running Snippets. */
	private synchronized void stopSnippets() {
		for (SnippetRunner runner : runnerMapping) {
			if (runner != null) {
				runner.stopSnippets();
			}
		}
	}

	/**
	 * Launch the given snippet items with the currently selected snippet runner.
	 * <p>
	 * The items must contain the {@link Snippet} metadata as data object.
	 * </p>
	 *
	 * @param items the Snippets to launch
	 * @see #snippetRunner
	 */
	private void launchSnippet(TableItem... items) {
		final List<Snippet> validSnippets = new ArrayList<>();
		for (TableItem item : items) {
			if (item != null && item.getData() instanceof Snippet) {
				validSnippets.add((Snippet) item.getData());
			}
		}

		if (validSnippets.size() > START_MANY_SNIPPETS_WARNING_THREASHOLD) {
			final MessageBox warnBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
			warnBox.setText("Starting many Snippets");
			warnBox.setMessage("You have selected " + validSnippets.size() + " Snippets to start.\n"
					+ "Do you really want to start so many Snippets at once?");
			if (warnBox.open() != SWT.YES) {
				return;
			}
		}

		if (snippetRunner != null) {
			snippetRunner.launchSnippet(validSnippets.toArray(new Snippet[0]));
		} else {
			nextExplorerLocation = shell.getLocation();
			serialSnippets = validSnippets;
			shell.close();
		}
	}

	/**
	 * Launches the given Snippet in the current thread by invoking the Snippets
	 * <code>main</code> method.
	 *
	 * @param snippet the Snippet to run (not <code>null</code>)
	 */
	private static void runSnippetInCurrentThread(Snippet snippet) {
		final Method method;
		final String[] arguments = snippet.arguments;
		try {
			method = snippet.snippetClass.getMethod("main", arguments.getClass());
		} catch (NoSuchMethodException ex) {
			System.err.println("Did not find main(String []) for " + snippet.snippetName);
			return;
		}
		try {
			method.invoke(null, new Object[] { arguments });
		} catch (IllegalAccessException | IllegalArgumentException e) {
			System.err.println("Failed to launch " + snippet.snippetName + ". Error: " + e);
		} catch (InvocationTargetException e) {
			System.err.println("Exception in Snippet " + snippet.snippetName + ": " + e.getTargetException());
		}
	}

	/**
	 * Show a warning dialog that the Snippet may print with the default printer
	 * without further warnings.
	 *
	 * @param shell       parent shell for the warning dialog
	 * @param snippetName the Snippet's name to warn for
	 * @return <code>true</code> if the user confirmed Snippet execution
	 */
	private static boolean printerWarning(Shell shell, String snippetName) {
		final MessageBox warnBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		warnBox.setText("Printing Snippet");
		warnBox.setMessage(
				snippetName + " may print something on your default printer without further warning or confirmation.");
		return (warnBox.open() == SWT.OK);
	}

	/** Class to store metadata for a Snippet. */
	private static class Snippet {
		/** The Snippet's number. (not all Snippets may have numbers in the future) */
		private int snippetNum;
		/** Snippet's name / main class name. */
		private String snippetName;
		/** Snippet's main class. */
		private Class<?> snippetClass;
		/** Snippet's source code or <code>null</code> if not available. */
		private String source;
		/**
		 * Snippet description extracted from its source code. (may be
		 * <code>null</code>)
		 */
		private String description;
		/**
		 * Arguments used when launching the Snippets. Can be configured in
		 * {@link SnippetsConfig#getSnippetArguments(int)}.
		 */
		private String[] arguments;

		public Snippet(int snippetNum, String snippetName, Class<?> snippetClass, String source, String description,
				String[] arguments) {
			super();
			this.snippetNum = snippetNum;
			this.snippetName = snippetName;
			this.snippetClass = snippetClass;
			this.source = source;
			this.description = description;
			this.arguments = arguments;
		}

		@Override
		public String toString() {
			return "Snippet [snippetNum=" + snippetNum + ", snippetName=" + snippetName + ", snippetClass="
					+ snippetClass + ", source=" + source + ", description=" + description + ", arguments="
					+ Arrays.toString(arguments) + "]";
		}
	}

	/** Interface for a runner capable to launch Snippets. */
	private interface SnippetRunner {
		/**
		 * Launch the given Snippets in the runner specific way.
		 *
		 * @param snippets Snippets to launch. Not <code>null</code>.
		 */
		void launchSnippet(Snippet... snippets);

		/**
		 * Stop all running Snippets launched with this runner. Some runners may not be
		 * able to stop Snippets.
		 */
		void stopSnippets();
	}

	/** Run Snippets in separate threads. */
	private class SnippetRunnerThread implements SnippetRunner {

		/** All currently <b>running</b> Snippets launched from this runner. */
		private final List<Thread> launchedSnippets = new ArrayList<>();

		/**
		 * Launch Snippets parallel in separate threads. Call returns immediately after
		 * all Snippets are started.
		 */
		@Override
		public void launchSnippet(Snippet... snippets) {
			for (Snippet snippet : snippets) {
				if (snippet == null) {
					return;
				}
				final Thread thread = new Thread(() -> {
					try {
						synchronized (launchedSnippets) {
							launchedSnippets.add(Thread.currentThread());
						}

						// warn user before printing
						if (SnippetsConfig.isPrintingSnippet(snippet.snippetNum)) {
							final Display d = new Display();
							try {
								if (!printerWarning(new Shell(d), snippet.snippetName)) {
									return;
								}
							} finally {
								d.dispose();
							}
						}

						runSnippetInCurrentThread(snippet);

					} finally {
						synchronized (launchedSnippets) {
							final Display d = Display.getCurrent();
							if (d != null) {
								d.dispose();
							}
							launchedSnippets.remove(Thread.currentThread());
						}
					}
				});
				thread.setDaemon(true);
				thread.setName(snippet.snippetName);
				thread.start();
			}
		}

		/**
		 * Stops all running Snippets launched by this runner. If a Snippt refuses to
		 * react to this stop signal it will not be force stopped until the
		 * SnippetExplorer itself is closed.
		 */
		@Override
		public void stopSnippets() {
			final List<Thread> runningSnippets;
			synchronized (launchedSnippets) {
				runningSnippets = new ArrayList<>(launchedSnippets);
			}
			for (Thread t : runningSnippets) {
				t.interrupt();
				final Display d = Display.findDisplay(t);
				if (d != null) {
					d.asyncExec(
							() -> Arrays.stream(d.getShells()).filter(s -> !s.isDisposed()).forEach(Shell::close));
				}
			}
			final long start = System.currentTimeMillis();
			for (Thread t : runningSnippets) {
				if (System.currentTimeMillis() - SHUTDOWN_GRACE_TIME_MS > start) {
					break;
				}
				try {
					t.join(200);
				} catch (InterruptedException e) {
				}
			}
			synchronized (launchedSnippets) {
				if (!launchedSnippets.isEmpty()) {
					System.err.println("Some Snippets are still running:");
					for (Thread t : launchedSnippets) {
						System.err.println("    " + t.getName() + " (ThreadId: " + t.getId() + ")");
						final Display d = Display.findDisplay(t);
						if (d != null && !d.isDisposed()) {
							d.syncExec(d::dispose);
						}
					}
				}
			}
		}
	}

	/** Run Snippets in separate processes. */
	private class SnippetRunnerProcess implements SnippetRunner {
		/**
		 * All Snippets launched from this runner. Listed Snippets may already
		 * terminated.
		 */
		private List<Process> launchedSnippets = new ArrayList<>();

		/**
		 * Launch Snippets parallel as separate processes using the auto discovered JRE.
		 * Call returns immediately after all Snippets are started.
		 */
		@Override
		public synchronized void launchSnippet(Snippet... snippets) {
			for (Snippet snippet : snippets) {
				if (snippet == null) {
					continue;
				}

				// warn user before printing
				if (SnippetsConfig.isPrintingSnippet(snippet.snippetNum)) {
					if (!printerWarning(shell, snippet.snippetName)) {
						continue;
					}
				}

				final List<String> command = new ArrayList<>();
				command.add(javaCommand);
				final String os = System.getProperty("os.name");
				if (os != null && os.toLowerCase().contains("mac")) {
					command.add("-XstartOnFirstThread");
				}
				final String cp = System.getProperty("java.class.path");
				if (cp != null && !cp.isEmpty()) {
					command.add("-cp");
					command.add(cp);
				}
				final String libPath = System.getProperty("java.library.path");
				if (libPath != null && !libPath.isEmpty()) {
					command.add("-Djava.library.path=" + libPath);
				}
				command.add(SnippetsConfig.SNIPPETS_PACKAGE + "." + snippet.snippetName);
				command.addAll(Arrays.asList(snippet.arguments));
				try {
					System.out.println("Exec: " + String.join(" ", command));
					ProcessBuilder processBuilder = new ProcessBuilder(command);
					processBuilder.redirectOutput(Redirect.INHERIT);
					processBuilder.redirectError(Redirect.INHERIT);
					final Process p = processBuilder.start();
					launchedSnippets.add(p);
				} catch (IOException e) {
					System.err.println("Failed to launch " + snippet.snippetName + ". Error: " + e);
				}
			}
		}

		/**
		 * Stops all running Snippets launched by this runner. If the stop signal was
		 * send but the Snippet is still running after a short grace time the runner
		 * tries to stop the Snippet forcefully.
		 * <p>
		 * If all attempts to stop the Snippet fail then the Snippet will run even after
		 * the SnippetExplorer was closed.
		 * </p>
		 */
		@Override
		public synchronized void stopSnippets() {
			for (Process p : launchedSnippets) {
				p.destroy();
			}

			final long start = System.currentTimeMillis();
			while (!launchedSnippets.isEmpty() && System.currentTimeMillis() - SHUTDOWN_GRACE_TIME_MS < start) {
				final Iterator<Process> it = launchedSnippets.iterator();
				while (it.hasNext()) {
					final Process p = it.next();
					if (!p.isAlive()) {
						it.remove();
					}
				}
				if (!launchedSnippets.isEmpty()) {
					try {
						launchedSnippets.get(0).waitFor(100, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						break;
					}
				}
			}

			if (!launchedSnippets.isEmpty()) {
				System.err.println(launchedSnippets.size() + " Snippets are still running.");
				for (Process p : launchedSnippets) {
					p.destroyForcibly();
				}
				final Iterator<Process> it = launchedSnippets.iterator();
				while (it.hasNext()) {
					final Process p = it.next();
					if (!p.isAlive()) {
						it.remove();
					}
				}
			}
		}
	}

	/**
	 * Update thread used to delay the list filtering due to changed filter string.
	 */
	private class ListUpdater extends Thread {
		/**
		 * The timestamp in milliseconds since epoch when the next update should be
		 * executed.
		 */
		private long nextListUpdate = 0;

		public ListUpdater() {
			setName("List Updater");
			setDaemon(true);
		}

		/**
		 * Reapply the table filter in X milliseconds.
		 * <p>
		 * If an update is already scheduled only the latest update time will be used.
		 * </p>
		 *
		 * @param ms sleep time before updating the main table
		 */
		public synchronized void updateInMs(long ms) {
			if (ms < 0) {
				return;
			}
			final long nextUpdate = System.currentTimeMillis() + ms;
			if (nextListUpdate < nextUpdate) {
				nextListUpdate = nextUpdate;
			}
			notify();
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				final long nextUpdate;
				synchronized (this) {
					nextUpdate = nextListUpdate;
				}
				if (nextUpdate - System.currentTimeMillis() <= 0) {
					if (filterField != null) {
						display.syncExec(() -> updateTable(filterField.getText()));
					}
					synchronized (this) {
						if (nextUpdate == nextListUpdate) {
							// no new update was scheduled while updating
							nextListUpdate = 0;
						}
					}
				}
				synchronized (this) {
					long sleepTime = nextListUpdate;
					if (sleepTime != 0) {
						sleepTime -= System.currentTimeMillis();
						if (sleepTime <= 0) {
							sleepTime = 1;
						}
					}
					try {
						wait(sleepTime);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		}
	}
}
