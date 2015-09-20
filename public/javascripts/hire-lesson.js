/**
 * Created by Paca on 9/19/15.
 */
angular.module('profLesson', [])

    .directive('hireLesson', function(){
        return {
            restrict: 'E',
            link : function(){
                console.log('holu');
            },
            template:
            '<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#hire-modal">' +
                'Hire' +
            '</button>' +

            '<!-- Modal -->' +
            '<div class="modal fade" id="hire-modal" role="dialog">' +
                '<div class="modal-dialog">'+

                    '<!-- Modal content-->' +
                    '<div class="modal-content">' +
                        '<div class="modal-header">' +
                            '<button type="button" class="close" data-dismiss="modal">&times;</button>' +
                            '<h4 class="modal-title">Hire lesson</h4>' +
                        '</div>' +
                        '<div class="modal-body">' +
                            '<div class="box box-primary">' +
                                '<!-- form start -->' +
                                //'<form role="form">' +
                                    '<div class="box-body">' +
                                        '<div class="checkbox">' +
                                            '<label>' +
                                                '<input type="checkbox"> Tomar clase en el domicilio del profesor' +
                                            '</label>' +
                                        '</div>' +
                                        '<div class="checkbox">' +
                                            '<label>' +
                                                '<input type="checkbox"> Tomar clases en mi domicilio' +
                                            '</label>' +
                                        '</div>' +
                                        '<div class="checkbox">' +
                                            '<label>' +
                                                '<input type="checkbox"> El domicilio se pactara despues' +
                                            '</label>' +
                                        '</div>' +
                                        '<!-- Calendar --> ' +
                                                '<div class="box box-solid bg-green-gradient"> ' +
                '<div class="box-header"> ' +
        '                           <i class="fa fa-calendar"></i> ' +
                '                                   <h3 class="box-title">Calendar</h3> ' +
        '                               <!-- tools box --> ' +
                '                                   <div class="pull-right box-tools"> ' +
        '                               <!-- button with a dropdown --> ' +
                '                                   <div class="btn-group"> ' +
        '                           <button class="btn btn-success btn-sm dropdown-toggle" data-toggle="dropdown"><i class="fa fa-bars"></i></button> ' +
                '                                   <ul class="dropdown-menu pull-right" role="menu"> ' +
        '                           <li><a href="#">Add new event</a></li> ' +
                '                                   <li><a href="#">Clear events</a></li> ' +
        '                           <li class="divider"></li> ' +
                '                                   <li><a href="#">View calendar</a></li> ' +
        '                           </ul> ' +
                '</div> ' +
        '                           <button class="btn btn-success btn-sm" data-widget="collapse"><i class="fa fa-minus"></i></button> ' +
                '                                   <button class="btn btn-success btn-sm" data-widget="remove"><i class="fa fa-times"></i></button> ' +
        '                           </div><!-- /. tools --> ' +
                '                                   </div><!-- /.box-header --> ' +
        '                           <div class="box-body no-padding"> ' +
                '                                       <!--The calendar --> ' +
        '                           <div id="calendar" style="width: 100%"></div> ' +
                '                                   </div><!-- /.box-body --> ' +
        '                           <div class="box-footer text-black"> ' +
                '                                   <div class="row"> ' +
        '                           <div class="col-sm-6"> ' +
                '                                       <!-- Progress bars --> ' +
        '                           <div class="clearfix">  ' +
                '                                   <span class="pull-left">Task #1</span>  ' +
        '                           <small class="pull-right">90%</small>  ' +
                '                                   </div>  ' +
        '                           <div class="progress xs">  ' +
                '                                   <div class="progress-bar progress-bar-green" style="width: 90%;"></div>  ' +
        '                           </div>  ' +

                '                                   <div class="clearfix">  ' +
        '                           <span class="pull-left">Task #2</span>  ' +
                '                                   <small class="pull-right">70%</small>  ' +
        '                           </div>  ' +
                '                                   <div class="progress xs">  ' +
        '                           <div class="progress-bar progress-bar-green" style="width: 70%;"></div>  ' +
                '                                   </div>  ' +
        '                           </div><!-- /.col -->  ' +
                '                                   <div class="col-sm-6">  ' +
        '                           <div class="clearfix">  ' +
                '                                   <span class="pull-left">Task #3</span>  ' +
        '                           <small class="pull-right">60%</small>  ' +
                '                                   </div>  ' +
        '                           <div class="progress xs">  ' +
                '                                   <div class="progress-bar progress-bar-green" style="width: 60%;"></div> ' +
        '                           </div> ' +

                '                                   <div class="clearfix"> ' +
        '                           <span class="pull-left">Task #4</span> ' +
                '                                   <small class="pull-right">40%</small> ' +
        '                           </div> ' +
                '                                   <div class="progress xs"> ' +
                '                           <div class="progress-bar progress-bar-green" style="width: 40%;"></div> ' +
                '                           </div> ' +
                '                           </div><!-- /.col --> ' +
                '                           </div><!-- /.row --> ' +
                '                           </div> ' +
            '                           </div><!-- /.box --> ' +


                                        '<div class="input-group">' +
                                            '<span class="input-group-addon"><i class="fa fa-calendar"></i></span>' +
                                            '<input type="email" class="form-control" placeholder="Fecha y horario">' +
                                        '</div>' +
                                        '<div class="form-group">' +
                                            '<label>Textarea</label>' +
                                            '<textarea class="form-control" rows="3" placeholder="Enter ..."></textarea>' +
                                        '</div>' +
                                    '</div><!-- /.box-body -->' +

                                    '<div class="box-footer">' +
                                        '<button type="submit" class="btn btn-primary">Submit</button>' +
                                    '</div>' +
                                //'</form>' +
                            '</div><!-- /.box -->' +
                        '</div>' +
                        '<div class="modal-footer">' +
                            '<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>' +
                        '</div>' +
                    '</div>' +
                '</div>'+
            '</div>'
        }
    });