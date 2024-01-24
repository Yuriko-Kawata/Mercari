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
    }


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
