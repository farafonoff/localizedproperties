package com.triadsoft.properties.model.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import com.triadsoft.properties.model.Property;

public class PropertyTransfer extends ByteArrayTransfer {

	private static final String MYTYPENAME = "my_type_name";
	private static final int MYTYPEID = registerType(MYTYPENAME);
	private static PropertyTransfer _instance = new PropertyTransfer();

	private PropertyTransfer() {
	}

	public static PropertyTransfer getInstance() {
		return _instance;
	}

	public void javaToNative(Object object, TransferData transferData) {
		if (object == null || !(object instanceof Property[]))
			return;

		if (isSupportedType(transferData)) {
			Property[] properties = (Property[]) object;
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				DataOutputStream writeOut = new DataOutputStream(out);
				writeOut.writeInt(properties.length);
				for (int i = 0, length = properties.length; i < length; i++) {
					writeOut.writeUTF(properties[i].getKey());
					int localesLength = properties[i].getLocales().length;
					writeOut.writeInt(localesLength);
					for (int j = 0; j < properties[i].getLocales().length; j++) {
						String value = properties[i].getValue(properties[i]
								.getLocales()[j]);
						writeOut.writeUTF(properties[i].getLocales()[j]
								.toString());
						writeOut.writeUTF(value);
					}
				}
				byte[] buffer = out.toByteArray();
				writeOut.close();
				super.javaToNative(buffer, transferData);
			} catch (IOException e) {
			}
		}
	}

	public Object nativeToJava(TransferData transferData) {
		if (isSupportedType(transferData)) {
			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null) {
				return null;
			}
			List<Property> properties = new LinkedList<Property>();
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				DataInputStream readIn = new DataInputStream(in);
				int propertiesSize = readIn.readInt();
				for (int i = 0; i < propertiesSize; i++) {
					String key = readIn.readUTF();
					Property property = new Property(key);
					int localesLength = readIn.readInt();
					for (int j = 0; j < localesLength; j++) {
						String locString = readIn.readUTF();
						String propValue = readIn.readUTF();
						Locale locale = StringUtils.getLocale(locString);
						property.setValue(locale, propValue);
					}
					properties.add(property);
				}
				readIn.close();
			} catch (IOException ex) {
				return null;
			}
			return properties;
		}

		return null;
	}

	protected String[] getTypeNames() {
		return new String[] { MYTYPENAME };
	}

	protected int[] getTypeIds() {
		return new int[] { MYTYPEID };
	}
}
