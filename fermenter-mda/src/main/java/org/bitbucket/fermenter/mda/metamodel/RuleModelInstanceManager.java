package org.bitbucket.fermenter.mda.metamodel;

import org.bitbucket.fermenter.mda.metamodel.element.Rule;
import org.bitbucket.fermenter.mda.metamodel.element.RuleElement;

/**
 * Responsible for maintaining the list of rule model instances elements in the system.
 */
class RuleModelInstanceManager extends AbstractMetamodelManager<Rule> {

    private static final RuleModelInstanceManager instance = new RuleModelInstanceManager();

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static RuleModelInstanceManager getInstance() {
        return instance;
    }

    /**
     * Prevent instantiation of this singleton from outside this class.
     */
    private RuleModelInstanceManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getRulesRelativePath();
    }

    @Override
    protected Class<RuleElement> getMetamodelClass() {
        return RuleElement.class;
    }

    @Override
    protected String getMetamodelDescription() {
        return Rule.class.getSimpleName();
    }

}
