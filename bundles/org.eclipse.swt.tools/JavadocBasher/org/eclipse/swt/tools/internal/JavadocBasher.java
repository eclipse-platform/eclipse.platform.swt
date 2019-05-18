package org.eclipse.swt.tools.internal;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

/**
 * Bashes the javadoc from one source tree into another. Only produces new
 * source files for compilation units that have changed.
 *
 * How to use: 1) make sure you have the latest org.eclipse.swt (master branch)
 * in your workspace, and that you have no outstanding org.eclipse.swt changes
 * 2) create a Bugzilla bug called
 * "Do the annual javadoc/copyright bash for x.x" 3) make a version (tag) of the
 * org.eclipse.swt project before you bash here is a sample tag name:
 * BEFORE_JAVADOC_BASH_FOR_43RC3 use the Bugzilla bug for the tag comment 4)
 * modify the code in main, below, so that 'workspaceDir' and 'outputDir' point
 * to the (git) directory that contains org.eclipse.swt in your workspace,
 * typically C:/git/eclipse.platform.swt/bundles (prior to 3.8/4.2, these
 * pointed to the workspace directory) 5) make sure 'sourceSubdir' (usually
 * win32), 'targetSubdirs' (all others), and 'folders' are correct (note: there
 * are typically a few new targetSubdirs and folders every year... although
 * nothing new for 4.3) 6) run JavadocBasher (for a more verbose output, set
 * fVerbose to true) 7) refresh (F5) the org.eclipse.swt project inside eclipse
 * 8) search for *** in console output to see results of API consistency
 * checking 9) synchronize, carefully reviewing every change. Watch out for: -
 * duplicated comments - // comments that have been removed (if they appear
 * before a javadoc comment) 10) use the Bugzilla bug as the commit comment for
 * javadoc and copyright bash commits 11) make a version of the org.eclipse.swt
 * project after bashing (use tag name AFTER_...)
 *
 * 12) Copyright bash (tag before and after): NOTE: JavadocBasher does not fix
 * copyrights. Use the "Fix Copyrights" tool in org.eclipse.releng.tools for
 * that (always fix copyrights after bash). Use Help->Install New Software... to
 * install "Releng Tools" from the "Eclipse Project Updates" site (for release -
 * 1). Select org.eclipse.swt project and choose "Fix Copyrights" from the
 * context menu. See http://wiki.eclipse.org/Development_Resources/
 * How_to_Use_Eclipse_Copyright_Tool for more info. NOTE: The copyright tool
 * takes about 45 minutes to run (for SWT). NOTE 2: Check console for possible
 * errors/warnings, refresh (F5), synchronize, and browse all changes. Use
 * keyboard (Ctrl+.) for next diff instead of mouse (keyboard is faster because
 * there are fewer focus changes). Only use git History view as needed - if it
 * is open and linked with editor, it gets bogged down and lags behind. NOTE 3:
 * SWT anomalies that confuse the tool: - Some ns*.h files in
 * Mozilla/common/library do not contain the word "copyright" so the tool tries
 * to add one - don't keep it (the text is fine as-is). - Other ns*.h files in
 * Mozilla/common/library have a copyright line that should not be updated
 * (Initial Developer) - don't keep the change suggested by the tool (the text
 * is fine as-is). - The ns*.java and some other *.java files in
 * internal/mozilla have 2 copyright lines and the tool tries to change the 1st
 * - don't keep the 1st change (Netscape 1998-2015), but update the 2nd (IBM)
 * manually.
 *
 * NOTE: JavadocBasher now does a fairly good job of checking API consistency.
 * We used to use org.eclipse.swt.diff for API consistency checking, but it was
 * difficult to maintain.
 */
public class JavadocBasher {
	static final boolean fVerbose = false; // set to true for verbose output
	List<String> fBashed;
	List<String> fUnchanged;
	List<String> fSkipped;

	public JavadocBasher() {
		fBashed = new ArrayList<>();
		fUnchanged = new ArrayList<>();
		fSkipped = new ArrayList<>();
	}
	
	public static class Edit {
		int start, length;
		String text;

		public Edit(int start, int length, String text) {
			this.start = start;
			this.length = length;
			this.text = text;
		}
	}

