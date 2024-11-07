document.addEventListener('DOMContentLoaded', function() {
	const container = document.getElementById('orgMap');
	if (!container) {
		console.error("Element with id 'orgMap' not found. Please ensure it exists in the HTML.");
		return;
	}

	// 부서 관리자 API에서 데이터를 가져와 트리 생성
	fetch('/api/deptManager')
		.then(response => {
			if (!response.ok) throw new Error('Network response was not ok');
			return response.json();
		})
		.then(data => {
			const departments = data.departments;  // departments 데이터를 여기서 정의

			const rootDept = departments.find(dept => dept.parentDeptNo === 0);
			const tree = buildDeptTree(departments, rootDept);
			container.appendChild(tree);
			addConnections(container, departments); // 부모-자식 간 연결선 추가

			// deleteDept 함수를 호출할 때 departments 배열을 함께 전달
			container.addEventListener('click', (event) => {
				if (event.target.classList.contains('delete-dept')) {
					const deptNo = parseInt(event.target.closest('.wrap1').getAttribute('data-dept-no'), 10);
					const li = event.target.closest('li');
					deleteDept(deptNo, li, departments); // departments 전달
				}
			});
		})
		.catch(error => console.error('Error fetching department data:', error));
});


// 부서 트리 빌드 함수
function buildDeptTree(departments, rootDept) {
	const deptMap = buildDeptMap(departments);
	return createDeptNode(rootDept, deptMap, 1); // 루트 부서부터 트리 생성 시작
}

// 부서 매핑 생성 함수
function buildDeptMap(departments) {
	const deptMap = {};
	departments.forEach(dept => {
		if (!deptMap[dept.parentDeptNo]) deptMap[dept.parentDeptNo] = [];
		deptMap[dept.parentDeptNo].push(dept);
	});
	return deptMap;
}

// 부서 트리 노드 생성 함수
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

	wrapDiv.appendChild(nameSpan);
	li.appendChild(wrapDiv);

	// 부서 이름 수정 기능 추가
	nameSpan.addEventListener('dblclick', () => editDeptName(nameSpan, dept));

	// 부서 인원 수 표시
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
	memberCountDiv.addEventListener('click', () => openModal(dept.deptNo));

	// 메뉴 버튼 및 부서 추가/삭제 기능
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

		// 모든 메뉴를 닫고 현재 메뉴만 열기
		document.querySelectorAll('.menu').forEach(menu => {
			if (menu !== menuDiv) {
				menu.classList.add('hidden');
			}
		});

		// 현재 메뉴 토글
		menuDiv.classList.toggle('hidden');
	});
	registerButton.addEventListener('click', () => addSubDept(dept.deptNo, li));
	deleteButton.addEventListener('click', () => deleteDept(dept.deptNo, li));
	document.addEventListener('click', (event) => {
		if (!wrapDiv.contains(event.target)) {
			menuDiv.classList.add('hidden');
		}
	});

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


// 하위 부서 등록 및 서버 저장
let modifiedDepartments = [];

// 중복된 이름 확인 함수
function isDeptNameDuplicate(deptName, excludeDeptNo = null) {
	const existingNames = new Set();

	// modifiedDepartments 배열에 저장된 이름 추가
	modifiedDepartments.forEach(dept => {
		if (dept.deptNo !== excludeDeptNo) {  // 수정 시 동일한 부서번호는 제외
			existingNames.add(dept.deptName);
		}
	});

	// 화면에 표시된 부서 이름 추가
	const container = document.getElementById('orgMap');
	const displayedDepartments = Array.from(container.querySelectorAll('.name')).map(node => node.textContent);
	displayedDepartments.forEach(name => existingNames.add(name));

	// 정확히 일치하는 이름만 중복으로 판단
	return existingNames.has(deptName);
}

// 부서 이름 수정 함수
function editDeptName(nameSpan, dept) {
	const input = document.createElement('input');
	input.type = 'text';
	input.value = nameSpan.textContent;
	input.classList.add('dept-edit-input');
	nameSpan.replaceWith(input);
	input.focus();

	// 중복 실행 방지를 위한 플래그
	let updateExecuted = false;

	function handleUpdate() {
		if (updateExecuted) return;
		updateExecuted = true;

		const newName = input.value.trim();

		// 중복된 이름 확인, 현재 부서를 제외
		if (isDeptNameDuplicate(newName, dept.deptNo)) {
			alert("이미 존재하는 부서 이름입니다. 다른 이름을 사용해주세요.");
			input.value = nameSpan.textContent; // 기존 이름으로 유지
			input.focus();
			updateExecuted = false; // 플래그 초기화
			return;
		}

		nameSpan.textContent = newName;
		input.replaceWith(nameSpan);
		modifiedDepartments.push({ deptNo: dept.deptNo, deptName: newName, action: 'update' });
		console.log("현재 modifiedDepartments:", modifiedDepartments); // 디버그용

		// 이벤트 리스너 제거
		input.removeEventListener('blur', handleUpdate);
		input.removeEventListener('keydown', handleKeyDown);
	}

	function handleKeyDown(event) {
		if (event.key === 'Enter') {
			event.preventDefault();
			handleUpdate();
		}
	}

	// 이벤트 리스너 등록
	input.addEventListener('blur', handleUpdate);
	input.addEventListener('keydown', handleKeyDown);
}

