
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

document.getElementById('edit-category').addEventListener('submit', function(event){    
    var confirmResult = confirm("変更しますか？")
    if(!confirmResult){
        event.preventDefault();
    }
});

document.getElementById('reload-button').addEventListener('click', function() {
    location.reload();
});