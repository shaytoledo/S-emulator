package semulator;

import semulator.logic.execution.ProgramExecutorImpl;
import semulator.logic.instruction.*;
import semulator.logic.label.LabelImpl;
import semulator.logic.program.SProgram;
import semulator.logic.program.SProgramImpl;
import semulator.logic.variable.Variable;
import semulator.logic.variable.VariableImpl;
import semulator.logic.variable.VariableType;

public class Main {

    public static void main(String[] args) {

        sanity();
    }

    private static void sanity() {
        /*

        {y = x1}

        [L1] x1 ← x1 – 1
             y ← y + 1
             IF x1 != 0 GOTO L1
        * */

        Variable x1 = new VariableImpl(VariableType.INPUT, 1);
        LabelImpl l1 = new LabelImpl(1);

        SProgram p = new SProgramImpl("SANITY");
        p.addInstruction(new DecreaseInstruction(x1, l1));
        p.addInstruction(new IncreaseInstruction(Variable.RESULT));
        p.addInstruction(new JumpNotZeroInstruction(x1, l1));
        p.addInstruction(new ZeroVariableInstruction(Variable.RESULT));

        long result = new ProgramExecutorImpl(p).run(4L, 7L, 8L);
        System.out.println(result);
    }
}
