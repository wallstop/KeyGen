package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;

import models.Operation;
import models.ValidatorNode;

public class ValidatorNodeGenerator
{
    public static ValidatorNode generate(final int minLength, final int maxLength)
    {
        final int length = ThreadLocalRandom.current().nextInt(minLength, maxLength);
        final ArrayList<DoubleUnaryOperator> operations = new ArrayList<>(length);
        final List<DoubleFunction<DoubleUnaryOperator>> possibleOperations = Operation.OPERATIONS;
        for(int i = 0; i < length; ++i)
        {
            final int chosenIndex = ThreadLocalRandom.current().nextInt(possibleOperations.size());
            final DoubleFunction<DoubleUnaryOperator> chosenOperation = possibleOperations
                    .get(chosenIndex);
            final int constant = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE,
                    Integer.MAX_VALUE);
            final DoubleUnaryOperator operation = chosenOperation.apply(constant);
            operations.add(operation);
        }
        return new ValidatorNode(operations);
    }

    public static ValidatorNode generate()
    {
        final int min = ThreadLocalRandom.current().nextInt(1, 10);
        final int lengthDifference = ThreadLocalRandom.current().nextInt(10, 50);
        return generate(min, min + lengthDifference);
    }
}
