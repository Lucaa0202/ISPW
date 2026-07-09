package org.example.project3.utilities.enums;

public enum RestTime {
    SECONDS30(30),
    SECONDS60(60),
    SECONDS90(90),
    SECONDS120(120);


    private final int id;

    //costruttore di Role
    private RestTime(int id) {
        this.id = id;
    }

    //Converte un intero al ruolo corrispondente
    public static RestTime convertIntToRestTime(int id) {
        for (RestTime type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
