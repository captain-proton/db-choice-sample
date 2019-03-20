package de.ungerichtet.dbchoice.dao;

import de.ungerichtet.dbchoice.model.Foo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * This one is for the architects. Each dao implementation against
 * a sql database looks similar. It might look like a base class is
 * the best idea to handle common operations. The structure is simple.
 * </p>
 *
 * <pre>
 *              FooDao
 *                |
 *            SqlFooDao
 *            /       \
 * MysqlFooDaoImpl   SqliteFooDaoImpl
 * </pre>
 *
 * <p>
 * The <code>SqlFooDao</code> encloses common sql behaviour like
 * open and close database connections. The benefit is a little bit
 * more dry code. There are duplicates like
 * {@link MysqlFooDaoImpl#getSelectFooQuery()} and
 * {@link SqliteFooDaoImpl#getSelectFooQuery()}. To get rid of these
 * duplicates you could easily put the into this abstract class.
 * This is not done because the table structure is based on each
 * dao implementation class. If you put these methods into this class
 * it is bound to them and that makes the abstraction unnecessary.
 * </p>
 * <p>
 * The {@link FooDao} defines all crud methods on a high level.
 * To access a database a sql query is needed and all used
 * parameters must be bound in an {@link PreparedStatement}.
 * These actions are normally separated into two calls like
 * {@link #getSelectFooQuery()} followed by
 * {@link #setSelectFooByIdParams(PreparedStatement, long)}.
 * </p>
 */
public abstract class SqlFooDao implements FooDao {

    @Override
    public void setup() throws SQLException {
        String query = getCreateFooTableStatement();
        execute(con -> {

            try (PreparedStatement statement = con.prepareStatement(query)) {

                statement.execute();

            } catch (SQLException e) {
                System.err.println("Could not create foo table; cause: " + e.getMessage());
            }
            return null;
        });
    }

    @Override
    public Foo createFoo(Foo foo) throws SQLException {

        String query = getCreateFooQuery();

        return execute(con -> {

            try (PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                setCreateFooStatementValues(statement, foo);
                statement.execute();

                // retrieve generated id and set in pojo
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                long id = generatedKeys.getLong(1);
                foo.setId(id);
                return foo;

            } catch (SQLException e) {
                System.err.println("Could not create foo '" + foo + "'; cause: " + e.getMessage());
            }
            return null;
        });
    }

    protected abstract String getCreateFooQuery();

    protected abstract void setCreateFooStatementValues(PreparedStatement statement, Foo f) throws SQLException;

    @Override
    public List<Foo> selectFoo() throws SQLException {

        String query = getSelectFooQuery();

        return execute(con -> {

            List<Foo> result = new ArrayList<>();

            try (PreparedStatement statement = con.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    result.add(createFooFromSelectQuery(rs));
                }
            } catch (SQLException e) {
                System.err.println("Could not read from foo; cause: " + e.getMessage());
            }
            return result;
        });
    }

    protected abstract String getSelectFooQuery();

    protected abstract Foo createFooFromSelectQuery(ResultSet rs) throws SQLException;

    @Override
    public Foo selectFooById(long id) throws SQLException {

        String query = getSelectFooByIdQuery();

        return execute(con -> {

            Foo foo = null;

            try (PreparedStatement statement = con.prepareStatement(query)) {

                setSelectFooByIdParams(statement, id);
                statement.execute();

                ResultSet rs = statement.getResultSet();
                if (rs.next()) {
                    foo = createFooFromSelectQuery(rs);
                }
            } catch (SQLException e) {
                System.err.println("Could not get foo with id '" + id + "'; cause: " + e.getMessage());
            }
            return foo;
        });
    }

    protected abstract String getSelectFooByIdQuery();

    protected abstract void setSelectFooByIdParams(PreparedStatement statement, long id) throws SQLException;

    @Override
    public Foo deleteFoo(long id) throws SQLException {
        String query = getDeleteFooByIdQuery();

        return execute(con -> {

            try (PreparedStatement statement = con.prepareStatement(query)) {

                Foo foo = selectFooById(id);
                setDeleteFooByIdParams(statement, foo);
                statement.executeUpdate();
                return foo;

            } catch (SQLException e) {
                System.err.println("Could not delete foo with id '" + id + "'; cause: " + e.getMessage());
            }
            return null;
        });
    }

    protected abstract String getDeleteFooByIdQuery();

    protected abstract void setDeleteFooByIdParams(PreparedStatement statement, Foo foo) throws SQLException;


    @Override
    public void updateFoo(Foo foo) throws SQLException {

        String query = getUpdateFooByIdQuery();
        execute(con -> {

            try (PreparedStatement statement = con.prepareStatement(query)) {

                setUpdateFooByIdParams(statement, foo);

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Could not update foo " + foo.getId() + "; cause: " + e.getMessage());
            }
            return null;
        });
    }

    protected abstract String getUpdateFooByIdQuery();

    protected abstract void setUpdateFooByIdParams(PreparedStatement statement, Foo foo) throws SQLException;

    private <T> T execute(Function<Connection, T> f) throws SQLException {
        T result;
        try (Connection con = DriverManager.getConnection(getDbUrl())) {
            result = f.apply(con);
        }
        return result;
    }

    protected abstract String getCreateFooTableStatement();

    protected abstract String getDbUrl();
}
