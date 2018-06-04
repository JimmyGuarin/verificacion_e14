import React, {Component} from 'react';
import Iframe from 'react-iframe';

import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom';

import reactLogo from './images/react.svg';
import playLogo from './images/play.svg';
import scalaLogo from './images/scala.png';
import TestJsx from './Test';
import { Row, Col } from 'react-bootstrap';
import MainContent from './ui-components/MainContent';


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
      <Router>
        <div className="App">
          <h1>Verification E14</h1>
          <MainContent />
        </div>
      </Router>
    );
  }
}
export default App;
