var wsServer = 'ws://127.0.0.1:8080/bikes/bike';
var websocket;
var bike;
var user;
var list;
var json;
var content;
var infowindow;
//初始化地图对象，加载地图
var map = new AMap.Map("container", {
    resizeEnable: true,
    zoomEnable: false,
    dragEnable: false,
    doubleClickZoom: false,
    touchZoom: false
});
map.plugin('AMap.Geolocation', function () {
    geolocation = new AMap.Geolocation({
        enableHighAccuracy: true,//是否使用高精度定位，默认:true
        timeout: 10000,          //超过10秒后停止定位，默认：无穷大
        buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
        zoomToAccuracy: true    //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
    });
    map.addControl(geolocation);
    geolocation.getCurrentPosition();
});
var markers = [{
    icon: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_b1.png',
    position: [116.206053, 39.910266]
}, {
    icon: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_b2.png',
    position: [116.205477, 39.909661]
}, {
    icon: 'http://webapi.amap.com/theme/v1.3/markers/n/mark_b3.png',
    position: [116.206401, 39.910106]
}];
markers.forEach(function (marker) {
    new AMap.Marker({
        map: map,
        icon: marker.icon,
        position: [marker.position[0], marker.position[1]],
        offset: new AMap.Pixel(-12, -36)
    });
});
function connect() {
    websocket = new WebSocket(wsServer);
    websocket.onopen = function (evt) {
        onOpen(evt)
    };
    websocket.onmessage = function (evt) {
        onMessage(evt)
    };
    websocket.onerror = function (evt) {
        onError(evt)
    };
    function onOpen(evt) {
        console.log("Connected to WebSocket server.");
    }
    function onClose(evt) {
        console.log("Disconnected");
    }
    function onMessage(evt) {
        var result = JSON.parse(evt.data); //解析数据
        switch (result.code) { //获取状态码
            case "200":{
                switch (result.method) { //获取请求方法
                    case "connect":{
                        console.log("connect");
                        if(user!=null){
                            content='<div class="info-title">校园租车</div><div class="info-content"><table cellpadding="0">' +
                            '<tr><td class="input-label">车牌号</td><td><div class="keyword-input"><input id="user_id" class="keyword" type="text"></div></td></tr>' +
                            '<tr><td><div class="button-group"><button class="button" onclick="start()" value="确认用车">确认用车</button></div></td></tr></table></div>';
                            infowindow = new AMap.InfoWindow({
                                content: content
                            });
                            infowindow.open(map,map.getCenter());
                        }else{
                            content='<div class="info-title">校园租车</div><div class="info-content"><table cellpadding="0">' +
                            '<tr><td class="input-label">学号</td><td><div class="keyword-input"><input id="user_id" class="keyword" type="text"></div></td></tr>' +
                            '<tr><td class="input-label">密码</td><td><div class="keyword-input"><input id="password" class="keyword" type="password"></div></td></tr>' +
                            '<tr><td class="input-label">姓名</td><td><div class="keyword-input"><input id="username" class="keyword" type="text"></div></td></tr>' +
                            '<tr><td><div class="button-group"><button class="button" onclick="submit()" value="登录">登录</button></div></td></tr></table></div>';
                            infowindow = new AMap.InfoWindow({
                                content: content
                            });
                            infowindow.open(map,map.getCenter());
                        }
                        break;
                    }
                    case "login":{
                        user = result.t;
                        console.log('user_id: ' + user.user_id); //获取用户id
                        find();
                        break;
                    }
                    case "find":{
                        list = result.t;
                        for (var i = 0; i < list.length; i++) {
                            console.log("find: " + list[i].bike_id);
                            console.log("find: " + list[i].addrx);
                            console.log("find: " + list[i].addry);
                            markers.push("");
                        }
                        break;
                    }
                    case "start":{
                        bike = result.t;
                        console.log("start: " + bike.bike_id);
                        console.log("start: " + bike.code);
                        break;
                    }
                    case "end":{
                        console.log("end");
                        break;
                    }
                }
                break;
            }
            case "500":{
                console.log("error :" + result.message); //获取错误状态信息
                break;
            }
        }
    }
    function onError(evt) {
        console.log('Error occured: ' + evt.data);
    }
}
function submit() {
    user = new Object(); //建立内层对象
    user.user_id = "02240130209";
    user.username = "季旭";
    user.password = "13174452";
    json = new Object(); //建立外层对象
    json.method = "login"; //调用的方法名
    json.user = user;
    var message = JSON.stringify(json);
    if (websocket != null) {
        websocket.send(message); //发送数据
    } else {
        connect(); //断线重连
        websocket.send(message);
    }
}
function find() {
    json = new Object(); //建立外层对象
    json.method = "find"; //调用的方法名
    var message = JSON.stringify(json);
    if (websocket != null) {
        websocket.send(message); //发送数据
    } else {
        connect(); //断线重连
        websocket.send(message);
    }
}
function start() {
    bike = new Object(); //建立内层对象
    bike.bike_id = "111111";
    json = new Object(); //建立外层对象
    json.method = "start"; //调用的方法名
    json.bike = bike;
    var message = JSON.stringify(json);
    if (websocket != null) {
        websocket.send(message); //发送数据
    } else {
        connect(); //断线重连
        websocket.send(message);
    }
}
function end() {
    bike.addrx = "116.205467";
    bike.addry = "39.907761";
    json = new Object(); //建立外层对象
    json.method = "end"; //调用的方法名
    json.bike = bike;
    var message = JSON.stringify(json);
    if (websocket != null) {
        websocket.send(message); //发送数据
    } else {
        connect(); //断线重连
        websocket.send(message);
    }
}
