import React, {Component} from 'react';
import { Route, Switch } from 'react-router-dom';

import { Redirect } from 'react-router';
import Validador from './Validador';
import QuienesSomos from './QuienesSomos';
import NavigationBar from '../ui-components/NavigationBar';
import AuthService from '../services/AuthService';
import { getUserInfo } from "../webapi/endpoints";
import ModalAyuda from '../ui-components/ModalAyuda';
const Auth = new AuthService();

class AppContent extends Component {
    constructor(props) {
        super(props);
        this.handleLogout = this.handleLogout.bind(this);
        this.updateUserInfo = this.updateUserInfo.bind(this);
        this.showHelpModal = this.showHelpModal.bind(this);
        this.closeHelpModal= this.closeHelpModal.bind(this);
        this.state = {
          user: {
            name: "",
            reportes: "",
            sospechosos: ""
          },
          showHelpModal: false
        }
    }

    handleLogout(){
       Auth.logout()
      this.props.history.replace('/login');
    }

    updateUserInfo(user) {
       this.setState({user: user});
    }

    showHelpModal() {
       this.setState({showHelpModal: true});
    }

    closeHelpModal() {
      this.setState({showHelpModal: false});
    }

  render() {
    const props = {
      userInfo: this.state.user,
      updateUserInfo: this.updateUserInfo
    }

    return (
      <div>
          <NavigationBar
            showHelpModal={this.showHelpModal}
            handleLogout={this.handleLogout}
            user={this.state.user}/>
          <Switch>
            <Route exact path="/verificar" render={() => (<Validador {...props} />)}/>
            <Route exact path="/quienesomos" component={QuienesSomos}/>
            <Redirect to="/verificar"/>
          </Switch>
          {
            (this.state.showHelpModal) ?
            <ModalAyuda onHide={this.closeHelpModal}/> : null
          }
      </div>
    );
  }

  componentDidMount() {
    console.log("componentDidMount Content");
    getUserInfo().then(res => {
      let user = res.response;
      this.setState({user: user});
    });
  }

}
export default AppContent;
