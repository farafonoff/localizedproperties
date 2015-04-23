package com.triadsoft.properties.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.triadsoft.common.properties.IPropertyFile;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.model.utils.StringUtils;
import com.triadsoft.properties.preferences.LocalizedPropertiesPreferencePage;
import com.triadsoft.properties.preferences.PreferenceConstants;

/**
 * This is an extension of Java API Properties to extend the functionality and
 * add new key/value separators. Also add methods to serve the editor
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @since 0.8.3
 */
public class PropertiesFile extends Properties implements IPropertyFile {

	protected IFile ifile = null;

	protected File file = null;

	protected char separator = '=';

	protected boolean hasEscapedCode = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4061423679037109983L;

	public PropertiesFile() {
		super();
	}

	public PropertiesFile(IFile ifile) {
		this(ifile.getLocation().toFile());
		this.ifile = ifile;
	}

	public PropertiesFile(File file) {
		super();
		this.file = file;
		try {
			FileInputStream fis = new FileInputStream(file);
			load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PropertiesFile(InputStream stream) {
		super();
		try {
			this.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides Properties method, cause I need to detect '~'
	 * codes, used in some type of files as separator. Also I need to keep the
	 * used separator on this file.
	 */
	public synchronized void load(InputStream inStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			Map.Entry<String, String> entry = splitLine(line);
			if (entry!=null) {
				put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	Map.Entry<String, String> splitLine(String line) {
		char[] convtBuf = new char[1024];		
		int limit;
		int keyLen;
		int valueStart;
		char c;
		boolean hasSep;
		boolean precedingBackslash;
		char[] lineBuf;
		while (line.startsWith("\uFEFF")) {//remove BOM
			line = line.substring(1);
		}
		
		limit = line.length();
		lineBuf = line.toCharArray();
		if (line.startsWith("#")) {
			return null;//skip comments here
		}
		c = 0;
		keyLen = 0;
		valueStart = limit;
		hasSep = false;

		// System.out.println("line=<" + new String(lineBuf, 0, limit) +
		// ">");
		precedingBackslash = false;
		while (keyLen < limit) {
			c = lineBuf[keyLen];
			// need check if escaped.
			if ((c == '=' || c == ':' || c == '~') && !precedingBackslash) {
				valueStart = keyLen + 1;
				separator = c;
				hasSep = true;
				break;
			} else if ((c == ' ' || c == '\t' || c == '\f')
					&& !precedingBackslash) {
				valueStart = keyLen + 1;
				break;
			}
			if (c == '\\') {
				precedingBackslash = !precedingBackslash;
			} else {
				precedingBackslash = false;
			}
			keyLen++;
		}
		while (valueStart < limit) {
			c = lineBuf[valueStart];
			if (c != ' ' && c != '\t' && c != '\f') {
				if (!hasSep && (c == '=' || c == ':' || c == '~')) {
					hasSep = true;
					separator = c;
				} else {
					break;
				}
			}
			valueStart++;
		}
		final String xkey = loadConvert(lineBuf, 0, keyLen, convtBuf);
		final String xvalue = loadConvert(lineBuf, valueStart, limit
				- valueStart, convtBuf);
		if (xkey.trim().isEmpty()) {
			return null;
		}
		return new Map.Entry<String, String>() {
			String key = xkey;
			String value = xvalue;
			
			public String getValue() {
				return this.value;
			}
			
			public String getKey() {
				return this.key;
			}

			public String setValue(String value) {
				return this.value = value;
			}
		};
	}

	String loadConvert(char[] in, int off, int len, char[] convtBuf) {
		if (convtBuf.length < len) {
			int newLen = len * 2;
			if (newLen < 0) {
				newLen = Integer.MAX_VALUE;
			}
			convtBuf = new char[newLen];
		}
		char aChar;
		char[] out = convtBuf;
		int outLen = 0;
		int end = off + len;

		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				aChar = in[off++];
				if (aChar == 'u') {
					hasEscapedCode = true;
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = in[off++];
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed \\uxxxx encoding.");
						}
					}
					out[outLen++] = (char) value;
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					out[outLen++] = aChar;
				}
			} else {
				out[outLen++] = (char) aChar;
			}
		}
		return new String(out, 0, outLen);
	}

	private String saveConvert(String theString, boolean escapeSpace,
			boolean escapeUnicode) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
//			case ':': // Fall through
			case '#': // Fall through
//			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

	/**
	 * Convert a nibble to a hex character
	 * 
	 * @param nibble
	 *            the nibble to convert.
	 */
	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	/** A table of hex digits */
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/** ========================== */
	public String[] getKeys() {
		List<String> keyList = new LinkedList<String>();
		Iterator<Object> iter = keySet().iterator();
		while (iter.hasNext()) {
			keyList.add((String) iter.next());
		}
		return keyList.toArray(new String[keyList.size()]);
	}

	/**
	 * This method keep the same state of file. If the file has been loaded
	 * width escaped code, keep same style
	 * 
	 * @throws IOException
	 *             , CoreException
	 */
	public void save() throws IOException, CoreException {
		/*if (ifile != null && ifile.getCharset().equals("UTF-8")
				|| ifile.getCharset().equals("UTF-16BE")
				|| ifile.getCharset().equals("UTF-16LE")
				|| ifile.getCharset().equals("UTF-32BE")
				|| ifile.getCharset().equals("UTF-32LE")) {
			storeFile(file, null, false);
			return;
		}*///don't esc unicode
		storeFile(file, null, false);
	}
	
	public void storeFile(File file, String comments,
			boolean escUnicode) throws IOException {
		File tmpFile = new File(file.getAbsoluteFile()+".bak");
		file.renameTo(tmpFile);		
		storeFile(tmpFile, file, null, false);		
	}
	

	public void storeFile(File template, File file, String comments,
			boolean escUnicode) throws IOException {
		File tmpFile = template.getAbsoluteFile();
		if (tmpFile.equals(file)) {
			storeFile(file, comments, escUnicode);
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(tmpFile), "UTF-8"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF-8"));
		try {
			String srcLine;
			Set<String> keys = new HashSet<String>(Arrays.asList(getKeys()));
			while ((srcLine = br.readLine()) != null) {
				Map.Entry<String, String> entry = splitLine(srcLine);
				if (entry != null) {
					String key = entry.getKey();
					String oldValue = entry.getValue();
					String newValue = getProperty(key);
					if (keys.contains(key)) {
						keys.remove(key);
						if (newValue != null && !newValue.equals(oldValue)) {
							String newConvKey = saveConvert(entry.getKey(),
									true, escUnicode);
							String newConvValue = saveConvert(newValue.trim(),
									false, escUnicode);
							bw.write(newConvKey + separator + newConvValue);
						} else {
							bw.write(srcLine);
						}
					}
				} else {
					bw.write(srcLine);
				}
				bw.newLine();
			}
			for (String key : keys) {
				String newConvKey = saveConvert(key, true, escUnicode);
				String newConvValue = saveConvert(getProperty(key), false,
						escUnicode);
				if (newConvValue != null && !newConvValue.isEmpty()) {
					bw.write(newConvKey + separator + newConvValue);
					bw.newLine();
				}
			}
		} finally {
			br.close();
			bw.close();
		}
	}

	/**
	 * This method force to save file in escaped mode.
	 * 
	 * @throws IOException
	 * @throws CoreException
	 */
	public void saveAsEscapedUnicode() throws IOException, CoreException {
		storeFile(file, null, true);
	}

	/**
	 * This method force to save the file in unescaped mode.
	 * 
	 * @throws IOException
	 * @throws CoreException
	 */
	public void saveAsUnescapedUnicode() throws IOException, CoreException {
		storeFile(file, null, false);
	}

	// private void save(boolean escapedUnicode) throws IOException,
	// CoreException {
	// OutputStream ostream = new FileOutputStream(file);
	// // If parameter is true, then texts will be converted
	// if (escapedUnicode) {
	// store(ostream, null, escapedUnicode);
	// return;
	// }
	// // If parameter is false (the conversion is not forced),
	// // then texts keep same state detected on load
	// store(ostream, null, hasEscapedCode);
	// }

	public IFile getIFile() {
		return ifile;
	}

	public File getFile() {
		return file;
	}

	public boolean hasEscapedCode() {
		return this.hasEscapedCode;
	}

	public void save(File template) throws IOException, CoreException {
		storeFile(template,file, null, false);
	}

}
