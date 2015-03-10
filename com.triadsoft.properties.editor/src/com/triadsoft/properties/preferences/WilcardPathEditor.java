package com.triadsoft.properties.preferences;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;

/**
 * This class make the editor used in preferences page. It's handler to wildcard
 * path collection, show and collect values of this collection.
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class WilcardPathEditor extends ListEditor {

	protected Composite parent;
	protected Composite buttonBox;
	protected Button defaultButton;
	protected List listControl;

	public WilcardPathEditor(String preferencesId, String label,
			Composite parent) {
		super(preferencesId, label, parent);
		this.parent = parent;
	}

	protected String[] parseString(String stringList) {
		// Con el id no tengo que hacer nada porque ya se recuperan
		// desde el plugin
		return LocalizedPropertiesPlugin.getWildcardPaths();
	}

	@Override
	protected String createList(String[] items) {
		// Guardo el preference name, porque la lista se guarda de otra manera
		return getPreferenceName();
	}

	@Override
	protected String getNewInputObject() {
		WilcardPathDialog dialog = new WilcardPathDialog(getShell());
		if (dialog.open() != InputDialog.OK) {
			return null;
		}
		return dialog.getWildcardPath();
	}

	protected void refreshValidState() {
		super.refreshValidState();
		int defaultIndex = LocalizedPropertiesPlugin
				.getDefault()
				.getPreferenceStore()
				.getInt(
						PreferenceConstants.WILDCARD_PATH_DEFAULT_INDEX_PREFERENCES);
		listControl.setSelection(defaultIndex);
		listControl.showSelection();
		defaultButton.setEnabled(false);
	}

	public List getListControl(Composite parent) {
		listControl = super.getListControl(parent);
		listControl.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent arg0) {
				selectionChanged1();
			}

			public void mouseDown(MouseEvent arg0) {
			}

			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});
		listControl.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent event) {
				if (event.keyCode == 32) {
					storeDefault();
				}
				selectionChanged1();
			}

			public void keyPressed(KeyEvent arg0) {
			}
		});
		return listControl;
	}

	protected void doLoad() {
		if (listControl != null) {
			IPreferenceStore store = getPreferenceStore();
			java.util.List<String> wps = new LinkedList<String>();
			int index = 0;
			while (store
					.contains(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
							+ index)
					&& store.getString(
							PreferenceConstants.WILDCARD_PATHS_PREFERENCES
									+ index).length() > 0) {
				String value = getPreferenceStore().getString(
						PreferenceConstants.WILDCARD_PATHS_PREFERENCES + index);
				if (value != null && !value.equals("")) {
					wps
							.add(store
									.getString(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
											+ index));
				}
				index++;
			}
			String[] array = wps.toArray(new String[wps.size()]);
			for (int i = 0; i < array.length; i++) {
				listControl.add(array[i]);
			}
		}
	}

	protected void doLoadDefault() {
		if (listControl != null) {
			listControl.removeAll();
			java.util.List<String> defaults = new LinkedList<String>();
			int index = 0;
			IPreferenceStore store = getPreferenceStore();
			// If the default exist and isn't null
			while (store
					.contains(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
							+ index)) {
				String value = getPreferenceStore().getDefaultString(
						PreferenceConstants.WILDCARD_PATHS_PREFERENCES + index);
				if (value != null && !value.equals("")) {
					defaults
							.add(store
									.getDefaultString(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
											+ index));
				}
				index++;
			}
			String[] array = defaults.toArray(new String[defaults.size()]);
			for (int i = 0; i < array.length; i++) {
				listControl.add(array[i]);
			}
		}
	}

	private void storeDefault() {
		LocalizedPropertiesPlugin.getDefault().getPreferenceStore().setValue(
				PreferenceConstants.WILDCARD_PATH_DEFAULT_INDEX_PREFERENCES,
				listControl.getSelectionIndex());
	}

	private void selectionChanged1() {
		if (listControl == null) {
			listControl = getListControl(parent);
		}
		int defaultIndex = LocalizedPropertiesPlugin
				.getDefault()
				.getPreferenceStore()
				.getInt(
						PreferenceConstants.WILDCARD_PATH_DEFAULT_INDEX_PREFERENCES);
		defaultButton
				.setEnabled(listControl.getSelectionIndex() != defaultIndex);
	}

	@Override
	protected void doStore() {
		// super.doStore();
		storeDefault();
		LocalizedPropertiesPlugin.setWildcardPaths(listControl.getItems());
	}

	public Composite getButtonBoxControl(Composite parent) {
		buttonBox = super.getButtonBoxControl(parent);
		defaultButton = new Button(buttonBox, SWT.PUSH);
		defaultButton
				.setText(LocalizedPropertiesMessages
						.getString(SeparatorsEditor.PREFERENCES_SEPARATORS_DEFAULT_BUTTON));
		defaultButton.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent arg0) {
				storeDefault();
				selectionChanged1();
			}

			public void mouseDown(MouseEvent arg0) {
			}

			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		int widthHint = convertHorizontalDLUsToPixels(defaultButton,
				IDialogConstants.BUTTON_WIDTH);
		data.widthHint = Math.max(widthHint, defaultButton.computeSize(
				SWT.DEFAULT, SWT.DEFAULT, true).x);
		defaultButton.setLayoutData(data);
		return buttonBox;
	}

	// private String getSeparator() {
	// String separator = LocalizedPropertiesPlugin
	// .getDefault()
	// .getPreferenceStore()
	// .getString(
	// PreferenceConstants.WILDCARD_PATH_SEPARATOR_PREFERENCES);
	// return separator == null ? "|" : separator;
	// }
}
