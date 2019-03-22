package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.*;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;

import java.util.ArrayList;

public class SlimeBossPatch {
    public static byte NOT_PREPARED = 0;
    public static byte PREPARED = 1;

    @SpirePatch(clz=SlimeBoss.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {})
    public static class InitMoves {
        public static void Postfix(SlimeBoss __instance) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "SLAM",
                    Intent.ATTACK,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "SLAM_NAME")
            );
            move1.add_phase(PREPARED);
            move1.add_effect(new AttackEffect(__instance.damage.get(1)));
            moves.add(move1);

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "PREP_SLAM",
                    Intent.UNKNOWN,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "PREP_NAME")
            );
            move2.add_phase(NOT_PREPARED);
            move2.add_effect(new UniqueEffect(
                    __instance.getClass(),
                    "UNIQUE_1"
            ));
            moves.add(move2);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "STICKY",
                    Intent.STRONG_DEBUFF,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "STICKY_NAME")
            );
            move4.add_phase(NOT_PREPARED);
            move4.add_phase(PREPARED);
            int slimedAmount = 3;
            if (AbstractDungeon.ascensionLevel >= 19) {
                slimedAmount = 5;
            }
            move4.add_effect(new DebuffCardDiscardEffect(Slimed.NAME, slimedAmount));
            moves.add(move4);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
            try {
                AbstractMonsterPatch.after_action_func.set(__instance,
                        SlimeBossPatch.class.getMethod("afterAction", SlimeBoss.class, IntentData.class));
            }
            catch (NoSuchMethodException exc) { }
        }
    }

    public static void afterAction(SlimeBoss monster, IntentData data) {
        if (data.equals(monster.getClass(), "PREP_SLAM")) {
            AbstractMonsterPatch.current_phase.set(monster, PREPARED);
        }
        if (data.equals(monster.getClass(), "SLAM")) {
            AbstractMonsterPatch.current_phase.set(monster, NOT_PREPARED);
        }
    }
}
