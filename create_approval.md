# 🖋電子決裁起案

### <a href="https://youtu.be/dpTXWomP3uY">電子決裁起案デモ映像</a>

## ✅ 注目ポイント

1. 클릭 이벤트, 체인지 이벤트와 연계하여 AJAX의 비동기 처리로 데이터를 취득 
   - 사용목적 : 이벤트를 발생 시킬 때 마다, 화면이 깜빡이는 것을 방지하여 UX를 향상

2. data-set 속성을 활용하여 이벤트 핸들러 공유
   - 사용목적 : 단일 이벤트 핸들러를 4개의 요소가 공유하여 코드 재사용성과 유지보수성 향상

3. 배열 타입을 이용하여, 여러 데이터를 데이터베이스에 격납
   - 사용목적 : 최대 4명의 복수 데이터를 안전하게 격납하기 위해 사용
   - 활용기술 : MyBatis의 foreach를 활용하여 배열 데이터를 반복 처리하여 격납
      
## 承認者選択

<img src="https://github.com/user-attachments/assets/08ae6b57-e6af-4cca-aef5-cda73922f5f2">

- モーダルウィンドウを通じてドロップダウンから部署を選択するとクリックイベントが実行され、非同期処理で部署に所属している社員の情報を取得

#### コード詳細

> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L238">AJAXで取得</a> <br>

<img src="https://github.com/user-attachments/assets/7d386123-dd86-42c5-afc8-6d48cc9fbd01" />

- 4つのボタンが同一なイベントハンドラーを通じてモーダルウィンドウ有効化し、まちまちのtrに承認者の情報を反映

#### コード詳細

> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L42">選択ボタンにdata-set属性設定</a> <br>
>
> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L130">data-set属性の値を入力するためモーダルウィンドウにinputのタグを宣言</a> <br>
>
> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L208">選択ボタンをクリックすると、イベントが実行される</a> <br>
>
> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L274">モーダルウィンドウの承認者選択ボタンをクリックすると、イベントを実行</a> <br>

上記の流れで4つの選択ボタンが1つのモーダルウィンドウ、モーダルウィンドウのイベントハンドラーを共有することが出来ます。
  
## テンプレート選択
<img src="https://github.com/user-attachments/assets/bb543124-0e11-4a55-bbd1-03b273ccca0a">

- change eventとAJAXを連携して非同期処理でテンプレートのデータを取得します。

#### コード詳細

> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L180">AJAXで取得</a> <br>

## 配列でデータを格納




----
### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
	
