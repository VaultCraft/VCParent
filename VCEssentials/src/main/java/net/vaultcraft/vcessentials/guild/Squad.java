package net.vaultcraft.vcessentials.guild;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tacticalsk8er on 11/15/2014.
 */
public class Squad {

    private String name;
    private UUID leader;
    private List<UUID> members;
    private Guild guild;

    public Squad(String name, UUID leader, Guild guild) {
        this.name = name;
        this.leader = leader;
        this.guild = guild;
        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setName(String name) {
        guild.getSquads().remove(this.name);
        guild.getSquads().put(name, this);
        this.name = name;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public void addMember(UUID member) {
        if(members.contains(member))
            members.remove(member);
        members.add(member);
    }

    public void removeMember(UUID member) {
        if(members.contains(member))
            members.remove(member);
    }
}
