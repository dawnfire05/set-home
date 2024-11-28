// HomeService.java
package org.setHome.setHome.service;

import org.bukkit.entity.Player;
import org.setHome.setHome.model.Home;

import java.util.List;

public interface HomeService {
    List<Home> getHomes(Player player);
    void addHome(Player player, Home home);
    void updateHome(Player player, Home home);
    void removeHome(Player player, String homeName);
    Home getHome(Player player, String homeName);
}