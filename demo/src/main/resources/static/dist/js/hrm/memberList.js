document.addEventListener('DOMContentLoaded', function() {
    const searchBox = document.getElementById('searchBox');
    const filters = ['deptName', 'jobName', 'posiName', 'workType'].map(id => document.getElementById(id));
    
    const resetButton = document.querySelector('button');
    resetButton.addEventListener('click', function() {
        location.reload(); 
    });
    
	function updateTableData() {
	    const searchText = searchBox.value.trim();
	    const deptFilter = document.getElementById('deptName').value.trim();
	    const jobFilter = document.getElementById('jobName').value.trim();
	    const posiFilter = document.getElementById('posiName').value.trim();
	    const workTypeFilter = document.getElementById('workType').value.trim();
	    
	    const baseParams = `searchText=${encodeURIComponent(searchText)}&deptFilter=${encodeURIComponent(deptFilter)}&jobFilter=${encodeURIComponent(jobFilter)}&posiFilter=${encodeURIComponent(posiFilter)}&workTypeFilter=${encodeURIComponent(workTypeFilter)}`;

	    fetch(`/memberList?${baseParams}&fragment=memberListBody`, { headers: { "X-Requested-With": "XMLHttpRequest" } })
	        .then(response => response.text())
	        .then(html => {
	            const doc = new DOMParser().parseFromString(html, 'text/html');
	            const tableBody = document.querySelector('.memberList_table tbody');
	            const newBody = doc.querySelector('.memberList_table tbody');

	            if (newBody && newBody.children.length > 0) {
	                // 결과가 있는 경우 테이블에 결과를 표시합니다.
	                tableBody.innerHTML = newBody.innerHTML;
	            } else {
	                // 결과가 없는 경우 안내 메시지를 표시합니다.
	                tableBody.innerHTML = `<tr><td colspan="8">검색 결과가 없습니다.</td></tr>`;
	            }
	        });

	    fetch(`/memberList?${baseParams}&fragment=pagination`, { headers: { "X-Requested-With": "XMLHttpRequest" } })
	        .then(response => response.text())
	        .then(html => {
	            const doc = new DOMParser().parseFromString(html, 'text/html');
	            document.querySelector('.pagination-and-insert-wrapper').innerHTML = doc.querySelector('.pagination-and-insert-wrapper').innerHTML;
	        });
	}


    const debounce = (func, delay) => {
        let timeout;
        return (...args) => {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), delay);
        };
    };

    const debouncedUpdateTableData = debounce(updateTableData, 300);
    searchBox.addEventListener('input', debouncedUpdateTableData);
    filters.forEach(filter => filter.addEventListener('change', debouncedUpdateTableData));
});