import React, {Component} from 'react';
import { Row, Col, Button, FormGroup, Form, 
    ControlLabel, FormControl, HelpBlock, Table  } from 'react-bootstrap';
import ReCAPTCHA   from 'react-google-recaptcha';   

export default class CandidatesSelected extends Component {

  constructor(props) {
    super(props);
    this.handleReCaptcha = this.handleReCaptcha.bind(this);
    this.tryToSend = this.tryToSend.bind(this);
    this.captcha = undefined;
  }

  handleReCaptcha(value) {
    console.log("handleReCaptcha", value);
    this.captcha = value;
  }

  tryToSend() {
    const { onSend } = this.props;
    if (this.captcha) {
      onSend();
    } else {
      alert("Completa el captcha");
    }
  }


  render() {
    const { candidatesAdded, onSend, onCancel } = this.props;  
    return (
      <div>
        <h3>Candidatos Agregados</h3>
        <Table bordered condensed>
          <thead className="thead-dark">
            <tr>
              <th>Candidatos</th>
              <th>Votos</th>
            </tr>
          </thead>
          <tbody>
            {
              candidatesAdded.map(c => {
                return (<tr>
                  <td>{c.nombre}</td>
                  <td>{c.votosSospechosos}</td>
                </tr>);
              })
            }
          </tbody>
        </Table>
        <div>
        <ReCAPTCHA
          ref="recaptcha"
          sitekey="6Ld9zlwUAAAAAOgvixsDnQsvIbguqleE05LwHSG5"
          onChange={this.handleReCaptcha}
        />
        <hr/>
        </div>
        <Row>
          <Col xsOffset={4} xs={2}>
            <Button bsStyle="success" onClick={this.tryToSend} id="someButton">
              Enviar
            </Button>
          </Col>
          <Col xs={2}>
            <Button bsStyle="danger" onClick={onCancel} id="someButton">
              Cancelar
            </Button>
          </Col>
        </Row>
      </div>
    );
      
  }

}