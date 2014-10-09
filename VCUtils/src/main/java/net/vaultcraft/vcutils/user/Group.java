package net.vaultcraft.vcutils.user;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Connor on 7/20/14. Designed for the VCUtils project.
 */
public enum Group {

    //STAFF RANKS
    OWNER("&f/&c&lOWNER&f/ &c%user%&f: &c%message%", 14, false),
    DEVELOPER("&f/&6&lDEV&f/ &7%user%&6: &f%message%", 13, false),
    MANAGER("&f/&6&lMANAGER&f/ &e%user%&6: &f%message%", 12, false),
    ADMIN("&f/&b&lADMIN&f/ &7%user%&b: &f%message%", 11, false),
    MOD("&f/&2&lMOD&f/ &7%user%&2: &f%message%", 10, false),
    HELPER("&f/&9&lHELPER&f/ &7%user%&9: &f%message%", 9, false),

    //DONOR RANKS
    ENDERDRAGON("&f/&5&lENDER&7&lDRAGON&f/ &5%user%&7: &f%message%", 7, 54, true),
    WITHER("&f/&e&lWITHER&f/ &e%user%&7: &f%message%", 7, 42, true),
    ENDERMAN("&f/&5&lENDERMAN&f/ &7%user%&5: &f%message%", 6, 30, true),
    SKELETON("&f/&lSKELETON&f/ &7%user%&f: &7%message%", 4, 20, true),
    SLIME("&f/&a&lSLIME&f/ &7%user%&a: &7%message%", 3, 12, true),
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

    private boolean hasPermission(Group me, Group other) {
        int level = other.permLevel;
        boolean donor = other.isDonorRank;

        if (me.getPermLevel() >= 11)
            return true;

        if (donor) {
            return (me.permLevel >= level && me.isDonorRank);
        } else {
            return (me.permLevel >= level);
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

    public static class GroupHandler {

        private Player player;
        private List<Group> all = Lists.newArrayList();
        private Group highest;

        public GroupHandler(Player player) {
            this.player = player;
            this.all.add(Group.COMMON);
            this.highest = Group.COMMON;
        }

        public List<Group> getAllGroups() {
            return all;
        }

        public int getPermLevel() {
            return highest.permLevel;
        }

        private void evalHighest() {
            Group highest = null;
            for (Group group : all) {
                if (highest == null || group.permLevel > highest.permLevel)
                    highest = group;
            }

            this.highest = highest;
        }

        public boolean hasPermission(Group other) {
            for (Group a : all) {
                boolean use = a.hasPermission(a, other);
                if (use == true)
                    return true;
            }

            return false;
        }

        public void merge(Group other) {
            if (all.contains(other))
                return;

            all.add(other);
            evalHighest();
        }

        public void remove(Group other) {
            all.remove(other);

            evalHighest();
        }

        public Group getHighest() {
            return highest;
        }
    }
}
