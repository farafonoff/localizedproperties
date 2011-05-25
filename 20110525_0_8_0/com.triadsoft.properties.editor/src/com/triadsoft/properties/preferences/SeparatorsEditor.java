package com.triadsoft.properties.preferences;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;

/**
 * Editor para los separadores de clave-valor.
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class SeparatorsEditor extends ListEditor {

	protected static final String PREFERENCES_SEPARATORS_DEFAULT_BUTTON = "preferences.separators.defaultButton";
	private org.eclipse.swt.widgets.List commandListControl;
	private Composite buttonBox;
	private Composite parent;

	public SeparatorsEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
		this.parent = parent;
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
		// buttonBox = super.getButtonBoxControl(parent);
		if (buttonBox == null) {
			buttonBox = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			buttonBox.setLayout(layout);
		}

		if (defaultButton == null) {
			defaultButton = new Button(buttonBox, SWT.PUSH);
			defaultButton.setText(LocalizedPropertiesPlugin
					.getString(PREFERENCES_SEPARATORS_DEFAULT_BUTTON));
			defaultButton.setEnabled(true);
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
			data.widthHint = Math
					.max(widthHint, defaultButton.computeSize(SWT.DEFAULT,
							SWT.DEFAULT, true).x);
			defaultButton.setLayoutData(data);
		}
		return buttonBox;
	}

	private void selectionChanged1() {
		if (commandListControl == null) {
			commandListControl = getListControl(parent);
		}
		String value = commandListControl.getSelection()[0];
		String defaultValue = LocalizedPropertiesPlugin
				.getDefault()
				.getPreferenceStore()
				.getString(
						PreferenceConstants.KEY_VALUE_DEFAULT_SEPARATOR_PREFERENCES);
		defaultButton.setEnabled(!defaultValue.equals(value));
	}

	protected void refreshValidState() {
		super.refreshValidState();
		String defaultValue = LocalizedPropertiesPlugin
				.getDefault()
				.getPreferenceStore()
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
		commandListControl = super.getListControl(parent);
		commandListControl.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				selectionChanged1();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				selectionChanged1();
			}
		});

		commandListControl.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent arg0) {
				selectionChanged1();
			}

			public void mouseDown(MouseEvent arg0) {
			}

			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});

		commandListControl.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent event) {
				if (event.keyCode == 32) {
					storeDefault();
				}
				selectionChanged1();
			}

			public void keyPressed(KeyEvent arg0) {
			}
		});
		return commandListControl;
	}

	public void dispose() {
		super.dispose();
		// org.eclipse.swt.widgets.List listControl =
		// super.getListControl(parent);
	}

	@Override
	protected void doStore() {
		super.doStore();
		this.storeDefault();
	}

	protected void storeDefault() {
		String value = commandListControl.getSelection()[0];
		LocalizedPropertiesPlugin
				.getDefault()
				.getPreferenceStore()
				.setValue(
						PreferenceConstants.KEY_VALUE_DEFAULT_SEPARATOR_PREFERENCES,
						value);
	}
}
