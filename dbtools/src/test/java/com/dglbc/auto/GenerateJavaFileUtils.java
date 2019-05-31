package com.dglbc.auto;

/**
 * Created by LBC on 2019/2/15.
 */

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.text.MessageFormat.format;

public class GenerateJavaFileUtils {
    // 定义数据库常用类型
    private static final String TYPE_CHAR = "char";
    private static final String TYPE_DATE = "date";
    private static final String TYPE_TIMESTAMP = "timestamp";
    private static final String TYPE_INT = "int";
    private static final String TYPE_BIGINT = "bigint";
    private static final String TYPE_TEXT = "text";
    private static final String TYPE_BIT = "bit";
    private static final String TYPE_DECIMAL = "decimal";
    private static final String TYPE_BLOB = "blob";
    // 配置文件存放地址
    private static final String PACKAGEPATH = "D:\\workspace\\entity\\";
    private static final String BEAN_PATH = PACKAGEPATH + "entity_bean";
    private static final String SERVICE_PATH = PACKAGEPATH + "entity_service";
    private static final String MAPPER_PATH = PACKAGEPATH + "entity_mapper";
    private static final String XML_PATH = PACKAGEPATH + "entity_mapper/xml";
    // 配置文件包名称 , 这些值需要根据各自的项目配置
    private static final String MODULENAME = "com.chenyulian";
    private static final String BEAN_PACKAGE = MODULENAME + ".entity";
    private static final String MAPPER_PACKAGE = MODULENAME + ".dao";
    private static final String SERVICE_PACKAGE = MODULENAME + ".service";
    private static final String SERVICEIMPL_PACKAGE = MODULENAME + ".service.impl";
    // 配置数据库连接信息
    private static final String DRIVERNAME = "com.mysql.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String URL = "jdbc:mysql://localhost:3306/chenyulian?characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
    // 方法统一命名
    private static final String save = "insert";
    private static final String update = "updateById";
    private static final String countTotalNum = "count";
    private static final String queryById = " selectById";
    private static final String delete = "deleteById";
    // sql语句
    private static final String showTablesName = "show tables";
    // 获取数据库的所有表名
    private static final String showTablesInfo = "show table status";
    // 获取数据库的所有表详情信息(包括注释)
    private static final String showFields = "show full fields from ";
    // 获取指定表的所有字段详情
    // 定义系统中使用到的全局变量
    private String tableName;
    private String beanName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    private String lowerBeanName;
    private String mapperName;
    private List columns = new ArrayList<>();
    private List types = new ArrayList<>();
    private List comments = new ArrayList<>();
    private Connection conn;
    // 常用的配置项
    /**
     * 用于指定生成类文件的表, 当值为空时会将数据库的所有表都生成类文件
     */

    private static final String TABLE_NAME = "gift";
    /**
     * 表名中的这些值将不会转换为类名的一部分
     */

    private static final String[] TABLE_PREFIXS = {"app", "lms", "wms", "zwms", "v2"};

    /**
     * 删除指定目录下所有文件,若目录不存在则创建该目录
     */

    private static void mkdirs(Runtime runtime) throws IOException {
        File file = new File(PACKAGEPATH);
        if (file.exists()) {
            runtime.exec("cmd /c del /q/a/f/s " + file.getAbsolutePath());
        }
        file.mkdirs();
    }

    /**
     * 获取连接
     */

