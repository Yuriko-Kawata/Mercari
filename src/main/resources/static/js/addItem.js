
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
document.getElementById('image').addEventListener('change', function(event) {
    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('imagePreview');
        output.src = reader.result;
        output.style.display = 'block'; // 画像を表示
    };
    reader.readAsDataURL(event.target.files[0]);
});
