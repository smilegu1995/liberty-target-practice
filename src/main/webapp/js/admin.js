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
  
//Run on the page load
  $(getDevicesStatus());

  function getDevicesStatus() {
    $.get("/SentryTargetChallenge/gameapp/game/devicestatus", function(data) {
      showDevicesStatus(data);
    });
  }
  
  function showDevicesStatus(data) {
	  console.log("GET DATA: " + JSON.stringify(data));
	}