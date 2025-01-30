# 📋テンプレート生成

### <a href="https://youtu.be/v9N_6WpRXqg">テンプレート生成デモ映像</a>

## ✅ 注目ポイント

 1. Naver smart editor 2.0를 활용하여 HTML5 코드를 데이터베이스에 저장
    - 사용목적 : 전자결재 템플릿을 DB에 저장하고, 저장된 HTML을 동적으로 렌더링하여 활용(수정필요) 
      
 2. Thymeleaf utext 활용하여 HTML5 코드 렌더링
    - 사용목적 : th:text를 사용할 경우 HTML 태그가 문자열로 출력되는 문제를 해결하고, HTML을 실제 요소로 해석하여 렌더링하기 위해 사용

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

<img src="https://private-user-images.githubusercontent.com/175101488/407699177-c17a1989-9023-4ba3-bc7b-a89d0263a3b2.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzgyNDMwNTksIm5iZiI6MTczODI0Mjc1OSwicGF0aCI6Ii8xNzUxMDE0ODgvNDA3Njk5MTc3LWMxN2ExOTg5LTkwMjMtNGJhMy1iYzdiLWE4OWQwMjYzYTNiMi5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwMTMwJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDEzMFQxMzEyMzlaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0yYTExM2RiZGEyNjdmY2NiMjcxNGMxYjlkMzcxN2I4MjAxMjEzNmNiMzVmOGRkNGE2NGZjZDFmNTYwYTRhYzk1JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.YGw-C7SNBO4nZmtgipFl1aoXI4ay1TAk0O1GaHWBSiw" />

- HTML5コードを入力する事でテーブルが実装されます。

### 5. テンプレート登録

<p>
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/table_DB.png" style:align="center" />
</p>

- 登録ボタンを押す事でHTML5コードをデータベースに格納します。

---

### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
