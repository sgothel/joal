package com.jogamp.openal.test.junit;

import org.junit.Assert;
import org.junit.Test;

/**
 * Dummy test - always successful, merely exist to not let our Jenkins build not fail.
 */
public class DummyTest {
    
    @Test 
    public void test00() {
        Assert.assertTrue(true);
    }
}
