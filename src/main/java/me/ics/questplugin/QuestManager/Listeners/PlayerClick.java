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
//                if(checkpoint == 4){
//                    java.util.List<Integer> NoteBlocksX = Arrays.asList(259,261,263,265);
//                    if(b.getY()==48 && b.getZ()==240 && (NoteBlocksX.contains(b.getX()))){
//                        Location l = player.getLocation();
//                        l.setX(259);l.setY(48);l.setZ(240);
//                        questWorldData.sideQuestOne.first = (NoteBlock)l.getBlock().getBlockData();
//                        l.setX(261);l.setY(48);l.setZ(240);
//                        questWorldData.sideQuestOne.second = (NoteBlock)l.getBlock().getBlockData();
//                        l.setX(263);l.setY(48);l.setZ(240);
//                        questWorldData.sideQuestOne.third = (NoteBlock)l.getBlock().getBlockData();
//                        l.setX(265);l.setY(48);l.setZ(240);
//                        questWorldData.sideQuestOne.fourth = (NoteBlock)l.getBlock().getBlockData();
//                        player.sendMessage(questWorldData.sideQuestOne.first.getNote().getTone().toString() + questWorldData.sideQuestOne.second.getNote().getTone().toString() + questWorldData.sideQuestOne.third.getNote().getTone().toString() + questWorldData.sideQuestOne.fourth.getNote().getTone().toString());
//                        if((questWorldData.sideQuestOne.first.getNote().getTone().toString() + questWorldData.sideQuestOne.second.getNote().getTone().toString() + questWorldData.sideQuestOne.third.getNote().getTone().toString() + questWorldData.sideQuestOne.fourth.getNote().getTone().toString()).equalsIgnoreCase("FACE")){
//                            questWorldData.checkpoint=5;
//                            player.sendMessage(ChatColor.GREEN + "Отлично! Ты справился :)");
//                            questWorldData.sideQuestOne.ticksEnd =player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart;
//                        }
//                    }
//                }
            }
        }
    }
}
