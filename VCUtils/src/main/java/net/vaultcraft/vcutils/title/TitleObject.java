package net.vaultcraft.vcutils.title;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

public class TitleObject
{
    private String rawTitle;
    private String rawSubtitle;
    private IChatBaseComponent title;
    private IChatBaseComponent subtitle;
    private int fadeIn = -1;
    private int stay = -1;
    private int fadeOut = -1;

    public TitleObject(String title, TitleType type)
    {
        if (type == TitleType.TITLE) {
            setTitle(title);
        } else if (type == TitleType.SUBTITLE) {
            setSubtitle(title);
        }
    }

    public TitleObject(String title, String subtitle)
    {
        this.rawTitle = title;
        this.rawSubtitle = subtitle;
        this.title = ChatSerializer.a(TextConverter.convert(title));
        this.subtitle = ChatSerializer.a(TextConverter.convert(subtitle));
    }

    public void send(Player p)
    {
        CraftPlayer player = (CraftPlayer)p;
        if (player.getHandle().playerConnection.networkManager.getVersion() != 47) {
            return;
        }
        player.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TIMES, this.fadeIn, this.stay, this.fadeOut));
        if (this.title != null) {
            player.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, this.title));
        }
        if (this.subtitle != null) {
            player.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, this.subtitle));
        }
    }

    public String getTitle()
    {
        return this.rawTitle;
    }

    public TitleObject setTitle(String title)
    {
        this.rawTitle = title;
        this.title = ChatSerializer.a(TextConverter.convert(title));
        return this;
    }

    public String getSubtitle()
    {
        return this.rawSubtitle;
    }

    public TitleObject setSubtitle(String subtitle)
    {
        this.rawSubtitle = subtitle;
        this.subtitle = ChatSerializer.a(TextConverter.convert(subtitle));
        return this;
    }

    public int getFadeIn()
    {
        return this.fadeIn;
    }

    public TitleObject setFadeIn(int i)
    {
        this.fadeIn = i;
        return this;
    }

    public int getStay()
    {
        return this.stay;
    }

    public TitleObject setStay(int i)
    {
        this.stay = i;
        return this;
    }

    public int getFadeOut()
    {
        return this.fadeOut;
    }

    public TitleObject setFadeOut(int i)
    {
        this.fadeOut = i;
        return this;
    }

    public static enum TitleType
    {
        TITLE,  SUBTITLE;

        private TitleType() {}
    }
}
