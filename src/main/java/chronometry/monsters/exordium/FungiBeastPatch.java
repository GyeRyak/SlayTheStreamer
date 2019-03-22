package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.patches.AbstractMonsterPatch;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.BuffStrengthEffect;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast;

import java.util.ArrayList;

public class FungiBeastPatch {
    @SpirePatch(clz=FungiBeast.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(FungiBeast __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "BITE",
                    Intent.ATTACK
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "GROW",
                    Intent.BUFF,
                    __instance.MOVES[0]
            );
            int strAmt = (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "strAmt");
            if (AbstractDungeon.ascensionLevel >= 17) {
                strAmt += 1;
            }
            move2.add_effect(new BuffStrengthEffect(strAmt));
            moves.add(move2);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
