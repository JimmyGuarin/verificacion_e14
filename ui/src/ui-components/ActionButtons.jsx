import React, {Component} from 'react';
import { Row, Col, Button } from 'react-bootstrap';


export default class ActionButtons extends Component {
  render() {
    const { handleSucess, handleFraud } = this.props;
    return (
      <Row>
        <Button bsStyle="success" onClick={handleSucess} bsSize="large">
          Limpio
        </Button>
        <Button bsStyle="danger" onClick={handleFraud} bsSize="large">
          Sospechoso
        </Button>
      </Row>
    );
  }
}
