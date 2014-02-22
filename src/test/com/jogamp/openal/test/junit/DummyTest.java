package com.jogamp.openal.test.junit;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jogamp.openal.test.util.UITestCase;

/**
 * Dummy test - always successful, merely exist to not let our Jenkins build not fail.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DummyTest extends UITestCase {

    @Test
    public void test00() {
        Assert.assertTrue(true);
    }
}
