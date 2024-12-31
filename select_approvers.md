# 決裁者選択
 - 결재자는 최대 4명까지 선택 가능합니다.
 - 결재자의 모든 정보는 배열로 저장되어집니다.

## 決裁者選択

1. 결재자 선택 버튼을 클릭하면 모달창이 활성화되어 부서를 선택할 수 있게 됩니다.
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/select_approvers.PNG">
  
2. 부서를 선택하면 비동기 작업이 실행됩니다.

```
 
 // 부서 선택 시, 해당 부서에 소속된 결재자 목록을 동적으로 불러오는 AJAX
        $('#selectDept').on('change', function() {
            var deptNo = $(this).val(); // 선택된 부서 번호

            // 부서 번호가 없으면 리턴
            if (!deptNo) return;

            // 결재자 선택 드롭다운 초기화
            $('#selectApprovers').empty();
            $('#selectApprovers').append('<option selected value="" disabled>==결재자 선택==</option>');

            // AJAX 요청: 해당 부서의 결재자 목록을 받아옴
            $.ajax({
                url: '/approvals/selectEmps/' + deptNo,
                type: 'POST',
                success: function(response) {
                    if (response.length === 0) {
                        $('#selectApprovers').append('<option value="">결재자 없음</option>');
                        return;
                    }
                    // 응답 받은 결재자 목록을 드롭다운에 추가
                    $.each(response, function(index, employeesInfo) {
                        $('#selectApprovers').append(
                            `<option value="${employeesInfo.empNo}">${employeesInfo.empName} / ${employeesInfo.posiName}</option>`
                        );
                    });
                },
                error: function(xhr, status, error) {
					Toast.fire({
					  icon: "error",
					  title: "결재자 정보를 불러오는 중 오류가 발생했습니다."
					});
                }
            });
        });
    });
    
```

3. 선택된 부서에 소속된 사원이 출력됩니다.
  <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/approvers.png">

4. 결재자 선택 버튼을 클릭하면 이벤트를 발생시킨 버튼의 하위 tr,td에 정보가 입력됩니다.
　<img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/selected_approvers.PNG">

 ```
<thead>
		<tr>
			<td></td>
					<td style="text-align: center;">
						<button type="button" class="btn btn-warning modalBtn" data-seq="1" data-bs-target="#approversSelect" data-bs-toggle="modal">선택</button>
					</td>
					
					<td style="text-align: center;">
	          <button type="button" class="btn btn-warning modalBtn" data-seq="2" data-bs-target="#approversSelect" data-bs-toggle="modal">선택</button>
					</td>
				
					<td style="text-align: center;">
	          <button type="button" class="btn btn-warning modalBtn" data-seq="3" data-bs-target="#approversSelect" data-bs-toggle="modal">선택</button>
					</td>

					<td style="text-align: center;">
	          <button type="button" class="btn btn-warning modalBtn" data-seq="4" data-bs-target="#approversSelect" data-bs-toggle="modal">선택</button>
          </td
      </tr>
</thead>
 ```

- 각 각의 버튼엔 data-set 속성을 부여하여 이벤트를 발생시킨 버튼을 구별합니다.
  
5. drop-down으로 기안하려는 전자결재 양식에 맞는 템플릿을 호출하여 내용을 정합니다.
   <img src="https://github.com/leewoosang-hub/CollaVore/blob/master/images/template.PNG">
