package nl.jovmit.gendapter.processor.data;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

import nl.jovmit.gendapter.annotations.RecyclerAdapter;

public class AdapterAnnotatedClass {

    private String adapterItemType;
    private String annotatedClassName;

    public AdapterAnnotatedClass(TypeElement typeElement) {
        annotatedClassName = typeElement.getQualifiedName().toString();
        RecyclerAdapter annotation = typeElement.getAnnotation(RecyclerAdapter.class);
        try {
            Class<?> clazz = annotation.itemType();
            adapterItemType = clazz.getCanonicalName();
        } catch (MirroredTypeException e) {
            DeclaredType classTypeMirror = (DeclaredType) e.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            adapterItemType = classTypeElement.getQualifiedName().toString();
        }
    }

    public String getAdapterItemType() {
        return adapterItemType;
    }

    public String getClassName() {
        return annotatedClassName;
    }
}
