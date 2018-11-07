package io.github.marmer.testutils.generators.beanmatcher;

import io.github.marmer.testutils.generators.beanmatcher.generation.HasPropertyMatcherClassGenerator;
import io.github.marmer.testutils.generators.beanmatcher.generation.MatcherGenerationException;
import io.github.marmer.testutils.generators.beanmatcher.processing.IllegalClassFilter;
import io.github.marmer.testutils.generators.beanmatcher.processing.PotentialPojoClassFinder;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * The Class MatcherFileGenerator.
 *
 * @author marmer
 * @since  20.06.2017
 */
public class MatcherFileGenerator implements MatcherGenerator {
    private final PotentialPojoClassFinder potentialPojoClassFinder;
    private final HasPropertyMatcherClassGenerator hasPropertyMatcherClassGenerator;

    private final IllegalClassFilter illegalClassFilter;

    public MatcherFileGenerator(final PotentialPojoClassFinder potentialPojoClassFinder,
                                final HasPropertyMatcherClassGenerator hasPropertyMatcherClassGenerator,
                                final IllegalClassFilter illegalClassFilter) {
        this.potentialPojoClassFinder = potentialPojoClassFinder;
        this.hasPropertyMatcherClassGenerator = hasPropertyMatcherClassGenerator;
        this.illegalClassFilter = illegalClassFilter;
    }

    @Override
    public List<Path> generateHelperForClassesAllIn(
            final String... packageOrQualifiedClassNames) {
        final List<Class<?>> potentialPojoClasses = illegalClassFilter.filter(
                potentialPojoClassFinder.findClasses(packageOrQualifiedClassNames));
        return generateMatchersFor(potentialPojoClasses);
    }

    private List<Path> generateMatchersFor(final List<Class<?>> potentialPojoClasses) {
        return potentialPojoClasses.parallelStream()
                .map(aClass -> {
                    try {
                        return hasPropertyMatcherClassGenerator.generateMatcherFor(aClass);
                    } catch (MatcherGenerationException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
