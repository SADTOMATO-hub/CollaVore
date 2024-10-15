document.addEventListener('DOMContentLoaded', function() {
	var calendarEl = document.getElementById('calendar');

	var calendar = new FullCalendar.Calendar(calendarEl, {
		locale: 'ko', // 한글 로케일 설정
		headerToolbar: {
			left: 'prev,next today',
			center: 'title',
			right: ''  // 오른쪽에 드롭다운 직접 추가하려고 비워둠
		},


		initialDate: getTodayDate(),// <<오늘날짜함수 getTodayDate >> 오늘 날짜로 설정
		navLinks: true, // can click day/week names to navigate views
		selectable: true,
		selectMirror: true,
		select: function(arg) {
			openModal(arg.start, arg.end); // 모달 열기
			calendar.unselect();
		},
		eventClick: function(arg) {
			if (confirm('이 일정을 삭제하시겠습니까?')) {
				arg.event.remove();
			}
		},
		editable: true,
		dayMaxEvents: true, // allow "more" link when too many events
		events: []
	});

	calendar.render();


	//-------------------------클릭시 일정생성 모달 --------------------------------------
	// 모달 열기/닫기 기능
	const openModalButton = document.getElementById('openModal');
	const eventModal = document.getElementById('eventModal');
	const closeModalButton = document.getElementById('closeModal');
	const saveEventButton = document.getElementById('saveEvent');

	// 모달 열기 버튼 이벤트
	openModalButton.addEventListener('click', () => {
		resetModalFields(); // 모달 열 때 필드 초기화
		eventModal.style.display = 'block';
	});


	// 모달 닫기 버튼 이벤트
	closeModalButton.addEventListener('click', () => {
		eventModal.style.display = 'none';
		resetModalFields(); // 닫을 때 필드 초기화
	});

	// 외부 클릭 시 모달 닫기
	window.addEventListener('click', (event) => {
		if (event.target === eventModal) {
			eventModal.style.display = 'none';
			resetModalFields(); // 외부 클릭으로 닫을 때 필드 초기화
		}
	});

	// 일정 저장
	saveEventButton.addEventListener('click', async () => {
		const title = document.getElementById('eventTitle').value;
		const startDate = document.getElementById('startDate').value;
		const startTime = document.getElementById('startTime').value;
		const endDate = document.getElementById('endDate').value;
		const endTime = document.getElementById('endTime').value;
		const calendarType = document.getElementById('calendarType').value;

		if (title && startDate) {
			const schVO = {
				title: `[${calendarType}] ${title}`,
				startDate: `${startDate}T${startTime}`,
				endDate: `${endDate}T${endTime}`,
				calNo: 1, // 캘린더 유형 예시
				isAlarm: 'Y' // 알림 여부 예시
			};
		
		try {
            const response = await fetch('/api/schs/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(schVO)
            });


			if (response.ok) {
                const savedEvent = await response.json();
                calendar.addEvent({
                    title: savedEvent.title,
                    start: savedEvent.startDate,
                    end: savedEvent.endDate,
                    allDay: false
                });
                eventModal.style.display = 'none';
                resetModalFields(); // 필드 초기화
            } else {
                alert('일정 저장에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    } else {
        alert('필수 항목을 입력하세요!');
    }
});



	//-------------------------모달 함수----------------------------------
	function openModal(start, end) {
		document.getElementById('startDate').value = start.toISOString().split('T')[0];
		document.getElementById('endDate').value = end.toISOString().split('T')[0];


		const now = new Date();
		const formattedTime = now.toTimeString().split(' ')[0].slice(0, 5); // HH:mm 형식

		document.getElementById('startTime').value = formattedTime;
		document.getElementById('endTime').value = formattedTime;

		eventModal.style.display = 'block';
	}

	// 새로입력시 필드 초기화 함수
	function resetModalFields() {
		document.getElementById('eventTitle').value = '';
		document.getElementById('calendarType').selectedIndex = 0; // 첫 번째 옵션 선택
		document.getElementById('startDate').value = '';
		document.getElementById('startTime').value = '';
		document.getElementById('endDate').value = '';
		document.getElementById('endTime').value = '';
	}
	//-------------------------END 모달 함수----------------------------------

	//-------------------------END 클릭시 일정생성 모달 ----------------------------------







	//-------------------------캘린더 상단 월간 주간 일간 ----------------------------------
	// 드롭다운 생성 및 이벤트 추가
	var viewSelect = document.createElement('select');
	viewSelect.innerHTML = `
        <option value="dayGridMonth">월간</option>
        <option value="timeGridWeek">주간</option>
        <option value="timeGridDay">일간</option>
    `;
	viewSelect.addEventListener('change', function(e) {
		calendar.changeView(e.target.value); // 선택한 뷰로 전환
		updateTitle(); // 타이틀 업데이트
	});

	// 드롭다운 스타일 설정
	viewSelect.style.width = '65px';
	viewSelect.style.marginRight = '10px'; // 우측에 여백 추가
	viewSelect.style.display = 'inline-block'; // 인라인 블록으로 설정

	// 캘린더 헤더에 드롭다운 추가
	var headerToolbar = calendarEl.querySelector('.fc-header-toolbar');
	headerToolbar.appendChild(viewSelect); // 드롭다운을 헤더의 오른쪽에 추가
	//-------------------------END 캘린더 상단 월간 주간 일간 ----------------------------------



});//end document.addEventListener('DOMContentLoaded', function() 최상단 돔










// 오늘날짜 함수 
function getTodayDate() {
	return new Date().toISOString().split('T')[0];
}



