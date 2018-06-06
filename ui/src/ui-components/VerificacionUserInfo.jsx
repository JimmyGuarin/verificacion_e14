import React, {Component} from 'react';
import { Row, Col, Label } from 'react-bootstrap';

export default class VerificacionUserInfo extends Component {
  
  render() { 
    const { totalSospechosos, totalVerificados } = this.props.userInfo;  
    return (
      <div>
        <Row>
          <Col xs={12} md={12}>
            <h3>Verificaciones personales</h3> 
          </Col>
        </Row>
        <Row>
          <Col xs={6} md={6}>
          <div>
            <h4><Label bsStyle="info">Total Verificados</Label></h4>  
            <h4>{totalVerificados}</h4> 
          </div>
          </Col>
          <Col xs={6} md={6}>
            <h4><Label bsStyle="info">Sospechosos</Label></h4>  
            <h4>{totalSospechosos}</h4>
          </Col>
        </Row>
    </div>
    );
  }
}