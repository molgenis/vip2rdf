package org.molgenis.vip2rdf.rdf.enums;

import org.eclipse.rdf4j.model.Triple;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public interface TripleEnum {
    Triple[] getTriples();

    static Set<Triple> getAllTriples(Class<? extends TripleEnum> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("enumClass must be an Enum");
        }
        return Arrays.stream(enumClass.getEnumConstants()).map(TripleEnum::getTriples)
                .flatMap(Arrays::stream).collect(Collectors.toSet());
    }
}
