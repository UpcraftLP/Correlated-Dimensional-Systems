package mod.upcraftlp.cds.world;

import com.google.common.collect.Maps;
import mod.upcraftlp.cds.Main;
import mod.upcraftlp.cds.util.PortalTarget;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

/**
 * contract: every channel ID only leads to one destination to allow for one-way portals.
 * two-way portals therefore need to have 2 channels.
 */
public class PortalData extends WorldSavedData {

    private static final String DATA_NAME = Main.MODID + "_PortalData";
    public static final String KEY_CHANNEL = "channel";
    private static final String KEY_DATA = "data";
    private static final String KEY_TARGET = "target";

    private Map<UUID, PortalTarget> channels = Maps.newConcurrentMap();

    public PortalData(String dataName) {
        super(dataName);
    }

    public PortalData() {
        this(DATA_NAME);
    }

    @Nullable
    public static PortalTarget getTarget(UUID channel) {
        return instance().channels.get(channel);
    }

    public static boolean hasTarget(UUID channel) {
        return channel != null && instance().channels.containsKey(channel);
    }

    private static PortalData instance() {
        MapStorage storage = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getMapStorage();
        PortalData instance = (PortalData) storage.getOrLoadData(PortalData.class, DATA_NAME);
        if(instance == null) {
            instance = new PortalData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }

    public static void registerTarget(UUID key, PortalTarget target) {
        PortalData data = instance();
        data.channels.put(key, target);
        data.markDirty();
    }

    public static UUID registerNewChannel(PortalTarget target) {
        UUID key = UUID.randomUUID();
        registerTarget(key, target);
        return key;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList(KEY_DATA, Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            UUID uuid = NBTUtil.getUUIDFromTag(tag.getCompoundTag(KEY_CHANNEL));
            PortalTarget target = new PortalTarget(tag.getCompoundTag(KEY_TARGET));
            this.channels.put(uuid, target);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList channelList = new NBTTagList();
        for(UUID uuid : this.channels.keySet()) {
            PortalTarget target = this.channels.get(uuid);
            if(target != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag(KEY_TARGET, target.serializeNBT());
                tag.setTag(KEY_CHANNEL, NBTUtil.createUUIDTag(uuid));
                channelList.appendTag(tag);
            }
        }
        compound.setTag(KEY_DATA, channelList);
        return compound;
    }

}
