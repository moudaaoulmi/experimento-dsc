package lib.distribution.rmi;

/**
 * Defines the serializable interface for library classes
 */
public aspect Serializer {

    declare parents : lib.util.ConcreteIterator ||
    				  lib.util.Date
    				  implements java.io.Serializable;
}
