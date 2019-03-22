package chronometry.monsters.exordium;

import chronometry.patches.AbstractMonsterPatch;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffWeakEffect;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;

import java.util.ArrayList;

public class LouseDefensivePatch {
    @SpirePatch(clz=LouseDefensive.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(LouseDefensive __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move3 = new IntentData(
                    __instance.getClass(),
                    "BITE",
                    Intent.ATTACK
            );
            move3.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move3);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "WEAKEN",
                    Intent.DEBUFF,
                    __instance.MOVES[0]
            );
            move4.add_effect(new DebuffWeakEffect(2));
            moves.add(move4);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
