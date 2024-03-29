package com.cytoscape.CytoscapeLiteratureNetwork.internal;

import java.util.Properties;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelComponent2;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.ShowPanelTaskFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.LiteratureNetworkCytoPanel;

public class CyActivator extends AbstractCyActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("\n\nStarting Cytoscape Literature Network\n");
		CyServiceRegistrar serviceRegistrar = getService(context, CyServiceRegistrar.class);

		{
			ShowPanelTaskFactory spt=new ShowPanelTaskFactory(serviceRegistrar);
			Properties p =new Properties();
			p.setProperty(ServiceProperties.PREFERRED_MENU, "Apps");
			p.setProperty(ServiceProperties.TITLE, "Literature Network");
			registerService(context,spt,TaskFactory.class,p);
		}
		
		// TODO Identify if a LiteratureNetwork is indeed there
		{
			CySwingApplication swingApplication = getService(context, CySwingApplication.class);
			CytoPanel cytoPanel = swingApplication.getCytoPanel(CytoPanelName.EAST);

			// If the panel is not already registered, create it
			if (cytoPanel.indexOfComponent(LiteratureNetworkCytoPanel.IDENTIFIER) < 0) {
				CytoPanelComponent2 panel = new LiteratureNetworkCytoPanel(serviceRegistrar);

				// Register it
				serviceRegistrar.registerService(panel, CytoPanelComponent.class, new Properties());
				serviceRegistrar.registerService(panel, RowsSetListener.class, new Properties());

				if (cytoPanel.getState() == CytoPanelState.HIDE)
					cytoPanel.setState(CytoPanelState.DOCK);
			}
		}
	}

}
