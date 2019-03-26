package edu.netcracker.backend.dao.sql;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j(topic = "log")
public class PostgresSqlBuilder implements SQLBuilder {

    private final PrimaryKey primaryKey;
    private final Map<Field, Attribute> fieldAttributeMap;

    private final String attributesSql;
    private final String tableName;
    private final String primaryKeyWhere;

    private final String SELECT_IN_SQL_TEMPLATE;

    @Getter
    private final String insertSql;
    @Getter
    private final String updateSql;
    @Getter
    private final String deleteSql;
    @Getter
    private final String existsSql;
    @Getter
    private final String selectSql;

    public PostgresSqlBuilder(Class<?> entityClass, PrimaryKey primaryKey, Map<Field, Attribute> fieldAttributeMap) {
        this.primaryKey = primaryKey;
        this.fieldAttributeMap = fieldAttributeMap;

        this.tableName = entityClass.getAnnotation(Table.class)
                                    .value();
        this.attributesSql = assembleAttributes();
        this.primaryKeyWhere = assemblePrimaryKeysWhere();

        this.SELECT_IN_SQL_TEMPLATE = preAssembleSelectInSql();

        this.insertSql = assembleInsertSql();
        this.updateSql = assembleUpdateSql();
        this.deleteSql = assembleDeleteSql();
        this.existsSql = assembleExistsSql();
        this.selectSql = assembleSelectSql();

        String entityName = entityClass.getSimpleName();
        log.debug("Generated insert sql for [{}]: [{}]", entityName, this.insertSql);
        log.debug("Generated update sql for [{}]: [{}]", entityName, this.updateSql);
        log.debug("Generated delete sql for [{}]: [{}]", entityName, this.deleteSql);
        log.debug("Generated exists sql for [{}]: [{}]", entityName, this.existsSql);
        log.debug("Generated select sql for [{}]: [{}]", entityName, this.existsSql);
    }

    private String assembleInsertSql() {
        StringBuilder sb = new StringBuilder();

        for (Attribute attribute : fieldAttributeMap.values()) {
            sb.append(attribute.value())
              .append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        String insertAttributes = sb.toString();

        sb = new StringBuilder();
        for (int i = 0; i < fieldAttributeMap.size(); i++) {
            sb.append("?, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        String values = sb.toString();

        String INSERT_SQL = "INSERT INTO :table_name (:insert_attributes) VALUES (:values)";
        return INSERT_SQL.replaceAll(":table_name", tableName)
                         .replaceAll(":insert_attributes", insertAttributes)
                         .replaceAll(":values", values);
    }

    private String assembleUpdateSql() {
        StringBuilder sb = new StringBuilder();
        for (Attribute attribute : fieldAttributeMap.values()) {
            sb.append(attribute.value())
              .append(" = ?, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        String updateAttributes = sb.toString();

        String UPDATE_SQL = "UPDATE :table_name SET :update_attr WHERE :primary_key";
        return UPDATE_SQL.replaceAll(":table_name", tableName)
                         .replaceAll(":update_attr", updateAttributes)
                         .replaceAll(":primary_key", primaryKeyWhere);
    }

    private String assembleDeleteSql() {
        String DELETE_SQL = "DELETE FROM :table_name WHERE :primary_key";
        return DELETE_SQL.replaceAll(":table_name", tableName)
                         .replaceAll(":primary_key", primaryKeyWhere);
    }

    private String assembleSelectSql() {
        String SELECT_SQL = "SELECT :attributes FROM :table_name WHERE :primary_key";
        return SELECT_SQL.replaceAll(":attributes", attributesSql)
                         .replaceAll(":table_name", tableName)
                         .replaceAll(":primary_key", primaryKeyWhere);
    }

    private String assembleExistsSql() {
        String EXISTS_SQL = "SELECT COUNT(*) FROM (SELECT :pk FROM :table_name WHERE :primary_key LIMIT 1) sub";
        return EXISTS_SQL.replaceAll(":pk", primaryKey.value())
                         .replaceAll(":table_name", tableName)
                         .replaceAll(":primary_key", primaryKeyWhere);
    }

    public String assembleVariableSelectInSql(int size) {
        if (size < 1) {
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("?, ");
        }
        sb.delete(sb.length() - 2, sb.length());

        return SELECT_IN_SQL_TEMPLATE.replaceAll(":in_list", sb.toString());
    }

    private String preAssembleSelectInSql() {
        String selectInSql = "SELECT :attributes FROM :table_name WHERE :pk IN (:in_list)";
        return selectInSql.replaceAll(":attributes", attributesSql)
                          .replaceAll(":table_name", tableName)
                          .replaceAll(":pk", primaryKey.value());
    }

    private String assemblePrimaryKeysWhere() {
        return primaryKey.value() + " = ?";
    }

    private String assembleAttributes() {
        StringBuilder sb = new StringBuilder();
        sb.append(primaryKey.value())
          .append(", ");
        for (Attribute attribute : fieldAttributeMap.values()) {
            sb.append(attribute.value())
              .append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}