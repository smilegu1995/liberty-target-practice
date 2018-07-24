const timerDisplay = document.querySelector(".timer-display");
var scoreVal = document.getElementById('scoreVal');
var beamText = document.getElementById('beamText');
const countDownTime = 1;
let runningTimer;
var moveTilt = false;
var movePan = false;
var anglePan = 125;
var angleTilt = 6;
var moveUp = 0;
var moveDown = 0;
var moveLeft = 0;
var moveRight = 0;
var websocket = null;
var websocket_url = null;
var beamToggle = false;
window.addEventListener('keyup', arrowUp)
window.addEventListener('keydown', arrowDown)

$("#fireLaser").click(function() {
  if (!beamToggle) {
    beamText.textContent = "BEAM OFF";
    toggleBeamOff();
    beamToggle = true;
  } else {
    beamText.textContent = "BEAM ON";
    toggleBeamOn();
    beamToggle = false;
  }
  fireLaser();
});

// Mouse action
$("#arrowUp").mousedown(function() {
  moveTilt = true;
  moveUp -= 1;
});
$("#arrowDown").mousedown(function() {
  moveTilt = true;
  moveDown += 1;
});
$("#arrowLeft").mousedown(function() {
  movePan = true;
  moveLeft += 2;
});
$("#arrowRight").mousedown(function() {
  movePan = true;
  moveRight -= 2;
});

$("#arrowUp").mouseup(function() {
  tiltShip();
});
$("#arrowDown").mouseup(function() {
  tiltShip();
});
$("#arrowLeft").mouseup(function() {
  panShip();
});
$("#arrowRight").mouseup(function() {
  panShip();
});

var scoreMusic = $('#audio-score')[0];
var scoreMusic500 = $('#audio-score500')[0];
var laserSound = $('#audio-laser')[0];
var countdownSound = $('#audio-cd')[0];

function startGame() {
  showGameBoard();

  runTimer();

  // Create EventSource object
  var source = new EventSource(
    '/SentryTargetChallenge/gameapp/game/gamestream');

  source.onmessage = function(e) {
    updateScore(e);
  };
}

// Keyboard action
function arrowDown(e) {
  e.preventDefault();
  if (e.which == 32) {
    const key = document.querySelector(`.fire-key[data-key="${e.which}"]`);
    key.classList.add('press');
  } else {
    const key = document.querySelector(`.arrow-key[data-key="${e.which}"]`);
    key.classList.add('press');
  }
  if (e.which == 37) {
    //console.log("Keyboard - Moving left!!");
    movePan = true;
    moveLeft += 2;
  } else if (e.which == 39) {
    //console.log("Keyboard - Moving right!!");
    movePan = true;
    moveRight -= 2;
  } else if (e.which == 38) {
    //console.log("Keyboard - Moving up!!");
    moveTilt = true;
    moveUp -= 1;
  } else if (e.which == 40) {
    //console.log("Keyboard - Moving down!!");
    moveTilt = true;
    moveDown += 1;
  }
}

function arrowUp(e) {
  e.preventDefault();
  if (e.which == 32) {
    const key = document.querySelector(`.fire-key[data-key="${e.which}"]`);
    key.classList.remove('press');
    if (!beamToggle) {
      beamText.textContent = "BEAM OFF";
      toggleBeamOff();
      beamToggle = true;
    } else {
      beamText.textContent = "BEAM ON";
      toggleBeamOn();
      beamToggle = false;
    }
  } else {
    const key = document.querySelector(`.arrow-key[data-key="${e.which}"]`);
    key.classList.remove('press');
  }
  console.log("MoveRight=" + moveRight + " MoveLeft=" + moveLeft + " moveUp=" +
    moveUp + " moveDown=" + moveDown);
  if (e.which == 37 || e.which == 39) {
    panShip();
  } else if (e.which == 38 || e.which == 40) {
    tiltShip();
  } else if (e.which == 32) {
    fireLaser();
  }
}

function toggleBeamOn() {
  $('.fire-key').css('background', '#2ecc71').css('box-shadow',
    '-1px 1px 0 #15B358, -2px 2px 0 #15B358, -3px 3px 0 #15B358, -4px 4px 0 #15B358'
  );
  $('.fire-key.press').css('box-shadow',
    '0px 0px 0 #15B358, 0px 0px 0 #15B358, 0px 0px 0 #15B358, -1px 1px 0 #15B358'
  );
  $('.fire-key:active').css('box-shadow',
    '0px 0px 0 #3C93D5, 0px 0px 0 #15B358, 0px 0px 0 #15B358, -1px 1px 0 #15B358'
  );

}

function toggleBeamOff() {
  $('.fire-key').css('background', '#e74c3c').css('box-shadow',
    '-1px 1px 0 #CE3323, -2px 2px 0 #CE3323, -3px 3px 0 #CE3323, -4px 4px 0 #CE3323'
  );
  $('.fire-key.press').css('box-shadow',
    '0px 0px 0 #CE3323, 0px 0px 0 #CE3323, 0px 0px 0 #CE3323, -1px 1px 0 #CE3323'
  );
  $('.fire-key:active').css('box-shadow',
    '0px 0px 0 #CE3323, 0px 0px 0 #CE3323, 0px 0px 0 #CE3323, -1px 1px 0 #CE3323'
  );
  laserSound.play();
}

