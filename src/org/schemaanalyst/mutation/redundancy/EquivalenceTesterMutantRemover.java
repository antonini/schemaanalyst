/*
 */
package org.schemaanalyst.mutation.redundancy;

/**
 * A parent class for a {@link RedundantMutantRemover} that uses an equivalence 
 * tester to determine which mutants to remove.
 * 
 * @author Chris J. Wright
 */
public abstract class EquivalenceTesterMutantRemover<T> extends RedundantMutantRemover<T> {

    EquivalenceChecker<T> checker;

    public EquivalenceTesterMutantRemover(EquivalenceChecker<T> checker) {
        this.checker = checker;
    }
}
