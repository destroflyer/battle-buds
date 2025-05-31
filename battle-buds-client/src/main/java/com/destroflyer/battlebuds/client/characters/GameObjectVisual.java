package com.destroflyer.battlebuds.client.characters;

import com.destroflyer.battlebuds.client.JMonkeyUtil;
import com.destroflyer.battlebuds.client.gui.GuiUtils;
import com.destroflyer.battlebuds.client.models.ModelObject;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.jme3.asset.AssetManager;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import lombok.Getter;

public class GameObjectVisual {

    public GameObjectVisual(Camera camera, AssetManager assetManager, ModelInfo modelInfo, ColorRGBA nameColor) {
        this.modelInfo = modelInfo;
        modelObject = new ModelObject(assetManager, "models/" + modelInfo.getModelName() + "/skin" + ((modelInfo.getSkinName() != null) ? "_" + modelInfo.getSkinName() : "") + ".xml");
        modelObject.addControl(new GameObjectVisualControl(this, camera));
        modelObject.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        // Calculate it here (in t-pose) before animations start changing the bounding box
        height = JMonkeyUtil.getWorldSize(modelObject).getY();
        playIdleAnimation();

        guiNode = new Node();

        lblName = new Label("");
        lblName.setFontSize(14);
        lblName.setColor(nameColor);

        healthBar = new ProgressBar();
        healthBar.setPreferredSize(new Vector3f(50, HEALTH_BAR_HEIGHT, 1));
        TbtQuadBackgroundComponent healthBarBackground = (TbtQuadBackgroundComponent) healthBar.getBackground();
        healthBarBackground.setColor(COLOR_PROGRESSBAR_BORDER);
        GuiUtils.setProgressBarColor(healthBar, COLOR_PROGRESSBAR_HEALTH_ALLY);

        manaBar = new ProgressBar();
        manaBar.setPreferredSize(new Vector3f(50, MANA_BAR_HEIGHT, 1));
        TbtQuadBackgroundComponent manaBarBackground = (TbtQuadBackgroundComponent) manaBar.getBackground();
        manaBarBackground.setColor(COLOR_PROGRESSBAR_BORDER);
        GuiUtils.setProgressBarColor(manaBar, COLOR_PROGRESSBAR_MANA);

        pansItem = new Panel[Unit.MAXIMUM_ITEM_COUNT];
        pansItemBorder = new Panel[Unit.MAXIMUM_ITEM_COUNT];
        for (int i = 0; i < pansItem.length; i++) {
            Panel panItem = new Panel();
            pansItem[i] = panItem;

            Panel panItemBorder = new Panel();
            IconComponent itemBorderIcon = new IconComponent("textures/black_border.png");
            itemBorderIcon.setIconSize(new Vector2f(ITEM_ICON_SIZE + 2, ITEM_ICON_SIZE + 2));
            panItemBorder.setBackground(itemBorderIcon);
            pansItemBorder[i] = panItemBorder;
        }
    }
    private static final ColorRGBA COLOR_PROGRESSBAR_BORDER = new ColorRGBA(0, 0.007f, 0.015f, 1);
    private static final ColorRGBA COLOR_PROGRESSBAR_HEALTH_ALLY = new ColorRGBA(0, 0.78f, 0.006f, 1);
    private static final ColorRGBA COLOR_PROGRESSBAR_HEALTH_ENEMY = new ColorRGBA(0.78f, 0.006f, 0, 1);
    private static final ColorRGBA COLOR_PROGRESSBAR_MANA = new ColorRGBA(0.009f, 0.233f, 0.54f, 1);
    private static final int HEALTH_BAR_HEIGHT = 8;
    private static final int MANA_BAR_HEIGHT = 4;
    private static final int ITEM_ICON_SIZE = 20;
    private ModelInfo modelInfo;
    @Getter
    private ModelObject modelObject;
    private float height;
    @Getter
    private Node guiNode;
    private Label lblName;
    private ProgressBar healthBar;
    private ProgressBar manaBar;
    private Panel[] pansItem;
    private Panel[] pansItemBorder;

    public void playIdleAnimation() {
        AnimationInfo idleAnimation = modelInfo.getIdleAnimation();
        Float loopDuration = ((idleAnimation != null) ? idleAnimation.getLoopDuration() : null);
        playAnimation(idleAnimation, loopDuration, true);
    }

    public void playWalkAnimation(float walkSpeed) {
        float walkStepDistance = ((modelInfo.getCustomWalkStepDistance() != null) ? modelInfo.getCustomWalkStepDistance() : 3.75f);
        float loopDuration = (walkStepDistance / walkSpeed);
        playAnimation(modelInfo.getWalkAnimation(), loopDuration, true);
    }

    public void playAttackAnimation(float loopDuration) {
        playAnimation(modelInfo.getAttackAnimation(), loopDuration, true);
    }

    public void playCastAnimation(float loopDuration) {
        playAnimation(modelInfo.getCastAnimation(), loopDuration, false);
    }

