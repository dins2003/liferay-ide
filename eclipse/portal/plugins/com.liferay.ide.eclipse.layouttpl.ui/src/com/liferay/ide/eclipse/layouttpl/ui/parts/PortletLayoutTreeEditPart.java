/*******************************************************************************
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************/

package com.liferay.ide.eclipse.layouttpl.ui.parts;



import com.liferay.ide.eclipse.layouttpl.ui.LayoutTplUI;
import com.liferay.ide.eclipse.layouttpl.ui.model.ModelElement;
import com.liferay.ide.eclipse.layouttpl.ui.model.PortletLayout;
import com.liferay.ide.eclipse.layouttpl.ui.policies.PortletLayoutComponentEditPolicy;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;


public class PortletLayoutTreeEditPart extends BaseTreeEditPart {

	protected Image icon;

	public PortletLayoutTreeEditPart(PortletLayout model) {
		super(model);

		URL url = LayoutTplUI.getDefault().getBundle().getEntry("/icons/e16/layout.png");
		icon = ImageDescriptor.createFromURL(url).createImage();
	}


	protected void createEditPolicies() {
		// allow removal of the associated model element
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PortletLayoutComponentEditPolicy());
	}


	protected PortletLayout getCastedModel() {
		return (PortletLayout) getModel();
	}

	protected List<ModelElement> getModelChildren() {
		return getCastedModel().getColumns();
	}

	protected Image getImage() {
		return icon;
	}


	protected String getText() {
		String text = "Portlet Row";

		int numcols = getCastedModel().getColumns().size();

		if (numcols == 1) {
			text += " - 1 column";
		}
		else if (numcols > 1) {
			text += " - " + numcols + " columns";
		}

		return text;
	}


	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (PortletLayout.COLUMN_ADDED_PROP.equals(prop)) {
			// add a child to this edit part
			// causes an additional entry to appear in the tree of the outline
			// view
			addChild(createChild(evt.getNewValue()), -1);
		}
		else if (PortletLayout.COLUMN_REMOVED_PROP.equals(prop)) {
			// remove a child from this edit part
			// causes the corresponding edit part to disappear from the tree in
			// the outline view
			removeChild(getEditPartForChild(evt.getNewValue()));
		}
		else {
			refreshVisuals();
		}
	}
}
