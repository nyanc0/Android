# Coroutineとは
- 中断可能な計算インスタンス
- スレッドと同様にコードブロック・ライフサイクルを持つが、特定のスレッドには束縛されない
- FutureやPromiseのように何らかの結果や例外を返す場合がある
- 継続状況を持つプログラムが容易に記述できる

### 中断可能な計算インスタンス

<img src="https://github.com/nyanc0/nyanc0_Android/blob/master/Coroutine/image/coroutine2.png?raw=true">

サブルーチン
```kt
fun loadData() {
    val result = fetch("id")
    showData(result)
}

fun fetch(id: String): String {
    // 時間のかかる処理
    return "result"
}

fun showData(result: String) {
    // UIへの表示
}
```

コルーチン
```kt
fun loadData() {
    launch {
        // この結果が帰ってくるまで処理は中断される
        val result = async{ fetch("id") }.await()
        showData(result)
    }
}

fun fetch(id: String): String {
    // 時間のかかる処理
    return "result"
}

fun showData(result: String) {
    // UIへの表示
}
```

※ 簡略化のためにスコープ等々は省略している

### 特定のスレッドには束縛されない

```kt
Thread {
  // {}内の処理はThreadの中で行われる
  val result = fetch("id")
  showData(result)
}.start()

// コルーチン
fun loadData() {
    launch {
        // {}内の処理自体はどこのスレッドで実行されるかを気にしない
        val result = async{ fetch("id") }.await()
        showData(result)
    }
}
```

### FutureやPromiseのように何らかの結果や例外を返す場合がある

```kt
fun loadData() {
    launch {
        // async = 値を返すコルーチン
        // await = 別のコルーチンを起動して値が帰ってくるまで中断する
        val result = async{ fetch("id") }.await()
        showData(result)
    }
}
```

### 継続状況を持つプログラムが容易に記述できる
```kt
fun loadData() {
    // 引数に関数を渡してコールバックとして受け取る.
    // 関数の中の処理がどこのスレッドで行われたかは知らなくても良い
    fetch("id") { result ->
        showData(result)
    }
}

fun fetch(id: String, success: (String) -> Unit) {

    // ~ 時間のかかる処理 ~

    // 処理が終わったのでコールバック
    success.invoke("result")
}
```

これを

```kt
fun loadData() {
    launch {
        // async(コルーチン)の処理がどこのスレッドで行われたかは知らなくても良い
        // asyncの処理が完了したら、再開
        val result = async{ fetch("id") }.await()
        showData(result)
    }
}
```

こう書ける。  
つまり、コルーチンが非同期処理を簡単に書けると言われる理由は、

どこのスレッドで実行されたかを意識せず(= UIスレッドだろうが、ワーカースレッドだろうが関係なく)、  
コールバックを簡単に書ける(= 継続を裏でよしなに書いてくれる)から。

# Coroutineがどう動くのかを理解する
## おさらい
Coroutineは、
- 中断可能な計算インスタンス
- スレッドと同様にコードブロック・ライフサイクルを持つが、特定のスレッドには束縛されない
- FutureやPromiseのように何らかの結果や例外を返す場合がある
- 継続状況を持つプログラムが容易に記述できる

簡単にいうと、
```
どこのスレッドで実行されたかを意識せず(= UIスレッドだろうが、ワーカースレッドだろうが関係なく)、  
コールバックを簡単に書ける(= 継続を裏でよしなに書いてくれる)。
```

## suspend関数
suspend関数とは、
- `suspend修飾子`をつけた関数のこと
- `suspend修飾子`をつけると、つけた関数が継続渡しに変換される
- `suspend関数`内で継続オブジェクトを取り出して任意に再開できる
- `suspend関数`は`suspend関数`の中からしか呼び出せない

## 継続渡しとは
「継続」とは、トップレベル(スタート)に戻ってくるまでにしなければならない計算。  

```
// 2 * 3が「継続」
1+ 2 * 3
```

「継続渡し」とは、関数が値を返す代わりに、計算した値をどの関数に渡すのかを明示的に指定する方法。

## Coroutineの登場人物
|クラス|役割|
|---|---|
|[CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)|Coroutineを実行するための情報が入っている|
|[CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html)|Coroutineが実行される(有効な)期間を定義する|
|[CoroutineDispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-dispatcher/index.html)([Dispatchers](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/index.html))|処理を実行するスレッドを決定する|
|[Job](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-job/index.html)|実行する一連の処理単位。CoroutineもJob。|
|CoroutineBuilder|Coroutineを作成する。CoroutineScopeの拡張関数。|

<img src="https://github.com/nyanc0/nyanc0_Android/blob/master/Coroutine/image/coroutine1.png?raw=true">



# 参考
- http://sys1yagi.hatenablog.com/entry/2018/08/28/000620
- http://www.shido.info/lisp/scheme_cc.html
