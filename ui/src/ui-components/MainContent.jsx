import React, {Component} from 'react';
import { Row, Col, Alert } from 'react-bootstrap';
import ContentActions from './ContentActions';
import IframeComponent from './IframeComponent';
import { getNewE14, sendReport } from "../webapi/endpoints";
import E14Info from '../ui-components/E14Info';

export default class MainContent extends Component {
  constructor(props) {
    super(props);
    this.e14File = null;
    this.state = {
      loading: true,
      sendOk: false,
      sendFail: false,
    }
    this.sendToReport = this.sendToReport.bind(this);
    this.fetchE14 = this.fetchE14.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  sendToReport(candidates, captchaCode) {
    this.setState({loading: true});
    const { updateUserInfo, userInfo } = this.props;
    let data = {
      e14Id: this.e14File.id,
      valido: true,
      detalles: candidates
    };
    if (captchaCode) {
      data.valido = false;
      data.captchaToken = captchaCode;
    }
    sendReport(data).then(res => {
      this.setState({sendOk: true});
      userInfo.reportes+=1;
      if (captchaCode)
        userInfo.sospechosos+=1;
        updateUserInfo(userInfo);
      this.fetchE14();
    },
    res => {
      this.setState({sendFail: true, loading: false});
    });
  }

  fetchE14() {
    const { getInfoUbicacion } = this.props;
    getNewE14().then(res => {
      this.e14File  = res.response;
      this.ubicacion = getInfoUbicacion(this.e14File.departamento, this.e14File.municipio);
      this.setState({loading: false});
      
    });
  }

  handleDismiss() {
    this.setState({sendOk: false, sendFail: false});
  }

  render() {
    return (
      this.state.loading ?
      <div>
        <h1>CARGANDO...</h1>
      </div>
      :
      <div>
        <div>
          {
          this.state.sendOk ?
            <Alert bsStyle="success" onDismiss={this.handleDismiss}>
              <h4>Datos enviados, gracias por aportar a tu pais!</h4>
            </Alert>
          :
          this.state.sendFail ?
            <Alert bsStyle="danger" onDismiss={this.handleDismiss}>
              <h4>Se ha producido un error enviando los datos!</h4>
            </Alert>
          : null
          }
        </div>
        <Row>
          <hr/>
          <Col xs={12}>
            <E14Info 
              departamento={this.ubicacion.nombreDepto}
              municipio={this.ubicacion.nombreMun}
              zona={this.e14File.zona}
              reportes={this.e14File.reportes}
            />
          </Col>
        </Row>
        <hr/>
        <Row>
        <Col xs={12} md={6}>
          <IframeComponent
            link={this.e14File.link}/>  
          </Col>
          <Col xs={12} md={6}>
            <ContentActions
              sendReport= {this.sendToReport}/>
          </Col>
        </Row>
      </div>
    );
  }

  componentDidMount() {
    this.fetchE14();
  }
}
