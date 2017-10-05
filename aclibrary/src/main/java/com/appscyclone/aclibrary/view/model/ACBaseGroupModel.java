package com.appscyclone.aclibrary.view.model;
/*
 * Created by HoangDong on 19/09/2017.
 */

import java.util.List;

public class ACBaseGroupModel<T> {
    private String title;
    private List<T> child;

    public ACBaseGroupModel(String title, List<T> child) {
        this.title = title;
        this.child = child;
    }
    public ACBaseGroupModel() {}

    public void setChild(List<T> child)
    {
        this.child=child;
    }
    protected void setChild(){};

    public String getTitle() {
        return title;
    }

    public List<T> getItems() {
        return child;
    }

    public int getItemCount() {
        if(child==null)
            setChild();
        return child == null ? 0 : child.size();
    }
}
