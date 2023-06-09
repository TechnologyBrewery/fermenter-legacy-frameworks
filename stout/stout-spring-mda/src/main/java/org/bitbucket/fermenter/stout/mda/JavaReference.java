package org.technologybrewery.fermenter.stout.mda;

import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.fermenter.mda.PackageManager;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.BaseReferenceDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Entity;
import org.technologybrewery.fermenter.mda.metamodel.element.Field;
import org.technologybrewery.fermenter.mda.metamodel.element.Reference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Decorates a {@link Reference} with convenience methods for Java code
 * generation.
 */
public class JavaReference extends BaseReferenceDecorator implements Reference, JavaNamedElement {

	/**
	 * {@inheritDoc}
	 */
	public JavaReference(Reference wrapped) {
		super(wrapped);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Field> getForeignKeyFields() {
		List<Field> wrappedFields = new ArrayList<>();
		for (Field foreignKeyField : wrapped.getForeignKeyFields()) {
			Field wrappedForeignKeyField = new JavaField(foreignKeyField);
			wrappedFields.add(wrappedForeignKeyField);
		}

		return wrappedFields;
	}

	public String getImport() {
		if (isExternal()) {
			return JavaElementUtils.getJavaImportByPackageAndType(getPackage(), getType(), false);
		} else {
			return JavaElementUtils.getJavaImportByPackageAndType("", getType());
		}
	}

	public boolean isExternal() {
	    DefaultModelInstanceRepository modelInstanceRepository = getModelInstanceRepository();
		String currentProject = modelInstanceRepository.getArtifactId();
		String basePackage = PackageManager.getBasePackage(currentProject);
		Map<String, Entity> referenceEntities = modelInstanceRepository.getEntities(getPackage());
		Entity referenceEntity = referenceEntities.get(getType());

		String currentPackage = referenceEntity != null ? referenceEntity.getPackage() : null;
		return !StringUtils.isBlank(currentPackage) && !currentPackage.equals(basePackage);
	}

	public String getImportPrefix() {
	    DefaultModelInstanceRepository modelInstanceRepository = getModelInstanceRepository();
		Map<String, Entity> referenceEntities = modelInstanceRepository.getEntities(getPackage());
		Entity referenceEntity = referenceEntities.get(getType());
		String currentPackage = referenceEntity.getPackage();
		return StringUtils.isBlank(currentPackage) ? PackageManager.getBasePackage(modelInstanceRepository.getArtifactId())
				: currentPackage;
	}

	/**
	 * Returns the Java imports needed for foreign keys.
	 * 
	 * @return imports
	 */
	public Set<String> getForeignKeyImports() {
		Set<String> importSet = new TreeSet<>();

		if (isExternal()) {
			for (Field foreignKeyField : getForeignKeyFields()) {
				JavaField javaForeignKeyField = (JavaField) foreignKeyField;
				importSet.add(javaForeignKeyField.getImport());
			}
		}

		return importSet;
	}

	/**
	 * Returns the condition block for checking if foreign keys exist.
	 * 
	 * @return condition block
	 */
	public String getForeignKeyCondition() {
		String condition = null;
		if (!isExternal()) {

			StringBuilder sb = new StringBuilder();
			Iterator<Field> foreignKeyFieldIterator = getForeignKeyFields().iterator();
			while (foreignKeyFieldIterator.hasNext()) {
				JavaField javaForeignKeyField = (JavaField) foreignKeyFieldIterator.next();
				sb.append("get");
				sb.append(getCapitalizedName());
				sb.append(javaForeignKeyField.getCapitalizedName());
				sb.append("() == null");
				if (foreignKeyFieldIterator.hasNext()) {
					sb.append(" && ");
				}
			}

			condition = sb.toString();
		}

		return condition;
	}

    static DefaultModelInstanceRepository getModelInstanceRepository() {
        return ModelInstanceRepositoryManager
            .getMetamodelRepository(DefaultModelInstanceRepository.class);
    }

}
