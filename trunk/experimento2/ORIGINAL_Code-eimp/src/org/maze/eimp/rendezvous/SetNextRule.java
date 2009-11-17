package org.maze.eimp.rendezvous;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Digester rule similar to the SetNextRule delivered with Digester, but the object binding
 * is performed on the beginning XML tag.
 * 
 * @author Ringo De Smet
 */
class SetNextRule extends Rule {

	private String methodName;
	private String paramType;

	/**
	 * Construct a "set next" rule with the specified method name. The method's
	 * argument type is assumed to be the class of the child object.
	 * 
	 * @param methodName
	 *            Method name of the parent method to call
	 */
	public SetNextRule(String methodName) {
		this(methodName, null);
	}

	/**
	 * Construct a "set next" rule with the specified method name.
	 * 
	 * @param methodName
	 *            Method name of the parent method to call
	 * @param paramType
	 *            Java class of the parent method's argument (if you wish to
	 *            use a primitive type, specify the corresonding Java wrapper
	 *            class instead, such as <code>java.lang.Boolean</code> for a
	 *            <code>boolean</code> parameter)
	 */
	public SetNextRule(String methodName, String paramType) {
		this.methodName = methodName;
		this.paramType = paramType;
	}

	public void begin(String arg0, String arg1, Attributes arg2)
		throws Exception {
		digester.getLogger().debug("begin(String, String, Attributes)");
		// Identify the objects to be used
		Object child = digester.peek(0);
		Object parent = digester.peek(1);
		if (digester.getLogger().isDebugEnabled()) {
			if (parent == null) {
				digester.getLogger().debug(
					"[SetNextRule]{"
						+ this.getDigester().getMatch()
						+ "} Call [NULL PARENT]."
						+ methodName
						+ "("
						+ child
						+ ")");
			} else {
				digester.getLogger().debug(
					"[SetNextRule]{"
						+ digester.getMatch()
						+ "} Call "
						+ parent.getClass().getName()
						+ "."
						+ methodName
						+ "("
						+ child
						+ ")");
			}
		}

		// Call the specified method
		Class paramTypes[] = new Class[1];
		if (paramType != null) {
			paramTypes[0] = digester.getClassLoader().loadClass(paramType);
		} else {
			paramTypes[0] = child.getClass();
		}

		MethodUtils.invokeMethod(
			parent,
			methodName,
			new Object[] { child },
			paramTypes);

	}

	/**
	 * Render a printable version of this Rule.
	 */
	public String toString() {

		StringBuffer sb = new StringBuffer("SetNextRule[");
		sb.append("methodName=");
		sb.append(methodName);
		sb.append(", paramType=");
		sb.append(paramType);
		sb.append("]");
		return (sb.toString());

	}

}
