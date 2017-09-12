package nl.jovmit.gendapter.processor.data;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class AdapterGroupedClass {

    private static final String SUFFIX = "ViewHolder";

    private final String annotatedClassName;
    private final String adapterItemType;

    public AdapterGroupedClass(String annotatedClassName, String adapterItemType) {
        this.annotatedClassName = annotatedClassName;
        this.adapterItemType = adapterItemType;
    }

    public void generateCode(Elements elementUtils, Filer filer) throws IOException {
        TypeElement className = elementUtils.getTypeElement(annotatedClassName);
        TypeElement itemType = elementUtils.getTypeElement(adapterItemType);
        new ViewHolderClass(elementUtils, filer, className, itemType, SUFFIX).generate();
        new AdapterClass(elementUtils, filer, className, itemType, SUFFIX).generate();
    }
}
