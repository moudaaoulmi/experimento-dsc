package healthwatcher.aspects.concurrency;

import healthwatcher.data.mem.ComplaintRepositoryArray;
import healthwatcher.data.mem.EmployeeRepositoryArray;

/**
 * This aspect synchronizes execution of all methods of classes declared synchronized 
 * classes (in this case, EmployeeRepositoryArray and ComplaintRepositoryArray)
 */
public aspect HWLocalSynchronization {

	private interface SynchronizedClasses {};

	declare parents: EmployeeRepositoryArray || 
					 ComplaintRepositoryArray 
					 implements SynchronizedClasses;
	
	Object around(Object o): this(o) && execution(* SynchronizedClasses+.*(..)) {
		synchronized(o) {
			return proceed(o);			
		}
	}
}
