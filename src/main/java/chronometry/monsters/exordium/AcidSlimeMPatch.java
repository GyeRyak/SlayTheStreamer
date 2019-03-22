package chronometry.monsters.exordium;

import chronometry.patches.AbstractMonsterPatch;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffWeakEffect;
import chronometry.effects.DebuffCardDiscardEffect;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;

import java.util.ArrayList;

public class AcidSlimeMPatch {
    @SpirePatch(clz=AcidSlime_M.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class, int.class, int.class})
    public static class InitMoves {
        public static void Postfix(AcidSlime_M __instance, float x, float y, int poisonAmount, int newHealth) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "WOUND_TACKLE",
                    Intent.ATTACK_DEBUFF,
                    __instance.MOVES[0]
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            move1.add_effect(new DebuffCardDiscardEffect(Slimed.NAME, 1));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "NORMAL_TACKLE",
                    Intent.ATTACK
            );
            move2.add_effect(new AttackEffect(__instance.damage.get(1)));
            moves.add(move2);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "WEAK_LICK",
                    Intent.DEBUFF,
                    __instance.MOVES[1]
            );
            move4.add_effect(new DebuffWeakEffect(1));
            moves.add(move4);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
