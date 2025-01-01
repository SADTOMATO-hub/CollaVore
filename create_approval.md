# 🖋電子決裁起案
- タイトルを決定します。
- 承認者は最大4名まで選択可能です。
- 電子決裁に使用するテンプレートを選択し、内容を入力します。
	
## 承認者選択
1. 選択ボタンをクリックすると、モーダルウィンドウが表示され、部署を選択できます。
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/selecting_approver.PNG">
  
2. 部署を選択すると非同期処理が実行されます。

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

3. 選択した部署に所属している社員が表示されます。
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/approver.PNG">

4. 承認者選択ボタンをクリックすると、イベントを発生させたボタンの直下にあるtr, tdに選択した承認者の情報が反映されます。
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/selected_approvers.PNG">

- 各ボタンにdata-set属性を設定することで、イベントを発生させたボタンを識別します。
 ```
<thead>
  <tr>
    <td></td>
	<-first->
     <td style="text-align: center;">
       <button type="button" class="btn btn-warning modalBtn"
        data-seq="1" data-bs-target="#approversSelect"
        data-bs-toggle="modal">선택</button>
     </td>
	<-second->			
     <td style="text-align: center;">
       <button type="button" class="btn btn-warning modalBtn"
        data-seq="2" data-bs-target="#approversSelect"
        data-bs-toggle="modal">선택</button>
     </td>
	<-third->		
     <td style="text-align: center;">
       <button type="button" class="btn btn-warning modalBtn"
        data-seq="3" data-bs-target="#approversSelect"
        data-bs-toggle="modal">선택</button>
     </td>
	<-fourth->
     <td style="text-align: center;">
       <button type="button" class="btn btn-warning modalBtn"
        data-seq="4" data-bs-target="#approversSelect"
        data-bs-toggle="modal">선택</button>
     </td
   </tr>
</thead>
 ```

## テンプレート選択
5. ドロップダウンから起案する電子決裁に対応するテンプレートを選択して、内容を入力します。
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/template.PNG">

## 起案
6. バリデーションを通過し、データが正常に格納されます。

```
// 起案ボタンを押すとフォームのデータをすべて送信します。
function submitPost(event) {
    event.preventDefault(); // フォーム送信を防止
    
    // drafterEmpNoの値をuserEmpNoに設定
    var drafterEmpNo = $('input[name=drafterEmpNo]').val(userEmpNo);

    // approvalTitleの検証
    var approvalTitle = $('#approvalTitle').val();
    if (!approvalTitle) {
        Toast.fire({
            icon: "warning",
            title: "タイトルを入力してください。"
        });
        $('#approvalTitle').focus();
        return;
    }

    // approvers[].empNo配列の値を検証
    var approvers = $('input[name^="approvers["][name$=".empNo"]').map(function() { return $(this).val(); }).get();
    if (approvers.length === 0 || approvers.every(val => !val)) {
        Toast.fire({
            icon: "warning",
            title: "承認者を最低1人以上指定してください。"
        });
        return;
    }
    // contentの検証
    oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", []); // contentを更新
    var content = $('#content').val();
    if (content == null || content.trim() == '' || content == '&nbsp;' || content == '<br>' || content == '<p>&nbsp;</p>') {
        Toast.fire({
            icon: "warning",
            title: "テンプレートを設定してください。"
        });
        oEditors.getById["content"].exec("FOCUS");
        return;
    }

    // 全ての検証を通過した場合、フォームを送信
    insertForm.submit(); // フォームを送信
  }	
}
```
7. 電子決裁が正常に起案されると、進行中の電子決裁項目に移動します。
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/approval_list.PNG">
  - ログインセッションの社員が作成した起案書および申請された起案書を全て閲覧できます。
  - ログインセッションの社員が作成した起案書のうち、電子決裁が進行していない文書にはボタンが表示され、削除することができます。

----
### トップページに戻る
	
