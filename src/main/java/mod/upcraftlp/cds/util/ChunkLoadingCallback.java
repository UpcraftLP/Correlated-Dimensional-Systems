package mod.upcraftlp.cds.util;

import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * @author UpcraftLP
 */
@Mod.EventBusSubscriber
public class ChunkLoadingCallback implements ForgeChunkManager.OrderedLoadingCallback {

    private static List<ForgeChunkManager.Ticket> tickets = Lists.newArrayList();

    @Override
    public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
        //NO-OP
        //there shouldn't be any tickets to load because the lsit will always be cleared first.
    }

    @Override
    public List<ForgeChunkManager.Ticket> ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world, int maxTicketCount) {
        tickets.clear();
        return tickets;
    }

    public static void onWorldUnload(WorldEvent.Unload event) {

    }
}
