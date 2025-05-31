package com.destroflyer.battlebuds.client.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GuiShopSlot {
    private String iconPath;
    private String name;
    private String[] traits;
    private int cost;
}
