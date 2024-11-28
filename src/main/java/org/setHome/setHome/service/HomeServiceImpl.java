// HomeServiceImpl.java
package org.setHome.setHome.service;

import org.bukkit.entity.Player;
import org.setHome.setHome.model.Home;

import java.util.*;

public class HomeServiceImpl implements HomeService {

    private Map<UUID, List<Home>> playerHomes = new HashMap<>();

    public HomeServiceImpl() {
        loadHomes();
    }

    @Override
    public List<Home> getHomes(Player player) {
        return playerHomes.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    @Override
    public void addHome(Player player, Home home) {
        playerHomes.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(home);
    }

    @Override
    public void removeHome(Player player, String homeName) {
        List<Home> homes = getHomes(player);
        homes.removeIf(home -> home.getName().equalsIgnoreCase(homeName));
    }

    @Override
    public Home getHome(Player player, String homeName) {
        for (Home home : getHomes(player)) {
            if (home.getName().equalsIgnoreCase(homeName)) {
                return home;
            }
        }
        return null;
    }

    @Override
    public void saveHomes() {
        // 파일 또는 데이터베이스에 집 정보를 저장하는 로직
    }

    @Override
    public void loadHomes() {
        // 파일 또는 데이터베이스에서 집 정보를 불러오는 로직
    }
}