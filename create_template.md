# 📋テンプレート生成

### Naver smart editor 2.0

#### Naver smart editor 2.0とは？
  ```
  Naver smart editor 2.0는 JavaScript로 구현된 웹 기반의 WYSIWYG 편집기이다.
  글꼴, 글자 크기, 줄 간격 등을 자유롭게 설정할 수 있으며, 단어 찾기/바꾸기와 같은 편리한 기능을 제공한다.
  ```
  出典 : Naver smart editor 2.0の<a href="https://naver.github.io/smarteditor2/user_guide/1_intro/">取り扱い説明書</a> 

#### 何故Naver smart editor 2.0を使用したのか？
1. 무료 API : 
2. ユーザーに使いやすい : 한국인들의 대부분은 과거부터 현재에 이르기까지 "네이버"라는 포탈 사이트를 이용합니다. 블로그, 게시판 등에 투고할 때, 이 Naver smart editor 2.0를 사용해 투고해 왔기 때문에 사용자에게 가장 익숙하기 때문에 선정했습니다.

### テンプレート生成の流れ

## API配置
![smartEditor](https://github.com/leewoosang-hub/CollaVore/blob/master/images/static.png)  
  
- staticフォルダにNaver smart editor 2.0 apiを配置します。

## API適用

```
// SmartEditor 참조를 위한 배열
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




## TEXTAREA変更
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/textarea_normal.PNG"/>

- 変更前

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/smarteditorENG.PNG" />

- 変更後


## HTML5コード入力
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/html-code.png" width="780" height="450"/>

- HTML5コードを入力する事でテーブルが実装されます。
  

|HTML5の通りテーブルが実装されました。このテーブルが電子決裁のテンプレートになります。|
|-----------------|
|<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/Leave%20Application.PNG" width="780" height="450"/>|


## テンプレート登録
|登録ボタンをクリックする事でテンプレートをデータベースに登録します。|
|---|
|<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/insert_temp.PNG" width="780" height="450"/>|

---

### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
