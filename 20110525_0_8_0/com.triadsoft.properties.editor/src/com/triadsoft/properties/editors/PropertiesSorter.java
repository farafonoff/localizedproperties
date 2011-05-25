package com.triadsoft.properties.editors;

import java.util.Locale;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.triadsoft.properties.model.Property;

public class PropertiesSorter extends ViewerSorter {
	private TableViewer viewer;
	private int propertyIndex;

	private Locale locale;

	public PropertiesSorter(TableViewer viewer) {
		this.propertyIndex = 0;
		this.viewer = viewer;
	}

	public void setColumn(int column) {
		int direction = viewer.getTable().getSortDirection();
		if (column == this.propertyIndex) {
			viewer.getTable().setSortDirection(
					direction == SWT.UP ? SWT.DOWN : SWT.UP);
		} else {
			this.propertyIndex = column;
			viewer.getTable().setSortDirection(SWT.UP);
		}
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		Property p1 = (Property) e1;
		Property p2 = (Property) e2;
		int rc = 0;
		switch (propertyIndex) {
		case 0:
			String key1 = p1.getKey();
			String key2 = p2.getKey();
			// rc = p1.getKey().compareTo(p2.getKey());
			rc = key1.toUpperCase().compareTo(key2.toUpperCase());
			break;
		default:
			String text1 = p1.getValue(locale);
			String text2 = p2.getValue(locale);
			rc = text1.toUpperCase().compareTo(text2.toUpperCase());
		}

		if (this.viewer.getTable().getSortDirection() == SWT.DOWN) {
			rc = -rc;
		}
		return rc;
	}
}
