/*******************************************************************************
 * Copyright (c) 2010-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.ide.eclipse.server.tomcat.core.util;

import com.liferay.ide.eclipse.server.util.CustomPropertiesConfigLayout;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

/**
 * @author Greg Amerson
 */
public class ExternalPropertiesConfiguration extends PropertiesConfiguration {

	public ExternalPropertiesConfiguration() {
		super();

		setDelimiterParsingDisabled(true);
	}

	@Override
	protected PropertiesConfigurationLayout createLayout() {
		return new CustomPropertiesConfigLayout(this);
	}

}
