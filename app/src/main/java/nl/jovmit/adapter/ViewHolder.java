package nl.jovmit.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewHolder extends BaseAdapterViewHolder {

    private final TextView firstName;
    private final TextView lastName;

    ViewHolder(View view) {
        super(view);
        firstName = view.findViewById(R.id.textViewFirstName);
        lastName = view.findViewById(R.id.textViewLastName);
    }

    @Override
    protected void bind(@NonNull RecyclerItem item) {
        Person person = (Person) item;
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context(), "Clicked Item", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
