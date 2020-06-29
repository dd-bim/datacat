package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Translation;
import de.bentrm.datacat.graphql.dto.DtoMapper;
import de.bentrm.datacat.graphql.dto.TextInput;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceUtil {

    @NotNull
    static Runnable throwEntityNotFoundException(String id) {
        return () -> {
            throw new IllegalArgumentException("No Object with id " + id + " found.");
        };
    }

    static void mapTextInputToTranslationSet(Set<Translation> persistentTranslations, List<TextInput> textInput) {
        DtoMapper dtoMapper = DtoMapper.INSTANCE;
        final Iterator<Translation> namesIter = persistentTranslations.iterator();
        while (namesIter.hasNext()) {
            final Translation name = namesIter.next();

            textInput.stream()
                    .filter(input -> input.getId().equals(name.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            input -> {
                                name.setLanguageCode(input.getLanguageCode());
                                name.setValue(input.getValue());
                            },
                            namesIter::remove
                    );

        }

        List<String> ids = persistentTranslations.stream()
                .map(Translation::getId)
                .collect(Collectors.toList());
        for (TextInput input : textInput) {
            if (input.getId() == null || !ids.contains(input.getId())) {
                final Translation newName = dtoMapper.toTranslation(input);
                persistentTranslations.add(newName);
            }
        }
    }

}
