package de.n8M4.hypixelapiforge;

import java.util.UUID;

public class PlayerStats {

    private final UUID uuid;
    private int level = 0;
    private boolean requested = false;

    public PlayerStats(UUID uuid) {
        this.uuid = uuid;
    }

    public int getLevel() {return this.level;}
    public UUID getUuid() {return this.uuid;}
    public boolean isRequested() {return this.requested;}
    public void setRequested(boolean requested) {this.requested = requested;}
    public void setLevel(int level) {this.level = level;}
    public boolean hasLevel() {
        return getLevel() != 0;
    }
}
