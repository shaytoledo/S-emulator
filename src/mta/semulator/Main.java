package mta.semulator;

import mta.semulator.logic.execution.ProgramExecutor;
import mta.semulator.logic.execution.ProgramExecutorImpl;
import mta.semulator.logic.instruction.SInstruction;
import mta.semulator.logic.instruction.DecreaseInstruction;
import mta.semulator.logic.instruction.IncreaseInstruction;
import mta.semulator.logic.instruction.JumpNotZeroInstruction;
import mta.semulator.logic.instruction.NoOpInstruction;
import mta.semulator.logic.label.LabelImpl;
import mta.semulator.logic.program.SProgram;
import mta.semulator.logic.program.SProgramImpl;
import mta.semulator.logic.variable.Variable;
import mta.semulator.logic.variable.VariableImpl;
import mta.semulator.logic.variable.VariableType;

public class Main {

    public static void main(String[] args) {

        Variable x1 = new VariableImpl(VariableType.INPUT, 1);
        //Variable z1 = new VariableImpl(VariableType.WORK, 1);

        LabelImpl l1 = new LabelImpl(1);
        LabelImpl l2 = new LabelImpl(2);

        SInstruction noop = new NoOpInstruction(Variable.RESULT);
        SInstruction increase = new IncreaseInstruction(x1, l1);
        // Jump to label l2 if x1 is not zero (instruction label is empty)
        SInstruction jnz = new JumpNotZeroInstruction(x1, l2);
        SInstruction decrease = new DecreaseInstruction(x1, l2);

        SProgram p = new SProgramImpl("test");
        p.addInstruction(noop);
        p.addInstruction(increase);
        p.addInstruction(jnz);
        p.addInstruction(decrease);

        ProgramExecutor programExecutor = new ProgramExecutorImpl(p);
        long result = programExecutor.run(3L, 7L, 6L);
        System.out.println(result);


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

        long result = new ProgramExecutorImpl(p).run(4L, 7L, 8L);
        System.out.println(result);
    }
}
