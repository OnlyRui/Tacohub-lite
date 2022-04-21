package me.jesusmx.hubcore.util.bukkit;

import com.google.common.base.Preconditions;
import me.jesusmx.hubcore.util.CC;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemBuilder {

    private final ItemStack stack;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, byte id) {
        this.stack = new ItemStack(material, 1, (short) 0, id);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, (byte) 0);
    }

    public ItemBuilder(ItemStack stack) {
        Preconditions.checkNotNull(stack, "ItemStack cannot be null");
        this.stack = stack;
    }

    public ItemBuilder(Material material, int amount, byte data) {
        Preconditions.checkNotNull(material, "Material cannot be null");
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        this.stack = new ItemStack(material, amount, data);
    }

    public ItemBuilder name(String name) {
        if (this.meta == null) {
            this.meta = stack.getItemMeta();
        }

        meta.setDisplayName(CC.translate(name));
        return this;
    }

    public ItemBuilder lore(List<String> list) {
        if (this.meta == null) {
            this.meta = stack.getItemMeta();
        }

        meta.setLore(CC.translate(list));
        return this;
    }

    public ItemBuilder lore(String... lore) {
        if (this.meta == null) {
            this.meta = stack.getItemMeta();
        }

        meta.setLore(CC.translate(Arrays.asList(lore)));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (this.meta == null) {
            this.meta = stack.getItemMeta();
        }

        meta.setLore(CC.translate(lore));
        return this;
    }

    public ItemBuilder data(int data) {
        stack.setDurability((short) data);
        return this;
    }

    public ItemStack build() {
        if (meta != null) {
            stack.setItemMeta(meta);
        }

        return stack;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta)this.stack.getItemMeta();
            im.setColor(color);
            this.stack.setItemMeta(im);
        }
        catch (ClassCastException ignored) {

        }
        return this;
    }

}
