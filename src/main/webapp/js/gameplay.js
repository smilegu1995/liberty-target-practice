const timerDisplay = document.querySelector(".timer-display");
var scoreVal = document.getElementById('scoreVal');
const countDownTime = 1;
let runningTimer;
var sliderH = document.getElementById('SliderH');
sliderH.oninput = function() {
  console.log(this.value);
}
var sliderV = document.getElementById('SliderV');
sliderV.oninput = function() {
  console.log(this.value);
}
// Run on the page load
$(startGame());

function startGame() {
  runTimer();

  // Create EventSource object
  var source = new EventSource('/SentryTargetChallenge/gameapp/game/gamestream');

  source.onmessage = function(e) {
	  updateScore(e);
  };
}

function updateScore(event) {
  console.log("EVENT DATA: " + event.data);
  var gameevent = JSON.parse(event.data);
  scoreVal.textContent = gameevent.score;
}

function displayTime(decSeconds) {
  const minutes = Math.floor(decSeconds / 600);
  const restDecSecs = decSeconds % 600;
  const seconds = Math.floor(restDecSecs / 10);
  const deciSeconds = restDecSecs % 10;

  const displayMins = `${minutes < 10 ? "0" : ""}${minutes}`;
  const displaySecs = `${seconds < 10 ? "0" : ""}${seconds}`;
  const displayDecSecs = `${deciSeconds}`;

  const display = `${displayMins}:${displaySecs}.${displayDecSecs}`;

  timerDisplay.textContent = display;
}

function runTimer() {
  clearInterval(runningTimer);
  let timer = 600;

  // start interval
  runningTimer = setInterval(() => {
    const runTimer = timer--;

    // if time is up (reached max of 60 secs) stop timer
    if (runTimer < countDownTime) {
      clearInterval(runningTimer);
      pageRedirect();
      return;
    }

    // display timer
    displayTime(timer);
  }, 100);
}

function pageRedirect() {
  window.location.replace("results.html");
}

function fire() {
  console.log("Fire");
}

