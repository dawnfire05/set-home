// HomesGUI.java
package org.setHome.setHome.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.setHome.setHome.model.Home;
import org.setHome.setHome.service.HomeService;

import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class HomesGUI implements Listener {

    private final HomeService homeService;

    public HomesGUI(HomeService homeService) {
        this.homeService = homeService;
    }

    public void openHomesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GREEN + "내 집 목록");

        // 집 목록 불러오기
        List<Home> homes = homeService.getHomes(player);

        if (homes != null && !homes.isEmpty()) {
            for (Home home : homes) {
                ItemStack homeItem = new ItemStack(home.getIcon());
                ItemMeta meta = homeItem.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + home.getName());
                homeItem.setItemMeta(meta);
                gui.addItem(homeItem);
            }
        }

        // 집 추가 버튼
        ItemStack addHomeItem = new ItemStack(Material.EMERALD);
        ItemMeta addMeta = addHomeItem.getItemMeta();
        addMeta.setDisplayName(ChatColor.YELLOW + "현재 위치에 집 추가");
        addHomeItem.setItemMeta(addMeta);
        gui.setItem(49, addHomeItem);

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "내 집 목록")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            if (clickedItem.getType() == Material.EMERALD) {
                getLogger().info("집 추가");
                if(event.isLeftClick()){
                    homeService.addHome(player, new Home("집 1", player.getLocation(), Material.ACACIA_BOAT));
                }else{

                }
                // 집 추가 로직
                player.closeInventory();
                player.sendMessage(ChatColor.YELLOW + "집 이름을 입력해주세요.");
                // 채팅 이벤트를 통해 이름과 아이콘을 설정하도록 구현
            } else {
                Home home = homeService.getHome(player, itemName);
                if (home != null) {
                    if (event.isLeftClick()) {
                        // 집으로 이동
                        player.teleport(home.getLocation());
                        player.sendMessage(ChatColor.GREEN + home.getName() + "으로 이동했습니다.");
                    } else if (event.isRightClick()) {
                        // 집 설정 GUI 열기
                        openHomeSettingsGUI(player, home);
                    }
                }
            }
        } else if (event.getView().getTitle().endsWith(" 설정")) {
            // 집 설정 GUI 클릭 처리
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String homeName = ChatColor.stripColor(event.getView().getTitle().replace(" 설정", ""));
            Home home = homeService.getHome(player, homeName);

            if (home == null) return;

            String action = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            switch (action) {
                case "이름 수정":
                    // 이름 수정 로직
                    player.closeInventory();
                    player.sendMessage(ChatColor.YELLOW + "새로운 이름을 입력해주세요.");
                    // 채팅 이벤트로 처리
                    break;
                case "아이콘 변경":
                    // 아이콘 변경 로직
                    player.closeInventory();
                    player.sendMessage(ChatColor.YELLOW + "새로운 아이콘의 아이템명을 입력해주세요.");
                    // 채팅 이벤트로 처리
                    break;
                case "삭제":
                    // 집 삭제
                    homeService.removeHome(player, homeName);
                    player.sendMessage(ChatColor.RED + homeName + " 집이 삭제되었습니다.");
                    openHomesGUI(player);
                    break;
                case "뒤로가기":
                    // 이전 GUI로 돌아가기
                    openHomesGUI(player);
                    break;
            }
        }
    }

    public void openHomeSettingsGUI(Player player, Home home) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.BLUE + home.getName() + " 설정");

        // 이름 수정 버튼
        ItemStack renameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = renameItem.getItemMeta();
        renameMeta.setDisplayName(ChatColor.YELLOW + "이름 수정");
        renameItem.setItemMeta(renameMeta);
        gui.setItem(11, renameItem);

        // 아이콘 변경 버튼
        ItemStack changeIconItem = new ItemStack(Material.ITEM_FRAME);
        ItemMeta iconMeta = changeIconItem.getItemMeta();
        iconMeta.setDisplayName(ChatColor.YELLOW + "아이콘 변경");
        changeIconItem.setItemMeta(iconMeta);
        gui.setItem(13, changeIconItem);

        // 삭제 버튼
        ItemStack deleteItem = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = deleteItem.getItemMeta();
        deleteMeta.setDisplayName(ChatColor.RED + "삭제");
        deleteItem.setItemMeta(deleteMeta);
        gui.setItem(15, deleteItem);

        // 뒤로가기 버튼
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(ChatColor.WHITE + "뒤로가기");
        backItem.setItemMeta(backMeta);
        gui.setItem(26, backItem);

        player.openInventory(gui);
    }
}