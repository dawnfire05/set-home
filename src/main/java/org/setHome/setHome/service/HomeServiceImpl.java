// HomeServiceImpl.java
package org.setHome.setHome.service;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.setHome.setHome.model.Home;
import org.setHome.setHome.repository.HomeRepository;

import java.util.*;

public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;

    @Inject
    HomeServiceImpl(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Override
    public List<Home> getHomes(Player player) {
        return homeRepository.getHomes(player.getUniqueId());
    }

    @Override
    public void addHome(Player player, Home home) {
        // 집이 이미 있는지 확인하는 로직 필요.
        if(home.getName() != null){
            homeRepository.addHome(player.getUniqueId(), home);
        } else{
            home.setName("Home" + homeRepository.getHomes(player.getUniqueId()).size()+1);
            homeRepository.addHome(player.getUniqueId(), home);
        }
        player.sendMessage("집이 저장되었습니다.");
    }


    @Override
    public void updateHome(Player player, Home home, String newName) {
        homeRepository.updateName(player.getUniqueId(), home, newName);
    }

    @Override
    public void removeHome(Player player, String homeName) {
        homeRepository.removeHome(player.getUniqueId(), homeName);
    }

    @Override
    public Home getHome(Player player, String homeName) {
        return homeRepository.getHome(player.getUniqueId(), homeName);
    }

}