package nl.jovmit.kotlin

import android.view.View
import android.widget.TextView
import nl.jovmit.adapter.Person
import nl.jovmit.adapter.R
import nl.jovmit.gendapter.annotations.RecyclerAdapter
import kotlin.LazyThreadSafetyMode.NONE

@RecyclerAdapter(itemType = Person::class)
class SimpleAdapter : SimpleGendapter() {

    override fun layoutResource(): Int = R.layout.recycler_item_view_one

    override fun createViewHolder(view: View): SimpleAdapterViewHolder = ViewHolder(view)

    class ViewHolder(view: View) : SimpleAdapterViewHolder(view) {

        private val firstName: TextView by lazy(NONE) { itemView.findViewById<TextView>(R.id.textViewFirstName) }
        private val lastName: TextView by lazy(NONE) { itemView.findViewById<TextView>(R.id.textViewLastName) }

        override fun bind(item: Person) {
            firstName.text = item.firstName
            lastName.text = item.lastName
        }
    }
}