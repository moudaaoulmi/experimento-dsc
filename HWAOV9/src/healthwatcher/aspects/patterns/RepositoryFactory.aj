package healthwatcher.aspects.patterns;

import healthwatcher.Constants;
import healthwatcher.data.factories.AbstractRepositoryFactory;
import healthwatcher.data.factories.ArrayRepositoryFactory;
import healthwatcher.data.factories.RDBRepositoryFactory;

import healthwatcher.aspects.persistence.*;

/**
 * This aspect provides the factory according to the system's configuration
 */
public aspect RepositoryFactory {

	/**
	 * The repository factory singleton instance
	 */
	private static AbstractRepositoryFactory AbstractRepositoryFactory.instance = null;
	
	/**
	 * Gets the repository factory configured for the Health Watcher system,
	 * creating the instance if needed
	 * 
	 * @return the configured repository factory
	 */
	public static AbstractRepositoryFactory AbstractRepositoryFactory.getRepositoryFactory() {
		if (instance == null) {
			if (Constants.isPersistent()) {
				instance = new RDBRepositoryFactory(HWPersistence.aspectOf().getPm());
			} else {
				instance = new ArrayRepositoryFactory();
			}
		}
		return instance;
	}	
}
