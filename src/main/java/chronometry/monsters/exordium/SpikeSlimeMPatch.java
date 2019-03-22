package chronometry.monsters.exordium;

import chronometry.patches.AbstractMonsterPatch;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffFrailEffect;
import chronometry.effects.DebuffCardDiscardEffect;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_M;

import java.util.ArrayList;

public class SpikeSlimeMPatch {
    @SpirePatch(clz=SpikeSlime_M.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(SpikeSlime_M __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "FLAME_TACKLE",
                    Intent.ATTACK_DEBUFF
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            move1.add_effect(new DebuffCardDiscardEffect(Slimed.NAME, 1));
            moves.add(move1);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "FRAIL_LICK",
                    Intent.DEBUFF,
                    __instance.MOVES[0]
            );
            move4.add_effect(new DebuffFrailEffect(1));
            moves.add(move4);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
