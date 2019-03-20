package de.ungerichtet.dbchoice.dao;

import de.ungerichtet.dbchoice.model.Foo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class SqliteFooDaoImpl extends SqlFooDao implements FooDao {

    private final DateTimeFormatter FORMAT = DateTimeFormatter.ISO_INSTANT;

    @Override
    protected String getCreateFooQuery() {
        return "INSERT INTO foo (created, value) VALUES (?, ?)";
    }

    @Override
    protected void setCreateFooStatementValues(PreparedStatement statement, Foo foo) throws SQLException {

        statement.setString(1, FORMAT.format(foo.getCreated()));
        statement.setString(2, foo.getValue());
    }

    @Override
    protected String getSelectFooQuery() {
        return "SELECT id, created, value FROM foo";
    }

    @Override
    protected Foo createFooFromSelectQuery(ResultSet rs) throws SQLException {
        Foo foo = new Foo();

        Instant created = Instant.from(FORMAT.parse(rs.getString(2)));

        foo.setId(rs.getLong(1));
        foo.setCreated(created);
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
    protected String getDbUrl() {
        return "jdbc:sqlite:foo.db";
    }

    @Override
    protected String getCreateFooTableStatement() {
        return "CREATE TABLE IF NOT EXISTS foo (" +
                " id INTEGER PRIMARY KEY," +
                " created TEXT," +
                " value TEXT" +
                " )";
    }
}
