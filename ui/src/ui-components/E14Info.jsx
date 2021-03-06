import React, {Component} from 'react';
import { Row, Col, Label } from 'react-bootstrap';

export default class E14Info extends Component {
  
  render() { 
    const { departamento, municipio, zona, reportes } = this.props;  
    return (
      <div>
        <Row>
         <Col xs={12} md={12}>
           <h3>Información E14</h3>
         </Col>
        </Row>
        <Row className="e14-info-body">
        <Col xs={3} md={3}>
          <div>
            <h4><Label bsStyle="primary">Departamento</Label></h4>  
            <h4>{departamento}</h4> 
          </div>
        </Col>
        <Col xs={3} md={3}>
          <div>
            <h4><Label bsStyle="primary">Municipio</Label></h4>  
            <h4>{municipio}</h4> 
          </div>
        </Col>
        <Col xs={3} md={3}>
          <div>
            <h4><Label bsStyle="primary">Zona</Label></h4>  
            <h4>{zona}</h4> 
          </div>
        </Col>
        <Col xs={3} md={3}>
          <div>
            <h4><Label bsStyle="primary">Verificaciones</Label></h4>  
            <h4>{reportes}</h4> 
          </div>
        </Col>
        {/* <Col xs={2} md={2}>
          <h3>
            <Label bsStyle="primary">Puesto</Label> {puesto}
          </h3>
        </Col> */}
      </Row>
    </div>
    );
  }
}