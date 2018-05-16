package mod.upcraftlp.cds.client.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mod.upcraftlp.cds.Main;
import mod.upcraftlp.cds.config.ModConfig;
import mod.upcraftlp.cds.entity.EntityPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

/**
 * @author UpcraftLP
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Main.MODID, value = {Side.CLIENT})
public class WorldRenderHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final Map<EntityPortal, Integer> textures = Maps.newConcurrentMap();
    public static final List<EntityPortal> pendingRemoval = Lists.newArrayList();

    private static boolean rendering = false;
    private static Entity renderEntity;
    private static Entity backupEntity;

    public static void generateTexture(EntityPortal entityPortal) {
        int textureID = GlStateManager.generateTexture();
        GlStateManager.bindTexture(textureID);
        GlStateManager.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, ModConfig.quality / 2, ModConfig.quality, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(3 * (ModConfig.quality / 2) * ModConfig.quality));
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        textures.put(entityPortal, textureID);
    }

    @SubscribeEvent
    public static void prePlayerRender(RenderPlayerEvent.Pre event) {
        if(!rendering) return;
        if(event.getEntityPlayer() == renderEntity) {
            backupEntity = mc.getRenderViewEntity();
            mc.getRenderManager().renderViewEntity = renderEntity; //TODO needed?
        }
    }

    @SubscribeEvent
    public static void postPlayerRender(RenderPlayerEvent.Post event) {
        if(!rendering) return;
        if(event.getEntityPlayer() == renderEntity) {
            Minecraft.getMinecraft().getRenderManager().renderViewEntity = backupEntity;
            renderEntity = null;
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {

        //cleanup to free up RAM
        if(!pendingRemoval.isEmpty()) {
            for(EntityPortal portal : pendingRemoval) {
                if(textures.containsKey(portal)) GlStateManager.deleteTexture(textures.get(portal));
                textures.remove(portal);
            }
        }

        if(event.phase != TickEvent.Phase.START) return;
        if(mc.inGameHasFocus) {
            for (EntityPortal portal : textures.keySet()) {
                if(portal == null) {
                    textures.remove(null);
                    continue;
                }
                if(!portal.isEntityAlive()) {
                    pendingRemoval.add(portal);
                    continue;
                }

                if(!portal.rendering) continue;

                //setup
                GameSettings settings = mc.gameSettings;
                backupEntity = mc.getRenderViewEntity();
                final int widthBackup = mc.displayWidth;
                final int heightBackup = mc.displayHeight;
                final int thirdPersonBackup = settings.thirdPersonView;
                final boolean hideGuiBackup = settings.hideGUI;
                final int mipmapBackup = settings.mipmapLevels;
                final float fovBackup = settings.fovSetting;
                final int textureID = textures.get(portal);

                //settings.fovSetting = 1.0F;  //TODO config
                settings.thirdPersonView = 0;
                settings.hideGUI = true;
                settings.mipmapLevels = 3;
                rendering = true;
                renderEntity = mc.player;

                mc.displayWidth = ModConfig.quality / 2;
                mc.displayHeight = ModConfig.quality;
                mc.setRenderViewEntity(portal);
                EntityRenderer entityRenderer = mc.entityRenderer;
                entityRenderer.renderWorld(event.renderTickTime, System.nanoTime() + (1000000000L / Math.max(30, mc.gameSettings.limitFramerate)));
                GlStateManager.bindTexture(textureID);
                GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, ModConfig.quality / 2, ModConfig.quality, 0);

                renderEntity = null;
                rendering = false;

                //restore values
                mc.setRenderViewEntity(mc.player);
                settings.fovSetting = fovBackup;
                settings.thirdPersonView = thirdPersonBackup;
                settings.hideGUI = hideGuiBackup;
                settings.mipmapLevels = mipmapBackup;
                mc.displayWidth = widthBackup;
                mc.displayHeight = heightBackup;
                portal.rendering = false;
            }
        }
    }

}
