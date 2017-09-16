package com.example.phamanh.easyhotel.other.enums;


import com.example.phamanh.easyhotel.R;

public enum TabsEnum {

    HOME(1,"Trang Chủ", R.drawable.selector_home),
    DISCOVER(2, "Bản Đồ", R.drawable.selector_discover),
    ADD(3, "Quanh Đây", R.drawable.selector_plus),
    SHARE(4, "Tin Tức", R.drawable.selector_share),
    SETTING(5, "Cài Đặt", R.drawable.selector_setting);

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
