package de.chat2u;

import org.junit.Assert;

public interface ShouldFail {
    void failCode();

    default void test(String failMessage) {
        try {
            failCode();
            Assert.fail(failMessage);
        } catch (Exception ignore) {
            Assert.assertEquals(0, 0);
        }
    }

    default void test() {
        test("should fail");
    }

    default void test(Class<?> t) {
        test("should throw " + t.getSimpleName());
    }
}
