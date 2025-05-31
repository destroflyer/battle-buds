package com.destroflyer.battlebuds.client.gui;

import com.jme3.math.ColorRGBA;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GuiVerticalListItem {
    private ColorRGBA customBackgroundColor;
    private String iconPath;
    private String text;
    private String tooltip;
    private Runnable onClick;
}
