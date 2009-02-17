package healthwatcher.aspects.persistence;

import healthwatcher.Constants;
import healthwatcher.business.HealthWatcherFacade;
import lib.exceptions.PersistenceMechanismException;
import lib.persistence.IPersistenceMechanism;
import lib.persistence.PersistenceMechanism;

/**
 * This aspect initializes persistence when the system initilizes (if the system is to be
 * persistent). It also provides a method for other aspects to get the current persistence
 * mechanism
 */
public aspect HWPersistence {

	private IPersistenceMechanism pm = null;
	
	private boolean pmCreated = false;
	
	public IPersistenceMechanism getPm() {
		if (!pmCreated) {
			pm = pmInit();
			if (pm != null) {
				pmCreated = true;
			}
		}
		return pm;
	}
	
	IPersistenceMechanism pmInit() {
		IPersistenceMechanism returnValue = null;
		if (isPersistent()) {
			try {
				returnValue = PersistenceMechanism.getInstance();
				// Persistence mechanism connection
				returnValue.connect();
			} catch (PersistenceMechanismException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// Persistence mechanism desconnection for resource release
				try {
					if (getPm() != null) {
						getPm().disconnect();
					}
				} catch (PersistenceMechanismException mpe) {
					mpe.printStackTrace();
				}
			}
		}
		return returnValue;
	}
	
	boolean isPersistent() {
		return Constants.isPersistent();
	}
	
	/**
	 * Before initializing a facade, init the pm
	 */
	before() : call(HealthWatcherFacade.new(..)) {
		getPm();
	}
}
