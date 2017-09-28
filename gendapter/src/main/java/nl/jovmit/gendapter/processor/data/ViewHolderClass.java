package nl.jovmit.gendapter.processor.data;

import android.support.annotation.NonNull;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static nl.jovmit.gendapter.processor.tools.Tools.colorRes;
import static nl.jovmit.gendapter.processor.tools.Tools.context;
import static nl.jovmit.gendapter.processor.tools.Tools.contextCompat;
import static nl.jovmit.gendapter.processor.tools.Tools.nonNull;
import static nl.jovmit.gendapter.processor.tools.Tools.recyclerViewHolder;
import static nl.jovmit.gendapter.processor.tools.Tools.stringRes;
import static nl.jovmit.gendapter.processor.tools.Tools.toClassName;
import static nl.jovmit.gendapter.processor.tools.Tools.view;

class ViewHolderClass {

    private final Elements elementUtils;
    private final Filer filer;
    private final TypeElement className;
    private final TypeElement itemType;
    private final String suffix;

    ViewHolderClass(Elements elementUtils, Filer filer, TypeElement className, TypeElement itemType, String suffix) {
        this.elementUtils = elementUtils;
        this.filer = filer;
        this.className = className;
        this.itemType = itemType;
        this.suffix = suffix;
    }

    void generate() throws IOException {
        String holderClassName = className.getSimpleName().toString() + suffix;
        TypeSpec holderClass = buildHolderClass(holderClassName, itemType);
        JavaFile javaFile = JavaFile.builder(elementUtils.getPackageOf(className).toString(), holderClass).build();
        javaFile.writeTo(filer);
    }

    private TypeSpec buildHolderClass(String holderClassName, TypeElement superClassElement) {
        return TypeSpec.classBuilder(holderClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(recyclerViewHolder())
                .addField(context(), "context", Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(constructor())
                .addMethod(bindMethod(superClassElement))
                .addMethod(getContextMethod())
                .addMethod(getStringMethod())
                .addMethod(getStringWithArgumentsMethod())
                .addMethod(getColorMethod())
                .build();
    }

    @NonNull
    private MethodSpec constructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(view(), "view")
                .addStatement("super(view)")
                .addStatement("this.$N = view.getContext()", "context")
                .build();
    }

    @NonNull
    private MethodSpec bindMethod(TypeElement superClassElement) {
        return MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                .returns(void.class)
                .addParameter(itemParameter(superClassElement))
                .build();
    }

    @NonNull
    private ParameterSpec itemParameter(TypeElement superClassElement) {
        return ParameterSpec.builder(toClassName(superClassElement), "item")
                .addAnnotation(nonNull()).build();
    }

    private MethodSpec getContextMethod() {
        return MethodSpec.methodBuilder("context")
                .addModifiers(Modifier.PROTECTED)
                .returns(context())
                .addStatement("return context")
                .build();
    }

    private MethodSpec getStringMethod() {
        return MethodSpec.methodBuilder("string")
                .addModifiers(Modifier.PROTECTED)
                .returns(String.class)
                .addParameter(stringResourceParameter())
                .addStatement("return context.getString(resourceId)")
                .build();
    }

    private MethodSpec getStringWithArgumentsMethod() {
        return MethodSpec.methodBuilder("string")
                .addModifiers(Modifier.PROTECTED)
                .returns(String.class)
                .addParameter(stringResourceParameter())
                .addParameter(objectVarArgsParameter())
                .varargs()
                .addStatement("return context.getString(resourceId, args)")
                .build();
    }

    private MethodSpec getColorMethod() {
        return MethodSpec.methodBuilder("color")
                .addModifiers(Modifier.PROTECTED)
                .returns(int.class)
                .addParameter(colorResourceParameter())
                .addStatement("return $T.getColor(context, colorResource)", contextCompat())
                .build();
    }

    @NonNull
    private ParameterSpec objectVarArgsParameter() {
        return ParameterSpec.builder(ArrayTypeName.get(Object[].class), "args").build();
    }

    @NonNull
    private ParameterSpec colorResourceParameter() {
        return ParameterSpec.builder(int.class, "colorResource")
                .addAnnotation(colorRes()).build();
    }

    @NonNull
    private ParameterSpec stringResourceParameter() {
        return ParameterSpec.builder(int.class, "resourceId")
                .addAnnotation(stringRes()).build();
    }
}
