package mod.upcraftlp.cds.util;

import mod.upcraftlp.cds.blocks.BlockPortalBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author UpcraftLP
 */
public class PortalTarget implements INBTSerializable<NBTTagCompound> {

    private static final String KEY_DIMENSION = "dim";
    private static final String KEY_POSITION = "pos";

    private int destWorld;
    private BlockPos destPos;

    public PortalTarget(World destWorld, BlockPos destPos) {
        this.destWorld = destWorld.provider.getDimension();
        this.destPos = destPos;
    }

    public PortalTarget(NBTTagCompound nbt) {
        this.deserializeNBT(nbt);
    }

    public boolean isInSameDimension(World world) {
        return destWorld == world.provider.getDimension();
    }

    public int getDimension() {
        return destWorld;
    }

    public World getWorld() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(destWorld);
    }

    public BlockPos getPosition() {
        return destPos;
    }

    public EnumFacing getOrientation() {
        return getWorld().getBlockState(getPosition()).getValue(BlockPortalBase.FACING);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setInteger(KEY_DIMENSION, getDimension());
        ret.setTag(KEY_POSITION, NBTUtil.createPosTag(getPosition()));
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.destWorld = nbt.getInteger(KEY_DIMENSION);
        this.destPos = NBTUtil.getPosFromTag(nbt.getCompoundTag(KEY_POSITION));
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        else if(obj instanceof PortalTarget) {
            PortalTarget other = (PortalTarget) obj;
            if(other.destWorld == this.destWorld) {
                if(this.destPos == null) return other.destPos == null;
                else return this.destPos.equals(other.destPos);
            }
        }
        return false;
    }
}
