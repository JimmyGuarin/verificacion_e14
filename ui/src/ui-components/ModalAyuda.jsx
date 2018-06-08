import React, {Component} from 'react';
import { Modal, Button, Carousel } from 'react-bootstrap';
import step1 from '../images/caraousel-help/step1.png';
import step2 from '../images/caraousel-help/step2.png';
import step3 from '../images/caraousel-help/step3.png';
import step3_2 from '../images/caraousel-help/step3_2.png';
import step4 from '../images/caraousel-help/step4.png';
import step5 from '../images/caraousel-help/step5.png';
import step6 from '../images/caraousel-help/step6.png';
import step7 from '../images/caraousel-help/step7.png';
import step8 from '../images/caraousel-help/step8.png';
import step9 from '../images/caraousel-help/step9.png';
import step11 from '../images/caraousel-help/step11.png';
import step12 from '../images/caraousel-help/step12.png';
import step13 from '../images/caraousel-help/step13.png';
import step14 from '../images/caraousel-help/step14.png';


export default class ModalAyuda extends Component {

    constructor(props, context) {
      super(props, context);
      this.handleClose = this.handleClose.bind(this);
      this.handleSelect = this.handleSelect.bind(this);
      this.state = {
        show: true,
        index: 0,
        direction: null
      };
      this.carouselText = [
        "",
        "Abajo encontrarás el E14 y dos opciones para validarlo: OK y Sospechoso",
        "Si seleccionas la opción Sospechoso se mostrará un formulario para especificar los detalles",
        "En la parte de arriba del formulario seleccionas el candidato y la cantidad de votos que son sospechosos",
        "Luego registras los votos sospechosos para ese candidato",
        "Más abajo estará la información de votos sospechosos registrados para cada candidato en el E14 actual",
        "Selecciona y completa el captcha",
        "Luego de completar el captcha se podrá enviar la verificación para ese E14",
        "Presiona el botón Enviar Verificación para terminar el proceso",
        "Luego de enviar la verificación se cargará otro E14",
        "Sección donde encontrarás la información correspondiente al E14 que se verificará incluida la cantidad de veces que ha sido revisado por otras personas",
        "Porcentaje total de formularios E14 que han sido revisados por la comunidad",
        "Seccíon donde podrás ver la cantidad total de E14 que llevas revisados y la cantidad de enviados como sospechosos"
      ];


    }

    handleClose() {
      const { onHide } = this.props;
      onHide();
      this.setState({ show: false });
    }

    handleSelect(selectedIndex, e) {
      this.setState({
        index: selectedIndex,
        direction: e.direction
      });
    }

    render() {
      const { show, index, direction } = this.state;
      return (
        <div>
          <Modal
            show={show}
            onHide={this.handleClose}
            bsSize="large"
            aria-labelledby="contained-modal-title-lg">
            <Modal.Header closeButton>
              <Modal.Title id="contained-modal-title-lg">Modal heading</Modal.Title>
            </Modal.Header>
            <Modal.Body>
            <div>
              <Carousel
                activeIndex={index}
                direction={direction}
                onSelect={this.handleSelect}>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step1} alt="2"/>
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step2} alt="2"/>
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step3} alt="3" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step3_2} alt="3" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step4} alt="4" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step5} alt="5" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step6} alt="6" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step7} alt="7" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step8} alt="8" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step9} alt="9" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step11} alt="9" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step12} alt="9" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step13} alt="9" />
                </Carousel.Item>
                <Carousel.Item>
                    <img width="100%" height="auto" src={step14} alt="9" />
                </Carousel.Item>
                </Carousel>
              </div>
            </Modal.Body>
            <Modal.Footer>
              <h4>{this.carouselText[index]}</h4>
              <Button onClick={this.handleClose}>Close</Button>
            </Modal.Footer>
          </Modal>
        </div>
      );
    }
  }
