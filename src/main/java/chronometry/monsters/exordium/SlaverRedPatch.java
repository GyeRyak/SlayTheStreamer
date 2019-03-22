package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffVulnerableEffect;
import chronometry.effects.UniqueEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;

import java.util.ArrayList;

public class SlaverRedPatch {
    @SpirePatch(clz=SlaverRed.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {float.class, float.class})
    public static class InitMoves {
        public static void Postfix(SlaverRed __instance, float x, float y) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "STAB",
                    Intent.ATTACK
            );
            move1.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "ENTANGLE",
                    Intent.ATTACK,
                    (String)ReflectionHacks.getPrivateStatic(__instance.getClass(), "ENTANGLE_NAME"),
                    1
            );
            move2.add_effect(new UniqueEffect(
                    __instance.getClass(),
                    "UNIQUE_1",
                    1
            ));
            moves.add(move2);

            IntentData move3 = new IntentData(
                    __instance.getClass(),
                    "SCRAPE",
                    Intent.ATTACK_DEBUFF,
                    (String)ReflectionHacks.getPrivateStatic(__instance.getClass(), "SCRAPE_NAME")
            );
            int VULN_AMT = (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "VULN_AMT");
            if (AbstractDungeon.ascensionLevel >= 17) {
                VULN_AMT += 1;
            }
            move3.add_effect(new AttackEffect(__instance.damage.get(1)));
            move3.add_effect(new DebuffVulnerableEffect(VULN_AMT));
            moves.add(move3);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
        }
    }
}
