# 📝決裁・先決

### <a href="https://youtu.be/6CGdhDFVPsE">電子決裁決裁デモ映像</a>
### <a href="https://youtu.be/3FdpodYiA90">先決デモ映像</a>

## ✅注目ポイント
1. クリックイベントと連携し、非同期処理で承認者の承認状況を即時反映
   - 使用目的：画面のちらつきやリロードなしでユーザーのリクエストを即座に画面へ反映し、UXを向上させるため。

 2. ストアドプロシージャを使用して承認状態を変更
    - 使用目的 : 複数のクエリを安全に実行し、データの整合性を維持するため。

 3. 先決

## プロシージャを通じて承認を進行

<img src="https://github.com/user-attachments/assets/e8f0911c-4ecc-40ee-af5b-04939a1c365d">

```
  $(".approveBtn").on('click', function(event) {
	const approveOrDenine = $(this).val();
    const earNo = this.getAttribute('data-ear-no');
    const buttonText = $(this).text();
    const statusSpan = $(this).siblings('.display-status');
    $.ajax({
        url: "/approvals/updateAppr",
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify({
            earNo: earNo,
            approverStatus: approveOrDenine,
        }),
   });
}
```

- 承認ボタンをクリックすると、AJAXを通じてControllerにデータ（承認者の社員番号、承認可否）を送信します。

```
<update id="updateApprStatus" statementType="CALLABLE">
	{ CALL
	appr_status_update(#{earNo, mode=IN}, #{approverStatus, mode=IN})
	}
</update>
```

- MyBatisからプロシージャを呼び出します。
 
### <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/resources/ApprovalUpdate.sql#L1">プロシージャ詳細<a> <br>

### プロシージャを利用し更新
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/updatedApproval.png">

- "a1"から"a2"に更新されたデータ

## 先決

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/waiting.png">

- 現在ログイン中のセッション(ポン部長)の前の順番の決裁が進行されていないため、承認ボタンが表示されず、「待機」イメージが表示されました。

### <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/demo/src/main/java/com/collavore/app/approvals/web/ApprovalsController.java#L178">電子決裁詳細ページ</a> 

----
### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
