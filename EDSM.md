# 📝決裁
- ログイン中のセッションの決裁順番に基づいて、決裁ボタンが表示されます。
- 承認者の中で1名以上が承認した場合、電子決裁の状態が「進行中」になります。
- 承認者の中で1名でも「否認」した場合、電子決裁の状態が「否認」になります。
- 全ての承認者が「承認」した場合、電子決裁の状態が「承認」になります。
- 

## プロシージャを通じて承認を進行。
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/accepctOrDenine.PNG">

- ログイン中のセッションが含まれる電子決裁の詳細ページで、自分の承認順番の場合、「待機」イメージの代わりに承認・否認ボタンが表示されます。
  
- 承認ボタンをクリックすると、AJAXを通じてControllerにデータ（承認者の社員番号、承認可否）を送信します。
  
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

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure1.PNG">
 
- パラメーターで承認者の社員番号、承認可否を受信します。
  
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure3.PNG">

- 決裁が進行する際に、電子決裁の状態を更新するために必要な変数を宣言しました。
  
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure2.PNG">

- パラメーターとして渡された承認可否の値を基に電子決裁の状態を更新します。

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure4.PNG">

- 更新される電子決裁の番号を照会します。

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure6-2.PNG">

- 承認者の承認可否は「b1/待機」、「b2/承認」、「b3/否認」の3つです。

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure6.PNG">

- 電子決裁の状態は「a1/承認待機」、「a2/進行中」、「a3/承認終了」、「a4/否認終了」の4つです。

 > 

<br>

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/approval_process.PNG">

- プロシージャを通過して承認者の承認可否が「承認」に更新され、それに応じたイメージが表示されます。
- 承認者の決裁が進行するにつれて、電子決裁の状態が更新されることが確認できます。

## 先決

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/not_yet.PNG">

- 現在ログイン中のセッションの前の順番の決裁が進行されていないため、承認ボタンが表示されず、「待機」イメージが表示されました。

```
// 電子決裁詳細ページ
@GetMapping("/readApprInfo")
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
