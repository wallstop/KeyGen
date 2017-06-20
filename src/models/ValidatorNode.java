package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

/**
 * The sweet, sweet character mutator
 *
 */
public final class ValidatorNode implements CharUnaryOperator, Serializable
{
    public static final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final List<Character> VALID_OUTPUT;

    static
    {
        final List<Character> output = new ArrayList<>(VALID_CHARACTERS.length());
        for(final char character : VALID_CHARACTERS.toCharArray())
        {
            output.add(character);
        }
        VALID_OUTPUT = Collections.unmodifiableList(output);
    }

    private static final long serialVersionUID = -891077009772958964L;

    private final List<DoubleUnaryOperator> _operations;

    public ValidatorNode(final List<DoubleUnaryOperator> operations)
    {
        _operations = operations;
    }

    @Override
    public char applyAsChar(final char value)
    {
        double out = value;
        for(final DoubleUnaryOperator mutatorFunction : _operations)
        {
            out = mutatorFunction.applyAsDouble(out);
        }
        final int rounded = (int) Math.round(out);
        final int finalIndex = ((rounded % VALID_OUTPUT.size()) + VALID_OUTPUT.size())
                % VALID_OUTPUT.size();
        return VALID_OUTPUT.get(finalIndex);
    }
}
