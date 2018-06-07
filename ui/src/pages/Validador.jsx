import React, {Component} from 'react';
import _ from 'lodash';
 
import { Row, Col} from 'react-bootstrap';
import MainContent from '../ui-components/MainContent';
import VerificadosTotales from '../ui-components/VerificadosTotales';
import withNavBar from '../utils/withNavbar';
import { getDepartamentos, getMunicipios} from '../webapi/endpoints';

class Validador extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
    };
    this.getInfoUbicacion = this.getInfoUbicacion.bind(this);
  }

  render() {
    const { ...rest } = this.props;
    return (
      this.state.loading ?
      <h1 align="center">Cargando información de Departamentos...</h1>
      :
      <div className="App">
        <Row>
          <Col xsOffset={0} xs={12}>
            <h1 align="center">Verificación E14</h1>
            <VerificadosTotales />
          </Col>
        </Row>
        <MainContent getInfoUbicacion={this.getInfoUbicacion} {...rest}/>
      </div>
    );
  }

  getInfoUbicacion(codDepto, codMun) {
    let nombreMun = "";
    if (this.municipios[codDepto] && this.municipios[codDepto][codMun] 
      && this.municipios[codDepto][codMun].nombre) 
      nombreMun = this.municipios[codDepto][codMun].nombre;
    return ({
      nombreDepto: this.departamentos[codDepto].nombre,
      nombreMun: nombreMun
    });
  }

  componentWillMount() {
    getDepartamentos().then(res => {
       this.departamentos = _.keyBy(res.response, 'codigo');
      getMunicipios().then(res => {
         this.municipios = _.mapValues(_.groupBy(res.response, 'codigoDepto'), (seq) => {
          return _.keyBy(seq, 'codigo');
        });
        this.setState({loading: false});
      });
    });
  }
}

export default Validador;

