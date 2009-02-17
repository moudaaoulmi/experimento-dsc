package lib.util;

import lib.exceptions.CommunicationException;

public interface IteratorDsk {

	public void close() throws CommunicationException;

	public boolean hasNext() throws CommunicationException;

	public Object next() throws CommunicationException;

	public void remove() throws CommunicationException;
}
