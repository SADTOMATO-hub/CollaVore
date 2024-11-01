document.addEventListener("DOMContentLoaded", function () {
    loadDeptTree();
});

async function loadDeptTree() {
    try {
        const response = await fetch('/api/deptMgr');
        const departments = await response.json();

        if (!Array.isArray(departments)) {
            throw new Error("Invalid response format");
        }

        const deptMap = buildDeptMap(departments);
        const container = document.getElementById('DeptTable');
        renderDeptTree(deptMap, container);



    } catch (error) {
        console.error('Error fetching department data:', error);
    }
}


// 부서를 계층 구조로 빌드하는 함수
function buildDeptMap(departments) {
	const deptMap = {};
	departments.forEach(dept => {
		const level = dept.level;

		if (!deptMap[level]) deptMap[level] = [];
		deptMap[level].push(dept);
	});
	return deptMap;
}

// 부서 트리를 렌더링하는 함수
function renderDeptTree(deptMap, container) {
	container.innerHTML = ''; // 기존 내용을 초기화

	Object.keys(deptMap).sort((a, b) => a - b).forEach(level => {
		const levelDiv = document.createElement('div');
		levelDiv.classList.add('tree-level');

		deptMap[level].forEach(dept => {
			const deptNode = createDeptNode(dept);
			levelDiv.appendChild(deptNode);
		});

		container.appendChild(levelDiv);
	});
}

// 트리 구조의 부서 노드를 생성하고 드롭다운 기능 추가
function createDeptNode(dept) {
	const deptDiv = document.createElement('div');
	deptDiv.classList.add('tree-node');
	deptDiv.dataset.deptNo = dept.deptNo;
	deptDiv.dataset.parentDeptNo = dept.parentDeptNo;

	// 부서명 및 부서장 정보 표시
	const deptHeader = document.createElement('div');
	deptHeader.classList.add('dept-header');
	deptHeader.innerHTML = `
            <div class="dept-info">
                <div class="dept-name">
               		${dept.deptName}
                </div>
                <div class="mgr-info">
                	<div class="mgr-image">
                	
                		<img class="profile-image" src="${dept.mgrImg ? `/imgs/${dept.mgrImg}` : '/assets/images/users/default.png'}" alt="부서장 이미지" width="40px">
                	</div>
                	<div class="mgr-basic">
                		<div class="mgr-name">${dept.mgrName}</div>
                		<div class="mgr-job">${dept.posiName}</div>
                	</div>
                </div>
            </div>
            
        `;

	// 부서명을 클릭하면 부서원 목록을 토글
	deptHeader.onclick = () => toggleEmployees(dept.deptNo, deptDiv);

	// 부서원 목록을 위한 div 생성
	const employeeListDiv = document.createElement('div');
	employeeListDiv.className = 'employee-list';
	employeeListDiv.id = `employee-list-${dept.deptNo}`;
	employeeListDiv.style.display = 'none';

	deptDiv.appendChild(deptHeader);
	deptDiv.appendChild(employeeListDiv);

	return deptDiv;
}

document.addEventListener("DOMContentLoaded", function() {
	loadDeptTree();
});


// 외부 클릭 시 employeeListDiv 닫기
function handleClickOutside(event) {
	// 열린 상태인 모든 employee-list 요소 찾기
	const openList = document.querySelector('.employee-list[style*="display: block"]');
	if (openList && !openList.contains(event.target) && !event.target.closest('.tree-node')) {
		closeEmployeeList(openList);
	}
}

function closeEmployeeList(employeeListDiv) {
	if (employeeListDiv) {
		employeeListDiv.style.display = 'none';
		document.querySelectorAll('.tree-node').forEach(node => node.classList.remove('active'));
	}
}

