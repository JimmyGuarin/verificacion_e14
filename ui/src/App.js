import React, {Component} from 'react';
import { Route, Switch } from 'react-router-dom';

import { Redirect } from 'react-router';
import Login from './pages/Login';
import Validador from './pages/Validador';
import './App.css';
import withAuth from './utils/withAuth';

class App extends Component {
  
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
