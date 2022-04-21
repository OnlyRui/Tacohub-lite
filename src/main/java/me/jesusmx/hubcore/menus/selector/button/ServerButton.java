package me.jesusmx.hubcore.menus.selector.button;

import lombok.AllArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.bukkit.ItemBuilder;
import me.jesusmx.hubcore.util.buttons.Button;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ServerButton extends Button {

    private String server;
    private final ConfigFile config = TacoHubLite.getInstance().getSelectorConfig();

    @Override
    public ItemStack getItem(Player player) {
        return
                new ItemBuilder(Material.valueOf(config.getString(d("item"))), (byte) Integer.parseInt(config.getString(d("data"))))
                        .name(CC.translate(config.getString(d("name"))))
                        .lore(CC.translate(PlaceholderAPI.setPlaceholders(player, config.getStringList(d("lore")))))
                        .build();
    }

    private String d(String a) {
        return "server-selector.items." + server + "." + a;
    }
}

