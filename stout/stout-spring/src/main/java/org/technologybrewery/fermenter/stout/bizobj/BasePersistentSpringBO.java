package org.technologybrewery.fermenter.stout.bizobj;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import org.technologybrewery.fermenter.stout.messages.Message;
import org.technologybrewery.fermenter.stout.messages.MessageManager;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides a base implementation of a business object that delegates to Spring Data JPA for persistence and JSR-303 for
 * object validation.
 *
 * @param <PK>
 * @param <BO>
 */
public abstract class BasePersistentSpringBO<PK extends Serializable, BO, JPA extends JpaRepository<BO, PK>>
        extends BaseSpringBO<BO> implements PersistentBusinessObject<PK, BO> {

    /**
     * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
     * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null PK value.
     */
    protected final UUID internalTransientID = UUID.randomUUID();

    /**
     * {@inheritDoc}
     */
    public final BO save() {
        setDefaults();
        preValidate();
        validate();
        if (!MessageManager.hasErrorMessages()) {
            postValidate();
            BO persistedBizObj = getRepository().save((BO) this);
            postSave();
            return persistedBizObj;
        } else {
        	if (getLogger().isWarnEnabled()) {
                getLogger().warn("Attempt to save BO of type [{}] with PK = [{}] was ignored due to collected errors", this.getClass(), this.getKey());
            }
            if (getLogger().isInfoEnabled()) {
                Collection<Message> messages = MessageManager.getMessages().getErrors();
                for (Message message : messages) {
                    String displayText = message.getDisplayText();
                    getLogger().info("Encountered the following error when trying to save persistent object: {}\n\t{}", this, displayText);
                }

            }

            return (BO) this;
        }

    }
    
    protected final void setDefaults() {
        defaultFieldValues();
        defaultComplexValues();
    }
    
    /**
     * Lifecycle method that is invoked before this business object's pre-save validation occurs to support
     * defaulting of field values that have been set in the entity metamodel.
     */
    protected void defaultFieldValues() {

    }
    

    /**
     * Lifecycle method that is invoked before this business object's pre-save validation occurs to support
     * defaulting. This should be overridden by the developer in cases when the defaults cannot be defined
     * in the entity metamodel and more complex values are necessary.
     */
    protected void defaultComplexValues() {

    }

    /**
     * {@inheritDoc}
     */
    public void delete() {
        getRepository().delete((BO) this);
    }

    protected abstract JPA getRepository();

    /**
     * Lifecycle method that is invoked after this business object has been persisted. Developers may override this
     * method in concrete business implementations to do things like broadcasting a notification after an object has
     * been successfully saved.
     */
    protected void postSave() {

    }

    @Override
    public boolean equals(Object o) {
        boolean areEqual = false;

        try {
            BasePersistentSpringBO thatBizObj = (BasePersistentSpringBO) o;

            PK thisPk = getKey();
            PK thatPk = (PK) (thatBizObj != null ? thatBizObj.getKey() : null);
            if (thatBizObj != null && thisPk == null && thatPk == null) {
                return this.internalTransientID == thatBizObj.internalTransientID;
            } else if (thisPk == thatPk || (thisPk.equals(thatPk))) {
                areEqual = true;
            }
        } catch (ClassCastException ex) {
            areEqual = false;
        }

        return areEqual;
    }

    @Override
    public int hashCode() {
        return (getKey() == null) ? internalTransientID.hashCode() : getKey().hashCode();
    }
}
