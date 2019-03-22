package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffWeakEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.SlaverBlue;

import java.util.ArrayList;

public class SlaverBluePatch {
    @SpirePatch(clz=SlaverBlue.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(SlaverBlue __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "STAB",
                    Intent.ATTACK
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move1);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "RAKE",
                    Intent.ATTACK_DEBUFF,
                    __instance.MOVES[0]
            );
            int weakAmt = (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "weakAmt");
            if (AbstractDungeon.ascensionLevel >= 17) {
                weakAmt += 1;
            }
            move4.add_effect(new AttackEffect(__instance.damage.get(1)));
            move4.add_effect(new DebuffWeakEffect(weakAmt));
            moves.add(move4);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
