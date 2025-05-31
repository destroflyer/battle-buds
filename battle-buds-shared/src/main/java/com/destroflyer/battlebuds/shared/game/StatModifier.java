package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.Unit;

public interface StatModifier {
    default float getBonusMaximumHealthPercent(Unit unit) { return 0; }
    default float getBonusMaximumHealthFlat(Unit unit) { return 0; }
    default float getBonusHealthRegenerationPercent(Unit unit) { return 0; }
    default float getBonusHealthRegenerationFlat(Unit unit) { return 0; }
    default float getBonusMaximumManaPercent(Unit unit) { return 0; }
    default float getBonusMaximumManaFlat(Unit unit) { return 0; }
    default float getBonusInitialManaPercent(Unit unit) { return 0; }
    default float getBonusInitialManaFlat(Unit unit) { return 0; }
    default float getBonusManaRegenerationPercent(Unit unit) { return 0; }
    default float getBonusManaRegenerationFlat(Unit unit) { return 0; }
    default float getBonusArmorPercent(Unit unit) { return 0; }
    default float getBonusArmorFlat(Unit unit) { return 0; }
    default float getBonusMagicResistancePercent(Unit unit) { return 0; }
    default float getBonusMagicResistanceFlat(Unit unit) { return 0; }
    default float getBonusAttackDamagePercent(Unit unit) { return 0; }
    default float getBonusAttackDamageFlat(Unit unit) { return 0; }
    default float getBonusAttackSpeedPercent(Unit unit) { return 0; }
    default float getBonusAttackSpeedFlat(Unit unit) { return 0; }
    default float getBonusAttackRangePercent(Unit unit) { return 0; }
    default float getBonusAttackRangeFlat(Unit unit) { return 0; }
    default float getBonusDodgeChancePercent(Unit unit) { return 0; }
    default float getBonusDodgeChanceFlat(Unit unit) { return 0; }
    default float getBonusAbilityPowerPercent(Unit unit) { return 0; }
    default float getBonusAbilityPowerFlat(Unit unit) { return 0; }
    default float getBonusCritChancePercent(Unit unit) { return 0; }
    default float getBonusCritChanceFlat(Unit unit) { return 0; }
    default float getBonusCritDamagePercent(Unit unit) { return 0; }
    default float getBonusCritDamageFlat(Unit unit) { return 0; }
    default float getBonusOmnivampPercent(Unit unit) { return 0; }
    default float getBonusOmnivampFlat(Unit unit) { return 0; }
    default float getBonusDamageDealtAmplificationPercent(Unit unit) { return 0; }
    default float getBonusDamageDealtAmplificationFlat(Unit unit) { return 0; }
    default float getBonusDamageTakenAmplificationPercent(Unit unit) { return 0; }
    default float getBonusDamageTakenAmplificationFlat(Unit unit) { return 0; }
    default float getBonusMovementPercent(Unit unit) { return 0; }
    default float getBonusMovementFlat(Unit unit) { return 0; }
}
