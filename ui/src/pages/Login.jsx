import React, {Component} from 'react';
import loginLogo from '../images/logo_login.png';
import { Row, Col } from 'react-bootstrap';
import GoogleLogin from 'react-google-login';
import AuthService from '../services/AuthService';


export default class Login extends Component {
  constructor(props) {
    super(props);
    this.responseGoogle = this.responseGoogle.bind(this);
    this.Auth = new AuthService();
  }

  responseGoogle(response) {
    console.log("Response", response);
    if (response.code) {
      this.Auth.login(response.code)
        .then(res =>{
            console.log("res", res);
            this.props.history.replace('/');
        })
        .catch(err =>{
            alert(err);
        })
    }    
  }

  render() { 
    return (
      <div>
        <Row>
          <Col xsOffset={4} xs={3} md={4}>
            <h1 align="center">Verificación E14</h1>
          </Col>
        </Row>
        <div>
        <img  width="450" height="300"  src={loginLogo} alt="Scala Logo" />
        </div>
        <Row>
          <Col xsOffset={5} xs={2} md={4}>
          <GoogleLogin
            clientId="657340641723-3anqvj67ckk4pf8ju9f8no0hapcddpdr.apps.googleusercontent.com"
            buttonText="Login"
            responseType='code'
            onSuccess={this.responseGoogle}
            onFailure={this.responseGoogle} /> 
          </Col>
        </Row>
      </div>
     
    );
  }

  componentWillMount(){
    if(this.Auth.loggedIn())
      this.props.history.replace('/');
  }

}