package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffCardDiscardEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;

import java.util.ArrayList;

public class SentryPatch {
    @SpirePatch(clz=Sentry.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(Sentry __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move3 = new IntentData(
                    __instance.getClass(),
                    "BOLT",
                    Intent.DEBUFF
            );
            move3.add_effect(new DebuffCardDiscardEffect(
                    Dazed.NAME,
                    (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "dazedAmt")
            ));
            moves.add(move3);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "BEAM",
                    Intent.ATTACK
            );
            move4.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move4);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
