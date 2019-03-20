package de.ungerichtet.dbchoice.environment;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.ungerichtet.dbchoice.dao.FooDao;
import de.ungerichtet.dbchoice.dao.MysqlFooDaoImpl;

public class MysqlEnvironment extends AbstractModule {

    @Override
    protected void configure() {

        bind(FooDao.class).to(MysqlFooDaoImpl.class).in(Singleton.class);
    }
}
