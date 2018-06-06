import React, { Component } from 'react';
import NavigationBar from '../ui-components/NavigationBar';
import AuthService from '../services/AuthService';
import { getUserInfo } from "../webapi/endpoints";

const Auth = new AuthService();

export default function withNavbar(NavBarComponent) {
    return class NavBarWrapped extends Component {
        constructor(props) {
            super(props);
            this.handleLogout = this.handleLogout.bind(this);
            this.updateUserInfo = this.updateUserInfo.bind(this);
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

        render() {
          return (
            <div>
              <NavigationBar handleLogout={this.handleLogout} user={this.state.user} />  
              <NavBarComponent 
                userInfo={this.state.user} updateUserInfo={this.updateUserInfo} />
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