    private void initConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVERNAME);
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 获取所有的表名
     */

    private List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        PreparedStatement pstate = conn.prepareStatement(showTablesName);
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString(1);
            if (tableName.toLowerCase().startsWith("yy_")) {
                tables.add(tableName);
            }
        }
        return tables;
    }

    /**
     * 根据表名生成实体类名称及所有相关的文件名
     */

    private void initNameByTable(String table) {
        tableName = table;
        beanName = getBeanName(table);
        lowerBeanName = lowerCaseFirstLitter(beanName);
        mapperName = beanName + "Mapper";
        serviceName = beanName + "Service";
        serviceImplName = serviceName + "Impl";
    }

    /**
     * 根据表名获取实体类名
     */

    private String getBeanName(String table) {
        StringBuilder entityName = new StringBuilder(table.length());
        String tableLower = table.toLowerCase();
        String[] tables = tableLower.split("_");
        String temp = null;
        for (int i = 0; i < tables.length; i++) {
            temp = tables[i].trim();
            if (canUseTemp(temp)) {
                entityName.append(upperCaseFirstLitter(temp));
            }
        }
        return entityName.toString();
    }

    /**
     * 判断表名前缀是否要加到实体类名上
     */

    private Boolean canUseTemp(String temp) {
        if (isEmpty(temp)) {
            return false;
        }
        for (String tablePrefix : TABLE_PREFIXS) {
            if (tablePrefix.equalsIgnoreCase(temp)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取实体类属性的数据类型
     */

    private String processType(String type) {
        if (type.indexOf(TYPE_CHAR) > -1) {
            return "String";
        } else if (type.indexOf(TYPE_BIGINT) > -1) {
            return "Long";
        } else if (type.indexOf(TYPE_INT) > -1) {
            return "Integer";
        } else if (type.indexOf(TYPE_DATE) > -1) {
            return "Date";
        } else if (type.indexOf(TYPE_TEXT) > -1) {
            return "String";
        } else if (type.indexOf(TYPE_TIMESTAMP) > -1) {
            return "Date";
        } else if (type.indexOf(TYPE_BIT) > -1) {
            return "Boolean";
        } else if (type.indexOf(TYPE_DECIMAL) > -1) {
            return "BigDecimal";
        } else if (type.indexOf(TYPE_BLOB) > -1) {
            return "byte[]";
        }
        return null;
    }

    /**
     * 将字段名转换为实体类的属性名
     */

    private String processField(String field) {
        StringBuilder sb = new StringBuilder(field.length());
        String[] fields = field.split("_");
        sb.append(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            sb.append(upperCaseFirstLitter(fields[i].trim()));
        }
        return sb.toString();
    }

    /**
     * 构建类上面的注释
     */

    private void buildClassComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" */                ");
    }

    /**
     * 构建方法上面的注释
     */

    private void buildMethodComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.write("\t/**" + text + "*/                    ");
    }

    /**
     * 生成实体类
     */

    private void buildEntityBean
    (List<String> columns, List<String> types, List<String> comments, String tableComment) throws
            IOException {
        instanceFolder(BEAN_PATH);
        BufferedWriter bw = instanceBufferedWriter(BEAN_PATH, beanName + ".java");
        writeBeanHead(tableComment, bw);
        writeBeanColumns(columns, types, comments, bw);
        writeGetSetMethod(columns, types, bw);
        writeEnd(bw);
    }

    /**
     * 写类结尾处代码
     */

    private void writeEnd(BufferedWriter bw) throws IOException {
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }

    /**
     * 写实体类的get,set方法
     */

    private void writeGetSetMethod(List<String> columns, List<String> types, BufferedWriter bw) throws
            IOException {
        String uppperField = null;
        String lowerField = null;
        String tempType = null;
        for (int i = 0; i < columns.size(); i++) {
            tempType = processType(types.get(i));
            lowerField = processField(columns.get(i));
            uppperField = upperCaseFirstLitter(lowerField);
            bw.newLine();
            bw.write("\tpublic void set" + uppperField + "(" + tempType + " " + lowerField + "){");
            bw.newLine();
            bw.write("\t\tthis." + lowerField + " = " + lowerField + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
            bw.write("\tpublic " + tempType + " get" + uppperField + "(){");
            bw.newLine();
            bw.write("\t\treturn this." + lowerField + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
        }
        bw.newLine();
        bw.newLine();
    }

    /**
     * 写实体类属性代码
     */

    private void writeBeanColumns
    (List<String> columns, List<String> types, List<String> comments, BufferedWriter bw) throws
            IOException {
        for (int i = 0; i < columns.size(); i++) {
            if (isNotEmpty(comments.get(i))) {
                bw.write("\t/**" + comments.get(i) + "*/                            ");
                bw.newLine();
            }
            bw.write("\t                    private " + processType(types.get(i)) + " " + processField(columns.get(i)) + ";                    ");
            bw.newLine();
            bw.newLine();
        }
        bw.newLine();
    }

    /**
     * 写实体类头部代码
     */

    private void writeBeanHead(String tableComment, BufferedWriter bw) throws IOException {
        bw.write("package " + BEAN_PACKAGE + ";");
        bw.newLine();
        bw.write("import java.io.Serializable;");
        bw.newLine();
        bw.write("import java.util.Date;");
        bw.newLine();
        buildClassComment(bw, tableComment + "实体类");
        bw.newLine();
        bw.write("public class " + beanName + " implements Serializable {");
        bw.newLine();
    }

    /**
     * 根据路径创建文件及输出流
     */

    private BufferedWriter instanceBufferedWriter(String parent, String child) throws
            FileNotFoundException {
        File beanFile = new File(parent, child);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
    }

    /**
     * 根据路径创建目录
     */

    private void instanceFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    /**
     * 构建方法代码
     *
     * @param comment    方法注释
     * @param returnType 方法返回类型
     * @param name       方法名
     * @param param      方法参数
     */

    private void buildMethod(BufferedWriter bw, String comment, String returnType, String name, String
            param) throws IOException {
        buildMethodComment(bw, comment);
        bw.newLine();
        String result = format("\t{0} {1}({2});", returnType, name, param);
        bw.write(result);
        bw.newLine();
    }

    /**
     * 构建Dao文件
     */

    private void buildMapper() throws IOException {
        instanceFolder(MAPPER_PATH);
        BufferedWriter bw = instanceBufferedWriter(MAPPER_PATH, mapperName + ".java");
        writeMapperHead(bw);
        writeMethod(bw);
        writeEnd(bw);
    }

    /**
     * 写Mapper及Service中方法代码
     */

    private void writeMethod(BufferedWriter bw) throws IOException {
        buildMethod(bw, "查询（根据主键ID查询", beanName, queryById, "String id");
        buildMethod(bw, "删除（根据主键ID删除）", "int", delete, "String id");
        buildMethod(bw, "添加", "int", save, beanName + " " + lowerBeanName);
        buildMethod(bw, "修改", "int", update, beanName + " " + lowerBeanName);
    }

    /**
     * 写Mapper类头部代码
     */

    private void writeMapperHead(BufferedWriter bw) throws IOException {
        bw.write("package " + MAPPER_PACKAGE + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + BEAN_PACKAGE + "." + beanName + ";");
        bw.newLine();
        bw.write("import java.util.List;");
        bw.newLine();
        bw.write("import java.util.Map;");
        bw.newLine();
        bw.write("import org.apache.ibatis.annotations.Param;");
        buildClassComment(bw, mapperName + "数据库操作接口类");
        bw.newLine();
        bw.write("public interface " + mapperName + "{");
        bw.newLine();
    }

    /**
     * 构建Service文件
     */

    private void buildServie() throws IOException {
        instanceFolder(SERVICE_PATH);
        BufferedWriter bw = instanceBufferedWriter(SERVICE_PATH, serviceName + ".java");
        writeServiceHead(bw);
        writeMethod(bw);
        writeEnd(bw);
    }

    /**
     * 写service接口头部代码
     */

    private void writeServiceHead(BufferedWriter bw) throws IOException {
        bw.write("package " + SERVICE_PACKAGE + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + BEAN_PACKAGE + "." + beanName + ";");
        bw.newLine();
        bw.write("import java.util.List;");
        bw.newLine();
        bw.write("import java.util.Map;");
        bw.newLine();
        bw.write("import org.apache.ibatis.annotations.Param;");
        buildClassComment(bw, serviceName + "数据库操作接口类");
        bw.newLine();
        bw.write("public interface " + serviceName + " {");
        bw.newLine();
    }

    /**
     * 构建ServiceImpl文件
     */

    private void buildServieImpl() throws IOException {
        instanceFolder(SERVICE_PATH);
        BufferedWriter bw = instanceBufferedWriter(SERVICE_PATH, serviceImplName + ".java");
        writeServiceImplHead(bw);
        writeServieImplMethod(bw);
        writeEnd(bw);
    }

    /**
     * 写serveImpl中的方法
     */

    private void writeServieImplMethod(BufferedWriter bw) throws IOException {
        String lowerMapperName = lowerCaseFirstLitter(mapperName);
        buildServiceImplMethod(bw, beanName, queryById, "String id", lowerMapperName);
        buildServiceImplMethod(bw, "int", delete, "String id", lowerMapperName);
        buildServiceImplMethod(bw, "int", save, beanName + " " + lowerBeanName, lowerMapperName);
        buildServiceImplMethod(bw, "int", update, beanName + " " + lowerBeanName, lowerMapperName);
    }

    /**
     * 写serveImpl中的方法
     */

    private void buildServiceImplMethod(BufferedWriter bw, String returnType, String name, String
            param, String lowerMapperName) throws IOException {
        bw.write("\t@Override");
        bw.newLine();
        bw.write(format("\tpublic {0} {1}({2})", returnType, name, param));
        bw.write("{");
        bw.newLine();
        bw.write(format("\t\treturn {0}.{1}({2});", lowerMapperName, name.trim(), param.split(" ")[1]));
        bw.newLine();
        bw.write("\t}");
        bw.newLine();
        bw.newLine();
    }

    /**
     * 写serviceImpl头部代码
     */

    private void writeServiceImplHead(BufferedWriter bw) throws IOException {
        String lowerMapperName = lowerCaseFirstLitter(mapperName);
        bw.write("package " + SERVICEIMPL_PACKAGE + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import java.util.List;");
        bw.newLine();
        bw.write("import org.springframework.beans.factory.annotation.Autowired;");
        bw.newLine();
        bw.write("import org.springframework.stereotype.Service;");
        bw.newLine();
        bw.write("import " + BEAN_PACKAGE + "." + beanName + ";");
        bw.newLine();
        buildClassComment(bw, serviceImplName + "数据库操作接口类");
        bw.newLine();
        bw.newLine();
        bw.write("@Service");
        bw.newLine();
        bw.write("public class " + serviceImplName + " implements " + serviceName + " {");
        bw.newLine();
        bw.newLine();
        bw.write("\t@Autowired");
        bw.newLine();
        bw.write("\t        private " + mapperName + " " + lowerMapperName + ";        ");
        bw.newLine();
        bw.newLine();
    }

    /**
     * 获取所有的数据库表名及注释
     */

    private Map<String, String> getTableComment() throws SQLException {
        Map maps = new HashMap<>();
//全部表
        PreparedStatement pstate = conn.prepareStatement(showTablesInfo);
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString("NAME");
            String comment = results.getString("COMMENT");
            maps.put(tableName, comment);
        }
        return maps;
    }

    public static Boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    public static Boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 将字符串首字母小写
     */
    public static String lowerCaseFirstLitter(String str) {
        if (isEmpty(str)) {
            return "";
        } else {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
    }

    /**
     * 将字符串首字母大写
     */
    public static String upperCaseFirstLitter(String str) {
        if (isEmpty(str)) {
            return "";
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    /**
     * 根据某一个表生成实体类,service,mapper
     */

    private void generateByTable(Map tableComments, String table) throws SQLException, IOException {
        columns.clear();
        types.clear();
        comments.clear();
        PreparedStatement pstate = conn.prepareStatement(showFields + table);
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            columns.add(results.getString("FIELD"));
            types.add(results.getString("TYPE"));
            comments.add(results.getString("COMMENT"));
        }
        initNameByTable(table);
        String tableComment = (String) tableComments.get(table);
        buildEntityBean(columns, types, comments, tableComment);
        buildMapper();
        buildServie();
        buildServieImpl();
    }

    /**
     * 获取所有的表信息并循环生成相应文件
     */
    public void generate() throws ClassNotFoundException, SQLException, IOException {
        initConnection();
        Map<String, String> tableComments = getTableComment();
        if (isNotEmpty(TABLE_NAME)) {
            generateByTable(tableComments, TABLE_NAME);
        } else {
            List<String> tables = getTables();
            for (String table : tables) {
                generateByTable(tableComments, table);
            }
        }
        conn.close();
    }

    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.getRuntime();
            mkdirs(runtime);
            new GenerateJavaFileUtils().generate(); // 自动打开生成文件的目录
            runtime.exec("cmd /c start explorer " + PACKAGEPATH);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
