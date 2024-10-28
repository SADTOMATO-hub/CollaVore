document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/deptMember')
        .then(response => response.json())
        .then(data => renderDeptTree(data.departments, data.membersByDept))
        .catch(error => console.error('Error fetching department data:', error));
});

function renderDeptTree(departments, membersByDept) {
    const container = document.getElementById('DeptTable');
    const deptMap = buildDeptMap(departments);

    // 최상위 부서 (parentDeptNo가 0인 부서)부터 시작하여 트리를 만듦
    departments.filter(dept => dept.parentDeptNo === 0).forEach(dept => {
        const deptNode = createDeptNode(dept, deptMap, membersByDept);
        container.appendChild(deptNode);
    });
}

function buildDeptMap(departments) {
    const deptMap = {};
    departments.forEach(dept => {
        if (!deptMap[dept.parentDeptNo]) deptMap[dept.parentDeptNo] = [];
        deptMap[dept.parentDeptNo].push(dept);
    });
    return deptMap;
}

function createDeptNode(dept, deptMap, membersByDept) {
    const deptDiv = document.createElement('div');
    deptDiv.classList.add('tree-node');

    const deptName = document.createElement('span');
    deptName.classList.add('dept-name');
    deptName.textContent = `${dept.deptName} (직원 수: ${dept.empCnt})`;
    deptDiv.appendChild(deptName);

    const members = membersByDept[dept.deptNo] || [];
    members.forEach(member => {
        const memberDiv = document.createElement('div');
        memberDiv.classList.add('dept-emp-count');
        memberDiv.textContent = `- ${member.name} (${member.position})`;
        deptDiv.appendChild(memberDiv);
    });

    if (deptMap[dept.deptNo]) {
        const subDepts = deptMap[dept.deptNo];
        subDepts.forEach(subDept => {
            const subDeptNode = createDeptNode(subDept, deptMap, membersByDept);
            subDeptNode.classList.add('sub-dept');
            deptDiv.appendChild(subDeptNode);
        });
    }

    return deptDiv;
}
