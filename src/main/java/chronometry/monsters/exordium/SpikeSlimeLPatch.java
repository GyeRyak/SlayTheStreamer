package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffCardDiscardEffect;
import chronometry.effects.DebuffFrailEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;

import java.util.ArrayList;

public class SpikeSlimeLPatch {
    @SpirePatch(clz=SpikeSlime_L.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(SpikeSlime_L __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "FLAME_TACKLE",
                    Intent.ATTACK_DEBUFF
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            move1.add_effect(new DebuffCardDiscardEffect(Slimed.NAME, 2));
            moves.add(move1);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "FRAIL_LICK",
                    Intent.DEBUFF,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "FRAIL_NAME")
            );
            int frailAmount = 2;
            if (AbstractDungeon.ascensionLevel >= 17) {
                frailAmount = 3;
            }
            move4.add_effect(new DebuffFrailEffect(frailAmount));
            moves.add(move4);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
