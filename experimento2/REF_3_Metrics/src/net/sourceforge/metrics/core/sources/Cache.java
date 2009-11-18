/*
 * Copyright (c) 2003 Frank Sauer. All rights reserved.
 *
 * Licenced under CPL 1.0 (Common Public License Version 1.0).
 * The licence is available at http://www.eclipse.org/legal/cpl-v10.html.
 *
 *
 * DISCLAIMER OF WARRANTIES AND LIABILITY:
 *
 * THE SOFTWARE IS PROVIDED "AS IS".  THE AUTHOR MAKES  NO REPRESENTATIONS OR WARRANTIES,
 * EITHER EXPRESS OR IMPLIED.  TO THE EXTENT NOT PROHIBITED BY LAW, IN NO EVENT WILL THE
 * AUTHOR  BE LIABLE FOR ANY DAMAGES, INCLUDING WITHOUT LIMITATION, LOST REVENUE,  PROFITS
 * OR DATA, OR FOR SPECIAL, INDIRECT, CONSEQUENTIAL, INCIDENTAL  OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF  LIABILITY, ARISING OUT OF OR RELATED TO
 * ANY FURNISHING, PRACTICING, MODIFYING OR ANY USE OF THE SOFTWARE, EVEN IF THE AUTHOR
 * HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 *
 * $id$
 */
package net.sourceforge.metrics.core.sources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.RecordManagerOptions;
import jdbm.helper.FastIterator;
import jdbm.helper.IterationException;
import jdbm.htree.HTree;
import net.sourceforge.metrics.core.Log;
import net.sourceforge.metrics.core.MetricsPlugin;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

/**
 * public API to the private database. Currently the database is a jdbm
 * persistent hashtable with MRU cache.
 * 
 * @author Frank Sauer
 */
public class Cache {

	private static final String DBNAME = "/metricsdb";
	private RecordManager recman;

	private String pluginDir;

	public final static Cache singleton = new Cache();

	// keep roots (projectName -> HTree)
	private HashMap projects = new HashMap();
	private HashMap keys = new HashMap();

	private Cache() {
		super();
		// the follwing fixes a bug submitted outside of SF by Parasoft
		pluginDir = MetricsPlugin.getDefault().getStateLocation().toString();
		// pluginDir =
		// Platform.getPlugin(Log.pluginId).getStateLocation().toString();
		initRecordManager();
	}

	private void initRecordManager() {
		Properties props = new Properties();
		props.put(RecordManagerOptions.CACHE_SIZE, "500");
		props.put(RecordManagerOptions.AUTO_COMMIT, "false");
		props.put(RecordManagerOptions.THREAD_SAFE, "true");
			recman = RecordManagerFactory.createRecordManager(pluginDir + DBNAME,
					props);
	}

	private HTree getHashtableForProject(String projectName) {
		HTree hashtable = (HTree) projects.get(projectName);
		if (hashtable == null) {
			hashtable = internalGetHashtableForProject(projectName, hashtable);
		}
		return hashtable;
	}

	private HTree internalGetHashtableForProject(String projectName,
			HTree hashtable) {
		long recid = recman.getNamedObject(projectName);
		if (recid != 0) {
			hashtable = HTree.load(recman, recid);
		} else {
			hashtable = HTree.createInstance(recman);
			recman.setNamedObject(projectName, hashtable.getRecid());
		}
		projects.put(projectName, hashtable);
		return hashtable;
	}

	private HTree getHashtableForHandle(String handle) {
		IJavaElement element = JavaCore.create(handle);
		String projectName = getProjectName(element);
		return getHashtableForProject(projectName);
	}

	/**
	 * @param element
	 * @return
	 */
	private String getProjectName(IJavaElement element) {
		if (element.getElementType() == IJavaElement.JAVA_PROJECT)
			return element.getElementName();
		else {
			IJavaElement p = element.getAncestor(IJavaElement.JAVA_PROJECT);
			return p.getElementName();
		}
	}

	public void put(AbstractMetricSource source) {
		if (source == null)
			return;
		String handle = source.getHandle();
		getHashtableForHandle(handle).put(handle, source);
		getKeysForHandle(handle).add(handle);
		if (source.getLevel() >= AbstractMetricSource.PACKAGEFRAGMENT)
			recman.commit();
	}

	/**
	 * @param handle
	 */
	public Set getKeysForHandle(String handle) {
		IJavaElement element = JavaCore.create(handle);
		String projectName = getProjectName(element);
		Set s = (Set) keys.get(projectName);
		if (s == null) {
			s = getKeys(handle);
			keys.put(projectName, s);
		}
		return s;

	}

	private Set getKeys(String handle) {
		HTree map = getHashtableForHandle(handle);
		Set result = new HashSet();
		internalGetKeys(map, result);
		return result;
	}

	private void internalGetKeys(HTree map, Set result) {
		FastIterator it = map.keys();
		String next = (String) it.next();
		while (next != null) {
			result.add(next);
			next = (String) it.next();
		}
	}

	public AbstractMetricSource get(IJavaElement element) {
		return (AbstractMetricSource) get(element.getHandleIdentifier());
	}

	public AbstractMetricSource get(String handle) {
			return (AbstractMetricSource) getHashtableForHandle(handle).get(handle);
	}

	public void remove(String handle) {
		getHashtableForHandle(handle).remove(handle);
		getKeysForHandle(handle).remove(handle);
	}

	public void removeSubtree(String handle) {
		HTree h = getHashtableForHandle(handle);
		if (h != null) {
			Set handles = getKeysForHandle(handle);
			for (Iterator i = handles.iterator(); i.hasNext();) {
				String next = (String) i.next();
				if (next.startsWith(handle))
					internalRemoveSubtree(h, i, next);
			}
		}
	}

	private void internalRemoveSubtree(HTree h, Iterator i, String next) {
		h.remove(next);
		i.remove();
	}

	public void close() {
		recman.close();
		keys.clear();
		projects.clear();
	}

	/**
	 * permanently remove all metrics related to given project
	 * 
	 * @param projectName
	 */
	public void clear(String projectName) {
		keys.remove(projectName);
		long id = recman.getNamedObject(projectName);
		if (id != 0) {
			recman.delete(id);
			HTree hashtable = HTree.createInstance(recman);
			recman.setNamedObject(projectName, hashtable.getRecid());
			recman.commit();
		}
	}

	/**
	 * clean out entire database
	 */
	public void clear() {
			recman.close();
		File db = new File(pluginDir + DBNAME);
		db.delete();
		initRecordManager();
		keys.clear();

	}

	/**
	 * 
	 */
	public void commit() {
			recman.commit();
	}
}
