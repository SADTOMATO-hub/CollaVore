// 기존 직무 데이터를 저장할 배열
let existingJobs = [];
let nonDeletableJobs = []; // 사원에 등록된 직무 번호 목록을 저장

// 직무 데이터 로드
function loadExistingJobs() {
    fetch('/jobs/getExistingJobs')
    .then(response => response.json())
    .then(data => {
        existingJobs = data.map(item => item.jobName); // 기존 직무 이름을 저장
        nonDeletableJobs = data.filter(item => item.isAssigned).map(item => item.jobNo); // 사원에 등록된 직무 번호 저장
        const container = document.getElementById('jobsTable');
        container.innerHTML = ''; // 기존 데이터 초기화

        // 데이터를 이용해 직무 테이블 업데이트
        data.forEach(item => {
            const div = document.createElement('div');
            div.innerHTML = `
                <span onclick="editJob(this)">${item.jobName}</span>
                <button data-job-no="${item.jobNo}" onclick="removeJob(this, ${item.jobNo})" 
                    ${item.isAssigned ? 'disabled title="사원에 등록된 직무는 삭제할 수 없습니다."' : ''}>X</button>
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

// 직무 저장 함수
function saveJobs() {
    const jobs = [];
    const container = document.getElementById('jobsTable');

    // 직무 데이터 수집
    container.querySelectorAll('div').forEach(div => {
        const input = div.querySelector('input');
        const span = div.querySelector('span');
        if (input && input.value.trim() !== '') {
            // 새로 추가된 직무만 수집
            if (!existingJobs.includes(input.value.trim())) {
                jobs.push({ jobName: input.value.trim() });
            }
        } else if (span) {
            // 기존 직무는 수집하지 않음
            if (!existingJobs.includes(span.textContent.trim())) {
                jobs.push({ jobName: span.textContent.trim() });
            }
        }
    });

    // 서버에 저장 요청
    if (jobs.length > 0) {
        fetch('/jobs/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(jobs),
        })
        .then(response => response.text()) // 응답을 JSON 대신 텍스트로 처리
        .then(data => {
            if (data === 'success') {
                alert('직무가 성공적으로 저장되었습니다.');
                loadExistingJobs(); // 저장 후 직무 목록 다시 로드
            } else {
                alert('저장에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error saving jobs:', error);
            alert('저장 중 오류가 발생했습니다.');
        });
    } else {
        alert('저장할 새로운 직무가 없습니다.');
    }
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
    if (confirm('정말로 삭제하시겠습니까?')) {
        if (jobNo) {
            fetch(`/jobs/delete/${jobNo}`, {
                method: 'DELETE'
            })
            .then(response => response.text())
            .then(result => {
                if (result === 'success') {
                    alert('직위가 성공적으로 삭제되었습니다.');
                    button.parentElement.remove();
                } else if (result === 'cannot_delete') {
                    alert('해당 직위가 사원에게 할당되어 있어 삭제할 수 없습니다.');
                } else {
                    alert('삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error deleting job:', error);
                alert('삭제 중 오류가 발생했습니다.');
            });
        } else {
            button.parentElement.remove(); // 새로 추가된 직위는 화면에서만 삭제
        }
    }
}




// 페이지 로드 시 기존 데이터 불러오기
document.addEventListener('DOMContentLoaded', loadExistingJobs);