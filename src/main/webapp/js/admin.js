$.extend( $.ui.slider.prototype.options, { 
  animate: 300
});

$("#z-slider")
.slider({
        animate: "slow",
        min: 0, 
        max: 15, 
        step: 3 
})
.slider("pips", {
    rest: "label"
});

$( "#z-slider" ).on( "slide", function( event, ui ) {
  console.log(ui.value)
  sendSocket(ui.value);
} );

$("#y-slider")
.slider({
        animate: "slow",
        min: 0, 
        max: 180, 
        step: 20 
})
.slider("pips", {
    rest: "label"
});


$( "#y-slider" ).on( "slide", function( event, ui ) {
  console.log(ui.value)
  sendSocket(ui.value);
} );

var target_status = document.getElementById('target_status');
var target_ip = document.getElementById('target_ip');
var target_button = document.getElementById('target_button');


//Run on the page load
$(getDevicesStatus());

$("#target_button1").click(function() {
  sendDeviceCommandReq("targets", "target1");
});
$("#target_button2").click(function() {
  sendDeviceCommandReq("targets", "target2");
});
$("#target_button3").click(function() {
  sendDeviceCommandReq("targets", "target3");
});
$("#target_button4").click(function() {
  sendDeviceCommandReq("targets", "target4");
});
$("#target_button5").click(function() {
  sendDeviceCommandReq("targets", "target5");
});

$("#allup_button").click(function() {
  sendDeviceCommandReq("targets", "allup");
});
$("#alldown_button").click(function() {
  sendDeviceCommandReq("targets", "alldown");
});
$("#cycle_button").click(function() {
  sendDeviceCommandReq("targets", "cycle");
});
$("#ship_firebutton").click(function() {
  sendSocket("fireLaser");
});


function sendDeviceCommandReq(device, cmd){
    $.ajax({
      type: "POST",
      url: "/SentryTargetChallenge/adminapp/admin/txcmd/"+ device +"/" + cmd,
      success: success,
      error: fail,
      dataType: "json"
    });
}

function sendDeviceCommandReq(device, cmd, value){
    $.ajax({
      type: "POST",
      url: "/SentryTargetChallenge/adminapp/admin/txcmd/"+ device +"/" + cmd + "/" + value,
      success: success,
      error: fail,
      dataType: "json"
    });
}

function getDevicesStatus() {
  $.get("/SentryTargetChallenge/adminapp/admin/devices/targets", function(data) {
    showDevicesStatus(data, "targets");
  });
  $.get("/SentryTargetChallenge/adminapp/admin/devices/ship", function(data) {
    showDevicesStatus(data, "ship");
  });
}

function showDevicesStatus(data, device) {
  console.log("GET DATA: " + JSON.stringify(data));
  if (device === "targets") {
    if (data.targets_connected === "true") {
      target_status.textContent = "connected";
      target_ip.textContent = data.targets_ip;
    }
    else {
      target_button.disabled = false;
      target_status.textContent = "not connected";
    }
  } else if (device === "ship") {
      if (data.ship_connected === "true") {
        ship_status.textContent = "connected";
        ship_ip.textContent = data.ship_ip;
      } else {
        ship_button.disabled = false;
        ship_status.textContent = "not connected";
      }
  }
}

function success() {
  console.log("success!");
}

function fail() {
  alert("Device not connected!");
}



////////////////Socket Code/////////////////
var websocket = null;
var websocket_url = null;

$(init('/SentryTargetChallenge/shipsocket'));

function init(url) {
  console.log("init %o, %s, %s", websocket, url);
  if ( websocket !== null ) {
    websocket.close();
    websocket = null;
  }

  // Set the URL, always reset the use_encoder attribute
  websocket_url = "ws://" + window.document.location.host + url;
  use_encoder = false;
  console.log(".. init %s, %s", url);

}


function sendSocket(payload) {
  console.log("sendSocket %o, %s", websocket, websocket_url);
  if ( websocket === null ) {
    websocket = new WebSocket(websocket_url);

    websocket.onerror = function(event) {
    }

    websocket.onopen = function(event) {
      if ( payload ) {
        websocket.send(payload);
      }
    }

    websocket.onclose = function(event) {
      websocket = null;
    }

    websocket.onmessage = function(event) {
    }
  } else if ( payload ) {
    websocket.send(payload);
  }

  console.log(".. sendSocket %o, %s", websocket, websocket_url);
  return websocket;
}