package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.Augment;

import java.util.ArrayList;

public class Augments {

    public static final Class<? extends Augment>[] CLASSES = new Class[] {
        BigGrabBag.class,
        HedgeFund.class,
        ItemGrabBag1.class,
        Placebo.class,
        PlaceboPlus.class,
        PumpingUp1.class,
        PumpingUp2.class,
        PumpingUp3.class,
        RichGetRicher.class,
        SilverSpoon.class,
    };

    public static ArrayList<Augment> createRandomAugments(int count) {
        if (count > CLASSES.length) {
            throw new IllegalArgumentException("Requested augments count: " + count);
        }
        ArrayList<Augment> augments = new ArrayList<>();
        while (augments.size() < count) {
            Class<? extends Augment> augmentClass = CLASSES[(int) (Math.random() * CLASSES.length)];
            if (augments.stream().noneMatch(augment -> augment.getClass() == augmentClass)) {
                Augment augment = Util.createObjectByClass(augmentClass);
                augments.add(augment);
            }
        }
        return augments;
    }
}
