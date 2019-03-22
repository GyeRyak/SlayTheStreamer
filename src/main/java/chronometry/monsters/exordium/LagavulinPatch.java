package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffDexterityEffect;
import chronometry.effects.DebuffStrengthEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;

import java.util.ArrayList;

public class LagavulinPatch {
    public static byte SLEEP = 0;
    public static byte REAL_SHIT = 1;

    @SpirePatch(clz=Lagavulin.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {boolean.class})
    public static class InitMoves {

        public static void Postfix(Lagavulin __instance, boolean setAsleep) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move1 = new IntentData(
                    __instance.getClass(),
                    "DEBUFF",
                    Intent.STRONG_DEBUFF,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "DEBUFF_NAME"),
                    1
            );
            move1.add_phase(REAL_SHIT);
            int debuff = (Integer)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "debuff");
            move1.add_effect(new DebuffStrengthEffect(debuff));
            move1.add_effect(new DebuffDexterityEffect(debuff));
            moves.add(move1);

            IntentData move3 = new IntentData(
                    __instance.getClass(),
                    "STRONG_ATK",
                    Intent.ATTACK
            );
            move3.add_phase(REAL_SHIT);
            move3.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move3);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
            try {
                AbstractMonsterPatch.turn_start_func.set(__instance,
                        LagavulinPatch.class.getMethod("turnStart", Lagavulin.class));
            }
            catch (NoSuchMethodException exc) { }
        }
    }

    public static void turnStart(Lagavulin monster) {
        boolean isOut = (boolean)ReflectionHacks.getPrivate(monster, monster.getClass(), "isOut");
        if (isOut) {
            AbstractMonsterPatch.current_phase.set(monster, REAL_SHIT);
        }
    }
}
