import React, {Component} from 'react';
import Iframe from 'react-iframe';
import { Row, Col, Button } from 'react-bootstrap';

export default class IframeComponent extends Component {

  constructor(props) {
    super(props);
    this.openExternal = this.openExternal.bind(this);
    this.modalWindows = null;
  }

  openExternal() {
    const { link } = this.props;
    this.modalWindows = window.open(link, "", "width=500,height=500");
  }


  componentWillUnmount() {
   if(this.modalWindows) {
    this.modalWindows.close();
   }

  }


  render() {
    const { link } = this.props;

    return (
      <div>
        <Row>
          <Col xsOffset={0} xs={12} mdOffset={4} md={4}>
           <Button bsStyle="link" onClick={this.openExternal}>Abrir en otra ventana</Button>
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
              <Iframe url={"http://docs.google.com/gview?url="+link+"&embedded=true"}
            position="absolute"
            width="100%"
            height="450px"
            id="myId"
            className="myClassname"/>
            <div style={{marginTop: '200px'}}>
              <a href={link} target="_blank">Vista previa no disponible</a>
            </div>
          </Col>
        </Row>
      </div>
    );
  }
}
