package org.technologybrewery.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.technologybrewery.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ServiceEntityExample entity.
 * @see org.technologybrewery.fermenter.cookbook.domain.bizobj.ServiceEntityExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 *
 * Originally generated from templates/bo.java.vm
 */
@Entity
@Table(name="SERVICE_ENTITY_EXAMPLE")
public class ServiceEntityExampleBO extends ServiceEntityExampleBaseBO {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceEntityExampleBO.class);

	public ServiceEntityExampleBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
    
    /**
    * Lifecycle method that is invoked when saving ServiceEntityExample via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If ServiceEntityExample requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        logger.debug("\t\tServiceEntityExampleBO: complexValidation() - START");
        logger.debug("\t\tServiceEntityExampleBO: complexValidation() - END");
	}

    @Override
    protected void validateRelations() {
        logger.debug("\tServiceEntityExampleBO: validateRelations() - START");
        super.validateRelations();
        logger.debug("\tServiceEntityExampleBO: validateRelations() - END");
    }

    @Override
    protected void defaultFieldValues() {
        logger.debug("\tServiceEntityExampleBO: defaultFieldValues() - START");
        super.defaultFieldValues();
        logger.debug("\tServiceEntityExampleBO: defaultFieldValues() - END");

    }

    @Override
    protected void defaultComplexValues() {
        logger.debug("\tServiceEntityExampleBO: defaultComplexValues() - START");
        super.defaultComplexValues();
        logger.debug("\tServiceEntityExampleBO: defaultComplexValues() - END");

    }

    @Override
    protected void postSave() {
        logger.debug("\t\t\tServiceEntityExampleBO: postSave() - START");
        super.postSave();
        logger.debug("\t\t\tServiceEntityExampleBO: postSave() - END");
    }

    @Override
    public void validate() {
        logger.debug("\t\tServiceEntityExampleBO: validate() - START");
        super.validate();
        logger.debug("\t\tServiceEntityExampleBO: validate() - END");
    }

    @Override
    protected void preValidate() {
        logger.debug("\tServiceEntityExampleBO: preValidate() - START");
        super.preValidate();
        logger.debug("\tServiceEntityExampleBO: preValidate() - END");
    }

    @Override
    protected void postValidate() {
        logger.debug("\t\tServiceEntityExampleBO: postValidate() - START");
        super.postValidate();
        logger.debug("\t\tServiceEntityExampleBO: postValidate() - END");
    }

    @Override
    public void validateFields() {
        logger.debug("\t\tServiceEntityExampleBO: validateFields() - START");
        super.validateFields();
        logger.debug("\t\tServiceEntityExampleBO: validateFields() - END");
    }

    @Override
    protected void complexValidationOnRelations() {
        logger.debug("\tServiceEntityExampleBO: complexValidationOnRelations() - START");
        super.complexValidationOnRelations();
        logger.debug("\tServiceEntityExampleBO: complexValidationOnRelations() - END");
    }

}
