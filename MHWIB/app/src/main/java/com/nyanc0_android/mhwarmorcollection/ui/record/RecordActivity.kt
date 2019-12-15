package com.nyanc0_android.mhwarmorcollection.ui.record

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.model.Armor
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.ArmorNameRepository
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.FavoriteRepository
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecordActivity : AppCompatActivity() {

    var armors = mutableListOf<Armor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        fetch()
        button_record.setOnClickListener {
            if (edit_set_name.text.isBlank()) {
                Toast.makeText(this, "セット名を入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val favoriteRepository = FavoriteRepository(this)
            favoriteRepository.saveFavoriteSet(edit_set_name.text.toString(), armors)
            Toast.makeText(this, "登録しました", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetch() = GlobalScope.launch(Dispatchers.Main) {
        intent.extras?.getStringArrayList(KEY)?.let {
            val repository = ArmorNameRepository()
            val result = repository.fetchRecord(it)
            armors.addAll(result)
            showList(result)
        }
    }

    private fun showList(list: List<Armor>) {
        if (recycler.adapter == null) {
            recycler.adapter = RecordListRecyclerAdapter(list)
        }
    }

    companion object {
        fun intent(context: Context, list: ArrayList<String>) =
            Intent(context, RecordActivity::class.java).also {
                it.putStringArrayListExtra(KEY, list)
            }

        private const val KEY = "list"
    }
}