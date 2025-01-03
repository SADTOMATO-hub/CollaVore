# 📝決裁
- 로그인 중인 세션에 따라 전자결재 항목이 출력됩니다.  ログイン中のセッション決裁を行います。
- 로그인 중인 세션이 결재를 해야하는 순서이면, 결재 버튼이 표시됩니다.
- 이전 순서의 결재자가 아직 결재를 진행하지 않으면 결재 버튼이 비활성화 됩니다.
- 결재자 중, 1명 이상이라고 '승인'을 하면 전자결재 상태가 '진행 중'이 됩니다
- 결재자 중, 1명이라도 '반려'를 하면 전자결재 상태가 '반려'가 됩니다.
- 모든 결재자가 '승인'하면 잔자결재 상태가 '승인'이 됩니다.

## 프로시저를 통해 전자결재 진행
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/accepctOrDenine.PNG">

- 로그인 중인 세션이 결재자에 포함된 전자결재 상세화면, 본인의 결재순서라면 '대기' 이미지 대신 승인여부 버튼이 표시됩니다.
  
- 承認ボタンをクリックすると、ajax를 통해 전자결재의 상태를 변경합니다。
  
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

- mybatis에서 프로시저를 호출합니다, 결재를 요청한 전자결재의 번호, 버튼을 통해 수신한 결재상태를 매개변수로 사용합니다.

 <br>

 <!-- 프로시저 상세 -->
 > 프로시저 상세

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure1.PNG">
 
- 매개변수로 전자결재 번호와 전자결재 상태를 수신합니다.
  
<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure3.PNG">

- 결재가 진행됨에 따라 전자결재의 상태도 갱신하기 위해 필요한 변수들을 선언했습니다.

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure2.PNG">

- 매개변수로 전달 받은 결재 상태를 통해 전자결재 상태를 갱신합니다.

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure4.PNG">

- 갱신되는 전자결재의 번호를 조회합니다.

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure6-2.PNG">

- 승인자의 승인 상태는 'b1/대기', 'b2/승인', 'b3/반려' 3개입니다. 

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/procedure6.PNG">

- 전자결재의 상태는 'a1/결재대기', 'a2/진행 증', 'a3/승인종료', 'b4/반려종료' 4개입니다.

 > 프로시저 상세 종료

<br>

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/approved.PNG">
  
- 프로시저를 거쳐 승인자의 결재상태가 '승인'으로 변경되었습니다. 그에 맞는 이미지가 표시됩니다.

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/approval_process.PNG">

- 승인자의 결재가 진행됨에 따라 전자결재의 상태가 갱신됨을 알 수 있습니다.

## 선결기능

<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/not_yet.PNG">

- 현재 로그인 중인 세션으로 접속, 본인의 앞순서가 결재를 진행하지 않아 승인 버튼이 표시되지 않고, '대기' 이미지가 표되었습니다.
