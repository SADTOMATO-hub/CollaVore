async function fetchDeptData() {
    try {
        const response = await fetch('/dept/getExistingDepts');  // 실제 API 엔드포인트로 수정
        const departments = await response.json();  // JSON 형태로 변환

        // console.log('서버로부터 받은 부서 데이터:', departments);  // 서버로부터 받은 데이터를 확인

        // 데이터를 확인해보고, 각 데이터의 필드가 존재하는지 확인
        if (departments && departments.length > 0) {
            // 받아온 데이터를 트리 형태로 렌더링
            renderDeptTree(departments);
        } else {
            console.error('서버로부터 받은 부서 데이터가 비어있습니다.');
        }
    } catch (error) {
        console.error('부서 데이터를 가져오는 중 오류 발생:', error);
    }
}

// 부서 데이터를 트리 구조로 렌더링
function renderDeptTree(departments) {
    const deptTable = document.getElementById('DeptTable');
    deptTable.innerHTML = ''; // 기존 내용을 초기화

    const deptMap = {};

    // 부서 데이터를 맵으로 변환 (부서 번호를 키로 사용)
    departments.forEach(dept => {
        deptMap[dept.deptNo] = { ...dept, children: [] }; // 각 부서에 children 배열을 미리 초기화
    });

    // 상위 부서에 자식 부서 연결
    departments.forEach(dept => {
        if (dept.parentDeptNo !== null && dept.parentDeptNo !== 0 && deptMap[dept.parentDeptNo]) {
            deptMap[dept.parentDeptNo].children.push(deptMap[dept.deptNo]);
        }
    });

    // 최상위 부서를 가져와 트리로 렌더링 (parentDeptNo가 null 또는 0인 부서)
    const rootDepartments = departments.filter(dept => dept.parentDeptNo === null || dept.parentDeptNo === 0);
    
    if (rootDepartments.length > 0) {
        // 최상위 부서만 1개를 선택하고 그 하위 부서를 모두 렌더링
        const rootDept = rootDepartments[0];  // 첫 번째 최상위 부서 선택
        const deptHTML = generateDeptHTML(deptMap[rootDept.deptNo]);
        deptTable.appendChild(deptHTML);  // rootDept에 해당하는 HTML 요소 추가
    } else {
        console.error('최상위 부서를 찾을 수 없습니다.');
    }
}

// 부서 트리 HTML을 재귀적으로 생성
function generateDeptHTML(dept) {
    const deptElement = document.createElement('div');
    deptElement.classList.add('dept-item');

    // null 값을 방지하기 위해 기본값 처리
    const deptNo = dept.deptNo !== null ? dept.deptNo : 'N/A';
    const deptName = dept.deptName !== null ? dept.deptName : 'N/A';
    const empCnt = dept.empCnt !== null ? dept.empCnt : '0';

    deptElement.innerHTML = `
        <div class="dept-info">
            <span class="dept-name" onclick="editDeptName(this, ${dept.deptNo})">${deptName}</span>
            <div class="dept-actions">
                <span class="options-icon" onclick="showOptionsMenu(this, ${dept.deptNo})"> 
                    <i class="fas fa-ellipsis-h"></i> 
                </span>
                <span class="emp-count" onclick="viewEmployees(${dept.deptNo})">
                    <i class="fas fa-user"></i> ${empCnt}
                </span>
            </div>
        </div>
    `;
    
    

    // 자식 부서가 있는 경우
    if (dept.children && dept.children.length > 0) {
        const childContainer = document.createElement('div');
        childContainer.classList.add('child-container');
        
        dept.children.forEach(childDept => {
            const childDeptHTML = generateDeptHTML(childDept);  // 재귀적으로 자식 부서도 생성
            childContainer.appendChild(childDeptHTML);
        });

        deptElement.appendChild(childContainer);
    }

    return deptElement;
}

// 페이지가 로드될 때 데이터를 가져와서 트리 구조로 렌더링
document.addEventListener('DOMContentLoaded', () => {
    fetchDeptData();  // 서버에서 데이터를 받아와 렌더링
});