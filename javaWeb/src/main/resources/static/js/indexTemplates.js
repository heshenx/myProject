//初始化下拉框参数
window.onload = function () {
    var systemFlag = $("#systemFlag");
    var opt = null;
    $.post("getComboboxOption", {"type": "SYSTEM_FLAG"},
        function (data) {
            for (var p in data.result[0]) {
                opt = new Option(data.result[0][p], data.result[0][p]);
                systemFlag[0].options.add(opt);
            }
        }, "json");
    // random(1);
}

//改变主题颜色
function setActiveStyleSheet(title) {
    var i, a;
    for (i = 0; (a = document.getElementsByTagName("link")[i]); i++) {
        if (a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
            a.disabled = true;
            if (a.getAttribute("title") == title) a.disabled = false;
        }
    }
}

function random(index){
    var length = document.getElementsByTagName("link").length;
    for(var i = 0 ; i<= length -1; i++ ){
        document.getElementsByTagName("link")[i].disabled = true;
        if(i == index){
            document.getElementsByTagName("link")[index - 1].disabled =false;
        }
    }
    setTimeout('random(Math.ceil(Math.random() * 3))',1000);
}

//重置按钮
function reset() {
    $("#interfaceName").val("");
    $("#systemFlag").val("  ");
}

//重置按钮
function bodyReset() {
    $("#headSubmitForm").form("clear");
    $("#bodySubmitForm").form("clear");
}

//根据接口号和项目标识提交
function interfaceFormSubmit() {
    var interfaceCode = $("#interfaceName").val();
    var systemFlag = $("#systemFlag").val();
    $("#main").hide();
    $.post("getInterfaceTemplates", {"interfaceCode": interfaceCode, "systemFlag": systemFlag}, function (response) {
        //前端调用成功后，可以处理后端传回的json格式数据。
        if (null != response) {
            var result = response.result;
            if (typeof(result) == "undefined") {
                $("#errorDiv").show();
                $("#projectDiv").hide();
                var errorflag = $("#errorflag").val();
                if (null == errorflag || "" == errorflag) {
                    $("#errorName").html($("#errorName").html() + "当前接口号对应的excel模板不存在，请添加模板后重试");
                    $("#errorflag").val(true);
                }
                return;
            }
            var projectList = result[0].list;//接口说明和项目信息
            var headList = result[1].list;//请求头信息
            var bodyList = result[2].list;//请求体信息

            $("#projectDiv").show();
            $("#hrDiv").show();
            $("#errorDiv").hide();
            $("#projectDiv").children("span").remove();
            $("#projectDiv").children("hr").remove();
            var projectDiv = document.getElementById("projectDiv");
            $.each(projectList, function (n, value) {
                var span1 = document.createElement('span');
                span1.innerHTML = "项目名称：" + value.fieldName + "&nbsp;&nbsp;&nbsp;";

                var span2 = document.createElement('span');
                span2.innerHTML = "接口名称：" + value.length + "&nbsp;&nbsp;&nbsp;";

                var span3 = document.createElement('span');
                span3.innerHTML = "接口号：" + value.description + "&nbsp;&nbsp;&nbsp;";

                var span4 = document.createElement('span');
                span4.innerHTML = "IP地址&端口号：" + value.type;
                $("#ipAddressPort").val(value.type);

                var hr = document.createElement('hr');
                projectDiv.appendChild(span1);
                projectDiv.appendChild(span2);
                projectDiv.appendChild(span3);
                projectDiv.appendChild(span4);
                projectDiv.appendChild(hr);
            });

            var headSubmitForm = document.getElementById("headSubmitForm");
            $("#headSubmitForm").children("div").remove();
            var headLength = "";
            $.each(headList, function (n, value) {
                var div = document.createElement('div');
                div.setAttribute("class", "form-row");

                var div1 = document.createElement('div');
                div1.setAttribute("class", "field-label");
                var label = document.createElement('label');
                label.innerHTML = value.fieldName + "(" + value.description + ")" + value.length + ":";
                div1.appendChild(label);

                var div2 = document.createElement('div');
                div2.setAttribute("class", "field-widget");
                var type = value.type;
                if (type == 'textbox') {
                    var input = document.createElement('input');
                    input.name = value.fieldName;
                    input.id = value.fieldName;
                    input.setAttribute("class", "required");
                    div2.appendChild(input);
                } else if (type == 'combobox') {
                    var select = document.createElement('select')
                    select.name = value.fieldName;
                    select.id = value.fieldName;
                    select.setAttribute("class", "validate-selection");
                    $.post("getComboboxOption", {"type": value.fieldName},
                        function (data) {
                            for (var p in data.result[0]) {
                                var opt = new Option(data.result[0][p], p);
                                select.options.add(opt);
                            }
                        }, "json");
                    div2.appendChild(select);
                }
                div.appendChild(div1);
                div.appendChild(div2);
                headSubmitForm.appendChild(div);
                headLength = headLength + value.length + ",";
            });
            $("#headLength").val(headLength);

            var bodySubmitForm = document.getElementById("bodySubmitForm");
            $("#bodySubmitForm").children("div").remove();
            var bodyLength = "";
            $.each(bodyList, function (n, value) {
                var div = document.createElement('div');
                div.setAttribute("class", "form-row");

                var div1 = document.createElement('div');
                div1.setAttribute("class", "field-label");
                var label = document.createElement('label');
                label.innerHTML = value.fieldName + "(" + value.description + ")" + value.length + ":";
                div1.appendChild(label);

                var div2 = document.createElement('div');
                div2.setAttribute("class", "field-widget");
                var type = value.type;
                if (type == 'textbox') {
                    var input = document.createElement('input');
                    input.name = value.fieldName;
                    input.id = value.fieldName;
                    input.setAttribute("class", "required");
                    div2.appendChild(input);
                } else if (type == 'combobox') {
                    var select = document.createElement('select')
                    select.name = value.fieldName;
                    select.id = value.fieldName;
                    select.setAttribute("class", "validate-selection");
                    $.post("getComboboxOption", {"type": value.fieldName},
                        function (data) {
                            for (var p in data.result[0]) {
                                var opt = new Option(data.result[0][p], p);
                                select.options.add(opt);
                            }
                        }, "json");
                    div2.appendChild(select);
                }
                div.appendChild(div1);
                div.appendChild(div2);
                bodyLength = bodyLength + value.length + ",";
                bodySubmitForm.appendChild(div);
            });
            $("#bodyLength").val(bodyLength);
            $("#main").show();
            $("#head").show();
            $("#body").show();
            $("#right").hide();
        }
    });
}

