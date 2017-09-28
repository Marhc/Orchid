package com.eden.orchid.impl.server.admin.lists;

import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class MenuItemsList implements AdminList<OrchidMenuItem> {

    private Set<OrchidMenuItem> list;

    @Inject
    public MenuItemsList(Set<OrchidMenuItem> list) {
        this.list = new TreeSet<>(list);
    }

    @Override
    public String getKey() {
        return "menuItems";
    }

    @Override
    public Collection<OrchidMenuItem> getItems() {
        return list;
    }

    @Override
    public String getItemId(OrchidMenuItem item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public OrchidMenuItem getItem(String id) {
        for(OrchidMenuItem item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
