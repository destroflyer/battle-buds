package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.Unit;

import java.util.List;
import java.util.function.Consumer;

public interface GameEventListener {
    default void onPlanningRoundStart() { forwardEvent(GameEventListener::onPlanningRoundStart); }
    default void onCombatRoundStart() { forwardEvent(GameEventListener::onCombatRoundStart); }
    default void onAllyUnitAttack(Unit unit, Unit target) { forwardEvent(eventListener -> eventListener.onAllyUnitAttack(unit, target)); }
    default void onAllyUnitDeath(Unit unit) { forwardEvent(eventListener -> eventListener.onAllyUnitDeath(unit)); }
    default void onEnemyUnitDeath(Unit unit) { forwardEvent(eventListener -> eventListener.onEnemyUnitDeath(unit)); }

    default void forwardEvent(Consumer<GameEventListener> callEventListener) {
        List<? extends GameEventListener> eventListeners = getEventListenersToForwardTo();
        if (eventListeners != null) {
            for (GameEventListener eventListener : eventListeners) {
                callEventListener.accept(eventListener);
            }
        }
    }

    default List<? extends GameEventListener> getEventListenersToForwardTo() {
        return null;
    }
}
