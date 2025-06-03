package com.destroflyer.battlebuds.client.appstates;

import com.destroflyer.battlebuds.client.JMonkeyUtil;
import com.destroflyer.battlebuds.client.gui.*;
import com.destroflyer.battlebuds.shared.game.*;
import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.boards.TimeBoard;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;
import com.destroflyer.battlebuds.shared.game.objects.players.HumanPlayer;
import com.destroflyer.battlebuds.shared.game.traits.Traits;
import com.destroflyer.battlebuds.shared.network.messages.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.MouseInput;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.event.DefaultMouseListener;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameGuiAppState extends BaseClientAppState {

    private static final int DISPLAYED_PLAYERS = 8;
    private static final int DISPLAYED_ROUNDS = 6;
    private static final int DISPLAYED_PROBABILITIES = 5;
    private static final int DISPLAYED_SHOP_SLOTS = 5;
    private static final int DISPLAYED_SHOP_SLOT_TRAITS = 3;
    private static final int DISPLAYED_TRAITS = 9;
    private static final int DISPLAYED_AUGMENTS = 3;

    private static final ColorRGBA VERTICAL_LIST_ITEM_DEFAULT_BACKGROUND_COLOR = new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f);
    private static final HashMap<Tier, ColorRGBA> TIER_COLORS = new HashMap<>();
    static {
        TIER_COLORS.put(Tier.BRONZE, new ColorRGBA(0.5f, 0.2f, 0.2f, 0.6f));
        TIER_COLORS.put(Tier.SILVER, new ColorRGBA(0.6f, 0.6f, 0.6f, 0.6f));
        TIER_COLORS.put(Tier.GOLD, new ColorRGBA(0.9f, 0.8f, 0.15f, 0.6f));
        TIER_COLORS.put(Tier.PRISMATIC, new ColorRGBA(0.2f, 1, 1, 0.6f));
    }

    private int totalWidth;
    private int totalHeight;

    private int sideMargin = 20;
    private int verticalListWidth = 200;
    private int itemSize = 50;

    private Node guiNode;

    // Rounds
    private Node guiNodeRounds;
    private Label lblRound;
    private Container[] pansRoundIcon;
    private ProgressBar boardTimeBar;
    // Players
    private VerticalList verticalListPlayers;
    // Traits
    private VerticalList verticalListTraits;
    // Shop
    private Node guiNodeShop;
    private Label lblLevel;
    private Label lblLevelProgress;
    private Label[] lblsShopProbability;
    private Container[] pansShopSlotIcon;
    private Container[] pansShopSlotIconWrapper;
    private Label[] lblsSlotName;
    private Label[][] lblsSlotTrait;
    private Button btnBuyExperience;
    private Button btnReroll;
    private Label lblGold;
    private Label lblStreak;
    private Container panLockIcon;
    // Items
    private Node guiNodeItems;
    private Container[] pansItemIcon;
    private Vector3f[] itemIconLocations;
    private SimpleMouseHoverListener[] itemMouseHoverListeners;
    private Integer hoveredItemIndex;
    private Integer draggedItemIndex;
    // Inspect
    private Node guiNodeInspect;
    private Container panInspectUnitIcon;
    private Panel[] pansInspectItemIcon;
    private SimpleMouseHoverListener[] inspectItemMouseHoverListeners;
    private Label lblInspectName;
    private Label lblInspectDescription;
    @Setter
    private Integer inspectedUnitId;
    // Augments
    private Node guiNodeAugments;
    private Container[][] containersAugment;
    private Label[][] lblsAugment;
    private SimpleMouseHoverListener[][] augmentHouseHoverListeners;
    // Decision
    private Node guiNodeDecision;
    private Container[] containersDecisionOption;
    private Label[] lblsDecisionOption;
    // Tooltip
    private Container containerTooltip;
    private Label lblTooltip;
    // GameOver
    private Container containerGameOver;
    private Label lblGameOver;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);

        AppSettings appSettings = mainApplication.getContext().getSettings();
        totalWidth = appSettings.getWidth();
        totalHeight = appSettings.getHeight();

        guiNode = new Node();

        createRounds();
        createPlayers();
        createTraits();
        createItems();
        createShop();
        createInspect();
        createAugments();
        createDecision();
        createGameOverContainer();
        createTooltip();

        mainApplication.getGuiNode().attachChild(guiNode);
    }

    private void createRounds() {
        int containerPadding = 3;
        int roundLabelWith = 30;
        int roundIconSize = 28;
        int borderSize = 1;

        int containerWidth = (containerPadding + roundLabelWith + containerPadding + (DISPLAYED_ROUNDS * (roundIconSize + containerPadding)));
        int containerHeight = (containerPadding + roundIconSize + containerPadding);
        int containerInnerHeight = (containerHeight - (2 * containerPadding));

        int containerX = ((totalWidth / 2) - (containerWidth / 2));
        int containerY = totalHeight;

        guiNodeRounds = new Node();
        guiNode.attachChild(guiNodeRounds);

        Container container = new Container();
        container.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        container.setLocalTranslation(containerX, containerY, 0);
        container.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));
        guiNodeRounds.attachChild(container);

        int roundLabelX = containerX + containerPadding;
        int roundLabelY = containerY - containerPadding;
        lblRound = new Label("7-5");
        lblRound.setLocalTranslation(roundLabelX, roundLabelY, 1);
        lblRound.setPreferredSize(new Vector3f(roundLabelWith, containerInnerHeight, 0));
        lblRound.setTextVAlignment(VAlignment.Center);
        lblRound.setFontSize(16);
        lblRound.setColor(ColorRGBA.White);
        guiNodeRounds.attachChild(lblRound);

        pansRoundIcon = new Container[DISPLAYED_ROUNDS];
        int roundX = containerX + containerPadding + roundLabelWith + containerPadding;
        int roundY = containerY - containerPadding;
        for (int i = 0; i < DISPLAYED_ROUNDS; i++) {
            Container panRoundIcon = new Container();
            panRoundIcon.setLocalTranslation(roundX, roundY, 2);
            IconComponent roundIcon = new IconComponent("textures/characters/alice.png");
            roundIcon.setIconSize(new Vector2f(roundIconSize, roundIconSize));
            panRoundIcon.setBackground(roundIcon);
            guiNodeRounds.attachChild(panRoundIcon);
            pansRoundIcon[i] = panRoundIcon;

            if (i == 0) {
                int borderX = roundX - borderSize;
                int borderY = roundY + borderSize;
                int borderBackgroundSize = roundIconSize + (2 * borderSize);
                Container panBorder = new Container();
                panBorder.setLocalTranslation(borderX, borderY, 1);
                IconComponent borderIcon = new IconComponent("textures/white_border.png");
                borderIcon.setIconSize(new Vector2f(borderBackgroundSize, borderBackgroundSize));
                panBorder.setBackground(borderIcon);
                guiNodeRounds.attachChild(panBorder);
            }

            roundX += roundIconSize + containerPadding;
        }

        boardTimeBar = new ProgressBar();
        boardTimeBar.setLocalTranslation(containerX, containerY - containerHeight + borderSize, 0);
        boardTimeBar.setPreferredSize(new Vector3f(containerWidth, 10, 1));
        GuiUtils.setProgressBarColor(boardTimeBar, new ColorRGBA(0.119f, 0.67f, 0.67f, 1));
    }

    private void createShop() {
        int containerPadding = 3;
        int slotSize = 120;
        int outerWrapperHeightBig = 30;
        int outerWrapperHeightSmall = 22;

        int leftButtonsWidth = slotSize;
        int leftButtonsHeight = ((slotSize- containerPadding) / 2);

        int containerWidth = (containerPadding + leftButtonsWidth + containerPadding + (DISPLAYED_SHOP_SLOTS * (slotSize + containerPadding)));
        int containerHeight = (containerPadding + slotSize + containerPadding);

        int containerX = ((totalWidth / 2) - (containerWidth / 2));
        int containerY = containerHeight;

        guiNodeShop = new Node();
        guiNode.attachChild(guiNodeShop);

        Container container = new Container();
        container.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        container.setLocalTranslation(containerX, containerY, 0);
        container.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));
        guiNodeShop.attachChild(container);

        int levelWrapperWidth = (containerPadding + slotSize + containerPadding);
        // Looks a bit more aligned
        levelWrapperWidth -= 1;
        int levelX = containerX;
        int levelY = containerY + outerWrapperHeightBig;

        Container panLevelWrapper = new Container();
        panLevelWrapper.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        panLevelWrapper.setLocalTranslation(levelX, levelY, 0);
        panLevelWrapper.setPreferredSize(new Vector3f(levelWrapperWidth, outerWrapperHeightBig, 0));
        guiNodeShop.attachChild(panLevelWrapper);

        lblLevel = new Label("Lvl. 9");
        lblLevel.setLocalTranslation(levelX + containerPadding, levelY, 1);
        lblLevel.setPreferredSize(new Vector3f(levelWrapperWidth, outerWrapperHeightBig, 1));
        lblLevel.setTextVAlignment(VAlignment.Center);
        lblLevel.setFontSize(16);
        lblLevel.setColor(ColorRGBA.White);
        guiNodeShop.attachChild(lblLevel);

        // Looks a bit more aligned
        int levelProgressVisualAdjustmentY = -4;
        lblLevelProgress = new Label("30/84");
        lblLevelProgress.setLocalTranslation(levelX + containerPadding, levelY + levelProgressVisualAdjustmentY, 1);
        lblLevelProgress.setPreferredSize(new Vector3f(levelWrapperWidth - (2 * containerPadding), outerWrapperHeightBig, 1));
        lblLevelProgress.setTextHAlignment(HAlignment.Right);
        lblLevelProgress.setTextVAlignment(VAlignment.Center);
        lblLevelProgress.setFontSize(12);
        lblLevelProgress.setColor(ColorRGBA.White);

        int probabilityWidth = 34;
        int probabilityHeight = (outerWrapperHeightSmall - (2 * containerPadding));
        int probabilityWrapperX = levelX + levelWrapperWidth;
        int probabilityWrapperY = containerY + outerWrapperHeightSmall;
        int probabilityWrapperWidth = (containerPadding + (DISPLAYED_PROBABILITIES * probabilityWidth) + containerPadding);

        Container panProbabilitiesWrapper = new Container();
        panProbabilitiesWrapper.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        panProbabilitiesWrapper.setLocalTranslation(probabilityWrapperX, probabilityWrapperY, 0);
        panProbabilitiesWrapper.setPreferredSize(new Vector3f(probabilityWrapperWidth, outerWrapperHeightSmall, 0));
        guiNodeShop.attachChild(panProbabilitiesWrapper);

        lblsShopProbability = new Label[DISPLAYED_PROBABILITIES];
        int probabilityX = probabilityWrapperX + containerPadding;
        int probabilityY = probabilityWrapperY - containerPadding;
        for (int i = 0; i < DISPLAYED_PROBABILITIES; i++) {
            Label lblProbability = new Label(((i == 0) ? 100 : 50) + "%");
            lblProbability.setLocalTranslation(probabilityX, probabilityY, 1);
            lblProbability.setPreferredSize(new Vector3f(probabilityWidth, probabilityHeight, 1));
            lblProbability.setTextHAlignment(HAlignment.Center);
            lblProbability.setTextVAlignment(VAlignment.Center);
            lblProbability.setFontSize(12);
            lblProbability.setColor(ColorRGBA.White);
            guiNodeShop.attachChild(lblProbability);
            probabilityX += probabilityWidth;
            lblsShopProbability[i] = lblProbability;
        }

        int leftButtonsX = containerX + containerPadding;
        int leftButtonsY = containerY - containerPadding;

        btnBuyExperience = new Button("Buy XP (4g)");
        btnBuyExperience.setLocalTranslation(leftButtonsX, leftButtonsY, 1);
        btnBuyExperience.setPreferredSize(new Vector3f(leftButtonsWidth, leftButtonsHeight, 1));
        btnBuyExperience.setInsets(new Insets3f(0, 0, 0, 0));
        btnBuyExperience.setTextHAlignment(HAlignment.Center);
        btnBuyExperience.setTextVAlignment(VAlignment.Center);
        btnBuyExperience.setFontSize(16);
        btnBuyExperience.setColor(ColorRGBA.White);
        btnBuyExperience.addClickCommands(_ -> getAppState(ClientNetworkAppState.class).send(new BuyExperienceMessage()));
        guiNodeShop.attachChild(btnBuyExperience);

        leftButtonsY -= leftButtonsHeight + containerPadding;
        btnReroll = new Button("Reroll (2g)");
        btnReroll.setLocalTranslation(leftButtonsX, leftButtonsY, 1);
        btnReroll.setPreferredSize(new Vector3f(leftButtonsWidth, leftButtonsHeight, 1));
        btnReroll.setInsets(new Insets3f(0, 0, 0, 0));
        btnReroll.setTextHAlignment(HAlignment.Center);
        btnReroll.setTextVAlignment(VAlignment.Center);
        btnReroll.setFontSize(16);
        btnReroll.setColor(ColorRGBA.White);
        btnReroll.addClickCommands(_ -> getAppState(ClientNetworkAppState.class).send(new BuyRerollMessage()));
        guiNodeShop.attachChild(btnReroll);

        pansShopSlotIcon = new Container[DISPLAYED_SHOP_SLOTS];
        pansShopSlotIconWrapper = new Container[DISPLAYED_SHOP_SLOTS];
        lblsSlotName = new Label[DISPLAYED_SHOP_SLOTS];
        lblsSlotTrait = new Label[DISPLAYED_SHOP_SLOTS][DISPLAYED_SHOP_SLOT_TRAITS];
        int slotX = containerX + containerPadding + leftButtonsWidth + containerPadding;
        int slotY = containerY - containerPadding;
        int slotPaddingX = 5;
        int slotLabelHeight = 20;
        for (int i = 0; i < DISPLAYED_SHOP_SLOTS; i++) {
            int _i = i;

            Container panSlotIcon = new Container();
            panSlotIcon.setLocalTranslation(slotX, slotY, 1);
            IconComponent slotIcon = new IconComponent("textures/characters/tristan.png");
            slotIcon.setIconSize(new Vector2f(slotSize, slotSize));
            panSlotIcon.setBackground(slotIcon);
            SimpleMouseClickListener mouseClickListener = new SimpleMouseClickListener();
            mouseClickListener.setOnClick(() -> getAppState(ClientNetworkAppState.class).send(new BuyUnitMessage(_i)));
            panSlotIcon.addMouseListener(mouseClickListener);
            pansShopSlotIcon[i] = panSlotIcon;

            Container panSlotIconWrapper = new Container();
            panSlotIconWrapper.setLocalTranslation(slotX, slotY, 2);
            IconComponent slotIconWrapper = new IconComponent("textures/shop/" + (i + 1) + ".png");
            slotIconWrapper.setIconSize(new Vector2f(slotSize, slotSize));
            panSlotIconWrapper.setBackground(slotIconWrapper);
            pansShopSlotIconWrapper[i] = panSlotIconWrapper;

            int slotLabelX = slotX + slotPaddingX;
            int slotLabelY = slotY - slotSize + 3 + slotLabelHeight;
            Label lblSlotName = new Label("Senna (" + (i + 1) + "g)");
            lblSlotName.setLocalTranslation(slotLabelX, slotLabelY, 3);
            lblSlotName.setPreferredSize(new Vector3f(slotSize, slotLabelHeight, 1));
            lblSlotName.setFontSize(16);
            lblSlotName.setColor(ColorRGBA.White);
            lblsSlotName[i] = lblSlotName;

            for (int r = (DISPLAYED_SHOP_SLOT_TRAITS - 1); r >= 0; r--) {
                slotLabelY += slotLabelHeight + 2;
                Label lblSlotTrait = new Label("Divinicorp");
                lblSlotTrait.setLocalTranslation(slotLabelX, slotLabelY, 3);
                lblSlotTrait.setPreferredSize(new Vector3f(slotSize, slotLabelHeight, 1));
                lblSlotTrait.setFontSize(12);
                lblSlotTrait.setColor(ColorRGBA.White);
                lblsSlotTrait[i][r] = lblSlotTrait;
            }

            slotX += slotSize + containerPadding;
        }

        int goldWrapperWidth = 80;
        int goldX = (containerX + (containerWidth / 2) - (goldWrapperWidth / 2));
        int goldY = containerY + outerWrapperHeightBig;

        Container panGoldWrapper = new Container();
        panGoldWrapper.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        panGoldWrapper.setLocalTranslation(goldX, goldY, 0);
        panGoldWrapper.setPreferredSize(new Vector3f(goldWrapperWidth, outerWrapperHeightBig, 0));
        guiNodeShop.attachChild(panGoldWrapper);

        lblGold = new Label("30g");
        lblGold.setLocalTranslation(goldX, goldY, 1);
        lblGold.setPreferredSize(new Vector3f(goldWrapperWidth, outerWrapperHeightBig, 1));
        lblGold.setTextHAlignment(HAlignment.Center);
        lblGold.setTextVAlignment(VAlignment.Center);
        lblGold.setFontSize(16);
        lblGold.setColor(ColorRGBA.White);
        guiNodeShop.attachChild(lblGold);

        int streakWidth = 40;
        int streakX = goldX + goldWrapperWidth;
        int streakY = containerY + outerWrapperHeightSmall;

        Container panStreakWrapper = new Container();
        panStreakWrapper.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        panStreakWrapper.setLocalTranslation(streakX, streakY, 0);
        panStreakWrapper.setPreferredSize(new Vector3f(streakWidth, outerWrapperHeightSmall, 0));
        SimpleMouseHoverListener streakMouseHoverListener = new SimpleMouseHoverListener();
        streakMouseHoverListener.setOnEnter(() -> showTooltip(panStreakWrapper, "TODO: Support streak gold", true));
        streakMouseHoverListener.setOnExit(this::hideTooltip);
        panStreakWrapper.addMouseListener(streakMouseHoverListener);
        guiNodeShop.attachChild(panStreakWrapper);

        lblStreak = new Label("+4");
        lblStreak.setLocalTranslation(streakX, streakY, 1);
        lblStreak.setPreferredSize(new Vector3f(streakWidth, outerWrapperHeightSmall, 1));
        lblStreak.setTextHAlignment(HAlignment.Center);
        lblStreak.setTextVAlignment(VAlignment.Center);
        lblStreak.setFontSize(12);
        lblStreak.setColor(ColorRGBA.White);
        guiNodeShop.attachChild(lblStreak);

        int lockWrapperWidth = 50;
        int lockX = containerX + containerWidth - lockWrapperWidth;
        int lockY = containerY + outerWrapperHeightBig;
        int lockIconSize = outerWrapperHeightBig;
        int lockIconX = lockX + (lockWrapperWidth / 2) - (lockIconSize / 2);

        Container panLockIconWrapper = new Container();
        panLockIconWrapper.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        panLockIconWrapper.setLocalTranslation(lockX, lockY, 0);
        panLockIconWrapper.setPreferredSize(new Vector3f(lockWrapperWidth, outerWrapperHeightBig, 0));
        SimpleMouseHoverListener lockMouseHoverListener = new SimpleMouseHoverListener();
        lockMouseHoverListener.setOnEnter(() -> showTooltip(panLockIconWrapper, "TODO: Support shop locking", true));
        lockMouseHoverListener.setOnExit(this::hideTooltip);
        panLockIconWrapper.addMouseListener(lockMouseHoverListener);
        guiNodeShop.attachChild(panLockIconWrapper);

        panLockIcon = new Container();
        panLockIcon.setLocalTranslation(lockIconX, lockY, 1);
        IconComponent lockIcon = new IconComponent("textures/lock.png");
        lockIcon.setIconSize(new Vector2f(lockIconSize, lockIconSize));
        panLockIcon.setBackground(lockIcon);
        guiNodeShop.attachChild(panLockIcon);
    }

    private void createPlayers() {
        int playersX = (totalWidth - sideMargin - verticalListWidth);
        verticalListPlayers = createVerticalList(playersX, DISPLAYED_PLAYERS, false);
    }

    private void createTraits() {
        int traitsX = 61;
        verticalListTraits = createVerticalList(traitsX, DISPLAYED_TRAITS, true);
    }

    record VerticalList (
        Container[] containers,
        Container[] pansIcon,
        Label[] lblsText,
        Container containerMore,
        Label lblMore,
        SimpleMouseClickListener[] mouseClickListeners,
        SimpleMouseHoverListener[] mouseHoverListeners
    ) {}
    private VerticalList createVerticalList(int x, int count, boolean withMore) {
        Container[] containers = new Container[count];
        Container[] pansIcons = new Container[count];
        Label[] lblsText = new Label[count];
        Container containerMore = null;
        Label lblMore = null;
        SimpleMouseClickListener[] mouseClickListeners = new SimpleMouseClickListener[count];
        SimpleMouseHoverListener[] mouseHoverListeners = new SimpleMouseHoverListener[count];

        int displayedCount = count;
        if (withMore) {
            displayedCount += 1;
        }

        int width = 200;
        int height = 50;
        int padding = 2;
        int margin = 5;
        int iconAndTextGap = 7;
        int innerHeight = (height - (2 * padding));
        int y = ((totalHeight / 2) + (((displayedCount * height) + ((displayedCount - 1) * margin)) / 2));
        int iconX = (x + padding);
        int textX = (iconX + innerHeight + iconAndTextGap);
        int textWidth = (width - padding - innerHeight - iconAndTextGap - padding);

        for (int i = 0; i < displayedCount; i++) {
            int innerY = (y - padding);

            if (i < count) {
                Container container = new Container();
                container.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
                container.setLocalTranslation(x, y, 0);
                container.setPreferredSize(new Vector3f(width, height, 0));
                SimpleMouseClickListener mouseClickListener = new SimpleMouseClickListener();
                container.addMouseListener(mouseClickListener);
                mouseClickListeners[i] = mouseClickListener;
                containers[i] = container;

                Container panIcon = new Container();
                panIcon.setLocalTranslation(iconX, innerY, 1);
                IconComponent icon = new IconComponent("textures/characters/garmon.png");
                icon.setIconSize(new Vector2f(innerHeight, innerHeight));
                panIcon.setBackground(icon);
                pansIcons[i] = panIcon;

                Label lblText = new Label("destroflyer (100)");
                lblText.setLocalTranslation(textX, innerY, 1);
                lblText.setPreferredSize(new Vector3f(textWidth, innerHeight, 1));
                lblText.setTextVAlignment(VAlignment.Center);
                lblText.setFontSize(16);
                lblText.setColor(ColorRGBA.White);
                lblsText[i] = lblText;

                SimpleMouseHoverListener mouseHoverListener = new SimpleMouseHoverListener();
                container.addMouseListener(mouseHoverListener);
                mouseHoverListeners[i] = mouseHoverListener;
            } else {
                containerMore = new Container();
                containerMore.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
                containerMore.setLocalTranslation(iconX, innerY, 0);
                containerMore.setPreferredSize(new Vector3f(innerHeight, innerHeight, 0));

                lblMore = new Label("+2");
                lblMore.setLocalTranslation(iconX, innerY, 1);
                lblMore.setPreferredSize(new Vector3f(innerHeight, innerHeight, 1));
                lblMore.setTextHAlignment(HAlignment.Center);
                lblMore.setTextVAlignment(VAlignment.Center);
                lblMore.setFontSize(16);
                lblMore.setColor(ColorRGBA.White);
            }

            y -= (height + margin);
        }

        return new VerticalList(containers, pansIcons, lblsText, containerMore, lblMore, mouseClickListeners, mouseHoverListeners);
    }

    private void createItems() {
        int padding = 3;

        int containerWidth = (padding + itemSize + padding);
        int containerHeight = (padding + (ActualPlayer.MAXIMUM_ITEM_COUNT * (itemSize + padding)));
        int containerX = 0;
        int containerY = ((totalHeight / 2) + (containerHeight / 2));

        guiNodeItems = new Node();
        guiNode.attachChild(guiNodeItems);

        Container container = new Container();
        container.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        container.setLocalTranslation(containerX, containerY, 0);
        container.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));
        guiNodeItems.attachChild(container);

        pansItemIcon = new Container[ActualPlayer.MAXIMUM_ITEM_COUNT];
        itemIconLocations = new Vector3f[ActualPlayer.MAXIMUM_ITEM_COUNT];
        itemMouseHoverListeners = new SimpleMouseHoverListener[ActualPlayer.MAXIMUM_ITEM_COUNT];
        int itemX = containerX + padding;
        int itemY = containerY - padding;
        for (int i = 0; i < ActualPlayer.MAXIMUM_ITEM_COUNT; i++) {
            int _i = i;

            Container panItemBackground = new Container();
            panItemBackground.setLocalTranslation(new Vector3f(itemX, itemY, 1));
            IconComponent itemBackgroundIcon = new IconComponent("textures/items/none.png");
            itemBackgroundIcon.setIconSize(new Vector2f(itemSize, itemSize));
            panItemBackground.setBackground(itemBackgroundIcon);
            guiNodeItems.attachChild(panItemBackground);

            Container panItemIcon = new Container();
            panItemIcon.setLocalTranslation(new Vector3f(itemX, itemY, 2));
            IconComponent itemIcon = new IconComponent("textures/characters/elven_archer.png");
            itemIcon.setIconSize(new Vector2f(itemSize, itemSize));
            panItemIcon.setBackground(itemIcon);
            panItemIcon.addMouseListener(new DefaultMouseListener() {

                @Override
                public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture) {
                    super.mouseEntered(event, target, capture);
                    hoveredItemIndex = _i;
                }

                @Override
                public void mouseExited(MouseMotionEvent event, Spatial target, Spatial capture) {
                    super.mouseExited(event, target, capture);
                    hoveredItemIndex = null;
                }

                @Override
                public void mouseButtonEvent(MouseButtonEvent event, Spatial target, Spatial capture) {
                    super.mouseButtonEvent(event, target, capture);
                    if (event.getButtonIndex() == MouseInput.BUTTON_LEFT) {
                        if (event.isPressed()) {
                            draggedItemIndex = _i;
                            hideTooltip();
                        } else if (draggedItemIndex != null) {
                            Integer hoveredObjectId = getAppState(GameWorldAppState.class).getHoveredObjectId();
                            if (hoveredObjectId != null) {
                                getAppState(ClientNetworkAppState.class).send(new UseItemMessage(draggedItemIndex, hoveredObjectId));
                            } else if (hoveredItemIndex != null) {
                                getAppState(ClientNetworkAppState.class).send(new CombineBenchItemsMessage(draggedItemIndex, hoveredItemIndex));
                            }
                            draggedItemIndex = null;
                        }
                    }
                }
            });
            SimpleMouseHoverListener itemMouseHoverListener = new SimpleMouseHoverListener();
            panItemIcon.addMouseListener(itemMouseHoverListener);
            pansItemIcon[i] = panItemIcon;
            itemIconLocations[i] = panItemIcon.getLocalTranslation().clone();
            itemMouseHoverListeners[i] = itemMouseHoverListener;

            itemY -= itemSize + padding;
        }
    }

    private void createGameOverContainer() {
        int gameOverInsetsX = 400;
        int gameOverInsetsBorder = 200;
        int gameOverInsetsMiddle = 0;
        containerGameOver = new Container();
        containerGameOver.setLocalTranslation(0, totalHeight, 999);
        containerGameOver.setPreferredSize(new Vector3f(totalWidth, totalHeight, 0));
        TbtQuadBackgroundComponent containerGameOverBackground = (TbtQuadBackgroundComponent) containerGameOver.getBackground();
        containerGameOverBackground.setColor(new ColorRGBA(0, 0, 0, 0.8f));
        lblGameOver = new Label("");
        lblGameOver.setInsets(new Insets3f(gameOverInsetsBorder, gameOverInsetsX, gameOverInsetsMiddle, gameOverInsetsX));
        lblGameOver.setTextHAlignment(HAlignment.Center);
        lblGameOver.setTextVAlignment(VAlignment.Center);
        lblGameOver.setFontSize(40);
        lblGameOver.setColor(ColorRGBA.White);
        containerGameOver.addChild(lblGameOver);
        Button btnBackToMenu = new Button("Continue");
        btnBackToMenu.setInsets(new Insets3f(gameOverInsetsMiddle, gameOverInsetsX, gameOverInsetsBorder, gameOverInsetsX));
        btnBackToMenu.setTextHAlignment(HAlignment.Center);
        btnBackToMenu.setTextVAlignment(VAlignment.Center);
        btnBackToMenu.setFontSize(40);
        btnBackToMenu.setColor(ColorRGBA.White);
        btnBackToMenu.addCommands(Button.ButtonAction.Up, _ -> mainApplication.exitGame());
        containerGameOver.addChild(btnBackToMenu);
    }

    private void createInspect() {
        float containerWidth = 250;
        float containerHeight = 610;
        float containerX = (totalWidth - containerWidth);
        float containerY = ((totalHeight / 2f) + (containerHeight / 2f));
        float padding = 10;
        float iconUnitSize = 50;
        float iconItemSize = 50;
        float buttonHeight = 40;

        guiNodeInspect = new Node();
        // In front of enemy augments
        guiNodeInspect.setLocalTranslation(0, 0, 10);
        guiNode.attachChild(guiNodeInspect);

        Container containerInspect = new Container();
        containerInspect.setLocalTranslation(containerX, containerY, 0);
        containerInspect.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));
        guiNodeInspect.attachChild(containerInspect);

        panInspectUnitIcon = new Container();
        panInspectUnitIcon.setLocalTranslation(containerX + padding, containerY - padding, 1);
        IconComponent inspectUnitIcon = new IconComponent("textures/characters/elven_archer.png");
        inspectUnitIcon.setIconSize(new Vector2f(iconUnitSize, iconUnitSize));
        panInspectUnitIcon.setBackground(inspectUnitIcon);
        guiNodeInspect.attachChild(panInspectUnitIcon);

        lblInspectName = new Label("");
        lblInspectName.setLocalTranslation(containerX + padding + iconUnitSize + padding, containerY - padding, 1);
        lblInspectName.setPreferredSize(new Vector3f(containerWidth - padding - iconUnitSize - padding - padding, iconUnitSize, 0));
        lblInspectName.setTextVAlignment(VAlignment.Center);
        lblInspectName.setFontSize(20);
        lblInspectName.setColor(ColorRGBA.White);
        lblInspectName.setTextVAlignment(VAlignment.Center);
        guiNodeInspect.attachChild(lblInspectName);

        pansInspectItemIcon = new Panel[Unit.MAXIMUM_ITEM_COUNT];
        inspectItemMouseHoverListeners = new SimpleMouseHoverListener[Unit.MAXIMUM_ITEM_COUNT];
        for (int i = 0; i < pansInspectItemIcon.length; i++) {
            Panel panInspectItemIcon = new Panel();
            panInspectItemIcon.setLocalTranslation(containerX + (containerWidth / 2f) - ((iconItemSize + padding + iconItemSize + padding + iconItemSize) / 2f) + (i * (iconItemSize + padding)), containerY - padding - iconUnitSize - padding, 1);
            IconComponent inspectItemIcon = new IconComponent("textures/items/none.png");
            inspectItemIcon.setIconSize(new Vector2f(iconItemSize, iconItemSize));
            panInspectItemIcon.setBackground(inspectItemIcon);
            SimpleMouseHoverListener inspectItemMouseHoverListener = new SimpleMouseHoverListener();
            panInspectItemIcon.addMouseListener(inspectItemMouseHoverListener);
            guiNodeInspect.attachChild(panInspectItemIcon);
            pansInspectItemIcon[i] = panInspectItemIcon;
            inspectItemMouseHoverListeners[i] = inspectItemMouseHoverListener;
        }

        lblInspectDescription = new Label("");
        lblInspectDescription.setLocalTranslation(containerX + padding, containerY - padding - iconUnitSize - padding - iconItemSize - padding, 1);
        lblInspectDescription.setPreferredSize(new Vector3f(containerWidth - padding - padding, containerHeight - padding - iconUnitSize - padding - iconItemSize - padding - padding - buttonHeight - padding, 0));
        lblInspectDescription.setFontSize(14);
        lblInspectDescription.setColor(ColorRGBA.White);
        guiNodeInspect.attachChild(lblInspectDescription);

        Button btnInspectClose = new Button("Close");
        btnInspectClose.setLocalTranslation(containerX + padding, containerY - containerHeight + padding + buttonHeight, 1);
        btnInspectClose.setPreferredSize(new Vector3f(containerWidth - padding - padding, buttonHeight, 0));
        btnInspectClose.setTextHAlignment(HAlignment.Center);
        btnInspectClose.setTextVAlignment(VAlignment.Center);
        btnInspectClose.setFontSize(14);
        SimpleMouseClickListener closeMouseClickListener = new SimpleMouseClickListener();
        closeMouseClickListener.setOnClick(() -> inspectedUnitId = null);
        btnInspectClose.addMouseListener(closeMouseClickListener);
        guiNodeInspect.attachChild(btnInspectClose);
    }

    private void createDecision() {
        guiNodeDecision = new Node();
        guiNode.attachChild(guiNodeDecision);

        float optionWidth = 300;
        float optionHeight = 200;
        float optionMargin = 50;
        float optionX = ((totalWidth / 2f) - ((optionWidth + optionMargin + optionWidth + optionMargin + optionWidth) / 2f));
        float optionY = ((totalHeight / 2f) + (optionHeight / 2f)) + 60;

        float titleWidth = 400;
        float titleHeight = 50;
        float titleX = ((totalWidth / 2f) - (titleWidth / 2f));
        float titleY = optionY + 60;
        Label lblTitle = new Label("Choose one:");
        lblTitle.setPreferredSize(new Vector3f(titleWidth, titleHeight, 0));
        lblTitle.setLocalTranslation(titleX, titleY, 0);
        lblTitle.setTextHAlignment(HAlignment.Center);
        lblTitle.setFontSize(25);
        lblTitle.setColor(ColorRGBA.White);
        guiNodeDecision.attachChild(lblTitle);

        containersDecisionOption = new Container[ActualPlayer.DECISION_OPTIONS_COUNT];
        lblsDecisionOption = new Label[ActualPlayer.DECISION_OPTIONS_COUNT];
        for (int i = 0; i < ActualPlayer.DECISION_OPTIONS_COUNT; i++) {
            int _i = i;

            Container containerDecisionOption = new Container();
            containerDecisionOption.setLocalTranslation(optionX, optionY, 0);
            containerDecisionOption.setPreferredSize(new Vector3f(optionWidth, optionHeight, 0));
            SimpleMouseClickListener mouseClickListener = new SimpleMouseClickListener();
            mouseClickListener.setOnClick(() -> getAppState(ClientNetworkAppState.class).send(new DecideMessage(_i)));
            containerDecisionOption.addMouseListener(mouseClickListener);
            containersDecisionOption[i] = containerDecisionOption;

            Label lblDecisionOption = new Label("");
            lblDecisionOption.setTextHAlignment(HAlignment.Center);
            lblDecisionOption.setTextVAlignment(VAlignment.Center);
            lblDecisionOption.setFontSize(16);
            lblDecisionOption.setColor(ColorRGBA.White);
            containerDecisionOption.addChild(lblDecisionOption);
            lblsDecisionOption[i] = lblDecisionOption;

            optionX += optionWidth + optionMargin;
        }
    }

    private void createAugments() {
        float containerWidth = 170;
        float containerHeight = 20;

        guiNodeAugments = new Node();
        guiNode.attachChild(guiNodeAugments);

        containersAugment = new Container[2][DISPLAYED_AUGMENTS];
        lblsAugment = new Label[2][DISPLAYED_AUGMENTS];
        augmentHouseHoverListeners = new SimpleMouseHoverListener[2][DISPLAYED_AUGMENTS];
        for (int playerIndex = 0; playerIndex < 2; playerIndex++) {
            float containerX;
            float containerY;
            if (playerIndex == 0) {
                containerX = (((353f / 1600) * totalWidth) - (containerWidth / 2));
                containerY = ((1 - (200f / 900)) * totalHeight);
            } else {
                containerX = (((1330f / 1600) * totalWidth) - (containerWidth / 2));
                containerY = ((348f / 900) * totalHeight);
            }
            for (int i = 0; i < DISPLAYED_AUGMENTS; i++) {
                Container containerAugment = new Container();
                containerAugment.setLocalTranslation(containerX, containerY, 0);
                containerAugment.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));
                SimpleMouseHoverListener mouseHoverListener = new SimpleMouseHoverListener();
                containerAugment.addMouseListener(mouseHoverListener);
                containersAugment[playerIndex][i] = containerAugment;
                augmentHouseHoverListeners[playerIndex][i] = mouseHoverListener;

                Label lblAugment = new Label("");
                lblAugment.setTextHAlignment(HAlignment.Center);
                lblAugment.setTextVAlignment(VAlignment.Center);
                lblAugment.setFontSize(14);
                lblAugment.setColor(ColorRGBA.White);
                containerAugment.addChild(lblAugment);
                lblsAugment[playerIndex][i] = lblAugment;

                containerY += containerHeight;
            }
        }
    }

    private void createTooltip() {
        containerTooltip = new Container();
        lblTooltip = new Label("");
        lblTooltip.setMaxWidth(300);
        lblTooltip.setFontSize(14);
        lblTooltip.setColor(ColorRGBA.White);
        containerTooltip.addChild(lblTooltip);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        GameAppState gameAppState = getAppState(GameAppState.class);
        Game game = gameAppState.getGame();
        HumanPlayer ownPlayer = gameAppState.getOwnPlayer();
        ActualPlayer watchedPlayer = ownPlayer.getWatchedPlayer();
        Board watchedBoard = ownPlayer.getWatchedBoard();
        Unit inspectedUnit = getInspectedUnit(game);

        boolean isPlayerSpecificGuiVisible = (!game.isWalkOnlyPhase() && (watchedBoard != null));
        boolean isOwnPlayerSpecificGuiVisible = (isPlayerSpecificGuiVisible && (watchedPlayer == ownPlayer) && ownPlayer.isAlive());

        // Rounds

        Float boardDuration = null;
        Float boardTime = null;
        if (watchedBoard != null) {
            boardDuration = ((watchedBoard instanceof TimeBoard timeBoard) ? timeBoard.getDuration() : TimeBoard.DEFAULT_DURATION);
            boardTime = watchedBoard.getTime();
        }
        String[] roundIconPaths = new String[DISPLAYED_ROUNDS];
        int nextDisplayedPhase = game.getPhase();
        for (int i = 0; i < DISPLAYED_ROUNDS; i++) {
            while (PhaseMath.getPhaseType(nextDisplayedPhase) == PhaseType.PLANNING) {
                nextDisplayedPhase++;
            }
            roundIconPaths[i] = "textures/rounds/" + PhaseMath.getPhaseType(nextDisplayedPhase).name().toLowerCase() + ".png";
            nextDisplayedPhase++;
        }
        updateRounds(game.getPhase(), boardDuration, boardTime, roundIconPaths);

        // Players

        List<GuiVerticalListItem> players;
        boolean displayPlayers = (inspectedUnit == null);
        if (displayPlayers) {
            players = game.getActualPlayers().stream()
                    .sorted((p1, p2) -> (int) (p2.getCurrentHealth() - p1.getCurrentHealth()))
                    .map(player -> {
                        ColorRGBA customBackgroundColor = ((player == ownPlayer) ? TIER_COLORS.get(Tier.GOLD) : null);
                        return new GuiVerticalListItem(customBackgroundColor, "textures/characters/garmon.png", player.getName() + " (" + ((int) Math.ceil(player.getCurrentHealth())) + ")", null, () -> {
                            getAppState(ClientNetworkAppState.class).send(new WatchPlayerMessage(player.getId()));
                        });
                    })
                    .toList();
        } else {
            players = new LinkedList<>();
        }
        updatePlayers(players);

        // Traits

        boolean displayTraits = isPlayerSpecificGuiVisible;
        List<GuiVerticalListItem> traits;
        Integer moreTraitsCount = null;
        if (displayTraits) {
            ArrayList<Trait> allTraits = watchedPlayer.getAllTraits();
            HashMap<Trait, Integer> traitTierValues = new HashMap<>();
            HashMap<Trait, Integer> traitCounts = new HashMap<>();
            for (Trait trait : allTraits) {
                Tier tier = trait.getTier();
                traitTierValues.put(trait, ((tier != null) ? tier.ordinal() : -1));
                traitCounts.put(trait, trait.getUniqueUnitsOfTraitOnBoard());
            }
            traits = allTraits.stream()
                    .filter(trait -> traitCounts.get(trait) > 0)
                    .sorted((t1, t2) -> {
                        int tierComparison = traitTierValues.get(t2) - traitTierValues.get(t1);
                        if (tierComparison != 0) {
                            return tierComparison;
                        }
                        int countComparison = traitCounts.get(t2) - traitCounts.get(t1);
                        if (countComparison != 0) {
                            return countComparison;
                        }
                        return t1.getName().compareTo(t2.getName());
                    })
                    .map(trait -> new GuiVerticalListItem(TIER_COLORS.get(trait.getTier()), "textures/characters/dosaz.png", trait.getName() + " (" + traitCounts.get(trait) + ")", trait.getDescription(), null))
                    .toList();
            if (traits.size() > DISPLAYED_TRAITS) {
                traits = traits.subList(0, DISPLAYED_TRAITS);
                moreTraitsCount = DISPLAYED_TRAITS - traits.size();
            }
        } else {
            traits = new LinkedList<>();
        }
        updateTraits(traits, moreTraitsCount);

        // Shop

        boolean displayShop = (isOwnPlayerSpecificGuiVisible && (game.getPhase() > 2));
        if (displayShop) {
            GuiShopSlot[] shopSlots = new GuiShopSlot[ownPlayer.getShopUnits().length];
            for (int i = 0; i < shopSlots.length; i++) {
                GuiShopSlot shopSlot = null;
                Unit unit = ownPlayer.getShopUnits()[i];
                if (unit != null) {
                    String[] unitTraits = new String[unit.getBaseTraits().length];
                    for (int r = 0; r < unitTraits.length; r++) {
                        unitTraits[r] = Traits.getReferenceTrait(unit.getBaseTraits()[r]).getName();
                    }
                    shopSlot = new GuiShopSlot("textures/units/" + unit.getVisualName() + ".png", unit.getName(), unitTraits, unit.getCost());
                }
                shopSlots[i] = shopSlot;
            }
            updateShop(
                ownPlayer.getLevel(),
                ownPlayer.getExperience(),
                ownPlayer.getRequiredExperienceForNextLevel(),
                ownPlayer.getShopProbabilities(),
                ownPlayer.getBuyExperienceCost(),
                ownPlayer.getRerollCost(),
                ownPlayer.getGold(),
                "+?",
                false,
                shopSlots
            );
        }
        JMonkeyUtil.setAttached(guiNode, guiNodeShop, displayShop);

        // Items

        boolean displayItems = isPlayerSpecificGuiVisible;
        if (displayItems) {
            List<GuiItem> items = watchedPlayer.getItems().stream()
                    .map(item -> new GuiItem("textures/items/" + item.getVisualName() + ".png", getItemTooltip(item)))
                    .toList();
            updateItems(items);
        }
        JMonkeyUtil.setAttached(guiNode, guiNodeItems, displayItems);

        // Augments

        boolean displayAugments = isPlayerSpecificGuiVisible;
        if (displayAugments) {
            updateAugments(watchedBoard);
        }
        JMonkeyUtil.setAttached(guiNode, guiNodeAugments, displayAugments);

        // Decision

        boolean displayDecision = (isOwnPlayerSpecificGuiVisible && (ownPlayer.getNextDecision() != null));
        if (displayDecision) {
            updateDecision(ownPlayer.getNextDecision());
        }
        JMonkeyUtil.setAttached(guiNode, guiNodeDecision, displayDecision);

        // Inspect

        boolean displayInspect = (inspectedUnit != null);
        if (displayInspect) {
            updateInspect(inspectedUnit);
        }
        JMonkeyUtil.setAttached(guiNode, guiNodeInspect, displayInspect);
    }

    private Unit getInspectedUnit(Game game) {
        if (inspectedUnitId != null) {
            GameObject inspectedObject = game.getObjectById(inspectedUnitId);
            if (inspectedObject instanceof Unit) {
                return (Unit) inspectedObject;
            }
            inspectedUnitId = null;
        }
        return null;
    }

    public void updateRounds(int phase, Float boardDuration, Float boardTime, String[] roundIconPaths) {
        lblRound.setText(PhaseMath.getStage(phase) + "-"  + PhaseMath.getRound(phase));
        for (int i = 0; i < DISPLAYED_ROUNDS; i++) {
            IconComponent roundIcon = (IconComponent) pansRoundIcon[i].getBackground();
            roundIcon.setImageTexture(loadTexture(roundIconPaths[i]));
        }
        boolean displayTimeBar = (boardDuration != null);
        if (displayTimeBar) {
            boardTimeBar.getModel().setMaximum(boardDuration);
            boardTimeBar.setProgressValue(boardTime);
        }
        JMonkeyUtil.setAttached(guiNodeRounds, boardTimeBar, displayTimeBar);
    }

    public void updateItems(List<GuiItem> items) {
        for (int i = 0; i < ActualPlayer.MAXIMUM_ITEM_COUNT; i++) {
            int _i = i;
            boolean isVisible = i < items.size();
            GuiItem item = (isVisible ? items.get(i) : null);
            if (isVisible) {
                IconComponent roundIcon = (IconComponent) pansItemIcon[i].getBackground();
                roundIcon.setImageTexture(loadTexture(item.getIconPath()));
                Vector3f itemIconLocation = itemIconLocations[i];
                if ((draggedItemIndex != null) && (i == draggedItemIndex)) {
                    Vector2f cursorPosition = mainApplication.getInputManager().getCursorPosition();
                    // The z position of the dragged items needs to be smaller than the other ones, so that the second hovered item receives the mouse events
                    itemIconLocation = new Vector3f(cursorPosition.getX() - (itemSize / 2f), cursorPosition.getY() + (itemSize / 2f), itemIconLocations[i].getZ() - 1);
                }
                pansItemIcon[i].setLocalTranslation(itemIconLocation);
            }
            JMonkeyUtil.setAttached(guiNodeItems, pansItemIcon[i], isVisible);
            itemMouseHoverListeners[i].setOnEnter(isVisible ? () -> {
                // Otherwise, the dragged items tooltip pops up again at times
                if ((draggedItemIndex == null) || (_i != draggedItemIndex)) {
                    showTooltip(pansItemIcon[_i], item.getTooltip(), true);
                }
            } : null);
            itemMouseHoverListeners[i].setOnExit(isVisible ? this::hideTooltip : null);
        }
    }

    public void updatePlayers(List<GuiVerticalListItem> players) {
        updateVerticalList(verticalListPlayers, players, null, false);
    }

    public void updateTraits(List<GuiVerticalListItem> traits, Integer moreCount) {
        updateVerticalList(verticalListTraits, traits, moreCount, true);
    }

    private void updateVerticalList(
        VerticalList verticalList,
        List<GuiVerticalListItem> items,
        Integer moreCount,
        boolean tooltipsRightOrLeft
    ) {
        for (int i = 0; i < verticalList.pansIcon.length; i++) {
            final int _i = i;
            boolean isVisible = i < items.size();
            if (isVisible) {
                GuiVerticalListItem item = items.get(i);
                GuiUtils.setPanelBackgroundColor(verticalList.containers[i], ((item.getCustomBackgroundColor() != null) ? item.getCustomBackgroundColor() : VERTICAL_LIST_ITEM_DEFAULT_BACKGROUND_COLOR));
                IconComponent iconComponent = (IconComponent) verticalList.pansIcon[i].getBackground();
                iconComponent.setImageTexture(loadTexture(item.getIconPath()));
                verticalList.lblsText[i].setText(item.getText());
                verticalList.mouseClickListeners[i].setOnClick(item.getOnClick());
                verticalList.mouseHoverListeners[i].setOnEnter(item.getTooltip() != null ? () -> showTooltip(verticalList.containers[_i], item.getTooltip(), tooltipsRightOrLeft) : null);
                verticalList.mouseHoverListeners[i].setOnExit(item.getTooltip() != null ? this::hideTooltip : null);
            }
            JMonkeyUtil.setAttached(guiNode, verticalList.containers[i], isVisible);
            JMonkeyUtil.setAttached(guiNode, verticalList.pansIcon[i], isVisible);
            JMonkeyUtil.setAttached(guiNode, verticalList.lblsText[i], isVisible);
        }
        if (verticalList.lblMore != null) {
            boolean isMoreVisible = moreCount != null;
            if (isMoreVisible) {
                verticalList.lblMore.setText("+" + moreCount);
            }
            JMonkeyUtil.setAttached(guiNode, verticalList.containerMore, isMoreVisible);
            JMonkeyUtil.setAttached(guiNode, verticalList.lblMore, isMoreVisible);
        }
    }

    public void updateShop(
        int level,
        int experience,
        Integer nextLevelExperience,
        int[] shopProbabilities,
        int buyExperienceCost,
        int rerollCost,
        int gold,
        String streak,
        boolean isLocked,
        GuiShopSlot[] slots
    ) {
        lblLevel.setText("Lvl. " + level);
        boolean isLevelProgressVisible = (nextLevelExperience != null);
        if (isLevelProgressVisible) {
            lblLevelProgress.setText(experience + "/" + nextLevelExperience);
        }
        JMonkeyUtil.setAttached(guiNodeShop, lblLevelProgress, isLevelProgressVisible);
        for (int i = 0; i < DISPLAYED_PROBABILITIES; i++) {
            lblsShopProbability[i].setText(shopProbabilities[i] + "%");
        }

        btnBuyExperience.setText("Buy XP (" + buyExperienceCost + "g)");
        btnReroll.setText("Reroll (" + rerollCost + "g)");
        lblGold.setText(gold + "g");
        lblStreak.setText(streak);
        // TODO: Style panLockIcon depending on isLocked
        for (int i = 0; i < DISPLAYED_SHOP_SLOTS; i++) {
            GuiShopSlot slot = slots[i];
            boolean isSlotVisible = slot != null;
            if (isSlotVisible) {
                IconComponent slotIcon = (IconComponent) pansShopSlotIcon[i].getBackground();
                slotIcon.setImageTexture(loadTexture(slot.getIconPath()));
                IconComponent slotIconWrapper = (IconComponent) pansShopSlotIconWrapper[i].getBackground();
                slotIconWrapper.setImageTexture(loadTexture("textures/shop/" + slot.getCost() + ".png"));
                lblsSlotName[i].setText(slot.getName() + " (" + slot.getCost() + "g)");
            }
            JMonkeyUtil.setAttached(guiNodeShop, pansShopSlotIcon[i], isSlotVisible);
            JMonkeyUtil.setAttached(guiNodeShop, pansShopSlotIconWrapper[i], isSlotVisible);
            JMonkeyUtil.setAttached(guiNodeShop, lblsSlotName[i], isSlotVisible);

            int traitOffset = ((slot != null) ? (DISPLAYED_SHOP_SLOT_TRAITS - slot.getTraits().length) : DISPLAYED_SHOP_SLOT_TRAITS);
            for (int r = 0; r < DISPLAYED_SHOP_SLOT_TRAITS; r++) {
                boolean isTraitVisible = (r >= traitOffset);
                if (isTraitVisible) {
                    lblsSlotTrait[i][r].setText(slot.getTraits()[r - traitOffset]);
                }
                JMonkeyUtil.setAttached(guiNodeShop, lblsSlotTrait[i][r], isTraitVisible);
            }
        }
    }

    private void updateAugments(Board watchedBoard) {
        for (int playerIndex = 0; playerIndex < 2; playerIndex++) {
            int _playerIndex = playerIndex;
            Player player = ((playerIndex < watchedBoard.getOwners().size()) ? watchedBoard.getOwners().get(playerIndex) : null);
            for (int i = 0; i < DISPLAYED_AUGMENTS; i++) {
                int _i = i;
                boolean isVisible = ((player instanceof ActualPlayer actualPlayer) && (i < actualPlayer.getAugments().size()));
                if (isVisible) {
                    ActualPlayer actualPlayer = (ActualPlayer) player;
                    Augment augment = actualPlayer.getAugments().get(i);
                    lblsAugment[playerIndex][i].setText(augment.getName());
                    augmentHouseHoverListeners[playerIndex][i].setOnEnter(() -> showTooltip(containersAugment[_playerIndex][_i], augment.getDescription(), (_playerIndex == 0)));
                    augmentHouseHoverListeners[playerIndex][i].setOnExit(this::hideTooltip);
                }
                JMonkeyUtil.setAttached(guiNodeAugments, containersAugment[playerIndex][i], isVisible);
            }
        }
    }

    private void updateDecision(Decision decision) {
        for (int i = 0; i < lblsDecisionOption.length; i++) {
            boolean isVisible = (i < decision.getOptions().size());
            if (isVisible) {
                DecisionOption decisionOption = decision.getOptions().get(i);
                lblsDecisionOption[i].setText(decisionOption.getText());
                GuiUtils.setPanelBackgroundColor(containersDecisionOption[i], TIER_COLORS.get(decisionOption.getTier()));
            }
            JMonkeyUtil.setAttached(guiNodeDecision, containersDecisionOption[i], isVisible);
        }
    }

    private void updateInspect(Unit unit) {
        IconComponent inspectUnitIcon = (IconComponent) panInspectUnitIcon.getBackground();
        inspectUnitIcon.setImageTexture(loadTexture("textures/units/" + unit.getVisualName() + ".png"));

        lblInspectName.setText(unit.getName() + ((unit.getStars() > 1) ? " (" + unit.getStars() + "*)" : ""));

        for (int i = 0; i < pansInspectItemIcon.length; i++) {
            Panel panInspectItemIcon = pansInspectItemIcon[i];
            Item item = ((i < unit.getItems().size()) ? unit.getItems().get(i) : null);
            IconComponent inspectItemIcon = (IconComponent) panInspectItemIcon.getBackground();
            inspectItemIcon.setImageTexture(loadTexture("textures/items/" + ((item != null) ? item.getVisualName() : "none") + ".png"));
            inspectItemMouseHoverListeners[i].setOnEnter((item != null) ? () -> showTooltip(panInspectItemIcon, getItemTooltip(item), false) : null);
            inspectItemMouseHoverListeners[i].setOnExit((item != null) ? this::hideTooltip : null);
        }

        String description = "Traits: ";
        for (int i = 0; i < unit.getBaseTraits().length; i++) {
            if (i > 0) {
                description += ", ";
            }
            description += Traits.getReferenceTrait(unit.getBaseTraits()[i]).getName();
        }
        description += "\n\n";
        Spell spell = unit.getSpell();
        if (spell != null) {
            description += "Spell: " + spell.getName() + " - " + spell.getDescription() + "\n\n";
        }
        if (unit.getCurrentHealth() != null) {
            description += "Health: " + formatIntegerStat(unit.getCurrentHealth()) + "/" + formatIntegerStat(unit.getMaximumHealth()) + "\n";
        }
        if (unit.getCurrentMana() != null) {
            description += "Mana: " + formatIntegerStat(unit.getCurrentMana()) + "/" + formatIntegerStat(unit.getMaximumMana()) + "\n";
        }
        if (unit.getCurrentHealth() != null) {
            description += "Health Regeneration: " + formatFloatStat(unit.getHealthRegeneration()) + "\n";
        }
        if (unit.getCurrentMana() != null) {
            description += "Mana Regeneration: " + formatFloatStat(unit.getManaRegeneration()) + "\n";
        }
        description += "Attack Damage: " + formatIntegerStat(unit.getAttackDamage()) + "\n";
        description += "Attack Speed: " + formatPercentageStat(unit.getAttackSpeed()) + "\n";
        description += "Attack Range: " + formatIntegerStat(unit.getAttackRange()) + "\n";
        description += "Ability Power: " + formatIntegerStat(unit.getAbilityPower()) + "\n";
        description += "Armor: " + formatIntegerStat(unit.getArmor()) + "\n";
        description += "Magic Resistance: " + formatIntegerStat(unit.getMagicResistance()) + "\n";
        description += "Dodge Chance: " + formatPercentageStat(unit.getDodgeChance()) + "\n";
        description += "Crit Chance: " + formatPercentageStat(unit.getCritChance()) + "\n";
        description += "Crit Damage: " + formatPercentageStat(unit.getCritDamage()) + "\n";
        description += "Omnivamp: " + formatPercentageStat(unit.getOmnivamp()) + "\n";
        description += "Damage Amplification: " + formatPercentageStat(unit.getDamageDealtAmplification() - 1) + "\n";
        description += "Damage Reduction: " + formatPercentageStat(1 - unit.getDamageTakenAmplification()) + "\n";
        description += "Movement Speed: " + formatIntegerStat(unit.getMovementSpeed());
        lblInspectDescription.setText(description);
    }

    private String formatIntegerStat(float value) {
        return "" + Math.round(value);
    }

    private String formatFloatStat(float value) {
        return "" + (Math.round(value * 10) / 10f);
    }

    private String formatPercentageStat(float value) {
        return Math.round(value * 100) + "%";
    }

    private String getItemTooltip(Item item) {
        return item.getName() + "\n" + item.getDescription();
    }

    private void showTooltip(Panel element, String text, boolean rightOrLeft) {
        lblTooltip.setText(text);
        containerTooltip.setLocalTranslation(element.getLocalTranslation().add(
            (rightOrLeft ? element.getSize().getX() : -1 * containerTooltip.getPreferredSize().getX()),
            (element.getSize().getY() / -2) + (containerTooltip.getPreferredSize().getY() / 2),
            0
        ));
        guiNode.attachChild(containerTooltip);
    }

    private void hideTooltip() {
        guiNode.detachChild(containerTooltip);
    }

    public void onGameOver() {
        lblGameOver.setText("Game over");
        guiNode.attachChild(containerGameOver);
    }

    private Texture loadTexture(String path) {
        return GuiGlobals.getInstance().loadTexture(path, false, false);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
    }
}
