package mod.upcraftlp.cds.proxy;

import mod.upcraftlp.cds.Main;
import mod.upcraftlp.cds.blocks.tile.TileEntityPortalBase;
import mod.upcraftlp.cds.entity.EntityPortal;
import mod.upcraftlp.cds.util.ChunkLoadingCallback;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	private static int entityID = 0;

	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityPortalBase.class, Main.MODID + "_portalbase");
		EntityRegistry.registerModEntity(new ResourceLocation(Main.MODID, "portalEntity"), EntityPortal.class, "portalEntity", entityID++, Main.INSTANCE, 64, 20, false);
	}
	
	public void init(FMLInitializationEvent event) {
		ForgeChunkManager.setForcedChunkLoadingCallback(Main.INSTANCE, new ChunkLoadingCallback());
	}

	public void postInit(FMLPostInitializationEvent event) {
		
	}

	public void killPortal(EntityPortal portal) {
		//NO-OP
	}

}
