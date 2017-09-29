# gendapter

The project is a playground for me learning about annotation processing and generating code. The 
first version should be able to generate simple abstract `RecyclerView.Adapter` class capable to consume 
items, and corresponding `RecyclerView.ViewHolder` class. Both generated classes are abstract to let the user define a layout
and binding implementation for the items. In the later versions I'm planning to add `Header` handling
capabilities as well as possibility to define `DiffUtils`

PRs are welcome, but since I got some ideas on further implementations, please file an issue before
making one so we could discuss.

## Usage

The idea of the project is to make simple recycler adapter generator.
To get started, in the main `build.gradle` file we need to add `jitpack` repository:
 
```groovy

allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

and in the app module's `build.gradle` we need to add the library dependency:
```groovy

dependencies {
    ...
    implementation 'com.github.mitrejcevski:gendapter:1.0.3'
    annotationProcessor 'com.github.mitrejcevski:gendapter:1.0.3'
}
```

Next, we need to define a class with an annotation that looks somewhat like this:
```java

@RecyclerAdapter(itemType = String.class)
public class BaseAdapter {
    
}
```
The `itemType` in the annotation is used to define the type of the data items that are going to be held in the adapter.
Then we need to build the project in order for the classes to get generated. Once that's done, we can simply extend the generated adapter class in the same file:

```java

@RecyclerAdapter(itemType = String.class)
public class BaseAdapter extends BaseGendapter {

    @Override
    protected int layoutResource() {
        return 0;
    }

    @Override
    protected BaseAdapterViewHolder createViewHolder(@NonNull View view) {
        return null;
    }
}
```

Once we extend the generated class `BaseGendapter` we will have to override the methods `layoutResource()` which has to provide the layout of the item in the recycler adapter, and `createViewHolder` method that has to provide an implementation for the view holder. 
This method returns a type `BaseAdapterViewHolder` which is an abstract generated `RecyclerView.ViewHolder` class. It means we need to make a concrete implementation of that abstract class so we will define the actual binding of the data into layout:

```java

public class ViewHolder extends BaseAdapterViewHolder {

    ViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bind(@NonNull String item) {
        
    }
}
```

That's it. So eventually, all we have to do is just annotate, extend the abstract adapter and provide layout and view holder instance, that should extend from the generated view holder class. Here is the full implementation example:

The adapter class:
```java

@RecyclerAdapter(itemType = String.class)
public class BaseAdapter extends BaseGendapter {

    @Override
    protected int layoutResource() {
        return R.layout.base_list_item_layout;
    }

    @Override
    protected BaseAdapterViewHolder createViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }
}
```

The view holder class:
```java

public class ViewHolder extends BaseAdapterViewHolder {
    
    private final TextView title;
    
    ViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.textViewLabel);
    }

    @Override
    protected void bind(@NonNull String item) {
        title.setText(item);
    }
}
```

The use of the adapter:
```java
public class SampleActivity extends AppCompatActivity {
    ...
    
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseAdapter adapter = new BaseAdapter();
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
    }
}
```

### ProGuard
If the project using this library enables proguard, the proguard file should include the following configuration
```proguard
-dontwarn nl.jovmit.gendapter.**
-dontwarn com.squareup.javapoet.**
```