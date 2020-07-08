package ru.skillbranch.skillarticles.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.local.entities.CategoryData
import ru.skillbranch.skillarticles.extensions.dpToIntPx

class CategoriesAdapter(private val listener: (CategoryData, Boolean) -> Unit)
    : ListAdapter<Pair<CategoryData, Boolean>, CategoryVH>(CategoryCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryVH(view, listener)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) = holder.bind(getItem(position))
}

class CategoryVH(
    override val containerView: View,
    private val listener: (CategoryData, Boolean) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val categorySize = containerView.context.dpToIntPx(40)

    fun bind(data: Pair<CategoryData, Boolean>) {
        ch_select.isChecked = data.second
        ch_select.setOnCheckedChangeListener { _, isChecked ->
            listener(data.first, isChecked)
        }

        Glide.with(containerView.context)
            .load(data.first.icon)
            .circleCrop()
            .override(categorySize)
            .into(iv_icon)

        tv_category.text = data.first.title
        tv_count.text = "${data.first.articlesCount}"

        itemView.setOnClickListener {
            ch_select.isChecked = !ch_select.isChecked
            listener(data.first, ch_select.isChecked)
        }
    }
}

class CategoryCallback : DiffUtil.ItemCallback<Pair<CategoryData, Boolean>>() {
    override fun areItemsTheSame(oldItem: Pair<CategoryData, Boolean>, newItem: Pair<CategoryData, Boolean>) =
        oldItem.first.categoryId == newItem.first.categoryId

    override fun areContentsTheSame(oldItem: Pair<CategoryData, Boolean>, newItem: Pair<CategoryData, Boolean>) =
        oldItem == newItem
}