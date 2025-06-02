package com.destroflyer.battlebuds.client.appstates;

import com.destroflyer.battlebuds.client.JMonkeyUtil;
import com.destroflyer.battlebuds.client.animations.Animation;
import com.destroflyer.battlebuds.client.characters.GameObjectVisual;
import com.destroflyer.battlebuds.client.characters.ModelInfos;
import com.destroflyer.battlebuds.client.filters.GrayScaleFilter;
import com.destroflyer.battlebuds.client.models.ModelObject;
import com.destroflyer.battlebuds.shared.game.*;
import com.destroflyer.battlebuds.shared.game.boards.PlanningBoard;
import com.destroflyer.battlebuds.shared.game.objects.*;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;
import com.destroflyer.battlebuds.shared.game.objects.players.HumanPlayer;
import com.destroflyer.battlebuds.shared.network.messages.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GameWorldAppState extends BaseClientAppState implements ActionListener {

    private Node rootNode = new Node();
    private Node guiNode = new Node();
    private ModelObject[][] goldIndicators;
    private HashMap<Integer, GameObjectVisual> gameObjectVisuals = new HashMap<>();
    private LinkedList<Animation> playingAnimations = new LinkedList<>();
    private Vector2f hoveredPosition;
    @Getter
    private Integer hoveredObjectId;
    @Getter
    private Integer draggedVisualObjectId;
    private Integer draggedVisualObjectPhaseAtStart;
    private GrayScaleFilter grayscaleFilter = new GrayScaleFilter();

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        mainApplication.getRootNode().attachChild(rootNode);
        mainApplication.getGuiNode().attachChild(guiNode);
        initGoldIndicators();
        initInputListener();
    }

    private void initGoldIndicators() {
        goldIndicators = new ModelObject[2][5];
        for (int playerIndex = 0; playerIndex < goldIndicators.length; playerIndex++) {
            int direction = (playerIndex == 0) ? -1 : 1;
            for (int goldIndex = 0; goldIndex < goldIndicators[playerIndex].length; goldIndex++) {
                ModelObject goldIndicator = new ModelObject(mainApplication.getAssetManager(), "models/gold_bag/skin.xml");
                goldIndicator.setLocalTranslation(direction * 23.2f, 0, (direction * -11.7f) + (direction * goldIndex * 4.5f));
                goldIndicators[playerIndex][goldIndex] = goldIndicator;
            }
        }
    }

    private void initInputListener() {
        mainApplication.getInputManager().addMapping("key_buy_experience", new KeyTrigger(KeyInput.KEY_E));
        mainApplication.getInputManager().addMapping("key_buy_reroll", new KeyTrigger(KeyInput.KEY_D));
        mainApplication.getInputManager().addMapping("key_sell_unit", new KeyTrigger(KeyInput.KEY_F));
        mainApplication.getInputManager().addMapping("mouse_left", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        mainApplication.getInputManager().addMapping("mouse_right", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        mainApplication.getInputManager().addListener(this, "key_buy_experience", "key_buy_reroll", "key_sell_unit", "mouse_left", "mouse_right");
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        updateHover();
        updateDrag();
        updateWorld();
        updateAnimations(tpf);
    }

    private void updateHover() {
        TerrainQuad terrain = getAppState(ForestBoardAppState.class).getTerrain();

        hoveredPosition = null;
        hoveredObjectId = null;
        CollisionResults results = mainApplication.getRayCastingResults_Cursor(mainApplication.getRootNode());
        for (int i = 0; i < results.size(); i++) {
            CollisionResult collisionResult = results.getCollision(i);
            Spatial collisionSpatial = collisionResult.getGeometry();
            while (collisionSpatial.getParent() != null) {
                collisionSpatial = collisionSpatial.getParent();
                if (collisionSpatial == terrain) {
                    if (hoveredPosition == null) {
                        hoveredPosition = new Vector2f(collisionResult.getContactPoint().getX(), collisionResult.getContactPoint().getZ());
                    }
                } else if (hoveredObjectId == null) {
                    Integer gameObjectId = collisionSpatial.getUserData("gameObjectId");
                    if (gameObjectId != null) {
                        hoveredObjectId = gameObjectId;
                    }
                }
            }
        }
    }

    private void updateDrag() {
        Game game = getAppState(GameAppState.class).getGame();
        if ((draggedVisualObjectPhaseAtStart != null) && (draggedVisualObjectPhaseAtStart != game.getPhase())) {
            draggedVisualObjectId = null;
            draggedVisualObjectPhaseAtStart = null;
        }
    }

    private void updateWorld() {
        GameAppState gameAppState = getAppState(GameAppState.class);
        Game game = gameAppState.getGame();
        HumanPlayer ownPlayer = gameAppState.getOwnPlayer();
        Board watchedBoard = ownPlayer.getWatchedBoard();

        updateGameObjects(game, ownPlayer, watchedBoard);
        updateGoldIndicators(game, watchedBoard);
        updateFilters(ownPlayer, watchedBoard);
    }

    private void updateGameObjects(Game game, HumanPlayer ownPlayer, Board watchedBoard) {
        for (Map.Entry<Integer, GameObjectVisual> entry : gameObjectVisuals.entrySet().toArray(Map.Entry[]::new)) {
            Integer objectId = entry.getKey();
            GameObjectVisual visual = entry.getValue();
            GameObject gameObject = ((watchedBoard != null) ? watchedBoard.getObjectById(objectId) : null);
            if ((gameObject == null) || !isGameObjectRelevant(game, gameObject)) {
                rootNode.detachChild(visual.getModelObject());
                guiNode.detachChild(visual.getGuiNode());
                gameObjectVisuals.remove(objectId);
            }
        }
        if (watchedBoard != null) {
            for (GameObject gameObject : watchedBoard.getObjects()) {
                if (isGameObjectRelevant(game, gameObject)) {
                    VisualObject visualObject = (VisualObject) gameObject;
                    ActualPlayer actualPlayer = ((visualObject instanceof ActualPlayer ap) ? ap : null);
                    Unit unit = ((visualObject instanceof Unit u) ? u : null);
                    boolean displayName = ((actualPlayer != null) || ((unit != null) && (unit.getStars() > 1)));
                    boolean displayHealthBar = (unit != null) && unit.isActive() && (unit.getCurrentHealth() != null);
                    boolean displayManaBar = (unit != null) && unit.isActive() && (unit.getCurrentMana() != null);
                    boolean displayItems = (unit != null) && (unit.getItems().size() > 0);
                    GameObjectVisual visual = gameObjectVisuals.computeIfAbsent(visualObject.getId(), _ -> {
                        GameObjectVisual newVisual = new GameObjectVisual(mainApplication.getCamera(), mainApplication.getAssetManager(), ModelInfos.get(visualObject.getVisualName()), ColorRGBA.White);
                        newVisual.getModelObject().setUserData("gameObjectId", visualObject.getId());
                        rootNode.attachChild(newVisual.getModelObject());
                        guiNode.attachChild(newVisual.getGuiNode());
                        return newVisual;
                    });
                    boolean isDragged = ((draggedVisualObjectId != null) && (draggedVisualObjectId == visualObject.getId()));
                    visual.setPosition(isDragged ? hoveredPosition : visualObject.getPosition());
                    visual.setDirection(visualObject.getDirection());
                    visual.getModelObject().setLocalScale(1 + ((unit != null) ? ((unit.getStars() - 1) * 0.23f) : 0));
                    switch (visualObject.getActionState()) {
                        case IDLE:
                            visual.playIdleAnimation();
                            break;
                        case WALK:
                            visual.playWalkAnimation(visualObject.getMovementSpeed());
                            break;
                        case ATTACK:
                            visual.playAttackAnimation(1f / unit.getAttackSpeed());
                            break;
                        case CAST:
                            visual.playCastAnimation(unit.getSpell().getCastDuration());
                            break;
                        case DANCE:
                            visual.playDanceAnimation();
                            break;
                    }
                    if (displayName) {
                        if ((unit != null) && (unit.getStars() > 1)) {
                            visual.setName(unit.getStars() + "*" );
                        } else {
                            visual.setName(visualObject.getName());
                        }
                    }
                    visual.setNameVisible(displayName);
                    if (displayHealthBar) {
                        visual.setAllied(unit.getPlayer() == ownPlayer);
                        visual.setMaximumHealth(unit.getMaximumHealth());
                        visual.setCurrentHealth(unit.getCurrentHealth());
                    }
                    visual.setHealthVisible(displayHealthBar);
                    if (displayManaBar) {
                        visual.setMaximumMana(unit.getMaximumMana());
                        visual.setCurrentMana(unit.getCurrentMana());
                    }
                    visual.setManaVisible(displayManaBar);
                    String[] itemIconPaths = new String[0];
                    if (displayItems) {
                        itemIconPaths = new String[unit.getItems().size()];
                        for (int i = 0; i < itemIconPaths.length; i++) {
                            itemIconPaths[i] = "textures/items/" + unit.getItems().get(i).getVisualName() + ".png";
                        }
                    }
                    visual.setItems(itemIconPaths);
                }
            }
        }
    }

    private boolean isGameObjectRelevant(Game game, GameObject gameObject) {
        if (game.isWalkOnlyPhase() ? !(gameObject instanceof ActualPlayer) : !(gameObject instanceof VisualObject)) {
            return false;
        }
        VisualObject visualObject = (VisualObject) gameObject;
        return (visualObject.getVisualName() != null);
    }

    private void updateGoldIndicators(Game game, Board watchedBoard) {
        for (int playerIndex = 0; playerIndex < goldIndicators.length; playerIndex++) {
            Integer gold = null;
            if ((!game.isWalkOnlyPhase()) && (watchedBoard != null) && (playerIndex < watchedBoard.getOwners().size())) {
                Player owner = watchedBoard.getOwners().get(playerIndex);
                if (owner instanceof ActualPlayer ownerActualPlayer) {
                    gold = ownerActualPlayer.getGold();
                }
            }
            for (int goldIndex = 0; goldIndex < goldIndicators[playerIndex].length; goldIndex++) {
                boolean isVisible = ((gold != null) && (gold >= ((goldIndex + 1) * 10)));
                JMonkeyUtil.setAttached(rootNode, goldIndicators[playerIndex][goldIndex], isVisible);
            }
        }
    }

    private void updateFilters(HumanPlayer ownPlayer, Board watchedBoard) {
        boolean shouldDisplayGrayscale = ((!ownPlayer.isAlive()) && ((watchedBoard == null) || (watchedBoard == ownPlayer.getOwnBoard())));
        boolean isDisplayingGrayscale = mainApplication.getFilterPostProcessor().getFilterList().contains(grayscaleFilter);
        if (shouldDisplayGrayscale) {
            if (!isDisplayingGrayscale) {
                mainApplication.getFilterPostProcessor().addFilter(grayscaleFilter);
            }
        } else if (isDisplayingGrayscale) {
            mainApplication.getFilterPostProcessor().removeFilter(grayscaleFilter);
        }
    }

    private void updateAnimations(float tpf) {
        for (Animation animation : playingAnimations.toArray(Animation[]::new)) {
            animation.update(tpf);
            if (animation.isFinished()) {
                animation.end();
                playingAnimations.remove(animation);
            }
        }
    }

    private void playAnimation(Animation animation) {
        animation.start();
        playingAnimations.add(animation);
    }

    @Override
    public void onAction(String actionName, boolean isPressed, float tpf) {
        ClientNetworkAppState clientNetworkAppState = getAppState(ClientNetworkAppState.class);
        GameAppState gameAppState = getAppState(GameAppState.class);
        GameGuiAppState gameGuiAppState = getAppState(GameGuiAppState.class);
        switch (actionName) {
            case "mouse_right":
                if (isPressed) {
                    if (hoveredObjectId != null) {
                        gameGuiAppState.setInspectedUnitId(hoveredObjectId);
                    } else {
                        clientNetworkAppState.send(new WalkMessage(hoveredPosition));
                    }
                }
                break;
            case "mouse_left":
                if (isPressed) {
                    if ((hoveredObjectId != null) && isDraggable(hoveredObjectId)) {
                        draggedVisualObjectId = hoveredObjectId;
                        draggedVisualObjectPhaseAtStart = gameAppState.getGame().getPhase();
                    }
                } else  if (draggedVisualObjectId != null) {
                    PositionSlot positionSlot = BoardMath.getPositionSlot(hoveredPosition);
                    if (positionSlot != null) {
                        clientNetworkAppState.send(new MoveUnitMessage(draggedVisualObjectId, positionSlot));
                    }
                    draggedVisualObjectId = null;
                    draggedVisualObjectPhaseAtStart = null;
                }
                break;
            case "key_buy_experience":
                if (isPressed) {
                    clientNetworkAppState.send(new BuyExperienceMessage());
                }
                break;
            case "key_buy_reroll":
                if (isPressed) {
                    clientNetworkAppState.send(new BuyRerollMessage());
                }
                break;
            case "key_sell_unit":
                if (isPressed && (hoveredObjectId != null)) {
                    clientNetworkAppState.send(new SellUnitMessage(hoveredObjectId));
                }
                break;
        }
    }

    private boolean isDraggable(int objectId) {
        GameAppState gameAppState = getAppState(GameAppState.class);
        GameObject gameObject = gameAppState.getGame().getObjectById(objectId);
        if (gameObject instanceof Unit unit) {
            HumanPlayer ownPlayer = gameAppState.getOwnPlayer();
            if (unit.getPlayer() == ownPlayer) {
                // Player.planningBoard is not synced to client, but we can just check by class
                if (unit.getBoard() instanceof PlanningBoard) {
                    return true;
                }
                // TODO: Offer a method on the player to check if a unit is moveable in general
                PositionSlot positionSlot = unit.getPlayer().getUnitPositionSlot(unit);
                return (positionSlot != null) && (positionSlot.getType() == PositionSlot.Type.BENCH);
            }
        }
        return false;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(rootNode);
        mainApplication.getGuiNode().detachChild(guiNode);
        mainApplication.getInputManager().removeListener(this);
        mainApplication.getFilterPostProcessor().removeFilter(grayscaleFilter);
    }
}
