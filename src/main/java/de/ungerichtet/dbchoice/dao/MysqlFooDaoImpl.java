package de.ungerichtet.dbchoice.dao;

import de.ungerichtet.dbchoice.model.Foo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class MysqlFooDaoImpl extends SqlFooDao implements FooDao {

    public MysqlFooDaoImpl() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load mysql driver");
        }
    }

    @Override
    protected String getCreateFooQuery() {
        return "INSERT INTO foo (created, value) VALUES (?, ?)";
    }

    @Override
    protected void setCreateFooStatementValues(PreparedStatement statement, Foo foo) throws SQLException {

        statement.setLong(1, foo.getCreated().toEpochMilli());
        statement.setString(2, foo.getValue());
    }

    @Override
    protected String getSelectFooQuery() {

        return "SELECT id, created, value FROM foo";
    }

    @Override
    protected Foo createFooFromSelectQuery(ResultSet rs) throws SQLException {

        Foo foo = new Foo();
        foo.setId(rs.getLong(1));
        foo.setCreated(Instant.ofEpochMilli(rs.getLong(2)));
        foo.setValue(rs.getString(3));
        return foo;
    }

    @Override
    protected String getSelectFooByIdQuery() {
        return "SELECT id, created, value FROM foo WHERE id = ?";
    }

    @Override
    protected void setSelectFooByIdParams(PreparedStatement statement, long id) throws SQLException {
        statement.setLong(1, id);
    }

    @Override
    protected String getDeleteFooByIdQuery() {
        return "DELETE FROM foo WHERE id = ?";
    }

    @Override
    protected void setDeleteFooByIdParams(PreparedStatement statement, Foo foo) throws SQLException {
        statement.setLong(1, foo.getId());
    }

    @Override
    protected String getUpdateFooByIdQuery() {
        return "UPDATE foo SET value = ? WHERE id = ?";
    }

    @Override
    protected void setUpdateFooByIdParams(PreparedStatement statement, Foo foo) throws SQLException {

        statement.setString(1, foo.getValue());
        statement.setLong(2, foo.getId());
    }

    @Override
    protected String getCreateFooTableStatement() {
        return "create table if not exists foo ( " +
                " id bigint auto_increment, " +
                " created bigint not null, " +
                " value text, " +
                " constraint foo_pk primary key (id) " +
                ")";
    }

    @Override
    protected String getDbUrl() {
        return "jdbc:mysql://localhost:3306/foo?user=foo&password=bar";
    }
}
