// 직무 데이터 로드
function loadExistingJobs() {
    fetch('/jobs/getExistingJobs')
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById('jobsTable');
        container.innerHTML = ''; // 기존 데이터 초기화
        // 데이터를 이용해 직무 테이블 업데이트
        data.forEach(item => {
            const div = document.createElement('div');
            div.innerHTML = `
                <span onclick="editJob(this)">${item.jobName}</span>
                <button data-job-no="${item.jobNo}" onclick="removeJob(this, ${item.jobNo})">X</button>
            `;
            container.appendChild(div);
        });
    })
    .catch(error => {
        console.error('Error fetching jobs:', error);
    });
}

// 새 직무 추가 함수
function addJob() {
    const container = document.getElementById('jobsTable');
    const div = document.createElement('div');
    div.innerHTML = `
        <input type="text" placeholder="새 직무 입력" />
        <button onclick="removeJob(this)">X</button>
    `; 
    container.appendChild(div);
}

// 직무 수정 함수 (span을 클릭했을 때 실행)
function editJob(span) {
    const jobName = span.textContent;
    const input = document.createElement('input');
    input.type = 'text';
    input.value = jobName;
    input.onblur = function() {
        // 수정된 값을 입력 후 focus가 사라질 때 span으로 다시 변환
        if (input.value.trim() !== '') {
            span.textContent = input.value.trim();
        } else {
            span.textContent = jobName; // 값이 없을 경우 기존 값으로 되돌림
        }
        span.style.display = 'inline';
        input.remove(); // input 제거
    };
    span.style.display = 'none';
    span.parentNode.insertBefore(input, span);
    input.focus(); // 바로 입력할 수 있도록 포커스 설정
}

// 직무 삭제 함수
function removeJob(button, jobNo = null) {
    // 확인 대화 상자
    if (confirm('정말로 삭제하시겠습니까?')) {
        if (jobNo) {
            // 서버로 삭제 요청
            fetch(`/jobs/delete/${jobNo}`, {
                method: 'DELETE'
            })
            .then(response => response.text())
            .then(result => {
                if (result === 'success') {
                    button.parentElement.remove();
                } else {
                    alert('삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error deleting job:', error);
                alert('삭제 중 오류가 발생했습니다.');
            });
        } else {
            // 새로 추가된 직무는 화면에서만 삭제
            button.parentElement.remove();
        }
    }
}

// 페이지 로드 시 기존 데이터 불러오기
document.addEventListener('DOMContentLoaded', loadExistingJobs);
