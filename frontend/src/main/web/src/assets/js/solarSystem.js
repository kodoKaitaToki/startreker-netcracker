var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {return typeof obj;} : function (obj) {return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;}; // cleaning console

console.clear();
var scene, camera, renderer, controls;
//var raycaster = new THREE.Raycaster(),INTERSECTED;
//var mouse = new THREE.Vector2();
var quaternion = new THREE.Quaternion();
var renderManager;
var curve, startName, startPoint, sizePoint, finishName, finishPoint;

var planetsInfo =
{
  sun: { texture: "https://preview.ibb.co/gADrB7/sol.jpg",
    position: 0,
    size: 25
},

  mercury: { texture: "https://preview.ibb.co/g8ZpjS/mercurio.jpg",
    position: -40,
    size: 1
},

  venus: { texture: "https://preview.ibb.co/i1uE17/venus.jpg",
    position: 50,
    size: 2
},

  earth: { texture: "https://preview.ibb.co/fwZWg7/tierra4.jpg",
    position: -60,
    size: 2
},

  mars: { texture: "https://preview.ibb.co/gQLOB7/marte.jpg",
    position: 70,
    size: 1
},
  jupiter: { texture: "https://preview.ibb.co/hAAjJn/jupiter.jpg",
    position: -100,
    size: 10
},
  saturn: { texture: "https://image.ibb.co/j2r8dn/Saturn.jpg",
    position: 125,
    size: 9
},
  uranus: { texture: "https://preview.ibb.co/kthWyn/uranus.jpg",
    position: -155,
    size: 9
},
  neptune: { texture: "https://preview.ibb.co/cZ1DB7/Neptune.jpg",
    position: 180,
    size: 9
},
  pluto: { texture: "https://preview.ibb.co/cZ1DB7/Neptune.jpg",
    position: -200,
    size: 2
} };

function main(){

  init();
  animate();

}

function init() {

  renderer = new THREE.WebGLRenderer({ alpha: true });
  renderer.setSize(window.innerWidth*0.8, window.innerHeight*0.8);

  map.appendChild(renderer.domElement);

  //>>>>>>>>>>>>>>// RENDER MANAGER

  renderManager = new THREE.Extras.RenderManager(renderer);

  //СЦЕНА 1
  scene = new THREE.Scene();

  //камера
  camera = new THREE.PerspectiveCamera(20, window.innerWidth / window.innerHeight, 0.2, 10000);
  camera.position.set(0, 200, 500);
  scene.add(camera);

  // controls
  controls = new THREE.OrbitControls(camera, renderer.domElement);
  controls.minDistance = 150;
  controls.maxDistance = 1000;

  //освещение
  light = new THREE.PointLight(0xffffff, 1);
  light.position.set(-1, 0, 0);
  scene.add(light);

  createPlanets();

  renderManager.add('sceneMain', scene, camera, function (delta, renderer)
                                                  {renderer.render(this.scene, this.camera);},
                                                {planet: planets });
  //слушатели
  window.addEventListener('resize', onWindowResize, false);
  //document.addEventListener('mousedown', onDocumentMouseDown, false);
}

function planet(plName, array, size){
  var constr = planetsInfo[plName];

  var loader = new THREE.TextureLoader().load(constr.texture);
  var geometry = new THREE.SphereGeometry(size, 32, 32);

  var positionPl = constr.position;

  //солнце создаем отдельно другим материалом, так как его не нужно освещать
  if (plName == "sun") {
    var material = new THREE.MeshBasicMaterial({ map: loader });
  } else{
    material = new THREE.MeshLambertMaterial({ map: loader });
  }

  var newPlanet = new THREE.Mesh(geometry, material);
  newPlanet.position.x = positionPl;
  newPlanet.name = plName;

  array.add(newPlanet);

  //кольцо для Сатурна
  if (plName == "saturn") {
    material = new THREE.MeshLambertMaterial({ color: 0xC0C0C0, side: THREE.DoubleSide });
    geometry = new THREE.RingGeometry(10, 11, 32);
    var satCircle = new THREE.Mesh(geometry, material);
    satCircle.position.set(positionPl, 0, 0);
    satCircle.rotation.y = Math.PI / 8;
    satCircle.rotation.x = Math.PI / 2;
    array.add(satCircle);
  }
}

