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
        int index = 0;
        for(QuestWorldData questWorldData : listQuestWorldData.allQuestWorlds){
            // если мир зянят, игрок есть игрок, который прохожит
            // то проверяем чекпоинты
            if(questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(player.getName())) {
                if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§aИнформация о квесте")){
                    player.sendMessage(ChatColor.RED + "Квест №1 - не пройден.");
                    player.sendMessage(ChatColor.RED + "Квест №2 - не пройден.");
                    player.sendMessage(ChatColor.RED + "Квест №3 - не пройден.");
                    player.sendMessage(ChatColor.RED + "Квест №4 - не пройден.");
                    player.sendMessage(ChatColor.RED + "Квест №5 - не пройден.");
                    player.sendMessage(ChatColor.GREEN + "Время в квесте - " + (questWorldData.ticksSavedBeforeLeaving + player.getTicksLived() - questWorldData.ticksLivedWhenStart)/1200 + " мин, " + (questWorldData.ticksSavedBeforeLeaving + player.getTicksLived() - questWorldData.ticksLivedWhenStart)%1200/20 + " сек.");
                }
                int checkpoint = questWorldData.checkpoint;
                java.util.List<Integer> validValues = Arrays.asList(67,68,69);
                if(checkpoint == 0 && b.getX()==227 && (b.getZ()==415 || b.getZ()==416) && (validValues.contains(b.getY()))){
                    if(item.getType().equals(Material.TRIPWIRE_HOOK)){
                        for(int y = 67; y < 70; y++){
                            for(int z = 415; z < 417; z++){
                                Location l = new Location(Bukkit.getWorld(questWorldData.questWorldName),227,y,z);
                                l.getBlock().setType(Material.AIR);
                            }
                        }
                        player.sendMessage(ChatColor.GRAY + "Отлично!");
                        player.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,10,1);
                        questWorldData.checkpoint = 1;
                        item.setAmount(0);
                        new RewriteDataInCycle().rewrite(index, questWorldData, editor, true);
                    }else{
                        player.sendMessage(ChatColor.GRAY + "Надо найти ключ или альтернативный выход");
                    }
                }
            }
        }
    }
}
