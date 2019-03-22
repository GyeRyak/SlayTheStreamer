package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.DebuffVulnerableEffect;
import chronometry.effects.DebuffWeakEffect;
import chronometry.effects.DefendEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;

import java.util.ArrayList;

public class TheGuardianPatch {
    public static byte OPEN = 0;
    public static byte CLOSE = 1;

    @SpirePatch(clz=TheGuardian.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {})
    public static class InitMoves {
        public static void Postfix(TheGuardian __instance) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "FIERCE_BASH",
                    Intent.ATTACK,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "FIERCEBASH_NAME")
            );
            move2.add_phase(OPEN);
            move2.add_effect(new AttackEffect(__instance.damage.get(0)));
            moves.add(move2);

            IntentData move5 = new IntentData(
                    __instance.getClass(),
                    "WHIRLWIND",
                    Intent.ATTACK,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "WHIRLWIND_NAME")
            );
            move5.add_phase(OPEN);
            move5.add_effect(new AttackEffect(
                    __instance.damage.get(2),
                    (int) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "whirlwindCount")
            ));
            moves.add(move5);

            IntentData move6 = new IntentData(
                    __instance.getClass(),
                    "CHARGE_UP",
                    Intent.DEFEND,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "CHARGEUP_NAME")
            );
            move6.add_phase(OPEN);
            move6.add_effect(new DefendEffect(
                    (int) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "blockAmount")
            ));
            moves.add(move6);

            IntentData move7 = new IntentData(
                    __instance.getClass(),
                    "VENT_STEAM",
                    Intent.STRONG_DEBUFF,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "VENTSTEAM_NAME")
            );
            move7.add_phase(OPEN);
            int turns = (int) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "VENT_DEBUFF");
            move7.add_effect(new DebuffWeakEffect(turns));
            move7.add_effect(new DebuffVulnerableEffect(turns));
            moves.add(move7);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
            try {
                AbstractMonsterPatch.turn_start_func.set(__instance,
                        TheGuardianPatch.class.getMethod("turnStart", TheGuardian.class));
            }
            catch (NoSuchMethodException exc) { }
        }
    }

    public static void turnStart(TheGuardian monster) {
        boolean isOpen = (boolean) ReflectionHacks.getPrivate(monster, monster.getClass(), "isOpen");
        if (isOpen) {
            AbstractMonsterPatch.current_phase.set(monster, OPEN);
        }
        else {
            AbstractMonsterPatch.current_phase.set(monster, CLOSE);
        }
    }

    // 0 (open):    turnStart, open = True
    //              action => ...
    //              но мы его сбиваем
    //              close_up
    // 1 (open):    turnStart, open = False => phase = clos
    //              roll_attack
    // 2 (clos):    turnStart, open = False
    //              twin_slam
    // 3 (clos):    turnStart, open = True => phase = open
}
