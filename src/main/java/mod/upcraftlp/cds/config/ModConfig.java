package mod.upcraftlp.cds.config;

import mod.upcraftlp.cds.Main;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID, name = Main.MODNAME)
@Config.LangKey("config." + Main.MODID + ".title")
public class ModConfig {

    @Config.Comment("the resolution of the portal renderer, in pixels")
    public static int quality = 1000;

}
