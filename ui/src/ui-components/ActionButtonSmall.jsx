import React, {Component} from 'react';
import { Row, Col, Button } from 'react-bootstrap';


export default class ActionButtons extends Component {
  render() {
    const { handleSucess, handleFraud } = this.props;
    return (
        <Row>
          <Col xsOffset={2}  xs={4}>
            <Button className="buttons-action" bsStyle="success" onClick={handleSucess} bsSize="large">
              Limpio
            </Button>
          </Col>  
          <Col xs={4}>
            <Button className="buttons-action" bsStyle="danger" onClick={handleFraud} bsSize="large">
              Sospechoso
            </Button>
          </Col>  
        </Row>  
    );
  }
}