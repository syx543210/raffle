<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>EChart</title>
    <!-- 引入 ECharts 文件 -->

    <link rel="stylesheet" href="/bootstrap3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="/bootstrapTable/dist/bootstrap-table.css">
    <link rel="stylesheet" href="/bootstrapTable/pagejump/bootstrap-table-pagejump.css">
    <link rel="stylesheet" href="/jquery/jquery-confirm.min.css">
    <#--<link rel="stylesheet" href="/bootstrap/font-awesome-4.7.0/css/font-awesome.css">-->

    <#--<script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts-all-3.js"></script>-->
    <script type="text/javascript" src="/echarts/dist/echarts.min.js"></script>

    <script type="text/javascript" src="/jquery/jquery-3.5.1.min.js"></script>
    <script type="text/javascript" src="/jquery/jquery-confirm.min.js"></script>

    <#--<script type="text/javascript" src="/bootstrap/js/bootstrap.bundle.js"></script>-->
    <script type="text/javascript" src="/bootstrap3.3.7/js/bootstrap.js"></script>
    <script type="text/javascript" src="/bootstrapTable/dist/bootstrap-table.min.js"></script>
    <script type="text/javascript" src="/bootstrapTable/dist/locale/bootstrap-table-zh-CN.min.js"></script>
    <script type="text/javascript" src="/bootstrapTable/pagejump/bootstrap-table-pagejump.js"></script>

</head>
<body>

<!-- 为ECharts准备一个具备大小（宽高）的Dom -->

<table id="dataTable" style="text-align: center;" cellspacing="0" cellpadding="1" border="1px">
    <#--<tr>-->
        <#--<th>序号</th>-->
        <#--<th>日期</th>-->
        <#--<th>期数</th>-->
        <#--<th>第一个数</th>-->
        <#--<th>第二个数</th>-->
        <#--<th>第三个数</th>-->
    <#--</tr>-->
</table>

<div >
    <div class="form-inline" style="width: 40%">
        <input type="text" class="form-control" id="searchVal" placeholder="请输入名称">
        <button id="dosearch" type="button" class="btn btn-default">
           查询
        </button>
    </div>
</div>

<div id="toolbar" class="btn-group">
    <button id="check_select" type="button" class="btn btn-default">
        复选框选择
    </button>
    <#--<button id="btn_edit" type="button" class="btn btn-default">-->
        <#--<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改-->
    <#--</button>-->
    <#--<button id="btn_delete" type="button" class="btn btn-default">-->
        <#--<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除-->
    <#--</button>-->
</div>
<table id="tb"></table>


<div id="main" style="width: 800px;height:400px;"></div>

<button onclick="btnClick()">清除</button>
<button onclick="showData(1)">近七天</button>
</body>


