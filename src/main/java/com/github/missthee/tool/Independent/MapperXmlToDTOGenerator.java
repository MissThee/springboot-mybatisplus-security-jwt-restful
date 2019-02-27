package server.tool.Independent;

import com.google.common.base.Joiner;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
//使用mapper.xml的resultMap生成实体类
public class MapperXmlToDTOGenerator {
    private static StringBuilder currentFileSB;
    private static String currentFileName;
    private static Set<String> currentFileImportSet;

    public static void main(String[] args) {
        generateInFolder();
    }

    private static void generateInFolder() {
        try {
            String xmlFolderPath = "mybatis/mapper/primary/custom/";
            String outPutPath = "src.main.java.server.db.primary.dto";
            Boolean appendEnable = false;
            String fileType = "DTO";          //DTO、CLASS
            Boolean withInnerClass = true;
            Boolean separateFolder = true;
            List<String> fileNameList = getResourceFiles(xmlFolderPath);
            for (String fileName : fileNameList) {
                String xmlFilePath = xmlFolderPath + fileName;
                generate(xmlFilePath, outPutPath, appendEnable, fileType, separateFolder, withInnerClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateOne() {
        try {
            String xmlFilePath = "mybatis/mapper/primary/custom/CloginMapper.xml";
            String outPutPath = "src.main.java.server.config";
            Boolean appendEnable = false;
            String fileType = "DTO";          //DTO、CLASS
            Boolean withInnerClass = true;
            Boolean separateFolder = true;
            generate(xmlFilePath, outPutPath, appendEnable, fileType, separateFolder, withInnerClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generate(String xmlPath, String outPutPackage, Boolean appendEnable, String fileType, Boolean separateFolder, Boolean withInnerClass) throws DocumentException {
        outPutPackage = outPutPackage.replace(".", "\\");
        SAXReader read = new SAXReader();
        File file = getResourceFile(xmlPath);
        System.out.println("READ FILE: " + file.getName());
        Document doc = read.read(file);//返回Document对象
        Map<String, String> map = new HashMap<>();
        Element mapperElement = doc.getRootElement();//mapper标签
        List<Element> elementList = mapperElement.elements();
        for (Element e : elementList) {
            if (e.getName().equals("resultMap")) {
                List<Attribute> listAttr = e.attributes();
                Map<String, String> attrMap = new HashMap<>();//当前节点的所有属性的list
                for (Attribute attr : listAttr) {//遍历当前节点的所有属性
                    attrMap.put(attr.getName(), attr.getValue());
                }
                String packgeName = "";
                if (separateFolder) {
                    packgeName = file.getName().replace(".xml", "");
                    packgeName = packgeName.toLowerCase();
                }
                currentFileName = null;
                currentFileImportSet = new HashSet<>();
                currentFileSB = new StringBuilder();
                currentFileSB.append("package ").append(outPutPackage.replace("\\", "/").replace("/", ".").replace("src.main.java.", ""));
                currentFileSB.append(packgeName.length() > 0 ? ("." + packgeName) : "");
                currentFileSB.append(";\r\n");
                currentFileSB.append("\r\n");
                currentFileSB.append("import lombok.Data;\r\n");
                currentFileSB.append("import javax.persistence.Id;\r\n");
                currentFileSB.append("[IMPRORT_PACKEGE]\r\n");
                getNodes(e, fileType, withInnerClass);
                currentFileSB.append("}\r\n");
                String currentFileStr = currentFileSB.toString().replace("[IMPRORT_PACKEGE]", Joiner.on("").join(currentFileImportSet));
                outPutFile(currentFileStr, outPutPackage + "\\" + packgeName + "\\" + firstToUpper(currentFileName) + ".java", appendEnable);
            }
        }
    }


    private static void getNodes(Element node, String fileType, Boolean withInnerClass) {
        List<Attribute> listAttr = node.attributes();
        Map<String, String> attrMap = new HashMap<>();//当前节点的所有属性的list
        for (Attribute attr : listAttr) {//遍历当前节点的所有属性
            attrMap.put(attr.getName(), attr.getValue());
        }
//        if (node.getName().equals("resultMap") || node.getName().equals("association") || node.getName().equals("collection")) {
        String typeAttrKey = "";
        String pauseForClass = "";
        switch (node.getName()) {
            case "resultMap":
                System.out.println("    CLASS: " + getClassNameString(attrMap.get("type")));
                typeAttrKey = "type";

                currentFileSB.append("@Data\r\n");
                String publicClassName = "";
                if (fileType.equals("DTO")) {
                    publicClassName = firstToUpper(getClassNameString(attrMap.get("id")) + "DTO");
                    currentFileSB.append("public class ").append(publicClassName);
                } else {
                    publicClassName = firstToUpper(getClassNameString(attrMap.get(typeAttrKey)));
                    currentFileSB.append("public class ").append(publicClassName);
                }
                if (StringUtils.isEmpty(currentFileName)) {
                    currentFileName = publicClassName;
                }
                currentFileSB.append(" {\r\n");
                break;
            case "association":
                typeAttrKey = "javaType";
                pauseForClass = "    ";
                currentFileSB.append(pauseForClass).append("@Data\r\n");
                currentFileSB.append(pauseForClass).append("public static class ").append(getClassNameString(attrMap.get(typeAttrKey))).append(" {\r\n");
                break;
            case "collection":
                typeAttrKey = "ofType";
                pauseForClass = "    ";
                currentFileSB.append(pauseForClass).append("@Data\r\n");
                currentFileSB.append(pauseForClass).append("public static class ").append(getClassNameString(attrMap.get(typeAttrKey))).append(" {\r\n");
                break;
        }

        List<Element> childElementList = node.elements();
        for (Element childElement : childElementList) {
            List<Attribute> childAttrList = childElement.attributes();
            Map<String, String> childAttrMap = new HashMap<>();
            for (Attribute childAttr : childAttrList) {//遍历当前节点的所有属性
                childAttrMap.put(childAttr.getName(), childAttr.getValue());
            }
            switch (childElement.getName()) {
                case "id":
                    currentFileSB.append(pauseForClass).append("    @Id\r\n");
                    currentFileSB.append(pauseForClass).append("    private ").append(getJavaType(childAttrMap.get("jdbcType"))).append(" ").append(childAttrMap.get("property")).append(";\r\n");
                    break;
                case "result":
                    currentFileSB.append(pauseForClass).append("    private ").append(getJavaType(childAttrMap.get("jdbcType"))).append(" ").append(childAttrMap.get("property")).append(";\r\n");
                    break;
                case "association":
                    currentFileSB.append(pauseForClass).append("    private ").append(getClassNameString(childAttrMap.get("javaType"))).append(" ").append(childAttrMap.get("property")).append(";\r\n");
                    if (fileType.equals("DTO") && !withInnerClass) {
                        currentFileImportSet.add("import " + childAttrMap.get("javaType") + ";\r\n");
                    }
                    break;
                case "collection":
                    currentFileSB.append(pauseForClass).append("    private List<").append(getClassNameString(childAttrMap.get("ofType"))).append("> ").append(childAttrMap.get("property")).append(";\r\n");
                    currentFileImportSet.add("import java.util.List;\r\n");
                    if (fileType.equals("DTO") && !withInnerClass) {
                        currentFileImportSet.add("import " + childAttrMap.get("ofType") + ";\r\n");
                    }
                    break;
            }
        }
        if (!node.getName().equals("resultMap")) {
            currentFileSB.append(pauseForClass).append("}\r\n");
        }
//        }

        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for (Element e : listElement) {//遍历所有一级子节点
            if (e.getName().equals("resultMap")) {
                getNodes(e, fileType, withInnerClass);//递归
            } else if (withInnerClass) {
                if (e.getName().equals("association") || e.getName().equals("collection")) {
                    getNodes(e, fileType, withInnerClass);//递归
                }
            }
        }
    }

    private static String getJavaType(String jdbcType) {
        String javaType = "";
        jdbcType = jdbcType.toUpperCase();
        switch (jdbcType) {
            case "BIGDECIMAL":
                javaType = "BigDecimal";
                currentFileImportSet.add("import java.math.BigDecimal;\r\n");
                break;
            case "BIGINTEGER":
                javaType = "BigInteger";
                currentFileImportSet.add("import java.math.BigInteger;\r\n");
                break;
            case "BLOB":
                javaType = "String";
                break;
            case "CLOB":
                javaType = "String";
                break;
            case "DATE":
                javaType = "Date";
                currentFileImportSet.add("import java.util.Date;\r\n");
                break;
            case "DECIMAL":
                javaType = "Long";
                break;
            case "LOCALDATE":
                javaType = "LocalDate";
                currentFileImportSet.add("import java.time.LocalDate;\r\n");
                break;
            case "LOCALDATETIME":
                javaType = "LocalDateTime";
                currentFileImportSet.add("import java.time.LocalDateTime;\r\n");
                break;
            case "LOCALTIME":
                javaType = "LocalTime";
                currentFileImportSet.add("import java.time.LocalTime;\r\n");
                break;
            case "TIME":
                javaType = "Date";
                currentFileImportSet.add("import java.util.Date;\r\n");
                break;
            case "TIMESTAMP":
                javaType = "Date";
                currentFileImportSet.add("import java.util.Date;\r\n");
                break;
            case "VARCHAR":
                javaType = "String";
                break;
            default:
                javaType = "Object";
                break;
        }

        return javaType;
    }

    private static String getClassNameString(String str) {
        int dollarIndex = str.lastIndexOf("$");
        if (dollarIndex > -1) {
            return str.substring(dollarIndex + 1);
        } else {
            int docIndex = str.lastIndexOf(".");
            if (docIndex > -1) {
                return str.substring(docIndex + 1);
            } else {
                return str;
            }
        }
    }

    private static String getAfterLastDotString(String str) {
        int docIndex = str.lastIndexOf(".");
        if (docIndex > -1) {
            return str.substring(docIndex + 1);
        } else {
            return str;
        }
    }

    private static String getBeforeLastDotString(String str) {
        int docIndex = str.lastIndexOf(".");
        if (docIndex > -1) {
            return str.substring(0, docIndex);
        } else {
            return str;
        }
    }

    private static void outPutFile(String text, String path, Boolean appendEnable) {
        try {
            String splitStr = "";
            if (appendEnable) {
                splitStr = "//APPEND---------------" + new Date() + "-----------------\r\n";
            }
            File filr = new File(path.substring(0, path.lastIndexOf("\\")));
            if (!filr.exists()) {
                filr.mkdirs();
            }
            //1、打开流
            Writer w = new FileWriter(path, appendEnable);
            //2、写入内容
            w.write(splitStr);
            w.write(text);
            //3、关闭流
            w.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getResourceFiles(String path) {
        List<String> fileList = new ArrayList<>();
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:" + path);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File childFile : files) {
                        fileList.add(childFile.getName());
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    private static File getResourceFile(String path) {
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:" + path);
            if (file.exists()) {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static String firstToUpper(String str) {
        if (str.length() > 0) {
            if (Character.isUpperCase(str.charAt(0)))
                return str;
            else
                return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        } else {
            return str;
        }
    }
}
