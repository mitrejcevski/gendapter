package nl.jovmit.gendapter.processor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import nl.jovmit.gendapter.annotations.RecyclerAdapter;
import nl.jovmit.gendapter.processor.data.AdapterAnnotatedClass;
import nl.jovmit.gendapter.processor.data.AdapterGroupedClass;

public class Processor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;
    private Map<String, AdapterGroupedClass> adapterClasses = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new LinkedHashSet<>(2);
        result.add(RecyclerAdapter.class.getCanonicalName());
        return result;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        collectAdapterAnnotatedClasses(roundEnvironment);
        triggerAdapterClassesGeneration();
        return false;
    }

    private void collectAdapterAnnotatedClasses(RoundEnvironment roundEnvironment) {
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(RecyclerAdapter.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes should be annotated with @%s", RecyclerAdapter.class.getSimpleName());
            }
            TypeElement typeElement = (TypeElement) annotatedElement;
            AdapterAnnotatedClass annotatedClass = new AdapterAnnotatedClass(typeElement);
            final String annotationName = annotatedClass.getAdapterItemType();
            final String annotatedClassName = annotatedClass.getClassName();
            AdapterGroupedClass adapterClass = adapterClasses.get(annotationName);
            if (adapterClass == null) {
                adapterClass = new AdapterGroupedClass(annotatedClassName, annotationName);
                adapterClasses.put(annotationName, adapterClass);
            }
        }
    }

    private void triggerAdapterClassesGeneration() {
        try {
            generateAdapterClasses();
        } catch (IOException e) {
            error(null, e.getMessage());
        }
    }

    private void generateAdapterClasses() throws IOException {
        for (AdapterGroupedClass current : adapterClasses.values()) {
            current.generateCode(elementUtils, filer);
        }
        adapterClasses.clear();
    }

    private void error(Element element, String message, Object... arguments) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, arguments), element);
    }
}