package com.triadsoft.properties.preferences;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.triadsoft.properties.editor.Activator;

/**
 * Editor para los separadores de clave-valor.
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class SeparatorsEditor extends ListEditor {

	private static final String PREFERENCES_SEPARATORS_DEFAULT_BUTTON = "preferences.separators.defaultButton";
	private org.eclipse.swt.widgets.List commandListControl;

	public SeparatorsEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected String createList(String[] items) {
		StringBuffer path = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			path.append(items[i]);
		}
		return path.toString();
	}

	@Override
	protected String getNewInputObject() {
		return null;
	}

	private Button defaultButton;

	@Override
	public Composite getButtonBoxControl(Composite parent) {
		final Composite buttonBoxControl = super.getButtonBoxControl(parent);
		if (defaultButton == null) {
			defaultButton = new Button(buttonBoxControl, SWT.PUSH);
			defaultButton.setText(Activator
					.getString(PREFERENCES_SEPARATORS_DEFAULT_BUTTON));
			defaultButton.setEnabled(true);
			defaultButton.addMouseListener(new MouseListener() {

				public void mouseUp(MouseEvent arg0) {
					String defaultValue = commandListControl.getSelection()[0];
					Activator
							.getDefault()
							.getPluginPreferences()
							.setValue(
									PreferenceConstants.KEY_VALUE_DEFAULT_SEPARATOR_PREFERENCES,
									defaultValue);
					defaultButton.setEnabled(false);
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
		}
		return buttonBoxControl;
	}

	protected void refreshValidState() {
		super.refreshValidState();
		String defaultValue = Activator
				.getDefault()
				.getPluginPreferences()
				.getString(
						PreferenceConstants.KEY_VALUE_DEFAULT_SEPARATOR_PREFERENCES);
		String[] items = commandListControl.getItems();
		int _index = -1;
		for (int j = 0; j < items.length; j++) {
			if (items[j].charAt(0) == defaultValue.charAt(0)) {
				_index = j;
			}
		}
		commandListControl.setSelection(_index);
		commandListControl.showSelection();
		commandListControl.update();
		defaultButton.setEnabled(false);
	}

	@Override
	protected String[] parseString(String separators) {
		List<String> seps = new LinkedList<String>();
		for (int i = 0; i < separators.length(); i++) {
			String ch = separators.substring(i, i + 1);

			seps.add(ch);
		}
		return seps.toArray(new String[seps.size()]);
	}

	@Override
	public org.eclipse.swt.widgets.List getListControl(Composite parent) {
		org.eclipse.swt.widgets.List listControl = super.getListControl(parent);
		if (commandListControl == null) {
			commandListControl = listControl;
			commandListControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String value = commandListControl.getSelection()[0];
					if (value
							.equals(Activator
									.getDefault()
									.getPluginPreferences()
									.getString(
											PreferenceConstants.KEY_VALUE_DEFAULT_SEPARATOR_PREFERENCES))) {
						defaultButton.setEnabled(false);
						return;
					}
					defaultButton.setEnabled(true);
				}
			});
		}
		return listControl;
	}
}
