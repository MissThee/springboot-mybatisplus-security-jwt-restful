package generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class MybatisPlusGen {
    public static void main(String[] args) throws IOException {
        //获取本机信息
        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");// 获取用户名
        String computerName = map.get("COMPUTERNAME");// 获取计算机名
        String userDomain = map.get("USERDOMAIN");// 获取计算机域名
        //获取main-project路径
        String projectPath = System.getProperty("user.dir") + "/main-project";
        //读取main-project中的配置文件
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(projectPath + "/common/src/main/resources/application-common.properties")));

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig() {{
            setOutputDir(projectPath + "/manage-form/src/main/java");
            setAuthor(computerName + "," + userName);
            setFileOverride(false); //覆盖现有
            setOpen(false);         //生成完打开输出目录
            setSwagger2(true);      //注释使用swagger2
            setEnableCache(false);  //xml中二级缓存标志
            setBaseResultMap(true); //sml生成映射baseMap
            setServiceName("%sService");
            setServiceImplName("%sImp");
        }};
        mpg.setGlobalConfig(gc);

        // 数据源配置
        String propertiesKeyPrefix = "spring.datasource.primary.";
        DataSourceConfig dsc = new DataSourceConfig() {{
            setUrl(properties.getProperty(propertiesKeyPrefix + "jdbc-url"));
            setDriverName(properties.getProperty(propertiesKeyPrefix + "driver-class-name"));
            setUsername(properties.getProperty(propertiesKeyPrefix + "username"));
            setPassword(properties.getProperty(propertiesKeyPrefix + "password"));
        }};
        mpg.setDataSource(dsc);

        // 包配置
        final String functionName = "." + scanner("生成的文件所属包名");
        PackageConfig pc = new PackageConfig() {{
            setParent("com.github.form");
            setEntity("db.entity.primary" + functionName);
            setMapper("db.mapper.primary" + functionName);
            setService("service.interf" + functionName);
            setServiceImpl("service.imp" + functionName);
        }};
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);//下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//下划线转驼峰
        strategy.setEntityLombokModel(true);//lombok注解，无get、set
        strategy.setSkipView(true);//跳过视图
        strategy.setEntityColumnConstant(true);//跳过视图
        strategy.setEntityTableFieldAnnotationEnable(true);//属性上加字段注解
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        mpg.setStrategy(strategy);

        final String templateSelect = scanner("选择生成内容(可多选):\n\r1-entity; \n\r2-mapper.java; \n\r3-mapper.xml; \n\r4-service; \n\r5-impl; \n\r6-controller");
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        if (!templateSelect.contains("1")) {
            templateConfig.setEntity(null);
        }
        if (!templateSelect.contains("2")) {
            templateConfig.setMapper(null);
        }
        if (!templateSelect.contains("4")) {
            templateConfig.setService(null);
        }
        if (!templateSelect.contains("5")) {
            templateConfig.setServiceImpl(null);
        }
        if (!templateSelect.contains("6")) {
            templateConfig.setController(null);
        }
        mpg.setTemplate(templateConfig);

        if (templateSelect.contains("3")) {
            // 自定义配置
            InjectionConfig cfg = new InjectionConfig() {
                @Override
                public void initMap() {
                }
            };
            {
                String templatePath = "/templates/mapper.xml.ftl"; // 模板引擎是freemarker
                // 自定义输出配置
                List<FileOutConfig> focList = new ArrayList<FileOutConfig>() {{
                    add(new FileOutConfig(templatePath) {  // 自定义配置会被优先输出
                        @Override
                        public String outputFile(TableInfo tableInfo) {
                            // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                            return projectPath + "/src/main/resources/mybatis/mapper/primary/generate/"
                                    + functionName + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                        }
                    });
                }};
                cfg.setFileOutConfigList(focList);
            }
            mpg.setCfg(cfg);
        }
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
