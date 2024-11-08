// 기존 직무 데이터를 저장할 배열
let existingJobs = [];
let modifiedJobs = []; // 수정된 직무를 저장할 배열
let nonDeletableJobs = []; // 사원에 등록된 직무 번호 목록을 저장
let jobsToSave = []; // 서버에 전송할 직무 목록

// 직무 데이터 로드
function loadExistingJobs() {
	fetch('/jobs/getExistingJobs')
		.then(response => response.json())
		.then(data => {
			existingJobs = data.map(item => ({ jobNo: item.jobNo, jobName: item.jobName })); // 기존 직무 정보를 저장
			nonDeletableJobs = data.filter(item => item.isAssigned).map(item => item.jobNo); // 사원에 등록된 직무 번호 저장
			const container = document.getElementById('jobsTable');
			container.innerHTML = ''; // 기존 데이터 초기화

			// 데이터를 이용해 직무 테이블 업데이트
			data.forEach(item => {
				const div = document.createElement('div');
				div.innerHTML = `
                <span onclick="editJob(this, ${item.jobNo})" data-job-no="${item.jobNo}">${item.jobName}</span>
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
	const input = document.createElement('input');
	input.type = 'text';
	input.placeholder = "새 직무 입력";

	const div = document.createElement('div');
	div.appendChild(input);

	const button = document.createElement('button');
	button.textContent = 'X';
	button.onclick = function() {
		div.remove();
	};

	input.addEventListener('blur', function() {
		const newName = input.value.trim();

		// 기존 직무 이름과 중복 검사
		if (newName !== '' && !existingJobs.some(job => job.jobName === newName)) {
			//existingJobs.push({ jobName: newName }); // 중복되지 않으면 추가
			jobsToSave.push({ jobName: newName });
			div.innerHTML = `
                <span onclick="editJob(this)" class="newName">${newName}</span>
                <button onclick="removeJob(this)">X</button>
            `;
		} else if (newName === '') {
			div.remove(); // 값이 없으면 입력 취소
		} else {
			// 중복된 이름일 경우 알림 메시지 출력 후 다시 focus 유지
			alert("중복된 직무 이름은 사용할 수 없습니다.");

			Toast.fire({
				icon: "warning",
				title: "부서장 지정에 실패했습니다."
			});
			setTimeout(() => input.focus(), 0); // 경고 후 다시 focus
		}
	});

	div.appendChild(button);
	container.appendChild(div);
	input.focus();
}


// 직무 저장 함수
function saveJobs() {
	const container = document.getElementById('jobsTable');

	// 직무 데이터 수집
	container.querySelectorAll('div').forEach(div => {
		const spanElement = div.querySelector('span.newName');
		let input = '';
		if (spanElement) {
			input = spanElement.textContent.trim();
		}
		const span = div.querySelector('span');
		console.log(input);
		if (span) {
			// 기존 직무 중 수정된 직무만 수집
			const jobNo = parseInt(span.getAttribute('data-job-no'));
			const newName = span.textContent.trim();
			const originalJob = existingJobs.find(job => job.jobNo === jobNo);
			if (originalJob && originalJob.jobName !== newName) {
				jobsToSave.push({ jobNo, jobName: newName });
			}
		}
	});

	// 서버에 저장 요청

	console.log(jobsToSave);
	if (jobsToSave.length > 0) {
		fetch('/jobs/save', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(jobsToSave),
		})
			.then(response => response.text())
			.then(data => {
				if (data === 'success') {
					Toast.fire({
						icon: "success",
						title: "직무가 성공적으로 저장되었습니다."
					});
					loadExistingJobs(); // 저장 후 직무 목록 다시 로드
				} else {
					Toast.fire({
						icon: "warning",
						title: "저장에 실패했습니다."
					});
				}
			})
			.catch(error => {
				console.error('Error saving jobs:', error);
				Toast.fire({
					icon: "error",
					title: "저장 중 오류가 발생했습니다."
				});
			});
	} else {
		Toast.fire({
			icon: "warning",
			title: "저장할 새로운 직무가 없습니다."
		});
	}
}

// 직무 수정 함수 (span을 클릭했을 때 실행)
function editJob(span, jobNo = null) {
	const jobName = span.textContent;
	const input = document.createElement('input');
	input.type = 'text';
	input.value = jobName;
	let alertShown = false; // 중복 알림이 한 번만 뜨도록 제어

	input.onblur = function() {
		const newName = input.value.trim();

		// 기존 직무 이름과 중복 검사 및 수정된 내용 적용
		if (newName !== '' && !existingJobs.some(job => job.jobName === newName) && newName !== jobName) {
			span.textContent = newName;

			// 수정된 직무를 modifiedJobs 배열에 추가 (중복 방지)
			if (jobNo && !modifiedJobs.some(job => job.jobNo === jobNo)) {
				modifiedJobs.push({ jobNo: jobNo, jobName: newName });
			} else if (!jobNo) {
				modifiedJobs.push({ jobName: newName });
			}
		} else if (newName === jobName) {
			span.textContent = jobName; // 변경되지 않았으면 기존 값 유지
		} else if (existingJobs.some(job => job.jobName === newName)) {
			if (!alertShown) { // 한 번만 실행되도록 설정
				Toast.fire({
					icon: "warning",
					title: "중복된 직무 이름은 사용할 수 없습니다."
				});
				alertShown = true;
			}
			input.focus(); // 중복된 경우 입력 상태 유지
			return; // 중복된 경우 함수 종료
		}

		span.style.display = 'inline';
		input.remove(); // input 제거
	};

	span.style.display = 'none';
	span.parentNode.insertBefore(input, span);
	input.focus();
}




// 직무 삭제 함수
function removeJob(button, jobNo = null) {
	Swal.fire({
		title: "정말로 삭제하시겠습니까?",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#3085d6",
		cancelButtonColor: "#d33",
		confirmButtonText: "예",
		cancelButtonText: "아니요"
	}).then((result) => {
		if (result.isConfirmed) {
			// 아작스
			if (jobNo) {
				fetch(`/jobs/delete/${jobNo}`, {
					method: 'DELETE'
				})
					.then(response => response.text())
					.then(result => {
						if (result === 'success') {
							Toast.fire({
								icon: "success",
								title: "직위가 성공적으로 삭제되었습니다."
							});
							button.parentElement.remove();
						} else if (result === 'cannot_delete') {
							Toast.fire({
								icon: "warning",
								title: "해당 직위가 사원에게<br> 할당되어 있어 삭제할 수 없습니다."
							});
						} else {
							Toast.fire({
								icon: "error",
								title: "삭제에 실패했습니다."
							});
						}
					})
					.catch(error => {
						console.error('Error deleting job:', error);
							Toast.fire({
								icon: "error",
								title: "삭제 중 오류가 발생했습니다."
							});
					});
			} else {
				button.parentElement.remove(); // 새로 추가된 직위는 화면에서만 삭제
			}
		}
	});
}

// 페이지 로드 시 기존 데이터 불러오기
document.addEventListener('DOMContentLoaded', loadExistingJobs);