// 하위 부서 등록 함수
function addSubDept(parentDeptNo, li) {
	const inputField = document.createElement('input');
	inputField.type = 'text';
	inputField.placeholder = '새 부서 이름 입력';
	inputField.classList.add('new-dept-input');
	inputField.focus();

	const newLevel = parseInt(li.querySelector('.wrap1').getAttribute('data-real-depth'), 10) + 1;
	let childList = li.querySelector('ol');
	if (!childList) {
		childList = document.createElement('ol');
		li.appendChild(childList);
	}

	const newLi = document.createElement('li');
	newLi.classList.add('depth');
	newLi.appendChild(inputField);
	childList.appendChild(newLi);

	// 중복 실행 방지를 위한 플래그
	let saveExecuted = false;

	function handleSave() {
		if (saveExecuted) return;
		saveExecuted = true;

		const deptName = inputField.value.trim();

		// 중복된 이름 확인
		if (isDeptNameDuplicate(deptName)) {
			alert("이미 존재하는 부서 이름입니다. 다른 이름을 사용해주세요.");
			inputField.value = ''; // 중복된 이름일 경우 필드 초기화
			inputField.focus();

			// 중복이 감지된 경우 다시 입력을 받을 수 있도록 플래그 초기화
			saveExecuted = false;
			return; // 중복인 경우 함수 종료
		}

		if (deptName) {
			// 유효한 이름일 때만 modifiedDepartments에 저장하고 화면에 표시
			modifiedDepartments.push({
				parentDeptNo: parentDeptNo,
				deptNo: null,
				deptName: deptName,
				level: newLevel,
				action: 'add'
			});

			// 입력 필드를 새로운 span 요소로 교체하여 스타일 유지
			const nameSpan = document.createElement('span');
			nameSpan.classList.add('name'); // 스타일 유지를 위해 클래스 추가
			nameSpan.textContent = deptName;

			// inputField의 스타일을 nameSpan에 복사
			nameSpan.style.border = inputField.style.border || '1px solid #ccc';
			nameSpan.style.padding = inputField.style.padding || '4px 8px';
			nameSpan.style.borderRadius = inputField.style.borderRadius || '4px';
			nameSpan.style.backgroundColor = inputField.style.backgroundColor || '#f9f9f9';
			nameSpan.style.color = inputField.style.color || '#333';

			inputField.replaceWith(nameSpan); // 입력 필드를 span으로 교체

			// 이벤트 리스너 제거
			inputField.removeEventListener('blur', handleSave);
			inputField.removeEventListener('keydown', handleKeyDown);
		} else {
			newLi.remove(); // 이름이 입력되지 않으면 입력 필드를 제거
		}
	}

	function handleKeyDown(event) {
		if (event.key === 'Enter') {
			event.preventDefault(); // 기본 동작 방지
			handleSave();
		}
	}

	// 이벤트 리스너 등록
	inputField.addEventListener('blur', handleSave);
	inputField.addEventListener('keydown', handleKeyDown);

	inputField.focus();
}






// 저장 버튼을 클릭했을 때만 서버에 저장하는 함수
function saveDept() {
	if (modifiedDepartments.length === 0) {
		alert('변경 사항이 없습니다.');
		return;
	}

	fetch('/dept/save', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(modifiedDepartments)
	})
		.then(response => response.text())
		.then(result => {
			if (result.trim() === 'success') {
				alert('변경 사항이 저장되었습니다.');
				modifiedDepartments = []; // 수정 기록 초기화

				// 전체 트리를 새로 불러와 새로고침 없이 갱신
				loadExistingData(); // 새로 등록된 부서와 수정 사항 반영
			} else {
				alert('저장에 실패했습니다.');
			}
		})
		.catch(error => console.error('Error saving departments:', error));
}

// 데이터를 불러와서 트리를 새로 그리는 함수
function loadExistingData() {
	fetch('/api/deptManager')
		.then(response => {
			if (!response.ok) throw new Error('Network response was not ok');
			return response.json();
		})
		.then(data => {
			const container = document.getElementById('orgMap');
			container.innerHTML = ''; // 기존 트리를 제거하고 새로 추가
			const rootDept = data.departments.find(dept => dept.parentDeptNo === 0);
			const tree = buildDeptTree(data.departments, rootDept);
			container.appendChild(tree);
			addConnections(container, data.departments); // 부모-자식 간 연결선 추가
		})
		.catch(error => console.error('Error fetching department data:', error));
}


// 삭제된 부서를 추적하기 위한 객체
const deletedDepartments = {};

