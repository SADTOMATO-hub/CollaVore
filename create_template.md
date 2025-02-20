# 📋テンプレート生成

### <a href="https://youtu.be/v9N_6WpRXqg">テンプレート生成デモ映像</a>

## ✅ 注目ポイント

 1. Naver Smart Editor 2.0API
    - 使用目的 : 電子決裁のテンプレートとなるHTML5タグをデータベースに格納する為
 2. Thymeleafのutextを活用してHTML5コードをレンダリング
    - 使用目的 : th:textではHTMLタグが文字列として出力される問題を解決し、HTMLタグを解釈してレンダリングするために使用

## 💡 Naver smart editor 2.0とは？
  ```
Naver Smart Editor 2.0はJavaScriptで実装されたウェブベースのWYSIWYGエディターです。
フォント、フォントサイズ、行間などを自由に設定でき、単語の検索・置換などの便利な機能を提供します。
  ```
#### 何故Naver smart editor 2.0を使用したのか？
1. **無料 API** <br> Naver smart editor 2.0は株式会社NAVERから提供しているOPEN APIです。開発予算が節約できるため、本APIを選定しました。
2. **ユーザーが使いやすい** <br> 韓国人の多くは、過去から現在に至るまで、"NAVER"というポータルサイトを利用してきました。該当サイトでは、ブログ投稿やメール作成の際に本APIが活用されており、ユーザーにとって馴染み深いことを考慮し、本APIを選定しました。 

## 🔨 テンプレート生成の流れ

### 1. API配置
![smartEditor](https://github.com/leewoosang-hub/CollaVore/blob/master/images/static.png)  
  
- staticフォルダにNaver smart editor 2.0 apiを配置します。

### 2. API適用

```
<script type="text/javascript" src="/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>

// SmartEditor参照のための配列
	var oEditors = [];
	function smartEditor() {
		console.log("Naver SmartEditor");
		nhn.husky.EZCreator.createInIFrame({
			oAppRef : oEditors,
			elPlaceHolder : "content",
			sSkinURI : "/smarteditor/SmartEditor2Skin.html",
			fCreator : "createSEditor2"
		});
	}
```

- このようなコードを入力する事でIDがCONTENTである全てのTEXTAREAタグが変更されます。

### 3. textarea変更

#### 変更前のTEXTAREA

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/before_textarea.png"/>

#### 変更後のtextarea

<p>
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/after_textarea.png" />
</p>

### 4. HTML5コード入力

<img src="https://github.com/user-attachments/assets/c17a1989-9023-4ba3-bc7b-a89d0263a3b2" />

- HTML5コードを入力する事でテーブルが実装されます。

### 5. テンプレート登録

<p>
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/table_DB.png" style:align="center" />
</p>

- 登録ボタンを押す事でHTML5コードをデータベースに格納します。

### 6. utextを利用してHTML5タグのテーブルレンダリング

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/read_template.png">

- HTML5コードを文字列ではなくテーブルとして実装し、画面に出力

---

### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
