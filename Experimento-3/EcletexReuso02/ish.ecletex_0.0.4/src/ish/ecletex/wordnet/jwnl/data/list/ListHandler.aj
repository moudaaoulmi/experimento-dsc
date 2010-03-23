package ish.ecletex.wordnet.jwnl.data.list;

import java.util.List;

public privileged aspect ListHandler {

	pointcut reverseHandler(): execution(public PointerTargetNodeList PointerTargetNodeList.reverse());

	pointcut toListHandler(): execution(public List PointerTargetTreeNode.toList(PointerTargetNodeList));

	declare soft: CloneNotSupportedException : reverseHandler() || toListHandler();

	Object around(): reverseHandler() || toListHandler() {
		try {
			return proceed();
		} catch (CloneNotSupportedException ex) {
			throw new UnsupportedOperationException();
		}
	}

}
