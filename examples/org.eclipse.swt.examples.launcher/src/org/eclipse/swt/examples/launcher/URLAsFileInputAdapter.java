package org.eclipse.swt.examples.launcher;/* * (c) Copyright IBM Corp. 2000, 2001. * All Rights Reserved */import java.io.*;import java.net.URL;
/** * Grants access to a URL as if it were a file. *  * There _must_ be a better way of doing this! */
public class URLAsFileInputAdapter {
	URL  url;
	File file;
	/**	 * Constructs an adapter control object.	 * 	 * @param url the URL to be accessed	 */	public URLAsFileInputAdapter(URL url) {
		this.url = url;
	}		/**	 * Disposes of any allocated resources, called when finished with the file.	 */
	public void dispose() {
		file.delete();
		file = null;
	}		/**	 * Returns a File object corresponding to the URL specified in the constructor.	 * 	 * @return a read-only File object corresponding to the URL	 */
	public File getFile() throws IOException {
		if (file != null) return file;
		BufferedInputStream is = null;
		BufferedOutputStream os = null;
		try {
			is = new BufferedInputStream(url.openConnection().getInputStream());

			file = File.createTempFile("urlcache", "tmp");
			file.deleteOnExit();

			os = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buffer = new byte[4096];
			for (;;) {
				int count = is.read(buffer, 0, buffer.length);
				if (count < 0) break;
				os.write(buffer, 0, count);
			}
			file.setReadOnly();
			return file;
		} finally {
			if (is != null) is.close();
			if (os != null) os.close();
		}
	}

}
