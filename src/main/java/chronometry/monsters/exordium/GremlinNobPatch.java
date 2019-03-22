package chronometry.monsters.exordium;

import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffVulnerableEffect;
import chronometry.effects.UniqueEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;

import java.util.ArrayList;

public class GremlinNobPatch {
    @SpirePatch(clz=GremlinNob.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class, boolean.class})
    public static class InitMoves {
        public static void Postfix(GremlinNob __instance, float x, float y, boolean setVuln) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "BULL_RUSH",
                    Intent.ATTACK
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "SKULL_BASH",
                    Intent.ATTACK_DEBUFF,
                    __instance.MOVES[0]
            );
            move2.add_effect(new AttackEffect(__instance.damage.get(1)));
            move2.add_effect(new DebuffVulnerableEffect(2));
            moves.add(move2);

            IntentData move3 = new IntentData(
                    __instance.getClass(),
                    "BELLOW",
                    Intent.BUFF,
                    -1
            );
            int strength_gain = 2;
            if (AbstractDungeon.ascensionLevel >= 18) {
                strength_gain = 3;
            }
            move3.add_effect(new UniqueEffect(
                    __instance.getClass(),
                    "UNIQUE_1",
                    strength_gain
            ));
            moves.add(move3);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
