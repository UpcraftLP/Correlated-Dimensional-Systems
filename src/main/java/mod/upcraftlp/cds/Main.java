package mod.upcraftlp.cds;


import mod.upcraftlp.cds.proxy.CommonProxy;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Year;

@Mod(
        name = Main.MODNAME,
        version = Main.VERSION,
        acceptedMinecraftVersions = Main.MCVERSIONS,
        modid = Main.MODID,
        dependencies = Main.DEPENDENCIES,
        updateJSON = Main.UPDATE_JSON
)
public class Main {

    public static final String MODID = "cds";
    public static final String SERVERSIDE_PATH = "mod.upcraftlp.cds.proxy.ServerProxy";
    public static final String CLIENTSIDE_PATH = "mod.upcraftlp.cds.proxy.ClientProxy";
    public static final String MCVERSIONS = "[1.12,1.13)";
    public static final String VERSION = "@VERSION@";

    //TODO NO TEXTFORMATTING IN COMMON CODE!
    public static final String CREDITS = TextFormatting.GOLD + "\u00A9" + "2017-" + Year.now().getValue() + " UpcraftLP";
    // DO NOT CHANGE!!!
    public static final String MODNAME = "Correlated Dimensional Systems";
    public static final String DEPENDENCIES = "required-after:craftdevcore@[2.2.8,)";
    public static final String UPDATE_URL = ""; //TODO
    public static final String UPDATE_JSON = ""; //TODO


    @Instance(MODID)
    public static Main INSTANCE;

    private static Logger log = LogManager.getLogger(MODID);

    public static Logger getLogger() {
        return log;
    }

    @SidedProxy(clientSide = CLIENTSIDE_PATH, serverSide = SERVERSIDE_PATH)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
        proxy.preInit(event);
        log.info("Pre-Initialization finished.");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        log.info("Initialization finished.");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        log.info("Post-Initialization finished.");
    }
}
