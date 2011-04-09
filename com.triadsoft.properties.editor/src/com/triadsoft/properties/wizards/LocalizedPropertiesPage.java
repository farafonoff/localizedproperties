package com.triadsoft.properties.wizards;

import java.util.Locale;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.model.utils.LocalizedPropertiesLog;
import com.triadsoft.properties.model.utils.WildCardPath2;
import com.triadsoft.properties.preferences.PreferenceConstants;

/**
 * Permite crear un archivo de propiedades localizado por el idioma
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */

public class LocalizedPropertiesPage extends WizardPage {
	private static final String WIZARD_PAGE_DEFAULT_MODIFY = "wizard.page.default.modify";

	private static final String WIZARD_PAGE_DEFAULT_USE = "wizard.page.default.use";

	private static final String WIZARD_PAGE_FILENAME_ERROR_INVALID = "wizard.page.filename.error.invalid";

	private static final String WIZARD_PAGE_FOLDER_FILENAME_ERROR_VOID = "wizard.page.filename.error.void";

	private static final String WIZARD_PAGE_FOLDER_CONTAINER_ERROR_NOTWRITABLE = "wizard.page.folder.container.error.notwritable";

	private static final String WIZARD_PAGE_FOLDER_CONTAINER_ERROR_NOTEXIST = "wizard.page.folder.container.error.notexist";

	private static final String WIZARD_PAGE_FOLDER_CONTAINER_ERROR = "wizard.page.folder.container.error.void";

	private static final String WIZARD_PAGE_FOLDER_CONTAINER_MESSAGE = "wizard.page.folder.container.message";

	private static final String WIZARD_PAGE_WILCARD_COUNTRY_LABEL = "wizard.page.wilcard.country.label";

	private static final String WIZARD_PAGE_WILCARD_LANGUAJE_LABEL = "wizard.page.wilcard.languaje.label";

	private static final String WIZARD_PAGE_WILCARD_FILENAME_LABEL = "wizard.page.wilcard.filename.label";

	// private static final String WIZARD_PAGE_WILCARD_ROOT_LABEL =
	// "wizard.page.wilcard.root.label";

	private static final String WIZARD_PAGE_WILCARD_LIST_LABEL = "wizard.page.wilcard.list.label";

	private static final String WIZARD_PAGE_FOLDER_BUTTON = "wizard.page.folder.button";

	private static final String WIZARD_PAGE_FOLDER_LABEL = "wizard.page.folder.label";

	private static final String WIZARD_PAGE_WILCARDPATH_LABEL = "wizard.page.wilcardpath.label";

	private static final String WIZARD_PAGE_DESCRIPTION = "wizard.page.description";

	private static final String WIZARD_PAGE_TITLE = "wizard.page.title";

	private Text fileText;

	private ISelection selection;

	private List wildcardList;

	private Label filenameLabel;

	private Label langLabel;

	private Label countryLabel;

	// private Text rootText;

	private Text languageText;

	private Text countryText;

	private Label filepathLabel;

	private Text filepathText;

	private Text containerText;
	private boolean useDefaults = true;
	private Button useDefaultsButton;
	private Label useDefaultsLabel;

	private Composite folderPathContainer;
	private Composite listContainer;

