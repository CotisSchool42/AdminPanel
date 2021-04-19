// формируем новые поля
jQuery('.plus').click(function(){
    jQuery('.information_json_plus').before(
        '<tr>' +
        '<td><input type="text" class="form-control" name="information_json_name[]" placeholder="Название поля"></td>' +
        '<td><input type="text" class="form-control" name="information_json_val[]" placeholder="Значение поля"></td>' +
        '<td><span class="btn btn-danger minus pull-right">–</span></td>' +
        '</tr>'
    );
});
// on - так как элемент динамически создан и обычный обработчик с ним не работает
jQuery(document).on('click', '.minus', function(){
    jQuery( this ).closest( 'tr' ).remove(); // удаление строки с полями
});
function demo() {
    alert("Thymeleaf with CSS and Js Demo");
}
