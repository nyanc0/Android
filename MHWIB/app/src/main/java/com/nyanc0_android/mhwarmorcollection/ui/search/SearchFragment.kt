package com.nyanc0_android.mhwarmorcollection.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nyanc0_android.mhwarmorcollection.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_search_monster.setOnClickListener {
            findNavController().navigate(R.id.action_to_monster_list)
        }

        btn_search_armor.setOnClickListener {
            findNavController().navigate(R.id.action_to_armor_name_input)
        }
    }
}