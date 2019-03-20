package de.ungerichtet.dbchoice.environment;

import com.google.inject.AbstractModule;
import de.ungerichtet.dbchoice.dao.FooDao;
import de.ungerichtet.dbchoice.dao.SqliteFooDaoImpl;

public class SqliteEnvironment extends AbstractModule {

    @Override
    protected void configure() {
        bind(FooDao.class).to(SqliteFooDaoImpl.class);
    }
}
