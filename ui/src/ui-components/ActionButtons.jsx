import React, {Component} from 'react';
import { Row, Col, Button } from 'react-bootstrap';


export default class ActionButtons extends Component {
  render() {
    const { handleSucess, handleFraud } = this.props;
    return (
      <div>
        <Row>
          <Col xsOffset={2} mdOffset={0} md={4} xsHidden smHidden>
            <Button className="buttons-action" bsStyle="success" onClick={handleSucess} bsSize="large">
              OK
            </Button>
          </Col> 
        </Row>  
        <Row>
          <Col xsOffset={2} mdOffset={0} md={4} xsHidden smHidden>
            <Button className="buttons-action" bsStyle="danger" onClick={handleFraud} bsSize="large">
              Sospechoso
            </Button>
          </Col>  
        </Row>
        <Row>
          <Col xsOffset={1} xs={5} mdHidden lgHidden>
            <Button className="buttons-action" bsStyle="success" onClick={handleSucess} bsSize="large">
              OK
            </Button>
          </Col>
          <Col  xs={5} mdHidden lgHidden>
            <Button className="buttons-action" bsStyle="danger" onClick={handleFraud} bsSize="large">
              Sospechoso
            </Button>
          </Col>  
        </Row>  
      </div>
    );
  }
}
