document.addEventListener('DOMContentLoaded', function() {

	// 서버에서 JSON 데이터 가져오기 (Ajax)
	fetch( //조회 패치
		'/sch/schList',	// 스케줄 리스트를 가져오는 엔드포인트
		{
			method: "POST", // HTTP 요청 방식 (POST)
			headers: {
				'Content-Type': 'application/json', // 요청 본문이 JSON 형식임을 지정
			},
		}
	)
		.then(response => response.json()) // 응답 데이터를 JSON 형식으로 파싱
		.then(eventsData => {
			console.log('FullCalendar에 전달된 이벤트:', eventsData); // JSON 데이터 확인
			
				// 서버에서 받은 데이터를 FullCalendar의 이벤트 구조에 맞게 매핑
			const sList = eventsData.map(check => ({
				id: check.schNo,
				title: check.title,
				start: new Date(check.startDate),
				end: new Date(check.endDate),
				allDay: true
			}));

			// 캘린더 요소 가져오기
			var calendarEl = document.getElementById('calendar');
			//풀캘린더 초기화
			var calendar = new FullCalendar.Calendar(calendarEl, {
				locale: 'ko', // 한국어 설정
				headerToolbar: {
					left: 'prev,next today',
					center: 'title',
					right: 'dayGridMonth,timeGridWeek,timeGridDay' // 공백 처리 (드롭다운 위치)
				},

				initialDate: getTodayDate(), // 오늘 날짜를 기본값으로 설정
				navLinks: true, // 일/주 이름 클릭 시 해당 뷰로 이동 가능
				selectable: true, // 드래그해서 이벤트 추가 가능
				selectMirror: true,  // 드래그 시 이벤트가 미리보기로 표시됨


				//======================== 캘린더 뒤에 1일 2일 "일" 제거===================
				/*// 날짜 헤더 형식 변경 (요일만 표시)
				dayHeaderFormat: { weekday: 'short' },

				// 날짜 클릭 형식 변경 (접미사 없는 숫자)
				dayCellDidMount: function(info) {
					// 날짜에서 "일" 제거
					info.el.querySelector('.fc-daygrid-day-number').textContent =
						info.date.getDate(); // 날짜 숫자만 표시
				},*/
				//========================END 캘린더 뒤에 1일 2일 "일" 제거===================

				editable: true,
				dayMaxEvents: true, // allow "more" link when too many events
				//================ ajax데이터 불러올 부분 =====================

				events: sList,


				//================END ajax데이터 불러올 부분 =====================


				//========================일정등록=============================
				//등록
				select: function(arg) {
					var title = prompt('일정 제목을 입력하세요:');
					if (title) {
						fetch('/sch/schInsert', {  // 등록 API 호출
							method: 'POST',
							headers: {
								'Content-Type': 'application/json',
							},
							body: JSON.stringify({
								title: title,
								startDate: arg.start.toISOString(),
								endDate: arg.end ? arg.end.toISOString() : arg.start.toISOString(),
								allDay: arg.allDay
							})
						})
							.then(response => response.json())
							.then(data => {
								if (data.success) {
									calendar.addEvent({
										id: data.id,
										title: title,
										start: arg.start,
										end: arg.end,
										allDay: arg.allDay
									});
								} else {
									console.log(arg)
									alert('일정 등록에 실패했습니다.');
								}
							})
							.catch(error => console.error('Error:', error));
					}
					calendar.unselect();
					//========================END 일정등록=============================







					//========================일정삭제=============================	
				},
				// 일정 삭제
				eventClick: function(arg) {
					if (confirm('정말로 이 일정을 삭제하시겠습니까?')) {
						fetch('/sch/schDelete', {  // 삭제 API 호출
							method: 'POST',
							headers: {
								'Content-Type': 'application/json',
							},
							body: JSON.stringify({ schNo: arg.event.id })
						})
							.then(response => response.json())
							.then(data => {
								if (data.success) {
									arg.event.remove();  // 캘린더에서 삭제
								} else {
									console.log(arg)
									alert('일정 삭제에 실패했습니다.');
								}
							})
							.catch(error => console.error('Error:', error));
					}
				}
				//========================END 일정등록=============================
			});

			calendar.render();

		})

		.catch(error => console.error('Error fetching events:', error));




	//============================모달============================
	
	//============================END 모달============================


























	//============================캘린더 상단 월/주/일 드롭다운==========================================



	//==========================END 월/주/일 드롭다운========================================
});




// 오늘 날짜를 YYYY-MM-DD 형식으로 반환하는 함수
function getTodayDate() {
	return new Date().toISOString().slice(0, 10);
}



