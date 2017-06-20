package models;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * The basic concept behind the Validator is to act as a kind of two-way weak
 * encryption agent.
 *
 * The Validator can produce some number of valid output strings. This will be S
 * * (N ^ K), where
 *
 * S is the size of your alphabet (26 + 10 + 26 ish)
 *
 * N is the number of "branches" at each possible element (the size of the inner
 * lists)
 *
 * K is the length of the sequence (input and output)
 *
 * This Validator operates on some pretty stupid "arbitrary" logic (aka
 * ValidatorNodes), which, given a character, mutates it into some other
 * character (probably, might be the same as the input). Based on this, it can,
 * with 100% certainty, validate a sequence that it has created (or could
 * create).
 *
 * Thus, the larger N is, the larger the amount of sequences that can be
 * generated by a validator for a given K and N, and the larger the number of
 * inputs that it will validate (as true)
 *
 * So, for optimal "dickish-ness", you want to keep your N small. I think a
 * branching factor of 2 here works pretty well.
 *
 * TODO: Currently, what I don't have implemented, is a clean way to enumerate
 * all possible generated values. That should be relatively trivial (for all
 * input characters for all validator combinations do the thing)
 */
public class Validator implements Serializable
{
    private static final long serialVersionUID = 8861015263140839301L;

    private final List<List<ValidatorNode>> _validatorChain;

    public Validator(final List<List<ValidatorNode>> validatorChain)
    {
        if(validatorChain == null)
        {
            throw new IllegalArgumentException("Cannot deal with a null ValidatorChain");
        }
        for(final List<ValidatorNode> validatorNodes : validatorChain)
        {
            if(validatorNodes == null)
            {
                throw new IllegalArgumentException(
                        "Cannot deal with a ValidatorChain that has null");
            }
        }
        _validatorChain = validatorChain;
    }

    public String generateSequence()
    {
        return generateSequence(ThreadLocalRandom.current()::nextInt,
                (index, maxValidators) -> ThreadLocalRandom.current().nextInt(maxValidators));
    }

    public String generateSequence(final IntUnaryOperator initialCharacterSelect,
            final IntBinaryOperator validatorSelect)
    {
        final int beginningIndex = initialCharacterSelect
                .applyAsInt(ValidatorNode.VALID_CHARACTERS.length());
        char currentChar = ValidatorNode.VALID_CHARACTERS.charAt(beginningIndex);
        final StringBuilder outputBuilder = new StringBuilder();
        for(int i = 0; i < _validatorChain.size(); ++i)
        {
            final List<ValidatorNode> validators = _validatorChain.get(i);
            final int chosenValidatorIndex = validatorSelect.applyAsInt(i, validators.size());
            final ValidatorNode validator = validators.get(chosenValidatorIndex);
            final char output = validator.applyAsChar(currentChar);
            outputBuilder.append(currentChar);
            currentChar = output;
            // We don't actually care about the last character
        }
        return outputBuilder.toString();
    }

    public boolean isValid(final String sequence)
    {
        if(sequence.length() != _validatorChain.size())
        {
            return false;
        }

        validateSequence: for(int i = 0; i < sequence.length() - 1; ++i)
        {
            final char input = sequence.charAt(i);
            final char expectedOutput = sequence.charAt(i + 1);
            for(final ValidatorNode validator : _validatorChain.get(i))
            {
                final char output = validator.applyAsChar(input);
                if(output == expectedOutput)
                {
                    continue validateSequence;
                }
            }
            return false;
        }

        return true;
    }
}
