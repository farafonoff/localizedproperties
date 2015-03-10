package com.triadsoft.properties.editors;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.triadsoft.properties.model.ResourceList;

public class PropertiesContentProvider implements IStructuredContentProvider {

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getElements(Object obj) {
		if (obj instanceof ResourceList) {
			return ((ResourceList) obj).getProperties();
		}
		return null;
	}
}
