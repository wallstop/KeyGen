package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;

public final class Operation
{
    private static final int NUM_OPERATIONS = 4;

    public static final List<DoubleFunction<DoubleUnaryOperator>> OPERATIONS;

    static
    {
        final List<DoubleFunction<DoubleUnaryOperator>> operations = new ArrayList<>(
                NUM_OPERATIONS);
        operations.add(Operation::add);
        operations.add(Operation::subtract);
        operations.add(Operation::multiply);
        operations.add(Operation::divide);
        OPERATIONS = Collections.unmodifiableList(operations);
    }

    public static DoubleUnaryOperator add(final double operand)
    {
        return lhs -> lhs + operand;
    }

    public static DoubleUnaryOperator subtract(final double operand)
    {
        return lhs -> lhs - operand;
    }

    public static DoubleUnaryOperator multiply(final double operand)
    {
        return lhs -> lhs * operand;
    }

    public static DoubleUnaryOperator divide(final double operand)
    {
        return lhs ->
        {
            if(operand == 0)
            {
                return 0;
            }
            return lhs / operand;
        };
    }
}
