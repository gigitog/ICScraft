
package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class PlayerClick implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;

    public PlayerClick(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        Block b = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        // making a "list class" of quest worlds from file
        ListQuestWorldData listQuestWorldData = editor.getData();
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§9Вернуться в лобби §7(ПКМ)")){
                player.performCommand("spawn");
                return;
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aНачать квест §7(ПКМ)")) {
                player.performCommand("quest");
                return;
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aИнформация о квесте §7(ПКМ)")) {
                for (QuestWorldData questWorldData : listQuestWorldData.allQuestWorlds) {
                    if (questWorldData.playerName.equalsIgnoreCase(player.getName())) {
                        player.sendMessage(ChatColor.RED + "Квест №1 - не пройден.");
                        player.sendMessage(ChatColor.RED + "Квест №2 - не пройден.");
                        player.sendMessage(ChatColor.RED + "Квест №3 - не пройден.");
                        player.sendMessage(ChatColor.RED + "Квест №4 - не пройден.");
                        player.sendMessage(ChatColor.RED + "Квест №5 - не пройден.");
                        if(questWorldData.ticksPlayedFinal!=0){
                            player.sendMessage(ChatColor.GREEN + "Квест пройден за - " + questWorldData.ticksPlayedFinal /1200 + " мин., " + questWorldData.ticksPlayedFinal%1200/20 + " сек.");
                            return;
                        }else if(player.getWorld().getName().equalsIgnoreCase(questWorldData.questWorldName)){
                            player.sendMessage(ChatColor.GREEN + "Проведено времени в квесте - " + (questWorldData.ticksSavedBeforeLeaving + (player.getTicksLived() - questWorldData.ticksLivedWhenStart))/1200 + " мин, " + (questWorldData.ticksSavedBeforeLeaving + player.getTicksLived() - questWorldData.ticksLivedWhenStart)%1200/20 + " сек.");
                            return;
                        }
                        player.sendMessage(ChatColor.GREEN + "Проведено времени в квесте - " + questWorldData.ticksSavedBeforeLeaving/1200 + " мин, " + (questWorldData.ticksSavedBeforeLeaving%1200)/20 + " сек.");
                        return;
                    }
                }
                player.sendMessage(ChatColor.RED + "Вы не начинали квест!");
                return;
            }
        }
    }
}
