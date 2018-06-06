import React, {Component} from 'react';

import { getPorcentajeTotal } from '../webapi/endpoints';

export default class VerificadosTotales extends Component {
    constructor(props) {
      super(props);
      this.state = {
        verificados: 0
      };
      this.obtenerNumeroVerificados = this.obtenerNumeroVerificados.bind(this);
    }

    obtenerNumeroVerificados() {
        getPorcentajeTotal().then(res => {
           this.setState({verificados: this.state.verificados});
           console.log("RES", res.response.verificados);
        })
      }

    render() {
     return <h5>Total Verificados: {this.state.verificados * 100}%</h5>
    }

    componentDidMount() { 
      setInterval(this.obtenerNumeroVerificados, 60000);
    }
}