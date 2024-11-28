package org.setHome.setHome.presentation.gui;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.setHome.setHome.service.HomeService;
import org.setHome.setHome.model.Home;

import java.util.List;

public class HomesGUI implements Listener {

    private static HomeService homeService;

    @Inject
    public HomesGUI(HomeService homeService) {
        this.homeService = homeService;
    }

    public static void openHomesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GREEN + "내 집 목록");

        List<Home> homes = homeService.getHomes(player);
        for (Home home : homes) {
            ItemStack homeItem = new ItemStack(Material.OAK_DOOR);
            ItemMeta meta = homeItem.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + home.getName());
            homeItem.setItemMeta(meta);
            gui.addItem(homeItem);
        }

        ItemStack addHomeItem = new ItemStack(Material.ANVIL);
        ItemMeta addMeta = addHomeItem.getItemMeta();
        addMeta.setDisplayName(ChatColor.YELLOW + "현재 위치에 집 추가");
        addHomeItem.setItemMeta(addMeta);
        gui.setItem(49, addHomeItem); // 마지막 슬롯

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.GREEN + "내 집 목록")) return;

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        if (clickedItem.getType() == Material.OAK_DOOR) {
            if (event.isLeftClick()) {
                Home home = homeService.getHome(player, itemName);
                if (home != null) {
                    player.teleport(home.getLocation());
                    player.sendMessage(ChatColor.GREEN + home.getName() + "으로 이동했습니다.");
                } else {
                    player.sendMessage(ChatColor.RED + "해당 집을 찾을 수 없습니다.");
                }
            } else if (event.isRightClick()) {
                HomeDetailGUI.openHomeDetailGUI(player, itemName);
            }
        } else if (clickedItem.getType() == Material.ANVIL) {
            if (event.isLeftClick()) {
                Home home = new Home("Home" + (homeService.getHomes(player).size() + 1), player.getLocation());
                // 집 이름, 아이콘 설정하는 로직
                homeService.addHome(player, home);
                openHomesGUI(player);
//                String homeName = "Home" + (homeService.getHomes(player.getUniqueId()).size() + 1);
//                if (!homeService.isHomeNameTaken(player.getUniqueId(), homeName)) {
//                    Home newHome = new Home(homeName, player.getLocation());
//                    homeService.addHome(player.getUniqueId(), newHome);
//                    player.sendMessage(ChatColor.GREEN + homeName + " 집이 추가되었습니다.");
//                    // GUI 새로 고침
//                    openHomesGUI(player);
//                } else {
//                    player.sendMessage(ChatColor.RED + "이미 존재하는 집 이름입니다.");
//                }
            } else if (event.isRightClick()) {
                Home home = new Home("Home" + (homeService.getHomes(player).size() + 1),player.getLocation());
                homeService.addHome(player, home);
                openHomesGUI(player);
//                // 빠르게 집 생성하는 로직 (예: 현재 위치에 자동 집 생성)
//                String homeName = "FastHome" + (homeService.getHomes(player.getUniqueId()).size() + 1);
//                if (!homeService.isHomeNameTaken(player.getUniqueId(), homeName)) {
//                    Home newHome = new Home(homeName, player.getLocation());
//                    homeService.addHome(player.getUniqueId(), newHome);
//                    player.sendMessage(ChatColor.GREEN + homeName + " 집이 빠르게 추가되었습니다.");
//                    // GUI 새로 고침
//                    openHomesGUI(player);
//                } else {
//                    player.sendMessage(ChatColor.RED + "이미 존재하는 집 이름입니다.");
//                }
            }
        }
    }
}