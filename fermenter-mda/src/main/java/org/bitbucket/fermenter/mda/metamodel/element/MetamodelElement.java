package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.Objects;

import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * Implements the contract for a basic metamodel element.
 */
public abstract class MetamodelElement implements Metamodel {

    protected static MessageTracker messageTracker = MessageTracker.getInstance();

    @JsonProperty(required = true)
    protected String name;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the metadata element. param name element name.
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        boolean areEqual;
        if (obj == null) {
            areEqual = false;
            
        } else if (obj == this) {
            areEqual = true;
            
        } else if (obj.getClass() != getClass()) {
            areEqual = false;
            
        } else {
            Metamodel other = (Metamodel) obj;
            areEqual = Objects.equals(this.name, other.getName());
            
        }
        
        return areEqual;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).toString();
    }

}
