import React, {Component} from 'react';
import Iframe from 'react-iframe';

export default class IframeComponent extends Component {

  


  render() {
    return (
        <Iframe url="https://visor.e14digitalizacion.com/e14_divulgacion/01/001/001/PRE/3279017_E14_PRE_X_01_001_001_XX_01_005_X_XXX.pdf"
        position="absolute"
        width="50%"
        height="450px"
        id="myId"
        className="myClassname"/>
    );
  }
}