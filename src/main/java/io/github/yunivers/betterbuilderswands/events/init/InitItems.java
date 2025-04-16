package io.github.yunivers.betterbuilderswands.events.init;

import io.github.yunivers.betterbuilderswands.item.ItemRestrictedWandAdvanced;
import io.github.yunivers.betterbuilderswands.item.ItemRestrictedWandBasic;
import io.github.yunivers.betterbuilderswands.item.ItemUnrestrictedWand;
import io.github.yunivers.betterbuilderswands.wands.RestrictedWand;
import io.github.yunivers.betterbuilderswands.wands.UnbreakingWand;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;

import static io.github.yunivers.betterbuilderswands.BetterBuildersWandsMod.NAMESPACE;

public class InitItems
{
    // Items
    public static Item WAND_STONE;
    public static Item WAND_IRON;
    public static Item WAND_DIAMOND;
    public static Item WAND_UNBREAKABLE;

    @EventListener
    public void registerItems(ItemRegistryEvent event)
    {
        WAND_STONE = new ItemRestrictedWandBasic(NAMESPACE.id("wand_stone"), new RestrictedWand(5))
                .setTranslationKey(NAMESPACE, "wand_stone");
        WAND_IRON = new ItemRestrictedWandAdvanced(NAMESPACE.id("wand_iron"), new RestrictedWand(9))
                .setTranslationKey(NAMESPACE, "wand_iron");
        WAND_DIAMOND = new ItemUnrestrictedWand(NAMESPACE.id("wand_diamond"), new RestrictedWand(ToolMaterial.DIAMOND.getDurability()))
                .setMaxDamage(ToolMaterial.DIAMOND.getDurability())
                .setTranslationKey(NAMESPACE, "wand_diamond");
        WAND_UNBREAKABLE = new ItemUnrestrictedWand(NAMESPACE.id("wand_unbreakable"), new UnbreakingWand())
                .setTranslationKey(NAMESPACE, "wand_unbreakable");
    }
}
