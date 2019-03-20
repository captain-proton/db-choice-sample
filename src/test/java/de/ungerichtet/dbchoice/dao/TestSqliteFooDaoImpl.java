package de.ungerichtet.dbchoice.dao;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.ungerichtet.dbchoice.environment.SqliteEnvironment;
import de.ungerichtet.dbchoice.model.Foo;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;

public class TestSqliteFooDaoImpl {

    private FooDao fooDao;

    @BeforeClass
    public void setupSqliteTestEnvironment() {
        Injector injector = Guice.createInjector(new SqliteEnvironment());
        fooDao = injector.getInstance(FooDao.class);
    }

    @Test
    public void testInvalidCreate() {
        Foo foo = new Foo();
        foo.setCreated(null);

        Assert.assertThrows(() -> fooDao.createFoo(foo));
    }

    @Test
    public void testCreate() {
        Foo foo = new Foo("foo");
        try {
            Foo createdFoo = fooDao.createFoo(foo);
            Assert.assertEquals(foo, createdFoo);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(dependsOnMethods = {"testCreate"})
    public void testSelect() {
        try {
            List<Foo> foos = fooDao.selectFoo();
            Assert.assertNotEquals(0, foos.size());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(dependsOnMethods = {"testSelect"})
    public void testUpdate() {
        try {
            List<Foo> foos = fooDao.selectFoo();
            Foo foo = foos.get(0);
            foo.setValue("bar");
            fooDao.updateFoo(foo);

            Foo foo2 = fooDao.selectFooById(foo.getId());
            Assert.assertEquals(foo, foo2);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(dependsOnMethods = {"testUpdate"})
    public void testDelete() {
        try {
            List<Foo> foos = fooDao.selectFoo();
            for (Foo f : foos) {

                fooDao.deleteFoo(f.getId());
            }

            foos = fooDao.selectFoo();
            Assert.assertEquals(foos.size(), 0);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }
}
