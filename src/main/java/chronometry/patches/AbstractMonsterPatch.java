package chronometry.patches;

import chronometry.IntentData;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.lang.reflect.Method;
import java.util.ArrayList;


@SpirePatch(clz=AbstractMonster.class,
            method=SpirePatch.CLASS)
public class AbstractMonsterPatch {
    public static SpireField<ArrayList<IntentData>> intent_moves = new SpireField<>(() -> null);
    public static SpireField<Byte> current_phase = new SpireField<>(() -> (byte)0);
    public static SpireField<Method> turn_start_func = new SpireField<>(() -> null);
    public static SpireField<Method> after_action_func = new SpireField<>(() -> null);
    public static SpireField<Boolean> is_player = new SpireField<>(() -> false);
    public static SpireField<Boolean> had_turn = new SpireField<>(() -> false);
}
