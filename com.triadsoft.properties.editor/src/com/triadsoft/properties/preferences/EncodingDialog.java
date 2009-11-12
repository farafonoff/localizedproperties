package com.triadsoft.properties.preferences;

import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.model.utils.WildcardPath;

/**
 * <p>
 * Dialogo para poder agregar relaciones entre el language y el encoding del
 * archivo
 * </p>
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class EncodingDialog extends Dialog {

	private String _languageEncoding = null;
	private Label label;
	private Text languageTxt;
	private Text encodingTxt;

	public EncodingDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		final Label description = new Label(area, SWT.NONE);
		final GridData layoutData = new GridData();
		description.setLayoutData(layoutData);
		description
				.setText(Activator.getString("preferences.encodingSubtitle"));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		label = new Label(area, SWT.NONE);
		label.setText(Activator.getString("preferences.languageLabel"));
		label.setLayoutData(gridData);
		languageTxt = new Text(area, SWT.BORDER);
		languageTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// languageTxt.setText("/{root}");
		languageTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				_languageEncoding = languageTxt.getText() + "_"
						+ encodingTxt.getText();
				// changeData();
			}
		});
		Label previewLabel = new Label(area, SWT.NONE);
		previewLabel.setText(Activator.getString("preferences.encodingLabel"));
		encodingTxt = new Text(area, SWT.BORDER);
		encodingTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		encodingTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				_languageEncoding = languageTxt.getText() + "_"
						+ encodingTxt.getText();
				// changeData();
			}
		});
		encodingTxt.setEnabled(true);
		area.setLayout(gridLayout);
		return area;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Activator.getString("preferences.dialog.title"));
	}

	private void changeData() {
		if (_languageEncoding == null || _languageEncoding.length() == 0) {
			encodingTxt.setText(null);
			return;
		}
		WildcardPath _path = new WildcardPath(_languageEncoding);
		_path.replace(Locale.getDefault());
		_path.replace(WildcardPath.FILE_EXTENSION_WILDCARD, "properties");
		_path.replace(WildcardPath.FILENAME_WILDCARD, "application");
		_path.replace(WildcardPath.ROOT_WILDCARD, "locale");
		encodingTxt.setText(_path.getPath());
	}

	public String getLanguageEncoding() {
		return _languageEncoding;
	}
}