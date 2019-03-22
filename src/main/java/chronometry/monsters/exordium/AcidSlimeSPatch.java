package chronometry.monsters.exordium;

import chronometry.patches.AbstractMonsterPatch;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffWeakEffect;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;

import java.util.ArrayList;

public class AcidSlimeSPatch {
    @SpirePatch(clz=AcidSlime_S.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class, int.class})
    public static class AcidSlimeSInitMoves {
        public static void Postfix(AcidSlime_S __instance, float x, float y, int poisonAmount) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "TACKLE",
                    Intent.ATTACK
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "DEBUFF",
                    Intent.DEBUFF
            );
            move2.add_effect(new DebuffWeakEffect(1));
            moves.add(move2);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
