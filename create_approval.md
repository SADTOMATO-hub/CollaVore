# 🖋電子決裁起案

### <a href="https://youtu.be/dpTXWomP3uY">電子決裁起案デモ映像</a>

## ✅ 注目ポイント

1. 클릭 이벤트, 체인지 이벤트와 연계하여 AJAX의 비동기 처리로 데이터를 취득 
   - 사용목적 : 이벤트를 발생 시킬 때 마다, 화면이 깜빡이는 것을 방지하여 UX를 향상

2. data-set 속성을 활용하여 이벤트 핸들러 공유
   - 사용목적 : 단일 이벤트 핸들러를 통해 코드 재사용성과 유지보수성 향상

3. 배열 타입을 이용하여, 여러 데이터를 데이터베이스에 격납
   - 사용목적 : 한 번의 CRUD 처리를 통해 최대 4명의 정보를 동시에 데이터베이스에 격납하여 트랜직셔널 부하 감소 (좀 더 쉬운 말로 바꿔보자)
   - 활용기술 : MyBatis의 foreach를 활용하여 배열 데이터를 반복 처리
               Oracle의 시퀀스와 함수를 결합하여 PK를 자동생성하여 데이터 무결성 보장
      
## 承認者選択

<img src="https://github.com/user-attachments/assets/08ae6b57-e6af-4cca-aef5-cda73922f5f2">

- モーダルウィンドウを通じてドロップダウンから部署を選択するとクリックイベントが実行され、非同期処理で部署に所属している社員の情報を取得

#### コード詳細
```
 // 部署選択時、該当部署に所属する承認者リストを動的に取得するAJAX
$('#selectDept').on('change', function() {
    var deptNo = $(this).val(); // 選択された部署番号

    // 部署番号がない場合は処理を終了
    if (!deptNo) return;

    // 承認者選択ドロップダウンを初期化
    $('#selectApprovers').empty();
    $('#selectApprovers').append('<option selected value="" disabled>==承認者を選択==</option>');

    // AJAXリクエスト: 該当部署の承認者リストを取得
    $.ajax({
        url: '/approvals/selectEmps/' + deptNo,
        type: 'POST',
        success: function(response) {
            if (response.length === 0) {
                $('#selectApprovers').append('<option value="">承認者がいません</option>');
                return;
            }
            // 取得した承認者リストをドロップダウンに追加
            $.each(response, function(index, employeesInfo) {
                $('#selectApprovers').append(
                    `<option value="${employeesInfo.empNo}">${employeesInfo.empName} / ${employeesInfo.posiName}</option>`
                );
            });
        },
        error: function(xhr, status, error) {
			Toast.fire({
			  icon: "error",
			  title: "承認者情報の取得中にエラーが発生しました。"
	 });
       }
    });
}); 
```
<img src="https://github.com/user-attachments/assets/7d386123-dd86-42c5-afc8-6d48cc9fbd01" />

- 4つのボタンが同一なイベントハンドラーを通じてモーダルウィンドウ有効化し、まちまちのtrに承認者の情報を反映

#### コード詳細

> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L180">選択ボタンにdata-set属性設定</a> <br>
>
> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L266">data-set属性の値を入力するためモーダルウィンドウにinputのタグを宣言</a> <br>
>
> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L359">選択ボタンをクリックすると、イベントが実行される</a> <br>
>
> <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/templates/approvals/createApprovalForm.html#L423">モーダルウィンドウの承認者選択ボタンをクリックすると、イベントを実行</a> <br>

上記の流れで4つの選択ボタンが1つのモーダルウィンドウ、モーダルウィンドウのイベントハンドラーを共有することが出来ます。
  
## テンプレート選択
<img src="https://github.com/user-attachments/assets/bb543124-0e11-4a55-bbd1-03b273ccca0a">

- change eventとAJAXを連携して非同期処理でテンプレートのデータを取得します。

#### コード詳細
```
$(document).ready(function () {
    smartEditor();
    // テンプレートのデータ取得
    $("#selectContent").on("change", function (event) {
      var selectedVal = $(this).val();
      var eatNo = $("#eatNo").val(selectedVal);
      $.ajax({
        url: `/approvals/temp?eatNo=` + selectedVal,
        type: "GET",
        success: function (response) {
          oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
          oEditors.getById["content"].exec("SET_IR", [response.content]);
          $("#content").val(response.content);
        },
        error: function (xhr, status, error) {
          Toast.fire({
            icon: "error",
            title: "データ取得中エラーが発生しました。: " + error,
          });
        },
      });
    });
    // ボタンをクリックすると、データ提出
    $("#createButton").on("click", function (event) {
      submitPost(event);
    });
 });
```

## 配列でデータを格納

----
### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
	
