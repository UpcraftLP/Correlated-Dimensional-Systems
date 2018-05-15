package mod.upcraftlp.cds.util;

import core.upcraftlp.craftdev.api.util.RegistryUtils;
import mod.upcraftlp.cds.Main;
import mod.upcraftlp.cds.init.CorrelatedBlocks;
import mod.upcraftlp.cds.init.CorrelatedItems;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author UpcraftLP
 */
@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onregisterBlocks(RegistryEvent.Register<Block> event) {
        RegistryUtils.createRegistryEntries(Block.class, event, CorrelatedBlocks.class, Main.MODID, CreativeTabs.REDSTONE);
    }

    @SubscribeEvent
    public static void onregisterItems(RegistryEvent.Register<Item> event) {
        RegistryUtils.createRegistryEntries(Item.class, event, CorrelatedItems.class, Main.MODID, CreativeTabs.REDSTONE);
    }
}
