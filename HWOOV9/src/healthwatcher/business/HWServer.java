package healthwatcher.business;

import healthwatcher.business.factories.FacadeFactory;

/**
 * TODO - describe this file
 * 
 */
public class HWServer {

	public static void main(String[] args) {
		try {
			FacadeFactory.getRepositoryFactory().createServerFacade();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
