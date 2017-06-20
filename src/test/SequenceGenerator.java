package test;

import java.util.concurrent.ThreadLocalRandom;

import models.ValidatorNode;

public class SequenceGenerator
{
    public static String generateArbitrarySequence(final int length)
    {
        final StringBuilder outputBuilder = new StringBuilder(length);
        for(int i = 0; i < length; ++i)
        {
            final int chosenIndex = ThreadLocalRandom.current()
                    .nextInt(ValidatorNode.VALID_CHARACTERS.length());
            final char chosenCharacter = ValidatorNode.VALID_CHARACTERS.charAt(chosenIndex);
            outputBuilder.append(chosenCharacter);
        }
        return outputBuilder.toString();
    }
}
