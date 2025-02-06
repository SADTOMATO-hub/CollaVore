# 📝決裁・先決

### <a href="https://youtu.be/6CGdhDFVPsE">電子決裁決裁デモ映像</a>
### <a href="https://youtu.be/3FdpodYiA90">先決デモ映像</a>

## 注目ポイント
 1. 클릭 이벤트와 연계하여 비동기 처리로 승인자의 승인 여부를 즉시 반영
    - 사용목적 : 화면의 깜빡이나 새로고침 없이 유저의 요청을 즉각적으로 화면에 반영함으로써 UX 향상

 2. 프로시저를 사용하여 결재상태 변경
    - 사용목적 :
    - 

 3. 선결기능

## プロシージャを通じて承認を進行

버튼을 눌러 결재 하는 gif 따기

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

 <br>

 <!-- 프로시저 상세 -->
 > プロシージャ詳細

<br>

데이터베이스에 결재자 상태가 갱신된 이미지

## 先決

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/not_yet.png">

- 現在ログイン中のセッションの前の順番の決裁が進行されていないため、承認ボタンが表示されず、「待機」イメージが表示されました。

```
// 電子決裁詳細ページ
@GetMapping("/read-appr-info")
public String readapprinfo(Model model, ApprovalsVO apprVO, HttpSession session) {
    int userEmpNo = (Integer) session.getAttribute("userEmpNo");
    apprVO.setUserEmpNo(userEmpNo);
    
    // 決裁および承認者情報の取得
    ApprovalsVO approvals = approvalsService.approvalsInfo(apprVO);
    List<Map<String, Object>> approvers = approvalsService.approversInfo(apprVO);
    String documentStatus = approvals.getApprovalStatus(); // 文書ステータスを取得

    // ボタン有効化の判定およびステータス表示設定
    for (int i = 0; i < approvers.size(); i++) {
        Map<String, Object> approver = approvers.get(i);
        
        // ステータスの初期化
        String approverStatus = (String) approver.get("approverStatus");
        String displayStatus;

        // 現在の承認者の基本ステータスを設定
        if ("b2".equals(approverStatus)) {
            displayStatus = "承認";
        } else if ("b3".equals(approverStatus)) {
            displayStatus = "否認";
        } else {
            displayStatus = "承認待ち"; // デフォルト値の設定
        }

        // ボタン有効化の判定
        boolean buttonEnabled = false; // デフォルトでは非有効化
        boolean previousApprovedOrRejected = false;

        // 決裁文書のステータスがa3またはa4の場合はボタンを非有効化
        if ("a3".equals(documentStatus) || "a4".equals(documentStatus)) {
            buttonEnabled = false;
            previousApprovedOrRejected = false;
        } else {
            if (i == 0) {
                // 最初の承認者はapproverStatusがb1の場合のみボタンを有効化
                buttonEnabled = "b1".equals(approverStatus) && userEmpNo == ((Number) approver.get("approverEmpNo")).intValue();
            } else {
                // 2人目以降の承認者は、前の承認者全員が承認(b3)または否認(b2)である場合のみボタンを有効化
                previousApprovedOrRejected = true;
                for (int j = 0; j < i; j++) {
                    String previousStatus = (String) approvers.get(j).get("approverStatus");
                    if (!"b2".equals(previousStatus) && !"b3".equals(previousStatus)) {
                        previousApprovedOrRejected = false;
                        break;
                    }
                }
                buttonEnabled = previousApprovedOrRejected && "b1".equals(approverStatus) && userEmpNo == ((Number) approver.get("approverEmpNo")).intValue();
            }
        }

        // ボタンが有効化されている場合、「承認待ち」ステータスの表示を非表示
        if (buttonEnabled && "承認待ち".equals(displayStatus)) {
            displayStatus = ""; // ボタンが有効化された場合、ステータスを空文字列に設定
        }

        approver.put("buttonEnabled", buttonEnabled);
        approver.put("displayStatus", displayStatus);
    }

    // モデルに決裁情報を追加
    model.addAttribute("approvals", approvals);
    model.addAttribute("approvers", approvers);
    return "approvals/readApproval";
}
```

----
### <a href="https://github.com/leewoosang-hub/CollaVore">トップページに戻る
