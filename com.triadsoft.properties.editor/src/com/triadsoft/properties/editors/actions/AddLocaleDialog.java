package com.triadsoft.properties.editors.actions;

import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.model.utils.IWildcardPath;

/**
 * Dialogo para agregar una un nuevo locale
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class AddLocaleDialog extends Dialog {

	private static final String PREFERENCES_ADD_LOCALE_DIALOG_TITLE_ERRORS = "preferences.add.locale.dialog.title.errors";
	private static final String PREFERENCES_ADD_LOCALE_DIALOG_COUNTRY_ERROR = "preferences.add.locale.dialog.country.error";
	private static final String PREFERENCES_ADD_LOCALE_DIALOG_LANGUAGE_ERROR = "preferences.add.locale.dialog.language.error";
	private static final String PREFERENCES_ADD_LOCALE_DIALOG_TITLE = "preferences.add.locale.dialog.title";
	private static final String PREFERENCES_ADD_DIALOG_NEW_LOCALE_LABEL = "preferences.add.locale.dialog.language.label";
	private static final String PREFERENCES_ADD_DIALOG_NEW_COUNTRY_LABEL = "preferences.add.locale.dialog.country.label";
	private static final String PREFERENCES_ADD_DIALOG_DESCRIPTION = "preferences.add.locale.dialog.description";
	private Text locale;
	private Text country;
	private String errors;

	private String languageString;
	private String countryString;

	public AddLocaleDialog(Shell shell) {
		super(shell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		FillLayout descriptionLayout = new FillLayout();
		descriptionLayout.marginHeight = 0;

		GridLayout labelLayout = new GridLayout();
		labelLayout.numColumns = 4;
		area.setLayout(labelLayout);

		final Label descriptionLabel = new Label(area, SWT.NONE);
		descriptionLabel.setText(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADD_DIALOG_DESCRIPTION));
		GridData descriptionData = new GridData(GridData.FILL_HORIZONTAL);
		descriptionData.horizontalSpan = 4;
		descriptionLabel.setLayoutData(descriptionData);

		final Label localeLabel = new Label(area, SWT.NONE);
		localeLabel.setText(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADD_DIALOG_NEW_LOCALE_LABEL));
		// localeLabel.setLayoutData(gridData);

		final Label countryLabel = new Label(area, SWT.NONE);
		countryLabel.setText(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADD_DIALOG_NEW_COUNTRY_LABEL));

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		locale = new Text(area, SWT.BORDER);
		locale.setText("");
		locale.setMessage(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADD_LOCALE_DIALOG_LANGUAGE_ERROR));
		locale.setTextLimit(2);
		locale.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
			}
		});
		locale.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				if (locale.getText() != null
						&& !locale.getText().equals(
								locale.getText().toLowerCase())) {
					locale.setText(locale.getText().toLowerCase());
				}
				changeData();
			}

			public void focusGained(FocusEvent arg0) {
			}
		});
		country = new Text(area, SWT.BORDER);
		country.setText("");
		country.setMessage(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADD_LOCALE_DIALOG_COUNTRY_ERROR));
		country.setTextLimit(2);
		country.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
			}
		});
		country.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				if (country.getText() != null) {
					country.setText(country.getText().toUpperCase());
				}
				changeData();
			}

			public void focusGained(FocusEvent arg0) {
			}
		});
		area.setLayout(gridLayout);
		return area;
	}

	private void changeData() {
		languageString = locale.getText();
		countryString = country.getText();
		if (!validate()) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setText(LocalizedPropertiesPlugin
					.getString(PREFERENCES_ADD_LOCALE_DIALOG_TITLE_ERRORS));
			messageBox.setMessage(errors);
			if (messageBox.open() == SWT.OK) {
				return;
			}
		}
	}

	/**
	 * Validaciones para que se coloquen los valores correctos de pais y
	 * lenguaje
	 * 
	 * @return False cuando ocurre algun error
	 */
	private boolean validate() {
		errors = "";
		if (languageString != null && !languageString.matches(IWildcardPath.LANGUAGE_REGEX)) {
			errors += locale.getMessage();
		}
		if (countryString != null && countryString.length() > 0
				&& !countryString.matches(IWildcardPath.COUNTRY_REGEX)) {
			errors += "\n" + country.getMessage();
		}
		getButton(IDialogConstants.OK_ID).setEnabled(
				errors.length() > 0 ? false : true);
		return errors.length() > 0 ? false : true;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADD_LOCALE_DIALOG_TITLE));
	}

	public Locale getNewLocale() {
		if (languageString != null && countryString != null) {
			return new Locale(languageString, countryString);
		}
		if (languageString == null) {
			return new Locale(languageString);
		}
		return null;
	}
}