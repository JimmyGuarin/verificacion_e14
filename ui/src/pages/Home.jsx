import React, {Component} from 'react';

import {
  BrowserRouter as Router,
} from 'react-router-dom';

import { Redirect } from 'react-router';
import MainContent from '../ui-components/MainContent';


export default class Home extends Component {
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
      <div className="App">
        <h1>Verification E14</h1>
        <MainContent />
      </div>
    );
  }
}