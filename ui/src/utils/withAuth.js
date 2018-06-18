import React, { Component } from 'react';
import AuthService from '../services/AuthService';
import { prefixRoute } from '../webapi/endpoints';

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
                this.props.history.replace(prefixRoute + '/login')
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
                    this.props.history.replace(prefixRoute + '/login')
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