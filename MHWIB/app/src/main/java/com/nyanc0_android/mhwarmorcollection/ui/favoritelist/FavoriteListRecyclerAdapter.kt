package com.nyanc0_android.mhwarmorcollection.ui.favoritelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.db.Favorite
import com.nyanc0_android.mhwarmorcollection.infrastructure.db.FavoriteSetEntity
import kotlinx.android.synthetic.main.item_armor.view.*
import kotlinx.android.synthetic.main.item_armor.view.text_parts
import kotlinx.android.synthetic.main.item_set_armor.view.*

class FavoriteListRecyclerAdapter(
    favoriteList: List<Favorite>,
    setList: List<FavoriteSetEntity>,
    private val onClickSingleItem: (armorId: String) -> Unit,
    private val onClickSetItem: (favoriteSet: FavoriteSetEntity) -> Unit
) :
    RecyclerView.Adapter<FavoriteListRecyclerAdapter.VH>() {

    var viewModels = mutableListOf<VM>()

    init {
        viewModels = createVM(favoriteList, setList).toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Type.SINGLE.itemType -> {
                val singleView = inflater.inflate(R.layout.item_armor, parent, false)
                SingleVH(singleView)
            }
            Type.SET.itemType -> {
                val setView = inflater.inflate(R.layout.item_set_armor, parent, false)
                SetVH(setView)
            }
            else -> {
                val singleView = inflater.inflate(R.layout.item_armor, parent, false)
                SingleVH(singleView)
            }
        }
    }

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        when (holder) {
            is SingleVH -> {
                holder.bind(viewModels[position] as SingleVM, this::onClickSingleItem)
            }
            is SetVH -> {
                holder.bind(viewModels[position] as SetVM, this::onClickSetItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = viewModels[position].type.itemType

    fun refresh(favoriteList: List<Favorite>, setList: List<FavoriteSetEntity>) {
        viewModels.clear()
        viewModels.addAll(createVM(favoriteList, setList))
        notifyDataSetChanged()
    }

    private fun createVM(
        favoriteList: List<Favorite>,
        setFavoriteList: List<FavoriteSetEntity>
    ): List<VM> {
        val singleList = favoriteList.map {
            SingleVM(it)
        }

        val setList = setFavoriteList.map {
            SetVM(it)
        }

        return singleList + setList
    }

    private fun onClickSingleItem(armorId: String) {
        onClickSingleItem.invoke(armorId)
    }

    private fun onClickSetItem(favoriteSet: FavoriteSetEntity) {
        onClickSetItem.invoke(favoriteSet)
    }

    abstract class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
    class SingleVH(itemView: View) : VH(itemView) {
        fun bind(singleVM: SingleVM, onClickSingleItem: (armorId: String) -> Unit) {
            itemView.text_parts.text = singleVM.favorite.parts
            itemView.text_armor_name.text = singleVM.favorite.armorName
            itemView.text_monster_name.text = singleVM.favorite.monsterName
            itemView.setOnClickListener {
                onClickSingleItem.invoke(singleVM.favorite.armorId)
            }
        }
    }

    class SetVH(itemView: View) : VH(itemView) {
        fun bind(setVM: SetVM, onClickSetItem: (favoriteSet: FavoriteSetEntity) -> Unit) {
            itemView.text_set_name.text = setVM.favoriteSet.setName
            itemView.setOnClickListener {
                onClickSetItem.invoke(setVM.favoriteSet)
            }
        }
    }

    abstract class VM(val type: Type)
    data class SingleVM(
        val favorite: Favorite
    ) : VM(Type.SINGLE)

    data class SetVM(
        val favoriteSet: FavoriteSetEntity
    ) : VM(Type.SET)

    enum class Type(val itemType: Int) {
        SINGLE(0), SET(1)
    }
}