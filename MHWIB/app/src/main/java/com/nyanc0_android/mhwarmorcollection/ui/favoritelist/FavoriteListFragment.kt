package com.nyanc0_android.mhwarmorcollection.ui.favoritelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.db.Favorite
import com.nyanc0_android.mhwarmorcollection.infrastructure.db.FavoriteSetEntity
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.FavoriteRepository
import com.nyanc0_android.mhwarmorcollection.ui.detail.ArmorDetailActivity
import kotlinx.android.synthetic.main.fragment_favorite_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        fetchFavorite()
    }

    private fun fetchFavorite() {
        GlobalScope.launch(Dispatchers.Main) {
            val favoriteRepository = FavoriteRepository(activity!!)
            val favoriteList = favoriteRepository.getAllFavorites()
            val favoriteSetList = favoriteRepository.getAllFavoriteSet()
            if (favoriteList.isNotEmpty() || favoriteSetList.isNotEmpty()) {
                showList(favoriteList, favoriteSetList)
            } else {
                showNoFavorites()
            }
        }
    }

    private fun showList(favoriteList: List<Favorite>, favoriteSetList: List<FavoriteSetEntity>) {
        layout_no_favorite.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        if (recycler.adapter == null) {
            recycler.adapter = FavoriteListRecyclerAdapter(
                favoriteList,
                favoriteSetList,
                this::onSingleItemClicked,
                this::onSetItemClicked
            )
            recycler.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        } else {
            val adapter = recycler.adapter as FavoriteListRecyclerAdapter
            adapter.refresh(favoriteList, favoriteSetList)
        }
    }

    private fun onSingleItemClicked(armorId: String) {
        startActivity(ArmorDetailActivity.intent(activity!!, armorId))
    }

    private fun onSetItemClicked(favoriteSet: FavoriteSetEntity) {
        // TODO: セット一覧へ遷移
    }

    private fun showNoFavorites() {
        layout_no_favorite.visibility = View.VISIBLE
        recycler.visibility = View.GONE
    }
}