package com.cytoscape.CytoscapeLiteratureNetwork.internal;

import java.util.Properties;

import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.EdgeViewTaskFactory;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.ShowPanelTaskFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.ShowSentenceFactory;

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
		{
			ShowSentenceFactory ssf = new ShowSentenceFactory(serviceRegistrar);
			Properties p =new Properties();
			p.setProperty(ServiceProperties.PREFERRED_MENU, "Apps");
			p.setProperty(ServiceProperties.IN_CONTEXT_MENU, "true");
			p.setProperty(ServiceProperties.TITLE, "Show sentences");
			registerService(context,ssf,EdgeViewTaskFactory.class,p);
		}
	}

}
