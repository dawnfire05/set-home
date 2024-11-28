// CommandHandler.java
package org.setHome.setHome.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.setHome.setHome.gui.HomesGUI;

public class CommandHandler implements CommandExecutor {

    private HomesGUI homesGUI;

    public CommandHandler(HomesGUI homesGUI) {
        this.homesGUI = homesGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            homesGUI.openHomesGUI(player);
        } else {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
        }
        return true;
    }
}