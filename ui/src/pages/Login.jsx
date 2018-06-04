import React, {Component} from 'react';

import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom';

import { Redirect } from 'react-router';

import { Row, Col } from 'react-bootstrap';
import MainContent from '../ui-components/MainContent';
import GoogleLogin from 'react-google-login';


export default class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {title: ''};
  }

  render() {

    const redirect = false;
    if (redirect) {
      return (
      <Router>
        <Redirect to='/login'/>
      </Router>);
    } 
    return (
      <GoogleLogin
      clientId="188546035076-p80fv1c9d35a6ra3ogeu6k0n03836v2a.apps.googleusercontent.com"
      buttonText="Login"
      accessType="online"
      onSuccess={this.responseGoogle}
      onFailure={this.responseGoogle} />
    );
  }
}