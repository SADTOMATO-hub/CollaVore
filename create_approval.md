# 🖋電子決裁起案
- タイトルを決定します。
- 承認者は最大4名まで選択できますす。
- 電子決裁に使用するテンプレートを選択し、内容を入力します。

#### デモ映像

- <a href="https://youtu.be/dpTXWomP3uY">電子決裁起案デモ映像</a>

	
## 承認者選択
1. 選択ボタンをクリックすると、モーダルウィンドウが表示され、部署を選択できます。
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/selecting_approver.png">


2. 部署を選択すると、AJAXにより非同期処理が実行されます。

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
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/approver.png">

4. 承認者選択ボタンをクリックすると、イベントを発生させたボタンの直下にあるテーブルに選択した承認者の情報が反映されます。
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/selected_approvers.png">
  
````
<input type="hidden" name="approvers[0].empNo" value="">
<input type="hidden" name="approvers[1].empNo" value="">
<input type="hidden" name="approvers[2].empNo" value="">
<input type="hidden" name="approvers[3].empNo" value="">

<input type="hidden" name="approvers[0].sort" value="1">
<input type="hidden" name="approvers[1].sort" value="2">
<input type="hidden" name="approvers[2].sort" value="3">
<input type="hidden" name="approvers[3].sort" value="4">
````
- テーブルに入力された承認者の情報は使用者が確認するためで、実際の情報はこれらのhiddenタイプのinputタグに入力されます。
  


## テンプレート選択
5. ドロップダウンから起案する電子決裁に対応するテンプレートを選択して、内容を入力します。
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/template.png">

----
### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
	
