document.getElementById("register-user").addEventListener("submit", function(event) {
    var password = document.getElementById("password").value;
    var passwordCheck = document.getElementById("passwordCheck").value;
    var errorMessageDiv = document.getElementById("error-message");

    if (password !== passwordCheck) {
        errorMessageDiv.textContent = "passwordの入力内容と一致しません";
        event.preventDefault(); // フォームの送信を阻止
    } else {
        errorMessageDiv.textContent = ""; // エラーメッセージをクリア
    }
});
