//https://hptechblogs.com/using-json-web-token-react/
import decode from 'jwt-decode';

export function getToken() {
    // Retrieves the user token from localStorage
    return localStorage.getItem('id_token')
}

export default class AuthService {
    // Initializing important variables
    constructor(domain) {
        this.domain = domain || 'http://localhost:3000/api' // API server domain
        this.fetch = this.fetch.bind(this) // React binding stuff
        this.login = this.login.bind(this)
        this.getProfile = this.getProfile.bind(this)
    }

    login(googleCode) {
        // Get a token from api server using the fetch api
        var url = new URL(`${this.domain}/signin`),
        params = {code:googleCode}
        Object.keys(params).forEach(key => url.searchParams.append(key, params[key]))
        return this.fetch(url, {
            method: 'POST',
            body: JSON.stringify({
                code: googleCode,
            })
        }).then(res => {
            console.log("res.Authorization", res);
            this.setToken(res.Authorization) // Setting the token in localStorage
            return Promise.resolve(res.Authorization);
        })
    }

    loggedIn() {
        // Checks if there is a saved token and it's still valid
        const token = getToken() // GEtting token from localstorage
        return !!token && !this.isTokenExpired(token) // handwaiving here
    }

    isTokenExpired(token) {
        try {
            const decoded = decode(token);
            if (decoded.exp < Date.now() / 1000) { // Checking if token is expired. N
                return true;
            }
            else
                return false;
        }
        catch (err) {
            return false;
        }
    }

    setToken(idToken) {
        // Saves user token to localStorage
        localStorage.setItem('id_token', idToken)
    }

    logout() {
        // Clear user token and profile data from localStorage
        localStorage.removeItem('id_token');
    }

    getProfile() {
        // Using jwt-decode npm package to decode the token
        return decode(getToken());
    }


    fetch(url, options) {
        // performs api calls sending the required authentication headers
        const headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }

        // Setting Authorization header
        // Authorization: Bearer xxxxxxx.xxxxxxxx.xxxxxx
        if (this.loggedIn()) {
            headers['Authorization'] = 'Bearer ' + getToken()
        }

        return fetch(url, {
            headers,
            ...options
        })
            .then(this._checkStatus)
            .then(response => response.json())
    }

    _checkStatus(response) {
        // raises an error in case response status is not a success
        if (response.status >= 200 && response.status < 300) { // Success status lies between 200 to 300
            return response
        } else {
            var error = new Error(response.statusText)
            error.response = response
            throw error
        }
    }
}