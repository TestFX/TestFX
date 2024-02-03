package org.testfx;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class Repeat implements TestRule {

    private final int count;

    public Repeat(int count) {
        this.count = count;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {

            @Override
            @SuppressWarnings("synthetic-access")
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;
                int failuresCount = 0;
                for (int i = 0; i < count; i++) {
                    try {
                        base.evaluate();
                    } catch (Throwable t) {
                        caughtThrowable = t;
                        System.err.println(description.getDisplayName()
                                + ": run " + (i + 1) + " failed:");
                        t.printStackTrace();
                        ++failuresCount;
                    }
                }
                if (caughtThrowable == null) return;
                throw new AssertionError(description.getDisplayName()
                        + ": failures " + failuresCount + " out of "
                        + count + " tries. See last throwable as the cause.", caughtThrowable);
            }
        };
    }
}
