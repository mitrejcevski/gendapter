package nl.jovmit.gendapter.processor.data;

import android.support.annotation.NonNull;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static nl.jovmit.gendapter.processor.tools.Tools.bestGuess;
import static nl.jovmit.gendapter.processor.tools.Tools.layoutInflater;
import static nl.jovmit.gendapter.processor.tools.Tools.layoutRes;
import static nl.jovmit.gendapter.processor.tools.Tools.list;
import static nl.jovmit.gendapter.processor.tools.Tools.nonNull;
import static nl.jovmit.gendapter.processor.tools.Tools.parameterized;
import static nl.jovmit.gendapter.processor.tools.Tools.recyclerViewAdapter;
import static nl.jovmit.gendapter.processor.tools.Tools.toClassName;
import static nl.jovmit.gendapter.processor.tools.Tools.view;
import static nl.jovmit.gendapter.processor.tools.Tools.viewGroup;

class AdapterClass {

    private final Elements elementUtils;
    private final Filer filer;
    private final TypeElement className;
    private final TypeElement itemType;
    private final String nameSuffix;

    AdapterClass(Elements elementUtils,
                 Filer filer,
                 TypeElement className,
                 TypeElement itemType,
                 String nameSuffix) {
        this.elementUtils = elementUtils;
        this.filer = filer;
        this.className = className;
        this.itemType = itemType;
        this.nameSuffix = nameSuffix;
    }

    void generate() throws IOException {
        String holderClassName = className.getSimpleName().toString() + nameSuffix;
        String adapterClassName = adapterNameFrom(className.getSimpleName().toString());
        TypeSpec adapterClass = buildAdapterClass(adapterClassName, holderClassName, itemType);
        JavaFile javaFile = JavaFile.builder(elementUtils.getPackageOf(className).toString(), adapterClass).build();
        javaFile.writeTo(filer);
    }

    private TypeSpec buildAdapterClass(String adapterClassName, String holderClassName, TypeElement itemType) {
        ClassName viewHolderType = bestGuess(holderClassName);
        return TypeSpec.classBuilder(adapterClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(recyclerViewAdapter(), viewHolderType))
                .addField(itemsMethod(itemType))
                .addMethod(layoutResourceMethod())
                .addMethod(createViewHolderMethod(viewHolderType))
                .addMethod(onCreateViewHolderMethod(viewHolderType))
                .addMethod(onBindViewHolderMethod(viewHolderType))
                .addMethod(getItemCountMethod())
                .addMethod(setItemsMethod(itemType))
                .addMethod(getItemAtPositionMethod(itemType))
                .build();
    }

    private FieldSpec itemsMethod(TypeElement itemType) {
        return FieldSpec.builder(parameterized(list(), toClassName(itemType)), "items", Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T<>(0)", ArrayList.class)
                .build();
    }

    private MethodSpec layoutResourceMethod() {
        return MethodSpec.methodBuilder("layoutResource")
                .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                .addAnnotation(layoutRes())
                .returns(int.class)
                .build();
    }

    private MethodSpec createViewHolderMethod(ClassName viewHolderType) {
        return MethodSpec.methodBuilder("createViewHolder")
                .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                .returns(viewHolderType)
                .addParameter(viewParameter())
                .build();
    }

    @NonNull
    private ParameterSpec viewParameter() {
        return ParameterSpec.builder(view(), "view")
                .addAnnotation(nonNull()).build();
    }

    private MethodSpec onCreateViewHolderMethod(ClassName viewHolderType) {
        return MethodSpec.methodBuilder("onCreateViewHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(viewHolderType)
                .addParameter(viewGroup(), "parent")
                .addParameter(int.class, "viewType")
                .addStatement("$T view = $T.from(parent.getContext()).inflate(layoutResource(), parent, false)", view(), layoutInflater())
                .addStatement("return createViewHolder(view)")
                .build();
    }

    private MethodSpec onBindViewHolderMethod(ClassName viewHolderType) {
        return MethodSpec.methodBuilder("onBindViewHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(viewHolderType, "holder")
                .addParameter(int.class, "position")
                .addStatement("holder.bind(itemAt(position))")
                .build();
    }

    private MethodSpec getItemCountMethod() {
        return MethodSpec.methodBuilder("getItemCount")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addStatement("return items.size()")
                .build();
    }

    private MethodSpec setItemsMethod(TypeElement itemType) {
        return MethodSpec.methodBuilder("setItems")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(parameterized(list(), toClassName(itemType)), "newItems")
                .addStatement("items.clear()")
                .addStatement("items.addAll(newItems)")
                .addStatement("notifyDataSetChanged()")
                .build();
    }

    private MethodSpec getItemAtPositionMethod(TypeElement itemType) {
        return MethodSpec.methodBuilder("itemAt")
                .addModifiers(Modifier.PRIVATE)
                .returns(ClassName.get(itemType))
                .addParameter(int.class, "position")
                .addStatement("return items.get(position)")
                .build();
    }

    private String adapterNameFrom(String input) {
        return input.contains("Adapter") ?
                input.replace("Adapter", "Gendapter") :
                input + "Gendapter";
    }
}
