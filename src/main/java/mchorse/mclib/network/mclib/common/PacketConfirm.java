package mchorse.mclib.network.mclib.common;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.network.mclib.client.ClientHandlerConfirm;
import mchorse.mclib.network.mclib.server.ServerHandlerConfirm;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Map;
import java.util.function.Consumer;

public class PacketConfirm implements IMessage
{
    public int confirmId;
    public int behaviourId;
    public boolean confirm;

    public PacketConfirm(Consumer<Boolean> behaviour, Consumer<Boolean> callback)
    {
        Map.Entry<Integer, Consumer<Boolean>> entry = ServerHandlerConfirm.consumers.lastEntry();

        this.confirmId =  ServerHandlerConfirm.consumers.lastEntry()!=null ? entry.getKey()+1 : 0;

        entry = ClientHandlerConfirm.consumers.lastEntry();
        this.behaviourId =  ClientHandlerConfirm.consumers.lastEntry()!=null ? entry.getKey()+1 : 0;

        ServerHandlerConfirm.consumers.put(confirmId, callback);
        ClientHandlerConfirm.consumers.put(behaviourId, behaviour);
    }

    public PacketConfirm(boolean confirm)
    {
        this.confirm = confirm;
    }

    public PacketConfirm()
    {}

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.confirmId = buf.readInt();
        this.behaviourId = buf.readInt();
        this.confirm = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.confirmId);
        buf.writeInt(this.behaviourId);
        buf.writeBoolean(this.confirm);
    }
}