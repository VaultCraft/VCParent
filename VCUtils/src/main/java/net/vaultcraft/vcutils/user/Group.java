package net.vaultcraft.vcutils.user;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */
public enum Group {

    OWNER("&f/&c&lOWNER&f/ &c%user%&f: &c%message%", 16, false),
    DEVELOPER("&f/&6&lDEV&f/ &7%user%&6: &f%message%", 15, false),
    MANAGER("&f/&6&lMANAGER&f/ &e%user%&6: &f%message%", 14, false),
    ADMIN("&f/&b&lADMIN&f/ &7%user%&b: &f%message%", 13, false),
    MOD("&f/&3&lMOD&f/ &7%user%&3: &f%message%", 12, false),
    HELPER("&f/&9&lHELPER&f/ &7%user%&9: &f%message%", 11, false),
    ENDERDRAGON("&f/&5&lENDER&7&lDRAGON&f/ &5%user%&7: &f%message%", 10, 54, true),
    ENDERMAN("&f/&5&lENDERMAN&f/ &7%user%&5: &f%message%", 9, 35, true),
    CREEPER("&f/&2&lCREEPER&f/ &7%user%&2: &f%message%", 8, 27, true),
    YOUTUBE("&f/&c&lYOU&f&lTUBE&f/ &7%user%&c: &7%message%", 7, true),
    SKELETON("&f/&lSKELETON&f/ &7%user%&f: &7%message%", 6, 20, true),
    PIGMAN("&f/&4&lPIGMAN&f/ &7%user%&4: &7%message%", 5, 15, true),
    SLIME("&f/&a&lSLIME&f/ &7%user%&a: &7%message%", 4, 10, true),
    WOLF("&f/&8&lWOLF&f/ &7%user%&8: &7%message%", 3, 6, true),
    SILVERFISH("&f/&7&lSILVERFISH&f/ &7%user%: %message%", 2, 3, true),
    COMMON("&7%user%&f: &7%message%", 1, 1, false);

    private String tag;
    private int permLevel;
    private int enderChestSlots;
    private boolean isDonorRank;

    private Group(String tag, int permLevel, boolean isDonorRank) {
        this.tag = tag;
        this.permLevel = permLevel;
        this.isDonorRank = isDonorRank;
        this.enderChestSlots = 54;
    }

    private Group(String tag, int permLevel, int enderChestSlots, boolean isDonorRank) {
        this.tag = tag;
        this.permLevel = permLevel;
        this.isDonorRank = isDonorRank;
        this.enderChestSlots = enderChestSlots;
    }

    public int getEnderChestSlots() {
        return enderChestSlots;
    }

    public String getTag() {
        return tag;
    }

    public boolean hasPermission(Group other) {
        int level = other.permLevel;
        boolean donor = other.isDonorRank;

        if (this.getPermLevel() >= 13)
            return true;

        if (donor) {
            return (this.permLevel >= level && this.isDonorRank);
        } else {
            return (this.permLevel >= level);
        }
    }

    public static Group fromString(String find) {
        for (Group g : values()) {
            String name = g.toString().toLowerCase();
            if (find.replace("_", "").replace(" ", "").toLowerCase().equals(name))
                return g;
        }
        return null;
    }

    public String getName() {
        String format = toString();
        return format.substring(0, 1)+format.toString().substring(1).toLowerCase();
    }

    public int getPermLevel() {
        return permLevel;
    }

    public static Group fromPermLevel(int permLevel) {
        for(Group g: values()) {
            if(g.getPermLevel() == permLevel)
                return g;
        }

        return null;
    }
}
