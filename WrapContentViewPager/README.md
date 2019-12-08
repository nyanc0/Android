# AdjustableWrapContentViewPager

`wrap_content`に対応しつつ、  
ページのコンテンツの高さごとに`ViewPager`自体の高さを調整する`ViewPager`。  
実験的に作成したのでバグがあるかもしれません。

**注意1**  
各ぺージに表示する`View`の高さを保持するため、  
`offscreenPageLimit`に表示するページ数を指定することを前提としています。  
そのため、ページ枚数が極端に多い場合や扱うデータが大きい場合は、  
`OutOfMemory`を引き起こす可能性があるため注意してください。

![](./image/sample.gif)

## 使い方
### 1. レイアウトファイルに追加
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <!-- add to xml -->
    <com.sample.nyanc0_android.adjustablewrapcontentviewpager.AdjustableWrapContentViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:background="@color/colorPrimary"
        android:gravity="center"
        android:text="AnotherView"
        android:textColor="@color/colorAccent"
        android:textSize="20sp"
        android:textStyle="bold" />

</LinearLayout>
```

### 表示するページ数を指定
```kt
val data = create()
val pagerAdapter = WrapContentViewPagerAdapter(data)
val pager = findViewById<ViewPager>(R.id.pager) as AdjustableWrapContentViewPager
// set page counts
// `pager.offscreenPageLimit = counts` is ok.
pager.setPageLimit(data.size)
pager.adapter = pagerAdapter
```
