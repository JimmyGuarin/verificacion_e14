import React, {Component} from 'react';
import {
  BrowserRouter as Router,
  Route,
  Link, Switch, 
} from 'react-router-dom';

import { Redirect } from 'react-router';
import Home from './pages/Home';
import Login from './pages/Login';
import Validador from './pages/Validador';
import './App.css';
import withAuth from './utils/withAuth';
import AuthService from './services/AuthService';

const Auth = new AuthService();
const Tech = ({ match }) => {
  return <div>Current Route: {match.params.tech}</div>
};


class App extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Switch>
        <Route exact path="/login" component={Login}/>
        <Route exact path="/verificar" component={withAuth(Validador)}/>
        <Redirect to="/verificar"/>
      </Switch>
    );
  }

  

}
export default App;
