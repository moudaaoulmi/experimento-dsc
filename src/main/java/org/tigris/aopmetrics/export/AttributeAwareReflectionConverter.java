/*
 * Created on Mar 21, 2005
 *
 */
package org.tigris.aopmetrics.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.AbstractBasicConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SerializationMethodInvoker;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;


/**
 * Changes XStream default behaviour and saves primitive values and strings
 * as a attributes. 
 * 
 * <p>See: http://jira.codehaus.org/browse/XSTR-62
 */
public class AttributeAwareReflectionConverter implements Converter {

    private final Mapper mapper;
    private final ReflectionProvider reflectionProvider;
    private final SerializationMethodInvoker serializationMethodInvoker;
    private String classAttributeIdentifier;
    private ConverterLookup lookup;

    public AttributeAwareReflectionConverter(ClassMapper classMapper, String classAttributeIdentifier,
            ReflectionProvider reflectionProvider,ConverterLookup lookup) {
        this.mapper = classMapper;
        this.classAttributeIdentifier = classAttributeIdentifier;
        this.reflectionProvider = reflectionProvider;
        this.lookup = lookup;
        serializationMethodInvoker = new SerializationMethodInvoker();
    }

    /**
     * @param xstream
     */
    public AttributeAwareReflectionConverter(XStream xstream) {
        this(xstream.getClassMapper(), "class-name", new JVM().bestReflectionProvider(),xstream.getConverterLookup());
    }

    public boolean canConvert(Class cls) {
    	if (!cls.isPrimitive() && !cls.isArray() && !cls.isInterface()) {
    	    return !cls.getName().startsWith("java.");
        	}
            return false;
    }

    public void marshal(Object original, final HierarchicalStreamWriter writer, final MarshallingContext context) {
        final Object source = serializationMethodInvoker.callWriteReplace(original);

        if (source.getClass() != original.getClass()) {
            writer.addAttribute(mapper.attributeForReadResolveField(), mapper.serializedClass(source.getClass()));
        }

        final Set attributedFields = new HashSet();

        reflectionProvider.visitSerializableFields(source, new ReflectionProvider.Visitor() {

            public void visit(final String fieldName, Class fieldType, Class definedIn, Object value) {
                
                if (value != null) {
                    Converter converter = lookup.lookupConverterForType(fieldType);
                    if (AbstractBasicConverter.class.isAssignableFrom(converter.getClass())) {
                        converter.marshal(value, new HierarchicalStreamWriter(){
                            public void startNode(String name) {
                            }
                            public void addAttribute(String name, String value) {
                            }
                            public void setValue(String text) {
                                writer.addAttribute(fieldName, text);
                            }
                            public void endNode() {
                            }
                            public void flush() {
                            }
                            public void close() {
                            }
                            public HierarchicalStreamWriter underlyingWriter() {
                                return null;
                            }}, context);
                        attributedFields.add(fieldName);
                    }
                }
            }
        });
        final Set seenFields = new HashSet();

        reflectionProvider.visitSerializableFields(source, new ReflectionProvider.Visitor() {
            public void visit(String fieldName, Class fieldType, Class definedIn, Object newObj) {
                if (newObj != null && !attributedFields.contains(fieldName)) {
                    Mapper.ImplicitCollectionMapping mapping = mapper.getImplicitCollectionDefForFieldName(source.getClass(), fieldName);
                    if (mapping != null) {
                        if (mapping.getItemFieldName() != null) {
                            ArrayList list = (ArrayList) newObj;
                            for (Iterator iter = list.iterator(); iter.hasNext();) {
                                Object obj = iter.next();
                                writeField(mapping.getItemFieldName(), mapping.getItemType(), definedIn, obj);
                            }
                        } else {
                            context.convertAnother(newObj);
                        }
                    } else {
                        writeField(fieldName, fieldType, definedIn, newObj);
                        seenFields.add(fieldName);
                    }
                }
            }

            private void writeField(String fieldName, Class fieldType, Class definedIn, Object newObj) {
                writer.startNode(mapper.serializedMember(definedIn, fieldName));

                Class actualType = newObj.getClass();

                Class defaultType = mapper.defaultImplementationOf(fieldType);
                if (!actualType.equals(defaultType)) {
                    writer.addAttribute(mapper.attributeForImplementationClass(), mapper.serializedClass(actualType));
                }

                if (seenFields.contains(fieldName)) {
                    writer.addAttribute(mapper.attributeForClassDefiningField(), mapper.serializedClass(definedIn));
                }
                context.convertAnother(newObj);

                writer.endNode();
            }

        });
    }

    public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
        final Object result = instantiateNewInstance(context, reader.getAttribute(mapper.attributeForReadResolveField()));
        final SeenFields seenFields = new SeenFields();

        Map implicitCollectionsForCurrentObject = null;
        
        readAttributeBasedFields(result, reader);
        
