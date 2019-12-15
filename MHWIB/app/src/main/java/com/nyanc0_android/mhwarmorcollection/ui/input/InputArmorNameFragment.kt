package com.nyanc0_android.mhwarmorcollection.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.ui.armorlist.ArmorNameArmorListFragment
import kotlinx.android.synthetic.main.fragment_input_armor_name.*

class InputArmorNameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_armor_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_search.setOnClickListener {
            val inputText = edit_armor_name.text.toString()
            if (inputText.isBlank()) {
                Toast.makeText(context, R.string.message_empty_armor_text, Toast.LENGTH_SHORT)
                    .show()
            } else {
                findNavController().navigate(
                    R.id.action_to_armor_name_armor_list,
                    ArmorNameArmorListFragment.bundle(inputText)
                )
            }
        }
    }
}