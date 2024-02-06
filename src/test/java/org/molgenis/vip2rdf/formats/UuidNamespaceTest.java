package org.molgenis.vip2rdf.formats;

import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vip2rdf.formats.UuidNamespace;

import java.util.UUID;

class UuidNamespaceTest {
    @Test
    void createUuidNamespace() {
        UuidNamespace actual = new UuidNamespace("");
        String expectedName = String.format("urn:uuid:%s#", actual.getUuid().toString());
        Assertions.assertEquals(expectedName, actual.getName());
    }

    @Test
    void createUuidNamespaceWithCustomValidUuid() {
        UUID uuid = UUID.randomUUID();
        Namespace actual = new UuidNamespace("", uuid);
        String expectedName = String.format("urn:uuid:%s#", uuid);
        Assertions.assertEquals(expectedName, actual.getName());
    }

    @Test
    void createUuidNamespaceWithCustomValidUuidString() {
        UUID uuid = UUID.randomUUID();
        Namespace actual = new UuidNamespace("", uuid.toString());
        String expectedName = String.format("urn:uuid:%s#", uuid);
        Assertions.assertEquals(expectedName, actual.getName());
    }

    @Test
    void createUuidNamespaceWithCustomInvalidUuidString() {
        UUID uuid = UUID.randomUUID();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new UuidNamespace("", "invalidUuidString")
        );
    }

    @Test
    void fromNamespaceValid() {
        Namespace expected = new UuidNamespace("", "cb173486-ccdd-4713-9dc1-2803ac63af9d");
        Namespace actual = UuidNamespace.fromNamespace(new SimpleNamespace("", "urn:uuid:cb173486-ccdd-4713-9dc1-2803ac63af9d#"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void fromNamespaceInvalid() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> UuidNamespace.fromNamespace(new SimpleNamespace("", "https://example.com/"))
        );
    }
}