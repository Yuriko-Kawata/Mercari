
document.addEventListener('DOMContentLoaded', function(){
    let currentPage = 1;

    document.getElementById('prev').addEventListener('click', function(){
        if(currentPage > 1){
            currentPage --;
            fetchData(currentPage);
        }
    });
    
    document.getElementById('next').addEventListener('click', function(){
        if(currentPage < totalPage){
            currentPage ++;
            fetchData(currentPage);
        }
    });

    
})
