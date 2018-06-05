import React, {Component} from 'react';

import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom';

import { Redirect } from 'react-router';

import { Row, Col } from 'react-bootstrap';
import MainContent from '../ui-components/MainContent';
import withAuth from '../utils/withAuth';

export default class Validador extends Component {
  constructor(props) {
    super(props);
    this.state = {title: ''};
  }

  render() {
    const { handleLogout } = this.props;
    return (
      <div className="App">
        <h1>Verificación E14</h1>
        <MainContent handleLogout={handleLogout}/>
      </div>
    );
  }

}