async function toggleEmployees(deptNo, deptDiv) {
	const employeeListDiv = document.getElementById(`employee-list-${deptNo}`);

	// 이미 열려 있는 경우 닫기
	if (employeeListDiv.style.display === 'block') {
		closeEmployeeList(employeeListDiv);
		return;
	}

	// 모든 노드에서 'active' 클래스를 제거하고 employee-list를 닫기
	document.querySelectorAll('.tree-node').forEach(node => node.classList.remove('active'));
	document.querySelectorAll('.employee-list').forEach(list => list.style.display = 'none');

	try {
		const response = await fetch(`/api/deptMember?deptNo=${deptNo}`);
		const employees = await response.json();

		if (employees && employees.length > 0) {
    employeeListDiv.innerHTML = `
        <div class="employee-name">부서원</div>
        ${employees.map(emp => `
            <div class="employee-item" 
                 data-tel="${emp.tel || 'Unknown'}" 
                 data-email="${emp.email || 'Unknown'}">
                <div class="mgr-info">
                    <input type="hidden" name="empNo" value="${emp.empNo}">
                    <div class="mgr-image">
                        <img class="profile-image" src="${emp.img ? `/imgs/${emp.img}` : '/assets/images/users/default.png'}" alt="소속사원 이미지" width="40px">
                    </div>
                    <div class="mgr-basic">
                        <div class="mgr-name">${emp.empName}</div>
                        <div class="mgr-job">${emp.posiName}</div>
                    </div>
                </div>
            </div>
        `).join('')}
    `;
}

		// 위치 계산
		const rect = deptDiv.getBoundingClientRect();
		employeeListDiv.style.display = 'block';

		let topPosition = rect.bottom + window.scrollY;
		let leftPosition = rect.left + window.scrollX;

		if (topPosition + employeeListDiv.offsetHeight > window.innerHeight + window.scrollY) {
			topPosition = rect.top + window.scrollY - employeeListDiv.offsetHeight;
		}

		if (leftPosition + employeeListDiv.offsetWidth > window.innerWidth + window.scrollX) {
			leftPosition = window.innerWidth + window.scrollX - employeeListDiv.offsetWidth - 10;
		}

		employeeListDiv.style.top = `${topPosition}px`;
		employeeListDiv.style.left = `${leftPosition}px`;

		deptDiv.classList.add('active');
	} catch (error) {
		console.error('부서원 정보를 가져오는 중 오류 발생:', error);
	}
}

// 모달 열기 함수
// 모달 열기 함수
function openModal(empName, posiName, img, empNo, tel, email) {
    const modal = document.getElementById("employeeModal");
    var closeModal = document.querySelector(".modal-close");
    
    // 각 요소를 설정하기 전에 존재 여부를 확인
    modal.querySelector("#profilePreview").src = img || "/assets/images/users/default.png";
    modal.querySelector("#empNo").value = empNo;

    // 존재 여부를 콘솔에 출력
    console.log(modal.querySelector(".modal-span.name"));
    console.log(modal.querySelector(".modal-span.tel"));
    console.log(modal.querySelector(".modal-span.email"));
    console.log(modal.querySelector(".modal-span.position"));

    // 각 필드에 값 설정
    const nameElement = modal.querySelector(".modal-span.name");
    const telElement = modal.querySelector(".modal-span.tel");
    const emailElement = modal.querySelector(".modal-span.email");
    const positionElement = modal.querySelector(".modal-span.position");

    if (nameElement) nameElement.innerText = empName;
    if (telElement) telElement.innerText = tel;
    if (emailElement) emailElement.innerText = email;
    if (positionElement) positionElement.innerText = posiName;

    modal.style.display = "block";
}

// 모달 닫기 함수
function closeModal() {
    const modal = document.getElementById("employeeModal");
    modal.style.display = "none";
}

// employee-item 클릭 이벤트 감지 (이벤트 위임 방식)
document.addEventListener("click", function(event) {
    const empItem = event.target.closest(".employee-item");
    if (empItem) {
        console.log("employee-item clicked!");
        
        // 데이터 추출
        const empName = empItem.querySelector(".mgr-name") ? empItem.querySelector(".mgr-name").innerText : "Unknown";
        const posiName = empItem.querySelector(".mgr-job") ? empItem.querySelector(".mgr-job").innerText : "Unknown";
        const img = empItem.querySelector(".profile-image") ? empItem.querySelector(".profile-image").src : "/assets/images/users/default.png";
        const empNo = empItem.querySelector("input[name='empNo']").value;
        const tel = empItem.getAttribute("data-tel") || "Unknown"; // 연락처 데이터가 포함된 경우
        const email = empItem.getAttribute("data-email") || "Unknown"; // 이메일 데이터가 포함된 경우

        // 모달 열기
        openModal(empName, posiName, img, empNo, tel, email);
    }
});

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    const modal = document.getElementById("employeeModal");
    if (event.target == modal) {
        closeModal();
    }
};
