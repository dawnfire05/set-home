package org.setHome.setHome.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.setHome.setHome.gui.HomesGUI;
import org.setHome.setHome.repository.HomeRepository;
import org.setHome.setHome.repository.HomeRepositoryImpl;

import java.sql.Connection;


public class Module extends AbstractModule {
    private final Connection connection;

    public Module(Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void configure() {
        bind(HomeRepository.class).to(HomeRepositoryImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public Connection provideConnection() {
        return connection;
    }

}
