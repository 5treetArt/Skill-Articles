package ru.skillbranch.skillarticles.ui.dialogs

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_categories.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.ui.base.BaseDialogFragment
import ru.skillbranch.skillarticles.viewmodels.articles.ArticlesViewModel

class ChoseCategoryDialog : BaseDialogFragment<ArticlesViewModel>() {
    override val viewModel: ArticlesViewModel by activityViewModels()
    private val selectedCategories = mutableListOf<String>()
    private val args: ChoseCategoryDialogArgs by navArgs()
    override val layout = R.layout.fragment_categories
    //TODO save checked state and implement custom items
    private val categories by lazy {
        args.categories//.toList().map{ "${it.title} ${it.articlesCount}" }.toTypedArray()
    }
    private val checked by lazy {
        BooleanArray(args.categories.size) {
            args.selectedCategories.contains(args.categories[it].categoryId)
        }
    }

    private val categoriesAdapter = CategoriesAdapter { category, isChecked ->
        if (isChecked) selectedCategories.add(category.categoryId)
        else selectedCategories.remove(category.categoryId)
    }

    override val buildDialog: AlertDialog.Builder.() -> AlertDialog.Builder = {
        this.setTitle("Chose category")
            .setPositiveButton("Apply") { _, _ ->
                viewModel.applyCategories(selectedCategories)
            }
            .setNegativeButton("Reset") { _, _ ->
                viewModel.applyCategories(emptyList())
            }
            //.setMultiChoiceItems(categories, checked) { dialog, which, isChecked ->
            //    if (isChecked) selectedCategories.add(args.categories[which].categoryId)
            //    else selectedCategories.remove(args.categories[which].categoryId)
            //}
    }

    override fun setupViews() {
        with(categories_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = categoriesAdapter
        }

        val checkedCategories = categories.mapIndexed { idx, data -> data to checked[idx] }
        categoriesAdapter.submitList(checkedCategories)
    }
}