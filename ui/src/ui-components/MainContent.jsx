import React, {Component} from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import ContentActions from './ContentActions';
import IframeComponent from './IframeComponent';

export default class MainContent extends Component {
  
  constructor(props) {
    super(props);
  }
  
//TODO CHANGE THE PDF LINK


  render() {
    return (
      <div>
        <div>
          <IframeComponent /> 
        </div> 
        <Row>
          <Col xsOffset={6} xs={6} md={6}> 
            <ContentActions />  
          </Col>  
        </Row>
      </div>  
    );
  }
}