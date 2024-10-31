// 모달 관련 요소 가져오기
var modal = document.getElementById("editModal");
var btn = document.getElementById("editBtn");
var closeBtn = document.querySelector(".modal-close");

// 수정 버튼을 클릭하면 모달을 엽니다.
btn.onclick = function() {
	modal.style.display = "block";
	// 수정 전 원래 값을 저장
	document.getElementById("password").setAttribute(
		"data-original-value",
		document.getElementById("password").value);
	document.getElementById("tel").setAttribute("data-original-value",
		document.getElementById("tel").value);
}

// X 버튼을 클릭하면 모달을 닫습니다.
closeBtn.onclick = function() {
	modal.style.display = "none";
}

// 바깥을 클릭하면 모달을 닫습니다.
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
	}
}

// 비밀번호 유효성 검사
const passwordField = document.getElementById('password');
const passwordError = document.getElementById('passwordError');
const passwordSuccess = document.getElementById('passwordSuccess');

passwordField
	.addEventListener(
		'input',
		function() {
			const passwordValue = passwordField.value.trim();
			const passwordPattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[^A-Za-z\d])[A-Za-z\d@$!%*?&]{8,}$/;

			if (passwordValue
				&& !passwordPattern.test(passwordValue)) {
				passwordError.style.display = 'block';
				passwordSuccess.style.display = 'none';
			} else {
				passwordError.style.display = 'none';
				passwordSuccess.style.display = 'block';
			}
		});

// 연락처 유효성 검사
const telField = document.getElementById('tel');
const telError = document.getElementById('telError');
const telSuccess = document.getElementById('telSuccess');

const telPattern = /^010-\d{4}-\d{4}$/;

telField.addEventListener('input', function() {
	if (telField.value && !telPattern.test(telField.value)) {
		telError.style.display = 'block';
		telSuccess.style.display = 'none';
	} else {
		telError.style.display = 'none';
		telSuccess.style.display = 'block';
	}
});

// 폼 제출 시 유효성 검사 및 기존 값 유지
document.getElementById("editForm").onsubmit = function(event) {
	const passwordValue = passwordField.value.trim();
	const telValue = telField.value.trim();

	// 비밀번호 및 연락처가 비어 있으면 원래 값을 유지
	if (!passwordValue) {
		passwordField.value = passwordField
			.getAttribute("data-original-value");
	}

	if (!telValue) {
		telField.value = telField.getAttribute("data-original-value");
	}

	// 수정된 값이 있을 때만 유효성 검사
	if (passwordValue && !passwordPattern.test(passwordValue)) {
		event.preventDefault(); // 폼 제출 중단
		passwordError.style.display = 'block';
		passwordSuccess.style.display = 'none';
	}

	if (telValue && !telPattern.test(telValue)) {
		event.preventDefault(); // 폼 제출 중단
		telError.style.display = 'block';
		telSuccess.style.display = 'none';
	}
};

// 이미지 미리보기 함수
function previewImage(event) {
	var reader = new FileReader();  // 파일을 읽기 위한 FileReader 객체 생성
	reader.onload = function() {
		var output = document.getElementById('profilePreview');  // 미리보기 이미지 요소
		output.src = reader.result;  // 파일의 내용을 미리보기 이미지로 설정
	};
	reader.readAsDataURL(event.target.files[0]);  // 선택한 파일을 읽어서 미리보기로 보여줌
}
