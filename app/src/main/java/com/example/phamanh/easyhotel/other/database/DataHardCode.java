package com.example.phamanh.easyhotel.other.database;

import java.util.ArrayList;
import java.util.List;


public class DataHardCode {

    public static List<String> getListNearby() {
        List<String> rs = new ArrayList<>();
        rs.add("Cinema");
        rs.add("Cafe");
        rs.add("Hospital");
        rs.add("Restaurant / Food");
        rs.add("Police");
        rs.add("ATM");
        rs.add("Bank");
        return rs;
    }

    public static List<String> getListRoom(boolean check) {
        List<String> rs = new ArrayList<>();
        rs.add("1");
        rs.add("2");
        if (!check){
            rs.add("3");
            rs.add("4");
        }
        return rs;
    }

    public static List<String> getRoomHotel() {
        List<String> rs = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            rs.add(String.valueOf(i));
        }
        return rs;
    }
}
