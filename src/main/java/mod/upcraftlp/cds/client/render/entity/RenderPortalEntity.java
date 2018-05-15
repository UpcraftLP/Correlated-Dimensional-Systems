package mod.upcraftlp.cds.client.render.entity;

import mod.upcraftlp.cds.entity.EntityPortal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

/**
 * @author UpcraftLP
 */
public class RenderPortalEntity extends Render<EntityPortal> {

    public static final Factory FACTORY = new Factory();

    protected RenderPortalEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPortal entity) {
        return null;
    }

    public static class Factory implements IRenderFactory<EntityPortal> {

        @Override
        public Render<? super EntityPortal> createRenderFor(RenderManager manager) {
            return new RenderPortalEntity(manager);
        }
    }
}
