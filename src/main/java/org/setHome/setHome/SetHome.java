package org.setHome.setHome;

import org.bukkit.plugin.java.JavaPlugin;
import org.setHome.setHome.service.HomeService;
import org.setHome.setHome.service.HomeServiceImpl;
import org.setHome.setHome.gui.HomesGUI;
import org.setHome.setHome.command.CommandHandler;


public final class SetHome extends JavaPlugin {

    private HomeService homeService;
    private HomesGUI homesGUI;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {

        // 서비스와 GUI 생성
        this.homeService = new HomeServiceImpl();
        this.homesGUI = new HomesGUI(homeService);
        this.commandHandler = new CommandHandler(homesGUI);

        homeService.loadHomes();

        // 명령어 실행자 등록
        getCommand("homes").setExecutor(commandHandler);

        // 이벤트 리스너 등록
        getServer().getPluginManager().registerEvents(homesGUI, this);
    }

    @Override
    public void onDisable() {
        // 플러그인 종료 시 데이터 저장
        homeService.saveHomes();
    }
}