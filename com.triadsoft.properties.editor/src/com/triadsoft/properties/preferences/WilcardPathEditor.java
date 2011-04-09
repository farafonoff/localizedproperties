package com.triadsoft.properties.preferences;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
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

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;

/**
 * This class make the editor used in preferences page.
 * It's handler to wildcard path collection, show and collect values of this collection.  
 * @author Triad (flores.leonardo@gmail.com)
 */
public class WilcardPathEditor extends ListEditor {

	protected Composite parent;
	protected Composite buttonBox;
	protected Button defaultButton;
	protected List listControl;

	public WilcardPathEditor(String preferencesId, String label, Composite parent) {
		super(preferencesId, label, parent);
		this.parent = parent;
	}

	protected String[] parseString(String stringList) {
		String regex = "\\" + getSeparator();
		String[] strings = stringList.split(regex);
		return strings;
	}

	@Override
	protected String createList(String[] items) {
		StringBuffer path = new StringBuffer();
		String separator = getSeparator();
		for (int i = 0; i < items.length; i++) {
			path.append(items[i]);
			path.append(separator);
		}
		return path.toString();
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
				.getPluginPreferences()
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

	private void storeDefault() {
		LocalizedPropertiesPlugin.getDefault().getPluginPreferences().setValue(
				PreferenceConstants.WILDCARD_PATH_DEFAULT_INDEX_PREFERENCES,
				listControl.getSelectionIndex());
	}

	private void selectionChanged1() {
		if (listControl == null) {
			listControl = getListControl(parent);
		}
		int defaultIndex = LocalizedPropertiesPlugin
				.getDefault()
				.getPluginPreferences()
				.getInt(
						PreferenceConstants.WILDCARD_PATH_DEFAULT_INDEX_PREFERENCES);
		defaultButton
				.setEnabled(listControl.getSelectionIndex() != defaultIndex);
	}

	@Override
	protected void doStore() {
		super.doStore();
		storeDefault();
	}

	public Composite getButtonBoxControl(Composite parent) {
		buttonBox = super.getButtonBoxControl(parent);
		defaultButton = new Button(buttonBox, SWT.PUSH);
		defaultButton
				.setText(LocalizedPropertiesPlugin
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

	private String getSeparator() {
		String separator = LocalizedPropertiesPlugin
				.getDefault()
				.getPluginPreferences()
				.getString(
						PreferenceConstants.WILDCARD_PATH_SEPARATOR_PREFERENCES);
		return separator == null ? "|" : separator;
	}
}
