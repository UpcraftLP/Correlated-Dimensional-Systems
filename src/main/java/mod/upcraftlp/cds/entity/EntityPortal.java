package mod.upcraftlp.cds.entity;

import mod.upcraftlp.cds.Main;
import mod.upcraftlp.cds.init.CorrelatedBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author UpcraftLP
 */
public class EntityPortal extends Entity {

    @SideOnly(Side.CLIENT)
    public boolean rendering = false;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public EntityPortal(World world, BlockPos pos, EnumFacing facing) {
        this(world);
        if(facing == null) facing = EnumFacing.SOUTH;
        this.setPositionAndRotation(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, facing.getHorizontalAngle(), 0);
        double offset = 0.5D; //TODO position offset based on facing
        switch (facing) {
            case SOUTH: //positive Z
                this.posZ -= offset;
                break;
            case WEST: //negative X
                this.posX += offset;
                break;
            case NORTH: //negative Z
                this.posZ += offset;
                break;
            case EAST: //positive X
                this.posX -= offset;
                break;
        }
    }

    public EntityPortal(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setEntityInvulnerable(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onUpdate() {
        super.onUpdate();
        //TODO adjust the angle of the target entity a tiny bit to be able to look behind the portal
        /*if (rendering) {
            double distance = getDistanceToEntity(mc.player);
            double height = (mc.player.getEyeHeight() + mc.player.posY) - this.posY;
            double anglePitch = Math.atan2(height, distance) * (180D / Math.PI);
            this.rotationPitch = (float) MathHelper.clamp(anglePitch, -45F, 45F);;
        }*/
        if(world.getBlockState(this.getPosition()).getBlock() != CorrelatedBlocks.PORTAL) {
            this.setDead();
        }
    }

    @Override
    public void setDead() {
        super.setDead();
        Main.proxy.killPortal(this);
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public float getEyeHeight() {
        return super.getEyeHeight();
        //return 1.62F / 2;
        //return 1.62F; //see EntityPlayer#getDefaultEyeHeight()
    }

    @Override
    protected void entityInit() {
        /*
        this.ticket = ForgeChunkManager.requestTicket(Reference.MODID, this.world, ForgeChunkManager.Type.NORMAL);
        if(this.ticket != null) {
            ForgeChunkManager.forceChunk(ticket, this.world.getChunkFromBlockCoords(this.getPosition()).getPos());
        }
        */
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
