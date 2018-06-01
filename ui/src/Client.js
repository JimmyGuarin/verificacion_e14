/* eslint-disable no-undef */
import  LoginWithTwitter from 'login-with-twitter';

const cb = (b) => {
  console.log(b);
}

const tw = new LoginWithTwitter({
  consumerKey: 'bFSpZOQdH1DCLCFkNY7FgW181',
  consumerSecret: 'R1ael71lFeX0zlbu9NJ8wZe2lLZ3ILwpO6TEMRV3Dbeu7pZrVq',
  callbackUrl: 'https://example.com/twitter/callback'
});



console.log("tw", tw);

console.log("tw2", tw.login(cb));



function getSummary(cb) {
  return fetch('/summary', {
    accept: "application/json"
  })
    .then(checkStatus)
    .then(parseJSON)
    .then(cb);
}

function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }
  const error = new Error('HTTP Error ${response.statusText}');
  error.status = response.statusText;
  error.response = response;
  console.log(error); // eslint-disable-line no-console
  throw error;
}

function parseJSON(response) {
 return response
}

const Client = { getSummary };
export default Client;
