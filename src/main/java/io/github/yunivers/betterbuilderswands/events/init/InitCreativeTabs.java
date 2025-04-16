package io.github.yunivers.betterbuilderswands.events.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.api.CreativeTab;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

import static io.github.yunivers.betterbuilderswands.BetterBuildersWandsMod.NAMESPACE;

public class InitCreativeTabs
{
    // Creative Tabs
    public static CreativeTab bbwTab;

    @EventListener
    public void onTabInit(TabRegistryEvent event)
    {
        bbwTab = new SimpleTab(NAMESPACE.id("bbw"), InitItems.WAND_DIAMOND);
        bbwTab.addItem(new ItemStack(InitItems.WAND_STONE));
        bbwTab.addItem(new ItemStack(InitItems.WAND_IRON));
        bbwTab.addItem(new ItemStack(InitItems.WAND_DIAMOND));
        bbwTab.addItem(new ItemStack(InitItems.WAND_UNBREAKABLE));

        event.register(bbwTab); // Registering tab
    }
}
