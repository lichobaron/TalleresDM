function cambiarColor(elemento, color){
  elemento.style.color = color;
}

function generarColor(){
  var posibilidades=["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"];
  var rojo = posibilidades[aleatorio()] + posibilidades[aleatorio()];
  var verde = posibilidades[aleatorio()] + posibilidades[aleatorio()];
  var azul = posibilidades[aleatorio()] + posibilidades[aleatorio()];
  var color = "#" + rojo + verde + azul;
  return color;
}

function aleatorio(){
  return Math.floor((Math.random()*16));
}

var mostrarMsj = function(texto){
  alert(texto);
  prompt("Como va todo?");
}

window.onload = function(){
  console.log("Pagina Cargada!");
  var primerParrafo = document.querySelector("p");
  primerParrafo.addEventListener("click", function(){
    mostrarMsj("click sobre el parrafo");
  });
  primerParrafo.addEventListener("mouseover", function(){
    cambiarColor(this, generarColor());
  });
  primerParrafo.addEventListener("mouseout", function(){
    cambiarColor(this, generarColor());
  });
};
