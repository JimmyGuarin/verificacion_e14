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
           this.setState({verificados: parseFloat(res.response.verificados)});
        })
      }

    render() {
     return <h5>Total Verificados: {(this.state.verificados * 100).toFixed(3)}%</h5>
    }

    componentDidMount() {
      setInterval(this.obtenerNumeroVerificados, 60000);
    }
}
