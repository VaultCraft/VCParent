package net.vaultcraft.vcessentials.guild;

import java.util.*;

/**
 * Created by tacticalsk8er on 11/15/2014.
 */
public class Guild {

    private String name;
    private UUID creator;
    private HashMap<UUID, Role> members;
    private HashMap<String, Squad> squads;
    private List<Guild> allies;
    private List<Guild> enemies;


    public Guild(String name, UUID creator) {
        this.name = name;
        this.creator = creator;
        members = new HashMap<>();
        members.put(creator, Role.CREATOR);
        squads = new HashMap<>();
        allies = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public UUID getCreator() {
        return creator;
    }

    public HashMap<UUID, Role> getMembers() {
        return members;
    }

    public Optional<Role> getRole(UUID member) {
        return Optional.of(members.get(member));
    }

    public HashMap<String, Squad> getSquads() {
        return squads;
    }

    public List<Guild> getAllies() {
        return allies;
    }

    public List<Guild> getEnemies() {
        return enemies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMember(UUID member) {
        if(members.containsKey(member))
            members.remove(member);
        members.put(member, Role.MEMBER);
    }

    public void removeMemebr(UUID member) {
        if(members.containsKey(member))
            members.remove(member);
    }

    public void createSquad(String name, UUID leader) {
        Squad squad = new Squad(name, leader, this);
        squads.put(name, squad);
    }

    public void removeSquad(String name) {
        if(squads.containsKey(name))
            squads.remove(name);
    }

    public Optional<Squad> getSquad(String name) {
        return Optional.of(squads.get(name));
    }
}
