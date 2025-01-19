# 📋テンプレート生成

- Naver smart editor 2.0にHTML5コードを入力し、テンプレートを生成します。

#### デモ映像

- <a href="https://youtu.be/YwlYMtGN2Go">テンプレート生成ビデオデモ映像</a>

## Naver smart editor 2.0

#### Naver smart editor 2.0とは？
  ```
Naver Smart Editor 2.0はJavaScriptで実装されたウェブベースのWYSIWYGエディターです。
フォント、フォントサイズ、行間などを自由に設定でき、単語の検索・置換などの便利な機能を提供します。
  ```
  *出典 : Naver smart editor 2.0の<a href="https://naver.github.io/smarteditor2/user_guide/1_intro/">取り扱い説明書</a>*

#### 何故Naver smart editor 2.0を使用したのか？
1. **無料 API** <br> Naver smart editor 2.0は株式会社NAVERから提供しているOPEN APIです。開発予算を節約できるため、本APIを選定しました。
2. **ユーザーが使いやすい** <br> 韓国人の多くは、過去から現在に至るまで、"NAVER"というポータルサイトを利用してきました。該当サイトでは、ブログ投稿やメール作成の際に本APIが活用されており、ユーザーにとって馴染み深いことを考慮し、本APIを選定しました。 
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/naver.png">

 ***韓国人のポータルサイト利用率統計***
 
*出典 : <a href="https://www.infocubic.co.jp/blog/archives/6789/">韓国の検索エンジン「NAVER」とは？</a>*


## テンプレート生成の流れ

### 1. API配置
![smartEditor](https://github.com/leewoosang-hub/CollaVore/blob/master/images/static.png)  
  
- staticフォルダにNaver smart editor 2.0 apiを配置します。

### 2. API適用

```
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
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/textarea_normal.PNG"/>

- 変更前のTEXTAREA

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/smarteditorENG.PNG" />

- 変更後のtextarea


### 4. HTML5コード入力
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/html-code.png" width="780" height="450"/>

- HTML5コードを入力する事でテーブルが実装されます。
  
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/Leave%20Application.PNG" width="780" height="450"/>

- HTML5の通りテーブルが実装されました。このテーブルが電子決裁のテンプレートになります。

### 5. テンプレート登録
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/insert_temp.PNG" width="780" height="450"/>

- 登録ボタンをクリックする事でテンプレートをデータベースに登録します。

---

### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
