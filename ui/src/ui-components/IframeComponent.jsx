import React, {Component} from 'react';
import Iframe from 'react-iframe';

export default class IframeComponent extends Component {

  


  render() {
    const { link } = this.props;
    return (
        <Iframe url={link}
        position="absolute"
        width="50%"
        height="450px"
        id="myId"
        className="myClassname"/>
    );
  }
}