// 부서 삭제 함수
function deleteDept(deptNo, li, departments = []) {
	// 이미 삭제된 부서라면 함수 종료
	if (deletedDepartments[deptNo]) {
		console.log(`Department with deptNo ${deptNo} has already been deleted.`);
		return;
	}

	const deptData = departments.find(dept => dept.deptNo === deptNo);
	if (!deptData) {
		console.log(`Department with deptNo ${deptNo} not found. It may have been deleted already.`);
		return;
	}

	const hasEmployees = deptData.empCnt > 0;
	const hasChildDepartments = departments.some(dept => dept.parentDeptNo === deptNo);

	if (hasEmployees) {
		alert("부서에 소속된 사원이 있어 삭제할 수 없습니다.");
		return;
	}

	if (hasChildDepartments) {
		alert("하위 부서가 있어 삭제할 수 없습니다.");
		return;
	}

	fetch(`/dept/delete/${deptNo}`, { method: 'DELETE' })
		.then(response => response.text())
		.then(result => {
			if (result === 'success') {
				alert('부서가 삭제되었습니다.');
				li.remove();
				refreshConnections();

				// departments 배열에서 삭제된 부서 제거
				const index = departments.findIndex(dept => dept.deptNo === deptNo);
				if (index !== -1) {
					departments.splice(index, 1);
				}

				// 삭제된 부서 추적
				deletedDepartments[deptNo] = true;
			} else {
				alert('삭제에 실패했습니다.');
			}
		})
		.catch(error => console.error('Error deleting department:', error));
}




// 연결선을 다시 그리는 함수
function refreshConnections() {
	const container = document.getElementById('orgMap');
	const svg = container.querySelector('.line-container');
	if (svg) svg.remove(); // 기존 선 제거
	const displayedDepartments = Array.from(container.querySelectorAll('[data-dept-no]')).map(deptNode => ({
		deptNo: parseInt(deptNode.getAttribute('data-dept-no'), 10),
		parentDeptNo: parseInt(deptNode.getAttribute('data-parent-no'), 10),
	}));
	addConnections(container, displayedDepartments);
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

// 모달 열기 함수
function openModal(deptNo) {
	const modal = document.getElementById("deptModal");
	const deptNoSpan = document.getElementById("deptNo");

	// 부서 번호 업데이트
	deptNoSpan.textContent = deptNo;
	
	// 이전 모달 데이터를 초기화
	const managerInfoItems = document.getElementById("managerInfoItems");
	managerInfoItems.innerHTML = "";

	const employeeListItems = document.getElementById("employeeListItems");
	employeeListItems.innerHTML = "";

	// 모달 표시
	modal.style.display = "block";

	// 부서에 속한 사원 목록 가져오기
	findDeptEmp(deptNo);
}

// 부서에 속한 사원 목록을 가져와 모달에 표시
function findDeptEmp(deptNo) {
	fetch(`/employees/byDept/${deptNo}`)
		.then(response => response.json())
		.then(data => {
			const mgrInfo = data.deptMgrInfo;
			const managerInfoItems = document.getElementById("managerInfoItems");
			managerInfoItems.innerHTML = ""; // 초기화
			// 부서장 정보 표시
			if (mgrInfo.empName) {
				const li = document.createElement("li");
				li.innerHTML = `${mgrInfo.empName} (${mgrInfo.jobName} - ${mgrInfo.posiName})`;
				managerInfoItems.appendChild(li);
			} else {
				const li = document.createElement("li");
				li.textContent = "등록된 부서장이 없습니다"; // No registered department manager
				managerInfoItems.appendChild(li);
			}

			// 부서 사원 목록 표시
			const employees = data.deptEmpList;
			const employeeListItems = document.getElementById("employeeListItems");
			employeeListItems.innerHTML = ""; // 초기화

			if (employees.length > 0 && employees[0].empName) {
				employees.forEach(employee => {
					const li = document.createElement("li");
					li.innerHTML = `
						<input type="checkbox" name="emp" value="${employee.empNo}" onclick="selectOnlyThis(this)">
						${employee.empName} (${employee.jobName} - ${employee.posiName})
					`;
					employeeListItems.appendChild(li);
				});
			} else {
				const li = document.createElement("li");
				li.textContent = "등록된 부서원이 없습니다"; // No registered employees
				employeeListItems.appendChild(li);
			}
		})
		.catch(error => console.error("Error fetching employees:", error));
}

function selectOnlyThis(checkbox) {
	const checkboxes = document.querySelectorAll('input[name="emp"]');
	checkboxes.forEach(cb => {
		if (cb !== checkbox) cb.checked = false;
	});
}

// 모달 닫기 함수
function closeModal() {
	const modal = document.getElementById("deptModal");
	modal.style.display = "none";
}

// 닫기 버튼과 외부 클릭 시 모달 닫기 이벤트 설정
document.querySelector("#deptCloseBtn").addEventListener("click", closeModal);
window.addEventListener("click", (event) => {
	const modal = document.getElementById("deptModal");
	if (event.target === modal) {
		closeModal();
	}
});

// 부서장을 지정하는 버튼 클릭 이벤트
document.getElementById("setManagerBtn").addEventListener("click", function() {
	const deptNo = document.getElementById("deptNo").textContent;
	const selectedManager = document.querySelector('input[name="emp"]:checked');

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
				findDeptEmp(deptNo);
			} else {
				alert("부서장 지정에 실패했습니다.");
			}
		})
		.catch(error => console.error("Error updating manager:", error));
});
