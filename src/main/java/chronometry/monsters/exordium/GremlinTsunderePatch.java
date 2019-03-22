package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DefendOtherEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.GremlinTsundere;

import java.util.ArrayList;

public class GremlinTsunderePatch {
    @SpirePatch(clz=GremlinTsundere.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(GremlinTsundere __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "PROTECT",
                    Intent.DEFEND,
                    __instance.MOVES[0]
            );
            move1.add_effect(new DefendOtherEffect(
                    (int) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "blockAmt")
            ));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "BASH",
                    Intent.ATTACK,
                    __instance.MOVES[1]
            );
            move2.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move2);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
