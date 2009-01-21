//============================================================================
//
// Copyright (C) 2002-2007  David Schneider, Lars K�dderitzsch
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//============================================================================

package com.atlassw.tools.eclipse.checkstyle.config;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a configuration property who's value must be resolved.
 * 
 * @author David Schneider
 * @author Lars K�dderitzsch
 */
public class ResolvableProperty implements Cloneable
{
    // =================================================
    // Public static final variables.
    // =================================================

    // =================================================
    // Static class variables.
    // =================================================

    // =================================================
    // Instance member variables.
    // =================================================

    /** The name of the property. */
    private String mPropertyName;

    /** The property value. */
    private String mValue;

    // =================================================
    // Constructors & finalizer.
    // =================================================

    /**
     * Creates a resolvable property.
     * 
     * @param propertyName the name of the property
     * @param value the value of the property
     */
    public ResolvableProperty(String propertyName, String value)
    {
        setPropertyName(propertyName);
        setValue(value);
    }

    // =================================================
    // Methods.
    // =================================================

    /**
     * @return The value of the property.
     */
    public String getValue()
    {
        return mValue;
    }

    /**
     * @return The property's name.
     */
    public String getPropertyName()
    {
        return mPropertyName;
    }

    /**
     * @param string Value for the property.
     */
    public void setValue(String string)
    {
        mValue = string;
    }

    /**
     * @param string The property's name.
     */
    public void setPropertyName(String string)
    {
        mPropertyName = string;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof ResolvableProperty))
        {
            return false;
        }
        if (this == obj)
        {
            return true;
        }
        ResolvableProperty rhs = (ResolvableProperty) obj;
        return new EqualsBuilder().append(mPropertyName, rhs.mPropertyName).append(mValue,
                rhs.mValue).isEquals();
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return new HashCodeBuilder(32234343, 1000003).append(mPropertyName).append(mValue)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    public Object clone()
    {
        try
        {
            ResolvableProperty clone = (ResolvableProperty) super.clone();
            return clone;
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(); // should never happen
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
