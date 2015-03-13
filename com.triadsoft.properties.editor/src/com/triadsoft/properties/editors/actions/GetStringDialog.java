package com.triadsoft.properties.editors.actions;

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

import com.triadsoft.common.utils.LocalizedPropertiesMessages;

/**
 * Dialogo para agregar una nueva clave al archivo de recursos
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class GetStringDialog extends Dialog {

	private Label label;
	private Text wildcardPath;
	private String enteredText;
	
	String title;
	String textLabel;
	String description;

	public GetStringDialog(Shell shell, String title, String textLabel, String description, String defaultText) {
		super(shell);
		this.title = title;
		this.textLabel = textLabel;
		this.description = description;
		this.enteredText = defaultText;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		final Label description = new Label(area, SWT.NONE);
		final GridData layoutData = new GridData();
		description.setLayoutData(layoutData);
		description.setText(LocalizedPropertiesMessages
				.getString(this.description));

		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		label = new Label(area, SWT.NONE);
		label.setText(LocalizedPropertiesMessages
				.getString(this.textLabel));
		label.setLayoutData(gridData);
		wildcardPath = new Text(area, SWT.BORDER);
		wildcardPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wildcardPath.setText(enteredText);
		wildcardPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				enteredText = wildcardPath.getText();
				changeData();
			}
		});
		area.setLayout(gridLayout);
		return area;
	}

	private void changeData() {
		if (enteredText != null && enteredText.length() == 0) {
			enteredText = null;
			return;
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(LocalizedPropertiesMessages
				.getString(this.title));
	}

	public String getText() {
		return enteredText;
	}
}
