# 📝決裁
- 로그인 중인 세션에 따라 전자결재 항목이 출력됩니다.  ログイン中のセッション決裁を行います。
- 로그인 중인 세션이 결재를 해야하는 순서이면, 결재 버튼이 표시됩니다.
- 이전 순서의 결재자가 아직 결재를 진행하지 않으면 결재 버튼이 비활성화 됩니다.
- 결재자 중, 1명 이상이라고 '승인'을 하면 전자결재 상태가 '진행 중'이 됩니다
- 결재자 중, 1명이라도 '반려'를 하면 전자결재 상태가 '반려'가 됩니다.
- 모든 결재자가 '승인'하면 잔자결재 상태가 '승인'이 됩니다.

## 결재
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/accepctOrDenine.PNG">

- 로그인 중인 세션이 결재자에 포함된 전자결재 상세화면, 본인의 결재순서라면 '대기' 이미지 대신 승인여부 버튼이 표시됩니다.
  
- 承認ボタンをクリックすると、ajax를 통해 전자결재의 상태를 변경합니다.。
  
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

- mybatis에서 프로시저를, 결재를 요청한 전자결재 번호, 버튼을 통해 수신한 결재상태를 매개변수로 사용합니다.

>>


  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/approved.PNG">
  

  
