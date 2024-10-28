document.addEventListener('DOMContentLoaded', function() {
	const container = document.getElementById('orgMap');
	if (!container) {
		console.error("Element with id 'orgMap' not found. Please ensure it exists in the HTML.");
		return;
	}

	fetch('/api/deptMember')
		.then(response => {
			if (!response.ok) throw new Error('Network response was not ok');
			return response.json();
		})
		.then(data => {
			console.log("Fetched data:", data);
			const rootDept = data.departments.find(dept => dept.parentDeptNo === 0);
			const tree = buildDeptTree(data.departments, rootDept);
			container.appendChild(tree);
			addConnections(container, data.departments);
		})
		.catch(error => console.error('Error fetching department data:', error));
});

function buildDeptTree(departments, rootDept) {
	const deptMap = buildDeptMap(departments);
	return createDeptNode(rootDept, deptMap, 1);
}

function buildDeptMap(departments) {
	const deptMap = {};
	departments.forEach(dept => {
		if (!deptMap[dept.parentDeptNo]) deptMap[dept.parentDeptNo] = [];
		deptMap[dept.parentDeptNo].push(dept);
	});
	return deptMap;
}
function createDeptNode(dept, deptMap, depth) {
    const li = document.createElement('li');
    li.classList.add('depth');

    const wrapDiv = document.createElement('div');
    wrapDiv.classList.add('wrap1');
    wrapDiv.setAttribute('id', `tree-no-${dept.deptNo}`);
    wrapDiv.setAttribute('data-real-depth', depth);
    wrapDiv.setAttribute('data-parent-no', dept.parentDeptNo);
    wrapDiv.setAttribute('data-dept-no', dept.deptNo);

    const nameSpan = document.createElement('span');
    nameSpan.classList.add('name');
    nameSpan.textContent = dept.deptName;

    // 이름 수정 기능 (더블 클릭)
    nameSpan.addEventListener('dblclick', () => {
        const input = document.createElement('input');
        input.type = 'text';
        input.value = nameSpan.textContent;
        input.classList.add('dept-edit-input');
        nameSpan.replaceWith(input);
        input.focus();

        input.addEventListener('blur', () => updateDeptName(input, dept.deptNo));
        input.addEventListener('keydown', (event) => {
            if (event.key === 'Enter') updateDeptName(input, dept.deptNo);
        });
    });

    const memberCountDiv = document.createElement('div');
    memberCountDiv.classList.add('member-count');

    const iconSpan = document.createElement('span');
    iconSpan.classList.add('icon');
    iconSpan.textContent = '👤';

    const countSpan = document.createElement('span');
    countSpan.classList.add('count');
    countSpan.textContent = dept.empCnt || 0;

    memberCountDiv.appendChild(iconSpan);
    memberCountDiv.appendChild(countSpan);

    // 추가: member-count div 클릭 시 모달을 열고 data-dept-no 값을 가져오는 이벤트
    memberCountDiv.addEventListener('click', () => {
        const deptNo = wrapDiv.getAttribute('data-dept-no'); // 부모의 data-dept-no 값 가져오기
        console.log("Selected department number:", deptNo);

        // 여기서 모달을 열고 deptNo를 사용할 수 있습니다.
        openModal(deptNo);  // openModal 함수를 통해 모달 열기
    });

    const menuButton = document.createElement('button');
    menuButton.classList.add('menu-btn');
    menuButton.textContent = '⋯';

    const menuDiv = document.createElement('div');
    menuDiv.classList.add('menu', 'hidden');
    const registerButton = document.createElement('button');
    registerButton.classList.add('register-dept');
    registerButton.textContent = '부서 등록';
    const deleteButton = document.createElement('button');
    deleteButton.classList.add('delete-dept');
    deleteButton.textContent = '삭제';

    menuDiv.appendChild(registerButton);
    menuDiv.appendChild(deleteButton);

    menuButton.addEventListener('click', (event) => {
        event.stopPropagation();
        menuDiv.classList.toggle('hidden');
    });

    registerButton.addEventListener('click', () => addSubDept(dept.deptNo, li));
    deleteButton.addEventListener('click', () => deleteDept(dept.deptNo, li));

    document.addEventListener('click', (event) => {
        if (!wrapDiv.contains(event.target)) {
            menuDiv.classList.add('hidden');
        }
    });

    wrapDiv.appendChild(nameSpan);
    wrapDiv.appendChild(menuButton);
    wrapDiv.appendChild(menuDiv);
    wrapDiv.appendChild(memberCountDiv);
    li.appendChild(wrapDiv);

    const childDepts = deptMap[dept.deptNo] || [];
    if (childDepts.length > 0) {
        const ol = document.createElement('ol');
        childDepts.forEach(childDept => {
            ol.appendChild(createDeptNode(childDept, deptMap, depth + 1));
        });
        li.appendChild(ol);
    }

    return li;
}




