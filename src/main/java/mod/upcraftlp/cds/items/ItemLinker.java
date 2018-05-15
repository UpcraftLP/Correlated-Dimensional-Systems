package mod.upcraftlp.cds.items;

import core.upcraftlp.craftdev.api.item.Item;
import mod.upcraftlp.cds.blocks.BlockPortalBase;
import mod.upcraftlp.cds.blocks.tile.TileEntityPortalBase;
import mod.upcraftlp.cds.init.CorrelatedBlocks;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import static mod.upcraftlp.cds.world.PortalData.KEY_CHANNEL;

/**
 * @author UpcraftLP
 */
public class ItemLinker extends Item {

    public ItemLinker() {
        super("portal_linker");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.getBlockState(pos).getBlock() == CorrelatedBlocks.PORTAL) {
            pos = worldIn.getBlockState(pos).getValue(BlockPortalBase.HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
            ItemStack stack = player.getHeldItem(hand);
            NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            TileEntityPortalBase te = (TileEntityPortalBase) worldIn.getTileEntity(pos);
            if(nbt.hasKey(KEY_CHANNEL)) {
                te.setTarget(NBTUtil.getUUIDFromTag(nbt.getCompoundTag(KEY_CHANNEL)));
                nbt.removeTag(KEY_CHANNEL);
                player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "channel created!"), true);
            }
            else {
                nbt.setTag(KEY_CHANNEL, NBTUtil.createUUIDTag(te.createChannel()));
                player.sendStatusMessage(new TextComponentString(TextFormatting.DARK_GRAY + "channel stored!"), true);
            }
            stack.setTagCompound(nbt);
            player.setHeldItem(hand, stack);
        }
        return EnumActionResult.FAIL;
    }
}
