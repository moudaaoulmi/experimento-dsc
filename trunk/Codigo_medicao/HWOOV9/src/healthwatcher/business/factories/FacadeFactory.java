package healthwatcher.business.factories;


/**
 * This class provides the factory according to the system's configuration
 */
public class FacadeFactory {

	/**
	 * The repository factory singleton instance
	 */
	protected static AbstractFacadeFactory instance = null;
	
	/**
	 * Gets the facade factory configured for the Health Watcher system,
	 * creating the instance if needed.
	 * 
	 * @return the configured repository factory
	 */
	public static AbstractFacadeFactory getRepositoryFactory() {
		if (instance == null) {
			instance = new RMIFacadeFactory();
		}
		return instance;
	}
}
