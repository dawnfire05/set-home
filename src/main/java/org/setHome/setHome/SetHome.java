package org.setHome.setHome;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.setHome.setHome.command.CommandHandler;
import org.setHome.setHome.di.Module;
import org.setHome.setHome.gui.HomeDetailGUI;
import org.setHome.setHome.gui.HomesGUI;
import org.setHome.setHome.repository.HomeRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SetHome extends JavaPlugin {

    private static SetHome instance;
    private Connection connection;
    private Injector injector;

    @Override
    public void onEnable() {
        instance = this;
        connectToDatabase();
        setupInjector();

        try {
            // 의존성 주입을 통해 HomeRepository, HomeCommand, HomesGUI, HomeDetailGUI 가져오기
            HomeRepository homeRepository = injector.getInstance(HomeRepository.class);
            CommandHandler commandHandler = injector.getInstance(CommandHandler.class);
            HomesGUI homesGUI = injector.getInstance(HomesGUI.class);
            HomeDetailGUI homeDetailGUI = injector.getInstance(HomeDetailGUI.class);

            // 명령어 실행자 등록
            getCommand("homes").setExecutor(commandHandler);

            // 이벤트 리스너 등록
            getServer().getPluginManager().registerEvents(homesGUI, this);
            getServer().getPluginManager().registerEvents(homeDetailGUI, this);

            getLogger().info("SetHome 플러그인이 활성화되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("의존성 주입 중 오류가 발생했습니다.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (injector != null) {
            HomeRepository homeRepository = injector.getInstance(HomeRepository.class);
            homeRepository.close();
        }
        disconnectDatabase();
        getLogger().info("SetHome 플러그인이 비활성화되었습니다.");
    }


    private void connectToDatabase() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            String url = "jdbc:sqlite:" + getDataFolder().getPath() + "/homes.db";
            connection = DriverManager.getConnection(url);
            getLogger().info("SQLite 데이터베이스에 연결되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("SQLite 데이터베이스 연결에 실패했습니다.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void disconnectDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                getLogger().info("SQLite 데이터베이스 연결이 종료되었습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("SQLite 데이터베이스 연결 종료 중 오류가 발생했습니다.");
        }
    }

    private void setupInjector() {
        injector = Guice.createInjector(new Module(connection));
    }
    public Injector getInjector() {
        return injector;
    }

    public static SetHome getInstance() {
        return instance;
    }

    public Connection getDatabaseConnection() {
        return connection;
    }

}