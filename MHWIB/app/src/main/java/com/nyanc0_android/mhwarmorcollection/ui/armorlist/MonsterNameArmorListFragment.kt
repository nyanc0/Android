package com.nyanc0_android.mhwarmorcollection.ui.armorlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Armor
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.MonsterArmorListRepository
import com.nyanc0_android.mhwarmorcollection.ui.detail.ArmorDetailActivity
import kotlinx.android.synthetic.main.fragment_monster_name_armor_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MonsterNameArmorListFragment : Fragment() {

    private var armorList: List<Armor> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monster_name_armor_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetch()
    }

    private fun fetch() = GlobalScope.launch(Dispatchers.Main) {
        if (armorList.isEmpty()) {
            arguments?.getString(KEY)?.let {
                showLoading()
                val repository = MonsterArmorListRepository()
                armorList = repository.fetch(it)
                showList(armorList)
            } ?: showError()
        } else {
            showList(armorList)
        }
    }

    private fun showError() {
        hideLoading()
        layout_error.visibility = View.VISIBLE
    }

    private fun showList(armors: List<Armor>) {

        if (armors.isEmpty()) {
            showError()
            return
        }

        if (recycler.adapter == null) {
            recycler.adapter = ArmorListRecyclerAdapter(armors, this::onClickItem)
            recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        } else {
            val adapter = recycler.adapter as ArmorListRecyclerAdapter
            adapter.refresh(armors)
        }
        hideLoading()
        recycler.visibility = View.VISIBLE
    }

    private fun onClickItem(armor: Armor) {
        startActivity(ArmorDetailActivity.intent(activity!!, armor))
    }

    private fun showLoading() {
        recycler.visibility = View.GONE
        progress.visibility = View.VISIBLE
        layout_error.visibility = View.GONE
    }

    private fun hideLoading() {
        progress.visibility = View.GONE
    }

    companion object {
        fun bundle(monsterName: String) = Bundle().also {
            it.putString(KEY, monsterName)
        }

        private const val KEY = "monsterName"
    }
}