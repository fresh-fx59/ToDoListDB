$(function(){

    const appendToDo = function(data){
        var toDoCode = '<a href="#" class="toDo-link" data-id="' +
            data.id + '">' + data.name + '</a><br>';
        $('#book-list')
            .append('<div>' + bookCode + '</div>');
    };

    //Loading books on load page
//    $.get('/books/', function(response)
//    {
//        for(i in response) {
//            appendBook(response[i]);
//        }
//    });

    //Show adding book form
    $('#show-add-toDo-form').click(function(){
        $('#toDo-form').css('display', 'flex');
    });

    //Closing adding book form
    $('#toDo-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Getting toDo
    $(document).on('click', '.toDo-link', function(){
        var link = $(this);
        var toDoId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/todos/' + toDoId,
            success: function(response)
            {
                var code = '<span>Сделано:' + response.isDone + '</span>';
                link.parent().append(code);
            },
            error: function(response)
            {
                if(response.status == 404) {
                    alert('Дело не найдена!');
                }
            }
        });
        return false;
    });

    //Adding book
    $('#save-toDo').click(function()
    {
        var data = $('#toDo-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/todos/',
            data: data,
            success: function(response)
            {
                $('#toDo-form').css('display', 'none');
                var toDo = {};
                toDo.id = response;
                var dataArray = $('#toDo-form form').serializeArray();
                for(i in dataArray) {
                    toDo[dataArray[i]['name']] = dataArray[i]['value'];
                }
                appendTodo(book);
            }
        });
        return false;
    });

});