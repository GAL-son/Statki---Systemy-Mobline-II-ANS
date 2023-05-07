package com.battleships.model.client.ship;

import java.io.Serializable;

public class Ship implements Serializable {
    private int size;
    private int health;
    private String type;//??


    public Ship(int size) {
        this.size = size;
        this.health = size;
        this.type = resolveType(size);
    }

    private String resolveType(int size) {
        String name;
        switch (size) {
            case 1:
                name = "kuter torpedowy";
                break;
            case 2:
                name = "łódź podwodna";
                break;
            case 3:
                name = "ciężki krążownik";
                break;
            case 4:
                name = "lotniskowiec";
                break;
            default:
                name = "???";
        }
        return name;
    }

    public void hitShip() {
        this.health--;
        //
    }

    private boolean isSunk()
    {
        if(this.health==0)
        {
            return true;
        }
        else
        {
            return  false;

        }
    }

    public int getSize() {
        return size;
    }

    public int getHealth() {
        return health;
    }

    public String getType() {
        return type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setType(String type) {
        this.type = type;
    }

}
