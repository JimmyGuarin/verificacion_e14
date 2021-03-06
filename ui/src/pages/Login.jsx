import React, {Component} from 'react';
import loginLogo from '../images/logo_login.png';
import { Row, Col } from 'react-bootstrap';
import GoogleLogin from 'react-google-login';
import AuthService from '../services/AuthService';
import TitleApp from '../ui-components/TitleApp';

export default class Login extends Component {
  constructor(props) {
    super(props);
    this.responseGoogle = this.responseGoogle.bind(this);
    this.Auth = new AuthService();
  }

  responseGoogle(response) {
    if (response.code) {
      this.Auth.login(response.code)
        .then(res =>{
            console.log("res", res);
            this.props.history.push({
              pathname: '/',
              state: { fromLogin: true }
            });
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
          <Col xsOffset={2} mdOffset={4} xs={8} md={4}>
            <TitleApp />
          </Col>
        </Row>
        <br/>
        <Row>
          <Col xsOffset={2} mdOffset={4} xs={8} md={4}>
            <img  width="100%" height="auto"  src={loginLogo} alt="Scala Logo" />
          </Col>
        </Row>
        <br/>
        <Row>
          <Col xsOffset={4} mdOffset={5} xs={4} md={2} className="container-button-login">
          <GoogleLogin
            clientId="657340641723-3anqvj67ckk4pf8ju9f8no0hapcddpdr.apps.googleusercontent.com"
            buttonText="Login"
            responseType='code'
            onSuccess={this.responseGoogle}
            onFailure={this.responseGoogle} />
          </Col>
        </Row>
        <br/>
      </div>

    );
  }

  componentWillMount(){
    if(this.Auth.loggedIn())
      this.props.history.replace('/');
  }
}