// 모달 열기 함수
function openModal(deptNo) {
    const modal = document.getElementById("deptModal");
    const deptNoSpan = document.getElementById("deptNo");

    // 부서 번호 설정
    deptNoSpan.textContent = deptNo;

    // 모달 열기
    modal.style.display = "block";

    // 부서에 속한 사원 목록 가져오기
    fetch(`/employees/byDept/${deptNo}`)
        .then(response => response.json())
        .then(data => {
            const employeeListItems = document.getElementById("employeeListItems");
            employeeListItems.innerHTML = ""; // 기존 목록 초기화

            // 사원 목록을 체크박스 형태로 추가
            data.forEach(employee => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <input type="checkbox" name="manager" value="${employee.empNo}">
                    ${employee.empName} (${employee.jobName} - ${employee.posiName})
                `;
                employeeListItems.appendChild(li);
            });
        })
        .catch(error => console.error("Error fetching employees:", error));
}

// 모달 닫기 함수
function closeModal() {
    const modal = document.getElementById("deptModal");
    modal.style.display = "none";
}

// 닫기 버튼 이벤트 리스너
document.querySelector(".close-button").addEventListener("click", closeModal);

// 모달 외부 클릭 시 닫기
window.addEventListener("click", (event) => {
    const modal = document.getElementById("deptModal");
    if (event.target === modal) {
        closeModal();
    }
});

// 부서장 지정 버튼 클릭 이벤트
document.getElementById("setManagerBtn").addEventListener("click", function() {
    const deptNo = document.getElementById("deptNo").textContent;
    const selectedManager = document.querySelector('input[name="manager"]:checked');

    if (!selectedManager) {
        alert("부서장을 지정할 사원을 선택해주세요.");
        return;
    }

    const empNo = selectedManager.value;

    fetch(`/departments/updateManager`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ deptNo, empNo })
    })
    .then(response => response.text())
    .then(result => {
        if (result === "success") {
            alert("부서장이 지정되었습니다.");
            closeModal();
        } else {
            alert("부서장 지정에 실패했습니다.");
        }
    })
    .catch(error => console.error("Error updating manager:", error));
});






// 수정된 부서 정보를 저장하는 배열
let modifiedDepartments = [];

// 하위 부서 등록 함수
function addSubDept(parentDeptNo, li) {
	const inputField = document.createElement('input');
	inputField.type = 'text';
	inputField.placeholder = '새 부서 이름 입력';
	li.appendChild(inputField);
	inputField.focus();

	// 현재 부서의 레벨을 가져와 하위 부서의 레벨을 자동 설정
	const currentLevel = parseInt(li.querySelector('.wrap1').getAttribute('data-real-depth'), 10);
	const newLevel = currentLevel + 1;

	inputField.addEventListener('keydown', (event) => {
		if (event.key === 'Enter') {
			const deptName = inputField.value.trim();
			if (deptName) {
				modifiedDepartments.push({
					deptName: deptName,
					parentDeptNo: parentDeptNo,
					level: newLevel,  // 새로 추가된 레벨 정보
					action: 'add'
				});
				console.log("modifiedDepartments 배열에 추가됨:", modifiedDepartments); // 배열 확인용
				//nputField.remove();
			}
		}
	});
}


// 하위 부서 등록 저장 함수
function saveDept() {
	console.log("saveDept 함수가 호출되었습니다."); // 디버그용
	console.log(modifiedDepartments);
	// modifiedDepartments 배열에 데이터가 있는지 확인
	if (modifiedDepartments.length === 0) {
		alert('변경 사항이 없습니다.');
		return;
	}

	console.log("서버로 전송할 데이터:", modifiedDepartments); // 서버 전송 전에 데이터 확인

	// 배열 전송
	fetch('/dept/save', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(modifiedDepartments)
	})
		.then(response => response.text())
		.then(result => {
			console.log("서버 응답:", result); // 응답 확인
			if (result === 'success') {
				alert('변경 사항이 저장되었습니다.');
				location.reload();
			} else {
				alert('저장에 실패했습니다.');
			}
		})
		.catch(error => console.error('Error saving departments:', error));
}


// 부서 삭제 함수
function deleteDept(deptNo, li) {
	fetch(`/dept/delete/${deptNo}`, { method: 'DELETE' })
		.then(response => response.text())
		.then(result => {
			if (result === 'success') {
				alert('부서가 삭제되었습니다.');
				location.reload();
			} else if (result === 'cannot_delete') {
				alert('이 부서에는 사원이 등록되어 있어 삭제할 수 없습니다.');
			} else {
				alert('삭제에 실패했습니다.');
			}
		})
		.catch(error => console.error('Error deleting department:', error));
}

// 부서 이름 업데이트 함수
function updateDeptName(input, deptNo) {
	const newName = input.value.trim();
	if (!newName) return;

	fetch('/dept/update', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ deptNo, deptName: newName })
	})
		.then(response => response.text())
		.then(result => {
			if (result === 'success') {
				alert('부서 이름이 수정되었습니다.');
				const nameSpan = document.createElement('span');
				nameSpan.classList.add('name');
				nameSpan.textContent = newName;
				input.replaceWith(nameSpan);
			} else {
				alert('수정에 실패했습니다.');
			}
		})
		.catch(error => console.error('Error updating department name:', error));
}



// 부모-자식 간 연결선 설정
function addConnections(container, departments) {
	const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
	svg.classList.add("line-container");
	svg.setAttribute("width", container.offsetWidth);
	svg.setAttribute("height", container.offsetHeight);
	container.prepend(svg);

	departments.forEach(dept => {
		if (dept.parentDeptNo) {
			const parent = container.querySelector(`[data-dept-no="${dept.parentDeptNo}"]`);
			const child = container.querySelector(`[data-dept-no="${dept.deptNo}"]`);
			if (parent && child) {
				const line = createLine(parent, child);
				svg.appendChild(line);
			}
		}
	});
}

//  부모-자식 간 연결선 설정
function createLine(parent, child) {
	const line = document.createElementNS("http://www.w3.org/2000/svg", "path");
	const parentRect = parent.getBoundingClientRect();
	const childRect = child.getBoundingClientRect();
	const svgRect = document.querySelector(".line-container").getBoundingClientRect();

	const x1 = parentRect.left + parentRect.width / 2 - svgRect.left;
	const y1 = parentRect.bottom - svgRect.top;
	const x2 = childRect.left - svgRect.left;
	const y2 = childRect.top + childRect.height / 2 - svgRect.top;

	const d = `M${x1},${y1} V${y2} H${x2}`;
	line.setAttribute("d", d);
	line.classList.add("line");

	return line;
}
