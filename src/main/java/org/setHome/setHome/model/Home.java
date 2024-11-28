// Home.java
package org.setHome.setHome.model;

import org.bukkit.Location;
import org.bukkit.Material;

public class Home {
    private String name;
    private Location location;
//    private Material icon;

    public Home(String name, Location location) {
        this.name = name;
        this.location = location;
//        this.icon = icon;
    }

    // 게터와 세터 메서드들
    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

//    public Material getIcon() {
//        return icon;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

//    public void setIcon(Material icon) {
//        this.icon = icon;
//    }
}