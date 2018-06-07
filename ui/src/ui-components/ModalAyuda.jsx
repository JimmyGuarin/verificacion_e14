import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';

export default class ModalAyuda extends Component {

    constructor(props, context) {
      super(props, context);
      this.handleClose = this.handleClose.bind(this);
      this.state = {
        show: true
      };
    }

    handleClose() {
      const { onHide } = this.props;
      onHide();
      this.setState({ show: false });
    }

    render() {
        
      return (
        <div>
          <Modal show={this.state.show} onHide={this.handleClose}>
            <Modal.Header closeButton>
              <Modal.Title>Modal heading</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <h4>Text in a modal</h4>
            </Modal.Body>
            <Modal.Footer>
              <Button onClick={this.handleClose}>Close</Button>
            </Modal.Footer>
          </Modal>
        </div>
      );
    }
  }