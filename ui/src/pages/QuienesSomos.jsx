import React, {Component} from 'react';
import rysoftLogo from '../images/rysoftLogo.jpeg';

export default class QuienesSomos extends Component {
  constructor(props) {
    super(props);
    this.state = {title: ''};
  }

  render() {
    return (
      <div>
          <img  width="50%" height="auto"  src={rysoftLogo} alt="Rysoft Logo" />
          <br/>
          <h5 className= "text-about" align="center">
En Rysoft creemos que la tecnología debe estar al servicio de la sociedad y por lo tanto trabajamos
para empoderar a la ciudadanía con herramientas tecnológicas que permitan realizar cambios que de otra
forma no serían posible. </h5>
<h5 className= "text-about" align="center">
En el contexto de la primera vuelta presidencial, y debido a las controversias que se levantaron
por la circulación en redes sociales de formularios E14 aparentemente alterados decidimos realizar la
aplicación TRANSPARENCIA ELECTORAL, la cual está pensada para que de forma colaborativa los
ciudadanos podamos determinar con mayor exactitud cuántos formularios presentan anomalías y a qué candidatos
afectan.
</h5>

<h5 className= "text-about" align="center">
La aplicación surge como un ejercicio de Crowd Computing y participación democrática ciudadana y su propósito es realizar,
con la ayuda de todo el público voluntario, una verificación masiva de los formularios E14 abiertamente disponibles desde
la registraduría, para detectar cuantos formularios y votos presentan anomalías.
</h5>

<h5 className= "text-about" align="center">
El crowd computing define el conjunto de herramientas que permiten la utilización del "superávit cognitivo",
la habilidad de la población mundial de colaborar en grandes proyectos, permiten coordinar el uso de inteligencia humana para
realizar tareas que los computadores no pueden hacer actualmente.
La aplicación es muy sencilla de usar, desde un computador, Tablet o movil y con una cuenta de Google cualquier persona puede participar aportando su juicio sobre
la validez de los formularios E14 publicados por la registraduría.
</h5>
<h5  className= "text-about" align="center">Entre más personas participen mejores resultados podremos obtener, ¡difunde y comparte!</h5>
</div>
    );
  }
}
