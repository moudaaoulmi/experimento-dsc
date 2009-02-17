package lib.util;

public interface LocalIterator extends IteratorDsk {

	public void close();

	public boolean hasNext();

	public Object next();

	public void remove();
}
