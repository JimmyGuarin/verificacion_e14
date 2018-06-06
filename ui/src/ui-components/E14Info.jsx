import React, {Component} from 'react';
import { Row, Col, Label } from 'react-bootstrap';

export default class E14Info extends Component {
  
  render() { 
    const { departamento, municipio, zona, puesto } = this.props;  
    return (
      <div>
        <hr/>
        <Row>
        <Col xs={4} md={4}>
          <h3>
            <Label bsStyle="primary">Departamento</Label> {departamento}
          </h3> 
        </Col>
        <Col xs={4} md={4}>
          <h3>
            <Label bsStyle="primary">Municipio</Label> {municipio}
          </h3> 
        </Col>
        <Col xs={2} md={2}>
          <h3>
            <Label bsStyle="primary">Zona</Label> {zona}
          </h3>
        </Col>
        <Col xs={2} md={2}>
          <h3>
            <Label bsStyle="primary">Puesto</Label> {puesto}
          </h3>
        </Col>
      </Row>
      <br/>
      <hr/>
    </div>
    );
  }
}