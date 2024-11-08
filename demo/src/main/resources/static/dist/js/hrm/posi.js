let levelCounter = 1;

// 페이지 로드 시 기존 데이터 불러오기
function loadExistingData() {
	fetch('/positions/getExistingPositions')
		.then(response => response.json())
		.then(data => {
			const table = document.getElementById('positionTable');
			// 첫 번째 tr을 제외한 나머지 tr 제거
			while (table.rows.length > 1) {
				table.deleteRow(1); // 항상 두 번째 행부터 삭제
			}
			// 데이터로 테이블 업데이트
			data.forEach((item, index) => {
				const row = document.createElement('tr');
				row.innerHTML = `
                    <td>Level. ${index + 1}</td>
                    <td><input type="text" value="${item.posiName}" data-posi-no="${item.posiNo}" /></td>
                    <td><button onclick="removeLevel(this, ${item.posiNo})"><i class="fas fa-minus"></i></button></td>
                `;
				table.appendChild(row);
				levelCounter = index + 1; // 레벨 카운터 업데이트
			});
		});
}

// 중복 검사 함수
function checkDuplicate(input) {
	const newName = input.value.trim();
	if (newName === "") return;

	const existingNames = Array.from(document.querySelectorAll('#positionTable input'))
		.map(input => input.value.trim())
		.filter(name => name !== ""); // 입력된 직위명만 수집

	const duplicateCount = existingNames.filter(name => name === newName).length;

	if (duplicateCount > 1) {
		Toast.fire({
			icon: "warning",
			title: "중복된 직위명은 사용할 수 없습니다."
		});
		input.value = ""; // 중복이 발견되면 입력 필드를 초기화
		input.focus(); // 입력 필드에 다시 포커스를 맞춤
	}
}

// 새 레벨 추가 함수
function addLevel() {
	levelCounter++;
	const table = document.getElementById('positionTable');

	const row = document.createElement('tr');
	row.innerHTML = `
        <td>Level. ${levelCounter}</td>
        <td><input type="text" placeholder="새 직위 입력" onblur="checkDuplicate(this)" /></td>
        <td><button onclick="removeLevel(this)"><i class="fas fa-minus"></i></button></td>
    `;
	table.appendChild(row);
	reorderLevels();

	// 새로 추가된 입력 필드에 포커스 설정
	const newInput = row.querySelector('input');
	newInput.focus();
}

// 레벨 삭제 함수
function removeLevel(button, posiNo = null) {
	Swal.fire({
		title: "정말 삭제하시겠습니까?",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#3085d6",
		cancelButtonColor: "#d33",
		confirmButtonText: "예",
		cancelButtonText: "아니요"
	}).then((result) => {
		if (result.isConfirmed) {
			// 아작스
			if (posiNo) {
				// 서버로 삭제 요청
				fetch(`/positions/delete/${posiNo}`, {
					method: 'DELETE'
				})
					.then(response => response.text())
					.then(result => {
						if (result === 'success') {
							Toast.fire({
								icon: "success",
								title: "직위가 성공적으로 삭제되었습니다."
							});
							const row = button.closest('tr'); // 삭제할 행을 찾음
							row.remove(); // 행 삭제
							reorderLevels();
						} else if (result === 'cannot_delete') {
							Toast.fire({
								icon: "warning",
								title: "해당 직위가 사원에게 <br>할당되어 삭제할 수 없습니다."
							});
						} else {
							Toast.fire({
								icon: "error",
								title: "삭제에 실패했습니다."
							});
						}
					})
					.catch(error => {
						console.error('Error deleting position:', error);
						Toast.fire({
							icon: "warning",
							title: "삭제 중 오류가 발생했습니다. <br>관리자에게 문의하세요."
						});
					});
			} else {
				// 새로 추가된 레벨의 경우 화면에서만 삭제
				const row = button.closest('tr'); // 삭제할 행을 찾음
				row.remove();
				reorderLevels();
			}
		}
	});
}


// 레벨 재정렬
function reorderLevels() {
	const rows = document.querySelectorAll('#positionTable tr');
	rows.forEach((row, index) => {
		if (index === 0) return; // 헤더는 제외
		row.cells[0].textContent = `Level. ${index}`;
	});
}

// 데이터 저장 함수
function saveData() {
	const rows = document.querySelectorAll('#positionTable tr');
	const positions = [];
	let nonName = false;
	const nameSet = new Set(); // 중복 확인을 위한 Set
	let hasDuplicate = false; // 중복 발생 여부 확인

	rows.forEach((row, index) => {
		if (index === 0) return; // 첫 번째는 헤더이므로 제외
		const posiNo = row.cells[1].querySelector('input').dataset.posiNo || null;
		const posiName = row.cells[1].querySelector('input').value.trim();

		// 공백 이름 검사
		if (posiName === '') {
			nonName = true;
		}

		// 중복 검사
		if (nameSet.has(posiName)) {
			alert("중복된 직위명이 있습니다. 확인 후 다시 시도해주세요.");
			hasDuplicate = true;
			return; // 중복이 발견되면 즉시 종료
		}
		nameSet.add(posiName);

		positions.push({
			posiNo: posiNo,
			posiName: posiName,
			grade: index // 레벨을 등급으로 사용
		});
	});

	if (nonName) {
		Toast.fire({
			icon: "warning",
			title: "직위명이 입력되지 않았습니다. <br>확인 후 다시 시도해주세요."
		});
		return;
	}

	if (hasDuplicate) {
		return; // 중복이 있을 경우 저장 요청 중단
	}

	// 서버에 데이터 전송
	fetch('/positions/save', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(positions)
	})
		.then(response => response.text())
		.then(result => {
			if (result === 'success') {
				Toast.fire({
					icon: "success",
					title: "저장되었습니다."
				});
				loadExistingData();
			} else {
				Toast.fire({
					icon: "warning",
					title: "저장에 실패했습니다."
				});
			}
		})
		.catch(error => {
			console.error('Error:', error);
			Toast.fire({
				icon: "error",
				title: "에러 발생! 관리자에게 문의하세요."
			});
		});
}

// 페이지 로드 시 기존 데이터 불러오기
document.addEventListener('DOMContentLoaded', loadExistingData);
