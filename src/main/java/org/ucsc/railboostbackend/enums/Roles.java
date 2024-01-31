package org.ucsc.railboostbackend.enums;

public enum Roles {
    ADMINISTRATOR(1), CHIEF_STATION_MASTER(2), STATION_MASTER(3), TICKET_CHECKING_OFFICER(4), PASSENGER(5);

    private final int roleId;

    Roles(int roleId) {this.roleId = roleId;}

    public static Roles valueOfRoleId(int roleId) {
        for (Roles e : values()) {
            if (e.roleId == roleId) {
                return e;
            }
        }
        return null;
    }
}
