package com.github.missthee.tool;

import java.util.*;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Yangcq on 2018/08/16.
 */

public class MybatisPlusGen {


    public static void main(String[] args) {

        String projectPath = System.getProperty("user.dir");

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig() {{
            setOutputDir(projectPath + "/src/main/java");
            setAuthor("mt");
            setOpen(false);
            setSwagger2(true);
            setEnableCache(true);
            setFileOverride(true);
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
        final String functionName = "." + scanner("功能块名称。即生成文件的所属包名");

        // 包配置
        PackageConfig pc = new PackageConfig() {{
            setParent("com.github.missthee.db");
            setEntity("po.primary" + functionName);
            setMapper("mapper.primary" + functionName);
        }};
        mpg.setPackageInfo(pc);

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
                                + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                    }
                });
            }};
            cfg.setFileOutConfigList(focList);
        }
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig() {{
            //为null时不生成相应文件
            setXml(null);
            setController(null);
            setService(null);
            setServiceImpl(null);
        }};
        mpg.setTemplate(templateConfig);

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
