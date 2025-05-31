package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;

public class TricksterTrait extends Trait {

    public TricksterTrait() {
        name = "Trickster";
        description = "[TODO: Is not implemented yet!]\n\nGain unique items that can only be equipped by Tricksters.\n\n(2) TODO\n(3) TODO\n(4) TODO\n(5) TODO\n(6) TODO\n(7) TODO";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(2, 3, 4, 7);
    }
}
