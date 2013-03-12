package com.triadsoft.properties.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;

/**
 * Pagina de preferencias para los Wildcards Paths
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class LocalizedPropertiesPreferencePage extends
		FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final String PREFERENCES_DESCRIPTION = "preferences.description";
	private static final String PREFERENCES_SEPARATORS_LABEL = "preferences.separators.label";
	private static final String PREFERENCES_WILDCARD_TITLE_LABEL = "preferences.wildcardTitle.label";
	private static final String PREFERENCES_SORTED_KEYS_LABEL = "preferences.sorted_keys.label";

	public LocalizedPropertiesPreferencePage() {
		super(GRID);
		setPreferenceStore(LocalizedPropertiesPlugin.getDefault()
				.getPreferenceStore());
		setDescription(LocalizedPropertiesMessages
				.getString(PREFERENCES_DESCRIPTION));
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new WilcardPathEditor(PreferenceConstants.WILDCARD_PATHS_ID,
				LocalizedPropertiesMessages
						.getString(PREFERENCES_WILDCARD_TITLE_LABEL),
				getFieldEditorParent())); //$NON-NLS-1$
		// addField(new EncodingEditor(
		// PreferenceConstants.LANGUAGE_CONTENT_TYPE_PREFERENCES,
		// Activator.getString("preferences.encodingTitle.label"),
		// getFieldEditorParent()));

		addField(new SeparatorsEditor(
				PreferenceConstants.KEY_VALUE_SEPARATORS_PREFERENCES,
				LocalizedPropertiesMessages
						.getString(PREFERENCES_SEPARATORS_LABEL),
				getFieldEditorParent()));

		addField(new BooleanFieldEditor(
				PreferenceConstants. KEY_SORTERED_PREFERENCES ,
				LocalizedPropertiesMessages
						.getString(PREFERENCES_SORTED_KEYS_LABEL),
				getFieldEditorParent()));
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
}