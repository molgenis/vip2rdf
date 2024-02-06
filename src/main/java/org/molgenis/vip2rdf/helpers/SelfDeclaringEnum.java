package org.molgenis.vip2rdf.helpers;

import java.util.Arrays;

public interface SelfDeclaringEnum<T>  {
    /**
     *
     * @param enumClass the specified {@link Enum} {@link Class} to search for matching {@link Enum}{@code s} in
     * @param name the {@link String} to match with
     * @throws java.util.NoSuchElementException if {@code name} could not be found
     * @return the {@code enumClass} related to {@code name} (if found)
     */
    public static <T extends SelfDeclaringEnum> T getEnumValue(Class<T> enumClass, String name) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(value -> name.equals(value.getName())).findFirst().get();
    }

    /**
     * The name of this {@link Enum}. This can either be equal to {@link Enum#name} or different depending on the
     * actual characters in the original name the {@link Enum} represents (f.e. if a {@code -} is in the original name,
     * which cannot be part of the {@link Enum#name}.
     * @return
     */
    String getName();
}