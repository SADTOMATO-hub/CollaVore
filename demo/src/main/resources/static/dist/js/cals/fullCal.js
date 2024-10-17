document.addEventListener('DOMContentLoaded', function() {

    // 서버에서 JSON 데이터 가져오기 (Ajax)
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
            allDay: true
        }));

        // 캘린더 요소 가져오기
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            locale: 'ko',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            initialDate: getTodayDate(),
            navLinks: true,
            selectable: true,
            selectMirror: true,
            editable: true,
            dayMaxEvents: true,
            events: sList,

            // 일정 클릭 시 조회 모달 오픈
            eventClick: function(info) {
                // 조회 모달 창 열기
                var viewModal = document.getElementById('viewScheduleModal');
                document.getElementById('viewCalendar').value = info.event.extendedProps.calendarType || '없음';
                document.getElementById('viewTitle').value = info.event.title;
                document.getElementById('viewStartDate').value = info.event.startStr.split('T')[0];
                document.getElementById('viewStartTime').value = info.event.startStr.split('T')[1];
                document.getElementById('viewEndDate').value = info.event.endStr ? info.event.endStr.split('T')[0] : '';
                document.getElementById('viewEndTime').value = info.event.endStr ? info.event.endStr.split('T')[1] : '';

                viewModal.style.display = 'block'; // 모달 보이기

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

                // 모달 닫기 버튼
                var closeBtn = viewModal.querySelector('.close');
                closeBtn.onclick = function() {
                    viewModal.style.display = 'none';
                };

                // 모달 외부 클릭 시 닫기
                window.onclick = function(event) {
                    if (event.target == viewModal) {
                        viewModal.style.display = 'none';
                    }
                };
            },

            // 일정 추가 모달 처리
            select: function(arg) {
                // 일정 추가 모달 창 열기
                var modal = document.getElementById('scheduleModal');
                var closeBtn = modal.querySelector('.close');
                var form = document.getElementById('scheduleForm');

                // 시작 날짜와 종료 날짜를 자동으로 폼에 설정
                document.getElementById('startDate').value = arg.startStr;
                document.getElementById('endDate').value = arg.endStr || arg.startStr;

                modal.style.display = 'block'; // 모달 보이기

                // 모달 닫기 버튼 클릭 시 모달 닫기
                closeBtn.onclick = function() {
                    modal.style.display = 'none';
                };

                // 폼 제출 처리
                form.onsubmit = function(e) {
                    e.preventDefault();

                    // 폼 데이터 가져오기
                    var title = document.getElementById('title').value;
                    var startDate = document.getElementById('startDate').value;
                    var endDate = document.getElementById('endDate').value;

                    // 일정생성 
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
        });

        calendar.render();

    })
    .catch(error => console.error('Error fetching events:', error));
});


//=================================함수모음======================================
// 오늘 날짜를 YYYY-MM-DD 형식으로 반환하는 함수
function getTodayDate() {
    return new Date().toISOString().slice(0, 10);
}

// 모달 닫기 처리 (화면 바깥 클릭 시 모달 닫기)
window.onclick = function(event) {
    var modal = document.getElementById('scheduleModal');
    if (event.target == modal) {
        modal.style.display = 'none';
    }
};
//================================END 함수모음====================================
