package com.triadsoft.properties.preferences;

import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.model.utils.IWildcardPath;
import com.triadsoft.properties.model.utils.WildCardPath2;
import com.triadsoft.properties.model.utils.WildcardPath;

/**
 * <p>
 * Dialogo para poder agregar wildcard path a la lista
 * </p>
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @see WildcardPath
 */
public class WilcardPathDialog extends Dialog {

	private static final String PREFERENCES_ADDWP_DIALOG_PREVIEW_LABEL = "preferences.addwp.dialog.previewLabel";
	private static final String PREFERENCES_ADDWP_DIALOG_NEW_WP_LABEL = "preferences.addwp.dialog.newWpLabel";
	private static final String PREFERENCES_ADDWP_DIALOG_TITLE = "preferences.addwp.dialog.title";
	private String _wildcardPath = null;
	private Label label;
	private Text wildcardPath;
	private Text preview;

	public WilcardPathDialog(Shell parent) {
		super(parent);
	}

	@Override
	public int open() {
		return super.open();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		final Label description = new Label(area, SWT.NONE);
		final GridData layoutData = new GridData();
		description.setLayoutData(layoutData);
		description.setText(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADDWP_DIALOG_TITLE));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;

		this.createButtonsArea(area);

		label = new Label(area, SWT.NONE);
		label.setText(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADDWP_DIALOG_NEW_WP_LABEL));
		label.setLayoutData(gridData);
		wildcardPath = new Text(area, SWT.BORDER);
		wildcardPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wildcardPath.setText("/" + IWildcardPath.ROOT_WILDCARD);
		wildcardPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				_wildcardPath = wildcardPath.getText();
				changeData();
			}
		});
		Label previewLabel = new Label(area, SWT.NONE);
		previewLabel.setText(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADDWP_DIALOG_PREVIEW_LABEL));
		preview = new Text(area, SWT.BORDER);
		preview.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		preview.setEnabled(false);
		area.setLayout(gridLayout);
		return area;
	}

	private void createButtonsArea(Composite area) {
		final Composite buttons = new Composite(area, SWT.NULL);
		// final FillLayout layoutData = new FillLayout();
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		buttons.setLayout(layout);
		Button rootButton = new Button(buttons, SWT.PUSH);
		rootButton.setText(IWildcardPath.ROOT_WILDCARD);
		rootButton.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent event) {
				addWildcardToPath(event);
			}

			public void mouseDown(MouseEvent arg0) {
			}

			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});
		Button langButton = new Button(buttons, SWT.PUSH);
		langButton.setText(IWildcardPath.LANGUAGE_WILDCARD);
		langButton.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent event) {
				addWildcardToPath(event);
			}

			public void mouseDown(MouseEvent arg0) {

			}

			public void mouseDoubleClick(MouseEvent arg0) {

			}
		});
		Button countryButton = new Button(buttons, SWT.PUSH);
		countryButton.setText(IWildcardPath.COUNTRY_WILDCARD);
		countryButton.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent event) {
				addWildcardToPath(event);
			}

			public void mouseDown(MouseEvent arg0) {

			}

			public void mouseDoubleClick(MouseEvent arg0) {

			}
		});
		Button filenameButton = new Button(buttons, SWT.PUSH);
		filenameButton.setText(IWildcardPath.FILENAME_WILDCARD);
		filenameButton.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent event) {
				addWildcardToPath(event);
			}

			public void mouseDown(MouseEvent arg0) {

			}

			public void mouseDoubleClick(MouseEvent arg0) {

			}
		});
		Button extensionButton = new Button(buttons, SWT.PUSH);
		extensionButton.setText(IWildcardPath.FILE_EXTENSION_WILDCARD);
		extensionButton.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent event) {
				addWildcardToPath(event);
			}

			public void mouseDown(MouseEvent arg0) {

			}

			public void mouseDoubleClick(MouseEvent arg0) {

			}
		});
		buttons.setVisible(true);
	}

	private void addWildcardToPath(MouseEvent event) {
		Button button = (Button) event.widget;
		String text = wildcardPath.getText();
		int caretPosition = wildcardPath.getCaretPosition();
		wildcardPath.setText(text.substring(0, caretPosition)
				+ button.getText()
				+ text.substring(caretPosition, text.length()));
		wildcardPath.setFocus();
		wildcardPath.setSelection(caretPosition + button.getText().length());
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setSize(400, 200);
		Point position = new Point(
				(getParentShell().getLocation().x
						+ (getParentShell().getSize().x / 2) - (newShell
						.getSize().x / 2)),
				(getParentShell().getLocation().y
						+ (getParentShell().getSize().y / 2) - (newShell
						.getSize().y / 2)));
		newShell.setLocation(position);
		newShell.setText(LocalizedPropertiesPlugin
				.getString(PREFERENCES_ADDWP_DIALOG_TITLE));
	}

	private void changeData() {
		if (_wildcardPath == null || _wildcardPath.length() == 0) {
			preview.setText(null);
			return;
		}
		WildcardPath _path = new WildcardPath(_wildcardPath);
		// Nueva implementacion
		// _path.setLanguage(Locale.getDefault().getLanguage());
		// _path.setCountry(Locale.getDefault().getCountry());
		// _path.setFileExtension("properties");
		// _path.setFileName("application");

		_path.replace(Locale.getDefault());
		_path.replace(IWildcardPath.FILE_EXTENSION_WILDCARD, "properties");
		_path.replace(IWildcardPath.FILENAME_WILDCARD, "application");
		_path.replace(IWildcardPath.ROOT_WILDCARD, "locale");
		String path = _path.getPath();
		preview.setText(path.replaceAll("\\\\.", "\\."));
	}

	public String getWildcardPath() {
		return _wildcardPath;
	}
}
