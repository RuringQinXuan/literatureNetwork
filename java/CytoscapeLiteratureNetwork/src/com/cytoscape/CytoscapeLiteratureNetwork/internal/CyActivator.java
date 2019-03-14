package com.cytoscape.CytoscapeLiteratureNetwork.internal;

import java.util.Properties;

import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.ShowPanelTaskFactory;

public class CyActivator extends AbstractCyActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("\n\nStarting Cytoscape Literature Network\n");
		CyServiceRegistrar serviceRegistrar = getService(context, CyServiceRegistrar.class);
		// TODO Auto-generated method stub
		ShowPanelTaskFactory spt=new ShowPanelTaskFactory(serviceRegistrar);
		Properties p =new Properties();
		p.setProperty(ServiceProperties.PREFERRED_MENU, "Apps");
		p.setProperty(ServiceProperties.TITLE, "Literature Network");
		registerService(context,spt,TaskFactory.class,p);
	}

}