    public void playDanceAnimation() {
        AnimationInfo danceAnimation = modelInfo.getDanceAnimation();
        Float loopDuration = ((danceAnimation != null) ? danceAnimation.getLoopDuration() : null);
        playAnimation(danceAnimation, loopDuration, true);
    }

    private void playAnimation(AnimationInfo animation, Float loopDuration, boolean isLoop) {
        if (animation != null) {
            modelObject.playAnimation(animation.getName(), loopDuration, isLoop, false);
        } else if (modelInfo.getIdleAnimation() != null) {
            playIdleAnimation();
        } else {
            stopAndRewindAnimation();
        }
    }

    private void stopAndRewindAnimation() {
        modelObject.stopAndRewindAnimation();
    }

    public void setPosition(Vector2f position) {
        modelObject.setLocalTranslation(position.getX(), 0, position.getY());
    }

    public void setDirection(Vector2f direction) {
        modelObject.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI - direction.getAngle(), Vector3f.UNIT_Y));
    }

    public void setName(String name) {
        lblName.setText(name);
    }

    public void setNameVisible(boolean isVisible) {
        JMonkeyUtil.setAttached(guiNode, lblName, isVisible);
    }

    public void setAllied(boolean allied) {
        GuiUtils.setProgressBarColor(healthBar, (allied ? COLOR_PROGRESSBAR_HEALTH_ALLY : COLOR_PROGRESSBAR_HEALTH_ENEMY));
    }

    public void setMaximumHealth(float maximumHealth) {
        healthBar.getModel().setMaximum(maximumHealth);
    }

    public void setCurrentHealth(float currentHealth) {
        healthBar.setProgressValue(currentHealth);
    }

    public void setHealthVisible(boolean isVisible) {
        JMonkeyUtil.setAttached(guiNode, healthBar, isVisible);
    }

    public void setMaximumMana(float maximumMana) {
        manaBar.getModel().setMaximum(maximumMana);
    }

    public void setCurrentMana(float currentMana) {
        manaBar.setProgressValue(currentMana);
    }

    public void setManaVisible(boolean isVisible) {
        JMonkeyUtil.setAttached(guiNode, manaBar, isVisible);
    }

    public void setItems(String[] iconPaths) {
        for (int i = 0; i < pansItem.length; i++) {
            boolean isVisible = (i < iconPaths.length);
            if (isVisible) {
                IconComponent itemIcon = new IconComponent(iconPaths[i]);
                itemIcon.setIconSize(new Vector2f(ITEM_ICON_SIZE, ITEM_ICON_SIZE));
                pansItem[i].setBackground(itemIcon);
            }
            JMonkeyUtil.setAttached(guiNode, pansItem[i], isVisible);
            JMonkeyUtil.setAttached(guiNode ,pansItemBorder[i], isVisible);
        }
    }

    private boolean areItemsVisible() {
        for (Panel panItem : pansItem) {
            if (panItem.getParent() != null) {
                return true;
            }
        }
        return false;
    }

    public void setColor(ColorRGBA color) {
        ModelColorizer.setColor(modelObject.getOriginalRegisteredModel().getNode(), color);
    }

    public void updateGuiControlPositions(Camera camera) {
        int additionalScreenY = 0;
        boolean hasItems = areItemsVisible();
        if (hasItems) {
            additionalScreenY += ITEM_ICON_SIZE;
        }
        additionalScreenY += 20;
        placeAboveModel(camera, lblName, 0, additionalScreenY, 0);
        additionalScreenY -= 20;
        placeAboveModel(camera, healthBar, 0, additionalScreenY, 0);
        additionalScreenY -= (HEALTH_BAR_HEIGHT - 1);
        placeAboveModel(camera, manaBar, 0, additionalScreenY, 0);
        if (hasItems) {
            additionalScreenY -= MANA_BAR_HEIGHT;
            for (int i = 0; i < pansItem.length; i++) {
                int additionalScreenX = (int) ((i - ((Unit.MAXIMUM_ITEM_COUNT / 2f) - 0.5f)) * (ITEM_ICON_SIZE + 1));
                placeAboveModel(camera, pansItem[i], additionalScreenX, additionalScreenY, 1);
                // Move border 1px to the right, so that it aligns with the icons properly (the centering mechanism otherwise moves it 1px too far to the left)
                placeAboveModel(camera, pansItemBorder[i], additionalScreenX - 1 + 1, additionalScreenY + 1, 0);
            }
        }
    }

    private void placeAboveModel(Camera camera, Panel panel, int additionalScreenX, int additionalScreenY, int additionalScreenZ) {
        Vector3f screenPosition = camera.getScreenCoordinates(modelObject.getWorldTranslation().add(0, height + 1, 0));
        screenPosition.addLocal((panel.getPreferredSize().getX() / -2) + additionalScreenX, additionalScreenY, additionalScreenZ);
        panel.setLocalTranslation(screenPosition);
    }
}
