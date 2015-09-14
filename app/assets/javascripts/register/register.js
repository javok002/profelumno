/**
 * Created by Nicolás Burroni on 13/09/15.
 */
$(function(){
    initICheck();
    initDatepicker();
});

function initDatepicker() {
    $('.datepicker').datepicker({
        endDate: '0d',
        language: 'es',
        format: 'dd/mm/yyyy'
    });
}

function initICheck() {
    $('input').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
    });
}