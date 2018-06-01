import React, {Component} from 'react';
import { Row, Col, Button, FormGroup, Form, 
    ControlLabel, FormControl, HelpBlock, Table  } from 'react-bootstrap';

export default class CandidatesSelected extends Component {

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
                  <td>{c.name}</td>
                  <td>{c.votes}</td>
                </tr>);
              })
            }
          </tbody>
        </Table>
        <Row>
          <Col xsOffset={4} xs={2}>
            <Button bsStyle="success" onClick={onSend} id="someButton">
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