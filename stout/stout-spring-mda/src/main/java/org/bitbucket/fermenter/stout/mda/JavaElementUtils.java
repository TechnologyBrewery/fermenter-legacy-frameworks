package org.technologybrewery.fermenter.stout.mda;

import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.fermenter.mda.TypeManager;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.Enumeration;
import org.technologybrewery.fermenter.mda.metamodel.element.Field;
import org.technologybrewery.fermenter.mda.metamodel.element.MetamodelType;
import org.technologybrewery.fermenter.mda.metamodel.element.Parameter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class JavaElementUtils {

    private static final String DOT = ".";

    private static final String ENUMERATION = ".enumeration.";

    static final String VOID = "void";

    /** Needs to be a {@link List} and not {@link Collection} due to JAX-RS parameter requirements. */
    static final String PARAM_COLLECTION_TYPE = "List";

    private JavaElementUtils() {
        // prevent instantiation of all static class
    }

    /**
     * Returns a full qualified java class name for the given package and short name. Base package is treated as a local
     * reference.
     * 
     * @param packageName
     *            package (e.g., org.technologybrewery.fermenter)
     * @param name
     *            class (e.f., FooClass)
     * @return fully qualified name (e.g., org.technologybrewery.fermenter.bizobj.FooClassBO,
     *         org.technologybrewery.fermenter.transfer.FooClass)
     */
    static String getJavaImportByPackageAndType(String packageName, String name) {
        return getJavaImportByPackageAndType(packageName, name, true);
    }

    /**
     * Returns a full qualified java class name for the given package and short name while also specifying if the base
     * package represents a local or remove reference. The local/remote distinction helps control whether or not you
     * will get a BO or TO import for an entity.
     * 
     * @param packageName
     *            package (e.g., org.technologybrewery.fermenter)
     * @param name
     *            class (e.f., FooClass)
     * @param if
     *            the base package is local or not
     * @return fully qualified name (e.g., org.technologybrewery.fermenter.bizobj.FooClassBO,
     *         org.technologybrewery.fermenter.transfer.FooClass)
     */
    static String getJavaImportByPackageAndType(String packageName, String type, boolean basePackageLocal) {
        MetamodelType metamodelType = MetamodelType.getMetamodelType(packageName, type);

        DefaultModelInstanceRepository modelInstanceRepository = getModelInstanceRepository();

        String javaImportType = null;
        if (metamodelType == null) {
            javaImportType = VOID;

        } else if (MetamodelType.ENTITY.equals(metamodelType)) {
            String basePackage = modelInstanceRepository.getBasePackage();
            String entityPackage = StringUtils.isBlank(packageName) ? basePackage : packageName;
            if (basePackage.equals(entityPackage) && basePackageLocal) {
                // this is a local reference, so use the business object:
                javaImportType = entityPackage + ".bizobj." + type + "BO";
            } else {
                // this is a remote reference, so use the transfer object:
                javaImportType = entityPackage + ".transfer." + type;

            }
        } else if (MetamodelType.ENUMERATION.equals(metamodelType)) {
            Enumeration enumeration = modelInstanceRepository.getEnumeration(type);
            javaImportType = enumeration.getPackage() + ENUMERATION + type;

        } else {
            // Attempt to resolve primitive type:
            javaImportType = TypeManager.getFullyQualifiedType(type);
        }

        return javaImportType;

    }

    /**
     * Returns a short java class name for the given package and short name. Base package is treated as a local
     * reference.
     * 
     * @param packageName
     *            package (e.g., org.technologybrewery.fermenter)
     * @param name
     *            class (e.f., FooClass)
     * @return short name (e.g., FooClassBO, FooClass)
     */
    static String getJavaShortNameByPackageAndType(String packageName, String type) {
        String fullyQualifiedJavaClass = getJavaImportByPackageAndType(packageName, type);
        return getJavaShortName(fullyQualifiedJavaClass);
    }

    /**
     * Returns a short java class name for the given package and short name while also specifying if the base package
     * represents a local or remove reference. The local/remote distinction helps control whether or not you will get a
     * BO or TO import for an entity.
     * 
     * @param packageName
     *            package (e.g., org.technologybrewery.fermenter)
     * @param name
     *            class (e.f., FooClass)
     * @param if
     *            the base package is local or not
     * @return short name (e.g., FooClassBO, FooClass)
     */
    static String getJavaShortNameByPackageAndType(String packageName, String type, boolean basePackageLocal) {
        String fullyQualifiedJavaClass = getJavaImportByPackageAndType(packageName, type, basePackageLocal);
        return getJavaShortName(fullyQualifiedJavaClass);
    }

    static String createFullyQualifiedName(String type, String nestedPackage) {       
        return createFullyQualifiedName(type, nestedPackage, getModelInstanceRepository().getArtifactId());
    }

    static String createFullyQualifiedName(String type, String nestedPackage, String packageName) {
        StringBuilder sb = new StringBuilder();
        sb.append(packageName);
        sb.append(nestedPackage).append(type);
        return sb.toString();
    }

    /**
     * Utility to trim a fully qualified class name down to the short name
     * 
     * @param fullyQualifiedJavaClassName
     *            fully qualified class name (e.g., org.technologybrewery.fermenter.enumeration.BarEnumeration)
     * @return short class name (e.g., BarEnumeration)
     */
    static String getJavaShortName(String fullyQualifiedJavaClassName) {
        String result = fullyQualifiedJavaClassName;
        int index = (fullyQualifiedJavaClassName != null) ? fullyQualifiedJavaClassName.lastIndexOf(DOT) : 0;
        if (index > 0) {
            result = fullyQualifiedJavaClassName.substring(index + 1);
        }

        return result;
    }

    static String createSignatureParameters(List<Parameter> parameterList) {
        return createSignatureParameters(parameterList, null, null);
    }

    static String createSignatureParameters(List<Parameter> parameterList, String fieldNameSuffix,
            String fieldTypeSuffix) {
        StringBuilder params = new StringBuilder();
        if (parameterList != null) {
            boolean hasFieldTypeSuffix = StringUtils.isNotBlank(fieldTypeSuffix);
            boolean hasFieldNameSuffix = StringUtils.isNotBlank(fieldNameSuffix);

            for (Iterator<Parameter> i = parameterList.iterator(); i.hasNext();) {
                JavaParameter param = (JavaParameter) i.next();
                if (param.isMany()) {
                    params.append(PARAM_COLLECTION_TYPE + "<").append(param.getJavaType());
                    if ((hasFieldTypeSuffix) && (param.isEntity())) {
                        params.append(fieldTypeSuffix);
                    }
                    params.append(">");
                } else {
                    params.append(param.getJavaType());
                    if ((hasFieldTypeSuffix) && (param.isEntity())) {
                        params.append(fieldTypeSuffix);
                    }
                }
                params.append(" ");
                params.append(param.getName());
                if ((hasFieldNameSuffix) && (param.isEntity())) {
                    params.append(fieldNameSuffix);
                }
                if (i.hasNext()) {
                    params.append(", ");
                }
            }
        }
        return params.toString();
    }

    static String createJaxRSSignatureParameters(List<Parameter> parameterList, String fieldNameSuffix,
            String fieldTypeSuffix) {
        StringBuilder params = new StringBuilder();
        if (parameterList != null) {
            boolean hasFieldTypeSuffix = StringUtils.isNotBlank(fieldTypeSuffix);
            boolean hasFieldNameSuffix = StringUtils.isNotBlank(fieldNameSuffix);

            for (Iterator<Parameter> i = parameterList.iterator(); i.hasNext();) {
                JavaParameter param = (JavaParameter) i.next();
                if (param.isMany()) {
                    params.append(PARAM_COLLECTION_TYPE + "<").append(param.getJavaType());
                    if ((hasFieldTypeSuffix) && (param.isEntity())) {
                        params.append(fieldTypeSuffix);
                    }
                    params.append(">");
                } else {
                    params.append(param.getJavaType());
                    if ((hasFieldTypeSuffix) && (param.isEntity())) {
                        params.append(fieldTypeSuffix);
                    }
                }
                params.append(" ");
                params.append(param.getName());
                if ((hasFieldNameSuffix) && (param.isEntity())) {
                    params.append(fieldNameSuffix);
                }
                if (i.hasNext()) {
                    params.append(", ");
                }
            }
        }
        return params.toString();
    }

    /**
     * Returns the fields for a signature definition of a method.
     * 
     * @param fieldList
     *            a list of {@link Field} instances
     * @return A String like: String foo, Integer bar, Object blah
     */
    static String createSignatureFields(List<Field> fieldList) {
        return createSignatureFields(fieldList, null, null);
    }

    /**
     * Returns the fields for a signature definition of a method. Suffix will only be used if the field references an
     * entity type.
     * 
     * @param fieldList
     *            a list of {@link Field} instances
     * @param fieldNameSuffix
     *            a suffix to add to the end of each field name, if it needs to be altered
     * @param fieldTypeSuffix
     *            a suffix to add to the end of each field type, if it needs to be altered
     * @return A String like: String foo, Integer bar, Object blah
     */
    static String createSignatureFields(List<Field> fieldList, String fieldNameSuffix, String fieldTypeSuffix) {
        // TODO: this should probably include prefix too, but we don't need that right now
        StringBuilder fields = new StringBuilder();
        if (fieldList != null) {
            boolean hasFieldTypeSuffix = StringUtils.isNotBlank(fieldTypeSuffix);
            boolean hasFieldNameSuffix = StringUtils.isNotBlank(fieldNameSuffix);

            for (Iterator<Field> i = fieldList.iterator(); i.hasNext();) {
                JavaField field = (JavaField) i.next();
                fields.append(field.getJavaType());

                if ((hasFieldTypeSuffix) && (field.isEntity())) {
                    fields.append(fieldTypeSuffix);
                }
                fields.append(" ");
                fields.append(field.getName());

                if ((hasFieldNameSuffix) && (field.isEntity())) {
                    fields.append(fieldNameSuffix);
                }
                if (i.hasNext()) {
                    fields.append(", ");
                }
            }
        }
        return fields.toString();
    }

    /**
     * Returns the fields for a call to a method exactly as the fields are defined.
     * 
     * @param fieldList
     *            a list of {@link Field} instances
     * @return A String like: foo, bar, blah.
     */
    static String createSignatureFieldParams(List<Field> fieldList) {
        return createSignatureFieldParams(fieldList, null);
    }

    /**
     * Returns the fields for a call to a method, with an optional suffix for name and type. Suffix will only be used if
     * the field references an entity type.
     * 
     * @param fieldList
     *            a list of {@link Field} instances
     * @param fieldNameSuffix
     *            a suffix to add to the end of each field name, if it needs to be altered
     * @return A String like: foo, bar, blah.
     */
    static String createSignatureFieldParams(List<Field> fieldList, String fieldNameSuffix) {
        // TODO: this should probably include prefix too, but we don't need that right now
        StringBuilder fields = new StringBuilder();
        if (fieldList != null) {
            boolean hasFieldNameSuffix = StringUtils.isNotBlank(fieldNameSuffix);

            for (Iterator<Field> i = fieldList.iterator(); i.hasNext();) {
                JavaField field = (JavaField) i.next();
                fields.append(field.getName());
                if ((hasFieldNameSuffix) && (field.isEntity())) {
                    fields.append(fieldNameSuffix);
                }
                if (i.hasNext()) {
                    fields.append(", ");
                }
            }
        }
        return fields.toString();
    }

    /**
     * Returns a base JNDI name as the base packaage name with forward slashes substituted for periods.
     * 
     * @param basePackage
     *            package name (e.g., com.foo.bar)
     * @return base JNDI string (e.g., com/foo/bar)
     */
    public static String getBaseJndiName(String basePackage) {
        return (basePackage != null) ? basePackage.replace('.', '/') : "";
    }

    /**
     * Ensures proper Javadoc formatting for documentation content. For example, making sure there is a period at the
     * end of the first line so that previews of the content aren't limited to an incomplete set of characters.
     * 
     * @param documentation
     *            input
     * @return formatted input value
     */
    public static String formatForJavadoc(String documentation) {
        return ((StringUtils.isNotBlank(documentation)) && (documentation.endsWith(DOT))) ? documentation + DOT
                : documentation;

    }

    /**
     * Returns true if the import should be included.
     * 
     * @param importValue
     *            import to check
     * @return whether or not to include
     */
    static boolean checkImportAgainstDefaults(String importValue) {
        return (importValue != null && !importValue.startsWith("java.lang."));
    }

    static DefaultModelInstanceRepository getModelInstanceRepository() {
        return ModelInstanceRepositoryManager
            .getMetamodelRepository(DefaultModelInstanceRepository.class);
    }
}
