
document.querySelectorAll('input').forEach(function(element) {
    // 初期値を保存
    const initialValue = element.value;

    element.addEventListener('change', function() {
        if (this.value !== initialValue) {
            // 値が変更された場合、クラスを追加
            this.classList.add('changed');
        } else {
            // 値が初期値に戻った場合、クラスを削除
            this.classList.remove('changed');
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const inputs = document.querySelectorAll('input');
    const submitButton = document.getElementById('submit-button');

    inputs.forEach(input => {
        input.addEventListener('change', function () {
            submitButton.disabled = false;
        });
    });
});

// 初期状態でボタンの状態を設定
function editConfirmSubmission(isConfirmed) {
    document.getElementById('edit-modal').style.display = 'none';
    if (isConfirmed) {
        document.getElementById('edit-category').submit();
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const inputs = document.querySelectorAll('input, select');
    const submitButton = document.getElementById('submit-button');

    inputs.forEach(input => {
        input.addEventListener('change', function () {
            submitButton.disabled = false;
        });
    });

    submitButton.addEventListener('click', function(event) {
        event.preventDefault(); // デフォルトの送信を阻止
        document.getElementById('edit-modal').style.display = 'block'; // モーダルを表示
    });
});

function deleteConfirmSubmission(isConfirmed) {
    document.getElementById('delete-modal').style.display = 'none';
    if (isConfirmed) {
      window.location.href = document.getElementById('delete-button').getAttribute('href');
    }
  }

  document.addEventListener('DOMContentLoaded', function() {
    var deleteButton = document.getElementById('delete-button');
    if (deleteButton) {
      deleteButton.addEventListener('click', function(event) {
        event.preventDefault(); // リンクのデフォルト動作を防ぐ
        document.getElementById('delete-modal').style.display = 'block';
      });
    }
  });