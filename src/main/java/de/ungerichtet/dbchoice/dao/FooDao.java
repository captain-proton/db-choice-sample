package de.ungerichtet.dbchoice.dao;

import de.ungerichtet.dbchoice.model.Foo;

import java.sql.SQLException;
import java.util.List;

/**
 * Simple CRUD data access object interface to interact with
 * the chosen environment. Before you interact with the database
 * be sure to call {@link #setup()}, otherwise teh dao will
 * complain that no structure was found.
 */
public interface FooDao {

    /**
     * Executes table structure setup in the database.
     */
    void setup() throws SQLException;

    /**
     * Create a new {@link Foo} inside the database.
     *
     * @return The updated Foo (with identifier).
     */
    Foo createFoo(Foo foo) throws SQLException;

    /**
     * Select all {@link Foo} objects inside the database.
     *
     * @return A list with all foo objects or an empty list if
     * there are none
     */
    List<Foo> selectFoo() throws SQLException;

    /**
     * Select the foo with given id.
     *
     * @return The found foo or <code>null</code>
     */
    Foo selectFooById(long id) throws SQLException;

    /**
     * Delete the foo with given id.
     *
     * @return The deleted foo or null if none was deleted
     */
    Foo deleteFoo(long id) throws SQLException;

    /**
     * Updates given foos values.
     */
    void updateFoo(Foo foo) throws SQLException;
}
