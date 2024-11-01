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



	// 전역에서 모달 외부 클릭 시 닫기 처리
	window.onclick = function(event) {
		var addScheduleModal = document.getElementById('addScheduleModal'); // 일정 생성 모달
		var viewScheduleModal = document.getElementById('viewScheduleModal'); // 일정 조회 모달
		var editScheduleModal = document.getElementById('editScheduleModal'); // 일정 수정 모달

		// 클릭된 대상이 각각의 모달일 경우에만 닫기
		if (event.target == addScheduleModal) {
			addScheduleModal.style.display = 'none';
		} else if (event.target == viewScheduleModal) {
			viewScheduleModal.style.display = 'none';

			// 버튼 텍스트를 다시 "수정"으로 설정
			document.getElementById('viewScheduleEditBtn').textContent = '수정';
		} else if (event.target == editScheduleModal) {
			editScheduleModal.style.display = 'none';
		}
	};


	//============ 사이드바 선택시 캘린더 조회 ============     

	//============ end 사이드바 선택시 캘린더 조회  ============     




	//============ 캘린더 전체조회 ============     
	fetch('/sch/schList', {
		method: "POST",
		headers: { 'Content-Type': 'application/json' }
	})
		.then(response => response.json())
		.then(eventsData => {
			// 서버에서 받은 데이터를 FullCalendar의 이벤트 구조에 맞게 매핑
			let sList = eventsData.map(check => ({
				id: check.schNo,
				title: check.title,
				start: new Date(check.startDate),
				end: new Date(check.endDate),
				allDay: false,
				// 캘린더 타입이 g2(공유 캘린더)인 경우 특정 색상 적용
				backgroundColor: check.color, // 데이터베이스에서 가져온 색상 적용
				borderColor: check.color,      // 데이터베이스에서 가져온 색상 적용
				calType: check.calType,  // 일정의 캘린더 타입 (g1, g2, g3)
				calNo: check.calNo         // g2(공유 캘린더)의 이름 추가
			}));
			console.log(sList);

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



				// 서버로 업데이트된 일정 데이터 전송
				eventDrop: function(info) {
					// 이동 후의 시작일과 종료일을 Oracle에서 이해할 수 있는 'YYYY-MM-DD HH:MM:SS' 형식으로 변환
					var formatDateTime = (date) => {
						const d = new Date(date);
						const year = d.getFullYear();
						const month = String(d.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
						const day = String(d.getDate()).padStart(2, '0');
						const hours = String(d.getHours()).padStart(2, '0');
						const minutes = String(d.getMinutes()).padStart(2, '0');
						const seconds = String(d.getSeconds()).padStart(2, '0');
						return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
					};

					var updatedEvent = {
						schNo: info.event.id,  // schNo로 전달
						startDate: formatDateTime(info.event.start),
						endDate: info.event.end ? formatDateTime(info.event.end) : formatDateTime(info.event.start) // 종료일이 없으면 시작일과 동일하게 설정
					};

					// 서버로 업데이트된 일정 데이터 전송
					fetch('/sch/updateTime', {
						method: 'POST',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify(updatedEvent)
					})
						.then(response => response.text()) // 텍스트 형식으로 응답 받기
						.then(data => {
							console.log(data); // 여기서 응답을 출력
							if (data === "success") {S
								alert('일정이 성공적으로 업데이트되었습니다.');
							} else {
								alert('일정 업데이트에 실패했습니다.');
								info.revert(); // 서버 응답이 실패한 경우 일정 위치 복구
							}
						})
						.catch(error => {
							console.error('Error updating event:', error);
							alert('일정 업데이트 중 오류가 발생했습니다.');
							info.revert(); // 오류 발생 시 일정 위치 복구
						});
				},

				//events 안에서 캘린더 리스트 출력 출력할것들 여기 안에서 해결하
				events: sList, //풀캘린더 리스트



				//============================================== 단건조회 ============================================
				eventClick: function(info) {

					var eventId = info.event.id;

					fetch(`/sch/schInfo?schNo=${eventId}`)
						.then(response => response.json())
						.then(data => {
							if (data.success) {
								var eventData = data.data;

								console.log(eventData.calType);
								resetFieldsToReadonly();

								// 기본 일정 정보 표시
								var viewScheduleModal = document.getElementById('viewScheduleModal');
								document.getElementById('viewCalendar').value =
									eventData.calType === 'g1' ? '내 캘린더' :
										eventData.calType === 'g2' ? eventData.name :
											eventData.calType === 'g3' ? '프로젝트 캘린더' : '알 수 없음';
								document.getElementById('viewTitle').value = eventData.title || '';
								document.getElementById('viewStartDate').value = eventData.startDate ? eventData.startDate.split('T')[0] : '';
								document.getElementById('viewStartTime').value = eventData.startDate ? eventData.startDate.split('T')[1].substring(0, 5) : '';
								document.getElementById('viewEndDate').value = eventData.endDate ? eventData.endDate.split('T')[0] : '';
								document.getElementById('viewEndTime').value = eventData.endDate ? eventData.endDate.split('T')[1].substring(0, 5) : '';

								// 시작일과 종료일을 Date 객체로 변환
								var startDate = new Date(eventData.startDate);
								var endDate = new Date(eventData.endDate);

								// 날짜와 시간을 원하는 형식으로 설정
								document.getElementById('viewStartDate').value = startDate.toISOString().split('T')[0];
								document.getElementById('viewStartTime').value = startDate.toTimeString().substring(0, 5);
								document.getElementById('viewEndDate').value = endDate.toISOString().split('T')[0];
								document.getElementById('viewEndTime').value = endDate.toTimeString().substring(0, 5);



								// 알림 설정
								var alarmSettings = document.getElementById('viewAlarmSettings');
								var alarmTypeInput = document.getElementById('viewAlarmType');
								var dailySettings = document.getElementById('viewDailySettings');
								var weeklySettings = document.getElementById('viewWeeklySettings');
								var monthlySettings = document.getElementById('viewMonthlySettings');

								if (eventData.isAlarm === 'f1') {
									alarmSettings.style.display = 'block';
									alarmTypeInput.value = eventData.alarmType;

									if (eventData.alarmType === 'd1') {
										dailySettings.style.display = 'block';
										weeklySettings.style.display = 'none';
										monthlySettings.style.display = 'none';
										document.getElementById('viewDailyRepeat').value = eventData.alarmTime || '';
									} else if (eventData.alarmType === 'd2') {
										dailySettings.style.display = 'none';
										weeklySettings.style.display = 'block';
										monthlySettings.style.display = 'none';
										document.querySelectorAll('.weekly-checkbox').forEach((checkbox) => {
											checkbox.checked = eventData.alarmYoil.includes(checkbox.value);
											document.getElementById('viewWeeklyRepeat').value = eventData.alarmTime || '';
										});
									} else if (eventData.alarmType === 'd3') {
										dailySettings.style.display = 'none';
										weeklySettings.style.display = 'none';
										monthlySettings.style.display = 'block';
										document.getElementById('viewMonthlyDay').value = eventData.alarmDay || '';
										document.getElementById('viewMonthlyHour').value = eventData.alarmTime || '';
									}
								} else {
									alarmSettings.style.display = 'none';
								}



								// 체크박스 초기 설정 및 숨기기
								var alarmCheckboxWrapper = document.getElementById('viewAlarmCheckboxWrapper');
								var alarmCheckbox = document.getElementById('viewAlarmUse');

								console.log('alarmCheckboxWrapper:', alarmCheckboxWrapper); // null 이 아니어야 함
								console.log('alarmCheckbox:', alarmCheckbox); // null 이 아니어야 함

								if (alarmCheckboxWrapper && alarmCheckbox) {
									// 요소가 존재할 때만 속성 설정
									alarmCheckboxWrapper.style.display = 'none';  // 처음에는 숨김
									alarmCheckbox.checked = (eventData.isAlarm === 'f1');
								} else {
									console.error('알림 설정 체크박스를 찾을 수 없습니다.');
								}

								// 모달 표시
								viewScheduleModal.style.display = 'block';

								// 모달 닫을 때 필드 초기화
								document.getElementById('viewScheduleModal').querySelector('.viewScheduleClose').addEventListener('click', function() {
									var viewScheduleModal = document.getElementById('viewScheduleModal');
									viewScheduleModal.style.display = 'none';
									resetFieldsToReadonly(); // 필드를 읽기 전용으로 초기화
								});

								//==================================== 수정 ====================================
								document.getElementById('viewScheduleEditBtn').onclick = function() {
									// 수정 가능하게 설정
									document.getElementById('viewTitle').readOnly = false;
									document.getElementById('viewStartDate').readOnly = false;
									document.getElementById('viewStartTime').readOnly = false;
									document.getElementById('viewEndDate').readOnly = false;
									document.getElementById('viewEndTime').readOnly = false;

									// 알림 체크박스와 알림 빈도 필드 활성화
									var alarmCheckboxWrapper = document.getElementById('viewAlarmCheckboxWrapper');
									var alarmCheckbox = document.getElementById('viewAlarmUse');
									alarmCheckboxWrapper.style.display = 'block';
									alarmCheckbox.disabled = false;

									var alarmTypeInput = document.getElementById('viewAlarmType');
									alarmTypeInput.disabled = false;

									// 알림 타입 변경 시 세부 필드 활성화 함수
									function updateAlarmSettings(reset = false) {
										if (reset) {
											resetAlarmFields(); // 알림 타입 변경 시만 초기화
										}

										var dailySettings = document.getElementById('viewDailySettings');
										var weeklySettings = document.getElementById('viewWeeklySettings');
										var monthlySettings = document.getElementById('viewMonthlySettings');
										var alarmTypeInput = document.getElementById('viewAlarmType');


										if (alarmCheckbox.checked) {
											document.getElementById('viewAlarmSettings').style.display = 'block';

											if (alarmTypeInput.value === 'd1') {
												dailySettings.style.display = 'block';
												weeklySettings.style.display = 'none';
												monthlySettings.style.display = 'none';
												document.getElementById('viewDailyRepeat').readOnly = false;
											} else if (alarmTypeInput.value === 'd2') {
												dailySettings.style.display = 'none';
												weeklySettings.style.display = 'block';
												monthlySettings.style.display = 'none';
												document.querySelectorAll('.weekly-checkbox').forEach(cb => cb.disabled = false);
												document.getElementById('viewWeeklyRepeat').readOnly = false;
											} else if (alarmTypeInput.value === 'd3') {
												dailySettings.style.display = 'none';
												weeklySettings.style.display = 'none';
												monthlySettings.style.display = 'block';
												document.getElementById('viewMonthlyDay').readOnly = false;
												document.getElementById('viewMonthlyHour').readOnly = false;
											}
										} else {

											document.getElementById('viewAlarmSettings').style.display = 'none';
										}
									}

									// 초기 설정 및 이벤트 설정
									updateAlarmSettings();
									alarmCheckbox.addEventListener('change', updateAlarmSettings);
									alarmTypeInput.addEventListener('change', updateAlarmSettings);






									// 저장 버튼으로 변경
									this.textContent = '저장';

									// 취소 버튼 설정
									document.querySelector('.viewScheduleClose').onclick = function() {
										// 모달 닫기
										viewScheduleModal.style.display = 'none';

										// 버튼 텍스트를 다시 "수정"으로 설정
										document.getElementById('viewScheduleEditBtn').textContent = '수정';
									};
									//======================= 24이상 금지==========================
									// 숫자 입력 필드에서 24 이상 입력되지 않도록 제한하는 함수
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


									// 수정 모드에서 사용되는 필드에 24 제한 적용
									enforceMaxValue(document.getElementById('viewDailyRepeat'));
									enforceMaxValue(document.getElementById('viewWeeklyRepeat'));
									enforceMaxValue(document.getElementById('viewMonthlyHour'));
									//======================end 24이상 금지===============================
									//======================= 월에 상관없이 1~31 제한 ===========================
									// 특정 입력 필드에서 1~31 범위로만 제한하는 함수
									function enforceDayRange(dayInputField) {
										if (!dayInputField) {
											console.warn("dayInputField is not found.");
											return;
										}

										dayInputField.addEventListener('input', function() {
											let dayValue = parseInt(this.value, 10);

											if (dayValue > 31) {
												this.value = 31;  // 31을 초과하면 31로 다시 설정
											} else if (dayValue < 1) {
												this.value = 1;  // 1 미만이면 1로 설정
											}
										});
									}

									// 필드가 존재할 경우에만 1~31 제한 함수 적용
									const dayInputField = document.getElementById('viewMonthlyDay');
									if (dayInputField) {
										enforceDayRange(dayInputField);
									} else {
										console.warn("dayInputField is not found.");
									}
									//======================= end 월에 상관없이 1~31 제한 ===========================

									this.onclick = function() {



										// 예외 처리 추가
										if (alarmCheckbox.checked) {
											if (alarmTypeInput.value === 'd1') { // 매일 알림 예외 처리
												const dailyRepeat = document.getElementById('viewDailyRepeat').value;
												if (!dailyRepeat || dailyRepeat < 1 || dailyRepeat > 24) {
													alert("매일 알림의 '시 마다' 설정을 1~24 사이의 숫자로 입력해주세요.");
													return;
												}
											} else if (alarmTypeInput.value === 'd2') { // 매주 알림 예외 처리
												const weeklyRepeat = document.getElementById('viewWeeklyRepeat').value;
												const selectedDays = Array.from(document.querySelectorAll('.weekly-checkbox:checked')).map(cb => cb.value);

												if (selectedDays.length === 0) {
													alert("매주 알림의 요일을 선택해주세요.");
													return;
												}
												if (!weeklyRepeat || weeklyRepeat < 1 || weeklyRepeat > 24) {
													alert("매주 알림의 '시 마다' 설정을 1~24 사이의 숫자로 입력해주세요.");
													return;
												}
											} else if (alarmTypeInput.value === 'd3') { // 매달 알림 예외 처리
												const monthlyDay = document.getElementById('viewMonthlyDay').value;
												const monthlyHour = document.getElementById('viewMonthlyHour').value;

												if (!monthlyDay || monthlyDay < 1 || monthlyDay > 31) {
													alert("매달 알림의 '일' 설정을 1~31 사이의 숫자로 입력해주세요.");
													return;
												}
												if (!monthlyHour || monthlyHour < 1 || monthlyHour > 24) {
													alert("매달 알림의 '시 마다' 설정을 1~24 사이의 숫자로 입력해주세요.");
													return;
												}
											}
										}





										let alarmTime = null;
										const alarmType = alarmTypeInput.value;

										// alarmType에 따른 alarmTime 설정
										if (alarmType === 'd1') {
											alarmTime = document.getElementById('viewDailyRepeat').value;
										} else if (alarmType === 'd2') {
											alarmTime = document.getElementById('viewWeeklyRepeat').value;
										} else if (alarmType === 'd3') {
											alarmTime = document.getElementById('viewMonthlyHour').value;
										}

										// 요일 체크박스 처리
										const weeklyDaysArray = [];
										document.querySelectorAll('.weekly-checkbox').forEach((checkbox) => {
											if (checkbox.checked) {
												weeklyDaysArray.push(checkbox.value);
											}
										});
										const weeklyDays = weeklyDaysArray.length > 0 ? weeklyDaysArray.join(',') : null;

										const updatedData = {
											schNo: eventId,
											title: document.getElementById('viewTitle').value,
											startDate: document.getElementById('viewStartDate').value + 'T' + document.getElementById('viewStartTime').value,
											endDate: document.getElementById('viewEndDate').value + 'T' + document.getElementById('viewEndTime').value,
											calNo: eventData.calNo,
											alarmType: alarmType,
											alarmYoil: weeklyDays, // d2일 때 요일이 들어가도록
											alarmDay: alarmType === 'd3' ? document.getElementById('viewMonthlyDay').value : null, // d3일 때만 날짜가 들어가도록
											alarmTime: alarmTime,
											isAlarm: alarmCheckbox.checked ? 'f1' : 'f2'
										};

										console.log("Updated Data:", updatedData); // 여기서 `monthlyDay`와 `weeklyDays` 값을 확인




										// 서버에 업데이트된 데이터 전송
										fetch("/sch/schUpdate", {
											method: 'POST',
											headers: {
												'Content-Type': 'application/json'
											},
											body: JSON.stringify(updatedData)
										})
											.then(response => response.json())
											.then(data => {
												if (data.success) {
													alert('일정이 성공적으로 수정되었습니다.');
													console.log(updatedData);

													// UI 초기화
													document.getElementById('viewTitle').readOnly = true;
													document.getElementById('viewStartDate').readOnly = true;
													document.getElementById('viewStartTime').readOnly = true;
													document.getElementById('viewEndDate').readOnly = true;
													document.getElementById('viewEndTime').readOnly = true;
													alarmTypeInput.disabled = true;
													document.getElementById('viewScheduleEditBtn').textContent = '수정';
													document.getElementById('viewScheduleModal').style.display = 'none'; // 모달 닫기

													location.reload();
												} else {
													alert('일정 수정에 실패했습니다.');
												}
											})
											.catch(error => {
												console.log(updatedData);

												console.error('Error updating schedule:', error);
												alert('일정 수정 중 오류가 발생했습니다.');
											});
									};
								};
								//====================================END 수정 ====================================
							} else {
								alert('일정 상세 정보를 불러올 수 없습니다.');
							}
						})
						.catch(error => {
							console.error('Error fetching event details:', error);
							alert('일정 조회 중 오류가 발생했습니다.');
						});



					//====================================END 수정 ====================================

					// 삭제 버튼 클릭 시 이벤트
					var viewScheduleDeleteBtn = document.getElementById('viewScheduleDeleteBtn');
					if (viewScheduleDeleteBtn) {
						viewScheduleDeleteBtn.onclick = function() {
							if (confirm('정말로 이 일정을 삭제하시겠습니까?')) {
								console.log("Deleting schNo:", info.event.id);
								fetch('/sch/schDelete', {  // 삭제 API 호출
									method: 'POST',
									headers: { 'Content-Type': 'application/json' },
									body: JSON.stringify({ schNo: info.event.id })
								})
									.then(response => response.json()) // JSON으로 파싱
									.then(data => {
										alert(data.message); // 서버에서 받은 메시지 표시
										if (data.success) {
											location.reload();
											info.event.remove();  // FullCalendar에서 삭제
											viewScheduleModal.style.display = 'none'; // 모달 닫기
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



					// 일정 추가 모달 닫기 버튼 설정
					var addCancelBtn = document.getElementById('addCancelBtn');
					addCancelBtn.addEventListener('click', function() {
						addScheduleModal.style.display = 'none';
					});




					//캘린더 선택에서  공유캘린더 리스트 일정생성 모달창으로 가져오기//
					// 공유캘린더 리스트를 동적으로 가져와서 업데이트
					fetch('/cal/calAllList')
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






					// 알림 빈도 처리
					var alarmFrequency = document.getElementById('alarmFrequency').value;
					var alarmData = null;

					if (isAlarm) {
						alarmData = {
							frequency: alarmFrequency,
							interval: document.getElementById(alarmFrequency + 'Interval')?.value || null, // 일/주/달 마다 설정
							selectedDays: getSelectedDays(), // 매주일 경우 선택된 요일들
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



					//========================== 특정 입력 필드에서 24를 초과하면 24로 제한하는 함수
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
					//=============================== END 특정 입력 필드에서 24를 초과하면 24로 제한하는 함수	
					//======================= 월에 상관없이 1~31 제한 (일정 생성) ===========================
					// 특정 입력 필드에서 1~31 범위로만 제한하는 함수
					function enforceDayRange(dayInputField) {
						if (!dayInputField) {
							console.warn("dayInputField is not found.");
							return;
						}

						dayInputField.addEventListener('input', function() {
							let dayValue = parseInt(this.value, 10);

							if (dayValue > 31) {
								this.value = 31;  // 31을 초과하면 31로 다시 설정
							} else if (dayValue < 1) {
								this.value = 1;  // 1 미만이면 1로 설정
							}
						});
					}

					// 일정 생성 모달의 월별 일 제한 적용
					const addMonthlyDayInput = document.getElementById('addMonthlyDay');
					if (addMonthlyDayInput) {
						enforceDayRange(addMonthlyDayInput);
					} else {
						console.warn("addMonthlyDay input field is not found.");
					}
					//======================= end 월에 상관없이 1~31 제한 ===========================	





					// 종일 체크 여부
					var isAllDay = document.getElementById('allDay').checked;
					if (isAllDay) {
						endTime = '23:59:59'; // 자정까지
					}







					//							일정양식      제출
					document.getElementById('scheduleForm').onsubmit = function(e) {
						e.preventDefault();

						// 제목 필드 확인
						var title = document.getElementById('title').value;
						if (!title.trim()) {  // 제목이 비어있을 경우
							alert('일정 제목을 입력해주세요.');
							return;  // 함수를 종료하여 제출을 막음
						}

						// 알림 설정 여부 (f1: 사용, f2: 미사용)
						var isAlarm = document.getElementById('isAlarm').checked ? 'f1' : 'f2';
						console.log("isAlarm 값:", isAlarm);  // 'f1' 또는 'f2'인지 확인

						// 알림 빈도 및 기타 설정
						var alarmFrequency = document.getElementById('alarmFrequency').value;
						var alarmInterval = document.getElementById(alarmFrequency + 'Interval')?.value || null;
						var selectedDays = getSelectedDays();

						// 알림 타입 변환 (d1: 매일, d2: 매주, d3: 매달)
						var alarmType = null;
						if (isAlarm === 'f1') {
							if (alarmFrequency === 'daily') {
								alarmType = 'd1';
							} else if (alarmFrequency === 'weekly') {
								alarmType = 'd2';
							} else if (alarmFrequency === 'monthly') {
								alarmType = 'd3';
							}
						}

						// 예외 처리 추가
						if (isAlarm === 'f1') {
							if (alarmType === 'd1') { // 매일 알림 예외 처리
								const dailyRepeat = document.getElementById('dailyInterval').value;
								if (!dailyRepeat || dailyRepeat < 1 || dailyRepeat > 24) {
									alert("매일 알림의 '시 마다' 설정을 1~24 사이의 숫자로 입력해주세요.");
									return;
								}
							} else if (alarmType === 'd2') { // 매주 알림 예외 처리
								const weeklyRepeat = document.getElementById('weeklyHour').value;
								const selectedDays = Array.from(document.querySelectorAll('.weekly-checkbox:checked')).map(cb => cb.value);

								if (selectedDays.length === 0) {
									alert("매주 알림의 요일을 선택해주세요.");
									return;
								}
								if (!weeklyRepeat || weeklyRepeat < 1 || weeklyRepeat > 24) {
									alert("매주 알림의 '시 마다' 설정을 1~24 사이의 숫자로 입력해주세요.");
									return;
								}
							} else if (alarmType === 'd3') { // 매달 알림 예외 처리
								const monthlyDay = document.getElementById('monthlyDay').value;
								const monthlyHour = document.getElementById('monthlyHour').value;

								if (!monthlyDay || monthlyDay < 1 || monthlyDay > 31) {
									alert("매달 알림의 '일' 설정을 1~31 사이의 숫자로 입력해주세요.");
									return;
								}
								if (!monthlyHour || monthlyHour < 1 || monthlyHour > 24) {
									alert("매달 알림의 '시 마다' 설정을 1~24 사이의 숫자로 입력해주세요.");
									return;
								}
							}
						}

						// alarm_time 설정 (d1, d2, d3 각각의 경우에 대해 설정)
						// alarm_day 및 alarm_time 설정
						var alarmDay = null; // 일수 
						var alarmTime = null; // 시간 
						var alarmYoil = null; // 요일 
						if (alarmType === 'd1') {
							alarmTime = document.getElementById('dailyInterval')?.value || null; // d1 (매일)일 경우 시간 설정
						} else if (alarmType === 'd2') {
							alarmYoil = selectedDays.length > 0 ? selectedDays.join(",") : null; // d2 (매주)일 경우 요일 설정 (쉼표로 구분된 문자열)
							alarmTime = document.getElementById('weeklyHour')?.value || null; // d2 (매주)일 경우 시간 설정
						} else if (alarmType === 'd3') {
							alarmDay = document.getElementById('monthlyDay')?.value || null;  // d3 (매달)일 경우 일자 설정
							alarmTime = document.getElementById('monthlyHour')?.value || null; // d3 (매달)일 경우 시간 설정
						}


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
							isAlarm: isAlarm,
							alarmType: alarmType,
							alarmInterval: isAlarm === 'f1' ? alarmInterval : null,
							alarmYoil: alarmYoil, // d2 (매주)일 경우 요일 정보
							alarmDay: alarmDay,
							alarmTime: alarmTime // d1, d2, d3 모두에서 설정된 alarmTime
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
									console.log("isAlarm 값:", isAlarm);  // 'f1' 또는 'f2'인지 확인

									if (data.success) {
										alert('일정 등록에 성공했습니다.');
										addScheduleModal.style.display = 'none'; // 모달 닫기
										location.reload(); //화면엔 바로뜨는데 값같은거 바로 안들어가서 그냥 새로고침 
										// 추가된 이벤트가 전체 일정을 갱신하도록 이벤트 재로딩
										calendar.refetchEvents();
										calendar.unselect(); // FullCalendar의 선택 해제
									} else {
										console.log(data); // 데이터를 확인하기 위한 로그 출력
										alert('일정 등록에 실패했습니다.');
									}
								})
								.catch(error => console.error('Error:', error));
						}

					};
				}
				//============END 일정생성 ============
			});
			//=====================사이드바 내캘리더 선택시 리스트 뿌리기 =========================
			document.getElementById('personalCalendar').onclick = function() {
				fetchSoloCalendarEvents();
			};

			// g1 타입(내 캘린더) 일정만 가져오는 함수
			function fetchSoloCalendarEvents() {
				fetch('/sch/schSoloList', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					}
				})
					.then(response => response.json())
					.then(data => {
						// 풀캘린더 형식에 맞게 변환
						const events = data.map(event => ({
							id: event.schNo,
							title: event.title,
							start: event.startDate,
							end: event.endDate,
							color: event.color  // 일정 색상
						}));

						// 기존 이벤트 제거 후 새로 로드
						$('#calendar').fullCalendar('removeEvents');  // 기존 이벤트 삭제
						$('#calendar').fullCalendar('renderEvents', events, true);  // 새 일정 렌더링
					})
					.catch(error => console.error('Error fetching solo calendar events:', error));
			}












			calendar.render();
			//============ 풀캘린더API ============


			//============================사이드바 선택시 그 타입 조회 ==================================// 각 타입별 필터링 상태와 선택된 g2 하위 캘린더 항목을 추적
			let isFiltered = { g1: false, g2: false, g3: false };
			let selectedG2CalNos = new Set(); // g2에서 여러 calNo를 저장하기 위한 Set

			function applyFilters() {
				// 필터 상태에 따라 각 조건에 맞는 이벤트를 모으기
				let filteredEvents = [];

				if (isFiltered.g1) {
					filteredEvents = filteredEvents.concat(sList.filter(event => event.calType === 'g1'));
				}

				if (isFiltered.g2) {
					selectedG2CalNos.forEach(calNo => {
						filteredEvents = filteredEvents.concat(sList.filter(event => event.calType === 'g2' && event.calNo == calNo));
					});
				}

				if (isFiltered.g3) {
					filteredEvents = filteredEvents.concat(sList.filter(event => event.calType === 'g3'));
				}

				console.log("Applying filters, displaying events:", filteredEvents);

				calendar.removeAllEvents();
				calendar.addEventSource(filteredEvents.length > 0 ? filteredEvents : sList);
			}

			// g1 클릭 이벤트
			document.getElementById('personalCalendar').onclick = () => {
				isFiltered.g1 = !isFiltered.g1; // 상태 토글
				document.getElementById('personalCalendar').classList.toggle('active-filter', isFiltered.g1); // 스타일 토글
				console.log(`g1 filter is now ${isFiltered.g1}`);
				applyFilters();
			};

			// g2 클릭 이벤트 (하위 리스트 표시 토글)
			document.getElementById('sharedCalendar').onclick = (event) => {
				const g2List = document.getElementById('sharedCalendarList');
				g2List.style.display = g2List.style.display === 'none' ? 'block' : 'none';
				console.log("Toggled g2 list display");
				event.stopPropagation();
			};

			// g2 하위 리스트 클릭 이벤트 (특정 calNo별 필터링)
			document.querySelectorAll('#sharedCalendarList .calendar-item').forEach(item => {
				item.onclick = (event) => {
					const calNo = item.getAttribute('data-calno');
					console.log("Clicked calNo:", calNo);

					event.stopPropagation();

					if (selectedG2CalNos.has(calNo)) {
						selectedG2CalNos.delete(calNo); // 이미 선택된 경우 제거
						item.classList.remove('active-filter'); // 스타일 제거
						if (selectedG2CalNos.size === 0) isFiltered.g2 = false; // 선택이 모두 해제되면 g2 필터 해제
					} else {
						selectedG2CalNos.add(calNo); // 새로 선택된 경우 추가
						item.classList.add('active-filter'); // 스타일 추가
						isFiltered.g2 = true;
					}

					applyFilters();
				};
			});

			// g3 클릭 이벤트
			document.getElementById('projectCalendar').onclick = () => {
				isFiltered.g3 = !isFiltered.g3; // 상태 토글
				document.getElementById('projectCalendar').classList.toggle('active-filter', isFiltered.g3); // 스타일 토글
				console.log(`g3 filter is now ${isFiltered.g3}`);
				applyFilters();
			};



			//============================end 사이드바 선택시 그 타입 조회 ==================================

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


	//============ 사이드바 내 캘린더 클릭시 풀캘린더에 g1만 보이도록 하기===============


	//============ 사이드바 공유캘린더 리스트===========

	function loadSharedCalendars() {
		fetch('/cal/calTeamList')
			.then(response => response.json())
			.then(data => {
				const sharedCalendarList = document.getElementById('sharedCalendarList');
				sharedCalendarList.innerHTML = '';  // 기존 목록 초기화
				data.forEach(calendar => {
					const newCalendarItem = document.createElement('li');
					newCalendarItem.classList.add('sidebar-item', 'calendar-item-wrapper');
					newCalendarItem.innerHTML = makeSidEvent(calendar); //html 양식 맨아래 함수로 지
					sharedCalendarList.appendChild(newCalendarItem);

					//============ 사이드바 내 캘린더 수정 ==============



					// 연필 아이콘 클릭 이벤트 설정
					const editIcon = newCalendarItem.querySelector('.edit-icon');
					editIcon.addEventListener('click', function() {
						const calendarItem = this.parentElement;
						const selectedCalNo = calendarItem.querySelector('.calendar-item').getAttribute('data-calno');
						const calendarName = calendarItem.querySelector('.cal_name').value;
						const calendarIcon = calendarItem.querySelector('i');
						const calendarColor = window.getComputedStyle(calendarIcon).color;

						console.log("Current color in calendar (calendarColor):", calendarColor); // 확인용 로그


						// 선택된 calNo 값을 hidden input에 설정
						document.getElementById('selectedCalNo').value = selectedCalNo;

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


						// 부서와 사원 목록 로드 및 선택된 참여자 표시
						loadDeptAndEmpForEditModal(selectedCalNo);  // 수정하려는 캘린더의 calNo 전달

						// 모달 열기
						document.getElementById('editCalendarModal').style.display = 'block';
					});

					// 모달 닫기 버튼 처리
					document.querySelectorAll('.close').forEach(closeBtn => {
						closeBtn.addEventListener('click', function() {
							const modal = this.closest('.modal');
							modal.style.display = 'none'; // 모달 닫기
						});
					});

					document.querySelectorAll('input[type="radio"][name="color"]').forEach(radio => {
						radio.addEventListener('change', (event) => {
							// 이벤트가 발생할 때 실행할 코드
							document.querySelector("#selColor").value = event.target.value;
						});
					});
					document.getElementById('editCalendarForm').onsubmit = function(e) {
						e.preventDefault();

						const calNo = document.getElementById('selectedCalNo').value;
						const name = document.getElementById('editCalendarName').value;
						const color = document.getElementById('selColor').value;  // 선택된 색상 값 가져오기

						console.log("Selected color:", color);

						// 선택된 참여자 목록을 배열로 수집하고 "members"라는 키로 사용
						const members = Array.from(document.getElementById('editSelectedParticipantsList').children)
							.map(participant => participant.dataset.empno);

						console.log(members); // 전송하기 전에 members 배열 확인

						// 서버로 수정 요청 보내기
						fetch('/cal/calUpdate', {
							method: 'POST',
							headers: { 'Content-Type': 'application/json' },
							body: JSON.stringify({ calNo: calNo, name: name, color: color, members: members })

						})
							.then(response => response.json())
							.then(data => {
								console.log("Payload data:", { calNo: calNo, name: name, color: color, members: members });

								console.log(color);
								if (data.result) {
									alert('캘린더 수정이 성공적으로 완료되었습니다.');
									location.reload();  // 새로고침하여 변경 사항 반영
								} else {
									console.log(data);
									alert('캘린더 수정에 실패했습니다.');
								}
							})
							.catch(error => console.error('Error:', error));
					};

					//============END 사이드바 내 캘린더 수정 ==============

				});
			})
			.catch(error => console.log('Error loading calendars:', error));
	}
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=3333333333333333333333333333333333333333333333

	//123333213333333333333333333333333333333333333333333333333333333333
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




	// 중복 체크용 Set 생성
	const selectedEmpNos = new Set();

	// 페이지 로드 시 부서와 사원 데이터를 한 번에 가져오기

	fetch("/cal/deptWithEmp")
		.then(response => response.json())
		.then(data => {

			const deptListContainer = document.getElementById("sharedCalendarDeptList");
			deptListContainer.innerHTML = ""; // 중복 방지를 위해 리스트 초기화

			// 부서별로 사원을 저장할 객체 생성
			const deptEmployeeMap = {};

			data.forEach(item => {
				const deptNo = item.DEPT_NO;
				const deptName = item.DEPT_NAME;
				const empNo = item.EMP_NO;
				const empName = item.EMP_NAME;

				// 부서 항목이 없으면 생성
				if (!deptEmployeeMap[deptNo]) {
					deptEmployeeMap[deptNo] = {
						deptName: deptName,
						employees: []
					};
				}

				// 사원이 있으면 목록에 추가
				if (empNo) {
					deptEmployeeMap[deptNo].employees.push({ empNo, empName });
				}
			});

			// 부서 목록 렌더링
			Object.keys(deptEmployeeMap).forEach(deptNo => {
				const deptItem = document.createElement("li");
				deptItem.classList.add("dept-item");
				deptItem.textContent = deptEmployeeMap[deptNo].deptName;
				deptItem.dataset.deptno = deptNo;

				// 부서 클릭 시 사원 목록 표시
				deptItem.onclick = function() {
					const employeeContainer = document.getElementById("employeeContainer");
					employeeContainer.innerHTML = ''; // 이전 사원 목록 초기화

					const employeeListUl = document.createElement("ul");
					employeeListUl.classList.add("employee-list");

					deptEmployeeMap[deptNo].employees.forEach(employee => {
						const empItem = document.createElement("li");
						empItem.classList.add("emp-item");
						empItem.dataset.empno = employee.empNo;

						// 사원 이름 span
						const empNameSpan = document.createElement("span");
						empNameSpan.textContent = employee.empName;

						// 체크 표시 span
						const checkIcon = document.createElement("span");
						checkIcon.classList.add("check-icon");
						checkIcon.textContent = "✔️";
						checkIcon.style.visibility = selectedEmpNos.has(employee.empNo) ? "visible" : "hidden";

						// empItem에 사원 이름과 체크 표시 추가
						empItem.appendChild(empNameSpan);
						empItem.appendChild(checkIcon);

						// empItem 클릭 시 참여자 목록에 추가 또는 삭제
						empItem.onclick = function() {
							const selectedList = document.getElementById("selectedParticipantsList");

							if (!selectedEmpNos.has(employee.empNo)) {
								// Set에 추가
								selectedEmpNos.add(employee.empNo);

								// 참여자 목록에 추가
								const participant = document.createElement("li");
								participant.textContent = employee.empName;
								participant.dataset.empno = employee.empNo;

								// 참여자 클릭 시 목록에서 삭제
								participant.onclick = function() {
									selectedEmpNos.delete(employee.empNo);  // Set에서 삭제
									participant.remove();  // UI에서 삭제

									// 체크 표시 제거
									const currentEmpItem = document.querySelector(`[data-empno="${employee.empNo}"]`);
									if (currentEmpItem) {
										const currentCheckIcon = currentEmpItem.querySelector(".check-icon");
										currentCheckIcon.style.visibility = "hidden";
									}
								};

								selectedList.appendChild(participant);

								// 체크 표시 추가
								checkIcon.style.visibility = "visible";
							} else {
								console.log(`${employee.empName} already in participants list`);
							}
						};

						employeeListUl.appendChild(empItem);
					});

					employeeContainer.appendChild(employeeListUl);
				};

				deptListContainer.appendChild(deptItem);
			});

		})

		.catch(error => console.error("Error fetching department and employee list:", error));





	// 모달 폼 제출 처리
	document.getElementById('sharedCalendarForm').onsubmit = function(e) {
		e.preventDefault();

		// 입력받은 캘린더 정보 가져오기
		const sharedCalendarName = document.getElementById('sharedCalendarName').value;
		const selectedColor = document.querySelector('input[name="color"]:checked').value;
		const members = Array.from(document.getElementById('selectedParticipantsList').children)
			.map(li => li.getAttribute('data-empno'));  // 참여자 목록을 배열로 추출
		console.log(members);
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
				isDelete: 'h2', // 기본값으로 'h2' 설정
				members: members // 참여자 목록 추가
			})
		})
			.then(response => response.json())
			.then(data => {

				if (data.success) {
					const sharedCalendarList = document.getElementById('sharedCalendarList');
					const newCalendarItem = document.createElement('li');
					newCalendarItem.classList.add('sidebar-item', 'calendar-item-wrapper');
					newCalendarItem.innerHTML = makeSidEvent(data.calInfo); // data로 전달
					sharedCalendarList.appendChild(newCalendarItem);
				} else {
					console.log(data);
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


	//============END 사이드바 공유 캘린더 생성 ===========
	//============ 사이드바 휴지통 보내기  ===========
	// 삭제 버튼 클릭 시 이벤트 
	document.getElementById('deleteBtn').onclick = function() {
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
				newTrashItem.classList.add('sidebar-item', 'trash-item', 'calendar-item-wrapper');
				newTrashItem.setAttribute('data-calno', calendar.calNo);
				newTrashItem.innerHTML = makeSidEvent(calendar);
				wasteCalendarList.appendChild(newTrashItem);
				//========================= 휴지통 리스트 옆 연필 아이콘 클릭 모달 ==================================
				const editIcon = newTrashItem.querySelector('.edit-icon');
				editIcon.addEventListener('click', function() {
					const calendarItem = this.closest('.trash-item');
					const selectedCalNo = calendarItem.getAttribute('data-calno');
					const calendarName = calendarItem.querySelector('.cal_name').value;

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
	document.getElementById('restoreCalendarBtn').onclick = function() {
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
	};



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
				.then(response => response.text())
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

	// 영구 삭제 버튼 클릭 이벤트 등록
	document.getElementById('permanentlyDeleteBtn').onclick = handlePermanentlyDelete;


	//============ 사이드바 휴지통 완전삭제 =============

	//============ 사이드바 휴지통 리스트  ===========

	//============END 사이드바 휴지통  ===========




	loadSharedCalendars(); //사이드바 맨위
	//============END 사이드바 ============


	//location.reload();  //페이지 새로고침 ㅎㅎㅎㅎ
	//=================================함수모음======================================
	// 오늘 날짜를 YYYY-MM-DD 형식으로 반환하는 함수
	function getTodayDate() {
		return new Date().toISOString().slice(0, 10);
	}

	//사이드바  캘린더 리스트 생성 뿌리기 
	function makeSidEvent(calendar) {
		let truncatedName = calendar.name.length > 8 ? calendar.name.substring(0, 8) + '...' : calendar.name;

		let tag =
			`
	        <input type="hidden" class="cal_name" value="${calendar.name}" />
	        <a href="javascript:void(0)" data-calno="${calendar.calNo}" class="sidebar-link calendar-item">
	            <i class="mdi mdi-calendar-blank" style="color:${calendar.color};"></i> ${truncatedName}
	        </a>
	        <span class="hide-menu edit-icon" style="margin-left: 10px;">
	            <i class="mdi mdi-pencil" aria-hidden="true"></i>
	        </span>
	    `;
		return tag;
	}



	// 모든 필드를 읽기 전용으로 설정하는 함수 단건조회수정에서 취소버튼 누르고 다시 단건조회하는데 수정버튼 누르지도 않았는데 리드온리 풀려서 넣어봄 
	function resetFieldsToReadonly() {
		document.getElementById('viewTitle').readOnly = true;
		document.getElementById('viewStartDate').readOnly = true;
		document.getElementById('viewStartTime').readOnly = true;
		document.getElementById('viewEndDate').readOnly = true;
		document.getElementById('viewEndTime').readOnly = true;

		// 알림 관련 필드 비활성화
		document.getElementById('viewAlarmType').disabled = true;
		document.getElementById('viewDailyRepeat').readOnly = true;
		document.querySelectorAll('.weekly-checkbox').forEach(cb => cb.disabled = true);
		document.getElementById('viewWeeklyRepeat').readOnly = true;
		document.getElementById('viewMonthlyDay').readOnly = true;
		document.getElementById('viewMonthlyHour').readOnly = true;

		// 알림 체크박스 비활성화
		var alarmCheckbox = document.getElementById('viewAlarmUse');
		alarmCheckbox.disabled = true;
	}


	// 초기화 함수: 알림 관련 필드들을 초기화 단건조회 에서 수정선택 후 d1 d2 d3 값 중복으로 벨류로 넘어가는거 방지
	function resetAlarmFields() {
		document.getElementById('viewDailyRepeat').value = ''; // d1 - 매일 설정 초기화
		document.querySelectorAll('.weekly-checkbox').forEach(cb => cb.checked = false); // d2 - 요일 체크박스 초기화
		document.getElementById('viewWeeklyRepeat').value = ''; // d2 - 시간 설정 초기화
		document.getElementById('viewMonthlyDay').value = ''; // d3 - 매달 일자 초기화
		document.getElementById('viewMonthlyHour').value = ''; // d3 - 시간 초기화
	}




	// 상태 초기화 함수
	function resetModal() {
		selectedEmpNos.clear(); // Set 비우기
		document.getElementById("selectedParticipantsList").innerHTML = ""; // 참여자 목록 초기화
		document.getElementById("employeeContainer").innerHTML = ""; // 사원 목록 초기화
		document.getElementById("sharedCalendarDeptList").innerHTML = ""; // 부서 목록 초기화
		document.getElementById("calendarNameInput").value = ""; // 캘린더 이름 초기화

		fetchDataAndRender(); // 부서와 사원 목록 다시 로드
	}

	//=============================test
	// 중복 체크용 Set 생성
	const selectedEditEmpNos = new Set();

	// 수정 모달에서 부서 및 사원 목록 로드
	function loadDeptAndEmpForEditModal(calNo) {
		fetch(`/cal/deptWithEmp`)
			.then(response => response.json())
			.then(data => {
				const deptListContainer = document.getElementById("editSharedCalendarDeptList");
				const empSelectContainer = document.getElementById("editEmployeeContainer");
				const participantList = document.getElementById("editSelectedParticipantsList");

				deptListContainer.innerHTML = ''; // 부서 초기화
				empSelectContainer.innerHTML = ''; // 사원 초기화
				participantList.innerHTML = ''; // 참여자 초기화
				selectedEditEmpNos.clear(); // 선택된 사원 초기화

				// 선택된 캘린더의 기본 참여자 로드
				fetch(`/cal/getCalDeptEmpInfo?calNo=${calNo}`)
					.then(response => response.json())
					.then(selectedParticipants => {
						console.log("Loaded participants:", selectedParticipants); // 로그 확인

						// 선택된 참여자 empNo를 Set에 추가하고, UI에 표시
						selectedParticipants.forEach(participant => {
							// 모든 키를 소문자로 변환하여 새로운 객체 생성
							const normalizedParticipant = Object.fromEntries(
								Object.entries(participant).map(([key, value]) => [key.toLowerCase(), value])
							);


							// emp_name과 empno 필드로 접근
							const empName = normalizedParticipant['empname'];
							const empNo = normalizedParticipant['empno'];


							// empName이 유효한 경우에만 추가
							if (empName && empName.trim().length > 0) {
								selectedEditEmpNos.add(empNo);

								const participantItem = document.createElement("li");
								participantItem.textContent = empName;
								participantItem.dataset.empno = empNo;

								participantItem.onclick = function() {
									selectedEditEmpNos.delete(empNo);
									participantItem.remove();

									const currentEmpItem = document.querySelector(`[data-empno="${empNo}"]`);
									if (currentEmpItem) {
										const currentCheckIcon = currentEmpItem.querySelector(".check-icon");
										if (currentCheckIcon) {
											currentCheckIcon.style.visibility = "hidden";
										}
									}
								};

								participantList.appendChild(participantItem);  // 참여자 목록에 추가
							} else {
								console.warn("Participant without valid name:", normalizedParticipant);
							}
						});


						// 부서 목록 추가
						const deptEmployeeMap = organizeDeptEmployeeMap(data); // 부서와 사원 데이터 조직화
						displayDeptEmployeeList(deptEmployeeMap, deptListContainer, empSelectContainer, participantList);
					})
					.catch(error => console.error("참여자 기본 값 불러오기 에러:", error));
			})
			.catch(error => console.error("부서 및 사원 목록 불러오기 에러:", error));
	}

	// 부서와 사원 데이터를 조직화하는 함수
	function organizeDeptEmployeeMap(data) {
		const deptEmployeeMap = {};
		data.forEach(item => {
			const { DEPT_NO: deptNo, DEPT_NAME: deptName, EMP_NO: empNo, EMP_NAME: empName } = item;
			if (!deptEmployeeMap[deptNo]) {
				deptEmployeeMap[deptNo] = { deptName, employees: [] };
			}
			if (empNo) {
				deptEmployeeMap[deptNo].employees.push({ empNo, empName });
			}
		});
		return deptEmployeeMap;
	}

	// 부서 및 사원 목록을 UI에 렌더링
	function displayDeptEmployeeList(deptEmployeeMap, deptListContainer, empSelectContainer, participantList) {
		console.log("Department Employee Map:", deptEmployeeMap); // 부서-사원 정보 확인 로그

		Object.keys(deptEmployeeMap).forEach(deptNo => {
			const deptItem = document.createElement("li");
			deptItem.classList.add("dept-item");
			deptItem.textContent = deptEmployeeMap[deptNo].deptName;
			deptItem.dataset.deptno = deptNo;

			// 부서 클릭 시 사원 목록 표시
			deptItem.onclick = function() {
				empSelectContainer.innerHTML = ''; // 사원 초기화
				const employeeListUl = document.createElement("ul");
				employeeListUl.classList.add("employee-list");

				deptEmployeeMap[deptNo].employees.forEach(employee => {
					const empItem = createEmployeeItem(employee, participantList);
					employeeListUl.appendChild(empItem);
				});

				empSelectContainer.appendChild(employeeListUl);
			};

			deptListContainer.appendChild(deptItem);
		});
	}

	// 사원 항목 생성 함수
	function createEmployeeItem(employee, participantList) {
		const empItem = document.createElement("li");
		empItem.classList.add("emp-item");
		empItem.dataset.empno = employee.empNo;

		const empNameSpan = document.createElement("span");
		empNameSpan.textContent = employee.empName;

		const checkIcon = document.createElement("span");
		checkIcon.classList.add("check-icon");
		checkIcon.textContent = "✔️";

		// selectedEditEmpNos에 있는 사원이라면 체크 아이콘 표시
		checkIcon.style.visibility = selectedEditEmpNos.has(employee.empNo) ? "visible" : "hidden";

		empItem.appendChild(empNameSpan);
		empItem.appendChild(checkIcon);

		empItem.onclick = function() {
			toggleEmployeeSelection(employee, empItem, checkIcon, participantList);
		};

		return empItem;
	}


	// 사원 선택 및 해제 처리 함수
	function toggleEmployeeSelection(employee, empItem, checkIcon, participantList) {
		if (!selectedEditEmpNos.has(employee.empNo)) {
			selectedEditEmpNos.add(employee.empNo);

			const participant = document.createElement("li");
			participant.textContent = employee.empName;
			participant.dataset.empno = employee.empNo;

			participant.onclick = function() {
				selectedEditEmpNos.delete(employee.empNo);
				participant.remove();

				// 사원 리스트에서 체크 아이콘 숨김
				checkIcon.style.visibility = "hidden";
			};

			participantList.appendChild(participant);
			checkIcon.style.visibility = "visible"; // 체크 아이콘 표시
		} else {
			console.log(`${employee.empName} 이미 선택됨`);
		}
	}

	// 모달 열릴 때마다 사원 목록 초기화 및 로드
	document.getElementById("editCalendarModal").addEventListener("show", function() {
		loadDeptAndEmpForEditModal();
	});
});
//================================END 함수모음====================================
