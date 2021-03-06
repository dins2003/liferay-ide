/*******************************************************************************
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
 * Contributors:
 * 		Gregory Amerson - initial implementation and ongoing maintenance
 *******************************************************************************/

package com.liferay.ide.eclipse.service.ui;


import com.liferay.ide.eclipse.service.core.model.IServiceBuilder600;
import com.liferay.ide.eclipse.service.core.model.IServiceBuilder610;
import com.liferay.ide.eclipse.service.core.model.ServiceBuilderVersionType;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.xml.RootXmlResource;
import org.eclipse.sapphire.modeling.xml.XmlResourceStore;
import org.eclipse.sapphire.ui.gef.diagram.editor.SapphireDiagramEditor;
import org.eclipse.sapphire.ui.swt.xml.editor.SapphireEditorForXml;
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;


/**
 * @author Gregory Amerson
 */
public class ServiceBuilderEditor extends SapphireEditorForXml {

	private SapphireDiagramEditor pageDiagram;

	public ServiceBuilderEditor() {
		super(ServiceUI.PLUGIN_ID);

		setEditorDefinitionPath( ServiceUI.PLUGIN_ID +
			"/com/liferay/ide/eclipse/service/ui/ServiceBuilder.sdef/serviceBuilderPage" );
	}

	@Override
	public void doSave( final IProgressMonitor monitor )
	{
		super.doSave( monitor );

		this.pageDiagram.doSave( monitor );
	}

	protected ServiceBuilderVersionType getDTDVersion( Document document )
	{
		ServiceBuilderVersionType dtdVersion = null;
		DocumentType docType = document.getDoctype();

		if ( docType != null )
		{
			String publicId = docType.getPublicId();
			String systemId = docType.getSystemId();
			if ( publicId != null && systemId != null )
			{
				if ( publicId.contains( "6.0.0" ) || systemId.contains( "6.0.0" ) )
				{
					dtdVersion = ServiceBuilderVersionType.v6_0_0;
				}
				else if ( publicId.contains( "6.1.0" ) || systemId.contains( "6.1.0" ) )
				{
					dtdVersion = ServiceBuilderVersionType.v6_1_0;
				}
			}

		}

		return dtdVersion;
	}

	@Override
	protected IModelElement createModel()
	{
		IFile editorFile = getFile();
		ServiceBuilderVersionType dtdVersion = null;
		RootXmlResource resource = null;

		try
		{
			resource = new RootXmlResource( new XmlResourceStore( editorFile.getContents() ) );
			Document document = resource.getDomDocument();
			dtdVersion = getDTDVersion( document );

			if ( document != null )
			{
				switch ( dtdVersion )
				{

				case v6_0_0:
					setRootModelElementType( IServiceBuilder600.TYPE );
					break;

				case v6_1_0:
				default:
					setRootModelElementType( IServiceBuilder610.TYPE );
					break;

				}
			}
		}
		catch ( Exception e )
		{
			ServiceUI.logError( e );
			setRootModelElementType( IServiceBuilder600.TYPE );
		}
		finally
		{
			if ( resource != null )
			{
				resource.dispose();
			}
		}

		return super.createModel();
	}

	@Override
	protected void createDiagramPages() throws PartInitException
	{
		IPath path =
			new Path( ServiceUI.PLUGIN_ID + "/com/liferay/ide/eclipse/service/ui/ServiceBuilder.sdef/diagramPage" );
		this.pageDiagram = new SapphireDiagramEditor( this.getModelElement(), path );
		addPage( 0, this.pageDiagram, getEditorInput() );
		setPageText( 0, "Diagram" );
		setPageId( this.pages.get( 0 ), "Diagram", this.pageDiagram.getPart() );
	}

	@Override
	protected void createFormPages() throws PartInitException {
		super.createFormPages();

		setPageText( 0, "Overview" );
	}
}
