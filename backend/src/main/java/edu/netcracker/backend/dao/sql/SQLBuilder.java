package edu.netcracker.backend.dao.sql;

public interface SQLBuilder {

    String assembleInsertSql();
    String assembleUpdateSql();
    String assembleDeleteSql();
    String assembleExistsSql();
    String assembleSelectSql();
    String assembleVariableSelectInSql(int size);
}
