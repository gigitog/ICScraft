package me.ics.questplugin.QuestManager.Commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

// Test class
public class Answer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(args.length != 0) {
            if (args[0].equalsIgnoreCase("ICS")) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                Villager Yura = (Villager)player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
                Yura.setOp(true);
                Yura.setCustomName("Yura");
                Yura.setBaby();
                Yura.setTarget(player);
                return true;
            }
            if (args[0].equalsIgnoreCase("cat")) {
                Cat cat = (Cat)player.getWorld().spawnEntity(player.getLocation(),EntityType.CAT);
                cat.setCatType(Cat.Type.ALL_BLACK);
                cat.setAI(false);
                return true;
            }
        }player.sendMessage("Неправильный ответ!");
        return true;
    }
}
