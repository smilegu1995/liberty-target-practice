$.extend( $.ui.slider.prototype.options, { 
  animate: 300
});

$("#z-slider")
.slider({
    max: 90,
    min: -90,
    values: []
})
.slider("pips", {
    step: 15,
    rest: "label",
    labels: { first: "-90", last: "90" }
})
.slider("float");

$("#y-slider")
.slider({
    max: 90,
    min: -90,
    values: []
})
.slider("pips", {
    step: 15,
    rest: "label",
    labels: { first: "-90", last: "90" }
})
.slider("float");

var target_status = document.getElementById('target_status');
var target_ip = document.getElementById('target_ip');
var target_button = document.getElementById('target_button');


//Run on the page load
$(getDevicesStatus());
$(".target_button1").click(function() {
  sendDeviceCommandReq("targets", "target1");
});

function sendDeviceCommandReq(device, cmd){
    $.ajax({
      type: "POST",
      url: "/SentryTargetChallenge/adminapp/admin/txcmd/"+ device +"/" + cmd,
      data: user,
      success: success,
      dataType: "text"
    });
}

function getDevicesStatus() {
  $.get("/SentryTargetChallenge/adminapp/admin/devices", function(data) {
    showDevicesStatus(data);
  });
}

function showDevicesStatus(data) {
  console.log("GET DATA: " + JSON.stringify(data));
  if (data.targets_connected === "true")
	  target_status.textContent = "connected";
  else{
    target_button.disabled = false;
	  target_status.textContent = "not connected";
  }
  
  target_ip.textContent = data.targets_ip;
}

function success() {
  console.log("success!");
}