package org.molgenis.vip2rdf.formats;

import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;

import java.util.UUID;

public class UuidNamespace extends SimpleNamespace {
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Creates a new Namespace object.
     *
     * @param prefix The namespace's prefix.
     * @param uuid   The namespace's {@link UUID}.
     */
    public UuidNamespace(String prefix, UUID uuid) {
        super(prefix, prepareUuid(uuid.toString()));
        this.uuid = uuid;
    }

    public UuidNamespace(String prefix, String uuid) {
        this(prefix, UUID.fromString(uuid));
    }

    public UuidNamespace(String prefix) {
        this(prefix, UUID.randomUUID());
    }

    private static String prepareUuid(String uuid) {
        return String.format("urn:uuid:%s#", uuid);
    }

    public static UuidNamespace fromNamespace(Namespace namespace) {
        return new UuidNamespace(namespace.getPrefix(), namespace.getName().substring(9, namespace.getName().length()-1));
    }
}
