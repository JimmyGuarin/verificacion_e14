import React, { Component } from 'react';
import NavigationBar from '../ui-components/NavigationBar';
import AuthService from '../services/AuthService';
import { getUserInfo } from "../webapi/endpoints";

const Auth = new AuthService();

export default function withNavbar(NavBarComponent) {
    return class NavBarWrapped extends Component {
        constructor(props) {
            super(props);
            this.state = {
              name: ""
            }
            this.handleLogout = this.handleLogout.bind(this);
        }

        handleLogout(){
          Auth.logout()
          this.props.history.replace('/login');
        }

        render() {
          return (
            <div>
              <NavigationBar handleLogout={this.handleLogout} userName={this.state.name} />  
              <NavBarComponent/>
            </div>
            
          )
        }

      componentDidMount() {
        getUserInfo().then(res => {
          this.setState({name: res.response.name});
        });
      }
    }
}