import React, {Component} from 'react';
import Iframe from 'react-iframe';

import {
  BrowserRouter as Router,
  Route,
  Link, Switch, 
} from 'react-router-dom';

import { Redirect } from 'react-router';

import reactLogo from './images/react.svg';
import playLogo from './images/play.svg';
import scalaLogo from './images/scala.png';
import TestJsx from './Test';
import { Row, Col } from 'react-bootstrap';
import MainContent from './ui-components/MainContent';
import Home from './pages/Home';
import Login from './pages/Login';
import Validar from './pages/Validar';

import './App.css';

const Tech = ({ match }) => {
  return <div>Current Route: {match.params.tech}</div>
};


class App extends Component {
  constructor(props) {
    super(props);
    this.state = {title: ''};
  }

  render() {
    return (
      <Switch>
        <Route exact path="/" component={Home}/>
        <Route exact path="/login" component={Login}/>
        <Route exact path="/validar" component={Validar}/>
        <Redirect to="/"/>
      </Switch>
    );
  }
}
export default App;
