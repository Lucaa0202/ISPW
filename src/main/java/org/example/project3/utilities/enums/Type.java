package org.example.project3.utilities.enums;

public enum Type {
    MENSILE(1),
    ANNUALE(2),
    TRIMESTRALE(3),
    QUADRIMESTRALE(4);

    private final int id;

    //costruttore di Role
    private Type(int id) {
        this.id = id;
    }

    //Converte un intero al ruolo corrispondente
    public static Type convertIntToType(int id) {
        for (Type type : values()) {
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
