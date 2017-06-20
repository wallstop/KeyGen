package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import models.Validator;
import models.ValidatorNode;

public class Main
{
    private static final int NUM_TEST_RUNS = 100;
    private static final int NUM_ARBITRARY_SEQUENCES_PER_RUN = 100000;
    private static final int NUM_KEYS_GENERATED = 1000000;

    public static void main(final String [] args)
    {
        final int sequenceLength = 32;
        final int validatorsPerElement = 2;

        final List<List<ValidatorNode>> validatorChain = new ArrayList<>(sequenceLength);
        for(int i = 0; i < sequenceLength; ++i)
        {
            final List<ValidatorNode> validatorNodes = new ArrayList<>(validatorsPerElement);
            for(int j = 0; j < validatorsPerElement; ++j)
            {
                final ValidatorNode node = ValidatorNodeGenerator.generate();
                validatorNodes.add(node);
            }
            validatorChain.add(validatorNodes);
        }
        final Validator validator = new Validator(validatorChain);

        final HashSet<String> generatedKeys = new HashSet<>(NUM_KEYS_GENERATED);
        for(int i = 0; i < NUM_KEYS_GENERATED; ++i)
        {
            final String generatedKey = validator.generateSequence();
            generatedKeys.add(generatedKey);
        }
        System.out.printf("Generated %d unique keys out of %d tries (%.10f%% failure rate.\n",
                generatedKeys.size(), NUM_KEYS_GENERATED,
                (NUM_KEYS_GENERATED - generatedKeys.size()) / (NUM_KEYS_GENERATED * 1.0) * 100);
        generatedKeys.clear();
        // Make sure this thing actually works
        for(int i = 0; i < NUM_TEST_RUNS; ++i)
        {
            final String sequence = validator.generateSequence();
            assertTrue(sequence.length() == sequenceLength,
                    "Validator did not generate a sequence of correct length");
            final boolean isValid = validator.isValid(sequence);
            assertTrue(isValid, "Validator didn't validate a sequence it generated");
        }

        // Then see how bad it is against arbitrary input
        int numRuns = 0;
        while(true)
        {
            ++numRuns;
            int validGuesses = 0;
            for(int j = 0; j < NUM_ARBITRARY_SEQUENCES_PER_RUN; ++j)
            {
                final String generatedSequence = SequenceGenerator
                        .generateArbitrarySequence(sequenceLength);
                assertTrue(generatedSequence.length() == sequenceLength,
                        "SequenceGenerator did not generate a sequence of correct length");
                if(validator.isValid(generatedSequence))
                {
                    ++validGuesses;
                }
            }
            if(validGuesses != 0)
            {
                System.out.printf("It took %d runs to get a valid guess", numRuns);
                return;
            }
            System.out.printf("%d valid guesses out of %d tries (%.2f%% success rate)\n",
                    validGuesses, NUM_ARBITRARY_SEQUENCES_PER_RUN,
                    validGuesses * 100.0 / NUM_ARBITRARY_SEQUENCES_PER_RUN);
        }
    }

    private static void assertTrue(final boolean truthy, final String message)
    {
        if(!truthy)
        {
            throw new IllegalStateException(message);
        }
    }
}
