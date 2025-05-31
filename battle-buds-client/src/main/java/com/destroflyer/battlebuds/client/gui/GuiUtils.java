package com.destroflyer.battlebuds.client.gui;

import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

public class GuiUtils {

    public static ColorRGBA COLOR_PANEL_DEFAULT = new ColorRGBA(0, 0.75f, 0.75f, 0.5f);
    public static ColorRGBA COLOR_BUTTON_DEFAULT = COLOR_PANEL_DEFAULT;
    public static ColorRGBA COLOR_BUTTON_ACTIVE = new ColorRGBA(0.75f, 0.75f, 0, 0.5f);

    public static void setProgressBarColor(ProgressBar progressBar, ColorRGBA color) {
        QuadBackgroundComponent background = (QuadBackgroundComponent) progressBar.getValueIndicator().getBackground();
        background.setColor(color);
    }

    public static void markButtonAsActive(Button button, boolean isActive) {
        ColorRGBA backgroundColor = (isActive ? COLOR_BUTTON_ACTIVE : COLOR_BUTTON_DEFAULT);
        setPanelBackgroundColor(button, backgroundColor);
    }

    public static void setPanelBackgroundColor(Panel panel, ColorRGBA color) {
        TbtQuadBackgroundComponent background = (TbtQuadBackgroundComponent) panel.getBackground();
        background.setColor(color);
    }
}
