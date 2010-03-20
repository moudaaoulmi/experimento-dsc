package net.sourceforge.texlipse.editor.partitioner;

import net.sourceforge.texlipse.TexlipsePlugin;

import org.eclipse.jface.text.BadLocationException;

import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

public privileged aspect PartitionerHandler extends EmptyBlockAbstractExceptionHandling{

	pointcut internalUpdateBufferHandler(): execution(private void BufferedDocumentScanner.internalUpdateBuffer());

	pointcut internalReadHandler(): execution(private int BufferedDocumentScanner.internalRead());

	pointcut getColumnHandler(): execution(public final int BufferedDocumentScanner.getColumn());
	
	public pointcut emptyBlockException():  internalUpdateBufferHandler();

	declare soft: BadLocationException: internalUpdateBufferHandler()||getColumnHandler();

	int around(): getColumnHandler() {
		try {
			return proceed();
		} catch (BadLocationException e) {
		}
		return -1;
	}

	int around(BufferedDocumentScanner buff): internalReadHandler()&&this(buff) {
		try {
			return proceed(buff);
		} catch (ArrayIndexOutOfBoundsException ex) {
			StringBuffer buf = new StringBuffer();
			buf.append("Detailed state of 'BufferedDocumentScanner:'"); //$NON-NLS-1$
			buf.append("\n\tfOffset= "); //$NON-NLS-1$
			buf.append(buff.fOffset);
			buf.append("\n\tfBufferOffset= "); //$NON-NLS-1$
			buf.append(buff.fBufferOffset);
			buf.append("\n\tfBufferLength= "); //$NON-NLS-1$
			buf.append(buff.fBufferLength);
			buf.append("\n\tfRangeOffset= "); //$NON-NLS-1$
			buf.append(buff.fRangeOffset);
			buf.append("\n\tfRangeLength= "); //$NON-NLS-1$
			buf.append(buff.fRangeLength);
			TexlipsePlugin.log(buf.toString(), ex);
			throw ex;
		}
	}

//	void around(): internalUpdateBufferHandler() {
//		try {
//			proceed();
//		} catch (BadLocationException e) {
//		}
//	}
}
