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
		var modal = document.getElementById('scheduleModal');
		modal.style.display = 'block';  // 모달 열기
	});

	// 일정 생성 모달창 취소 버튼 클릭 시 모달 닫기
	document.getElementById('cancelBtn').addEventListener('click', function() {
		var modal = document.getElementById('scheduleModal');
		modal.style.display = 'none'; // 모달 닫기
	});

	// 전역에서 모달 외부 클릭 시 닫기 처리
	window.onclick = function(event) {
		var scheduleModal = document.getElementById('scheduleModal');
		var viewModal = document.getElementById('viewScheduleModal');
		var editModal = document.getElementById('editScheduleModal');

		// 클릭된 대상이 각각의 모달일 경우에만 닫기
		if (event.target == scheduleModal) {
			scheduleModal.style.display = 'none';
		}
		if (event.target == viewModal) {
			viewModal.style.display = 'none';
		}
		if (event.target == editModal) {
			editModal.style.display = 'none';
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
				events: sList, //풀캘린더 리스트



				//============ 단건조회 ============
				eventClick: function(info) {
					// 조회 모달 창 열기
					var viewModal = document.getElementById('viewScheduleModal');
					document.getElementById('viewCalendar').value = info.event.extendedProps.calendarType || '없음';
					document.getElementById('viewTitle').value = info.event.title;
					document.getElementById('viewStartDate').value = info.event.startStr.split('T')[0];
					document.getElementById('viewStartTime').value = info.event.startStr.split('T')[1];
					document.getElementById('viewEndDate').value = info.event.endStr ? info.event.endStr.split('T')[0] : '';
					document.getElementById('viewEndTime').value = info.event.endStr ? info.event.endStr.split('T')[1] : '';
					''
					viewModal.style.display = 'block'; // 조회 모달 보이기

					// 취소 버튼 클릭 시 모달 닫기
					document.getElementById('viewScheduleModal').querySelector('.close').addEventListener('click', function() {
						viewModal.style.display = 'none'; // 모달 닫기
					});

					//==================================== 수정 ====================================
					// 수정 버튼 클릭 시
					var editBtn = document.getElementById('editBtn');
					editBtn.onclick = function() {
						// 단건조회 모달 닫기
						viewModal.style.display = 'none';
						// 수정 모달 열기
						var editModal = document.getElementById('editScheduleModal');
						document.getElementById('editCalendarType').value = info.event.extendedProps.calendarType || '내캘린더';
						document.getElementById('editTitle').value = info.event.title;
						document.getElementById('editStartDate').value = info.event.startStr.split('T')[0];
						document.getElementById('editStartTime').value = info.event.startStr.split('T')[1];
						document.getElementById('editEndDate').value = info.event.endStr ? info.event.endStr.split('T')[0] : '';
						document.getElementById('editEndTime').value = info.event.endStr ? info.event.endStr.split('T')[1] : '';

						editModal.style.display = 'block'; // 수정 모달 보이기

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
										editModal.style.display = 'none';

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
					var deleteBtn = document.getElementById('deleteBtn');
					deleteBtn.onclick = function() {
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
										viewModal.style.display = 'none'; // 모달 닫기
									} else {
										alert('일정 삭제에 실패했습니다.');
									}
								})
								.catch(error => console.error('Error:', error));
						}
					};
				},
				//============END 단건조회 ============

				//============ 일정생성 ============
				select: function(arg) {
					// 일정 추가 모달 창 열기
					var modal = document.getElementById('scheduleModal');
					var form = document.getElementById('scheduleForm');

					//============ 사이드바 ============
					var selectedCalNo = null;  // 사이드바에서 선택된 캘린더 ID

					// 사이드바에서 캘린더 선택 시 cal_no 저장 (여기에 사이드바 캘린더 선택 코드 필요)
					document.querySelectorAll('.calendar-item').forEach(item => {
						item.addEventListener('click', function() {
							selectedCalNo = this.getAttribute('data-calno');  // 선택된 캘린더 ID 가져오기
							alert('선택된 캘린더: ' + this.textContent + ' (ID: ' + selectedCalNo + ')');
						});
					});


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



					// 반복 체크박스 클릭 시 필드 표시
					document.getElementById('isRepeat').addEventListener('change', function() {
						var repeatFields = document.getElementById('repeatFields');
						if (this.checked) {
							repeatFields.style.display = 'block';
						} else {
							repeatFields.style.display = 'none';
						}
					});

					// 반복 빈도 선택에 따라 추가 필드 표시
					document.getElementById('repeatFrequency').addEventListener('change', function() {
						var dailyRepeat = document.getElementById('dailyRepeat');
						var weeklyRepeat = document.getElementById('weeklyRepeat');
						var monthlyRepeat = document.getElementById('monthlyRepeat');


						// 모든 반복 필드를 숨김
						dailyRepeat.style.display = 'none';
						weeklyRepeat.style.display = 'none';
						monthlyRepeat.style.display = 'none';


						// 선택된 반복 빈도에 따라 필드를 보여줌
						if (this.value === 'daily') {
							dailyRepeat.style.display = 'block';
						} else if (this.value === 'weekly') {
							weeklyRepeat.style.display = 'block';
						} else if (this.value === 'monthly') {
							monthlyRepeat.style.display = 'block';
						}
					});




					modal.style.display = 'block'; // 모달 보이기



					// 폼 제출 처리
					form.onsubmit = function(e) {
						e.preventDefault();

						// 폼 데이터 가져오기
						var title = document.getElementById('title').value;
						var startDate = document.getElementById('startDate').value;
						var startTime = document.getElementById('startTime').value;
						var endDate = document.getElementById('endDate').value;
						var endTime = document.getElementById('endTime').value;

						// 시작일과 종료일에 시간을 추가하여 하나의 datetime으로 변환
						var startDateTime = new Date(startDate + 'T' + startTime).toISOString(); // ISO 형식으로 변환
						var endDateTime = new Date(endDate + 'T' + endTime).toISOString(); // ISO 형식으로 변환

						// 선택된 캘린더가 없으면 경고 메시지
						if (!selectedCalNo) {
							alert('캘린더를 먼저 선택해주세요!');
							return;
						}

						// 일정생성 
						if (title) {
							fetch('/sch/schInsert', {  // 등록 API 호출
								method: 'POST',
								headers: {
									'Content-Type': 'application/json',
								},
								body: JSON.stringify({
									title: title,
									startDate: startDateTime,
									endDate: endDateTime,
									calNo: selectedCalNo,  // 선택된 캘린더 ID 전송
									allDay: arg.allDay
								})
							})
								.then(response => response.json())
								.then(data => {
									if (data.success) {

										calendar.addEvent({
											id: data.id,
											title: title,
											start: new Date(startDate + 'T' + startTime), // 여기에서 Date 객체로 변환
											end: new Date(endDate + 'T' + endTime), // 여기에서 Date 객체로 변환
											allDay: arg.allDay,



										});
									} else {
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
	//============ END 캘린더 전체조회 ============



	//============ 사이드바 ============





	//============ 사이드바 리스트===========

	// 서버에서 캘린더 목록을 불러와서 사이드바에 추가하는 함수
	function loadCalendars() {
		fetch('/cal/calList')
			.then(response => response.json())
			.then(data => {
				const personalCalendarList = document.getElementById('personalCalendarList'); //개인캘린더리스트
				personalCalendarList.innerHTML = '';  // 기존 목록을 초기화

				data.forEach(calendar => {
					//li추가
					const newCalendarItem = document.createElement('li');
					newCalendarItem.innerHTML = `
                        <div class="calendar-item-wrapper" style="display: flex; align-items: center;">
                            <a href="javascript:void(0)" data-calno="${calendar.calNo}" class="calendar-item">
                                <i class="mdi mdi-calendar-blank" style="color:${calendar.color};"></i> ${calendar.name}
                            </a>
                            <span class="edit-icon" style="margin-left: 10px;">
                                <i class="mdi mdi-pencil" aria-hidden="true"></i>
                            </span>
                        </div>`;
					personalCalendarList.appendChild(newCalendarItem);

					//============ 사이드바 내 캘린더 수정 ==============
					// 연필 아이콘 클릭 이벤트 바로 추가
					const editIcon = newCalendarItem.querySelector('.edit-icon');
					editIcon.addEventListener('click', function() {
						const editModal = document.getElementById('editCalendarModal');
						if (editModal) {
							editModal.style.display = 'block';  // 모달을 열기
						} else {
							console.error('Edit modal not found');
						}
					});
					//============END 사이드바 내 캘린더 수정 ==============
				});
			})
			.catch(error => console.error('Error loading calendars:', error));
	}



	// 페이지 로드 시 캘린더 목록 불러오기
	loadCalendars();

	//============ END 사이드바 리스트===========
	//============ 사이드바 내 캘린더 생성 ===========
	// 내 캘린더 추가 모달 열기/닫기
	document.getElementById('addPersonalCalendarBtn').addEventListener('click', function() {
		document.getElementById('personalCalendarModal').style.display = 'block';
	});

	document.getElementById('cancelPersonalCalendar').addEventListener('click', function() {
		document.getElementById('personalCalendarModal').style.display = 'none';
	});





	// 모달 폼 제출 처리
	document.getElementById('addPersonalCalendarForm').onsubmit = function(e) {
		e.preventDefault();

		// 입력받은 캘린더 정보 가져오기
		const calendarName = document.getElementById('calendarName').value;
		const selectedColor = document.querySelector('input[name="color"]:checked').value;

		// 입력 값 검증
		if (!calendarName || !selectedColor) {
			alert('캘린더 이름과 색상을 선택해주세요.');
			return;
		}



		// 서버에 캘린더 정보 저장 요청
		fetch('/cal/calInsert', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({
				name: calendarName,
				color: selectedColor,
				type: 'g1', // 개인 캘린더 타입
				isDelete: 'h2' // 기본값으로 'h2' 설정
			})
		})
			.then(response => response.json())
			.then(data => {
				if (data.success) {
					// 캘린더가 성공적으로 추가되었을 때만 사이드바에 추가
					const personalCalendarList = document.getElementById('personalCalendarList');
					const newCalendarItem = document.createElement('li');

					//li추가 연필추가
					newCalendarItem.innerHTML = `
					    <div class="calendar-item-wrapper" style="display: flex; align-items: center;">
					        <a href="javascript:void(0)" data-calno="${data.calNo}" class="calendar-item">
					            <i class="mdi mdi-calendar-blank" style="color:${selectedColor};"></i> ${calendarName}
					        </a>
					        <span class="edit-icon" style="margin-left: 10px;">
					            <i class="mdi mdi-pencil" aria-hidden="true"></i>
					        </span>
					    </div>`;
					personalCalendarList.appendChild(newCalendarItem);



					//============ 사이드바 내 캘린더 수정 ==============
					// 연필 아이콘 클릭 시 수정 모달 열기
					document.querySelectorAll('.edit-icon').forEach(item => {
						item.addEventListener('click', function() {
							const editModal = document.getElementById('editCalendarModal');
							const calendarItem = this.closest('.calendar-item-wrapper');

							// 캘린더 번호 (data-calno) 가져오기
							const selectedCalNo = calendarItem.querySelector('.calendar-item').getAttribute('data-calno');

							// 모달을 열기
							editModal.style.display = 'block';

							// 캘린더 정보 로드 (캘린더 이름, 색상 등)
							const calendarName = calendarItem.querySelector('.calendar-item').textContent.trim();
							const calendarIcon = calendarItem.querySelector('i');
							const calendarColor = window.getComputedStyle(calendarIcon).color; // 아이콘 색상 가져오기

							document.getElementById('editCalendarName').value = calendarName;

							// 색상 선택 처리
							const colorRadios = document.querySelectorAll('input[name="color"]');
							colorRadios.forEach(radio => {
								const radioLabel = document.querySelector(`label[for="${radio.id}"]`);
								if (radioLabel.style.backgroundColor === calendarColor) {
									radio.checked = true;
								}
							});

							
						});
					});

					// 모달 닫기 버튼 처리
					document.querySelectorAll('.close').forEach(closeBtn => {
						closeBtn.addEventListener('click', function() {
							const modal = this.closest('.modal');
							modal.style.display = 'none'; // 모달 닫기
						});
					});
					// 모달 저장 처리
					document.getElementById('editCalendarForm').onsubmit = function(e) {
						e.preventDefault();

						const calendarName = document.getElementById('editCalendarName').value;
						const selectedColor = document.querySelector('input[name="color"]:checked').value;
						const selectedCalNo = document.querySelector('.calendar-item').getAttribute('data-calno');

					}
					

					//============END 사이드바 내 캘린더 수정 ==============




					// 폼 초기화 
					document.getElementById('calendarName').value = ''; // 캘린더 이름 필드 초기화
					const colorInputs = document.querySelectorAll('input[name="color"]'); // 색상 선택 필드 가져오기
					colorInputs.forEach(input => input.checked = false); // 색상 선택 해제

				} else {
					alert('캘린더 추가에 실패했습니다: ' + data.message);
				}
			})
			.catch(error => {
				console.error('Error:', error);
				alert('서버와 통신 중 문제가 발생했습니다.');
			});
		// 모달 닫기
		document.getElementById('personalCalendarModal').style.display = 'none';
	};
	//============END 사이드바 내 캘린더 생성 ===========




	//============ 사이드바 공유 캘린더 생성 ===========

	// 공유 캘린더 모달 열기/닫기
	document.getElementById('addSharedCalendarBtn').addEventListener('click', function() {
		document.getElementById('sharedCalendarModal').style.display = 'block';
	});
	document.getElementById('cancelSharedCalendar').addEventListener('click', function() {
		document.getElementById('sharedCalendarModal').style.display = 'none';
	});





	//============END 사이드바 공유 캘린더 생성 ===========
	//============ 사이드바 휴지통  ===========
	// 사이드바 내 캘린더 수정 (휴지통으로 이동)
	document.querySelectorAll('.edit-icon').forEach(item => {
		item.addEventListener('click', function() {
			const editModal = document.getElementById('editCalendarModal');
			const calendarItem = this.closest('.calendar-item-wrapper');

			const selectedCalNo = calendarItem.querySelector('.calendar-item').getAttribute('data-calno');

			// 모달을 열기
			editModal.style.display = 'block';

			// 캘린더 이름과 색상 로드
			const calendarName = calendarItem.querySelector('.calendar-item').textContent.trim();
			const calendarIcon = calendarItem.querySelector('i');
			const calendarColor = window.getComputedStyle(calendarIcon).color;

			document.getElementById('editCalendarName').value = calendarName;

			// 색상 선택
			const colorRadios = document.querySelectorAll('input[name="color"]');
			colorRadios.forEach(radio => {
				const radioLabel = document.querySelector(`label[for="${radio.id}"]`);
				if (radioLabel.style.backgroundColor === calendarColor) {
					radio.checked = true;
				}
			});

			// 삭제 버튼 (휴지통으로 이동)
			document.getElementById('deleteBtn').onclick = function() {
				if (confirm('정말로 이 캘린더를 휴지통으로 이동하시겠습니까?')) {
					// 서버로 휴지통 이동 요청
					fetch('/cal/calTrash', {
						method: 'POST',
						headers: { 'Content-Type': 'application/json' },
						body: JSON.stringify({ calNo: selectedCalNo })
					})
						.then(response => response.text())
						.then(data => {
							alert(data);
							calendarItem.remove(); // 해당 캘린더 항목을 리스트에서 삭제
							editModal.style.display = 'none'; // 모달 닫기
						})
						.catch(error => console.error('Error:', error));
				}
			};
		});
	});
	// 모달 닫기 버튼 처리
	document.querySelectorAll('.close').forEach(closeBtn => {
		closeBtn.addEventListener('click', function() {
			const modal = this.closest('.modal');
			modal.style.display = 'none';
		});
	});

	// 휴지통 리스트에서 복원, 완전 삭제 기능 모달 처리
	document.querySelectorAll('.trash-item').forEach(item => {
		item.addEventListener('click', function() {
			const trashModal = document.getElementById('trashModal');
			const calNo = this.getAttribute('data-calno'); // 휴지통에서 선택된 캘린더 번호

			// 모달을 열기
			trashModal.style.display = 'block';

			// 복원 버튼 클릭 시
			document.getElementById('restoreBtn').onclick = function() {
				fetch('/sch/calRestore', {
					method: 'POST',
					headers: { 'Content-Type': 'application/json' },
					body: JSON.stringify({ calNo: calNo })
				})
					.then(response => response.text())
					.then(data => {
						alert(data);
						trashModal.style.display = 'none';
						location.reload(); // 페이지 새로고침으로 복원된 항목 처리
					})
					.catch(error => console.error('Error:', error));
			};

			// 완전 삭제 버튼 클릭 시
			document.getElementById('permanentlyDeleteBtn').onclick = function() {
				if (confirm('정말로 이 캘린더를 완전히 삭제하시겠습니까?')) {
					fetch('/sch/calPermanentlyDel', {
						method: 'POST',
						headers: { 'Content-Type': 'application/json' },
						body: JSON.stringify({ calNo: calNo })
					})
						.then(response => response.text())
						.then(data => {
							alert(data);
							trashModal.style.display = 'none';
							location.reload(); // 페이지 새로고침으로 삭제된 항목 처리
						})
						.catch(error => console.error('Error:', error));
				}
			};

			// 취소 버튼 클릭 시 모달 닫기
			document.getElementById('cancelBtn').onclick = function() {
				trashModal.style.display = 'none';
			};
		});
	});
	//============END 사이드바 휴지통  ===========





	//============END 사이드바 ============

});

//=================================함수모음======================================
// 오늘 날짜를 YYYY-MM-DD 형식으로 반환하는 함수
function getTodayDate() {
	return new Date().toISOString().slice(0, 10);
}




//================================END 함수모음====================================
