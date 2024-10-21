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

// 새 레벨 추가 함수
function addLevel() {
    levelCounter++;
    const table = document.getElementById('positionTable');
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>Level. ${levelCounter}</td>
        <td><input type="text" placeholder="새 직위 입력" /></td>
        <td><button onclick="removeLevel(this)"><i class="fas fa-minus"></i></button></td>
    `;
    table.appendChild(row);
    reorderLevels();
}

// 레벨 삭제 함수
function removeLevel(button, posiNo = null) {
    // 확인 대화 상자
    if (confirm('정말로 삭제하시겠습니까?')) {
        if (posiNo) {
            // 서버로 삭제 요청
            fetch(`/positions/delete/${posiNo}`, {
                method: 'DELETE'
            })
            .then(response => response.text())
            .then(result => {
                if (result === 'success') {
                    button.parentElement.parentElement.remove();
                    reorderLevels();
                } else {
                    alert('삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error deleting position:', error);
                alert('삭제 중 오류가 발생했습니다.');
            });
        } else {
            // 새로 추가된 레벨의 경우 화면에서만 삭제
            button.parentElement.parentElement.remove();
            reorderLevels();
        }
    }
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

    rows.forEach((row, index) => {
        if (index === 0) return; // 첫 번째는 헤더이므로 제외
        const posiNo = row.cells[1].querySelector('input').dataset.posiNo || null;
        const posiName = row.cells[1].querySelector('input').value.trim();
        if (posiName === '') {
            nonName = true;
        }
        positions.push({
            posiNo: posiNo,
            posiName: posiName,
            grade: index // 레벨을 등급으로 사용
        });
    });

    if (nonName) {
        alert("직위명이 입력되지 않았습니다. 확인 후 다시 시도해주세요.");
        return;
    }

    console.log(positions);  // 전송할 데이터를 콘솔에 출력
    // 서버에 데이터 전송 (AJAX 사용)
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
            alert('저장되었습니다.');
            loadExistingData();
        } else {
            alert('저장에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('에러 발생!');
    });
}

// 페이지 로드 시 기존 데이터 불러오기
document.addEventListener('DOMContentLoaded', loadExistingData);
