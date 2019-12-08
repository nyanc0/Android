package com.sample.nyanc0_Android.coroutinesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main4()
    }

    fun loadData() {
        // 引数に関数を渡してコールバックとして受け取る
        fetch("id") { result ->
            showData(result)
        }
    }

    fun fetch(id: String, success: (String) -> Unit) {

        // ~ 時間のかかる処理 ~

        // 処理が終わったのでコールバック
        success.invoke("result")
    }

    fun showData(result: String) {
        // UIへの表示
    }

    fun main() {
        GlobalScope.launch {
            println("launch ThreadName = ${Thread.currentThread().name}")
            for (count in 1..9) {
                println("count = $count")
            }
            println("count finish!")
        }

        println("out ThreadName = ${Thread.currentThread().name}")
        for (count in 1..20) {
            println("out count = $count")
        }
    }

    fun main2() {
        GlobalScope.launch {
            println("launch ThreadName = ${Thread.currentThread().name}")
            for (count in 1..9) {
                println("count = $count")
            }
            println("count finish!")
        }

        runBlocking {
            println("out ThreadName = ${Thread.currentThread().name}")
            for (count in 1..20) {
                println("out count = $count")
            }
        }
    }

    fun main3() = runBlocking {
        println("start ThreadName = ${Thread.currentThread().name}")
        GlobalScope.launch {
            println("launch ThreadName = ${Thread.currentThread().name}")
            for (count in 1..9) {
                println("count = $count")
            }
            println("count finish!")
        }

        println("out ThreadName = ${Thread.currentThread().name}")
        for (count in 1..20) {
            println("out count = $count")
        }
    }

    fun main4() = runBlocking {
        // this: CoroutineScope

        launch {
            println("Task from runBlocking")
            println("ThreadName2 = ${Thread.currentThread().name}")
        }

        coroutineScope {
            // Creates a coroutine scope
            launch {
                println("Task from nested launch")
                println("ThreadName3 = ${Thread.currentThread().name}")
            }

            println("Task from coroutine scope") // This line will be printed before the nested launch
            println("ThreadName4 = ${Thread.currentThread().name}")
        }

        println("Coroutine scope is over") // This line is not printed until the nested launch completes
        println("ThreadName = ${Thread.currentThread().name}")
    }


}
