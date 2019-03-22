package chronometry.effects;

import chronometry.MoveEffect;

public class DebuffCardDiscardEffect extends MoveEffect {
    public DebuffCardDiscardEffect(String card_name, int amount) {
        this.effect_string = card_name;
        this.number = amount;
    }
}
