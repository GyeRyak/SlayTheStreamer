package chronometry.effects;

import chronometry.MoveEffect;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class AttackEffect extends MoveEffect {
    public DamageInfo info;

    public AttackEffect(DamageInfo damage) {
        this(damage, 1);
    }

    public AttackEffect(DamageInfo damage, int multiplier) {
        this.info = damage;
        this.multiplier = multiplier;
    }

    public int number() {
        return this.info.output;
    }
}
