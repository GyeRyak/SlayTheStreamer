package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DefendEffect;
import chronometry.effects.UniqueEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.Looter;

import java.util.ArrayList;

public class LooterPatch {
    @SpirePatch(clz=Looter.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(Looter __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "MUG",
                    Intent.ATTACK,
                    __instance.MOVES[1]
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "SMOKE_BOMB",
                    Intent.DEFEND
            );
            move2.add_effect(new DefendEffect(
                    (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "escapeDef")
            ));
            moves.add(move2);

            IntentData move3 = new IntentData(
                    __instance.getClass(),
                    "ESCAPE",
                    Intent.ESCAPE
            );
            move3.add_effect(new UniqueEffect(
                    __instance.getClass(),
                    "UNIQUE_1"
            ));
            moves.add(move3);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "LUNGE",
                    Intent.ATTACK,
                    __instance.MOVES[0]
            );
            move4.add_effect(new AttackEffect(__instance.damage.get(1)));
            moves.add(move4);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
