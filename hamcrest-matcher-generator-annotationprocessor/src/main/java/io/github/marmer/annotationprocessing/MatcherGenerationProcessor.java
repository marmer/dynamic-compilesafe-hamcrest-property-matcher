package io.github.marmer.annotationprocessing;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.lang.model.element.ElementKind.METHOD;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"io.github.marmer.annotationprocessing.MatcherConfiguration", "io.github.marmer.annotationprocessing.MatcherConfigurations"})
@AutoService(Processor.class)
public class MatcherGenerationProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        // TODO: marmer 24.01.2019 find all types of a package recursively

        // TODO: marmer 24.01.2019 find all methods of a type
        // TODO: marmer 24.01.2019 find all parameter types
        // TODO: marmer 25.01.2019 find a way to execute the annotation processor after lombok (and possibly other processors) have been executed

        if (roundEnv.processingOver()) {
            return true;
        }

        final Class<MatcherConfigurations> annotationType = MatcherConfigurations.class;
        final List<MatcherConfigurations> annotation = getAnnotation(roundEnv, annotationType);

        final TypeElement requestedPackage = processingEnv.getElementUtils().getTypeElement("io.github.marmer.annotationprocessing.samples.SimplePojo");

        final List<? extends Element> packageElements = requestedPackage
                .getEnclosedElements();
        packageElements.stream().forEach(this::print);

        return false;
    }

    private <T extends Annotation> List<T> getAnnotation(final RoundEnvironment roundEnv, final Class<T> annotationType) {
        return roundEnv.getElementsAnnotatedWith(annotationType).stream()
                .map(element -> ((Element) element).getAnnotation(annotationType))
                .collect(Collectors.toList());
    }

    private PackageElement getPackageOf(final Element element) {
        return processingEnv.getElementUtils().getPackageOf(element);
    }

    private void print(final Object value) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "##### -> " + value);
    }

    private List<? extends VariableElement> getMethodParametersOf(final ExecutableElement element) {
        return element.getParameters();
    }

    private TypeMirror getMethodReturnTypeOf(final ExecutableElement element) {
        return element.getReturnType();
    }

    private boolean isMethod(final Element element) {
        return METHOD == element.getKind();
    }

}
