package com.nyanc0_android.mhwarmorcollection.ui.monsterlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Monster
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.MonsterRepository
import com.nyanc0_android.mhwarmorcollection.ui.armorlist.MonsterNameArmorListFragment
import kotlinx.android.synthetic.main.fragment_monster_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MonsterListFragment : Fragment() {

    private var monsterList: List<Monster> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monster_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetch()
    }

    private fun fetch() = GlobalScope.launch(Dispatchers.Main) {
        if (monsterList.isEmpty()) {
            val repository = MonsterRepository()
            monsterList = repository.fetch()
        }
        showList(monsterList)
    }

    private fun showList(monsters: List<Monster>) {
        if (recycler.adapter == null) {
            recycler.adapter = MonsterListRecyclerAdapter(monsters, this::onItemClick)
            recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        } else {
            val adapter = recycler.adapter as MonsterListRecyclerAdapter
            adapter.refresh(monsters)
        }
    }

    private fun onItemClick(monster: Monster) {
        findNavController().navigate(
            R.id.action_to_monster_name_armor_list,
            MonsterNameArmorListFragment.bundle(monster.name)
        )
    }
}