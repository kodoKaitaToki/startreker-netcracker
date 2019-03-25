package edu.netcracker.backend.dao.sql;

public interface SQLBuilder {

    String getInsertSql();
    String getUpdateSql();
    String getDeleteSql();
    String getExistsSql();
    String getSelectSql();
    String assembleVariableSelectInSql(int size);
}
