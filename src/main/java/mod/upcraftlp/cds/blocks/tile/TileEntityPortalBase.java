package mod.upcraftlp.cds.blocks.tile;

import mod.upcraftlp.cds.Main;
import mod.upcraftlp.cds.blocks.BlockPortalBase;
import mod.upcraftlp.cds.entity.EntityPortal;
import mod.upcraftlp.cds.util.PortalTarget;
import mod.upcraftlp.cds.world.PortalData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

import static mod.upcraftlp.cds.world.PortalData.*;

public class TileEntityPortalBase extends TileEntity {

    private static final String KEY_DESTINATION_CHANNEL = "destinationChannel";
    private static final String KEY_WIDTH = "width";
    private static final String KEY_HEIGHT = "height";
    //TODO variable size
    //TODO chunk loading tickets

    private EntityPortal portal;
    private UUID channel = null; //this target
    private UUID targetChannel = null; //the target channel
    private int width;
    private int height;

    public TileEntityPortalBase() {
        this.setSize(3, 2); //TODO different sizes
    }

    /**
     * used to get the destination portal entity for easy rendering
     * @return the entity or {@code null} if there is no destination in range
     */
    @Nullable
    @SideOnly(Side.CLIENT)
    public EntityPortal getPortalEntity() {
        if(this.portal == null && PortalData.hasTarget(this.targetChannel)) {
            PortalTarget target = PortalData.getTarget(this.targetChannel);
            if(target.isInSameDimension(this.world)) { //TODO different dimensions
                this.portal = new EntityPortal(target.getWorld(), target.getPosition(), target.getOrientation());
                target.getWorld().spawnEntity(this.portal);
                if(!this.portal.isEntityAlive()) {
                    Main.getLogger().warn("unable to spawn new portal!");
                    this.portal = null;
                }
            }
        }
        return this.portal;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return this.getPortalEntity() == null ? new AxisAlignedBB(this.getPos()) : new AxisAlignedBB(this.getPos()).expand(this.getWidth(), this.getHeight(), this.getWidth()); //TODO better AABB based on facing and use actual size
    }

    public float getFacingAngle() {
        return world.getBlockState(this.pos).getValue(BlockPortalBase.FACING).getHorizontalAngle();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey(KEY_CHANNEL)) {
            this.channel = NBTUtil.getUUIDFromTag(compound.getCompoundTag(KEY_CHANNEL));
        }
        if(compound.hasKey(KEY_DESTINATION_CHANNEL)) this.targetChannel = NBTUtil.getUUIDFromTag(compound.getCompoundTag(KEY_DESTINATION_CHANNEL));
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger(KEY_WIDTH, this.width);
        compound.setInteger(KEY_HEIGHT, this.height);
        if(this.targetChannel != null) compound.setTag(KEY_DESTINATION_CHANNEL, NBTUtil.createUUIDTag(this.targetChannel));
        compound.setTag(KEY_CHANNEL, NBTUtil.createUUIDTag(this.createChannel()));
        return compound;
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 0, getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(KEY_WIDTH, width);
        nbt.setInteger(KEY_HEIGHT, height);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.width = tag.getInteger(KEY_WIDTH);
        this.height = tag.getInteger(KEY_HEIGHT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.handleUpdateTag(pkt.getNbtCompound());
    }
    
    @Override
    public void onChunkUnload() {
        if(this.portal != null) {
            this.portal.setDead();
            this.portal = null;
        }
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.markDirty();
    }

    public UUID createChannel() {
        if(!PortalData.hasTarget(this.channel)) {
            this.channel = PortalData.registerNewChannel(new PortalTarget(this.world, this.pos));
            this.markDirty();
        }
        return this.channel;
    }

    public void setTarget(UUID target) {
        this.targetChannel = target;
        if(this.portal != null) {
            this.portal.setDead();
            this.portal = null;
        }
        this.markDirty();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
