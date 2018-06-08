import React, { Component } from 'react';
import AuthService from '../services/AuthService';

export default function withAuth(AuthComponent) {
    const Auth = new AuthService();
    return class AuthWrapped extends Component {
        constructor() {
            super();
            this.state = {
                user: null
            }
        }
        componentWillMount() {
            if (!Auth.loggedIn()) {
                this.props.history.replace('/login')
            }
            else {
                try {
                    const profile = Auth.getProfile()
                    this.setState({
                        user: profile
                    })
                }
                catch(err){
                    Auth.logout()
                    this.props.history.replace('/login')
                }
            }
        }

        render() {
            const { user } = this.state;
            const { history } = this.props;
            if (user) {
              return (<AuthComponent history={history} user={user} />)
            }
            else {
                return null
            }
        }
    };
}