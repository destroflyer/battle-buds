package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.Trait;

import java.util.HashMap;

public class Traits {

    public static final Class<? extends Trait>[] CLASSES = new Class[] {
        AssassinTrait.class,
        BeastTrait.class,
        BrawlerTrait.class,
        DragonTrait.class,
        FairyTrait.class,
        ForestTrait.class,
        GolemTrait.class,
        GuardianTrait.class,
        LeaderTrait.class,
        LegionTrait.class,
        MinionTrait.class,
        PirateTrait.class,
        RangerTrait.class,
        SkybornTrait.class,
        TricksterTrait.class,
        UndeadTrait.class,
        WizardTrait.class
    };

    private static HashMap<Class, Trait> REFERENCE_TRAITS = new HashMap<>();
    static {
        for (Class<? extends Trait> traitClass : CLASSES) {
            REFERENCE_TRAITS.put(traitClass, Util.createObjectByClass(traitClass));
        }
    }

    public static Trait getReferenceTrait(Class<? extends Trait> traitClass) {
        return REFERENCE_TRAITS.get(traitClass);
    }
}