function tiltShip() {
  if (moveTilt) {
    angleTilt += moveUp + moveDown;
    if (angleTilt <= 0) {
      angleTilt = 0;
    } else if (angleTilt >= 15) {
      angleTilt = 15;
    }
  }

  // Reset values
  moveUp = 0;
  moveDown = 0;
  moveTilt = false;

  // Send new angle to server
  console.log("AngleV: " + angleTilt);
  sendSocket("V=" + angleTilt);
}


function panShip() {
  if (movePan) {
    anglePan += moveLeft + moveRight;
    if (anglePan <= 90) {
      anglePan = 90;
    } else if (anglePan >= 150) {
      anglePan = 150;
    }
  }

  // Reset values
  moveLeft = 0;
  moveRight = 0;
  movePan = false;

  // Send new angle to server
  console.log("AngleH: " + anglePan);
  sendSocket("H=" + anglePan);
}

function updateScore(event) {
  console.log("EVENT DATA: " + event.data);
  var gameevent = JSON.parse(event.data);
  scoreVal.textContent = gameevent.score;
  var score = parseInt(gameevent.score);
  if (score % 500 == 0 && score > 0) {
    scoreMusic500.play();
  } else {
    scoreMusic.play();
  }

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
    if (runTimer == 100) {
      countdownSound.play();
    }
    // if time is up (reached max of 60 secs) stop timer
    if (runTimer < countDownTime) {
      clearInterval(runningTimer);
      sendSocket("stopShip");
      websocket.close();
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

function fireLaser() {
  console.log("FIRE!");
  sendSocket("fireLaser");
}

/**********************************************************
 *********************** TERMINAL **************************
 ***********************************************************/
var bgMusic = $('#audio-bg')[0],
  playing = true;

bgMusic.addEventListener('ended', function() {
  this.currentTime = 0;
  if (playing) {
    this.play();
  }
}, false);

bgMusic.play();

var bind = Function.prototype.bind,
  $append = bind.call(Element.prototype.appendChild, document.querySelector(
    "output")),
  $new = bind.call(Document.prototype.createElement, document),
  $text = bind.call(Document.prototype.createTextNode, document),
  $rnd = function() {
    return 0;
  },
  $promise = function(thenFn) {
    var args, promise, wait, slice = Array.prototype.slice,
      isResolved = false;
    var promise = {
      wait: function(ms) {
        wait = ms;
        return promise;
      },
      then: function() {
        args = slice.call(arguments);
        return promise = $promise(thenFn);
      },
      resolve: function() {
        isResolved = true;
        if (args) {
          var next = Function.prototype.bind.apply(thenFn, [undefined].concat(
            args).concat([promise]));
          wait ? setTimeout(next, wait) : next();
          if (jQuery.inArray("#", args) != -1) {
            bgMusic.pause();
            setTimeout("startGame()", 1000);
          }
        }
      }
    };
    return promise;
  };

var process = function(target, chars, promise) {
  var first = chars[0],
    rest = chars.slice(1);
  if (!first) {
    promise.resolve();
    return;
  }
  target.appendChild(first);
  setTimeout(process.bind(undefined, target, rest, promise), $rnd());
}

var type = function(text, promise) {
  var chars = text.split("").map($text);
  promise = promise || $promise(type);
  $append($new("br"));
  process($append($new("q")), chars, promise);
  return promise;
};
printToTerminal();

function printToTerminal() {
  $("#gameShow").hide();
  // Start WebSocket
  init('/SentryTargetChallenge/shipsocket');
  sendSocket("Hello Earthlings!");

  type("Initiating systems...")
    .then("Connecting to Target and Spaceship systems... ")
    .then("Engaging all Targets... OK")
    .wait(500)
    .then("Starting the Spaceship... OK")
    .wait(500)
    .then("Laser Energy output... 98.9%")
    .wait(500)
    .then("All systems are green")
    .wait(1000)
    .then("#");
}

function showGameBoard() {
  $("#terminalShow").hide();
  $("#gameShow").show();
}

/***********************************************************
 *********************** WEB SOCKET ************************
 ***********************************************************/
function init(url) {
  console.log("init %o, %s, %s", websocket, url);
  if (websocket != null) {
    websocket.close();
    websocket = null;
  }

  // Set the URL, always reset the use_encoder attribute
  websocket_url = "ws://" + window.document.location.host + url;
  console.log(".. init %s, %s", url);

}


function sendSocket(payload) {
  console.log("sendSocket %o, %s", websocket, websocket_url);
  if (websocket === null) {
    websocket = new WebSocket(websocket_url);

    websocket.onerror = function(event) {
      console.log('Error: ' + event.data);
    }

    websocket.onopen = function(event) {
      console.log("Connection established!");
      // Start the Space Ship
      sendSocket("startShip");
    }

    websocket.onclose = function(event) {
      websocket = null;
      webSocketConnected = false;
      console.log("Connection closed : " + event.code);
    }

    websocket.onmessage = function(event) {
      console.log("Message" + event.data);
    }
  } else if (payload) {
    websocket.send(payload);
  }

  console.log(".. sendSocket %o, %s", websocket, websocket_url);
  return websocket;
}