        while (reader.hasMoreChildren()) {
            reader.moveDown();

            String fieldName = mapper.realMember(result.getClass(), reader.getNodeName());

            Class classDefiningField = determineWhichClassDefinesField(reader);
            boolean fieldExistsInClass = reflectionProvider.fieldDefinedInClass(fieldName, result.getClass());

            Class type = determineType(reader, fieldExistsInClass, result, fieldName, classDefiningField);
            Object value = context.convertAnother(result, type);

            if (fieldExistsInClass) {
                reflectionProvider.writeField(result, fieldName, value, classDefiningField);
                seenFields.add(classDefiningField, fieldName);
            } else {
                implicitCollectionsForCurrentObject = writeValueToImplicitCollection(context, value, implicitCollectionsForCurrentObject, result, fieldName);
            }

            reader.moveUp();
        }

        return serializationMethodInvoker.callReadResolve(result);
    }


    /**
     * 
     */
    private void readAttributeBasedFields(final Object finalResult, final HierarchicalStreamReader reader) {
        reflectionProvider.visitSerializableFields(finalResult, new ReflectionProvider.Visitor() {

            /* (non-Javadoc)
             * @see com.thoughtworks.xstream.converters.reflection.ReflectionProvider.Visitor#visit(java.lang.String, java.lang.Class, java.lang.Class, java.lang.Object)
             */
            public void visit(String fieldName, Class fieldType, Class definedIn, Object value) {
                Converter converter = lookup.lookupConverterForType(fieldType);
                if (AbstractBasicConverter.class.isAssignableFrom(converter.getClass())) {
                    final String fieldAttr = reader.getAttribute(fieldName);
                    if (fieldAttr!=null) {


	                    Object fieldValue = converter.unmarshal(new HierarchicalStreamReader() {
	                        /* (non-Javadoc)
	                         * @see com.thoughtworks.xstream.io.HierarchicalStreamReader#getValue()
	                         */
	                        public String getValue() {
	                            return fieldAttr;
	                        }
	
	                        public boolean hasMoreChildren() {
	                            return false;
	                        }
	
	                        public void moveDown() {
	                            
	                        }
	
	                        public void moveUp() {
	                            
	                        }
	
	                        public String getNodeName() {
	                            return null;
	                        }
	
	                        public String getAttribute(String arg0) {
	                            return null;
	                        }
	
	                        public Object peekUnderlyingNode() {
	                            return null;
	                        }

                            public void appendErrors(ErrorWriter errorWriter) {
                                // TODO Auto-generated method stub
                                
                            }

                            public void close() {
                                // TODO Auto-generated method stub
                                
                            }

                            public HierarchicalStreamReader underlyingReader() {
                                // TODO Auto-generated method stub
                                return null;
                            }
	                    }, null);
	                    reflectionProvider.writeField(finalResult, fieldName, fieldValue, definedIn);
	                    	//writeField(finalResult, fieldName, fieldValue);
                    }
                }
            }
        });
    }

    private Map writeValueToImplicitCollection(UnmarshallingContext context, Object value, Map implicitCollections, Object result, String itemFieldName) {
        String fieldName = mapper.getFieldNameForItemTypeAndName(context.getRequiredType(), value.getClass(), itemFieldName);
        if (fieldName != null) {
            if (implicitCollections == null) {
                implicitCollections = new HashMap(); // lazy instantiation
            }
            Collection collection = (Collection) implicitCollections.get(fieldName);
            if (collection == null) {
                collection = new ArrayList();
                reflectionProvider.writeField(result, fieldName, collection, null);
                implicitCollections.put(fieldName, collection);
            }
            collection.add(value);
        }
        return implicitCollections;
    }

    private Class determineWhichClassDefinesField(HierarchicalStreamReader reader) {
        String definedIn = reader.getAttribute(mapper.attributeForClassDefiningField());
        return definedIn == null ? null : mapper.realClass(definedIn);
    }

    private Object instantiateNewInstance(UnmarshallingContext context, String readResolveValue) {
        Object currentObject = context.currentObject();
        if (currentObject != null) {
            return currentObject;
        } else if (readResolveValue != null) {
            return reflectionProvider.newInstance(mapper.realClass(readResolveValue));
        } else {
            return reflectionProvider.newInstance(context.getRequiredType());
        }
    }

    private static class SeenFields {

        private Set seen = new HashSet();

        public void add(Class definedInCls, String fieldName) {
            String uniqueKey = fieldName;
            if (definedInCls != null) {
                uniqueKey += " [" + definedInCls.getName() + "]";
            }
            if (seen.contains(uniqueKey)) {
                throw new DuplicateFieldException(uniqueKey);
            } else {
                seen.add(uniqueKey);
            }
        }

    }

    private Class determineType(HierarchicalStreamReader reader, boolean validField, Object result, String fieldName, Class definedInCls) {
        String classAttribute = reader.getAttribute(mapper.attributeForImplementationClass());
        if (classAttribute != null) {
            return mapper.realClass(classAttribute);
        } else if (!validField) {
            Class itemType = mapper.getItemTypeForItemFieldName(result.getClass(), fieldName);
            if (itemType != null) {
                return itemType;
            } else {
                return mapper.realClass(reader.getNodeName());
            }
        } else {
            return mapper.defaultImplementationOf(reflectionProvider.getFieldType(result, fieldName, definedInCls));
        }
    }

    public static class DuplicateFieldException extends ConversionException {
        public DuplicateFieldException(String msg) {
            super(msg);
        }
    }
}
