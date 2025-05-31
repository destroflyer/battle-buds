package com.destroflyer.battlebuds.client.appstates;

import com.destroflyer.battlebuds.client.gui.GuiUtils;
import com.destroflyer.battlebuds.shared.GameMode;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import com.destroflyer.battlebuds.shared.lobby.LobbyPlayerStatus;
import com.destroflyer.battlebuds.shared.Characters;
import com.destroflyer.battlebuds.shared.network.messages.QueueMessage;
import com.destroflyer.battlebuds.shared.network.messages.SelectCharacterMessage;
import com.destroflyer.battlebuds.shared.network.messages.SelectGameModeMessage;
import com.destroflyer.battlebuds.shared.network.messages.UnqueueMessage;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MainMenuAppState extends MenuAppState {

    private static final int BUTTON_HEIGHT = 40;

    private int containerWidth;
    private Container buttonContainerPlayers;
    private Container buttonContainerGames;
    private HashMap<Integer, Button> buttonsPlayers = new HashMap<>();
    private HashMap<Integer, Button> buttonsGames = new HashMap<>();
    private LinkedList<Object> tmpButtonKeysToRemove = new LinkedList<>();
    private Button btnQueue;

    public MainMenuAppState() {
        autoEnabled = true;
    }

    @Override
    public void recreateMenu() {
        super.recreateMenu();

        addTitle("Battle Buds");

        int containerMarginOutside = 200;
        int containerMarginBetween = 100;
        int containerY = (totalHeight - containerMarginOutside);
        containerWidth = ((totalWidth - (2 * containerMarginOutside) - containerMarginBetween) / 2);
        int containerHeight = (containerY - containerMarginOutside);
        int containerX1 = containerMarginOutside;
        int containerX2 = (containerMarginOutside + containerWidth + containerMarginBetween);

        buttonsPlayers.clear();
        buttonsGames.clear();
        addSectionContainer("Players", containerX1, containerY, containerWidth, containerHeight);
        addSectionContainer("Games", containerX2, containerY, containerWidth, containerHeight);

        btnQueue = createButton();
        btnQueue.setText("");
        btnQueue.setLocalTranslation(new Vector3f(containerX1 + 1, containerY - containerHeight + 1 + BUTTON_HEIGHT, 1));
        btnQueue.addCommands(Button.ButtonAction.Up, _ -> {
            ClientNetworkAppState clientNetworkAppState = getAppState(ClientNetworkAppState.class);
            if (getAppState(ClientLobbyAppState.class).isQueueing()) {
                clientNetworkAppState.send(new UnqueueMessage());
            } else {
                clientNetworkAppState.send(new QueueMessage());
            }
        });
        guiNode.attachChild(btnQueue);

        int containerButtonY = (containerY - 40);
        buttonContainerPlayers = addSectionButtonContainer(containerX1, containerButtonY);
        buttonContainerGames = addSectionButtonContainer(containerX2, containerButtonY);

        int characterAndMapContainerY = (containerMarginOutside - 25);
        addCharacterContainer(containerMarginOutside, characterAndMapContainerY);
        addGameModeContainer(characterAndMapContainerY, (totalWidth - containerMarginOutside));

        int settingsMargin = 20;
        int settingsIconSize = 30;
        int fpsCounterHeight = 21;
        Button btnSettings = new Button("");
        btnSettings.setLocalTranslation(settingsMargin, fpsCounterHeight + settingsMargin + settingsIconSize, 0);
        btnSettings.setPreferredSize(new Vector3f(settingsIconSize, settingsIconSize, 0));
        IconComponent iconSettings = new IconComponent("textures/settings.png");
        iconSettings.setIconSize(new Vector2f(settingsIconSize, settingsIconSize));
        btnSettings.setBackground(iconSettings);
        btnSettings.addCommands(Button.ButtonAction.Up, _ -> openMenu(SettingsMenuAppState.class));
        guiNode.attachChild(btnSettings);
    }

    private void addSectionContainer(String title, int containerX, int containerY, int containerWidth, int containerHeight) {
        int y = containerY;
        Container container = new Container();
        container.setLocalTranslation(containerX, y, 0);
        container.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));

        y -= 10;
        Label label = new Label(title);
        label.setLocalTranslation(new Vector3f(containerX, y, 1));
        label.setPreferredSize(new Vector3f(containerWidth, 20, 0));
        label.setInsets(new Insets3f(0, 10, 0, 0));
        label.setTextVAlignment(VAlignment.Center);
        label.setFontSize(20);
        label.setColor(ColorRGBA.White);
        guiNode.attachChild(label);

        guiNode.attachChild(container);
    }

    private Container addSectionButtonContainer(int containerX, int y) {
        Container buttonContainer = new Container();
        buttonContainer.setLocalTranslation(containerX + 1, y, 0);
        buttonContainer.setBackground(null);
        guiNode.attachChild(buttonContainer);
        return buttonContainer;
    }

    public void addCharacterContainer(int containerX, int containerY) {
        Container characterContainer = addSelectionContainer(
            "Character",
            HAlignment.Left,
            Characters.CHARACTER_NAMES,
            characterName -> characterName,
            characterName -> "textures/characters/" + characterName + ".png",
            false,
            () -> getAppState(ClientLobbyAppState.class).getOwnLobbyPlayer().getCharacterName(),
            characterName -> getAppState(ClientNetworkAppState.class).send(new SelectCharacterMessage(characterName))
        );
        characterContainer.setLocalTranslation(containerX, containerY, 0);
        guiNode.attachChild(characterContainer);
    }

    public void addGameModeContainer(int containerY, int containerWidth) {
        Container gameModeContainerWrapper = new Container();
        gameModeContainerWrapper.setLayout(new SpringGridLayout(Axis.X, Axis.Y, FillMode.First, FillMode.None));
        gameModeContainerWrapper.setLocalTranslation(0, containerY, 0);
        gameModeContainerWrapper.setPreferredSize(new Vector3f(containerWidth, 0, 0));
        gameModeContainerWrapper.setBackground(null);

        Panel placeholder = new Panel();
        placeholder.setBackground(null);
        gameModeContainerWrapper.addChild(placeholder);

        Container gameModeContainer = addSelectionContainer(
            "Mode",
            HAlignment.Right,
            GameMode.values(),
            GameMode::getName,
            mode -> "textures/modes/" + mode.name().toLowerCase() + ".png",
            true,
            () -> getAppState(ClientLobbyAppState.class).getOwnLobbyPlayer().getGameMode(),
            gameMode -> getAppState(ClientNetworkAppState.class).send(new SelectGameModeMessage(gameMode))
        );
        gameModeContainerWrapper.addChild(gameModeContainer);

        guiNode.attachChild(gameModeContainerWrapper);
    }

    private <T> Container addSelectionContainer(String title, HAlignment titleHAlignment, T[] values, Function<T, String> getName, Function<T, String> getIconPath, boolean nearestMagFilter, Supplier<T> getSelectedValue, Consumer<T> setSelectedValue) {
        Container container = new Container();
        container.setBackground(null);

        Label lblTitle = new Label("");
        lblTitle.setPreferredSize(new Vector3f(200, 40, 0));
        float insetLeft = 0;
        float insetRight = 0;
        if (titleHAlignment == HAlignment.Left) {
            insetLeft = 10;
        } else {
            insetRight = 10;
        }
        lblTitle.setInsets(new Insets3f(0, insetLeft, 0, insetRight));
        lblTitle.setTextHAlignment(titleHAlignment);
        lblTitle.setTextVAlignment(VAlignment.Center);
        lblTitle.setFontSize(20);
        lblTitle.setColor(ColorRGBA.White);
        container.addChild(lblTitle);

        Runnable updateVisualSelection = () -> {
            T selectedValue = getSelectedValue.get();
            lblTitle.setText(title + ": " + getName.apply(selectedValue));
        };

        int iconSize = 80;
        Container iconsRow = new Container();
        iconsRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        for (T value : values) {
            Button button = new Button("");
            IconComponent icon = new IconComponent(getIconPath.apply(value));
            icon.setIconSize(new Vector2f(iconSize, iconSize));
            icon.setHAlignment(HAlignment.Center);
            icon.setVAlignment(VAlignment.Center);
            if (nearestMagFilter) {
                icon.getImageTexture().setMagFilter(Texture.MagFilter.Nearest);
            }
            button.setBackground(icon);
            button.addCommands(Button.ButtonAction.Up, _ -> {
                setSelectedValue.accept(value);
                updateVisualSelection.run();
            });
            iconsRow.addChild(button);
        }
        container.addChild(iconsRow);

        updateVisualSelection.run();

        return container;
    }

    @Override
    public void onEnabled(boolean enabled) {
        super.onEnabled(enabled);
        if (enabled) {
            Camera camera = mainApplication.getCamera();
            camera.setLocation(new Vector3f(0, 7, 0));
            camera.setRotation(new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        updatePlayersContainer();
        updateGamesContainer();
        checkIfGameStarted();
    }

    private void updatePlayersContainer() {
        ClientLobbyAppState clientLobbyAppState = getAppState(ClientLobbyAppState.class);
        updateButtons(
            buttonContainerPlayers,
            buttonsPlayers,
            clientLobbyAppState.getLobby().getPlayers(),
            lobbyPlayer -> lobbyPlayer.getAccount().getId(),
            lobbyPlayer -> {
                String text = lobbyPlayer.getAccount().getLogin() + " (";
                switch (lobbyPlayer.getStatus()) {
                    case IDLE:
                        text += "Idle";
                        break;
                    case QUEUE:
                        text += "In Queue for " + lobbyPlayer.getGameMode().getName();
                        break;
                    case INGAME:
                        text += "Ingame";
                        break;
                }
                text += ")";
                return text;
            },
            lobbyPlayer -> lobbyPlayer.getStatus() == LobbyPlayerStatus.QUEUE
        );

        boolean isQueueing = clientLobbyAppState.isQueueing();
        btnQueue.setText(isQueueing ? "Unqueue" : "Queue");
        GuiUtils.markButtonAsActive(btnQueue, isQueueing);
    }

    private void updateGamesContainer() {
        updateButtons(
            buttonContainerGames,
            buttonsGames,
            getAppState(ClientLobbyAppState.class).getLobby().getGames(),
            lobbyGame -> lobbyGame.getId(),
            lobbyGame -> "Game #" + lobbyGame.getId(),
            _ -> false
        );
    }

    private <K, O> void updateButtons(
        Container buttonContainer,
        HashMap<K, Button> buttons,
        Collection<O> objects,
        Function<O, K> getKey,
        Function<O, String> getText,
        Function<O, Boolean> isActive
    ) {
        for (Map.Entry<K, Button> entry : buttons.entrySet()) {
            if (objects.stream().noneMatch(object -> getKey.apply(object).equals(entry.getKey()))) {
                tmpButtonKeysToRemove.add(entry.getKey());
            }
        }
        for (Object key : tmpButtonKeysToRemove) {
            Button button = buttons.remove(key);
            buttonContainer.removeChild(button);
        }
        tmpButtonKeysToRemove.clear();
        for (O object : objects) {
            K key = getKey.apply(object);
            Button button = buttons.get(key);
            if (button == null) {
                button = createButton();
                buttonContainer.addChild(button);
                buttons.put(key, button);
            }
            button.setText(getText.apply(object));
            GuiUtils.markButtonAsActive(button, isActive.apply(object));
        }
    }

    private Button createButton() {
        Button button = new Button("");
        button.setPreferredSize(new Vector3f(containerWidth - 2, BUTTON_HEIGHT, 0));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.setFontSize(20);
        button.setColor(ColorRGBA.White);
        return button;
    }

    private void checkIfGameStarted() {
        LobbyGame lobbyGame = getAppState(ClientLobbyAppState.class).getOwnLobbyGame();
        if (lobbyGame != null) {
            mainApplication.joinGame(lobbyGame);
        }
    }
}
