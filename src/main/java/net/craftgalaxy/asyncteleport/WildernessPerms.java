package net.craftgalaxy.asyncteleport;

public enum WildernessPerms {

    // String permission nodes used across the plugin
    SIGN("asyncteleport.staff"),
    STAFF("asyncteleport.staff"),
    BYPASS("asyncteleport.bypass"),
    DONATOR("asyncteleport.donator"),
    TELEPORT("asyncteleport.teleport");

    private final String permission;

    WildernessPerms(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
