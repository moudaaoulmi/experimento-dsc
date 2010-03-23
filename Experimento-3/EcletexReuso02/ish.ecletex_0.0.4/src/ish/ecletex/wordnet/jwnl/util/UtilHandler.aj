package ish.ecletex.wordnet.jwnl.util;

import java.util.Collection;
import java.util.List;

public privileged aspect UtilHandler {

	pointcut copyBackingListHandler() : execution(protected List TypeCheckingList.copyBackingList());

	pointcut containsAllHandler() : execution(public boolean TypeCheckingList.containsAll(Collection));

	pointcut containsHandler() : execution(public boolean TypeCheckingList.contains(Object));

	pointcut removeHandler() : execution(public boolean TypeCheckingList.remove(Object));

	pointcut lastIndexOfHandler() : execution(public int TypeCheckingList.lastIndexOf(Object));

	pointcut indexOfHandler() : execution(public int TypeCheckingList.indexOf(Object));

	declare soft: Exception: copyBackingListHandler() || containsAllHandler() || containsHandler() ||removeHandler() ||lastIndexOfHandler() || indexOfHandler();

	int around(): indexOfHandler() || lastIndexOfHandler() {
		try {
			return proceed();
		} catch (Exception ex) {
			return -1;
		}
	}

	boolean around(): removeHandler() || containsHandler() || containsAllHandler() {
		try {
			return proceed();
		} catch (Exception ex) {
			return false;
		}
	}	

	List around() throws CloneNotSupportedException : copyBackingListHandler()  {
		try {
			return proceed();
		} catch (Exception ex) {
			throw new CloneNotSupportedException();
		}
	}

}
