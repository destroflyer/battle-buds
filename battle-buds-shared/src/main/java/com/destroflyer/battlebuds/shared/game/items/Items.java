package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.Item;

public class Items {

    public static final Class<? extends Item>[] COMPONENTS = new Class[] {
        AppropriatelySizedWand.class,
        BlueGem.class,
        EnchantedCloak.class,
        Greatsword.class,
        PlatedArmor.class,
        PrecisionGloves.class,
        Quiver.class,
        RedGem.class,
    };

    public static final Class<? extends Item>[] FULL = new Class[] {
        ArcaneCape.class,
        ArcaneHelmet.class,
        BlacksmithsWrath.class,
        BloodCapsule.class,
        Cataclysm.class,
        DemonFigurine.class,
        EgosShield.class,
        EthersArmor.class,
        ForbiddenChapter.class,
        GladiatorHelmet.class,
        HellsScream.class,
        Icecold.class,
        IcyTrinket.class,
        IronBulwark.class,
        MoltenHeart.class,
        NaturesSecret.class,
        NewDawn.class,
        Nightkiss.class,
        OrbOfMight.class,
        RainbowGem.class,
        RapidfireTower.class,
        Requiem.class,
        RunicBlades.class,
        RunicBracers.class,
        SacredNotes.class,
        ShardOfTime.class,
        SilverPlatedPike.class,
        SilverScroll.class,
        SolarDiadem.class,
        Soulblade.class,
        SpikedFlail.class,
        SwiftBow.class,
        SwordOfGreOp.class,
        TheUntamed.class,
        ThunderlordTrident.class,
        Waraxe.class,
    };

    record ItemRecipe (Class<? extends Item> ingredient1, Class<? extends Item> ingredient2, Class<? extends Item> result) {}
    public static final ItemRecipe[] RECIPES = new ItemRecipe[] {
        // -----
        new ItemRecipe(RedGem.class, RedGem.class, BloodCapsule.class),
        new ItemRecipe(RedGem.class, BlueGem.class, RainbowGem.class),
        new ItemRecipe(RedGem.class, Greatsword.class, GladiatorHelmet.class),
        new ItemRecipe(RedGem.class, Quiver.class, SilverScroll.class),
        new ItemRecipe(RedGem.class, AppropriatelySizedWand.class, ForbiddenChapter.class),
        new ItemRecipe(RedGem.class, PrecisionGloves.class, ThunderlordTrident.class),
        new ItemRecipe(RedGem.class, PlatedArmor.class, MoltenHeart.class),
        new ItemRecipe(RedGem.class, EnchantedCloak.class, DemonFigurine.class),
        // -----
        new ItemRecipe(BlueGem.class, BlueGem.class, IcyTrinket.class),
        new ItemRecipe(BlueGem.class, Greatsword.class, RunicBlades.class),
        new ItemRecipe(BlueGem.class, Quiver.class, Icecold.class),
        new ItemRecipe(BlueGem.class, AppropriatelySizedWand.class, Nightkiss.class),
        new ItemRecipe(BlueGem.class, PrecisionGloves.class, ShardOfTime.class),
        new ItemRecipe(BlueGem.class, PlatedArmor.class, RunicBracers.class),
        new ItemRecipe(BlueGem.class, EnchantedCloak.class, OrbOfMight.class),
        // -----
        new ItemRecipe(Greatsword.class, Greatsword.class, SwordOfGreOp.class),
        new ItemRecipe(Greatsword.class, Quiver.class, SilverPlatedPike.class),
        new ItemRecipe(Greatsword.class, AppropriatelySizedWand.class, Soulblade.class),
        new ItemRecipe(Greatsword.class, PrecisionGloves.class, NewDawn.class),
        new ItemRecipe(Greatsword.class, PlatedArmor.class, GladiatorHelmet.class),
        new ItemRecipe(Greatsword.class, EnchantedCloak.class, HellsScream.class),
        // -----
        new ItemRecipe(Quiver.class, Quiver.class, Cataclysm.class),
        new ItemRecipe(Quiver.class, AppropriatelySizedWand.class, RapidfireTower.class),
        new ItemRecipe(Quiver.class, PrecisionGloves.class, SwiftBow.class),
        new ItemRecipe(Quiver.class, PlatedArmor.class, BlacksmithsWrath.class),
        new ItemRecipe(Quiver.class, EnchantedCloak.class, Waraxe.class),
        // -----
        new ItemRecipe(AppropriatelySizedWand.class, AppropriatelySizedWand.class, TheUntamed.class),
        new ItemRecipe(AppropriatelySizedWand.class, PrecisionGloves.class, SacredNotes.class),
        new ItemRecipe(AppropriatelySizedWand.class, PlatedArmor.class, ArcaneHelmet.class),
        new ItemRecipe(AppropriatelySizedWand.class, EnchantedCloak.class, Requiem.class),
        // -----
        new ItemRecipe(PrecisionGloves.class, PrecisionGloves.class, NaturesSecret.class),
        new ItemRecipe(PrecisionGloves.class, PlatedArmor.class, EgosShield.class),
        new ItemRecipe(PrecisionGloves.class, EnchantedCloak.class, SolarDiadem.class),
        // -----
        new ItemRecipe(PlatedArmor.class, PlatedArmor.class, EthersArmor.class),
        new ItemRecipe(PlatedArmor.class, EnchantedCloak.class, IronBulwark.class),
        // -----
        new ItemRecipe(EnchantedCloak.class, EnchantedCloak.class, ArcaneCape.class),
    };

    public static Item createCombinedItem(Item item1, Item item2) {
        Class<? extends Item> combinedItemClass = getCombinedItemClass(item1, item2);
        return Util.createObjectByClass(combinedItemClass);
    }

    public static Class<? extends Item> getCombinedItemClass(Item item1, Item item2) {
        for (ItemRecipe recipe : RECIPES) {
            if ((recipe.ingredient1 == item1.getClass()) && (recipe.ingredient2 == item2.getClass())
             || (recipe.ingredient1 == item2.getClass()) && (recipe.ingredient2 == item1.getClass())) {
                return recipe.result;
            }
        }
        return null;
    }

    public static boolean isComponent(Item item) {
        for (Class<? extends Item> componentClass : COMPONENTS) {
            if (componentClass == item.getClass()) {
                return true;
            }
        }
        return false;
    }

    public static Item createRandomComponent() {
        return createRandomItem(Items.COMPONENTS);
    }

    public static Item createRandomFullItem() {
        return createRandomItem(Items.FULL);
    }

    public static Item createRandomItem(Class<? extends Item>[] itemClasses) {
        Class<? extends Item> itemClass = itemClasses[(int) (Math.random() * itemClasses.length)];
        return Util.createObjectByClass(itemClass);
    }
}
