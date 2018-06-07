import React, { Component } from 'react';
import NavigationBar from '../ui-components/NavigationBar';
import AuthService from '../services/AuthService';
import { getUserInfo } from "../webapi/endpoints";
import ModalAyuda from '../ui-components/ModalAyuda';

const Auth = new AuthService();

export default function withNavbar(NavBarComponent) {
    return class NavBarWrapped extends Component {
        constructor(props) {
            super(props);
            this.handleLogout = this.handleLogout.bind(this);
            this.updateUserInfo = this.updateUserInfo.bind(this);
            this.closeHelpModal= this.closeHelpModal.bind(this);
            this.modalViewed = false;
            this.state = {
              user: {
                name: "",
                reportes: "",
                sospechosos: ""
              }
            }
        }

        handleLogout(){
          Auth.logout()
          this.props.history.replace('/login');
        }

        updateUserInfo(user) {
          this.setState({user: user});
        }

        closeHelpModal() {
          this.modalViewed = true;
        }
        
        render() {
          return (
            <div>
              <NavigationBar handleLogout={this.handleLogout} user={this.state.user} />  
              <NavBarComponent 
                userInfo={this.state.user} updateUserInfo={this.updateUserInfo} />
                {
                  this.state.user.reportes < 100 && !this.modalViewed ? 
                  <ModalAyuda onHide={this.closeHelpModal}/> : null
                }
            </div>
          )
        }

      componentDidMount() {
        getUserInfo().then(res => {
          let user = res.response;
          this.setState({user: user});
        });
      }
    }
}