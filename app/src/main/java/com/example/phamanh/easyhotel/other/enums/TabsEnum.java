package com.example.phamanh.easyhotel.other.enums;


import com.example.phamanh.easyhotel.R;

public enum TabsEnum {

    HOME(1, "Home", R.drawable.selector_home),
    NEARBY(2, "Nearby", R.drawable.selector_plus),
    NEWS(3, "News", R.drawable.selector_share),
    SETTING(4, "Settings", R.drawable.selector_setting),
    USER(1, "User", R.drawable.selector_user),
    HOTEL(2, "Hotel", R.drawable.selector_home),
    SERVICE(3, "Service", R.drawable.selector_service),
    BOOKING(4, "Booking", R.drawable.selector_booking),
    MANAGER(5, "Settings", R.drawable.selector_setting);

    private String title;
    private int idIcon;
    private int order;

    TabsEnum(int order, String title, int idIcon) {
        this.title = title;
        this.idIcon = idIcon;
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public int getIdIcon() {
        return idIcon;
    }

    public int getOrder() {
        return order;
    }
}
