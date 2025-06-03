package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.augments.Augments;
import com.destroflyer.battlebuds.shared.game.boards.*;
import com.destroflyer.battlebuds.shared.game.objects.*;
import com.destroflyer.battlebuds.shared.game.objects.Character;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;
import com.destroflyer.battlebuds.shared.game.objects.players.BotPlayer;
import com.destroflyer.battlebuds.shared.game.objects.players.HumanPlayer;
import com.destroflyer.battlebuds.shared.game.objects.players.NeutralPlayer;
import com.destroflyer.battlebuds.shared.game.spells.TestSpell;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import com.destroflyer.battlebuds.shared.lobby.LobbyPlayer;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.destroflyer.battlebuds.shared.game.PhaseType.CAROUSEL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Game implements GameSerializable {

    public Game(LobbyGame lobbyGame) {
        this.lobbyGame = lobbyGame;

        // Players

        int actualPlayerCount = lobbyGame.getGameMode().getPlayerCount();
        if (actualPlayerCount == 1) {
            actualPlayerCount = 8;
        }
        int botsCount = (actualPlayerCount - lobbyGame.getPlayers().size());

        for (LobbyPlayer lobbyPlayer : lobbyGame.getPlayers()) {
            Account account = lobbyPlayer.getAccount();
            HumanPlayer humanPlayer = new HumanPlayer(account.getId());
            humanPlayer.setName(account.getLogin());
            humanPlayer.setVisualName(lobbyPlayer.getCharacterName());
            initializeActualPlayer(humanPlayer);
            players.add(humanPlayer);
        }
        for (int i = 0; i < botsCount; i++) {
            BotPlayer botPlayer = new BotPlayer();
            botPlayer.setName("Bot " + (i + 1));
            botPlayer.setVisualName("garmon");
            initializeActualPlayer(botPlayer);
            players.add(botPlayer);
        }
        for (int i = 0; i < actualPlayerCount; i++) {
            players.add(new NeutralPlayer());
        }
        for (ActualPlayer actualPlayer : getActualPlayers()) {
            actualPlayer.setCurrentHealth(actualPlayer.getMaximumHealth());
        }
        for (Player player : players) {
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
                addToUnitPool(unit);

                // FIXME: Test content
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
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Unit>[] unitPoolsByCost = new ArrayList[MAXIMUM_BUYABLE_UNIT_COST];
    @Getter
    private int phase = -1;
    @Getter
    private ArrayList<Board> boards = new ArrayList<>();
    private int nextObjectId = 1;
    private ArrayList<GameTask> tasks = new ArrayList<>();

    private void initializeActualPlayer(ActualPlayer actualPlayer) {
        PlanningBoard planningBoard = new PlanningBoard();
        planningBoard.setGame(this);
        ArrayList<Player> planningBoardOwners = new ArrayList<>();
        planningBoardOwners.add(actualPlayer);
        planningBoard.setOwners(planningBoardOwners);
        actualPlayer.setPlanningBoard(planningBoard);
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
        HashMap<ActualPlayer, ArrayList<PickupObject>> leftoverOwnedPickupObjects = new HashMap<>();
        for (Board board : boards) {
            board.onFinish();
            for (GameObject object : board.getObjects()) {
                if ((object instanceof PickupObject pickupObject) && (pickupObject.getOwner() != null)) {
                    leftoverOwnedPickupObjects.computeIfAbsent(pickupObject.getOwner(), _ -> new ArrayList<>()).add(pickupObject);
                }
            }
        }

        boards.clear();
        tasks.clear();

        phase++;
        createAndAddBoards();

        PhaseType phaseType = getPhaseType();
        for (Board board : boards) {
            for (Player player : board.getOwners()) {
                board.addObject(player);
                // Needs to be done before updating slot unit board states (so that they will be added to the board)
                player.resetUnitsRemovedFromBoard();
                // Needs to be done before resetting, as resetting involves calculations that require the board to be set (Example: A trait bonus for maximum health that looks at the units on the board)
                player.updateSlotUnitsBoardStates(true);
                player.resetUnits();
                if (leftoverOwnedPickupObjects.containsKey(player)) {
                    for (PickupObject pickupObject : leftoverOwnedPickupObjects.get(player)) {
                        board.addObject(pickupObject);
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

                if (player instanceof ActualPlayer actualPlayer) {
                    if (actualPlayer instanceof HumanPlayer humanPlayer) {
                        humanPlayer.watchPlayer(humanPlayer);
                    }
                    // Easiest way for now to get the first unit
                    if (phase == 1) {
                        actualPlayer.addGold(1);
                        actualPlayer.tryBuyUnit(0);
                        reroll(actualPlayer);
                    }
                }
            }
            board.onStart();
        }

        if (PhaseMath.isAugmentOffered(phase)) {
            offerAugments();
        }
    }

    private void createAndAddBoards() {
        List<ActualPlayer> aliveActualPlayers = getActualPlayers().stream().filter(Character::isAlive).toList();
        PhaseType phaseType = getPhaseType();
        switch (phaseType) {
            case CAROUSEL -> {
                CarouselBoard carouselBoard = new CarouselBoard();
                carouselBoard.setGame(this);
                carouselBoard.setOwners(aliveActualPlayers);
                boards.add(carouselBoard);
            }
            case PLANNING -> {
                for (ActualPlayer actualPlayer : aliveActualPlayers) {
                    addPlanningBoard(actualPlayer);
                }
            }
            case COMBAT_NEUTRAL -> {
                for (ActualPlayer actualPlayer : aliveActualPlayers) {
                    // Technically it doesn't matter which neutral player to battle, but this could make debugging easier
                    NeutralPlayer neutralPlayer = getNeutralPlayers().get(getActualPlayers().indexOf(actualPlayer));
                    neutralPlayer.setupSlotUnits();
                    addCombatBoard(new NeutralCombatBoard(), actualPlayer, neutralPlayer);
                }
            }
            case COMBAT_PLAYER -> {
                ArrayList<ActualPlayer> remainingPlayers = new ArrayList<>(aliveActualPlayers);
                Collections.shuffle(remainingPlayers);
                while (remainingPlayers.size() >= 2) {
                    ActualPlayer actualPlayer1 = remainingPlayers.removeFirst();
                    ActualPlayer actualPlayer2 = remainingPlayers.removeFirst();
                    addCombatBoard(new PlayerCombatBoard(), actualPlayer1, actualPlayer2);
                }
                // No ghosts supported yet, so one player gets a free round
                if (remainingPlayers.size() > 0) {
                    addPlanningBoard(remainingPlayers.getFirst());
                }
            }
        }
    }

    private void addPlanningBoard(ActualPlayer actualPlayer) {
        PlanningBoard planningBoard = actualPlayer.getPlanningBoard();
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

        if (player1 instanceof ActualPlayer actualPlayer1) {
            actualPlayer1.fillBoardFromBenchIfSpaceLeft();
        }
        if (player2 instanceof ActualPlayer actualPlayer2) {
            actualPlayer2.fillBoardFromBenchIfSpaceLeft();
        }
    }

    public void reroll(ActualPlayer actualPlayer) {
        // Put back old units
        for (Unit oldShopUnit : actualPlayer.getShopUnits()) {
            if (oldShopUnit != null) {
                unitPoolsByCost[oldShopUnit.getCost() - 1].add(oldShopUnit);
            }
        }
        // Roll new units
        int[] playerShopProbabilities = actualPlayer.getShopProbabilities();
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
        actualPlayer.setShopUnits(newShopUnits);
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

    public void addToUnitPool(Unit unit) {
        int costIndex = unit.getCost() - 1;
        unitPoolsByCost[costIndex].add(unit);
        unit.reset();
        unit.removeItems();
    }

    private void offerAugments() {
        for (ActualPlayer actualPlayer : getActualPlayers()) {
            ArrayList<Augment> augments = Augments.createRandomAugments(ActualPlayer.DECISION_OPTIONS_COUNT);
            Decision decision = new Decision(augments.stream()
                    .map(augment -> new DecisionOption(augment.getTier(), augment.getName() + "\n\n" + augment.getDescription(), () -> actualPlayer.addAugment(augment)))
                    .toList());
            actualPlayer.addDecision(decision);
        }
    }

    public void enqueue(Runnable runnable, float delay) {
        tasks.add(new GameTask(runnable, delay));
    }

    public List<ActualPlayer> getActualPlayers() {
        return getPlayersStream(player -> player instanceof ActualPlayer).map(player -> (ActualPlayer) player).toList();
    }

    public List<NeutralPlayer> getNeutralPlayers() {
        return getPlayersStream(player -> player instanceof NeutralPlayer).map(player -> (NeutralPlayer) player).toList();
    }

    public Player getPlayerById(int id) {
        return getPlayer(player -> player.getId() == id);
    }

    public HumanPlayer getHumanPlayerByAccountId(int accountId) {
        return (HumanPlayer) getPlayer(player -> (player instanceof HumanPlayer humanPlayer) && (humanPlayer.getAccountId() == accountId));
    }

    private Player getPlayer(Predicate<Player> predicate) {
        return getPlayersStream(predicate).findAny().orElse(null);
    }

    private Stream<Player> getPlayersStream(Predicate<Player> predicate) {
        return players.stream().filter(predicate);
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
        return getActualPlayers().stream().filter(Character::isAlive).count() <= 1;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeBits(phase, OptimizedBits.SIGNED_INT_TO_128);
        outputStream.writeObjectList(players, OptimizedBits.SIGNED_INT_TO_32);
        outputStream.writeObjectList(boards, OptimizedBits.SIGNED_INT_TO_32);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        phase = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_128);
        players = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_32);
        boards = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_32);
    }
}