	public static void main(String[] args) {
		String workspaceDir = ".."; // use forward slashes, no final slash
		String outputDir = ".."; // can point to another directory for debugging
		String[] folders = new String[] { // commented folders do not need to be
				// bashed
				"Eclipse SWT", "Eclipse SWT Accessibility",
				"Eclipse SWT AWT",
				"Eclipse SWT Browser",
				// "Eclipse SWT Custom Widgets",
				"Eclipse SWT Drag and Drop", "Eclipse SWT Effects",
				// "Eclipse SWT OLE Win32",
				"Eclipse SWT OpenGL",
				// "Eclipse SWT PI",
				"Eclipse SWT Printing", "Eclipse SWT Program",
				"Eclipse SWT Theme", "Eclipse SWT WebKit", };
		String sourceSubdir = "win32";
		String[] targetSubdirs = new String[] { "cairo", // used by gtk
				"cocoa",
				// "common",
				// "common_j2me",
				// "common_j2se",
				"emulated", "emulated/bidi", // used by carbon, cocoa
				"emulated/coolbar", // used by cocoa, gtk
				"emulated/expand", // used by cocoa
				"emulated/taskbar", // used by gtk
				"emulated/tooltip", // used by cocoa (?!)
				"glx", // used by gtk
				"gtk"
		};

		System.out.println("==== Start Bashing ====");
		int totalBashed = 0;
		for (int t = 0; t < targetSubdirs.length; t++) {
			for (int f = 0; f < folders.length; f++) {
				String targetSubdir = folders[f] + "/" + targetSubdirs[t];
				File source = new File(workspaceDir + "/org.eclipse.swt/"
						+ folders[f] + "/" + sourceSubdir);
				File target = new File(workspaceDir + "/org.eclipse.swt/"
						+ targetSubdir);
				File out = new File(outputDir + "/org.eclipse.swt/"
						+ targetSubdir);
				JavadocBasher basher = new JavadocBasher();
				System.out.println("\n==== Start Bashing " + targetSubdir);
				basher.bashJavaSourceTree(source, target, out);
				List<String> bashedList = basher.getBashed();
				basher.status("Bashed", bashedList, targetSubdir);
				if (bashedList.size() > 0) {
					totalBashed += bashedList.size();
					if (fVerbose)
						basher.status("Didn't change", basher.getUnchanged(),
								targetSubdir);
					basher.status("Skipped", basher.getSkipped(), targetSubdir);
				}
				System.out.println("==== Done Bashing " + targetSubdir);
			}
		}
		System.out.println("\n==== Done Bashing (Bashed " + totalBashed
				+ " files in total) - Be sure to Refresh (F5) project(s) ====");
	}

	void status(String label, List<String> list, String targetSubdir) {
		int count = list.size();
		System.out.println(label + " " + count
				+ ((count == 1) ? " file" : " files") + " in " + targetSubdir
				+ ((count > 0) ? ":" : "."));
		if (count > 0) {
			for(String s : list)
				System.out.println(label + ": " + s);
			System.out.println();
		}
	}

	char[] readFile(File file) {
		try (Reader in = new FileReader(file)) {
			CharArrayWriter storage = new CharArrayWriter();
			char[] chars = new char[8192];
			int read = in.read(chars);
			while (read > 0) {
				storage.write(chars, 0, read);
				storage.flush();
				read = in.read(chars);
			}
			return storage.toCharArray();
		} catch (IOException ioe) {
			System.out.println("*** Could not read " + file);
		}
		return null;
	}

	void writeFile(char[] contents, File file) {
		try (Writer out = new FileWriter(file)) {
			out.write(contents);
			out.flush();
		} catch (IOException ioe) {
			System.out.println("*** Could not write to " + file);
			if (fVerbose) {
				System.out.println("<dump filename=\"" + file + "\">");
				System.out.println(contents);
				System.out.println("</dump>");
			}
		}
	}

	void bashJavaSourceTree(File sourceDir, File targetDir, File outDir) {
		if (fVerbose)
			System.out.println("Reading source javadoc from " + sourceDir);
		if (!sourceDir.exists()) {
			System.out.println("Source: " + sourceDir + " was missing");
			return;
		}
		if (!targetDir.exists()) {
			System.out.println("Target: " + targetDir + " was missing");
			return;
		}

		String[] list = sourceDir.list();
		if (list != null) {
			int count = list.length;
			for (int i = 0; i < count; i++) {
				String filename = list[i];
				if (filename.equals("CVS") || filename.equals("internal")
						|| filename.equals("library"))
					continue;
				File source = new File(sourceDir, filename);
				File target = new File(targetDir, filename);
				File out = new File(outDir, filename);
				if (source.exists() && target.exists()) {
					if (source.isDirectory()) {
						if (target.isDirectory()) {
							bashJavaSourceTree(source, target, out);
						} else {
							System.out.println("*** " + target
									+ " should have been a directory.");
						}
					} else {
						if (filename.toLowerCase().endsWith(".java")) {
							bashFile(source, target, out);
						} else {
							fSkipped.add(source + " (not a java file)");
						}
					}
				} else {
					if (source.exists()) {
						fSkipped.add(target + " (does not exist)");
					} else {
						fSkipped.add(source + " (does not exist)");
					}
				}
			}
		}
	}


