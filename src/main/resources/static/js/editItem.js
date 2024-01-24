// <select> 要素を取得
const selectParentElement = document.getElementById('parentCategory');

// すべてのオプションを走査し、重複を追跡
const seenParentOptions = new Set();
for (const option of selectParentElement.options) {
    if (seenParentOptions.has(option.text)) {
        // 既に存在するテキストの場合、オプションを非表示にする
        option.style.display = 'none';
    }else {
    // 新しいテキストの場合、Setに追加
    seenParentOptions.add(option.text);
    }
};

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

    // <select> 要素を取得
    const selectChildElement = document.getElementById('childCategory');
    // すべてのオプションを走査し、重複を追跡
    const seenChildOptions = new Set();
    for (const option of selectChildElement.options) {
        if (seenChildOptions.has(option.text)) {
            // 既に存在するテキストの場合、オプションを非表示にする
            option.style.display = 'none';
        }else {
            // 新しいテキストの場合、Setに追加
            seenChildOptions.add(option.text);
        }
    };

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

    const selectChildElement = document.getElementById('grandCategory');
    // すべてのオプションを走査し、重複を追跡
    const seenChildOptions = new Set();
    for (const option of selectChildElement.options) {
        if (seenChildOptions.has(option.text)) {
            // 既に存在するテキストの場合、オプションを非表示にする
            option.style.display = 'none';
        }else {
            // 新しいテキストの場合、Setに追加
            seenChildOptions.add(option.text);
        }
    }
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

document.getElementById('edit-item').addEventListener('submit', function(event){    
    var confirmResult = confirm("変更しますか？")
    if(!confirmResult){
        event.preventDefault();
    }
});

document.getElementById('reload-button').addEventListener('click', function() {
    location.reload();
});