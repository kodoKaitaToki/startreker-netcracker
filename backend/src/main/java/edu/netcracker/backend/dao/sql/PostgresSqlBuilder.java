package edu.netcracker.backend.dao.sql;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;

import java.lang.reflect.Field;
import java.util.Map;

public class PostgresSqlBuilder implements SQLBuilder {

    private final Map<Field, PrimaryKey> fieldPrimaryKeyMap;
    private final Map<Field, Attribute> fieldAttributeMap;
    private final String selectInTemplateSql;
    private final String attributesSql;
    private final String tableName;

    public PostgresSqlBuilder(Class<?> entityClass,
                              Map<Field, PrimaryKey> fieldPrimaryKeyMap,
                              Map<Field, Attribute> fieldAttributeMap) {
        this.fieldPrimaryKeyMap = fieldPrimaryKeyMap;
        this.fieldAttributeMap = fieldAttributeMap;
        this.selectInTemplateSql = assembleSelectInSql();
        this.attributesSql = assembleAttributes();
        this.tableName = entityClass.getAnnotation(Table.class)
                                    .value();
    }

    public String assembleInsertSql() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName)
          .append(" (");
        for (Attribute attribute : fieldAttributeMap.values()) {
            sb.append(attribute.value())
              .append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(") VALUES(");
        for (int i = 0; i < fieldAttributeMap.size(); i++) {
            sb.append("?, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");
        return sb.toString();
    }

    public String assembleUpdateSql() {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableName)
          .append(" SET ");
        for (Attribute attribute : fieldAttributeMap.values()) {
            sb.append(attribute.value())
              .append(" = ?, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        addPrimaryKeysWhere(sb);
        return sb.toString();
    }

    public String assembleDeleteSql() {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(tableName);
        addPrimaryKeysWhere(sb);
        return sb.toString();
    }

    public String assembleSelectSql() {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(attributesSql)
          .append(" FROM ")
          .append(tableName);
        addPrimaryKeysWhere(sb);
        return sb.toString();
    }

    public String assembleExistsSql() {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM (SELECT ");
        sb.append(attributesSql)
          .append(" FROM ")
          .append(tableName);
        addPrimaryKeysWhere(sb);
        sb.append(" LIMIT 1) sub");
        System.out.println(sb.toString());
        return sb.toString();
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
        return selectInTemplateSql.replaceAll(":var", sb.toString());
    }

    private String assembleSelectInSql() {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(attributesSql)
          .append(" FROM ")
          .append(tableName)
          .append(" WHERE ");
        for (PrimaryKey primaryKey : fieldPrimaryKeyMap.values()) {
            sb.append(primaryKey.value())
              .append(" IN (:var) AND ");
        }
        sb.delete(sb.length() - 5, sb.length());
        return sb.toString();
    }

    private void addPrimaryKeysWhere(StringBuilder sb) {
        sb.append(" WHERE ");
        for (PrimaryKey primaryKey : fieldPrimaryKeyMap.values()) {
            sb.append(primaryKey.value())
              .append(" = ? AND ");
        }
        sb.delete(sb.length() - 5, sb.length());
    }

    private String assembleAttributes() {
        StringBuilder sb = new StringBuilder();
        for (PrimaryKey primaryKey : fieldPrimaryKeyMap.values()) {
            sb.append(primaryKey.value())
              .append(", ");
        }
        for (Attribute attribute : fieldAttributeMap.values()) {
            sb.append(attribute.value())
              .append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