function orbita(type, name, curscene){
  this.constr = type[name].position;
  this.curve = new THREE.EllipseCurve(
    0, 0, // ax, aY
    this.constr, this.constr, // xRadius, yRadius
    0, 2 * Math.PI, // aStartAngle, aEndAngle
    false, // aClockwise
    0 // aRotation
  );

  this.points = this.curve.getPoints(50);
  this.geometry = new THREE.BufferGeometry().setFromPoints(this.points);
  this.material = new THREE.LineBasicMaterial({ color: 0x404040 });

  this.ellipse = new THREE.Line(this.geometry, this.material);
  this.ellipse.rotation.x = Math.PI / 2;
  curscene.add(this.ellipse);
}

//функция создания планет
function createPlanets() {
  planets = new THREE.Object3D();
  var par = Object.getOwnPropertyNames(planetsInfo);
  for (var i = 0, l = par.length; i < l; i++) {
    planet(par[i], planets, planetsInfo[par[i]].size);
    orbita(planetsInfo, par[i], scene);
  }
  scene.add(planets);
}

//функция слушателя изменения размера экрана
function onWindowResize() {
  camera.aspect = window.innerWidth / window.innerHeight;
  renderer.setSize(window.innerWidth*0.8, window.innerHeight*0.8);
  camera.updateProjectionMatrix();
}

function animate() {
  requestAnimationFrame(animate);
  controls.update();
  renderManager.renderCurrent();
}


/*function render() {
  renderManager.renderCurrentrenderCurrent();
}


/*function onDocumentMouseDown(event) {
  //event.preventDefault();
  mouse.x = event.clientX / window.innerWidth * 2 - 1;
  mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;
  raycaster.setFromCamera(mouse, camera);
  console.log(raycaster.intersectObjects(planets.children));
  var intersects = raycaster.intersectObjects(planets.children);
    if (intersects.length > 0) {
    }

}*/

function setStartPoint(){
  var select = document.getElementById("select_from");
  startName = select.value;
  if(startName in planetsInfo){
    startPoint = planetsInfo[startName].position;
    sizePoint = planetsInfo[startName].size;
  }
}

function setFinishPoint(){
  var select = document.getElementById("select_to");
  finishName = select.value;
  if(finishName in planetsInfo){
    finishPoint = planetsInfo[finishName].position;
  }
}

function addCurve(){
  var newCurve = new THREE.QuadraticBezierCurve(
    new THREE.Vector2( startPoint, sizePoint / 2 + 2 ),
    new THREE.Vector2( (startPoint + finishPoint)/2, Math.abs((startPoint - finishPoint)/2) ),
    new THREE.Vector2( finishPoint, 0 )
  );

  var points = newCurve.getPoints( 50 );
  var geometry = new THREE.BufferGeometry().setFromPoints( points );

  var material = new THREE.LineBasicMaterial( { color : 0xffffff } );

  //Create the final object to add to the scene
  var curveObject = new THREE.Line( geometry, material );
  curveObject.name = 'curve';
  scene.add(curveObject);
}

function removeCurve(){
  var curveObj = scene.getObjectByName("curve");
  scene.remove(curveObj);
}


function setPoint(point){
  var sceneCurve = scene.getObjectByName("curve");
	  if(point == 'start'){
      if(document.getElementById("select_from").value !== "Choose a planet"){
		    if(sceneCurve == null){
          if(curve == null){

			      setStartPoint();

          }else if(document.getElementById("select_from").value !== curve.startName){
            setStartPoint();
            if(finishPoint !== undefined){

              removeCurve();
              addCurve();
            }
          }
		    }else{
          setStartPoint();

          removeCurve();
          addCurve();
        }
      createRing('select_from');
      }
	  }else{
      if(document.getElementById("select_to").value !== "Choose a planet"){
        if(sceneCurve == null){
          if(curve == null){

            setFinishPoint();

          }else if(document.getElementById("select_to").value !== curve.finishName){
            setFinishPoint();
            if(startPoint !== undefined){

              removeCurve();
              addCurve();
            }
          }
        }else{
          setFinishPoint();
          removeCurve();
          addCurve();
        }
      createRing('select_to');
      }
    }
}

function createRing(selectField){
  var select = document.getElementById(selectField);
  var name = select.value;
  if(name in planetsInfo){
    var position = planetsInfo[name].position;
    var size = planetsInfo[name].size + 1;

    var whitePl = scene.getObjectByName(selectField);
    if(whitePl !== null){
      scene.remove(whitePl);
    }

    var material = new THREE.MeshBasicMaterial({ color: 0xffffff, side: THREE.DoubleSide });
    var geometry = new THREE.RingGeometry(size, size + 0.5, 32);
    var ring = new THREE.Mesh(geometry, material);
    ring.position.set(position, 0, 0);
    ring.name = selectField;
    scene.add(ring);
  }

}
