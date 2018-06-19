import React, {Component} from 'react';
import { Route, Switch} from 'react-router-dom';

import { Redirect } from 'react-router';
import Login from './pages/Login';
import AppContent from './pages/AppContent';
import './App.css';
import withAuth from './utils/withAuth';

class App extends Component {
  
  render() {
    return (
      <Switch>
        <Route exact path="/login" component={Login}/>
        <Route path="/" component={withAuth(AppContent)}/>
        <Redirect to="/verificar"/>
      </Switch>
    );
  }
}

export default App;
