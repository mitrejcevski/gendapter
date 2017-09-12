package nl.jovmit.gendapter.processor.tools;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;

public class Tools {

    public static ClassName stringRes() {
        return className("android.support.annotation", "StringRes");
    }

    public static ClassName colorRes() {
        return className("android.support.annotation", "ColorRes");
    }

    public static ClassName layoutRes() {
        return className("android.support.annotation", "LayoutRes");
    }

    public static ClassName contextCompat() {
        return className("android.support.v4.content", "ContextCompat");
    }

    public static ClassName context() {
        return className("android.content", "Context");
    }

    public static ClassName view() {
        return className("android.view", "View");
    }

    public static ClassName recyclerViewHolder() {
        return className("android.support.v7.widget.RecyclerView", "ViewHolder");
    }

    public static ClassName list() {
        return className("java.util", "List");
    }

    public static ClassName layoutInflater() {
        return className("android.view", "LayoutInflater");
    }

    public static ClassName viewGroup() {
        return className("android.view", "ViewGroup");
    }

    public static ClassName recyclerViewAdapter() {
        return className("android.support.v7.widget.RecyclerView", "Adapter");
    }

    public static ClassName bestGuess(String classNameString) {
        return ClassName.bestGuess(classNameString);
    }

    public static TypeName toClassName(TypeElement typeElement) {
        return TypeName.get(typeElement.asType());
    }

    public static ClassName className(String thePackage, String type) {
        return ClassName.get(thePackage, type);
    }

    public static ParameterizedTypeName parameterized(ClassName rawType, TypeName... arguments) {
        return ParameterizedTypeName.get(rawType, arguments);
    }
}
