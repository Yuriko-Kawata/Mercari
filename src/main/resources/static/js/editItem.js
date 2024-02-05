document.getElementById('parentCategory').addEventListener('change', function() {
    var select = this;
    var selectedOption = select.options[select.selectedIndex];
    var selectedId = selectedOption.getAttribute('data-parent-id');
    var childOptions = document.querySelectorAll('#childCategory option');
    var inputValue = this.value;
    
    document.getElementById('childCategory').disabled = inputValue.trim() === '';
    
    childOptions.forEach(function(option) {
        if (option.getAttribute('data-child-parentId') === selectedId) {
            option.style.display = 'block'; // 親カテゴリに対応する子カテゴリを表示
        } else {
            option.style.display = 'none'; // それ以外の子カテゴリを非表示
        }
    });
});

document.getElementById('childCategory').addEventListener('change', function() {
    var select = this;
    var selectedOption = select.options[select.selectedIndex];
    var selectedId = selectedOption.getAttribute('data-child-id');
    var grandOptions = document.querySelectorAll('#grandCategory option');
    var inputValue = this.value;
    
    document.getElementById('grandCategory').disabled = inputValue.trim() === '';

    grandOptions.forEach(function(option) {
        if (option.getAttribute('data-grand-id') === selectedId) {
            option.style.display = 'block'; // 親カテゴリに対応する子カテゴリを表示
        } else {
            option.style.display = 'none'; // それ以外の子カテゴリを非表示
        }
        
    });
});



document.querySelectorAll('input, select, textarea').forEach(function(element) {
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

document.addEventListener('DOMContentLoaded', function() {
    const radioButtons = document.querySelectorAll('input[type="radio"][name="condition"]');
    let initialCondition = null;
    
    // 初期状態で選択されているラジオボタンを特定
    radioButtons.forEach(function(radio) {
        if (radio.checked) {
            initialCondition = radio;
        }
    });

    radioButtons.forEach(function(radio) {
        radio.addEventListener('change', function() {
            if (this !== initialCondition) {
                // 現在の選択が初期状態と異なる場合、クラスを追加
                this.labels[0].classList.add('changed');
            } else {
                // 現在の選択が初期状態と同じ場合、クラスを削除
                this.labels[0].classList.remove('changed');
            }

            // 別のラジオボタンが選択された場合、それ以外のラジオボタンのラベルからクラスを削除
            radioButtons.forEach(function(otherRadio) {
                if (otherRadio !== radio && otherRadio.labels[0].classList.contains('changed')) {
                    otherRadio.labels[0].classList.remove('changed');
                }
            });
        });
    });
});

function editConfirmSubmission(isConfirmed) {
    document.getElementById('edit-modal').style.display = 'none';
    if (isConfirmed) {
        document.getElementById('edit-item').submit();
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const inputs = document.querySelectorAll('input, select, textarea');
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
      // 削除処理の実行。この部分は実際の削除処理に合わせて変更してください。
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

document.getElementById('image').addEventListener('change', function(event) {
    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('imagePreview');
        output.src = reader.result;
        output.style.display = 'block'; // 画像を表示
    };
    reader.readAsDataURL(event.target.files[0]);
});