	private IResource resource;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public LocalizedPropertiesPage(ISelection selection) {
		super("wizardPage");
		setTitle(LocalizedPropertiesPlugin.getString(WIZARD_PAGE_TITLE));
		setDescription(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_DESCRIPTION));
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		try {
			Composite container = new Composite(parent, SWT.NULL);

			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			layout.verticalSpacing = 9;
			container.setLayout(layout);

			this.createFileParts(container);
			this.createUseDefaults(container);
			this.createFolderPath(container);
			this.createList(container);

			initialize();
			dialogChanged();
			defaultsChanged();
			setControl(container);
		} catch (Exception e) {
			LocalizedPropertiesLog.error(e.getLocalizedMessage());
		}
	}

	private void createUseDefaults(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		useDefaultsLabel = new Label(container, SWT.NULL);
		useDefaultsLabel.setText(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_DEFAULT_USE));

		useDefaultsButton = new Button(container, SWT.CHECK);
		useDefaultsButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				useDefaults = !useDefaults;
				defaultsChanged();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	private void defaultsChanged() {
		// TODO: Quedaría mejor hacerlo deshabilitado en lugar de invisible
		folderPathContainer.setVisible(!useDefaults);
		listContainer.setVisible(!useDefaults);
		if (!useDefaults) {
			// useDefaultsLabel.setText(Activator
			// .getString(WIZARD_PAGE_DEFAULT_USE));
		} else {
			useDefaultsLabel.setText(LocalizedPropertiesPlugin
					.getString(WIZARD_PAGE_DEFAULT_MODIFY));
		}
	}

	private void createFileParts(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		container.setLayoutData(layoutData);

		// Label rootLabel = new Label(container, SWT.NULL);
		// rootLabel.setText(Activator.getString(WIZARD_PAGE_WILCARD_ROOT_LABEL));

		filenameLabel = new Label(container, SWT.NULL);
		filenameLabel.setText(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_WILCARD_FILENAME_LABEL));

		langLabel = new Label(container, SWT.NULL);
		langLabel.setText(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_WILCARD_LANGUAJE_LABEL));

		countryLabel = new Label(container, SWT.NULL);
		countryLabel.setText(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_WILCARD_COUNTRY_LABEL));

		// rootText = new Text(container, SWT.BORDER | SWT.SINGLE);
		// rootText.setText("locale");
		GridData fullData = new GridData(GridData.FILL_HORIZONTAL);
		// rootText.setLayoutData(fullData);
		// rootText.addModifyListener(new ModifyListener() {
		// public void modifyText(ModifyEvent arg0) {
		// changeFilepath();
		// }
		// });

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				changeFilepath();
				dialogChanged();
			}
		});
		fileText.setLayoutData(fullData);

		languageText = new Text(container, SWT.BORDER | SWT.SINGLE);
		languageText.setText(Locale.getDefault().getLanguage());
		languageText.setLayoutData(fullData);
		languageText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				changeFilepath();
			}
		});

		countryText = new Text(container, SWT.BORDER | SWT.SINGLE);
		countryText.setText(Locale.getDefault().getCountry());
		countryText.setLayoutData(fullData);
		countryText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				changeFilepath();
			}
		});
		filepathLabel = new Label(container, SWT.NULL);
		filepathLabel.setText(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_WILCARDPATH_LABEL));
		GridData filepath = new GridData();
		filepath.horizontalSpan = 3;
		filepathLabel.setLayoutData(filepath);

		filepathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		filepathText.setText("<resultado>");
		GridData filepathData = new GridData(GridData.FILL_HORIZONTAL);
		filepathData.horizontalSpan = 3;
		filepathText.setLayoutData(filepathData);
		filepathText.setEnabled(false);
	}

	private void createFolderPath(Composite parent) {
		folderPathContainer = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		folderPathContainer.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		folderPathContainer.setLayoutData(layoutData);
		Label label = new Label(folderPathContainer, SWT.NULL);
		label.setText(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_FOLDER_LABEL));
		GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
		labelData.horizontalSpan = 2;
		label.setLayoutData(labelData);
		containerText = new Text(folderPathContainer, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				changeFilepath();
				dialogChanged();
			}
		});

		Button button = new Button(folderPathContainer, SWT.PUSH);
		button.setText(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_FOLDER_BUTTON));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
	}

	private void createList(Composite parent) {
		listContainer = new Composite(parent, SWT.NULL);
		Label listLabel = new Label(listContainer, SWT.NULL);
		listLabel.setText(LocalizedPropertiesPlugin
				.getString(WIZARD_PAGE_WILCARD_LIST_LABEL));
		GridLayout layout = new GridLayout(1, false);
		listContainer.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		listContainer.setLayoutData(layoutData);

		GridData pathsData = new GridData();
		pathsData.horizontalSpan = 4;
		listLabel.setLayoutData(pathsData);

		wildcardList = new List(listContainer, SWT.SINGLE);
		wildcardList.setItems(LocalizedPropertiesPlugin.getWildcardPaths());
		wildcardList
				.setSelection(LocalizedPropertiesPlugin
						.getDefault()
						.getPreferenceStore()
						.getInt(PreferenceConstants.WILDCARD_PATH_DEFAULT_INDEX_PREFERENCES));
		GridData wildData = new GridData(GridData.FILL_HORIZONTAL);
		wildData.horizontalSpan = 4;
		wildcardList.setLayoutData(wildData);
		wildcardList.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent evt) {
				changeFilepath();
				dialogChanged();
			}

			public void widgetDefaultSelected(SelectionEvent evt) {
			}
		});
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				resource = (IResource) obj;
			} else if (obj instanceof IProject) {
				resource = (IResource) obj;
			} else if (obj instanceof IJavaElement) {
				IJavaElement element = (IJavaElement) obj;
				resource = element.getResource();
			}

			if (resource == null) {
				return;
			}
			containerText.setText(resource.getFullPath().toString());
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				LocalizedPropertiesPlugin
						.getString(WIZARD_PAGE_FOLDER_CONTAINER_MESSAGE));
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	private void changeFilepath() {
		String[] selected = wildcardList.getSelection();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		resource = root.findMember(new Path(containerText.getText()));
		if (selected == null || selected.length == 0) {
			filepathText.setText("");
			return;
		}
		// WildcardPath wp = new WildcardPath(selected[0]);
		// // TODO:Validar que no vengan vacios
		// wp.replace(IWildcardPath.COUNTRY_WILDCARD, countryText.getText());
		// wp.replace(IWildcardPath.LANGUAGE_WILDCARD, languageText.getText());
		//
		// if (resource != null) {
		// if (wp.haveRoot()) {
		// wp.replace(IWildcardPath.ROOT_WILDCARD, resource.getFullPath()
		// .lastSegment());
		// }
		// }
		// wp.replace(IWildcardPath.FILENAME_WILDCARD, fileText.getText());
		// wp.replace(IWildcardPath.FILE_EXTENSION_WILDCARD, "properties");
		// // FIXME: Ver que el wilcard path deja los puntos escapeados
		// wp.replace("\\.", ".");
		// wp.replace("\\\\.", ".");
		// wp.replace("\\\\._", ".");
		// wp.replace("\\_\\\\.", "\\.");
		// wp.replace("\\_/", "/");
		WildCardPath2 wp = new WildCardPath2(selected[0]);
		filepathText.setText(wp.getPath());
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		String fileName = getFileName();

		if (this.resource == null) {
			updateStatus(LocalizedPropertiesPlugin
					.getString(WIZARD_PAGE_FOLDER_CONTAINER_ERROR));
			return;
		}
		if (this.resource == null
				|| (resource.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus(LocalizedPropertiesPlugin
					.getString(WIZARD_PAGE_FOLDER_CONTAINER_ERROR_NOTEXIST));
			return;
		}
		if (!this.resource.isAccessible()) {
			updateStatus(LocalizedPropertiesPlugin
					.getString(WIZARD_PAGE_FOLDER_CONTAINER_ERROR_NOTWRITABLE));
			return;
		}
		if (fileName.length() == 0) {
			updateStatus(LocalizedPropertiesPlugin
					.getString(WIZARD_PAGE_FOLDER_FILENAME_ERROR_VOID));
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus(LocalizedPropertiesPlugin
					.getString(WIZARD_PAGE_FILENAME_ERROR_INVALID));
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public IResource getResource() {
		return resource;
	}

	public IPath getFilePath() {
		return new Path(filepathText.getText());
	}

	public String getFileName() {
		return fileText.getText();
	}
}