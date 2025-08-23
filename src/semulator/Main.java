package semulator;

import semulator.engine.logic.execution.ProgramExecutorImpl;
import semulator.engine.logic.instruction.*;
import semulator.engine.logic.label.LabelImpl;
import semulator.engine.logic.program.Program;
import semulator.engine.logic.program.ProgramImpl;
import semulator.engine.logic.variable.Variable;
import semulator.engine.logic.variable.VariableImpl;
import semulator.engine.logic.variable.VariableType;

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

        Program p = new ProgramImpl("SANITY");
        p.addInstruction(new DecreaseInstruction(x1, l1));
        p.addInstruction(new IncreaseInstruction(Variable.RESULT));
        p.addInstruction(new JumpNotZeroInstruction(x1, l1));
        p.addInstruction(new ZeroVariableInstruction(Variable.RESULT));

        long result = new ProgramExecutorImpl(p).run(4L, 7L, 8L);
        System.out.println(result);
    }
}
