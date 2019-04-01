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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Yangcq on 2018/08/16.
 */

public class MybatisPlusGen {
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir") + "/main-project";

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig() {{
            setOutputDir(projectPath + "/src/main/java");
            setAuthor("mt");
            setFileOverride(false); //覆盖现有
            setOpen(false);         //生成完打开输出目录
            setSwagger2(true);      //注释使用swagger2
            setEnableCache(false);  //xml中二级缓存标志
            setBaseResultMap(true); //sml生成映射baseMap
            setServiceName("%sService");
            setServiceImplName("%sServiceImp");
        }};
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig() {{
            setUrl("jdbc:mysql://localhost:3306/mybatis_test_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullNamePatternMatchesAll=true&useSSL=false");
            setDriverName("com.mysql.jdbc.Driver");
            setUsername("user");
            setPassword("1234");
        }};
        mpg.setDataSource(dsc);
        final String functionName = scanner("生成的文件所属包名");
        String functionNameWithDot = "." + functionName;
        // 包配置
        PackageConfig pc = new PackageConfig() {{
            setParent("com.github.missthee");
            setEntity("db.po.primary" + functionNameWithDot);
            setMapper("db.mapper.primary" + functionNameWithDot);
            setService("service.interf" + functionNameWithDot);
            setServiceImpl("service.imp" + functionNameWithDot);
        }};
        mpg.setPackageInfo(pc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);//lombok注解，无get、set
        strategy.setSkipView(true);//跳过视图
        strategy.setEntityColumnConstant(true);//跳过视图
        strategy.setEntityTableFieldAnnotationEnable(true);//属性上加字段注解
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        mpg.setStrategy(strategy);


        final String templateSelect = scanner("设置生成内容:\n\r1-entity; \n\r2-mapper.java; \n\r3-mapper.xml; \n\r4-service; \n\r5-impl; \n\r6-controller");
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
                String templatePath = "/templates/mapper.xml.ftl"; // 如果模板引擎是 freemarker
                // 自定义输出配置
                List<FileOutConfig> focList = new ArrayList<FileOutConfig>() {{
                    // 自定义配置会被优先输出
                    add(new FileOutConfig(templatePath) {
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
