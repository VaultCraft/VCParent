package net.vaultcraft.vcutils.chat.bot;

/**
 * Created by Connor on 8/27/14. Designed for the VCUtils project.
 */
public interface ChatterBotSession {

    ChatterBotThought think(ChatterBotThought thought) throws Exception;

    String think(String text) throws Exception;
}