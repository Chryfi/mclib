package mchorse.mclib.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpHelper
{
    /**
     * Minimum OP level according to vanilla code
     */
    public static final int VANILLA_OP_LEVEL = 2;

    @SideOnly(Side.CLIENT)
    public static int getPlayerOpLevel()
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        return player == null ? 0 : player.getPermissionLevel();
    }

    @SideOnly(Side.CLIENT)
    public static boolean isPlayerOp()
    {
        return isOp(getPlayerOpLevel());
    }

    public static boolean isPlayerOp(EntityPlayerMP player)
    {
        if (player == null)
        {
            return false;
        }

        MinecraftServer server = player.mcServer;

        if (server.getPlayerList().canSendCommands(player.getGameProfile()))
        {
            UserListOpsEntry userEntry = server.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());

            return isOp(userEntry == null ? server.getOpPermissionLevel() : userEntry.getPermissionLevel());
        }

        return false;
    }

    public static boolean isOp(int opLevel)
    {
        return opLevel >= VANILLA_OP_LEVEL;
    }
}
