package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Provides baseline decorator functionality for {@link Return}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorate does only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorate requires by default.
 */
public class BaseReturnDecorator implements Return {

    protected Return wrapped;

    /**
     * New decorator for {@link Return}.
     * 
     * @param returnToDecorate
     *            instance to decorate
     */
    public BaseReturnDecorator(Return returnToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), returnToDecorate);
        wrapped = returnToDecorate;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return wrapped.getName();
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPackage() {
        return wrapped.getPackage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        wrapped.validate();
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return wrapped.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isMany() {
        return wrapped.isMany();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseEncoding() {
        return wrapped.getResponseEncoding();
    }  

}