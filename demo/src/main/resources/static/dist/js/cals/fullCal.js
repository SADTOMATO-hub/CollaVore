document.addEventListener('DOMContentLoaded', function() {

	// 서버에서 JSON 데이터 가져오기 (Ajax)
	fetch( //조회 패치
		'/sch/schList',
		{
			method: "POST"
		}
	)
		.then(response => response.json())
		.then(eventsData => {
			console.log('FullCalendar에 전달된 이벤트:', eventsData); // JSON 데이터 확인
		
			const sList = eventsData.map(check => ({
				id: check.schNo,
				title: check.title,
				start: new Date(check.startDate),
				end: new Date(check.endDate),
				allDay: true
			}));
		
	
			var calendarEl = document.getElementById('calendar');
			//풀캘린더 초기화
			var calendar = new FullCalendar.Calendar(calendarEl, {
				locale: 'ko', // 한국어 설정
				headerToolbar: {
					left: 'prev,next today',
					center: 'title',
					right: '' // 공백 처리 (드롭다운 위치)
				},

				initialDate: getTodayDate(), // 오늘 날짜를 기본값으로 설정
				navLinks: true, // 일/주 이름 클릭 시 해당 뷰로 이동 가능
				selectable: true, // 드래그해서 이벤트 추가 가능
				selectMirror: true,  // 드래그 시 이벤트가 미리보기로 표시됨


				//======================== 캘린더 뒤에 1일 2일 "일" 제거===================
				// 날짜 헤더 형식 변경 (요일만 표시)
				dayHeaderFormat: { weekday: 'short' },

				// 날짜 클릭 형식 변경 (접미사 없는 숫자)
				dayCellDidMount: function(info) {
					// 날짜에서 "일" 제거
					info.el.querySelector('.fc-daygrid-day-number').textContent =
						info.date.getDate(); // 날짜 숫자만 표시
				},
				//========================END 캘린더 뒤에 1일 2일 "일" 제거===================

				editable: true,
				dayMaxEvents: true, // allow "more" link when too many events
				//================ ajax데이터 불러올 부분 =====================

				events: sList,

				
				//================END ajax데이터 불러올 부분 =====================

				
				
				//등록
				select: function(arg) {
					var title = prompt('Event Title:');
					if (title) {
						calendar.addEvent({
							title: title,
							start: arg.start,
							end: arg.end,
							allDay: arg.allDay
						})
					}
					calendar.unselect()
				},

				//삭제
				eventClick: function(arg) {
					if (confirm('Are you sure you want to delete this event?')) {
						arg.event.remove()
					}
				},

			});

			calendar.render();
		
			})
		
		.catch(error => console.error('Error fetching events:', error));
		/*	// 등록패치
			fetch('/sch/schinsert',{method: "POST"})
	.then(response => response.json())
	.then(insertDate => {
		console.log('FullCalendar에 전달된 이벤트:',insertDate);
		
		const sInsert = insertDate.map(check => ({
			
		}))
	})*/

		



















	//============================캘린더 사이즈============================
	// 캘린더가 화면 전체를 차지하도록 CSS 조정
	calendarEl.style.width = '80%';
	calendarEl.style.maxWidth = '1200px';  // 너무 커지지 않도록 제한
	calendarEl.style.margin = '0 auto';    // 가운데 정렬

	//============================END 캘린더 사이즈============================



	//============================캘린더 상단 월/주/일 드롭다운==========================================


	// 드롭다운 생성 및 옵션 추가
	var viewSelector = document.createElement('select');
	viewSelector.id = 'viewSelector';

	// 드롭다운 스타일 설정
	viewSelector.style.width = '80px';         // 세 글자가 들어갈 정도의 크기
	viewSelector.style.fontSize = '14px';
	viewSelector.style.padding = '10px';
	viewSelector.style.textAlign = 'center';
	viewSelector.style.border = '1px solid #ddd';

	var options = [
		{ value: 'dayGridMonth', text: '월간' },
		{ value: 'timeGridWeek', text: '주간' },
		{ value: 'timeGridDay', text: '일간' }
	];

	options.forEach(function(option) {
		var opt = document.createElement('option');
		opt.value = option.value;
		opt.textContent = option.text;
		viewSelector.appendChild(opt);
	});

	// 드롭다운 변경 시 뷰 전환
	viewSelector.addEventListener('change', function(e) {
		calendar.changeView(e.target.value);
	});

	// 캘린더 헤더의 오른쪽에 드롭다운 삽입
	calendarEl.querySelector('.fc-header-toolbar').appendChild(viewSelector);


	// 이전/다음/오늘 버튼 정렬 조정
	var leftSection = calendarEl.querySelector('.fc-toolbar-chunk:first-child');
	leftSection.style.display = 'flex';
	leftSection.style.gap = '10px'; // 버튼 사이 간격 조정
	//==========================END 월/주/일 드롭다운========================================
});




// 오늘 날짜를 YYYY-MM-DD 형식으로 반환하는 함수
function getTodayDate() {
	return new Date().toISOString().slice(0, 10);
}