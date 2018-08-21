window.onload = function () {
    var systemFlag = $("#systemFlag");
    var opt = null;
    $.ajax({
        url: "getSystemFlag",
        //type、contentType必填,指明传参方式
        type: "post",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success: function (response) {
            if (response.result != null) {
                $.each(response.result, function (n, value) {
                    opt = new Option(value, value)
                    systemFlag[0].options.add(opt);
                });
            }
        }
    });

}

function interfaceSubmit() {
    var interfaceCode = $("#interfaceName").val();
    var systemFlag = $("#systemFlag").val();
    $.post("getInterfaceTemplates",
        {"interfaceCode": interfaceCode, "systemFlag": systemFlag},
        function (response) {
            //前端调用成功后，可以处理后端传回的json格式数据。
            if (null != response) {
                var result = response.result;
                var projectList = result[0].list;
                var headList = result[1].list;
                var bodyList = result[2].list;

                $("#projectDiv").show();
                var flag = $("#flag").val();
                if (null == flag || "" == flag) {
                    $.each(projectList, function (n, value) {
                        $("#projectName").html($("#projectName").html() + value.fieldName);
                        $("#projectInterface").html($("#projectInterface").html() + value.length);
                        $("#interfaceCode").html($("#interfaceCode").html() + value.description);
                        $("#flag").val(true);
                    });
                }
                //下面使用each进行遍历报文头的信息
                var headbody = "";
                var headLength = "";
                $.each(headList, function (n, value) {
                    var trs = "";
                    trs += "<tr><td>" + value.fieldName + "(" + value.description + ")" + "</td><td>" + value.length + "</td><td>" + '<input type="text" style=' + "width:98%" + ' name = ' + value.fieldName + '/>' + "</td></tr>";
                    headbody += trs;
                    headLength = headLength + value.length + ",";
                });
                $("#headLength").val(headLength);
                //判断当前创建的表格中是否有tr属性，有的话则不创建新的表单，若无则创建
                if ($('#headTable').find('tr:visible').length == 0) {
                    $("#headTable").append(headbody);
                }
                //下面使用each进行遍历报文体的信息
                var tbody = "";
                var bodyLength = "";
                $.each(bodyList, function (n, value) {
                    var trs = "";
                    trs += "<tr><td>" + value.fieldName + "(" + value.description + ")" + "</td> <td>" + value.length + "</td><td>" + '<input type="text" style=' + "width:98%" + ' name= ' + value.fieldName + '/>' + "</td></tr>";
                    tbody += trs;
                    bodyLength = bodyLength + value.length + ",";
                });
                $("#bodyLength").val(bodyLength);
                //判断当前创建的表格中是否有tr属性，有的话则不创建新的表单，若无则创建
                if ($('#bodyTable').find('tr:visible').length == 0) {
                    $("#bodyTable").append(tbody);
                }
            }
            $("#headDiv").show();
            $("#bodyDiv").show();
        }, "json");
}

function saveHeadMessage() {
    var headSubmitForm = $("#headSubmitForm").serialize();
    headSubmitForm = headSubmitForm.replace(/%2F/g, "");
    var array = headSubmitForm.split("&");
    var params = "";
    for (var i = 0; i < array.length; i++) {
        params = params + array[i] + ",";
    }
    //获取接口标识
    var systemFlag = $("#systemFlag").val();
    //获取接口的长度
    var headLength = $("#headLength").val();
    $.post("saveInterfaceHeadMessage", {"systemFlag": systemFlag, "params": params, "headLength": headLength},
        function (data) {
            alert(data.result);
        }, "json");
}

function saveBodyMessage() {
    var bodySubmitForm = $("#bodySubmitForm").serialize();
    bodySubmitForm = bodySubmitForm.replace(/%2F/g, "");
    var array = bodySubmitForm.split("&");
    var params = "";
    for (var i = 0; i < array.length; i++) {
        params = params + array[i] + ",";
    }
    //获取接口号
    var interfaceCode = $("#interfaceName").val();
    //获取接口的长度
    var headLength = $("#headLength").val();
    //获取接口标识
    var systemFlag = $("#systemFlag").val();
    $.post("getInterfaceCallBack", {
            "systemFlag": systemFlag,
            "interfaceCode": interfaceCode,
            "params": params,
            "headLength": headLength
        },
        function (data) {
            //下面使用each进行遍历回复包的信息
            var callBackList = data.result;
            var cbody = "";
            $.each(callBackList, function (n, value) {
                var trs = "";
                trs += "<tr><td>" + value.fieldName + "(" + value.description + ")" + "</td> <td>" + value.length + "</td><td>" + '<input type="text" name = "body"/>' + "</td></tr>";
                cbody += trs;
            });
            //判断当前创建的表格中是否有tr属性，有的话则不创建新的表单，若无则创建
            if ($('#callbackTable').find('tr:visible').length == 0) {
                $("#callbackTable").append(cbody);
            }
            if (null != callBackList) {
                $("#callbackDiv").show();
            }
        }, "json");
}