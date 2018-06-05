import React, {Component} from 'react';
import { Row, Col, Button, Alert } from 'react-bootstrap';
import ContentActions from './ContentActions';
import IframeComponent from './IframeComponent';
import { getNewE14, sendReport } from "../webapi/endpoints";

import { GoogleLogin, GoogleLogout } from 'react-google-login';

export default class MainContent extends Component {
  constructor(props) {
    super(props);
    this.e14File = null;
    this.state = {
      loading: true,
      sendOk: false,
      sendFail: false,
    }
    this.sendToReport = this.sendToReport.bind(this);
    this.fetchE14 = this.fetchE14.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  sendToReport(candidates, captchaCode) {
    //senddata
    this.setState({loading: true});
    let data = {
      e14Id: this.e14File.id,
      valido: true,
      detalles: candidates
    };
    if (candidates) {
      data.valido = false;
      data.captchaToken = captchaCode;
    }
    sendReport(data).then(res => {
      this.setState({sendOk: true});
      this.fetchE14();
    },
    res => {
      console.log(res);
      this.setState({sendFail: true, loading: false});
    });
  }

  fetchE14() {
    getNewE14().then(res => {
      this.e14File  = res.response;
      this.setState({loading: false});
    });
  }

  handleDismiss() {
    this.setState({sendOk: false, sendFail: false});
  }

  responseGoogle(response) {
    console.log("Response", response);
  }

  logout(data){
    console.log("Logout success", data);
  }


//TODO CHANGE THE PDF LINK
  render() {
    console.log("render", this.e14File);
    return (
      this.state.loading ?
      <div>
        <h1>CARGANDO...</h1>
        <GoogleLogin
                    clientId="188546035076-p80fv1c9d35a6ra3ogeu6k0n03836v2a.apps.googleusercontent.com"
                    buttonText="Login"
                    responseType='code'
                    onSuccess={this.responseGoogle}
                    onFailure={this.responseGoogle} />
        <GoogleLogout
              buttonText="Logout"
              onLogoutSuccess={this.logout}
            >
            </GoogleLogout>
      </div>
      :
      <div>
        <div>
          {
          this.state.sendOk ?
            <Alert bsStyle="success" onDismiss={this.handleDismiss}>
              <h4>Datos enviados, gracias por aportar a tu pais!</h4>
            </Alert>
          :
          this.state.sendFail ?
            <Alert bsStyle="danger" onDismiss={this.handleDismiss}>
              <h4>Se ha producido un error enviando los datos!</h4>
            </Alert>
          : null
          }
        </div>
        <div>
          <IframeComponent
            link={this.e14File.link}/>
        </div>
        <Row>
          <Col xsOffset={6} xs={6} md={6}>
            <ContentActions
              sendReport= {this.sendToReport}/>
          </Col>
        </Row>
      </div>
    );
  }

  componentDidMount() {
    this.fetchE14();
  }
}
