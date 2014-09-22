package net.vaultcraft.vcutils.user;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */
public enum Group {

    //STAFF RANKS
    OWNER("&f/&c&lOWNER&f/ &c%user%&f: &c%message%", 13, false),
    DEVELOPER("&f/&6&lDEV&f/ &7%user%&6: &f%message%", 12, false),
    MANAGER("&f/&6&lMANAGER&f/ &e%user%&6: &f%message%", 11, false),
    ADMIN("&f/&b&lADMIN&f/ &7%user%&b: &f%message%", 10, false),
    MOD("&f/&2&lMOD&f/ &7%user%&2: &f%message%", 9, false),
    HELPER("&f/&9&lHELPER&f/ &7%user%&9: &f%message%", 8, false),

    //DONOR RANKS
    ENDERDRAGON("&f/&5&lENDER&7&lDRAGON&f/ &5%user%&7: &f%message%", 7, 54, true),
    ENDERMAN("&f/&5&lENDERMAN&f/ &7%user%&5: &f%message%", 6, 40, true),
    SKELETON("&f/&lSKELETON&f/ &7%user%&f: &7%message%", 4, 25, true),
    SLIME("&f/&a&lSLIME&f/ &7%user%&a: &7%message%", 3, 14, true),
    WOLF("&f/&8&lWOLF&f/ &7%user%&8: &7%message%", 2, 6, true),

    //DEFAULT RANK
    COMMON("&7%user%&f: &7%message%", 1, 1, false),

    //EXTRA
    YOUTUBE("&f/&c&lYOU&f&lTUBE&f/ &7%user%&c: &7%message%", 5, 20, true);

    private String tag;
    private int permLevel;
    private int enderChestSlots = 1;
    private boolean isDonorRank;

    private Group(String tag, int permLevel, boolean isDonorRank) {
        this(tag, permLevel, 54, isDonorRank);
    }

    private List<Group> all = Lists.newArrayList();
    private Group highest;

    private Group(String tag, int permLevel, int enderChestSlots, boolean isDonorRank) {
        this();
        this.tag = tag;
        this.permLevel = permLevel;
        this.isDonorRank = isDonorRank;
        this.enderChestSlots = enderChestSlots;
    }

    private Group() {
        merge(this);
    }

    public int getEnderChestSlots() {
        return highest.enderChestSlots;
    }

    public String getTag() {
        return highest.tag;
    }

    public boolean hasPermission(Group other) {
        for (Group a : all) {
            boolean use = _hasPermission(a, other);
            if (use == true)
                return true;
        }

        return false;
    }

    private boolean _hasPermission(Group me, Group other) {
        int level = other.permLevel;
        boolean donor = other.isDonorRank;

        if (me.getPermLevel() >= 10)
            return true;

        if (donor) {
            return (me.permLevel >= level && me.isDonorRank);
        } else {
            return (me.permLevel >= level);
        }
    }

    public void merge(Group other) {
        if (all.contains(other))
            return;

        all.add(other);
        if (highest == null || other.hasPermission(highest))
            highest = other;
    }

    public void remove(Group other) {
        all.remove(other);
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
        return highest.permLevel;
    }

    public List<Group> getAllGroups() {
        return all;
    }

    public static Group fromPermLevel(int permLevel) {
        for(Group g: values()) {
            if(g.getPermLevel() == permLevel)
                return g;
        }

        return null;
    }

    public Group getHighest() {
        return highest;
    }
}
