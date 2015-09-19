/**
 * Created by Nicolás Burroni on 13/09/15.
 */
$(function(){
    initDatepicker();
    initICheck();
});

function initDatepicker() {
    $('.datepicker').datepicker({
        endDate: '0d'
    });
}

function initICheck() {
    $('input').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
    });
}