<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('main'));
    $(function () {
        // 基于准备好的dom，初始化echarts实例
        // showData(1);


        //1.初始化Table
        var oTable = new TableInit();
        oTable.Init();

        //2.初始化Button的点击事件
        var oButtonInit = new ButtonInit();
        oButtonInit.Init();


    });





    function btnClick() {
        // option.title.text = "aaa";
        // option.xAxis.data = ["1", "2", "3", "4", "5", "6"];
        // myChart.setOption(option);
        myChart.clear();
    }

    function showData(type) {
        $.ajax({
            type: "GET",
            url: "/history/showBack",
            // async:false,
            data: {username: "1111", content: "111", type: type},
            dataType: "json", //预期服务器返回数据的类型
            success: function (data) {
                if (data.success) {
                    // 指定图表的配置项和数据


                    var itemStyle = {
                        normal: {
                            label: {
                                show: true,  //开启显示
                                position: 'top',  //在上方显示
                                textStyle: {  //数值样式
                                    color: 'black',
                                    fontSize: 16
                                }
                            }
                        }
                    };

                    var dataList = data.dataList;

                    var a =dataList.forEach(tem => {
                        tem.itemStyle = itemStyle;
                })

                    var option = {
                        title: {
                            text: data.title
                        },
                        tooltip: {},
                        legend: {
                            data: data.numberTitle
                        },
                        xAxis: {
                            data: data.dateTitleList
                        },
                        yAxis: {},
                        series: dataList,
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);

                    var tableLabel = $("#dataTable");
                    var historyData = data.historyData;
                    tableLabel.empty();
                    var tableHead = "<tr>\n" +
                        "<th>序号</th>\n" +
                        "<th>日期</th>\n" +
                        "<th>期数</th>\n" +
                        "<th>第一个数</th>\n" +
                        "<th>第二个数</th>\n" +
                        "<th>第三个数</th>\n" +
                        "</tr>\n";
                    tableLabel.append(tableHead);
                    for (var i = 0; i < historyData.length; i++) {
                        tableLabel.append(" <tr>\n" +
                            "<td>" + (i + 1) + "</td>\n" +
                            "<td>" + (historyData[i].date===null?"":formateDate(historyData[i].date)) + "</td>\n" +
                            "<td>" + (historyData[i].term===null?"": historyData[i].term) + "</td>\n" +
                            "<td>" + (historyData[i].firstNum===null?"":historyData[i].firstNum) + "</td>\n" +
                            "<td>" + (historyData[i].secondNum===null?"":historyData[i].secondNum) + "</td>\n" +
                            "<td>" + (historyData[i].thirdNum===null?"":historyData[i].thirdNum) + "</td>\n" +
                            "</tr>\n");
                    }


                } else {
                    alert("出现错误：" + data.msg);
                }
            },
            error: function (jqXHR) {
                alert("发生错误：" + jqXHR.status);
            }
        });
    }

    function formateDate(date) {
        var newDate = new Date(date);
        var year = newDate.getFullYear();
        var month =(newDate.getMonth() + 1).toString();
        var day = (newDate.getDate()).toString();
        if (month.length == 1) {
            month = "0" + month;
        }
        if (day.length == 1) {
            day = "0" + day;
        }
        var dateTime = year + "-" + month + "-" + day;
        return dateTime;
    }


    var TableInit = function () {
        var oTableInit = new Object();
        //初始化Table
        oTableInit.Init = function () {
            $('#tb').bootstrapTable({
                url: '/history/getTableData',         //请求后台的URL（*）
                method: 'get',                      //请求方式（*）
                toolbar: '#toolbar',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: false,                     //是否启用排序
                sortOrder: "asc",                   //排序方式
                queryParams: oTableInit.queryParams,//传递参数（*）
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber:1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
                paginationShowPageGo: true,
                search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                strictSearch: true,
                showColumns: true,                  //是否显示所有的列
                showRefresh: true,                  //是否显示刷新按钮
                minimumCountColumns: 2,             //最少允许的列数
                clickToSelect: true,                //是否启用点击选中行
                height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
                showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
                cardView: false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                columns: [{
                    checkbox: true
                },{
                    field: 'date',
                    title: '日期',
                    formatter:dateFormat
                }, {
                    field: 'term',
                    title: '期数'
                }, {
                    field: 'firstNum',
                    title: '第一个数'
                }, {
                    field: 'secondNum',
                    title: '第二个数'
                }, {
                    field: 'thirdNum',
                    title: '第三个数'
                }, ]
            });
        };

        //得到查询的参数
        oTableInit.queryParams = function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit,   //页面大小
                offset: params.offset,  //页码
                search: $("#searchVal").val() //查询
                // departmentname: $("#txt_search_departmentname").val(),
                // statu: $("#txt_search_statu").val()
            };
            return temp;
        };
        return oTableInit;
    };


    var ButtonInit = function () {
        var oInit = new Object();
        var postdata = {};

        oInit.Init = function () {
            //初始化页面上面的按钮事件

            //查询按钮
            $('#dosearch').click(function() {
                var params = $('#tb').bootstrapTable('getOptions');
                $('#tb').bootstrapTable('refresh', params)
            });


            //复选框选择按钮
            $("#check_select").click(function () {
                var a= $("#tb").bootstrapTable("getSelections");
                if(a.length<=0){
                    $.confirm({
                        title: '提示',
                        content: '请至少选择一个渠道',
                        type: 'green',
                        icon: 'glyphicon glyphicon-question-sign',
                        buttons: {
                            ok: {
                                text: '确认',
                                btnClass: 'btn-primary',
                                action: function () {

                                }
                            },
                            cancel: {
                                text: '取消',
                                btnClass: 'btn-primary'
                            }
                        }
                    });
                }else {
                    $.confirm({
                        title: '确认',
                        content: '确认选中么?',
                        type: 'green',
                        icon: 'glyphicon glyphicon-question-sign',
                        buttons: {
                            ok: {
                                text: '确认',
                                btnClass: 'btn-primary',
                                action: function() {
                                    var b = JSON.stringify(a);
                                    console.log(b);
                                    // $.ajax({
                                    //     type: "post",
                                    //     url: "/channel/batchUpdateStatus",
                                    //     dataType: "json",
                                    //     data: {
                                    //         "datalist": b
                                    //     },
                                    //     success: function (data) {
                                    //
                                    //     },
                                    //     beforeSend: function () {
                                    //
                                    //     },
                                    //     error: function (jqXHR) {
                                    //         alert("发生错误：" + jqXHR.status);
                                    //     }
                                    // });
                                }
                            },
                            cancel: {
                                text: '取消',
                                btnClass: 'btn-primary'
                            }
                        }
                    });
                }
            })


        };

        return oInit;
    };

    function dateFormat(value, row, index) {
        return formateDate(value);
    }


</script>

</html>