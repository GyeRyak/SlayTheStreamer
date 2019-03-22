package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import chronometry.patches.AbstractMonsterPatch;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DefendEffect;
import chronometry.effects.BuffStrengthEffect;

import java.util.ArrayList;

public class JawWormPatch {
    @SpirePatch(clz=JawWorm.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class, boolean.class})
    public static class InitMoves {
        public static void Postfix(JawWorm __instance, final float x, final float y, final boolean hard) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "CHOMP",
                    Intent.ATTACK
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "BELLOW",
                    Intent.DEFEND_BUFF,
                    __instance.MOVES[0]
            );
            move2.add_effect(new DefendEffect(
                    (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "bellowBlock")
            ));
            move2.add_effect(new BuffStrengthEffect(
                    (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "bellowStr")
            ));
            moves.add(move2);

            IntentData move3 = new IntentData(
                    __instance.getClass(),
                    "THRASH",
                    Intent.ATTACK_DEFEND,
                    __instance.MOVES[1]
            );
            move3.add_effect(new AttackEffect(__instance.damage.get(1)));
            move3.add_effect(new DefendEffect(
                    (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "thrashBlock")
            ));
            moves.add(move3);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
