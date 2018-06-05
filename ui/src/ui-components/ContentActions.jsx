import React, {Component} from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import ActionButtons from './ActionButtons';
import FraudForm from './FraudForm';

export default class ContentActions extends Component {
  
  constructor(props) {
    super(props);
    this.state = {
      clear: true,
    }
    this.handleSucess = this.handleSucess.bind(this);
    this.handleFraud = this.handleFraud.bind(this);
    this.handleCancelFraud = this.handleCancelFraud.bind(this);
    this.handleSendFraud = this.handleSendFraud.bind(this);
  }
  
  handleSucess = () => {
    const { sendReport} = this.props;
    sendReport([], null);
  }

  handleFraud = () => {
    this.setState({clear: false});
  }

  handleCancelFraud = () => {
    console.log("Cancel handleFraud")
    this.setState({clear: true});
  }
  handleSendFraud = (candidates, captchaCode) => {
    //TODO SENT TO SERVER AND NEXT 
    const { sendReport} = this.props;
    sendReport(candidates, captchaCode);
  }
  
  render() {
    return (
      this.state.clear ?
      <div className= "container-response">
        <ActionButtons 
          handleSucess={this.handleSucess}
          handleFraud={this.handleFraud}
        />
      </div> :
        <FraudForm 
          handleCancelFraud={this.handleCancelFraud}
          handleSendFraud={this.handleSendFraud}
        />
    );
  }
}