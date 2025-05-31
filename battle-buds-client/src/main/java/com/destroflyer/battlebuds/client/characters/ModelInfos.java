package com.destroflyer.battlebuds.client.characters;

import java.util.HashMap;

public class ModelInfos {

    private static HashMap<String, ModelInfo> MODEL_INFOS = new HashMap<>();

    public static ModelInfo get(String visualName) {
        return MODEL_INFOS.computeIfAbsent(visualName, _ -> {
            switch (visualName) {
                case "aland":
                    return new ModelInfo(
                        "aland",
                        null,
                        new AnimationInfo("idle", 11.267f),
                        new AnimationInfo("run"),
                        6f,
                        new AnimationInfo("throw1"),
                        new AnimationInfo("throw2"),
                        new AnimationInfo("chicken_dance", 4.8f)
                    );
                case "alice":
                    return new ModelInfo(
                        "alice",
                        null,
                        new AnimationInfo("idle1", 1.867f),
                        new AnimationInfo("run"),
                        6f,
                        new AnimationInfo("spell2"),
                        new AnimationInfo("spell3"),
                        new AnimationInfo("hip_hop_dance", 15.8f)
                    );
                case "beetle_golem":
                    return new ModelInfo(
                        "beetle_golem",
                        null,
                        new AnimationInfo("Idle_Normal", 2.5f),
                        new AnimationInfo("Walk"),
                        null,
                        new AnimationInfo("Attack1"),
                        new AnimationInfo("Sleep_Idle"),
                        null
                    );
                case "chicken":
                    return new ModelInfo(
                        "chicken",
                        null,
                        new AnimationInfo("idle3", 1.48f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("pecking3"),
                        null,
                        null
                    );
                case "cow":
                    return new ModelInfo(
                        "cow",
                        null,
                        null,
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        new AnimationInfo("bodyslam"),
                        null
                    );
                case "daydream":
                    return new ModelInfo(
                        "daydream",
                        null,
                        new AnimationInfo("stand", 8f),
                        new AnimationInfo("walk"),
                        7f,
                        new AnimationInfo("attack_1"),
                        null,
                        null
                    );
                case "dosaz":
                    return new ModelInfo(
                        "dosaz",
                        null,
                        new AnimationInfo("idle", 7.417f),
                        new AnimationInfo("run2"),
                        7f,
                        new AnimationInfo("spell3"),
                        new AnimationInfo("spell9"),
                        new AnimationInfo("dance", 15.833f)
                    );
                case "dwarf_warrior":
                    return new ModelInfo(
                        "dwarf_warrior",
                        null,
                        new AnimationInfo("idle1", 7.875f),
                        new AnimationInfo("run2"),
                        6f,
                        new AnimationInfo("attack_downwards"),
                        new AnimationInfo("battlecry"),
                        new AnimationInfo("breakdance", 0.4f)
                    );
                case "earth_elemental":
                    return new ModelInfo(
                        "earth_elemental",
                        null,
                        new AnimationInfo("idle", 2.5f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("punch_right"),
                        new AnimationInfo("roll_loop", 0.3f),
                        null
                    );
                case "elven_archer":
                    return new ModelInfo(
                        "elven_archer",
                        null,
                        new AnimationInfo("idle1", 5.1f),
                        new AnimationInfo("run1"),
                        6f,
                        new AnimationInfo("shoot_arrow"),
                        new AnimationInfo("draw_arrow", 0.2f),
                        new AnimationInfo("jazz_dance", 5.1f)
                    );
                case "erika":
                    return new ModelInfo(
                        "erika",
                        null,
                        new AnimationInfo("standing_idle_01", 6f),
                        new AnimationInfo("walking_inPlace"),
                        4f,
                        new AnimationInfo("shoot_arrow"),
                        new AnimationInfo("shoot_arrow", 0.2f),
                        new AnimationInfo("samba_dancing")
                    );
                case "fire_dragon":
                    return new ModelInfo(
                        "fire_dragon",
                        null,
                        new AnimationInfo("idle", 4f),
                        new AnimationInfo("walk"),
                        6.5f,
                        new AnimationInfo("projectile_attack"),
                        new AnimationInfo("cast_spell"),
                        null
                    );
                case "flying_minion":
                    return new ModelInfo(
                        "flying_minion",
                        null,
                        new AnimationInfo("walk", 3f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        null,
                        null
                    );
                case "forest_monster":
                    return new ModelInfo(
                        "forest_monster",
                        null,
                        new AnimationInfo("Melee_Hold", 2f),
                        new AnimationInfo("Walk"),
                        null,
                        new AnimationInfo("Attack"),
                        null,
                        null
                    );
                case "ganfaul":
                    return new ModelInfo(
                        "ganfaul",
                        null,
                        new AnimationInfo("standing_idle", 2.5f),
                        new AnimationInfo("standing_walk_forward"),
                        6f,
                        new AnimationInfo("standing_1h_magic_attack_01"),
                        new AnimationInfo("standing_2h_magic_area_attack_01"),
                        new AnimationInfo("hip_hop_dancing")
                    );
                case "garmon":
                    return new ModelInfo(
                        "garmon",
                        null,
                        new AnimationInfo("idle2", 10f),
                        new AnimationInfo("walk2"),
                        6f,
                        new AnimationInfo("attack_right"),
                        new AnimationInfo("spell11"),
                        new AnimationInfo("arm_wave", 4.166f)
                    );
                case "gentleman_minion":
                    return new ModelInfo(
                        "minion",
                        "gentleman",
                        null,
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        new AnimationInfo("cast_1"),
                        new AnimationInfo("dance", 1.75f)
                    );
                case "ghost":
                    return new ModelInfo(
                        "ghost",
                        null,
                        new AnimationInfo("idle", 1.5f),
                        new AnimationInfo("fly_forward"),
                        6f,
                        new AnimationInfo("melee_attack"),
                        new AnimationInfo("cast_spell"),
                        null
                    );
                case "golem_minion":
                    return new ModelInfo(
                        "golem_minion",
                        null,
                        null,
                        new AnimationInfo("walk"),
                        6f, // TODO: Check (guessed)
                        new AnimationInfo("auto_attack"),
                        null,
                        null
                    );
                case "jaime":
                    return new ModelInfo(
                        "jaime",
                        null,
                        new AnimationInfo("Idle", 8f),
                        new AnimationInfo("Walk"),
                        4.5f,
                        new AnimationInfo("Punches"),
                        new AnimationInfo("Taunt"),
                        new AnimationInfo("Wave")
                    );
                case "kachujin":
                    return new ModelInfo(
                        "kachujin",
                        null,
                        new AnimationInfo("fighting_idle", 4.5f),
                        new AnimationInfo("running_inPlace"),
                        5f,
                        new AnimationInfo("boxing"),
                        new AnimationInfo("side_kick"),
                        null
                    );
                case "legion_soldier":
                    return new ModelInfo(
                        "legion_soldier",
                        null,
                        new AnimationInfo("idle", 2f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("attack_1"),
                        null,
                        null
                    );
                case "legion_archer":
                    return new ModelInfo(
                        "legion_archer",
                        null,
                        new AnimationInfo("idle", 2f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("shoot_arrow"),
                        null,
                        null
                    );
                case "little_dragon":
                    return new ModelInfo(
                        "little_dragon",
                        null,
                        new AnimationInfo("default", 8f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        null,
                        null
                    );
                case "loot_legendary":
                    return new ModelInfo("cube", "loot_legendary");
                case "loot_rare":
                    return new ModelInfo("cube", "loot_rare");
                case "maria":
                    return new ModelInfo(
                        "maria",
                        null,
                        new AnimationInfo("great_sword_idle", 2f),
                        new AnimationInfo("great_sword_walk_inPlace"),
                        null,
                        new AnimationInfo("great_sword_slash"),
                        new AnimationInfo("standing_melee_attack_360_low"),
                        new AnimationInfo("ymca_dance")
                    );
                case "maw":
                    return new ModelInfo(
                        "maw",
                        null,
                        new AnimationInfo("orc_idle", 8f),
                        new AnimationInfo("walking_inPlace"),
                        6f,
                        new AnimationInfo("hook_punch"),
                        new AnimationInfo("jumping", 2.5f),
                        new AnimationInfo("house_dancing")
                    );
                case "minion":
                    return new ModelInfo(
                        "minion",
                        null,
                        null,
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        new AnimationInfo("cast_1"),
                        new AnimationInfo("dance", 1.75f)
                    );
                case "nathalya":
                    return new ModelInfo(
                        "nathalya",
                        null,
                        new AnimationInfo("combatReadyA", 2.5f),
                        new AnimationInfo("walk"),
                        6f,
                        new AnimationInfo("magicShotSpell"),
                        new AnimationInfo("spellCastC"),
                        null
                    );
                case "oz":
                    return new ModelInfo(
                        "oz",
                        null,
                        new AnimationInfo("stand", 8f),
                        new AnimationInfo("walk"),
                        8f,
                        new AnimationInfo("attack1"),
                        new AnimationInfo("attack1"),
                        null
                    );
                case "pseudospider":
                    return new ModelInfo(
                        "pseudospider",
                        null,
                        new AnimationInfo("idle", 1.5f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("melee_attack"),
                        new AnimationInfo("leap_attack"),
                        null
                    );
                case "rebellion_archer":
                    return new ModelInfo(
                            "legion_archer",
                            "rebellion_archer",
                            new AnimationInfo("idle", 2f),
                            new AnimationInfo("walk"),
                            null,
                            new AnimationInfo("shoot_arrow"),
                            null,
                            null
                    );
                case "rebellion_assassin":
                    return new ModelInfo(
                        "legion_soldier",
                        "rebellion_assassin",
                        new AnimationInfo("idle", 2f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("attack_1"),
                        null,
                        null
                    );
                case "rebellion_baron":
                    return new ModelInfo(
                        "cow",
                        "baron",
                        null,
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        new AnimationInfo("bodyslam"),
                        null
                    );
                case "rebellion_elite":
                    return new ModelInfo(
                        "legion_archer",
                        "rebellion_elite",
                        new AnimationInfo("idle", 2f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("shoot_arrow"),
                        null,
                        null
                    );
                case "rebellion_giant":
                    return new ModelInfo(
                        "legion_soldier",
                        "rebellion_giant",
                        new AnimationInfo("idle", 2f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("attack_1"),
                        null,
                        null
                    );
                case "rebellion_soldier":
                    return new ModelInfo(
                        "legion_soldier",
                        "rebellion_soldier",
                        new AnimationInfo("idle", 2f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("attack_1"),
                        null,
                        null
                    );
                case "robot_minion":
                    return new ModelInfo(
                        "robot_minion",
                        null,
                        null,
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        new AnimationInfo("grab"),
                        null
                    );
                case "scarlet":
                    return new ModelInfo(
                        "scarlet",
                        null,
                        new AnimationInfo("idle", 2f),
                        new AnimationInfo("run3"),
                        7f,
                        new AnimationInfo("stab"),
                        new AnimationInfo("down_hit"),
                        new AnimationInfo("ymca_dance", 3.6f)
                    );
                case "shyvana":
                    return new ModelInfo(
                        "shyvana",
                        null,
                        new AnimationInfo("idle", 5.42f),
                        new AnimationInfo("walk"),
                        null, // TODO: Check (Amara had it like this)
                        new AnimationInfo("auto_attack"),
                        null,
                        null
                    );
                case "sinbad":
                    return new ModelInfo(
                        "sinbad",
                        null,
                        new AnimationInfo("IdleTop", 10f), // TODO: Check
                        new AnimationInfo("RunBase"),
                        6f, // TODO: Check (guessed)
                        new AnimationInfo("SliceHorizontal"),
                        new AnimationInfo("SliceVertical"),
                        new AnimationInfo("Dance")
                    );
                case "soldier":
                    return new ModelInfo(
                        "soldier",
                        null,
                        null,
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        new AnimationInfo("spin", 0.3f),
                        null
                    );
                case "steve":
                    return new ModelInfo(
                        "steve",
                        null,
                        new AnimationInfo("stand", 8f),
                        new AnimationInfo("walk"),
                        5f,
                        new AnimationInfo("punch"),
                        null,
                        null
                    );
                case "swimming_minion":
                    return new ModelInfo(
                        "swimming_minion",
                        null,
                        null,
                        new AnimationInfo("walk"),
                        null, // TODO: Check, but probably fine
                        new AnimationInfo("walk"),
                        null,
                        null
                    );
                case "tristan":
                    return new ModelInfo(
                        "tristan",
                        null,
                        new AnimationInfo("idle1", 8f),
                        new AnimationInfo("run1"),
                        7.8f,
                        new AnimationInfo("slash1"),
                        new AnimationInfo("spin"),
                        new AnimationInfo("hip_hop_dance", 4.5f)
                    );
                case "varus":
                    return new ModelInfo(
                        "varus",
                        null,
                        new AnimationInfo("idle", 3f),
                        new AnimationInfo("walk"),
                        null, // TODO: Check (Amara had it like this)
                        new AnimationInfo("auto_attack"),
                        null,
                        null
                    );
                case "wizard_minion":
                    return new ModelInfo(
                        "wizard_minion",
                        null,
                        new AnimationInfo("idle", 6f),
                        new AnimationInfo("walk"),
                        null,
                        new AnimationInfo("auto_attack"),
                        new AnimationInfo("auto_attack"),
                        new AnimationInfo("dance", 2f)
                    );
                default:
                    return new ModelInfo(visualName);
            }
        });
    }
}
