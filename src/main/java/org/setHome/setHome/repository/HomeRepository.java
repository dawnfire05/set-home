package org.setHome.setHome.repository;

import org.setHome.setHome.model.Home;

import java.util.List;
import java.util.UUID;

public interface HomeRepository {
    List<Home> getHomes(UUID playerUUID);
    void addHome(UUID playerUUID, Home home);
    void removeHome(UUID playerUUID, String homeName);
    Home getHome(UUID playerUUID, String homeName);
    boolean isHomeNameTaken(UUID playerUUID, String homeName);
    void close();
}