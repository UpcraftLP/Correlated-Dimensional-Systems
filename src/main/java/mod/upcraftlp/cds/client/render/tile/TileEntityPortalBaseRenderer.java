package mod.upcraftlp.cds.client.render.tile;

import mod.upcraftlp.cds.blocks.tile.TileEntityPortalBase;
import mod.upcraftlp.cds.client.render.WorldRenderHandler;
import mod.upcraftlp.cds.entity.EntityPortal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

//FIXME find workaround for mc rendering a black bar above the frame when using the default resolution of 854x480
public class TileEntityPortalBaseRenderer extends TileEntitySpecialRenderer<TileEntityPortalBase> {

    @Override
    public void renderTileEntityFast(TileEntityPortalBase te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        super.renderTileEntityFast(te, x, y, z, partialTicks, destroyStage, partial, buffer);
    }

    @Override
    public void render(TileEntityPortalBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha); //render custom name tag etc.
        if(TileEntityRendererDispatcher.instance.entity instanceof EntityPortal) return; //we don't want recursive loops!
        EntityPortal entityPortal = te.getPortalEntity(); //FIXME remove entity!
        if(entityPortal == null) return; //no destination set
        if(!WorldRenderHandler.textures.containsKey(entityPortal)) {
            WorldRenderHandler.generateTexture(entityPortal);
            return;
        }
        //if(!state.shouldSideBeRendered(getWorld(), te.getPos(), state.getValue(BlockPortalBase.FACING))) return; //no need to render invisible stuff //TODO implement shouldSideBeRendered

        entityPortal.rendering = true;

        GlStateManager.disableLighting();
        GlStateManager.disableNormalize();
        GlStateManager.enableBlend();
        //GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.00625F);
        GlStateManager.enableCull();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 15*16, 15*16);
        GlStateManager.disableTexture2D();

        GlStateManager.pushMatrix();
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int textureID = WorldRenderHandler.textures.get(entityPortal);
            GlStateManager.bindTexture(textureID);
            //bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE); //FIXME why does regular bindTexture() work?!

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexBuffer = tessellator.getBuffer();

            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            {
                //TODO rotate properly!
                GlStateManager.rotate(te.getFacingAngle(), 0.0F, 1.0F, 0.0F); //make the portal face the correct direction
            }
            GlStateManager.translate(-x - 0.5D, -y, -z - 0.5D);
            vertexBuffer.setTranslation(x + 0.5D, y, z + 0.5D);
            {
                vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

                final double w_one = te.getWidth() / 2.0D;
                final double w_zero = -w_one;
                final double h_one = te.getHeight();
                final double h_zero = 0.0D;
                final double z_offset = 0.0001D;

                vertexBuffer.pos(w_zero, h_zero, z_offset).tex(1.0D, 100.0D).endVertex();
                vertexBuffer.pos(w_one, h_zero, z_offset).tex(0.0D, 100.0D).endVertex();
                vertexBuffer.pos(w_one, h_one, z_offset).tex(0.0D, 0.0D).endVertex();
                vertexBuffer.pos(w_zero, h_one, z_offset).tex(1.0D, 0.0D).endVertex();
            }
            vertexBuffer.setTranslation(0, 0, 0);
            tessellator.draw();
        }
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.disableCull();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableNormalize();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        int light = te.getWorld().getCombinedLight(te.getPos(), 0);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(light % 65536) / 1.0F, (float)(light / 65536) / 1.0F);
        entityPortal.rendering = false;
    }

    @Override
    public boolean isGlobalRenderer(TileEntityPortalBase te) {
        return true;
    }
}
