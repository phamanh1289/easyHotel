package com.example.phamanh.easyhotel.other.database;

import java.util.ArrayList;
import java.util.List;


public class DataHardCode {

    public static List<String> getListNearby() {
        List<String> rs = new ArrayList<>();
        rs.add("Hotel");
        rs.add("Cinema");
        rs.add("Cafe");
        rs.add("Hospital");
        rs.add("Restaurant / Food");
        rs.add("Police");
        rs.add("ATM");
        rs.add("Bank");
        return rs;
    }

    public static List<String> getListRoom() {
        List<String> rs = new ArrayList<>();
        rs.add("Single");
        rs.add("Double");
        rs.add("Group");
        return rs;
    }

}
