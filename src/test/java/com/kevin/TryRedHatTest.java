package com.kevin;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;


import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class TryRedHatTest {

    @Inject
    TryRedHat tryRedHat;

    @Test
    void testTryRedHatAppServices() {
        assertEquals("Success", tryRedHat.tryRedHatAppServices("CloudNative").toString());
    }
}