package chronometry.effects;

import chronometry.MoveEffect;
import chronometry.SlayTheStreamer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UniqueEffect extends MoveEffect {
    public UniqueEffect(Class<? extends AbstractMonster> monsterClass, String effectString) {
        this(monsterClass, effectString, 0, 0);
    }

    public UniqueEffect(Class<? extends AbstractMonster> monsterClass, String effectString, int number) {
        this(monsterClass, effectString, number, 0);
    }

    public UniqueEffect(Class<? extends AbstractMonster> monsterClass, String effectString, int number, int multiplier) {
        this.effect_string = SlayTheStreamer.localizedMonsterMoves.get(monsterClass.getSimpleName()).get(effectString);
        this.number = number;
        this.multiplier = multiplier;
    }

    @Override
    public String toString() {
        return effect_string.replaceAll("\\{num\\}", String.valueOf(this.number))
                .replaceAll("\\{mul\\}", String.valueOf(this.multiplier));
    }
}
