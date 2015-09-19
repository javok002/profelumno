/**
 * Created by tombatto on 19/09/15.
 */
$(function(){
    initDatepicker();
});

function initDatepicker() {
    $('.datepicker').datepicker({
        endDate: '0d',
        language: 'es',
        format: 'dd/mm/yyyy'
    });
}
