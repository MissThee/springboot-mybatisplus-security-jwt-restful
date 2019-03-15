//package com.github.missthee.config.flowable.ConfigBeans;
//
//
//import org.flowable.common.engine.impl.history.HistoryLevel;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.List;
//@Component
//@ConfigurationProperties(
//        prefix = "flowable"
//)
//public class FlowableProperties {
//    private boolean checkProcessDefinitions = true;
//    private boolean asyncExecutorActivate = true;
//    private boolean asyncHistoryExecutorActivate = true;
//    private boolean restApiEnabled;
//    private String activityFontName = "Arial";
//    private String labelFontName = "Arial";
//    private String annotationFontName = "Arial";
//    private String deploymentName = "SpringBootAutoDeployment";
//    private String databaseSchemaUpdate = "true";
//    private String databaseSchema;
//    /** @deprecated */
//    @Deprecated
//    private boolean isDbIdentityUsed = true;
//    private boolean isDbHistoryUsed = true;
//    private HistoryLevel historyLevel;
//    private String processDefinitionLocationPrefix;
//    private List<String> processDefinitionLocationSuffixes;
//    private boolean jpaEnabled;
//    private List<String> customMybatisMappers;
//    private List<String> customMybatisXMLMappers;
//
//    public FlowableProperties() {
//        this.historyLevel = HistoryLevel.AUDIT;
//        this.processDefinitionLocationPrefix = "classpath*:/processes/";
//        this.processDefinitionLocationSuffixes = Arrays.asList("**.bpmn20.xml", "**.bpmn");
//        this.jpaEnabled = true;
//    }
//
//    public boolean isAsyncExecutorActivate() {
//        return this.asyncExecutorActivate;
//    }
//
//    public void setAsyncExecutorActivate(boolean asyncExecutorActivate) {
//        this.asyncExecutorActivate = asyncExecutorActivate;
//    }
//
//    public boolean isAsyncHistoryExecutorActivate() {
//        return this.asyncHistoryExecutorActivate;
//    }
//
//    public void setAsyncHistoryExecutorActivate(boolean asyncHistoryExecutorActivate) {
//        this.asyncHistoryExecutorActivate = asyncHistoryExecutorActivate;
//    }
//
//    public boolean isRestApiEnabled() {
//        return this.restApiEnabled;
//    }
//
//    public void setRestApiEnabled(boolean restApiEnabled) {
//        this.restApiEnabled = restApiEnabled;
//    }
//
//    public boolean isJpaEnabled() {
//        return this.jpaEnabled;
//    }
//
//    public void setJpaEnabled(boolean jpaEnabled) {
//        this.jpaEnabled = jpaEnabled;
//    }
//
//    /** @deprecated */
//    @DeprecatedConfigurationProperty(
//            replacement = "flowable.process.servlet.path"
//    )
//    @Deprecated
//    public String getRestApiMapping() {
//        throw new IllegalStateException("Usage of deprecated property. Use FlowableProcessProperties");
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public void setRestApiMapping(String restApiMapping) {
//        throw new IllegalStateException("Usage of deprecated property. Use FlowableProcessProperties");
//    }
//
//    /** @deprecated */
//    @DeprecatedConfigurationProperty(
//            replacement = "flowable.process.servlet.name"
//    )
//    @Deprecated
//    public String getRestApiServletName() {
//        throw new IllegalStateException("Usage of deprecated property. Use FlowableProcessProperties");
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public void setRestApiServletName(String restApiServletName) {
//        throw new IllegalStateException("Usage of deprecated property. Use FlowableProcessProperties");
//    }
//
//    public boolean isCheckProcessDefinitions() {
//        return this.checkProcessDefinitions;
//    }
//
//    public void setCheckProcessDefinitions(boolean checkProcessDefinitions) {
//        this.checkProcessDefinitions = checkProcessDefinitions;
//    }
//
//    public String getDeploymentName() {
//        return this.deploymentName;
//    }
//
//    public void setDeploymentName(String deploymentName) {
//        this.deploymentName = deploymentName;
//    }
//
//    public String getDatabaseSchemaUpdate() {
//        return this.databaseSchemaUpdate;
//    }
//
//    public void setDatabaseSchemaUpdate(String databaseSchemaUpdate) {
//        this.databaseSchemaUpdate = databaseSchemaUpdate;
//    }
//
//    public String getDatabaseSchema() {
//        return this.databaseSchema;
//    }
//
//    public void setDatabaseSchema(String databaseSchema) {
//        this.databaseSchema = databaseSchema;
//    }
//
//    /** @deprecated */
//    @DeprecatedConfigurationProperty(
//            replacement = "flowable.idm.enabled"
//    )
//    @Deprecated
//    public boolean isDbIdentityUsed() {
//        return this.isDbIdentityUsed;
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public void setDbIdentityUsed(boolean isDbIdentityUsed) {
//        this.isDbIdentityUsed = isDbIdentityUsed;
//    }
//
//    public boolean isDbHistoryUsed() {
//        return this.isDbHistoryUsed;
//    }
//
//    public void setDbHistoryUsed(boolean isDbHistoryUsed) {
//        this.isDbHistoryUsed = isDbHistoryUsed;
//    }
//
//    public HistoryLevel getHistoryLevel() {
//        return this.historyLevel;
//    }
//
//    public void setHistoryLevel(HistoryLevel historyLevel) {
//        this.historyLevel = historyLevel;
//    }
//
//    public String getProcessDefinitionLocationPrefix() {
//        return this.processDefinitionLocationPrefix;
//    }
//
//    public void setProcessDefinitionLocationPrefix(String processDefinitionLocationPrefix) {
//        this.processDefinitionLocationPrefix = processDefinitionLocationPrefix;
//    }
//
//    public List<String> getProcessDefinitionLocationSuffixes() {
//        return this.processDefinitionLocationSuffixes;
//    }
//
//    public void setProcessDefinitionLocationSuffixes(List<String> processDefinitionLocationSuffixes) {
//        this.processDefinitionLocationSuffixes = processDefinitionLocationSuffixes;
//    }
//
//    public List<String> getCustomMybatisMappers() {
//        return this.customMybatisMappers;
//    }
//
//    public void setCustomMybatisMappers(List<String> customMyBatisMappers) {
//        this.customMybatisMappers = customMyBatisMappers;
//    }
//
//    public List<String> getCustomMybatisXMLMappers() {
//        return this.customMybatisXMLMappers;
//    }
//
//    public void setCustomMybatisXMLMappers(List<String> customMybatisXMLMappers) {
//        this.customMybatisXMLMappers = customMybatisXMLMappers;
//    }
//
//    public String getActivityFontName() {
//        return this.activityFontName;
//    }
//
//    public void setActivityFontName(String activityFontName) {
//        this.activityFontName = activityFontName;
//    }
//
//    public String getLabelFontName() {
//        return this.labelFontName;
//    }
//
//    public void setLabelFontName(String labelFontName) {
//        this.labelFontName = labelFontName;
//    }
//
//    public String getAnnotationFontName() {
//        return this.annotationFontName;
//    }
//
//    public void setAnnotationFontName(String annotationFontName) {
//        this.annotationFontName = annotationFontName;
//    }
//}
