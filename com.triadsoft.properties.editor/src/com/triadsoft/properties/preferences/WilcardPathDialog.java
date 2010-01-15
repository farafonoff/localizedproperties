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
 * Dialogo para poder agregar wildcard path a la lista
 * </p>
 * 
 * @author Triad (flores.leonardo@gmail.com)
 *@see WildcardPath
 */
public class WilcardPathDialog extends Dialog {

	private String _wildcardPath = null;
	private Label label;
	private Text wildcardPath;
	private Text preview;

	public WilcardPathDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		final Label description = new Label(area, SWT.NONE);
		final GridData layoutData = new GridData();
		description.setLayoutData(layoutData);
		description.setText(Activator
				.getString("preferences.addwp.dialog.title"));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		label = new Label(area, SWT.NONE);
		label.setText(Activator
				.getString("preferences.addwp.dialog.newWpLabel"));
		label.setLayoutData(gridData);
		wildcardPath = new Text(area, SWT.BORDER);
		wildcardPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wildcardPath.setText("/{root}");
		wildcardPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				_wildcardPath = wildcardPath.getText();
				changeData();
			}
		});
		Label previewLabel = new Label(area, SWT.NONE);
		previewLabel.setText(Activator
				.getString("preferences.addwp.dialog.previewLabel"));
		preview = new Text(area, SWT.BORDER);
		preview.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		preview.setEnabled(false);
		area.setLayout(gridLayout);
		return area;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Activator.getString("preferences.addwp.dialog.title"));
	}

	private void changeData() {
		if (_wildcardPath == null || _wildcardPath.length() == 0) {
			preview.setText(null);
			return;
		}
		WildcardPath _path = new WildcardPath(_wildcardPath);
		_path.replace(Locale.getDefault());
		_path.replace(WildcardPath.FILE_EXTENSION_WILDCARD, "properties");
		_path.replace(WildcardPath.FILENAME_WILDCARD, "application");
		_path.replace(WildcardPath.ROOT_WILDCARD, "locale");
		preview.setText(_path.getPath());
	}

	public String getWildcardPath() {
		return _wildcardPath;
	}
}