	void bashFile(final File source, final File target, File out) {
		char[] contents = readFile(source);
		if (contents == null) return;
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		final Document sourceDocument = new Document(new String(contents));
		parser.setSource(contents);
		CompilationUnit sourceUnit = (CompilationUnit)parser.createAST(null);

		contents = readFile(target);
		if (contents == null) return;
		String targetContents = new String(contents);
		final Document targetDocument = new Document(targetContents);
		parser.setSource(contents);
		CompilationUnit targetUnit = (CompilationUnit)parser.createAST(null);

		final HashMap<String, String> comments = new HashMap<>();
		sourceUnit.accept(new ASTVisitor() {
			String prefix = "";
			@Override
			public boolean visit(Block node) {
				return false;
			}
			@Override
			public boolean visit(VariableDeclarationFragment node) {
				FieldDeclaration field = (FieldDeclaration)node.getParent();
				int mods = field.getModifiers();
				if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
					Javadoc javadoc = field.getJavadoc();
					if (field.fragments().size() > 1 && javadoc != null) {
						System.err.println("Field declaration with multiple variables is not supported. -> " + source + " " + node.getName().getFullyQualifiedName());
					}
					try {
						String key = prefix + "." + node.getName().getFullyQualifiedName();
						comments.put(key, javadoc != null ? sourceDocument.get(javadoc.getStartPosition(), getJavadocLength(sourceDocument, javadoc)) : "");
					} catch (BadLocationException e) {}
					return true;
				}
				return false;
			}
			@Override
			public boolean visit(MethodDeclaration node) {
				int mods = node.getModifiers();
				if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
					Javadoc javadoc = node.getJavadoc();
					try {
						String key = prefix + "." + node.getName().getFullyQualifiedName();
						for (Iterator<SingleVariableDeclaration> iterator = node.parameters().iterator(); iterator.hasNext();) {
							SingleVariableDeclaration param = iterator.next();
							key += param.getType().toString();
						}
						comments.put(key, javadoc != null ? sourceDocument.get(javadoc.getStartPosition(), getJavadocLength(sourceDocument, javadoc)) : "");
					} catch (BadLocationException e) {}
					return true;
				}
				return false;
			}
			@Override
			public boolean visit(TypeDeclaration node) {
				int mods = node.getModifiers();
				if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
					Javadoc javadoc = node.getJavadoc();
					try {
						String key = prefix + "." + node.getName().getFullyQualifiedName();
						comments.put(key, javadoc != null ? sourceDocument.get(javadoc.getStartPosition(), getJavadocLength(sourceDocument, javadoc)) : "");
					} catch (BadLocationException e) {}
					prefix = node.getName().getFullyQualifiedName();
					return true;
				}
				return false;
			}
		});


		final List<Edit> edits = new ArrayList<>();
		targetUnit.accept(new ASTVisitor() {
			String prefix = "";
			@Override
			public boolean visit(Block node) {
				return false;
			}
			@Override
			public boolean visit(VariableDeclarationFragment node) {
				FieldDeclaration field = (FieldDeclaration)node.getParent();
				int mods = field.getModifiers();
				if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
					Javadoc javadoc = field.getJavadoc();
					if (field.fragments().size() > 1 && javadoc != null) {
						System.err.println("Field declaration with multiple variables is not supported. -> " + target + " " + node.getName().getFullyQualifiedName());
					}
					String key = prefix + "." + node.getName().getFullyQualifiedName();
					String newComment = comments.get(key);
					if (newComment != null) {
						comments.remove(key);
						if (javadoc != null) {
							edits.add(new Edit(javadoc.getStartPosition(), getJavadocLength(targetDocument, javadoc), newComment));
						} else {
							edits.add(new Edit(field.getStartPosition(), 0, newComment));
						}
					}
					return true;
				}
				return false;
			}
			@Override
			public boolean visit(MethodDeclaration node) {
				int mods = node.getModifiers();
				if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
					Javadoc javadoc = node.getJavadoc();
					String key = prefix + "." + node.getName().getFullyQualifiedName();
					for (Iterator<SingleVariableDeclaration> iterator = node.parameters().iterator(); iterator.hasNext();) {
						SingleVariableDeclaration param = iterator.next();
						key += param.getType().toString();
					}
					String newComment = comments.get(key);
					if (newComment != null) {
						comments.remove(key);
						if (javadoc != null) {
							edits.add(new Edit(javadoc.getStartPosition(), getJavadocLength(targetDocument, javadoc), newComment));
						} else {
							edits.add(new Edit(node.getStartPosition(), 0, newComment));
						}
					}
					return true;
				}
				return false;
			}
			@Override
			public boolean visit(TypeDeclaration node) {
				int mods = node.getModifiers();
				if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
					Javadoc javadoc = node.getJavadoc();
					String key = prefix + "." + node.getName().getFullyQualifiedName();
					String newComment = comments.get(key);
					if (newComment != null) {
						comments.remove(key);
						if (javadoc != null) {
							edits.add(new Edit(javadoc.getStartPosition(), getJavadocLength(targetDocument, javadoc), newComment));
						} else {
							edits.add(new Edit(node.getStartPosition(), 0, newComment));
						}
					}
					prefix = node.getName().getFullyQualifiedName();
					return true;
				}
				return false;
			}
		});

		for (int i = edits.size() - 1; i >=0 ; i--) {
			Edit edit = edits.get(i);
			try {
				targetDocument.replace(edit.start, edit.length, edit.text);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		/* Rudimentary API consistency checker.
		 * This assumes that:
		 * a) the sourceSubdir (typically win32) API is correct
		 * b) all sourceSubdir API classes, methods and fields do have a comment
		 * c) names that are in the filter list are never API,
		 * 		or they are old API that is defined in the super on some platforms
		 */
		if (comments.size() > 0) {
			String [] filter = new String [] {
				"Color.win32_newDeviceint",
				"Cursor.win32_newDeviceint",
				"Device.hPalette",
				"Font.win32_newDevicelong",
				"FontData.data",
				"FontData.win32_newLOGFONTfloat",
				"FontMetrics.handle",
				"FontMetrics.win32_newTEXTMETRIC",
				"GC.win32_newlongGCData",
				"GC.win32_newDrawableGCData",
				"Image.win32_newDeviceintlong",
				"Pattern.handle",
				"Region.win32_newDeviceint",
				"Control.handle",
				"Display.getSystemFont",
				"Display.msg",
				"Menu.handle",
				"Shell.win32_newDisplaylong",
				"Accessible.internal_WM_GETOBJECTlonglong",
				"TransferData.result",
				"TransferData.stgmedium",
				"TransferData.pIDataObject",
				"TransferData.formatetc",
				"Printer.handle",
				"Printer.checkDevice",
				"TableDragSourceEffect.dragFinishedDragSourceEvent",
				"TableDragSourceEffect.dragStartDragSourceEvent",
				"TableDropTargetEffect.dragOverDropTargetEvent",
				"TableDropTargetEffect.dragEnterDropTargetEvent",
				"TableDropTargetEffect.dragLeaveDropTargetEvent",
				"Transfer.validateObject",
				"TransferData.result",
				"TransferData.stgmedium",
				"TransferData.pIDataObject",
				"TransferData.formatetc",
				"TreeDragSourceEffect.dragFinishedDragSourceEvent",
				"TreeDragSourceEffect.dragStartDragSourceEvent",
				"TreeDropTargetEffect.dragLeaveDropTargetEvent",
				"TreeDropTargetEffect.dragEnterDropTargetEvent",
				"TreeDropTargetEffect.dragOverDropTargetEvent",
				"Printer.createDeviceData",
				"Printer.internal_dispose_GClongGCData",
				"Printer.release",
				"Printer.destroy",
				"Image.handle",
				"Display.getClientArea",
				"TreeItem.handle",
			};
			for (Entry<String, String> entry: comments.entrySet()) {
				String name = entry.getKey();
				if (entry.getValue().length() > 0){
					int i = 0;
					for (i = 0; i < filter.length; i++) {
						if (name.equals(filter[i])) break;
					}
					if (i >= filter.length) {
						System.err.println("***No target for " + name);
					}
				}
			}
		}
		
		String newContents = targetDocument.get();
		if (!targetContents.equals(newContents)) {
			if (makeDirectory(out.getParentFile())) {
				writeFile(newContents.toCharArray(), out);
				fBashed.add(target.toString());
			} else {
				System.out.println("*** Could not create " + out.getParent());
			}
		} else {
			fUnchanged.add(target.toString());
		}
	}

	int getJavadocLength(Document sourceDocument, Javadoc javadoc) {
		return skipWhitespace(sourceDocument, javadoc.getStartPosition() + javadoc.getLength()) - javadoc.getStartPosition();
	}
	
	int skipWhitespace(Document doc, int offset) {
		try {
			while (Character.isWhitespace(doc.getChar(offset))){
				offset++;
			}
		} catch (BadLocationException e) {
		}
		return offset;
	}

	boolean makeDirectory(File directory) {
		if (directory.exists())
			return true;
		return directory.mkdirs();
	}

	List<String> getBashed() {
		return fBashed;
	}

	List<String> getUnchanged() {
		return fUnchanged;
	}

	List<String> getSkipped() {
		return fSkipped;
	}
}
