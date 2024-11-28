package org.setHome.setHome.presentation.gui;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.persistence.PersistentDataType;
import org.setHome.setHome.Main;
import org.setHome.setHome.service.HomeService;
import org.setHome.setHome.model.Home;

public class HomeDetailGUI implements Listener {

    private final HomeService homeService;

    @Inject
    public HomeDetailGUI(HomeService homeService) {
        this.homeService = homeService;
    }

    public static void openHomeDetailGUI(Player player, String homeName) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.BLUE + homeName + " 설정");

        // 이름 수정 버튼
        ItemStack renameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = renameItem.getItemMeta();
        renameMeta.setDisplayName(ChatColor.YELLOW + "이름 수정");
        renameItem.setItemMeta(renameMeta);
        gui.addItem(renameItem);

        // 아이콘 변경 버튼
        ItemStack changeIconItem = new ItemStack(Material.ITEM_FRAME);
        ItemMeta iconMeta = changeIconItem.getItemMeta();
        iconMeta.setDisplayName(ChatColor.YELLOW + "아이콘 변경");
        changeIconItem.setItemMeta(iconMeta);
        gui.addItem(changeIconItem);

        // 삭제 버튼
        ItemStack deleteItem = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = deleteItem.getItemMeta();
        deleteMeta.setDisplayName(ChatColor.RED + "삭제");
        deleteItem.setItemMeta(deleteMeta);
        gui.addItem(deleteItem);

        // 뒤로가기 버튼
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(ChatColor.WHITE + "뒤로가기");
        backItem.setItemMeta(backMeta);
        gui.setItem(26, backItem); // 마지막 슬롯

        // 홈 이름을 메타 데이터로 저장
        renameItem.getItemMeta().getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "homeName"), PersistentDataType.STRING, homeName);

        player.openInventory(gui);
    }

    @EventHandler
    public void onHomeDetailClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().endsWith(" 설정")) return;

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        String homeName = ChatColor.stripColor(event.getView().getTitle()).replace(" 설정", "");

        if (clickedItem.getType() == Material.NAME_TAG) {
            // 이름 수정 로직 (예: 대화창을 통해 새 이름 받기)
            // 여기서는 예시로 이름 변경
            String newName = homeName + "_Renamed";
            homeService.removeHome(player, homeName);
            Home home = homeService.getHome(player, homeName);
            home.setName(newName);
            homeService.updateHome(player, home);
            player.sendMessage(ChatColor.GREEN + "집 이름이 변경되었습니다.");
            // 이전 GUI 열기
            HomesGUI.openHomesGUI(player);
        } else if (clickedItem.getType() == Material.ITEM_FRAME) {
            // 아이콘 변경 로직 (예: 다른 아이콘 설정)
            // 여기서는 예시로 아이콘 변경
            player.sendMessage(ChatColor.GREEN + "아이콘이 변경되었습니다.");
            // 실제 아이콘 변경 로직 구현 필요
        } else if (clickedItem.getType() == Material.BARRIER) {
            // 집 삭제 로직
            homeService.removeHome(player, homeName);
            player.sendMessage(ChatColor.RED + homeName + " 집이 삭제되었습니다.");
            // 이전 GUI 열기
            HomesGUI.openHomesGUI(player);
        } else if (clickedItem.getType() == Material.ARROW) {
            // 이전 GUI 열기
            HomesGUI.openHomesGUI(player);
        }
    }
}