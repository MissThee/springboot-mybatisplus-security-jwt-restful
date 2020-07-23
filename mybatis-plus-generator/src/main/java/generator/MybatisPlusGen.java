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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MybatisPlusGen {
    private static StringBuilder ShowInConsole = new StringBuilder();
    //main-project开始的配置文件路径
    private static final String propertiesFilePath = "/common/src/main/resources/application-common.properties";
    private static final String packageNameAfterCom = "github";

    public static void main(String[] args) throws IOException {
        //配置文件数据源选择
        final String dbTagName = getDbTagName();
        final String subProjectName = getSubProjectName();
        final String functionPackageName = scanner(
                "输入生成的文件所属包名（可写多层包名，点分隔）：" +
                        "\n\r附加说明：" +
                        "\n\r如选择的子项目为manage-base，则文件会生成在com.github.base中。其中com.github为固定值，最后的值为子项目名最后一个“-”之后的字符串，若没有“-”，则直接使用子项目名" +
                        "\n\r文件会输出到目录下的 controller、db/mapper/primary、service/imp、service/interf等下面" +
                        "\n\r可观察现在子项目manage-base中，以上路径均有名为manage的包，“manage”为生成时此步骤输入的字符串");
        updateConsole("已输入包名：" + functionPackageName);
        updateConsole("    ┕━预期输出目录(entity举例)：" + System.getProperty("user.dir") + "/main-project/" + subProjectName + "/src/main/java  com." + packageNameAfterCom + "." + (subProjectName.contains("-") ? subProjectName.substring(subProjectName.lastIndexOf("-") + 1) : subProjectName) + ".db.entity." + functionPackageName);

        String tableNameStr = getTableNameStr();
        final List<String> tableName = Arrays.stream(tableNameStr.split(",")).filter(e -> !e.equals("")).collect(Collectors.toList());
        final String templateSelect = scanner("[最后]选择生成内容(输入序号，可多选如：1345):\n\r[1] entity \n\r[2] mapper.java \n\r[3] mapper.xml \n\r[4] service \n\r[5] impl \n\r[6] controller");
        GenerateCodeExcludeModel(subProjectName, functionPackageName, tableName, templateSelect, dbTagName);
    }

    private static String getTableNameStr() {
        String tableNameStr = null;
        while (tableNameStr == null) {
            String input = scanner("表名，多表个英文逗号分割").replace(" ", "");
            tableNameStr = input;
        }
        updateConsole("已输入表名：" + tableNameStr);
        return tableNameStr;
    }

    private static String getDbTagName() {
        String dbTagName = null;
        while (dbTagName == null) {
            String input = scanner("请选择数据源spring.datasource.XXX，输入序号 ：\n\r[1] primary\n\r[2] secondary");
            switch (input) {
                case "1":
                    dbTagName = "primary";
                    break;
                case "2":
                    dbTagName = "secondary";
                    break;
                default:
                    System.out.println("输入有误，请重新输入");
                    break;
            }
        }
        updateConsole("已选数据源：" + dbTagName);
        return dbTagName;
    }

    private static String getSubProjectName() {
        List<String> subProjectPathNameList = new ArrayList<>();
        {
            File f = new File("./main-project/");
            if (f.isDirectory() && Objects.requireNonNull(f.listFiles()).length > 0) {
                for (File file : Objects.requireNonNull(f.listFiles())) {
                    if (file.isDirectory()) {
                        subProjectPathNameList.add(file.getName());
                    }
                }
            }
        }
        StringBuilder subProjectPathNameStr = new StringBuilder();
        for (int i = 0; i < subProjectPathNameList.size(); i++) {
            subProjectPathNameStr.append("\n\r").append("[").append(i + 1).append("] ");
            subProjectPathNameStr.append(subProjectPathNameList.get(i));
        }
        String subProjectName = null;
        while (subProjectName == null) {
            String input = scanner("选择生成代码存放的子项目，输入序号：" + subProjectPathNameStr.toString());
            try {
                subProjectName = subProjectPathNameList.get(Integer.parseInt(input) - 1);
            } catch (Exception ignored) {
                System.out.println("输入有误，重新输入");
            }
        }
        updateConsole("已选子项目：" + subProjectName);
        return subProjectName;
    }

    private static void updateConsole(String text) {
        if (ShowInConsole.toString().equals("")) {
            for (int i = 0; i < 50; i++) {
                ShowInConsole.append("\n\r");
            }
            ShowInConsole.append("===================================================================\n\r");
        }
        ShowInConsole.append("=  ").append(text).append("\n\r");
        System.out.print(ShowInConsole.toString());
        System.out.println("===================================================================");
    }

    private static void GenerateCodeExcludeModel(String subProjectName, String functionPackageName, List<String> tableName, String templateSelect, String dbSelectTmp) throws IOException {
        final String dbSelect = dbSelectTmp;

        //获取main-project路径
        String projectPath = System.getProperty("user.dir") + "/main-project";
        //读取main-project中的配置文件
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(projectPath + propertiesFilePath)));
        //获取本机信息
        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");// 获取用户名
        String computerName = map.get("COMPUTERNAME");// 获取计算机名
        String userDomain = map.get("USERDOMAIN");// 获取计算机域名
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig()
                .setOutputDir(projectPath + "/" + subProjectName + "/src/main/java")
                .setAuthor(computerName + "," + userName)
                .setFileOverride(false) //覆盖现有
                .setOpen(false)         //生成完打开输出目录
                .setSwagger2(true)      //注释使用swagger2
                .setEnableCache(false)  //xml中二级缓存标志
                .setBaseResultMap(true) //sml生成映射baseMap
                .setServiceName("%sService")
                .setServiceImplName("%sImp");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        String propertiesKeyPrefix = "spring.datasource." + dbSelect + ".";
        DataSourceConfig dsc = new DataSourceConfig()
                .setUrl(properties.getProperty(propertiesKeyPrefix + "jdbc-url"))
                .setDriverName(properties.getProperty(propertiesKeyPrefix + "driver-class-name"))
                .setUsername(properties.getProperty(propertiesKeyPrefix + "username"))
                .setPassword(properties.getProperty(propertiesKeyPrefix + "password"));
        if (dsc.getUrl() == null) {
            dsc.setUrl(properties.getProperty(propertiesKeyPrefix + "url"));
        }
        mpg.setDataSource(dsc);
        if (!new File(projectPath + "/" + subProjectName + "/src/main/java/com").exists()) {
            if (!new File(projectPath + "/" + subProjectName + "/src/main/java/com").mkdirs()) {
                System.out.println("创建目录失败，无法在指定子项目中，创建com文件夹");
                return;
            }
        }
        File file = new File(projectPath + "/" + subProjectName + "/src/main/java");
        File packageName = Objects.requireNonNull(file.listFiles())[0];
        String aa = packageName.getName();
        // 包配置
        PackageConfig pc = new PackageConfig()
                .setParent(packageName.getName() + "." + packageNameAfterCom + "." + (subProjectName.contains("-") ? subProjectName.substring(subProjectName.lastIndexOf("-") + 1) : subProjectName))
                .setEntity("db.entity." + dbSelect + "." + functionPackageName)
                .setMapper("db.mapper." + dbSelect + "." + functionPackageName)
                .setService("service.interf" + "." + functionPackageName)
                .setController("controller" + "." + functionPackageName)
                .setServiceImpl("service.imp" + "." + functionPackageName);
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)//下划线转驼峰
                .setColumnNaming(NamingStrategy.underline_to_camel)//下划线转驼峰
                .setEntityLombokModel(true)//lombok注解，无get、set
                .setSkipView(true)//跳过视图
                .setEntityColumnConstant(true)//跳过视图
                .setEntityTableFieldAnnotationEnable(true)//属性上加字段注解
                .setRestControllerStyle(true)//使用@RestController注解
                .setInclude(tableName.toArray(new String[0]));
        mpg.setStrategy(strategy);
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
                List<FileOutConfig> focList = new ArrayList<>();
                focList.add(new FileOutConfig(templatePath) {  // 自定义配置会被优先输出
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                        return projectPath + "/" + subProjectName + "/src/main/resources/mybatis/mapper/" + dbSelect + "/generate/"
                                + functionPackageName + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                    }
                });

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
        System.out.println(tip + "\n\r请输入，回车确定：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
