package edu.netcracker.backend.dao.sql;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;

import java.lang.reflect.Field;
import java.util.Map;

public class PostgresSqlBuilder implements SQLBuilder {

    private Class<?> clazz;
    private Map<Field, PrimaryKey> fieldPrimaryKeyMap;
    private Map<Field, Attribute> fieldAttributeMap;
    private String selectInTemplateSql;

    public PostgresSqlBuilder(Class<?> clazz, Map<Field, PrimaryKey> fieldPrimaryKeyMap, Map<Field, Attribute> fieldAttributeMap) {
        this.clazz = clazz;
        this.fieldPrimaryKeyMap = fieldPrimaryKeyMap;
        this.fieldAttributeMap = fieldAttributeMap;
        this.selectInTemplateSql = assembleSelectInSql();
    }

    public String assembleInsertSql() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(clazz.getAnnotation(Table.class).value())
                .append(" (");
        for (Attribute attribute : fieldAttributeMap.values()) {
            sb.append(attribute.value()).append(", ");
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
        sb.append(clazz.getAnnotation(Table.class).value())
                .append(" SET ");
        for (Attribute attribute : fieldAttributeMap.values()) {
            sb.append(attribute.value()).append(" = ?, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        addPrimaryKeysWhere(sb);
        return sb.toString();
    }

    public String assembleDeleteSql() {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(clazz.getAnnotation(Table.class).value());
        addPrimaryKeysWhere(sb);
        return sb.toString();
    }

    public String assembleSelectSql() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(clazz.getAnnotation(Table.class).value());
        addPrimaryKeysWhere(sb);
        return sb.toString();
    }

    public String assembleExistsSql() {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM ");
        sb.append(clazz.getAnnotation(Table.class).value());
        addPrimaryKeysWhere(sb);
        return sb.toString();
    }

    public String assembleVariableSelectInSql(int size) {
        if(size < 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("?, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return selectInTemplateSql.replaceAll(":var", sb.toString());
    }

    private String assembleSelectInSql() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(clazz.getAnnotation(Table.class).value());
        sb.append(" WHERE ");
        for (PrimaryKey primaryKey : fieldPrimaryKeyMap.values()) {
            sb.append(primaryKey.value()).append(" IN (:var) AND ");
        }
        sb.delete(sb.length() - 5, sb.length());
        return sb.toString();
    }

    private void addPrimaryKeysWhere(StringBuilder sb) {
        sb.append(" WHERE ");
        for (PrimaryKey primaryKey : fieldPrimaryKeyMap.values()) {
            sb.append(primaryKey.value()).append(" = ? AND ");
        }
        sb.delete(sb.length() - 5, sb.length());
    }

}