//保存报文头信息
function saveHeadMessage() {
    var headSubmitForm = $("#headSubmitForm").serialize();
    headSubmitForm = decodeURIComponent(headSubmitForm, true);
    var array = headSubmitForm.split("&");
    var params = "";
    for (var i = 0; i < array.length; i++) {
        params = params + array[i] + ",";
    }
    //获取接口标识
    var systemFlag = $("#systemFlag").val();
    //获取接口的长度
    var headLength = $("#headLength").val();
    //获取接口号
    var interfaceCode = $("#interfaceName").val();
    var result = "";
    $.post("saveInterfaceHeadMessage", {
            "systemFlag": systemFlag,
            "interfaceCode": interfaceCode,
            "params": params,
            "headLength": headLength
        },
        function (data) {
        }, "json");
}

//提交返回报文
function submit() {
    saveHeadMessage();
    var bodySubmitForm = $("#bodySubmitForm").serialize();
    bodySubmitForm = decodeURIComponent(bodySubmitForm, true);
    var array = bodySubmitForm.split("&");
    var params = "";
    for (var i = 0; i < array.length; i++) {
        params = params + array[i] + ",";
    }
    //获取接口号
    var interfaceCode = $("#interfaceName").val();
    //获取接口的长度
    var headLength = $("#bodyLength").val();
    //获取接口标识
    var systemFlag = $("#systemFlag").val();
    var ipAddressPort = $("#ipAddressPort").val();
    var callSubmitForm = document.getElementById("callSubmitForm");
    $("#callSubmitForm").children("div").remove();
    $.post("getInterfaceCallBack", {
        "systemFlag": systemFlag,
        "interfaceCode": interfaceCode,
        "params": params,
        "headLength": headLength,
        "ipAddressPort": ipAddressPort
    }, function (data) {
        if (data.errorResult != null) {
            alert("请求异常：" + data.errorResult);
            return;
        }
        //下面使用each进行遍历回复包的信息
        $.each(data.result, function (n, value) {
            var div = document.createElement('div');
            div.setAttribute("class", "form-row");

            var div1 = document.createElement('div');
            div1.setAttribute("class", "field-label");
            var label = document.createElement('label');
            label.innerHTML = value.fieldName + "(" + value.description + ")" + value.length + ":";
            div1.appendChild(label);

            var div2 = document.createElement('div');
            div2.setAttribute("class", "field-widget");
            var input = document.createElement('input');
            input.name = value.fieldName;
            input.id = value.fieldName;
            input.setAttribute("class", "required");
            input.value = value.value;
            div2.appendChild(input);
            div.appendChild(div1);
            div.appendChild(div2);
            callSubmitForm.appendChild(div);
        });
        $("#right").show();
    });
}