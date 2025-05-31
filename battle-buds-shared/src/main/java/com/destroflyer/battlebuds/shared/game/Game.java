package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.augments.Augments;
import com.destroflyer.battlebuds.shared.game.boards.*;
import com.destroflyer.battlebuds.shared.game.objects.*;
import com.destroflyer.battlebuds.shared.game.objects.Character;
import com.destroflyer.battlebuds.shared.game.objects.units.*;
import com.destroflyer.battlebuds.shared.game.spells.TestSpell;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import com.destroflyer.battlebuds.shared.lobby.LobbyPlayer;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.destroflyer.battlebuds.shared.game.PhaseType.CAROUSEL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Game implements GameSerializable {

    public Game(LobbyGame lobbyGame) {
        this.lobbyGame = lobbyGame;

        // Players

        for (LobbyPlayer lobbyPlayer : lobbyGame.getPlayers()) {
            Account account = lobbyPlayer.getAccount();
            actualPlayers.add(createActualPlayer(account.getId(), account.getLogin(), lobbyPlayer.getCharacterName()));
        }
        int totalPlayerCount = lobbyGame.getGameMode().getPlayerCount();
        if (totalPlayerCount == 1) {
            totalPlayerCount = 8;
        }
        int botsCount = (totalPlayerCount - lobbyGame.getPlayers().size());
        for (int i = 0; i < botsCount; i++) {
            actualPlayers.add(createActualPlayer(null, "Bot " + (i + 1), "garmon"));
        }
        for (int i = 0; i < actualPlayers.size(); i++) {
            Player actualPlayer = actualPlayers.get(i);

            float angle = ((((float) i) / actualPlayers.size()) * FastMath.TWO_PI);
            float radius = 12;
            float x = (float) (Math.sin(angle) * radius);
            float y = (float) (Math.cos(angle) * radius);
            actualPlayer.setPosition(new Vector2f(x, y));

            Player neutralPlayer = createNeutralPlayer();
            neutralPlayers.add(neutralPlayer);
            allPlayers.add(actualPlayer);
            allPlayers.add(neutralPlayer);
        }
        for (Player player : allPlayers) {
            register(player);
        }

        // Unit pools

        for (int i = 0; i < unitPoolsByCost.length; i++) {
            unitPoolsByCost[i] = new ArrayList<>();
        }
        for (Class<? extends Unit> unitClass : Units.BUYABLE_CLASSES) {
            Supplier<Unit> createUnit = () -> Util.createObjectByClass(unitClass);
            Unit firstUnit = createUnit.get();
            int costIndex = firstUnit.getCost() - 1;
            for (int i = 0; i < UNIT_POOL_SIZES[costIndex]; i++) {
                Unit unit = ((i == 0) ? firstUnit : createUnit.get());
                unit.reset();
                unitPoolsByCost[costIndex].add(unit);

                // TODO: Test content
                unit.setSpell(new TestSpell());
            }
        }

        // Start

        startNextPhase();
    }
    private static final int MAXIMUM_BUYABLE_UNIT_COST = 5;
    private static final int[] UNIT_POOL_SIZES = new int[] { 30, 25, 18, 10, 9 };
    public static final int SHOP_SLOTS = 5;
    @Getter
    private LobbyGame lobbyGame;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    @Getter
    private ArrayList<Player> actualPlayers = new ArrayList<>();
    private ArrayList<Player> neutralPlayers = new ArrayList<>();
    private ArrayList<Unit>[] unitPoolsByCost = new ArrayList[MAXIMUM_BUYABLE_UNIT_COST];
    @Getter
    private int phase = -1;
    @Getter
    private ArrayList<Board> boards = new ArrayList<>();
    private int nextObjectId = 1;
    private ArrayList<GameTask> tasks = new ArrayList<>();

    private Player createActualPlayer(Integer accountId, String name, String characterName) {
        Player player = createPlayer(accountId, name, characterName);
        player.setCurrentHealth(player.getMaximumHealth());
        return player;
    }

    private Player createNeutralPlayer() {
        return createPlayer(null, "Neutral", null);
    }

    private Player createPlayer(Integer accountId, String name, String visualName) {
        Player player = new Player(accountId);
        player.setName(name);
        player.setVisualName(visualName);

        PlanningBoard planningBoard = new PlanningBoard();
        planningBoard.setGame(this);
        ArrayList<Player> planningBoardOwners = new ArrayList<>();
        planningBoardOwners.add(player);
        planningBoard.setOwners(planningBoardOwners);
        player.setPlanningBoard(planningBoard);

        return player;
    }

    public void register(GameObject object) {
        if (!object.isRegistered()) {
            object.register(this, nextObjectId++);
        }
    }

    public void update(float tpf) {
        for (GameTask task : tasks.toArray(GameTask[]::new)) {
            if (task.update(tpf)) {
                tasks.remove(task);
            }
        }
        for (Board board : boards) {
            board.update(tpf);
        }
        if (boards.stream().allMatch(Board::isFinished)) {
            startNextPhase();
        }
    }

    private void startNextPhase() {
        HashMap<Player, ArrayList<PickUpObject>> leftoverOwnedPickUpObjects = new HashMap<>();
        for (Board board : boards) {
            board.onFinish();
            for (GameObject object : board.getObjects()) {
                if ((object instanceof PickUpObject pickUpObject) && (pickUpObject.getOwner() != null)) {
                    leftoverOwnedPickUpObjects.computeIfAbsent(pickUpObject.getOwner(), _ -> new ArrayList<>()).add(pickUpObject);
                }
            }
        }

        boards.clear();
        tasks.clear();

        List<Player> alivePlayers = actualPlayers.stream().filter(Character::isAlive).toList();

        phase++;
        PhaseType phaseType = getPhaseType();
        switch (phaseType) {
            case CAROUSEL -> {
                CarouselBoard carouselBoard = new CarouselBoard();
                carouselBoard.setGame(this);
                carouselBoard.setOwners(actualPlayers);
                boards.add(carouselBoard);
            }
            case PLANNING -> {
                for (Player player : alivePlayers) {
                    player.addExperience(2);
                    player.addGold(player.getGoldIncome());
                    reroll(player);

                    PlanningBoard planningBoard = player.getPlanningBoard();
                    planningBoard.reset();
                    boards.add(planningBoard);
                }
            }
            case COMBAT_NEUTRAL -> {
                for (int i = 0; i < actualPlayers.size(); i++) {
                    Player actualPlayer = actualPlayers.get(i);
                    Player neutralPlayer = neutralPlayers.get(i);
                    addCombatBoard(new NeutralCombatBoard(), actualPlayer, neutralPlayer);
                    setupNeutralBoard(neutralPlayer);
                }
            }
            case COMBAT_PLAYER -> {
                ArrayList<Player> remainingPlayers = new ArrayList<>(alivePlayers);
                Collections.shuffle(remainingPlayers);
                while (remainingPlayers.size() >= 2) {
                    Player player1 = remainingPlayers.removeFirst();
                    Player player2 = remainingPlayers.removeFirst();
                    addCombatBoard(new PlayerCombatBoard(), player1, player2);
                }
                // No ghosts supported yet
                if (remainingPlayers.size() > 0) {
                    addPlanningBoard(remainingPlayers.getFirst());
                }
            }
        }

        for (Board board : boards) {
            for (Player player : board.getOwners()) {
                player.watchPlayer(player);
                // Needs to be done before updating slot unit board states (so that they will be added to the board)
                player.resetUnitsRemovedFromBoard();
                // Needs to be done before resetting, as resetting involves calculations that require the board to be set (Example: A trait bonus for maximum health that looks at the units on the board)
                player.updateSlotUnitsBoardStates(true);
                player.resetUnits();
                if (leftoverOwnedPickUpObjects.containsKey(player)) {
                    for (PickUpObject pickUpObject : leftoverOwnedPickUpObjects.get(player)) {
                        board.addObject(pickUpObject);
                    }
                }

                switch (phaseType) {
                    case PhaseType.PLANNING:
                        player.onPlanningRoundStart();
                        break;
                    case PhaseType.COMBAT_NEUTRAL:
                    case PhaseType.COMBAT_PLAYER:
                        player.onCombatRoundStart();
                        break;
                }

                // Easiest way for now to get the first unit
                if (phase == 1) {
                    player.tryBuyUnit(0);
                    reroll(player);
                }
            }
        }

        if (PhaseMath.isAugmentOffered(phase)) {
            offerAugments();
        }
    }

    private void addPlanningBoard(Player player) {
        PlanningBoard planningBoard = player.getPlanningBoard();
        planningBoard.reset();
        boards.add(planningBoard);
    }

    private void addCombatBoard(CombatBoard combatBoard, Player player1, Player player2) {
        combatBoard.setGame(this);
        ArrayList<Player> boardOwners = new ArrayList<>();
        boardOwners.add(player1);
        boardOwners.add(player2);
        combatBoard.setOwners(boardOwners);
        boards.add(combatBoard);

        player1.fillBoardFromBenchIfSpaceLeft();
        player2.fillBoardFromBenchIfSpaceLeft();
    }

    public void setupNeutralBoard(Player neutralPlayer) {
        neutralPlayer.clearUnits();
        int stage = PhaseMath.getStage(phase);
        int round = PhaseMath.getRound(phase);
        switch (stage) {
            case 1:
                if (round > 3) {
                    neutralPlayer.addNewUnit(new RebellionArcher(), new PositionSlot(PositionSlot.Type.BOARD, 5, 2));
                }
                if (round > 2) {
                    neutralPlayer.addNewUnit(new RebellionArcher(), new PositionSlot(PositionSlot.Type.BOARD, 1, 2));
                }
                neutralPlayer.addNewUnit(new RebellionSoldier(), new PositionSlot(PositionSlot.Type.BOARD, 2, 1));
                neutralPlayer.addNewUnit(new RebellionSoldier(), new PositionSlot(PositionSlot.Type.BOARD, 4, 1));
                break;
            case 2:
                neutralPlayer.addNewUnit(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 0, 0));
                neutralPlayer.addNewUnit(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 1, 2));
                neutralPlayer.addNewUnit(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 5, 0));
                break;
            case 3:
                neutralPlayer.addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 1, 3));
                neutralPlayer.addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 2, 3));
                neutralPlayer.addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 3, 1));
                neutralPlayer.addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 4, 3));
                neutralPlayer.addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 5, 3));
                break;
            case 4:
                neutralPlayer.addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 1, 1));
                neutralPlayer.addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 2, 2));
                neutralPlayer.addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 3, 3));
                neutralPlayer.addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 4, 2));
                neutralPlayer.addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 5, 1));
                break;
            case 5:
            default:
                neutralPlayer.addNewUnit(new RebellionBaron(), new PositionSlot(PositionSlot.Type.BOARD, 3, 1));
                break;
        }
    }

    public void reroll(Player player) {
        // Put back old units
        for (Unit oldShopUnit : player.getShopUnits()) {
            if (oldShopUnit != null) {
                unitPoolsByCost[oldShopUnit.getCost() - 1].add(oldShopUnit);
            }
        }
        // Roll new units
        int[] playerShopProbabilities = player.getShopProbabilities();
        Unit[] newShopUnits = new Unit[SHOP_SLOTS];
        for (int i = 0; i < SHOP_SLOTS; i++) {
            Unit unit = null;

            // Calculate effective shop probabilities per roll
            int[] shopProbabilities = Arrays.copyOf(playerShopProbabilities, playerShopProbabilities.length);
            int remainingProbabilitiesSum = 0;
            for (int r = 0; r < unitPoolsByCost.length; r++) {
                if (unitPoolsByCost[r].isEmpty()) {
                    shopProbabilities[r] = 0;
                } else {
                    remainingProbabilitiesSum += shopProbabilities[r];
                }
            }

            Integer rolledCostIndex = rollShopUnitCostIndex(shopProbabilities, remainingProbabilitiesSum);
            if (rolledCostIndex != null) {
                ArrayList<Unit> rolledCostUnitPool = unitPoolsByCost[rolledCostIndex];
                int randomUnitIndex = (int) (Math.random() * rolledCostUnitPool.size());
                unit = rolledCostUnitPool.remove(randomUnitIndex);
            }
            newShopUnits[i] = unit;
        }
        player.setShopUnits(newShopUnits);
    }

    private Integer rollShopUnitCostIndex(int[] shopProbabilities, int remainingProbabilitiesSum) {
        if (remainingProbabilitiesSum == 0) {
            return null;
        }
        double roll = Math.random() * remainingProbabilitiesSum;
        int minimumRequiredRoll = 0;
        for (int i = 0; i < shopProbabilities.length; i++) {
            minimumRequiredRoll += shopProbabilities[i];
            if (roll < minimumRequiredRoll) {
                return i;
            }
        }
        return null;
    }

    private void offerAugments() {
        for (Player player : actualPlayers) {
            ArrayList<Augment> augments = Augments.createRandomAugments(Player.DECISION_OPTIONS_COUNT);
            Decision decision = new Decision(augments.stream()
                    .map(augment -> new DecisionOption(augment.getTier(), augment.getName() + "\n\n" + augment.getDescription(), () -> player.addAugment(augment)))
                    .toList());
            player.addDecision(decision);
        }
    }

    public void enqueue(Runnable runnable, float delay) {
        tasks.add(new GameTask(runnable, delay));
    }

    public Player getPlayerById(int id) {
        return getPlayer(player -> player.getId() == id);
    }

    public Player getPlayerByAccountId(int accountId) {
        return getPlayer(player -> (player.getAccountId() != null) && (player.getAccountId() == accountId));
    }

    private Player getPlayer(Predicate<Player> predicate) {
        return allPlayers.stream().filter(predicate).findAny().orElse(null);
    }

    public Board getBoardByOwnerId(int playerId) {
        return boards.stream().filter(board -> board.getOwners().stream().anyMatch(player -> player.getId() == playerId)).findAny().orElse(null);
    }

    public GameObject getObjectById(int id) {
        for (Board board : boards) {
            GameObject object = board.getObjectById(id);
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    public boolean isWalkOnlyPhase() {
        return getPhaseType() == CAROUSEL;
    }

    public PhaseType getPhaseType() {
        return PhaseMath.getPhaseType(phase);
    }

    public boolean isFinished() {
        return actualPlayers.stream().filter(Character::isAlive).count() <= 1;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeBits(phase, OptimizedBits.SIGNED_INT_TO_128);
        outputStream.writeObjectList(allPlayers, OptimizedBits.SIGNED_INT_TO_32);
        outputStream.writeObjectList(actualPlayers, OptimizedBits.SIGNED_INT_TO_16);
        outputStream.writeObjectList(neutralPlayers, OptimizedBits.SIGNED_INT_TO_16);
        outputStream.writeObjectList(boards, OptimizedBits.SIGNED_INT_TO_32);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        phase = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_128);
        allPlayers = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_32);
        actualPlayers = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_16);
        neutralPlayers = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_16);
        boards = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_32);
    }
}
