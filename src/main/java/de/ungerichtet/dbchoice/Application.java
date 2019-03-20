package de.ungerichtet.dbchoice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.ungerichtet.dbchoice.dao.FooDao;
import de.ungerichtet.dbchoice.environment.MysqlEnvironment;
import de.ungerichtet.dbchoice.environment.SqliteEnvironment;
import de.ungerichtet.dbchoice.model.Foo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class Application {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {

        /*
        Choose between sqlite and mysql environment. Inside the grade.build
        file one task is defined for this purpose to run against mysql.
        The environment defines which dao class is used.

        ./gradew run        <- use sqlite
        ./gradew runMysql   <- use mysql

        Google Guice is used as dependency injector. There is no default
        implementation of JSR330 inside JSE. This is only found on JEE :(.

        https://github.com/google/guice/wiki/GettingStarted
         */
        Injector injector = args.length > 0 && args[0].equals("mysql")
                ? Guice.createInjector(new MysqlEnvironment())
                : Guice.createInjector(new SqliteEnvironment());

        FooDao fooDao = injector.getInstance(FooDao.class);
        LOG.info(format("Using", fooDao.getClass().getSimpleName()));

        try {
            fooDao.setup();
            LOG.info("Setup called");

            /*
            Produces an output like (trimmed log4j2)

                           Using: SqliteFooDaoImpl
            Created foo table
                     Created foo: Foo{id=1, created=2019-03-20T08:50:44.501Z, value='Hello foo'}
                           Count: 1
                   Selected foo2: Foo{id=1, created=2019-03-20T08:50:44.501Z, value='Hello foo'}
                foo.equals(foo2): true
                     foo == foo2: false
                     Updated foo: Foo{id=1, created=2019-03-20T08:50:44.501Z, value='Hello bar'}
                foo.equals(foo2): false
                     Deleted foo: Foo{id=1, created=2019-03-20T08:50:44.501Z, value='Hello bar'}
                           Count: 0


             */
            Foo foo = fooDao.createFoo(new Foo("Hello for"));
            LOG.info(format("Created 'foo'", foo));

            List<Foo> foos = fooDao.selectFoo();
            LOG.info(format("Count", foos.size()));

            Foo foo2 = fooDao.selectFooById(foo.getId());
            LOG.info(format("Selected 'foo2", foo2));
            LOG.info(format("foo.equals(foo2)", foo.equals(foo2)));
            LOG.info(format("foo == foo2", (foo == foo2)));

            String newValue = "Hello bar";
            foo.setValue(newValue);
            fooDao.updateFoo(foo);
            LOG.info(format("Updated foo", foo));
            LOG.info(format("foo.equals(foo2)", foo.equals(foo2)));

            fooDao.deleteFoo(foo.getId());
            LOG.info(format("Deleted foo", foo));

            foos = fooDao.selectFoo();
            LOG.info(format("Count", foos.size()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String format(String key, Object value) {
        return String.format("%20s: %s", key, value);
    }
}
