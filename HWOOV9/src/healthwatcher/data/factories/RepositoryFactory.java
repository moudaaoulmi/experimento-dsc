package healthwatcher.data.factories;

import healthwatcher.Constants;
import healthwatcher.business.HealthWatcherFacade;

/**
 * This class provides the factory according to the system's configuration
 */
public class RepositoryFactory {

	/**
	 * The repository factory singleton instance
	 */
	protected static AbstractRepositoryFactory instance = null;
	
	/**
	 * Gets the repository factory configured for the Health Watcher system,
	 * creating the instance if needed
	 * 
	 * @return the configured repository factory
	 */
	public static AbstractRepositoryFactory getRepositoryFactory() {
		if (instance == null) {
			if (Constants.isPersistent()) {
				instance = new RDBRepositoryFactory(HealthWatcherFacade.getPm());
			} else {
				instance = new ArrayRepositoryFactory();
			}
		}
		return instance;
	}
}
