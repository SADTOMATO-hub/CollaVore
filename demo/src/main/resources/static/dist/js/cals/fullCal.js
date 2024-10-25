document.addEventListener('DOMContentLoaded', function() {


	// 종일 체크박스 이벤트 처리
	document.getElementById('allDay').addEventListener('change', function() {
		var startTime = document.getElementById('startTime');
		var endTime = document.getElementById('endTime');

		if (this.checked) {
			// 종일 체크박스가 체크되면 시간 입력 비활성화
			startTime.disabled = true;
			endTime.disabled = true;
		} else {
			// 체크가 해제되면 시간 입력 활성화
			startTime.disabled = false;
			endTime.disabled = false;
		}
	});

	// 일정 생성 버튼 클릭 시 모달 열기
	document.getElementById('createScheduleBtn').addEventListener('click', function() {
		var modal = document.getElementById('scheduleModal'); // 일정생성모달 
		modal.style.display = 'block';  // 모달 열기
	});

	// 일정 생성 모달창 취소 버튼 클릭 시 모달 닫기
	document.getElementById('addCancelBtn').addEventListener('click', function() {
		var modal = document.getElementById('scheduleModal'); // 일정생성모달 
		modal.style.display = 'none'; // 모달 닫기
	});

	// 전역에서 모달 외부 클릭 시 닫기 처리
	window.onclick = function(event) {
		var addScheduleModal = document.getElementById('addScheduleModal'); // 일정생성모달 
		var viewScheduleModal = document.getElementById('viewScheduleModal'); // 일정조회모달 
		var editScheduleModal = document.getElementById('editScheduleModal'); // 일정수정모달 

		// 클릭된 대상이 각각의 모달일 경우에만 닫기
		if (event.target == addScheduleModal) {
			addScheduleModal.style.display = 'none';
		} else if (event.target == viewScheduleModal) {
			viewScheduleModal.style.display = 'none';
		} else if (event.target == editScheduleModal) {
			editScheduleModal.style.display = 'none';
		}
	};


	//============ 캘린더 전체조회 ============     
	fetch('/sch/schList', {
		method: "POST",
		headers: { 'Content-Type': 'application/json' }
	})
		.then(response => response.json())
		.then(eventsData => {

			// 서버에서 받은 데이터를 FullCalendar의 이벤트 구조에 맞게 매핑
			const sList = eventsData.map(check => ({
				id: check.schNo,
				title: check.title,
				start: new Date(check.startDate),
				end: new Date(check.endDate),
				allDay: false
			}));

			//============ 풀캘린더API ============
			var calendarEl = document.getElementById('calendar');
			var calendar = new FullCalendar.Calendar(calendarEl, {
				locale: 'ko',
				headerToolbar: {
					left: 'prev,next today',
					center: 'title',
					right: 'dayGridMonth,timeGridWeek,timeGridDay'
				},
				buttonText: {
					today: "오늘",
					month: "월별",
					week: "주별",
					day: "일별",
				},
				initialDate: getTodayDate(),
				navLinks: true,
				selectable: true,
				selectMirror: true,
				editable: true,
				dayMaxEvents: true,

				//events 안에서 캘린더 리스트 출력 출력할것들 여기 안에서 해결하
				events: sList, //풀캘린더 리스트



				//============ 단건조회 ============
				eventClick: function(info) {
					// 조회 모달 창 열기
					var viewScheduleModal = document.getElementById('viewScheduleModal');
					document.getElementById('viewCalendar').value = info.event.extendedProps.calendarType || '없음';
					document.getElementById('viewTitle').value = info.event.title;
					document.getElementById('viewStartDate').value = info.event.startStr.split('T')[0];
					document.getElementById('viewStartTime').value = info.event.startStr.split('T')[1];
					document.getElementById('viewEndDate').value = info.event.endStr ? info.event.endStr.split('T')[0] : '';
					document.getElementById('viewEndTime').value = info.event.endStr ? info.event.endStr.split('T')[1] : '';

					viewScheduleModal.style.display = 'block'; // 조회 모달 보이기

					// 취소 버튼 클릭 시 모달 닫기
					document.getElementById('viewScheduleModal').querySelector('.viewScheduleClose').addEventListener('click', function() {
						viewScheduleModal.style.display = 'none'; // 모달 닫기
					});

					//==================================== 수정 ====================================
					// 수정 버튼 클릭 시
					var viewScheduleEditBtn = document.getElementById('viewScheduleEditBtn');
					viewScheduleEditBtn.onclick = function() {
						// 단건조회 모달 닫기
						viewScheduleModal.style.display = 'none';
						// 수정 모달 열기editScheduleModal
						var editScheduleModal = document.getElementById('editScheduleModal');
						document.getElementById('editCalendarType').value = info.event.extendedProps.calendarType || '내캘린더';
						document.getElementById('editTitle').value = info.event.title;
						document.getElementById('editStartDate').value = info.event.startStr.split('T')[0];
						document.getElementById('editStartTime').value = info.event.startStr.split('T')[1];
						document.getElementById('editEndDate').value = info.event.endStr ? info.event.endStr.split('T')[0] : '';
						document.getElementById('editEndTime').value = info.event.endStr ? info.event.endStr.split('T')[1] : '';

						editScheduleModal.style.display = 'block'; // 수정 모달 보이기

						document.getElementById('editScheduleForm').onsubmit = function(e) {
							e.preventDefault();

							const schNo = info.event.id;
							const title = document.getElementById('editTitle').value;
							const startDate = document.getElementById('editStartDate').value;
							const endDate = document.getElementById('editEndDate').value;
							const calNo = 0;  // 임시로 0 설정
							const isAlarm = document.getElementById('editAlarm').checked ? "Y" : "N";

							// POST 요청으로 일정 수정하는 부분
							fetch('/sch/schUpdate', {
								method: 'POST',
								headers: { 'Content-Type': 'application/json' },
								body: JSON.stringify({ schNo, title, startDate, endDate, calNo, isAlarm })
							})
								.then(response => response.json())
								.then(data => {
									if (data.result) {
										// 캘린더의 일정을 업데이트
										// 변경된 내용을 즉시 반영하기 위해 info.event 객체를 업데이트
										info.event.setProp('title', title);
										info.event.setStart(startDate);
										info.event.setEnd(endDate);

										// 수정 완료 후 모달 닫기 
										editScheduleModal.style.display = 'none';

										// 성공 메시지 출력 
										alert(data.message);
									} else {
										// 실패 메시지 출력 
										alert(data.message);
									}
								})
								.catch(error => console.error('Error:', error));
						};

					};
					//====================================END 수정 ====================================

					// 삭제 버튼 클릭 시 이벤트
					var viewScheduleDeleteBtn = document.getElementById('viewScheduleDeleteBtn');
					if (viewScheduleDeleteBtn) {
						viewScheduleDeleteBtn.onclick = function() {
							if (confirm('정말로 이 일정을 삭제하시겠습니까?')) {
								fetch('/sch/schDelete', {  // 삭제 API 호출
									method: 'POST',
									headers: { 'Content-Type': 'application/json' },
									body: JSON.stringify({ schNo: info.event.id })
								})
									.then(response => response.json())
									.then(data => {
										if (data.success) {
											info.event.remove();  // FullCalendar에서 삭제
											viewScheduleModal.style.display = 'none'; // 모달 닫기
										} else {
											alert('일정 삭제에 실패했습니다.');
										}
									})
									.catch(error => console.error('Error:', error));
							}
						};
					} else {
						console.error('삭제 버튼을 찾을 수 없습니다.');
					}
				},
				//============END 단건조회 ============

				//============ 일정생성 ============
				//캘린더 안에서 일정생성 API 안에서 일정생성 일어나는것들 모두 여기서 처리 
				select: function(arg) {
					// 일정 추가 모달 창 열기
					var addScheduleModal = document.getElementById('addScheduleModal');
					addScheduleModal.style.display = 'block';


					// 단건조회 모달 닫기
					var addCancelBtn = document.getElementById('addCancelBtn');
					addCancelBtn.style.display = 'none';



					//캘린더 선택에서  공유캘린더 리스트 일정생성 모달창으로 가져오기//
					// 공유캘린더 리스트를 동적으로 가져와서 업데이트
					fetch('/cal/calList')
						.then(response => response.json())
						.then(data => {
							console.log('Received data:', data); // 데이터를 확인하기 위한 로그 출력

							calendarSelectBox.innerHTML = ''; // 기존 리스트 초기화
							// 옵션을 동적으로 추가
							data.forEach(function(calendar) {
								var option = document.createElement('option');
								option.value = calendar.calNo;
								option.text = calendar.name;
								calendarSelectBox.appendChild(option);
							});
						})
						.catch(error => console.error('Error:', error));



					// 현재 시간을 로컬 시간으로 가져오기
					var now = new Date();
					var hours = now.getHours().toString().padStart(2, '0'); // 2자리로 맞추기
					var minutes = now.getMinutes().toString().padStart(2, '0'); // 2자리로 맞추기
					var currentTime = hours + ':' + minutes; // HH:MM 형식으로

					// 시작 시간과 종료 시간에 현재 시간 설정
					document.getElementById('startDate').value = arg.startStr;
					document.getElementById('startTime').value = currentTime;
					document.getElementById('endDate').value = arg.endStr || arg.startStr;
					document.getElementById('endTime').value = currentTime;




					// 알림 설정 여부 (f1: 사용, f2: 미사용)
					var isAlarmEnabled = document.getElementById('isAlarm').checked ? 'f1' : 'f2';

					// 알림 체크박스 선택 시 알림 필드 보이기
					document.getElementById('isAlarm').addEventListener('change', function() {
						var alarmFields = document.getElementById('alarmFields');
						var alarmFrequency = document.getElementById('alarmFrequency').value;

						if (this.checked) {
							alarmFields.style.display = 'block'; // 알림 필드 전체 표시
							showAlarmFields(alarmFrequency); // 현재 선택된 알림 빈도에 맞는 필드 표시
						} else {
							alarmFields.style.display = 'none'; // 알림 필드 전체 숨김
							hideAllAlarmFields(); // 알림 빈도 필드도 숨김
						}
					});

					// 알림 체크박스 선택 시 알림 필드 보이기
					document.getElementById('isAlarm').addEventListener('change', function() {
						var alarmFields = document.getElementById('alarmFields');
						var alarmFrequency = document.getElementById('alarmFrequency').value;

						if (this.checked) {
							alarmFields.style.display = 'block'; // 알림 필드 전체 표시
							showAlarmFields(alarmFrequency); // 현재 선택된 알림 빈도에 맞는 필드 표시
						} else {
							alarmFields.style.display = 'none'; // 알림 필드 전체 숨김
							hideAllAlarmFields(); // 알림 빈도 필드도 숨김
						}
					});

					// 알림 빈도 선택에 따라 필드 표시
					document.getElementById('alarmFrequency').addEventListener('change', function() {
						var frequency = this.value;
						showAlarmFields(frequency); // 빈도에 따라 필드 표시
					});

					function showAlarmFields(frequency) {
						hideAllAlarmFields(); // 먼저 모든 알림 필드를 숨김

						// 선택된 빈도에 맞는 필드만 표시
						if (frequency === 'daily') {
							document.getElementById('dailyRepeat').style.display = 'block';
						} else if (frequency === 'weekly') {
							document.getElementById('weeklyRepeat').style.display = 'block';
						} else if (frequency === 'monthly') {
							document.getElementById('monthlyRepeat').style.display = 'block';
						}
					}

					function hideAllAlarmFields() {
						document.getElementById('dailyRepeat').style.display = 'none';
						document.getElementById('weeklyRepeat').style.display = 'none';
						document.getElementById('monthlyRepeat').style.display = 'none';
					}



					// 특정 입력 필드에서 24를 초과하면 24로 제한하는 함수
					function enforceMaxValue(inputField) {
						inputField.addEventListener('input', function() {
							var value = parseInt(this.value, 10);
							if (value > 24) {
								this.value = 24;  // 24를 초과하면 24로 다시 설정
							} else if (value < 1) {
								this.value = 1;  // 1 미만이면 1로 설정
							}
						});
					}

					// weeklyHour와 monthlyHour 필드에 대해 24를 초과하지 않도록 제한
					var dailyIntervalInput = document.getElementById('dailyInterval');
					var weeklyHourInput = document.getElementById('weeklyHour');
					var monthlyHourInput = document.getElementById('monthlyHour');

					enforceMaxValue(dailyIntervalInput);
					enforceMaxValue(weeklyHourInput);
					enforceMaxValue(monthlyHourInput);
					// END 특정 입력 필드에서 24를 초과하면 24로 제한하는 함수	


					// 알림 빈도 처리
					var isAlarm = document.getElementById('isAlarm').checked;
					var alarmFrequency = document.getElementById('alarmFrequency').value;
					var alarmData = null;

					if (isAlarm) {
						alarmData = {
							frequency: alarmFrequency,
							interval: document.getElementById(alarmFrequency + 'Interval')?.value || null, // 일/주/달 마다 설정
							selectedDays: getSelectedDays(), // 매주일 경우 선택된 요일들
							repeatEndDate: document.getElementById('repeatEndDate')?.value || null // 종료일이 있을 경우
						};
					}

					// 선택된 요일들 반환 (매주 알림 설정 시)
					function getSelectedDays() {
						var selectedDays = [];
						document.querySelectorAll('input[name="weeklyDay"]:checked').forEach(function(checkbox) {
							selectedDays.push(checkbox.value);
						});
						return selectedDays;
					}

					// 종일 체크 여부
					var isAllDay = document.getElementById('allDay').checked;
					if (isAllDay) {
						endTime = '23:59:59'; // 자정까지
					}

					//							일정양식
					document.getElementById('scheduleForm').onsubmit = function(e) {
						e.preventDefault();

						var calNo = document.getElementById('calendarSelectBox').value;
						var title = document.getElementById('title').value;
						var startDate = document.getElementById('startDate').value;
						var startTime = document.getElementById('startTime').value;
						var endDate = document.getElementById('endDate').value;
						var endTime = document.getElementById('endTime').value;

						// 시작일과 종료일에 시간을 추가하여 하나의 datetime으로 변환
						var startDateTime = new Date(startDate + 'T' + startTime).toISOString(); // ISO 형식으로 변환
						var endDateTime = new Date(endDate + 'T' + endTime).toISOString(); // ISO 형식으로 변환

						// schsData 변수 초기화 및 데이터 설정
						var schsData = {
							title: title,
							startDate: startDateTime,
							endDate: endDateTime,
							calNo: calNo,  // 선택된 캘린더 번호
							isAllDay: isAllDay,
							isAlarm: isAlarm,  // 알림 여부 (f1 or f2)
							repeatData: repeatData
						};

						console.log(schsData); // 데이터를 확인하기 위한 로그 출력

						if (title) {
							fetch('/sch/schInsert', {  // 등록 API 호출
								method: 'POST',
								headers: { 'Content-Type': 'application/json' },
								body: JSON.stringify(schsData)
							})
								.then(response => response.json())
								.then(data => {
									console.log(data); // 데이터를 확인하기 위한 로그 출력
									if (data.success) {
										alert('일정 등록에 성공했습니다.');
										addScheduleModal.style.display = 'none'; // 모달 닫기
										// FullCalendar에 새 일정 추가
										calendar.addEvent({
											id: data.schNo,  // 서버에서 반환된 일정 번호
											title: schsData.title,
											start: schsData.startDate,  // FullCalendar에서 사용할 시작 시간
											end: schsData.endDate,      // FullCalendar에서 사용할 종료 시간
											allDay: schsData.isAllDay   // 하루 종일 여부
										});
									} else {
										console.log(data); // 데이터를 확인하기 위한 로그 출력
										alert('일정 등록에 실패했습니다.');
									}
								})
								.catch(error => console.error('Error:', error));
						}
						// 모달 닫기
						modal.style.display = 'none';
						calendar.unselect();
					};
				}
				//============END 일정생성 ============
			});

			calendar.render();
			//============ 풀캘린더API ============

		})
		.catch(error => console.error('Error fetching events:', error));
	//============ END 캘린더 전체조회 ======================== END 캘린더 전체조회 ======================== END 캘린더 전체조회 ============
	//============ END 캘린더 전체조회 ======================== END 캘린더 전체조회 ======================== END 캘린더 전체조회 ============
	//============ END 캘린더 전체조회 ======================== END 캘린더 전체조회 ======================== END 캘린더 전체조회 ============
	//============ END 캘린더 전체조회 ======================== END 캘린더 전체조회 ======================== END 캘린더 전체조회 ============
	//============ END 캘린더 전체조회 ======================== END 캘린더 전체조회 ======================== END 캘린더 전체조회 ============
	//============ END 캘린더 전체조회 ======================== END 캘린더 전체조회 ======================== END 캘린더 전체조회 ============
	//==========================================================================================================================================
	//==========================================================================================================================================
	//==========================================================================================================================================
	//==========================================================================================================================================
	//==========================================================================================================================================
	//==========================================================================================================================================
	//==========================================================================================================================================
	//==========================================================================================================================================
	//============ 사이드바 ======================== 사이드바 ======================== 사이드바 ======================== 사이드바 ============
	//============ 사이드바 ======================== 사이드바 ======================== 사이드바 ======================== 사이드바 ============
	//============ 사이드바 ======================== 사이드바 ======================== 사이드바 ======================== 사이드바 ============
	//============ 사이드바 ======================== 사이드바 ======================== 사이드바 ======================== 사이드바 ============
	//============ 사이드바 ======================== 사이드바 ======================== 사이드바 ======================== 사이드바 ============
	//============ 사이드바 ======================== 사이드바 ======================== 사이드바 ======================== 사이드바 ============
	//============ 사이드바 리스트===========
	function loadSharedCalendars() {
		fetch('/cal/calList')
			.then(response => response.json())
			.then(data => {
				const sharedCalendarList = document.getElementById('sharedCalendarList');
				sharedCalendarList.innerHTML = '';  // 기존 목록 초기화

				data.forEach(calendar => {
					const newCalendarItem = document.createElement('li');
					newCalendarItem.innerHTML = makeSidEvent(calendar); //html 양식 맨아래 함수로 지
					sharedCalendarList.appendChild(newCalendarItem);

					//============ 사이드바 내 캘린더 수정 ==============
					// 연필 아이콘 클릭 이벤트 바로 추가
					const editIcon = newCalendarItem.querySelector('.edit-icon');
					editIcon.addEventListener('click', function() {
						const calendarItem = this.closest('.calendar-item-wrapper');
						const selectedCalNo = calendarItem.querySelector('.calendar-item').getAttribute('data-calno');

						// 선택된 calNo 값을 hidden input에 설정
						document.getElementById('selectedCalNo').value = selectedCalNo;
						// 모달 열기
						document.getElementById('editCalendarModal').style.display = 'block';
					});

					// 연필 아이콘 클릭 이벤트 바로 추가
					// 사이드바의 전체 영역에 이벤트 위임을 사용하여 edit-icon 클릭 이벤트 처리
					editIcon.addEventListener('click', function() {
						const calendarItem = event.target.closest('.calendar-item-wrapper');
						const selectedCalNo = calendarItem.querySelector('.calendar-item').getAttribute('data-calno');
						const calendarName = calendarItem.querySelector('.calendar-item').textContent.trim();
						const calendarIcon = calendarItem.querySelector('i');
						const calendarColor = window.getComputedStyle(calendarIcon).color;

						// 모달창에 기존 값 설정
						document.getElementById('editCalendarName').value = calendarName;

						// 색상 설정
						const colorRadios = document.querySelectorAll('input[name="color"]');
						colorRadios.forEach(radio => {
							const radioLabel = document.querySelector(`label[for="${radio.id}"]`);
							if (radioLabel && window.getComputedStyle(radioLabel).backgroundColor === calendarColor) {
								radio.checked = true;
							}
						});

						// 모달창 열기
						document.getElementById('editCalendarModal').style.display = 'block';
					});

					// 모달 닫기 버튼 처리  여러모달이 잇는 경우에도 작동하며 각 모달에 대한 닫기 버튼을 클릭하면 해당 모달이 닫히도록 
					document.querySelectorAll('.close').forEach(closeBtn => {
						closeBtn.addEventListener('click', function() {
							const modal = this.closest('.modal');
							modal.style.display = 'none'; // 모달 닫기
						});
					});

					document.getElementById('editCalendarForm').onsubmit = function(e) {
						e.preventDefault();


						const calNo = document.getElementById('selectedCalNo').value;
						const name = document.getElementById('editCalendarName').value;
						const color = document.querySelector('input[name="color"]:checked').value;

						// 서버로 수정 요청 보내기
						fetch('/cal/calUpdate', {
							method: 'POST',
							headers: { 'Content-Type': 'application/json' },
							body: JSON.stringify({ calNo: calNo, name: name, color: color })
						})
							.then(response => response.json())
							.then(data => {
								if (data.result) {
									alert('캘린더 수정이 성공적으로 완료되었습니다.');
									location.reload();  // 새로고침하여 변경 사항 반영
								} else {
									alert('캘린더 수정에 실패했습니다.');
								}
							})
							.catch(error => console.error('Error:', error));
					};

					//============END 사이드바 내 캘린더 수정 ==============

						const calNo = document.getElementById('selectedCalNo').value;
						const name = document.getElementById('editCalendarName').value;
						const color = document.querySelector('input[name="color"]:checked').value;

						// 서버로 수정 요청 보내기
						fetch('/cal/calUpdate', {
							method: 'POST',
							headers: { 'Content-Type': 'application/json' },
							body: JSON.stringify({ calNo: calNo, name: name, color: color })
						})
							.then(response => response.json())
							.then(data => {
								if (data.result) {
									alert('캘린더 수정이 성공적으로 완료되었습니다.');
									location.reload();  // 새로고침하여 변경 사항 반영
								} else {
									alert('캘린더 수정에 실패했습니다.');
								}
							})
							.catch(error => console.error('Error:', error));
					};

					//============END 사이드바 내 캘린더 수정 ==============

				});
			})
			.catch(error => console.error('Error loading calendars:', error));
	}

	// 페이지 로드 시 캘린더 목록 불러오기 맨아래 있는거 원래 위치



	//============ END 사이드바 리스트===========
	//============ 사이드바 내 캘린더 생성 ===========
	// 공유 캘린더 모달 열기/닫기
	document.getElementById('addSharedCalendarBtn').onclick = function() {
		document.getElementById('sharedCalendarModal').style.display = 'block';
	};
	document.getElementById('cancelSharedCalendar').onclick = function() {
		document.getElementById('sharedCalendarModal').style.display = 'none';
	};




	// 모달 폼 제출 처리
	document.getElementById('sharedCalendarForm').onsubmit = function(e) {
		e.preventDefault();

		// 입력받은 캘린더 정보 가져오기
		const sharedCalendarName = document.getElementById('sharedCalendarName').value;
		const selectedColor = document.querySelector('input[name="color"]:checked').value;
		const members = Array.from(document.getElementById('sharedCalendarMembers').selectedOptions).map(option => option.value);

		// calendarName 대신 sharedCalendarName 사용
		if (!sharedCalendarName || !selectedColor || members.length === 0) {
			alert('캘린더 이름, 색상, 공유 대상을 선택해주세요.');
			return;
		}



		// 서버에 캘린더 정보 저장 요청
		fetch('/cal/calInsert', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({
				name: sharedCalendarName,
				color: selectedColor,
				type: 'g2', // 공유 캘린더 타입
				isDelete: 'h2' // 기본값으로 'h2' 설정
			})
		})
			.then(response => response.json())
			.then(data => {
				if (data.success) {
					// 캘린더가 성공적으로 추가되었을 때만 사이드바에 추가
					const sharedCalendarList = document.getElementById('sharedCalendarList');
					const newCalendarItem = document.createElement('li');

					//li추가 연필추가
					newCalendarItem.innerHTML = makeSidEvent(data.calsVO);


					sharedCalendarList.appendChild(newCalendarItem);




					// 폼 초기화 
					//document.getElementById('sharedCalendarName').value = ''; // 캘린더 이름 필드 초기화
					//const colorInputs = document.querySelectorAll('input[name="color"]'); // 색상 선택 필드 가져오기
					//colorInputs.forEach(input => input.checked = false); // 색상 선택 해제

				} else {
					alert('캘린더 추가에 실패했습니다: ' + data.message);
				}
			})
			.catch(error => {
				console.error('Error:', error);
				alert('서버와 통신 중 문제가 발생했습니다.');
			});
		// 모달 닫기
		document.getElementById('sharedCalendarModal').style.display = 'none';
	};




	//============END 사이드바 내 캘린더 생성 ===========




	//============ 사이드바 공유 캘린더 생성 ===========







	//============END 사이드바 공유 캘린더 생성 ===========
	//============ 사이드바 휴지통 보내기  ===========
	// 삭제 버튼 클릭 시 이벤트 
	document.getElementById('deleteBtn').onclick = function()  {
		const calNo = parseInt(document.getElementById('selectedCalNo').value); // 문자열을 숫자로 변환

		if (confirm('정말로 이 캘린더를 삭제하시겠습니까?')) {
			fetch('/cal/calTrash', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ calNo: calNo }) // 선택된 캘린더 번호를 전송
			})
				.then(response => response.json())
				.then(data => {
					if (data.success) {
						alert('캘린더가 휴지통으로 이동되었습니다.');
						document.getElementById('editCalendarModal').style.display = 'none';
						location.reload();  // 페이지 새로고침하여 변경 반영
					} else {
						alert('캘린더 삭제에 실패했습니다.');
					}
				})
				.catch(error => console.error('Error:', error));
		}
	};
	//============ 사이드바 휴지통 보내기   ===========

	//============ 사이드바 휴지통에서 복원 ==============


	//============ 사이드바 휴지통에서 복원 ==============

	//============ 휴지통 리스트 불러오기 ===========
	fetch('/cal/trashList')
		.then(response => response.json())
		.then(data => {
			const wasteCalendarList = document.getElementById('wasteCalendarList');
			wasteCalendarList.innerHTML = '';  // 기존 리스트 초기화

			data.forEach(calendar => {
				const newTrashItem = document.createElement('li');
				newTrashItem.classList.add('trash-item');
				newTrashItem.setAttribute('data-calno', calendar.calNo);
				newTrashItem.innerHTML = makeSidEvent(calendar);
				wasteCalendarList.appendChild(newTrashItem);
				//========================= 휴지통 리스트 옆 연필 아이콘 클릭 모달 ==================================
				const editIcon = newTrashItem.querySelector('.edit-icon');
				editIcon.addEventListener('click', function() {
					const calendarItem = this.closest('.trash-item');
					const selectedCalNo = calendarItem.getAttribute('data-calno');
					const calendarName = calendarItem.textContent.trim();

					// 모달창에 기존 값 설정
					document.getElementById('trashSelectedCalNo').value = selectedCalNo;
					document.getElementById('trashCalendarName').value = calendarName;

					// 모달 열기
					document.getElementById('trashCalendarModal').style.display = 'block';

					//========================= END 휴지통 리스트 옆 연필 아이콘 클릭 모달 ==================================

				});

			});








		})
		.catch(error => console.error('Error loading trash calendars:', error));


	//=========================  휴지통 리스트 옆 연필 아이콘 클릭 모달 ==================================
	// 모달 닫기 버튼 처리
	document.querySelectorAll('.close').forEach(closeBtn => {
		closeBtn.addEventListener('click', function() {
			const modal = this.closest('.modal');
			modal.style.display = 'none'; // 모달 닫기
		});
	});
	//=========================  휴지통 리스트 옆 연필 아이콘 클릭 모달 ==================================

	//============ 사이드바 휴지통 복원   ===========

	// 휴지통에서 복원 버튼 클릭 시 복원 처리
	document.getElementById('restoreCalendarBtn').addEventListener('click', function() {
		const calNo = document.getElementById('trashSelectedCalNo').value;

		fetch('/cal/calRestore', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ calNo: calNo })
		})
			.then(response => response.json())
			.then(data => {
				if (data.success) {
					alert('캘린더가 복원되었습니다.');
					document.getElementById('trashCalendarModal').style.display = 'none';
					location.reload();  //페이지 새로고침
				} else {
					alert('복원에 실패했습니다.');
				}
			})
			.catch(error => console.error('Error:', error));
	});



	//============END 사이드바 휴지통 복원   ===========

	//============ 사이드바 휴지통 완전삭제 =============
	//============ 휴지통에서 영구 삭제  ===========
	// 영구 삭제 버튼 클릭 시 이벤트 처리
	//============ 휴지통에서 영구 삭제  ===========
	// 기존에 등록된 이벤트 리스너 제거 후 새로 등록
	const deleteBtn = document.getElementById('permanentlyDeleteBtn');

	// 기존 이벤트 리스너 제거
	deleteBtn.removeEventListener('click', handlePermanentlyDelete);

	// 삭제 처리 함수
	function handlePermanentlyDelete() {
		const calNo = parseInt(document.getElementById('trashSelectedCalNo').value);  // 선택된 캘린더 번호 가져오기

		if (confirm('정말로 이 캘린더를 완전히 삭제하시겠습니까?')) {
			fetch('/cal/calDel', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ calNo: calNo })  // JSON 데이터로 전송
			})
				.then(response => response.json())
				.then(data => {
					if (data === "캘린더가 완전히 삭제되었습니다.") {
						alert('캘린더가 영구적으로 삭제되었습니다.');
						document.getElementById('trashCalendarModal').style.display = 'none';
						location.reload();  //페이지 새로고침
					} else {
						alert('캘린더 삭제에 실패했습니다.');
					}
				})
				.catch(error => console.error('Error:', error));
		}
	}

	// 새로운 이벤트 리스너 등록
	deleteBtn.addEventListener('click', handlePermanentlyDelete);


	//============ 사이드바 휴지통 완전삭제 =============

	//============ 사이드바 휴지통 리스트  ===========

	//============END 사이드바 휴지통  ===========




	loadSharedCalendars(); //사이드바 맨위
	//============END 사이드바 ============

});
//location.reload();  //페이지 새로고침 ㅎㅎㅎㅎ
//=================================함수모음======================================
// 오늘 날짜를 YYYY-MM-DD 형식으로 반환하는 함수
function getTodayDate() {
	return new Date().toISOString().slice(0, 10);
}

//사이드바  캘린더 리스트 생성 뿌리기 
function makeSidEvent(calendar) {
	let tag =
		`
                        <div class="calendar-item-wrapper" style="display: flex; align-items: center;">
                            <a href="javascript:void(0)" data-calno="${calendar.calNo}" class="calendar-item">
                                <i class="mdi mdi-calendar-blank" style="color:${calendar.color};"></i> ${calendar.name}
                            </a>
                            <span class="edit-icon" style="margin-left: 10px;">
                                <i class="mdi mdi-pencil" aria-hidden="true"></i>
                            </span>
                        </div>`;
	return tag;
}



//================================END 함수모음====================================
